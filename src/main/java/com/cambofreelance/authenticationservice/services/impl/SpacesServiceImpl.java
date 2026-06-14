package com.cambofreelance.authenticationservice.services.impl;

import com.cambofreelance.authenticationservice.services.SpacesService;
import java.net.URI;
import java.time.Duration;
import java.util.List;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.model.CORSConfiguration;
import software.amazon.awssdk.services.s3.model.CORSRule;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutBucketCorsRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Service
public class SpacesServiceImpl implements SpacesService {

    @Override
    public String presignPut(
        String endpoint,
        String region,
        String bucket,
        String accessKey,
        String secretKey,
        String objectKey,
        String mimeType,
        Duration expiry
    ) {
        try (S3Presigner presigner = S3Presigner.builder()
            .endpointOverride(URI.create(endpoint))
            .region(Region.of(region))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(accessKey, secretKey)
                )
            )
            .serviceConfiguration(S3Configuration.builder()
                .pathStyleAccessEnabled(true)
                .build())
            .build()
        ) {
            PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey)
                .contentType(mimeType)
                .build();

            PresignedPutObjectRequest presigned = presigner.presignPutObject(
                PutObjectPresignRequest.builder()
                    .signatureDuration(expiry)
                    .putObjectRequest(putRequest)
                    .build()
            );

            return presigned.url().toString();
        }
    }

    @Override
    public byte[] getObject(
        String endpoint,
        String region,
        String bucket,
        String accessKey,
        String secretKey,
        String objectKey
    ) {
        try (S3Client s3 = S3Client.builder()
            .endpointOverride(URI.create(endpoint))
            .region(Region.of(region))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(accessKey, secretKey)
                )
            )
            .serviceConfiguration(S3Configuration.builder()
                .pathStyleAccessEnabled(true)
                .build())
            .build()
        ) {
            return s3.getObject(
                GetObjectRequest.builder().bucket(bucket).key(objectKey).build(),
                ResponseTransformer.toBytes()
            ).asByteArray();
        }
    }

    @Override
    public void setupBucketCors(
        String endpoint,
        String region,
        String bucket,
        String accessKey,
        String secretKey,
        List<String> allowedOrigins
    ) {
        try (S3Client s3 = S3Client.builder()
            .endpointOverride(URI.create(endpoint))
            .region(Region.of(region))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(accessKey, secretKey)
                )
            )
            .serviceConfiguration(S3Configuration.builder()
                .pathStyleAccessEnabled(true)
                .build())
            .build()
        ) {
            CORSRule rule = CORSRule.builder()
                .allowedOrigins(allowedOrigins)
                .allowedMethods("GET", "PUT", "HEAD", "DELETE")
                .allowedHeaders("*")
                .exposeHeaders("ETag", "Content-Length")
                .maxAgeSeconds(3600)
                .build();

            s3.putBucketCors(PutBucketCorsRequest.builder()
                .bucket(bucket)
                .corsConfiguration(CORSConfiguration.builder()
                    .corsRules(rule)
                    .build())
                .build());
        }
    }
}
