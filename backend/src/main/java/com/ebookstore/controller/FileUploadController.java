package com.ebookstore.controller;

import com.ebookstore.vo.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    @Value("${file.upload-path:uploads}")
    private String uploadPath;

    @PostMapping("/cover")
    public Result<String> uploadCover(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        if (file.isEmpty()) {
            return Result.error(400, "文件不能为空");
        }
        String originalName = file.getOriginalFilename();
        String ext = "";
        if (originalName != null && originalName.contains(".")) {
            ext = originalName.substring(originalName.lastIndexOf("."));
        }
        String newName = UUID.randomUUID().toString().replace("-", "") + ext;
        String dateDir = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String dirPath = uploadPath + "/covers/" + dateDir;
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File dest = new File(dir, newName);
        try {
            file.transferTo(dest);
            String url = "/uploads/covers/" + dateDir + "/" + newName;
            return Result.success(url);
        } catch (IOException e) {
            return Result.error(500, "上传失败：" + e.getMessage());
        }
    }
}