package com.sports.stf.cdk;

import dev.stratospheric.cdk.ApplicationEnvironment;
import dev.stratospheric.cdk.Network;
import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Environment;
import software.amazon.awscdk.core.RemovalPolicy;
import software.amazon.awscdk.services.certificatemanager.Certificate;
import software.amazon.awscdk.services.certificatemanager.ICertificate;
import software.amazon.awscdk.services.cloudfront.*;
import software.amazon.awscdk.services.cloudfront.origins.HttpOrigin;
import software.amazon.awscdk.services.cloudfront.origins.S3Origin;
import software.amazon.awscdk.services.route53.*;
import software.amazon.awscdk.services.route53.targets.CloudFrontTarget;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.BucketAccessControl;
import software.amazon.awscdk.services.s3.BucketProps;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class CloudFrontDistribution extends Construct {


    public CloudFrontDistribution(final Construct scope,
                                  final String id, Environment awsEnvironment,
                                  final ApplicationEnvironment applicationEnvironment,
                                  final CloudFrontDistribution.DistributionInputParameters distributionInputParameters) {
        super(scope, id);
        Bucket myBucket = new Bucket(this, "S3Bucket", BucketProps.builder()
                .accessControl(BucketAccessControl.BUCKET_OWNER_FULL_CONTROL)
                .bucketName(applicationEnvironment.prefix("s3bucket"))
                .publicReadAccess(true)
                .removalPolicy(RemovalPolicy.DESTROY)
                .websiteIndexDocument("index.html")
                .websiteErrorDocument("index.html")
                .build());

        Network.NetworkOutputParameters networkOutputParameters = Network
                .getOutputParametersFromParameterStore(this, applicationEnvironment.getEnvironmentName());


        Distribution.Builder distributionBuilder = Distribution.Builder.create(this, "CloudFront")
                .defaultBehavior(BehaviorOptions.builder()
                        .origin(new S3Origin(myBucket))
                        .viewerProtocolPolicy(ViewerProtocolPolicy.ALLOW_ALL)
                        .build())
                .defaultRootObject("index.html")

                .additionalBehaviors(Map.of(
                        "/api/*", BehaviorOptions.builder()
                                .origin(HttpOrigin.Builder.create(networkOutputParameters.getLoadBalancerDnsName())
                                    .protocolPolicy(OriginProtocolPolicy.MATCH_VIEWER)
                                    .build())
                                .viewerProtocolPolicy(ViewerProtocolPolicy.ALLOW_ALL)
                                .originRequestPolicy(OriginRequestPolicy.ALL_VIEWER)
                                .allowedMethods(AllowedMethods.ALLOW_ALL)
                                .build()));


        if (distributionInputParameters.applicationDomain.isPresent() &&
                distributionInputParameters.sslCertificateArn.isPresent() &&
                distributionInputParameters.hostedZoneDomain.isPresent()) {

            ICertificate certificate = Certificate
                    .fromCertificateArn(this, "Certificate", (String)distributionInputParameters
                    .sslCertificateArn.get());
            String applicationDomain = distributionInputParameters.applicationDomain.get();
            String hostedZoneDomain = distributionInputParameters.hostedZoneDomain.get();
            distributionBuilder
                    .domainNames(Collections.singletonList(applicationDomain))
                    .certificate(certificate);
            IHostedZone hostedZone = HostedZone.fromLookup(this, "HostedZone", HostedZoneProviderProps.builder()
                    .domainName(hostedZoneDomain)
                    .build());
            ARecord aRecord = ARecord.Builder.create(this, "ARecord")
                    .recordName(applicationDomain)
                    .zone(hostedZone)
                    .target(RecordTarget.fromAlias(new CloudFrontTarget(distributionBuilder.build()))).build();
        } else {
            distributionBuilder.build();
        }
    }

    public static class DistributionInputParameters {

        private Optional<String> applicationDomain;
        private Optional<String> sslCertificateArn;
        private Optional<String> hostedZoneDomain;
        public DistributionInputParameters(String applicationDomain, String sslCertificateArn, String hostedZoneDomain) {
            this.applicationDomain = Optional.ofNullable(applicationDomain);
            this.sslCertificateArn = Optional.ofNullable(sslCertificateArn);
            this.hostedZoneDomain = Optional.ofNullable(hostedZoneDomain);
        }
    }
}
