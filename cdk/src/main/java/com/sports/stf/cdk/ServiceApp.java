package com.sports.stf.cdk;

import dev.stratospheric.cdk.ApplicationEnvironment;
import dev.stratospheric.cdk.Network;
import dev.stratospheric.cdk.PostgresDatabase;
import dev.stratospheric.cdk.Service;
import software.amazon.awscdk.core.*;
import software.amazon.awscdk.services.secretsmanager.ISecret;
import software.amazon.awscdk.services.secretsmanager.Secret;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sports.stf.cdk.Validations.requireNonEmpty;

public class ServiceApp {

    public static void main(final String[] args) {

        App app = new App();

        String environmentName = (String) app.getNode().tryGetContext("environmentName");
        requireNonEmpty(environmentName, "context variable 'environmentName' must not be null");

        String applicationName = (String) app.getNode().tryGetContext("applicationName" );
        requireNonEmpty(applicationName, "context variable 'applicationName' must not be null");

        String springProfile = (String) app.getNode().tryGetContext("springProfile" );
        requireNonEmpty(springProfile, "context variable 'springProfile' must not be null");

        String dockerImageUrl = (String) app.getNode().tryGetContext("dockerImageUrl" );
        requireNonEmpty(dockerImageUrl, "context variable 'dockerImageUrl' must not be null");

        String accountId = (String) app.getNode().tryGetContext("accountId");
        requireNonEmpty(accountId, "context variable 'accountId' must not be null");

        String region = (String) app.getNode().tryGetContext("region");
        requireNonEmpty(region, "context variable 'region' must not be null");

        String s3Bucket = (String) app.getNode().tryGetContext("s3Bucket");
        requireNonEmpty(s3Bucket, "context variable 's3Bucket' must not be null");

        String s3Key = (String) app.getNode().tryGetContext("s3Key");
        requireNonEmpty(s3Key, "context variable 's3Key' must not be null");

        String s3KeyId = (String) app.getNode().tryGetContext("s3KeyId");
        requireNonEmpty(s3KeyId, "context variable 's3KeyId' must not be null");

        String firebaseClientCertUrl = (String) app.getNode().tryGetContext("firebaseClientCertUrl");
        requireNonEmpty(firebaseClientCertUrl, "context variable 'firebaseClientCertUrl' must not be null");

        String firebaseProjectId = (String) app.getNode().tryGetContext("firebaseProjectId");
        requireNonEmpty(firebaseProjectId, "context variable 'firebaseProjectId' must not be null");

        String firebaseClientId = (String) app.getNode().tryGetContext("firebaseClientId");
        requireNonEmpty(firebaseClientId, "context variable 'firebaseClientId' must not be null");

        String firebaseClientEmail = (String) app.getNode().tryGetContext("firebaseClientEmail");
        requireNonEmpty(firebaseClientEmail, "context variable 'firebaseClientEmail' must not be null");

        String firebasePrivateKeyId = (String) app.getNode().tryGetContext("firebasePrivateKeyId");
        requireNonEmpty(firebasePrivateKeyId, "context variable 'firebasePrivateKeyId' must not be null");

        String firebasePrivateKey = (String) app.getNode().tryGetContext("firebasePrivateKey");
        requireNonEmpty(firebasePrivateKey, "context variable 'firebasePrivateKey' must not be null");

        String firebaseKey = (String) app.getNode().tryGetContext("firebaseKey");
        requireNonEmpty(firebaseKey, "context variable 'firebaseKey' must not be null");

        Environment awsEnvironment = makeEnv(accountId, region);

        ApplicationEnvironment applicationEnvironment = new ApplicationEnvironment(
                applicationName,
                environmentName);

        long timestamp = System.currentTimeMillis();
        Stack parametersStack = new Stack(app, "ServiceParameters-" + timestamp, StackProps.builder()
                .stackName(applicationEnvironment.prefix("Service-Parameters-" + timestamp))
                .env(awsEnvironment)
                .build());

        Stack serviceStack = new Stack(app, "ServiceStack", StackProps.builder()
                .stackName(applicationEnvironment.prefix("Service"))
                .env(awsEnvironment)
                .build());

        PostgresDatabase.DatabaseOutputParameters databaseOutputParameters =
                PostgresDatabase.getOutputParametersFromParameterStore(parametersStack, applicationEnvironment);

        List<String> securityGroupIdsToGrantIngressFromEcs = List.of(
                databaseOutputParameters.getDatabaseSecurityGroupId());




        // specifies the source of the Docker image to deploy
        Service.DockerImageSource dockerImageSource = new Service.DockerImageSource(dockerImageUrl);
        Service.ServiceInputParameters serviceInputParameters = new Service
                .ServiceInputParameters(dockerImageSource,
                    securityGroupIdsToGrantIngressFromEcs,
                        environmentVariables(
                            serviceStack,
                            databaseOutputParameters,
                            springProfile,
                            environmentName,
                            s3Bucket,
                            s3Key,
                            s3KeyId,
                            firebaseKey,
                            firebaseClientEmail,
                            firebaseClientCertUrl,
                            firebaseClientId,
                            firebasePrivateKey,
                            firebasePrivateKeyId,
                            firebaseProjectId))
                .withHealthCheckPath("/api/v1/users/emails/amneris@gmail.com/available")
                .withHealthCheckIntervalSeconds(30);

        // access output parameters from SSM stored by the network application
        Network.NetworkOutputParameters networkOutputParameters = Network
                .getOutputParametersFromParameterStore(serviceStack, applicationEnvironment.getEnvironmentName());

        // creates the service and binds the resources it deploys to the existing network infrastructure
        Service service = new Service(
                serviceStack,
                "Service",
                awsEnvironment,
                applicationEnvironment,
                serviceInputParameters,
                networkOutputParameters);

        app.synth();
    }

    static Map<String, String> environmentVariables(
            Construct scope,
            PostgresDatabase.DatabaseOutputParameters databaseOutputParameters,
            String springProfile, String environmentName, String s3Bucket, String s3Key, String s3KeyId,
                String firebaseKey, String firebaseClientEmail, String firebaseClientCertUrl, String firebaseClientId,
                String firebasePrivateKey, String firebasePrivateKeyId,
                String firebaseProjectId) {

        Map<String, String> vars = new HashMap<>();

        String databaseSecretArn = databaseOutputParameters.getDatabaseSecretArn();
        ISecret databaseSecret = Secret.fromSecretCompleteArn(scope, "databaseSecret", databaseSecretArn);

        vars.put("SPRING_PROFILES_ACTIVE", springProfile);
        vars.put("SPRING_DATASOURCE_URL",
                String.format("jdbc:postgresql://%s:%s/%s",
                        databaseOutputParameters.getEndpointAddress(),
                        databaseOutputParameters.getEndpointPort(),
                        databaseOutputParameters.getDbName()));
        vars.put("SPRING_DATASOURCE_USERNAME",
                databaseSecret.secretValueFromJson("username").toString());
        vars.put("SPRING_DATASOURCE_PASSWORD",
                databaseSecret.secretValueFromJson("password").toString());
        vars.put("ENVIRONMENT_NAME", environmentName);

        vars.put("FIREBASE_CLIENT_CERT_URL", firebaseClientCertUrl);
        vars.put("FIREBASE_PROJECT_ID", firebaseProjectId);
        vars.put("FIREBASE_CLIENT_ID", firebaseClientId);
        vars.put("FIREBASE_CLIENT_EMAIL", firebaseClientEmail);
        vars.put("FIREBASE_PRIVATE_KEY_ID", firebasePrivateKeyId);
        vars.put("FIREBASE_PRIVATE_KEY", "-----BEGIN PRIVATE KEY-----\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDNuSUfuOhxY1PQ\n/9+HxoocD6NLZAptmx8RnGK0T/KeMRsmVT8aJBSDp+kaNn5gtWZGH4sI1LkRFxGf\nfJYbmMCZ9m1GTxGUACmDyu97MYUcUM4IRe7lO7m0lA/2TFIFmYr7qLuf3T3yuJIe\nCvhfW6S2lVyq6Y5jahZN2bL7qpeog2pOgW/IelQ3AeUO1Y12MmaISq1wwbw/ioL4\n/fhxIUfPmZ452O9U1EOkRQ14hsfavmI+X6guiSolsk1XeD2arBOfnenQLkIZTHqp\n1SAIPiDrDPSFkXfntfgq223HrdSRStky33eGAupbBKyrUASgJEYLQXi8u2TodROh\n8QDtBdDLAgMBAAECggEAHdLyg9fNsuqAczM1F7z7tre5p6tYK62qYmY2lQdmF/Ls\nibEgFc0XQELuc0NlLOX/brkR8fTM46JhhqqMJHIPsgJcsy2xUbB4VEfuUgDK2GEK\njX599LOk533zlnRYC8HHuVYr5TAw0+hYHozelwx1I5chuRFk+BqgIyclYIDJvb8U\nsUE/Uz1K4gM19CqgcaO3VB2GEFCowZpdfVYGHaHYrqmBWegR/MzkqDqcjt/bj6th\nS5cpLVVdzdbiyQhKK86N8cLEh1wzdrN5XaxS9czcD+f7TdU398YuoTd4hWWlZEh+\n+F7V7T13wisaHAtvheI5Po5ZiObNaP814byGRUUwIQKBgQD7PTdeLLgDfVjio0Rs\nCN2dISRdFytBtlpWC4RIc1xDQDbT5kPpMohOJP+dh0oav3RqO8dxrN10BPqkAqG+\nm4lowULBVrvjQyvHvYSW3XINRy58Vo2zyHuO+DIU5c87Doxvx4R9H+4Ma6bzFlo4\n3rLBqx9w1hSz8fSYKdQIwj4BmwKBgQDRnyCBMxb/qvkQBFqNK8w7kkq2Cv+Bq0vz\n68p7YLzmJyjOKyCExtml323VNrc8Md67Ai2Zg61MJj/ZZxPKFqNxK/205+ZRSTWi\nDUavGgrjXKBiM7BgzfyCKivYvNfehH+LGobqqT5cjqOVqS5IhvQkBx5/qaO+89g3\niyADkfc4kQKBgQC45VFlHlYCQ3JxwKQvOcsWw3hGhjGQd8o9vtYIl9WTrKNtCO57\npXPIxtPTs+gmjTX4pKvgDc8gfjWTFFjCtSbIEEq3jMRTuXBz3BbDDI/OE8+zM+Ov\nJuE/04rP9FyxYsCZ1uS7TQ9c1rET/yRA7Cvz+aL24KZ106Alh8sC8yi89wKBgCrY\nHlmp6pJ5DIz0htmB0S1/2htZJh4Ht+90Cx+qhxF3gYSaFfxtdEDN6PICS/NWshKB\nAHb3De2Zh8VUpxvQArn35tQZ7d5BW1/y3LafWQhdfz5zGCLBF1I3clzLREfrxvHU\niyzdSKy8Tr0hJh+WzcGrXtVzA8/coR3BJtrz85axAoGAVFpPZu935iBirgMUDSFD\nqqBlwcNkjDHRmmZSzHXTxTQ8dhzyTPgGVASZrD/GvVAxzjHf1gnbrMJLug+eWUZo\nOFBsFD6Lp5bSEnaQoxbFns94cG7tycIQ5aL0pVxWjHNDA+T9hXU3YpInZOKKdBGQ\nVe0Y7pd9zsegWrEmHMy+v4c=\n-----END PRIVATE KEY-----\n");
        vars.put("FIREBASE_KEY", firebaseKey);

        vars.put("AMAZON_S3_BUCKET", s3Bucket);
        vars.put("AMAZON_S3_KEY", s3Key);
        vars.put("AMAZON_S3_KEY_ID", s3KeyId);

        return vars;
    }

    static Environment makeEnv(String account, String region) {
        return Environment.builder()
                .account(account)
                .region(region)
                .build();
    }
}