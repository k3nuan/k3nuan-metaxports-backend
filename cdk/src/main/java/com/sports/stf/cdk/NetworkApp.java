package com.sports.stf.cdk;

import dev.stratospheric.cdk.Network;
import software.amazon.awscdk.core.App;
import software.amazon.awscdk.core.Environment;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;

import static com.sports.stf.cdk.Validations.requireNonEmpty;

/**
 * Creates the Network on AWS where the SportSTF Backend Service
 * and the MS SQL Server RDS will be deployed given an Environment
 *
 * To deploy the network execute:
 * npm run repository:deploy -- -c accountId=YOUR_ACCOUNT_ID -c region=YOUR_REGION -c environmentName=YOUR_ENVIRONMENT
 *
 * To destroy the network execute:
 * npm run network:destroy -- -c accountId=YOUR_ACCOUNT_ID -c region=YOUR_REGION -c environmentName=YOUR_ENVIRONMENT
 *
 * To get your account id execute: aws sts get-caller-identity
 * To get your default region execute: aws configure get region
 * The environment name can be staging, testing, prod, dev, or whatever other name you want
 * */
public class NetworkApp {

    public static void main(final String[] args) {

        App app = new App();

        String environmentName = (String) app.getNode().tryGetContext("environmentName");
        requireNonEmpty(environmentName, "context variable 'environmentName' must not be null");

        String accountId = (String) app.getNode().tryGetContext("accountId");
        requireNonEmpty(accountId, "context variable 'accountId' must not be null");

        String region = (String) app.getNode().tryGetContext("region");
        requireNonEmpty(region, "context variable 'region' must not be null");

        String sslCertificateArn = (String) app.getNode().tryGetContext("sslCertificateArn");
        //    requireNonEmpty(sslCertificateArn, "context variable 'sslCertificateArn' must not be null");

        Environment awsEnvironment = makeEnv(accountId, region);

        Stack networkStack = new Stack(app, "NetworkStack", StackProps.builder()
                .stackName(environmentName + "-Network")
                .env(awsEnvironment)
                .build());

        Network network = new Network(
                networkStack,
                "Network",
                awsEnvironment,
                environmentName,
                new Network
                        .NetworkInputParameters(sslCertificateArn));
        app.synth();
    }

    static Environment makeEnv(String account, String region) {
        return Environment.builder()
                .account(account)
                .region(region)
                .build();
    }
}