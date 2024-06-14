package cn.org.dialogue.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Api(tags="其他文件接口")
@RestController
@RequestMapping("/download")
public class DownloadController {

    @ApiOperation(value = "下载APP")
    @GetMapping("app")
    public ResponseEntity<byte[]> AppResponseEntity() throws Exception {
        String filePath = "/templates/dialogue-0.1.0.apk";
        ClassPathResource readFile = new ClassPathResource(filePath);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Disposition", "attachment;filename=" + URLEncoder.encode(readFile.getFilename(), "UTF-8"));
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        File file = readFile.getFile();
        Path path = Paths.get(file.toURI());
        byte[] bytes = Files.readAllBytes(path);
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    @ApiOperation(value = "下载桌面端")
    @GetMapping("windows")
    public ResponseEntity<byte[]> ExeResponseEntity() throws Exception {
        String filePath = "/templates/dialogue-0.1.0-x64.exe";
        ClassPathResource readFile = new ClassPathResource(filePath);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Disposition", "attachment;filename=" + URLEncoder.encode(readFile.getFilename(), "UTF-8"));
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        File file = readFile.getFile();
        Path path = Paths.get(file.toURI());
        byte[] bytes = Files.readAllBytes(path);
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

}