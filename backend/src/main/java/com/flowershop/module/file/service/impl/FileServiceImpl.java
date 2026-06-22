package com.flowershop.module.file.service.impl;

import com.flowershop.common.BusinessException;
import com.flowershop.module.file.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Value("${app.upload.path:./uploads}")
    private String uploadPath;

    private static final List<String> ALLOWED_TYPES = List.of(
            "image/jpeg", "image/png", "image/gif", "image/webp"
    );

    @Override
    public String upload(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BusinessException("文件为空");
        }
        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new BusinessException("不支持的文件类型，仅允许 jpg/png/gif/webp");
        }

        try {
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String newFilename = "flower-images/" + UUID.randomUUID() + extension;

            Path targetPath = Paths.get(uploadPath, newFilename);
            Files.createDirectories(targetPath.getParent());
            file.transferTo(targetPath.toFile());

            return "/uploads/" + newFilename.replace("\\", "/");
        } catch (IOException e) {
            throw new BusinessException("文件上传失败: " + e.getMessage());
        }
    }
}
