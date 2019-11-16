package com.anonym.module.file.service;

import com.anonym.common.domain.ResponseDTO;
import com.anonym.module.file.domain.domain.UploadVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 文件服务接口
 */
public interface IFileService {

    /**
     * 文件上传
     *
     * @param multipartFile
     * @param folder
     * @return
     */
    ResponseDTO<UploadVO> fileUpload(MultipartFile multipartFile, String folder);

    /**
     * 获取文件url
     *
     * @param fileKey
     * @return
     */
    ResponseDTO<String> getFileUrl(String fileKey);

    /**
     * 文件下载
     *
     * @param key
     * @return
     */
    ResponseDTO<byte[]> fileDownload(String key) throws IOException;

    /**
     * 生成文件名字
     * 当前年月日时分秒 +32位 uuid + 文件格式后缀
     *
     * @param originalFileName
     * @return String
     */
    default String generateFileName(String originalFileName) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHH"));
        String fileType = originalFileName.substring(originalFileName.lastIndexOf("."));
        return uuid + time + fileType;
    }

    public static void main(String[] args) {
        String originalFileName = "listen.png";
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHH"));
        String fileType = originalFileName.substring(originalFileName.lastIndexOf("."));
        System.out.println(uuid + time + fileType);
    }

    /**
     * 获取文件类型
     *
     * @param fileExt
     * @return
     */
    default String getContentType(String fileExt) {
        // 文件的后缀名
        if ("bmp".equalsIgnoreCase(fileExt)) {
            return "image/bmp";
        }
        if ("gif".equalsIgnoreCase(fileExt)) {
            return "image/gif";
        }
        if ("jpeg".equalsIgnoreCase(fileExt) || "jpg".equalsIgnoreCase(fileExt) || ".png".equalsIgnoreCase(fileExt)) {
            return "image/jpeg";
        }
        if ("png".equalsIgnoreCase(fileExt)) {
            return "image/png";
        }
        if ("html".equalsIgnoreCase(fileExt)) {
            return "text/html";
        }
        if ("txt".equalsIgnoreCase(fileExt)) {
            return "text/plain";
        }
        if ("vsd".equalsIgnoreCase(fileExt)) {
            return "application/vnd.visio";
        }
        if ("ppt".equalsIgnoreCase(fileExt) || "pptx".equalsIgnoreCase(fileExt)) {
            return "application/vnd.ms-powerpoint";
        }
        if ("doc".equalsIgnoreCase(fileExt) || "docx".equalsIgnoreCase(fileExt)) {
            return "application/msword";
        }
        if ("xml".equalsIgnoreCase(fileExt)) {
            return "text/xml";
        }
        return "";
    }

    /**
     * 根据不同的浏览器 返回对应编码的文件名称
     *
     * @param fileName
     * @param userAgent
     * @return
     */
    default String getDownloadFileNameByUA(String fileName, String userAgent) {
        try {
            if (userAgent.toLowerCase().indexOf("firefox") > 0) {
                // firefox浏览器
                fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
            } else if (userAgent.toUpperCase().indexOf("MSIE") > 0) {
                // IE浏览器
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } else if (userAgent.toUpperCase().indexOf("EDGE") > 0) {
                // WIN10浏览器
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } else if (userAgent.toUpperCase().indexOf("CHROME") > 0) {
                // 谷歌
                fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
            } else {
                //万能乱码问题解决
                fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            }
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        return fileName;
    }
}
