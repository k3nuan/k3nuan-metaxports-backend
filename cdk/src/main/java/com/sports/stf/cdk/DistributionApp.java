package com.sports.stf.cdk;

import dev.stratospheric.cdk.ApplicationEnvironment;
import software.amazon.awscdk.core.App;
import software.amazon.awscdk.core.Environment;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;

import static com.sports.stf.cdk.Validations.requireNonEmpty;

public class DistributionApp {
    public static void main(final String[] args) {
        App app = new App();

        String applicationName = (String) app.getNode().tryGetContext("applicationName" );
        requireNonEmpty(applicationName, "context variable 'applicationName' must not be null");

        String environmentName = (String) app.getNode().tryGetContext("environmentName");
        requireNonEmpty(environmentName, "context variable 'environmentName' must not be null");

        String accountId = (String) app.getNode().tryGetContext("accountId");
        requireNonEmpty(accountId, "context variable 'accountId' must not be null");

        String region = (String) app.getNode().tryGetContext("region");
        requireNonEmpty(region, "context variable 'region' must not be null");

        String applicationDomain = (String) app.getNode().tryGetContext("applicationDomain");
        //Validations.requireNonEmpty(applicationDomain, "context variable 'applicationDomain' must not be null");

        String sslCertificateArn = (String) app.getNode().tryGetContext("sslCertificateArn");

        String hostedZoneDomain = (String) app.getNode().tryGetContext("hostedZoneDomain");

        ApplicationEnvironment applicationEnvironment = new ApplicationEnvironment(
                applicationName,
                environmentName);

        Environment awsEnvironment = makeEnv(accountId, region);

        Stack distributionStack = new Stack(app, "DistributionStack", StackProps.builder()
                .stackName(applicationEnvironment.prefix("Distribution"))
                .env(awsEnvironment)
                .build());

        new CloudFrontDistribution(distributionStack,
                "Distribution",
                awsEnvironment,
                applicationEnvironment,
                new CloudFrontDistribution.DistributionInputParameters(applicationDomain, sslCertificateArn, hostedZoneDomain));



        app.synth();
    }

    static Environment makeEnv(String account, String region) {
        return Environment.builder()
                .account(account)
                .region(region)
                .build();
    }
}