package com.ruantang.service.prompt.controller;

import com.ruantang.commons.api.ApiResult;
import com.ruantang.service.prompt.model.DocGenerateRequest;
import com.ruantang.service.prompt.model.DocGenerateResponse;
import com.ruantang.service.prompt.model.DocWordExportRequest;
import com.ruantang.service.prompt.service.DocGenerateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 文档生成Controller
 */
@RestController
@RequestMapping("/api/doc-generate")
public class DocGenerateController {

    @Autowired
    private DocGenerateService docGenerateService;

    /**
     * 生成文档
     */
    @PostMapping
    public ApiResult<DocGenerateResponse> generateDoc(@RequestBody DocGenerateRequest request) {
//        DocGenerateResponse response = docGenerateService.generateDoc(request);
        DocGenerateResponse response = docGenerateService.generateDocWithContext(request);
        return ApiResult.success(response, "文档生成成功");
    }
    
    /**
     * 导出Word文档
     */
    @PostMapping("/export-word")
    public ResponseEntity<byte[]> exportWordDocument(@RequestBody DocWordExportRequest request) {
        try {
            byte[] wordData = docGenerateService.exportWordDocument(request);
            String fileName = generateFileName(request.getTitle(), request.getFileName());

            HttpHeaders headers = new HttpHeaders();
            // 设置正确的MIME类型
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));

            // 使用RFC 5987标准编码文件名
            String encodedFileName = "=?UTF-8?B?" +
                    Base64.getEncoder().encodeToString(fileName.getBytes(StandardCharsets.UTF_8)) +
                    "?=";

            headers.setContentDisposition(
                    ContentDisposition.builder("attachment")
                            .filename(encodedFileName)
                            .build()
            );

            headers.setContentLength(wordData.length);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(wordData);

        } catch (Exception e) {
            String errorMsg = "文档导出失败: " + e.getMessage();
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(errorMsg.getBytes(StandardCharsets.UTF_8));
        }
    }
    
    /**
     * 生成文件名
     *
     * @param title 文档标题
     * @param customFileName 自定义文件名
     * @return 文件名
     */
    private String generateFileName(String title, String customFileName) {
        String fileName;
        if (StringUtils.hasText(customFileName)) {
            fileName = customFileName;
            // 确保以 .docx 结尾
            if (!fileName.endsWith(".docx")) {
                fileName += ".docx";
            }
        } else {
            fileName = StringUtils.hasText(title) ? title : "技术文档";
            // 移除文件名中的非法字符
            fileName = fileName.replaceAll("[\\\\/:*?\"<>|]", "_");
            fileName += "_" + System.currentTimeMillis() + ".docx";
        }

        // 确保文件名是有效的
        return fileName.replaceAll("[^\\w\\.\\-]", "_"); // 只保留字母、数字、下划线、点和连字符
    }
} 