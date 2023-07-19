package com.sports.stf.cdk;

import dev.stratospheric.cdk.DockerRepository;
import software.amazon.awscdk.core.App;
import software.amazon.awscdk.core.Environment;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import static com.sports.stf.cdk.Validations.requireNonEmpty;

/**
 * Creates a Docker Repository on AWS to Store SportSTF Backend Image
 * To deploy the repository execute:
 * npm run repository:deploy -- -c accountId=YOUR_ACCOUNT_ID -c region=YOUR_REGION
 *
 * To destroy the repository exeute:
 * npm run repository:destroy -- -c accountId=YOUR_ACCOUNT_ID -c region=YOUR_REGION
 *
 * To get your account id execute: aws sts get-caller-identity
 * To get your default region execute: aws configure get region
 * */
public class DockerRepositoryApp {

    public static void main(final String[] args) {

        App app = new App();

        String accountId = (String) app.getNode().tryGetContext("accountId" );
        requireNonEmpty(accountId, "context variable 'accountId' must not be null");

        String region = (String) app.getNode().tryGetContext("region" );
        requireNonEmpty(region, "context variable 'region' must not be null");

        String applicationName = (String) app.getNode().tryGetContext("applicationName" );
        requireNonEmpty(applicationName, "context variable 'applicationName' must not be null");

        Environment awsEnvironment = makeEnv(accountId, region);

        Stack dockerRepositoryStack = new Stack(app, "DockerRepositoryStack", StackProps.builder()
                .stackName(applicationName + "-DockerRepository")
                .env(awsEnvironment)
                .build());

        DockerRepository dockerRepository = new DockerRepository(
                dockerRepositoryStack,
                "DockerRepository",
                awsEnvironment,
                new DockerRepository.DockerRepositoryInputParameters(applicationName, accountId, 10, false));

        app.synth();
    }

    private static Environment makeEnv(String account, String region) {
        return Environment.builder().account(account).region(region).build();
    }
}
