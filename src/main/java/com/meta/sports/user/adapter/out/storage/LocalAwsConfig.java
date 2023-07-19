package com.meta.sports.user.adapter.out.storage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@Profile("dev")
public class LocalAwsConfig {
    
    @Bean
    public AmazonS3 s3Client() {
        return AmazonS3ClientBuilder.standard()
            .withEndpointConfiguration(new EndpointConfiguration("http://localstack:4566", "us-east-1"))
            .withCredentials(getFakeCredentials())
            .enablePathStyleAccess()
            .build();
    }

    private AWSCredentialsProvider getFakeCredentials() {
        return new AWSCredentialsProvider() {
            @Override
            public AWSCredentials getCredentials() {
                return new AWSCredentials() {
                    @Override
                    public String getAWSAccessKeyId() {
                        return "foo";
                    }

                    @Override
                    public String getAWSSecretKey() {
                        return "bar";
                    }
                };
            }

            @Override
            public void refresh() {
            }
        };
    }
}
