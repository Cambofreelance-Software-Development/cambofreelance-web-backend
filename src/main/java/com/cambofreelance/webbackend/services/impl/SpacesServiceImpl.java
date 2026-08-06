package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.services.SpacesObject;
import com.cambofreelance.webbackend.services.SpacesService;
import java.net.URI;
import java.time.Duration;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.model.CORSConfiguration;
import software.amazon.awssdk.services.s3.model.CORSRule;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
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
    public SpacesObject getObject(
        String endpoint,
        String region,
        String bucket,
        String accessKey,
        String secretKey,
        String objectKey,
        String rangeHeader
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
            GetObjectRequest.Builder requestBuilder = GetObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey);
            if (StringUtils.hasText(rangeHeader)) {
                requestBuilder.range(rangeHeader);
            }

            ResponseBytes<GetObjectResponse> response =
                s3.getObject(requestBuilder.build(), ResponseTransformer.toBytes());
            GetObjectResponse metadata = response.response();
            String contentRange = metadata.contentRange();

            if (StringUtils.hasText(contentRange)) {
                // Format: "bytes <start>-<end>/<total>"
                String spec = contentRange.substring(contentRange.indexOf(' ') + 1);
                String[] rangeAndTotal = spec.split("/");
                String[] startEnd = rangeAndTotal[0].split("-");
                return SpacesObject.builder()
                    .data(response.asByteArray())
                    .totalSize(Long.parseLong(rangeAndTotal[1]))
                    .partial(true)
                    .rangeStart(Long.parseLong(startEnd[0]))
                    .rangeEnd(Long.parseLong(startEnd[1]))
                    .build();
            }

            return SpacesObject.builder()
                .data(response.asByteArray())
                .totalSize(metadata.contentLength())
                .partial(false)
                .build();
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
