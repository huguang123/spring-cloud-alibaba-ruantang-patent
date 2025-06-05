package com.ruantang.service.prompt.controller;

import com.ruantang.commons.api.ApiResult;
import com.ruantang.service.prompt.model.DocGenerateRequest;
import com.ruantang.service.prompt.model.DocGenerateResponse;
import com.ruantang.service.prompt.model.DocWordExportRequest;
import com.ruantang.service.prompt.service.DocGenerateService;
import org.springframework.beans.factory.annotation.Autowired;
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
        DocGenerateResponse response = docGenerateService.generateDoc(request);
        return ApiResult.success(response, "文档生成成功");
    }
    
    /**
     * 导出Word文档
     */
    @PostMapping("/export-word")
    public ResponseEntity<byte[]> exportWordDocument(@RequestBody DocWordExportRequest request) {
        try {
            // 调用服务生成Word文档
            byte[] wordData = docGenerateService.exportWordDocument(request);
            
            // 生成文件名
            String fileName = generateFileName(request.getTitle(), request.getFileName());
            
            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", 
                    URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            headers.setContentLength(wordData.length);
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(wordData);
                    
        } catch (Exception e) {
            // 发生错误时返回错误响应
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
        if (StringUtils.hasText(customFileName)) {
            return customFileName.endsWith(".docx") ? customFileName : customFileName + ".docx";
        }
        
        String fileName = StringUtils.hasText(title) ? title : "技术文档";
        // 移除文件名中的非法字符
        fileName = fileName.replaceAll("[\\\\/:*?\"<>|]", "_");
        return fileName + "_" + System.currentTimeMillis() + ".docx";
    }
} 