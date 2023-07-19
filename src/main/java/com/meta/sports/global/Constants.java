package com.meta.sports.global;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Constants {

    public static final Locale DEFAULT_LOCALE = new Locale("en");
    public static final List<String> LOCALE_SUPPORTED = new ArrayList<String>() {{
        add("en"); add("es");
    }};

    public static final String REQUEST_EVENT = "RQ_EVENT";
    public static final String REQUEST_USER_ID = "RQ_UID";

    public static final String ROLE_PREFIX = "ROLE_";

    public static final String APUESTA_GANADA = "GANADOR";
    public static final String APUESTA_PERDIDA= "PEDEDOR";

    public static final String PRIMERA_ETAPA = "PRIMERA";
    public static final String SEGUNDA_ETAPA= "SEGUNDA";



    public static final String ROLE_ADMIN = ROLE_PREFIX + "ADMIN";
    public static final String ROLE_USER = ROLE_PREFIX + "USER";

    public static final String ROLE_ALIADO = ROLE_PREFIX + "ALIADO";


}
