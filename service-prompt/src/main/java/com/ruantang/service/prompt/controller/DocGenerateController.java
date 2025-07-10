package com.ruantang.service.prompt.controller;

import com.ruantang.commons.api.ApiResult;
import com.ruantang.service.prompt.model.DocGenerateRequest;
import com.ruantang.service.prompt.model.DocGenerateResponse;
import com.ruantang.service.prompt.model.DocWordExportRequest;
import com.ruantang.service.prompt.service.DocGenerateService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
            String fileName = generateFileName(request.getTitle(), request.getTitle());
            log.info("文档导出成功，文件名：{}", fileName);

            HttpHeaders headers = new HttpHeaders();
            // 设置正确的MIME类型
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));

            // 使用 ContentDisposition 工具类构建标准格式的 Content-Disposition 头部
            ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                    .filename(fileName)
                    .build();
            headers.setContentDisposition(contentDisposition);

            headers.setContentLength(wordData.length);
            return ResponseEntity.ok().headers(headers).body(wordData);

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
        return fileName;
    }

    /**
     * 编码文件名以符合 Content-Disposition 标准
     *
     * @param fileName 文件名
     * @return 编码后的文件名
     */
    private String encodeFileNameForContentDisposition(String fileName) {
        try {
            // RFC 6266 标准格式
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name())
                    .replaceAll("\\+", "%20"); // 替换空格编码

            // 构建标准格式的文件名
            return "filename=\"" + encodedFileName + "\"; " +
                    "filename*=UTF-8''" + encodedFileName;
        } catch (Exception e) {
            log.error("文件名编码失败: {}", e.getMessage());
            return fileName; // 如果编码失败，返回原始文件名
        }
    }
} 