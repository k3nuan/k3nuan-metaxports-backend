package com.meta.sports;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meta.sports.catalog.domain.Catalog;
import com.meta.sports.events.EventsService;
import com.meta.sports.events.domain.Event;
import com.meta.sports.events.utils.EventSource;
import com.meta.sports.global.Constants;
import com.meta.sports.global.domain.ApiResponse;
import com.meta.sports.global.exceptions.BadRequestException;
import com.meta.sports.global.exceptions.NotFoundException;
import com.meta.sports.user.application.port.in.AuthUser;
import com.meta.sports.user.application.port.in.FindUser;
import com.meta.sports.user.domain.User;
import com.meta.sports.user.domain.UserAuthoritySwitcher;
import com.meta.sports.user.domain.UserSession;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

@SpringBootApplication
@EnableScheduling
public class MetaSportsApplication {

    private static final Logger logger = LoggerFactory.getLogger(MetaSportsApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MetaSportsApplication.class, args);
        logger.info("Meta Sports API on execution");
    }


    @Configuration
    public static class AuthenticationConfiguration extends GlobalAuthenticationConfigurerAdapter {

        @Autowired
        private ApiAuthenticationProvider apiAuthenticationProvider;

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            auth.authenticationProvider(apiAuthenticationProvider);
        }

        @Component
        protected class ApiAuthenticationProvider implements AuthenticationProvider {

            @Autowired
            private AuthUser authUser;

            @Autowired
            private FindUser findUser;

            @Autowired
            private HttpServletRequest request;

            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {

                User user;
                String origin = getOriginAddress(request);

                try {
                    user = authUser.authenticate(authentication.getName(), (String) authentication.getCredentials(), origin);
                } catch (NotFoundException e) {
                    throw new UsernameNotFoundException("user " + authentication.getName().toLowerCase() + " not found");
                }

                request.setAttribute(Constants.REQUEST_USER_ID, user.getId());

                //user authorities
                List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
                grantedAuthorities.add(new SimpleGrantedAuthority(Constants.ROLE_PREFIX + user.getRole()));


                //user principal authority
                List<UserAuthoritySwitcher> userPrincipalAuthority = new ArrayList<>();
                userPrincipalAuthority.add(new UserAuthoritySwitcher(Constants.ROLE_PREFIX + user.getRole(), grantedAuthorities));

                UserSession userSession = new UserSession(user.getUsername(), user.getPassword(), userPrincipalAuthority);
                userSession.setId(user.getId());
                userSession.setOrigin(origin);

                UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(userSession, authentication.getCredentials(), userPrincipalAuthority);

                //set authentication for reference
                ((UserAuthoritySwitcher)result.getAuthorities().stream().findFirst().get()).setSource(result);

                return result;
            }
    
            @Override
            public boolean supports(Class<?> authentication) {
                return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
            }

        }

    }

    @Configuration
    @EnableWebSecurity
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    public static class AuthorizationConfiguration extends WebSecurityConfigurerAdapter {

        @Value("${app.security.max-sessions}")
        private Integer maxSessions;

        @Value("${app.security.max-sessions-prevents-login}")
        private Boolean maxSessionsPreventsLogin;

        @Autowired
        private ApiAuthenticationEntryPoint apiAuthenticationEntryPoint;

        @Autowired
        private ApiAuthenticationFailureHandler apiAuthenticationFailureHandler;


        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http.authorizeRequests()
                    .antMatchers("/api/v1/device/**").permitAll()
                    .antMatchers("/api/v1/catalogs/**").permitAll()
                    .antMatchers("/api/v1/**/join*").permitAll()
                    .antMatchers("/api/v1/tournaments/**").permitAll()
                    .antMatchers("/api/v1/soccerGames/**").permitAll()
                    .antMatchers("/api/v1/users/**").permitAll()
                    .antMatchers("/api/v1/users/emails/**").permitAll()
                    .antMatchers("/api/v1/email/**/validateOtp").permitAll()
                    .antMatchers("/api/v1/users/**/security/password").permitAll()
                    .antMatchers("/api/v1/email-password/**").permitAll()
                    .antMatchers("/api/v1/email/**").permitAll()
                    .antMatchers("/api/v1/events/**").permitAll()
                    .antMatchers("/api/v1/apuestas/**").permitAll()
                    .antMatchers("/api/v1/pronosticos/**").permitAll()                    .antMatchers("/api/v1/pronosticos/**").permitAll()
                    .antMatchers("/api/v1/potes/**").permitAll()
                    .antMatchers("/api/v1/recargas/**").permitAll()
                    .antMatchers("/api/v1/descuentos/**").permitAll()
                    .antMatchers("/api/v1/apuestasExactas/**").permitAll()
                    .antMatchers("/api/v1/pronosticosExactos/**").permitAll()


                    .antMatchers("/api/v1/**").authenticated()
                    .and().httpBasic().authenticationEntryPoint(apiAuthenticationEntryPoint)
                    .and().cors()
                    .and().csrf().disable();

            http.sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .invalidSessionStrategy((request, response) -> response.setStatus(HttpStatus.UNAUTHORIZED.value()))
                    .sessionAuthenticationFailureHandler(apiAuthenticationFailureHandler)
                    .maximumSessions(maxSessions)
                    .maxSessionsPreventsLogin(maxSessionsPreventsLogin);

        }

        @Bean
        public WebMvcConfigurer corsConfigurer() {
            return new WebMvcConfigurer() {

                @Override
                public void addInterceptors(InterceptorRegistry registry) {
                    registry.addWebRequestInterceptor(new WebRequestInterceptor() {
                        @Override
                        public void preHandle(WebRequest webRequest) throws Exception {
                            evalAppLocale(webRequest.getUserPrincipal(), webRequest.getHeader("Accept-Language"), Constants.LOCALE_SUPPORTED);
                        }

                        @Override
                        public void postHandle(WebRequest webRequest, ModelMap modelMap) throws Exception {

                        }

                        @Override
                        public void afterCompletion(WebRequest webRequest, Exception e) throws Exception {

                        }
                    });
                }

                @Override
                public void addCorsMappings(CorsRegistry registry) {
                    registry.addMapping("/api/**")
                            .allowedOrigins("*")
                            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                            .allowedHeaders("Authorization", "Content-Type", "Created-Entity", "X-Auth-Token")
                            .exposedHeaders("X-Auth-Token", "Created-Entity");
                }

            };
        }

        @Bean
        public HttpSessionIdResolver httpSessionIdResolver() {
            return HeaderHttpSessionIdResolver.xAuthToken();
        }

    }

    @Component
    public static class ApiAuthenticationEntryPoint implements AuthenticationEntryPoint {

        private static final Integer INVALID_CREDENTIALS_ERROR = 150;
        private static final Integer USER_BLOCKED_ERROR = 151;
        private static final Integer EXPIRED_PASSWORD_ERROR = 152;

        @Autowired
        private MessageSource messageSource;

        @Autowired
        private ObjectMapper jsonMapper;

        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
            Integer errorCode = null;

            if (e instanceof UsernameNotFoundException || e instanceof BadCredentialsException) {
                errorCode = INVALID_CREDENTIALS_ERROR;
            } else if (e instanceof LockedException) {
                errorCode = USER_BLOCKED_ERROR;
            } else if (e instanceof CredentialsExpiredException) {
                errorCode = EXPIRED_PASSWORD_ERROR;
            } else if (e instanceof InsufficientAuthenticationException) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }

            if (errorCode != null) {

                Locale locale = evalAppLocale(request.getUserPrincipal(), request.getHeader("Accept-Language"), Constants.LOCALE_SUPPORTED);

                String message = messageSource.getMessage("error." + errorCode, null, locale);

                ApiResponse apiResponse = new ApiResponse(HttpStatus.UNAUTHORIZED.value(), errorCode, message);

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().print(jsonMapper.writeValueAsString(apiResponse));
            }
        }

    }

    @Component
    public static class ApiAuthenticationFailureHandler implements AuthenticationFailureHandler {

        private static final Integer USER_ALREADY_IN_SESSION = 154;

        @Autowired
        private EventsService eventsService;

        @Autowired
        private FindUser findUser;

        @Autowired
        private MessageSource messageSource;

        @Autowired
        private ObjectMapper jsonMapper;


        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
            if (e instanceof SessionAuthenticationException) {

                Locale locale = evalAppLocale(request.getUserPrincipal(), request.getHeader("Accept-Language"), Constants.LOCALE_SUPPORTED);

                String message = messageSource.getMessage("error." + USER_ALREADY_IN_SESSION, null, locale);

                if (request.getUserPrincipal() instanceof UsernamePasswordAuthenticationToken
                    && ((UsernamePasswordAuthenticationToken)request.getUserPrincipal()).getPrincipal() instanceof UserSession) {

                    UserSession userSession = (UserSession)((UsernamePasswordAuthenticationToken)request.getUserPrincipal()).getPrincipal();

                    eventsService.createEvent(new Event(EventSource.SPORTS_SFT, getOriginAddress(request), 400, null,
                        findUser.byId(userSession.getId()), USER_ALREADY_IN_SESSION, message));
                }


                ApiResponse apiResponse = new ApiResponse(HttpStatus.UNAUTHORIZED.value(), USER_ALREADY_IN_SESSION, message);

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().print(jsonMapper.writeValueAsString(apiResponse));
            }
        }

    }

    @Configuration
    @EnableAsync
    public class ApiAsyncConfiguration implements AsyncConfigurer {

        @Override
        public Executor getAsyncExecutor() {
            ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
            threadPoolTaskExecutor.initialize();

            return threadPoolTaskExecutor;
        }

        @Override
        public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
            return new SimpleAsyncUncaughtExceptionHandler();
        }

    }

    @Configuration
    @EnableScheduling
    public class ApiSchedulingConfiguration implements SchedulingConfigurer {

        @Override
        public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
            scheduledTaskRegistrar.setScheduler(taskExecutor());
        }

        @Bean(destroyMethod = "shutdown")
        public Executor taskExecutor() {
            return Executors.newScheduledThreadPool(25);
        }

    }

    @Aspect
    @Component
    @Profile("develop")
    public static class LoggingAspect {

        @Autowired
        private ObjectMapper objectMapper;

        @Around("execution(* com.meta.sports.*.*Service.*(..))")
        public Object aroundMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
            if (logger.isDebugEnabled()) {
                try {
                    logger.debug(proceedingJoinPoint.getSignature().toShortString() + " <- " + objectMapper.writeValueAsString(proceedingJoinPoint.getArgs()));
                } catch (Exception e) {
                    logger.warn(proceedingJoinPoint.getSignature().toShortString() + " <- [CANNOT CONVERT]");
                }
            }

            Object response = proceedingJoinPoint.proceed();

            if (logger.isDebugEnabled()) {
                try {
                    logger.debug(proceedingJoinPoint.getSignature().toShortString() + " -> " + objectMapper.writeValueAsString(response));
                } catch (Exception e) {
                    logger.warn(proceedingJoinPoint.getSignature().toShortString() + " -> [CANNOT CONVERT]");
                }
            }

            return response;
        }

    }

    @Component
    @Order(1)
    public class LoggingFilter extends OncePerRequestFilter {

        private final AtomicLong sequence = new AtomicLong(0);

        @Override
        public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
            ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
            ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

            Long id = sequence.incrementAndGet();

            if (logger.isInfoEnabled()) {
                StringBuilder entrada = new StringBuilder();

                entrada.append("Inbound Message\n");
                entrada.append("  ID: ").append(id);
                entrada.append("  Http-Method: ").append(requestWrapper.getMethod());
                entrada.append(" ").append(requestWrapper.getRequestURL());

                if (logger.isDebugEnabled()) {
                    entrada.append("\n");
                    entrada.append("  Headers: ").append(convertHeadersToMap(requestWrapper).toString());

                    if (requestWrapper.getContentType() != null && requestWrapper.getContentType().startsWith("application/json") && requestWrapper.getContentAsByteArray().length > 0) {
                        entrada.append("\n");
                        entrada.append("  Payload: ").append(new String(requestWrapper.getContentAsByteArray()));
                    }

                    logger.debug(entrada.toString());
                } else {
                    logger.info(entrada.toString());
                }
            }

            Long nanoTime = System.nanoTime();

            chain.doFilter(requestWrapper, responseWrapper);

            nanoTime = System.nanoTime() - nanoTime;

            if (logger.isInfoEnabled()) {
                StringBuilder salida = new StringBuilder();

                salida.append("Outbound Message\n");
                salida.append("  ID: ").append(id);
                salida.append("  Http-Status: ").append(responseWrapper.getStatus());
                salida.append("  Execution Time: ").append((double) nanoTime / 1000000.00).append(" ms.");

                if (logger.isDebugEnabled()) {
                    salida.append("\n");
                    salida.append("  Headers: ").append(convertHeadersToMap(responseWrapper).toString()).append("\n");

                    if (responseWrapper.getContentType() != null && responseWrapper.getContentType().startsWith("application/json")) {
                        salida.append("  Payload: ").append(getResponsePayload(responseWrapper));
                    }

                    logger.debug(salida.toString());
                } else {
                    logger.info(salida.toString());
                }
            }

            responseWrapper.copyBodyToResponse();
        }

        private Map<String, String> convertHeadersToMap(HttpServletRequest request) {
            Map<String, String> map = new HashMap<>();

            Enumeration<String> headerNames = request.getHeaderNames();

            while (headerNames.hasMoreElements()) {
                String key = headerNames.nextElement();
                String value = request.getHeader(key);
                map.put(key, value);
            }

            return map;
        }

        private Map<String, String> convertHeadersToMap(HttpServletResponse response) {
            Map<String, String> map = new HashMap<>();

            response.getHeaderNames().forEach(headerName -> map.put(headerName, response.getHeader(headerName)));

            return map;
        }

        private String getResponsePayload(ContentCachingResponseWrapper responseWrapper) {
            String payload = "";

            try {
                byte[] buffer = responseWrapper.getContentAsByteArray();
                if (buffer.length > 0) {
                    payload = new String(buffer, 0, buffer.length, responseWrapper.getCharacterEncoding());
                }
            } catch (Exception e) {
                logger.error("error on getting response body", e);
            }

            return payload;
        }

    }

    @Component
    @Order(11)
    public class EventsFilter extends OncePerRequestFilter {

        @Autowired
        private EventsService eventsService;

        @Override
        public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
            Event event = new Event();

            event.setSource(EventSource.SPORTS_SFT);

            if (request.getSession(false) != null) {
                if (SecurityContextHolder.getContext().getAuthentication() != null &&
                        SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserSession) {

                    UserSession usuarioSesion = (UserSession) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                    event.setCreatedBy(new User(usuarioSesion.getId(), usuarioSesion.getUsername()));
                } else {
//                    String subject = SecurityContextHolder.getContext().getAuthentication() != null ?
//                            (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal() : request.getHeader("device");
//
//                    event.setSource(subject == null ? "unlnow" : subject);
                }
            }

            event.setOrigin(getOriginAddress(request));
            event.setSource(event.getSource() == null ? EventSource.SPORTS_SFT : event.getSource());

            request.setAttribute(Constants.REQUEST_EVENT, event);

            chain.doFilter(request, response);

            if (event.getType() != null) {
                eventsService.createEvent(event);
            }
        }

    }

    @ControllerAdvice
    public class ApiResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

        private final Integer SYSTEM_ERROR = 100;

        @Autowired
        private MessageSource messageSource;

        @ExceptionHandler(AccessDeniedException.class)
        public final ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException e, WebRequest request) {

            return new ResponseEntity<>(new ApiResponse(HttpStatus.FORBIDDEN.value(), null, e.getMessage()), HttpStatus.FORBIDDEN);
        }

        @ExceptionHandler(BadRequestException.class)
        public final ResponseEntity<ApiResponse> handleBadRequestException(BadRequestException e, WebRequest request) {

            Locale locale = evalAppLocale(request.getUserPrincipal(), request.getHeader("Accept-Language"), Constants.LOCALE_SUPPORTED);

            String message = messageSource.getMessage("error." + e.getCode(), e.getParameters(), locale);

            Event event = (Event) request.getAttribute(Constants.REQUEST_EVENT, RequestAttributes.SCOPE_REQUEST);

            if (event != null && event.getType() != null) {
                event.setErrorCode(e.getCode());
                event.setMessage(message);
            }

            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), e.getCode(), message), HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(NotFoundException.class)
        public final ResponseEntity<ApiResponse> handleNotFoundException(NotFoundException e, WebRequest request) {

            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND.value(), null, "Resource not found"), HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(MaxUploadSizeExceededException.class)
        public final ResponseEntity<ApiResponse> handleUploadSizeException(Exception e, WebRequest request) {

            Locale locale = evalAppLocale(request.getUserPrincipal(), request.getHeader("Accept-Language"), Constants.LOCALE_SUPPORTED);
            String message = messageSource.getMessage("error.300", null, locale);

            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), null, message), HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(MultipartException.class)
        public final ResponseEntity<ApiResponse> handleMultipartException(Exception e, WebRequest request) {

            Locale locale = evalAppLocale(request.getUserPrincipal(), request.getHeader("Accept-Language"), Constants.LOCALE_SUPPORTED);
            String message = messageSource.getMessage("error.304", null, locale);

            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_ACCEPTABLE.value(), null, message), HttpStatus.NOT_ACCEPTABLE);
        }

        @ExceptionHandler(Exception.class)
        public final ResponseEntity<ApiResponse> handleRuntimeException(Exception e, WebRequest request) {
            String uuid = UUID.randomUUID().toString();
            logger.error("UUID = " + uuid, e);

            Locale locale = evalAppLocale(request.getUserPrincipal(), request.getHeader("Accept-Language"), Constants.LOCALE_SUPPORTED);
            String message = messageSource.getMessage("error." + SYSTEM_ERROR, null, locale);
            String exception = e.getClass().getCanonicalName() + ": " + e.getMessage();

            Event event = (Event) request.getAttribute(Constants.REQUEST_EVENT, RequestAttributes.SCOPE_REQUEST);

            if (event != null && event.getType() != null) {
                event.setErrorCode(SYSTEM_ERROR);
                event.setMessage(message);
                event.addDataMapElement("UUID", uuid);
                event.addDataMapElement("Exception", exception);
            }

            return new ResponseEntity<>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, message), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private static Locale evalAppLocale(Principal principal, String langHeader, List<String> locales) {

        Locale locale = Constants.DEFAULT_LOCALE;

        UserSession userSession = null;

        if (principal instanceof UsernamePasswordAuthenticationToken
            && ((UsernamePasswordAuthenticationToken)principal).getPrincipal() instanceof UserSession) {

            userSession = (UserSession)((UsernamePasswordAuthenticationToken)principal).getPrincipal();
        }

        if (langHeader != null) {

            if (locales.stream().anyMatch(l -> l.equalsIgnoreCase(langHeader.trim().toLowerCase()))){
                locale = new Locale(langHeader);
            }

            if (userSession != null && (userSession.getLocale() == null || userSession.getLocale().getLanguage().equalsIgnoreCase(langHeader))) {

                userSession.setLocale(locale);
            }
        }
        else if (userSession != null && userSession.getLocale() == null) {
            userSession.setLocale(locale);
        }
        else if (userSession != null) {
            locale = userSession.getLocale();
        }

        return locale;
    }

    private static String getOriginAddress(HttpServletRequest request) {
        if (request.getHeader("X-Forwarded-For") != null) {
            return request.getHeader("X-Forwarded-For");
        } else {
            return request.getRemoteAddr();
        }
    }

}