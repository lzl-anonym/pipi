package com.anonym.module.file.service;

import com.anonym.common.domain.ResponseDTO;
import com.anonym.module.file.constant.FileResponseCodeConst;
import com.anonym.module.file.constant.FileServiceNameConst;
import com.anonym.module.file.domain.domain.UploadVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


@Slf4j
@Service(FileServiceNameConst.LOCAL)
public class FileServiceLocal implements IFileService {

    @Value("${file-upload-service.path}")
    private String fileParentPath;

    @Value("${file-upload-service.getUrl}")
    private String fileUrl;

    @Override
    public ResponseDTO<UploadVO> fileUpload(MultipartFile multipartFile, String path) {

        String fileUploadPath = fileParentPath + path;

        File directory = new File(fileUploadPath);
        if (!directory.exists()) {
            // 目录不存在，新建
            directory.mkdirs();
        }

        String newFileName = this.generateFileName(multipartFile.getOriginalFilename());
        File fileTemp = new File(directory, newFileName);
        UploadVO uploadVO = new UploadVO();
        try {
            multipartFile.transferTo(fileTemp);
            String fileKey = path + newFileName;
            uploadVO.setFileUrl(fileUrl + fileKey);
            uploadVO.setFileName(newFileName);
            uploadVO.setFileKey(fileKey);
            uploadVO.setFileSize(multipartFile.getSize());
        } catch (IOException e) {
            if (fileTemp.exists() && fileTemp.isFile()) {
                fileTemp.delete();
            }
            log.error("", e);
            return ResponseDTO.wrap(FileResponseCodeConst.UPLOAD_ERROR);
        }
        return ResponseDTO.succData(uploadVO);
    }

    @Override
    public ResponseDTO<String> getFileUrl(String path) {
        String url = fileUrl + path;
        return ResponseDTO.succData(url);
    }

    @Override
    public ResponseDTO<byte[]> fileDownload(String key) throws IOException {
        String url = fileParentPath + key;
        // 创建文件
        File file = new File(url);
        byte[] bytes = FileCopyUtils.copyToByteArray(file);
        return ResponseDTO.succData(bytes);
    }
}
