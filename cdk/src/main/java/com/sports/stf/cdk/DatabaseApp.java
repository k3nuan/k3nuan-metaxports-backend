package com.sports.stf.cdk;

import dev.stratospheric.cdk.ApplicationEnvironment;
import dev.stratospheric.cdk.PostgresDatabase;
import software.amazon.awscdk.core.App;
import software.amazon.awscdk.core.Environment;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;

import static com.sports.stf.cdk.Validations.requireNonEmpty;

public class DatabaseApp {

    public static void main(final String[] args) {
        App app = new App();

        String environmentName = (String) app.getNode().tryGetContext("environmentName");
        requireNonEmpty(environmentName, "context variable 'environmentName' must not be null");

        String applicationName = (String) app.getNode().tryGetContext("applicationName" );
        requireNonEmpty(applicationName, "context variable 'applicationName' must not be null");

        String accountId = (String) app.getNode().tryGetContext("accountId");
        requireNonEmpty(accountId, "context variable 'accountId' must not be null");

        String region = (String) app.getNode().tryGetContext("region");
        requireNonEmpty(region, "context variable 'region' must not be null");

        Environment awsEnvironment = makeEnv(accountId, region);

        ApplicationEnvironment applicationEnvironment = new ApplicationEnvironment(
                applicationName,
                environmentName);

        Stack databaseStack = new Stack(app, "DatabaseStack", StackProps.builder()
                .stackName(applicationEnvironment.prefix("Database"))
                .env(awsEnvironment)
                .build());

        new PostgresDatabase(
                databaseStack,
                "Database",
                awsEnvironment,
                applicationEnvironment,
                new PostgresDatabase.DatabaseInputParameters()
                        .withPostgresVersion("11.15"));

        app.synth();
    }

    static Environment makeEnv(String account, String region) {
        return Environment.builder()
                .account(account)
                .region(region)
                .build();
    }
}