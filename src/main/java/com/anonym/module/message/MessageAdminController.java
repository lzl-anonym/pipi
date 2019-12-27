package com.anonym.module.message;

import com.anonym.common.anno.AdminAuthorityLevel;
import com.anonym.common.controller.AdminBaseController;
import com.anonym.common.domain.PageResultDTO;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.constant.SwaggerTagConst;
import com.anonym.module.message.domain.dto.MessageAppQueryDTO;
import com.anonym.module.message.domain.vo.MessageAppVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 留言业务层控制层
 *
 * @author lizongliang
 * @date 2019-12-20 18:48
 */
@Api(tags = SwaggerTagConst.Admin.MESSAGE)
@RestController
public class MessageAdminController extends AdminBaseController {

    @Autowired
    private MessageService messageService;

    @AdminAuthorityLevel
    @ApiOperation("分页查询 @author lizongliang")
    @PostMapping("/message/page/query")
    public ResponseDTO<PageResultDTO<MessageAppVO>> queryByPage(@RequestBody MessageAppQueryDTO queryDTO) {
        return messageService.queryByPage(queryDTO);
    }


    @AdminAuthorityLevel
    @ApiOperation("留言详情 @author lizongliang")
    @GetMapping("/message/detail/{messageId}")
    public ResponseDTO<MessageAppVO> detail(@PathVariable Long messageId) {
        return messageService.detail(messageId);
    }

    @AdminAuthorityLevel
    @ApiOperation("删除留言 @author lizongliang")
    @GetMapping("/message/delete/{id}")
    public ResponseDTO<String> delete(@PathVariable("id") Long messageId) {
        return messageService.delete(messageId);
    }
}
