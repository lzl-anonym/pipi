package com.anonym.module.admin.notice;

import com.anonym.common.anno.AdminNoValidPrivilege;
import com.anonym.common.domain.PageBaseDTO;
import com.anonym.common.domain.PageResultDTO;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.constant.SwaggerTagConst;
import com.anonym.module.admin.notice.domain.dto.*;
import com.anonym.utils.SmartRequestTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@Api(tags = {SwaggerTagConst.Admin.MANAGER_NOTICE})
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @ApiOperation(value = "分页查询全部消息")
    @PostMapping("/admin/notice/page/query")
    @AdminNoValidPrivilege
    public ResponseDTO<PageResultDTO<NoticeVO>> queryByPage(@RequestBody NoticeQueryDTO queryDTO) {
        return noticeService.queryByPage(queryDTO);
    }

    @ApiOperation(value = "获取已收取的所有消息")
    @PostMapping("/admin/notice/receive/page/query")
    @AdminNoValidPrivilege
    public ResponseDTO<PageResultDTO<NoticeReceiveDTO>> queryReceiveByPage(@RequestBody NoticeReceiveQueryDTO queryDTO) {
        return noticeService.queryReceiveByPage(queryDTO, SmartRequestTokenUtil.getRequestUser());
    }

    @ApiOperation(value = "分页查询未读消息")
    @PostMapping("/admin/notice/unread/page/query")
    @AdminNoValidPrivilege
    public ResponseDTO<PageResultDTO<NoticeVO>> queryUnreadByPage(@RequestBody PageBaseDTO queryDTO) {
        return noticeService.queryUnreadByPage(queryDTO, SmartRequestTokenUtil.getRequestUser());
    }

    @ApiOperation(value = "添加")
    @PostMapping("/admin/notice/add")
    @AdminNoValidPrivilege
    public ResponseDTO<String> add(@RequestBody @Valid NoticeAddDTO addTO) {
        return noticeService.add(addTO, SmartRequestTokenUtil.getRequestUser());
    }

    @ApiOperation(value = "修改")
    @PostMapping("/admin/notice/update")
    @AdminNoValidPrivilege
    public ResponseDTO<String> update(@RequestBody @Valid NoticeUpdateDTO updateDTO) {
        return noticeService.update(updateDTO);
    }

    @ApiOperation(value = "删除")
    @GetMapping("/admin/notice/delete/{id}")
    @AdminNoValidPrivilege
    public ResponseDTO<String> delete(@PathVariable("id") Long id) {
        return noticeService.delete(id);
    }

    @ApiOperation(value = "详情")
    @GetMapping("/admin/notice/detail/{id}")
    @AdminNoValidPrivilege
    public ResponseDTO<NoticeDetailVO> detail(@PathVariable("id") Long id) {
        return noticeService.detail(id);
    }

    @ApiOperation(value = "发送")
    @GetMapping("/admin/notice/send/{id}")
    @AdminNoValidPrivilege
    public ResponseDTO<NoticeDetailVO> send(@PathVariable("id") Long id) {
        return noticeService.send(id, SmartRequestTokenUtil.getRequestUser());
    }

    @ApiOperation(value = "读取消息")
    @GetMapping("/admin/notice/read/{id}")
    @AdminNoValidPrivilege
    public ResponseDTO<NoticeDetailVO> read(@PathVariable("id") Long id) {
        return noticeService.read(id, SmartRequestTokenUtil.getRequestUser());
    }
}
