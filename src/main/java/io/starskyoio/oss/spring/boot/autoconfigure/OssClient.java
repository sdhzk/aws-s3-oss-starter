package io.starskyoio.oss.spring.boot.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * oss客户端
 * @author Linus.Lee
 * @date 2023-2-10
 */
@Slf4j
@RequiredArgsConstructor
public class OssClient {
    private final OssTemplate ossTemplate;
    private final OssProperties ossProperties;

    public void upload(String objectName, InputStream stream) {
        try {
            ossTemplate.putObject(ossProperties.getBucketName(), objectName, stream);
        } catch (Exception e) {
            throw new RuntimeException("上传失败，errorMsg:" + e.getMessage(), e);
        }
    }

    public String uploadPublic(String objectName, InputStream stream) {
        try {
            ossTemplate.putPublicReadObject(ossProperties.getBucketName(), objectName, stream);
            return ossTemplate.getObjectURL(ossProperties.getBucketName(), objectName);
        } catch (Exception e) {
            throw new RuntimeException("上传失败，errorMsg:" + e.getMessage(), e);
        }
    }

    public List<String> getSignedUrls(List<String> objectNames) {
        if(CollectionUtils.isEmpty(objectNames)){
            return null;
        }
        return objectNames.stream()
                .map(objectName -> CompletableFuture.supplyAsync(() -> generatePresignedUrl(objectName, null)))
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .toList();
    }

    public String getSignedUrl(String objectName) {
        return generatePresignedUrl(objectName, null);
    }

    public String getSignedUrl(String objectName, String downloadFileName) {
        return generatePresignedUrl(objectName, downloadFileName);
    }

    private String generatePresignedUrl(String objectName, String downloadFileName) {
        if(!StringUtils.hasText(objectName)){
            return null;
        }
        try {
            return ossTemplate.getObjectURL(ossProperties.getBucketName(), objectName, downloadFileName,ossProperties.getExpires());
        } catch (Exception e){
            log.warn("获取文件签名URL失败", e);
            return null;
        }
    }
}
