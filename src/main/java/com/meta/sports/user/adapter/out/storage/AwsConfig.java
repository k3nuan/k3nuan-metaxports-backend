package com.meta.sports.user.adapter.out.storage;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("aws")
public class AwsConfig {

    @Value("${amazon.s3.key}")
    private String amazonS3Key;

    @Value("${amazon.s3.key.id}")
    private String amazonS3Keyid;

    @Bean
    public AmazonS3 s3Client() {
        return AmazonS3ClientBuilder.standard().withClientConfiguration(new ClientConfiguration())
                .withRegion(Regions.US_EAST_1).withCredentials(new AWSCredentialsProvider() {
                    @Override
                    public AWSCredentials getCredentials() {
                        return new AWSCredentials() {
                            @Override
                            public String getAWSAccessKeyId() {
                                return amazonS3Keyid;
                            }

                            @Override
                            public String getAWSSecretKey() {
                                return amazonS3Key;
                            }
                        };
                    }

                    @Override
                    public void refresh() {
                    }
                }).build();
    }

}