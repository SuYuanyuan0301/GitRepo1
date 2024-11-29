package com.fansu.controller;

import com.fansu.pojo.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
public class FileUploadController {

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws IOException {
        //把文件存放在本地磁盘上（桌面的files文件夹中）
        String originalFileName = file.getOriginalFilename();
        //使用uuid保证文件的名字是唯一的，从而防止发生文件重名后对文件进行覆盖
        String filename = UUID.randomUUID().toString() + originalFileName.substring(originalFileName.lastIndexOf("."));
        file.transferTo(new File("D:\\Desktop\\files\\"+filename));
        return Result.success("url地址、、、上传成功");
    }
}
