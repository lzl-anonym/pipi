package com.anonym.module.file;

import com.anonym.common.anno.AdminAuthorityLevel;
import com.anonym.common.anno.AppAuthorityLevel;
import com.anonym.common.controller.CommonBaseController;
import com.anonym.common.domain.PageResultDTO;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.constant.SwaggerTagConst;
import com.anonym.module.file.constant.FileFolderTypeEnum;
import com.anonym.module.file.constant.FileServiceTypeEnum;
import com.anonym.module.file.domain.domain.FileQueryDTO;
import com.anonym.module.file.domain.domain.FileVO;
import com.anonym.module.file.domain.domain.UploadVO;
import com.anonym.module.file.service.FileService;
import com.anonym.utils.SmartRequestTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@RestController
@Api(tags = {SwaggerTagConst.Common.FILE_SERVICE})
public class FileController extends CommonBaseController {

    @Autowired
    private FileService fileService;

    @AdminAuthorityLevel
    @AppAuthorityLevel
    @ApiOperation(value = "文件上传", notes = FileFolderTypeEnum.INFO)
    @PostMapping("/file/upload/{folder}")
    public ResponseDTO<UploadVO> upload(MultipartFile file, @PathVariable Integer folder) {
        Long requestUserId = SmartRequestTokenUtil.getRequestUserId();
        return fileService.fileUpload(file, FileServiceTypeEnum.LOCAL, folder, requestUserId);
    }

    @AdminAuthorityLevel
    @AppAuthorityLevel
    @ApiOperation("获取文件URL：根据fileKey")
    @GetMapping("/file/url")
    public ResponseDTO<String> getUrl(String fileKey) {
        return fileService.getFileUrl(fileKey, FileServiceTypeEnum.LOCAL);
    }

    @ApiOperation(value = "系统文件查询")
    @PostMapping("/file/query")
    public ResponseDTO<PageResultDTO<FileVO>> queryListByPage(@RequestBody FileQueryDTO queryDTO) {
        return fileService.queryListByPage(queryDTO);
    }

    @AdminAuthorityLevel
    @AppAuthorityLevel
    @ApiOperation(value = "下载文件流（根据fileId）")
    @GetMapping("/file/downLoad/{id}")
    public ResponseEntity<Object> downLoadById(@PathVariable Long id, HttpServletRequest request) throws IOException {
        String ua = request.getHeader("User-Agent");
        return fileService.downLoadById(id, ua);
    }

    @AdminAuthorityLevel
    @AppAuthorityLevel
    @ApiOperation(value = "下载文件流（根据fileKey）")
    @GetMapping("/file/downLoad")
    public ResponseEntity<Object> downLoad(String fileKey, HttpServletRequest request) throws IOException {
        String ua = request.getHeader("User-Agent");
        return fileService.downloadByFileKey(FileServiceTypeEnum.LOCAL, fileKey, ua);
    }

}
