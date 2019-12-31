package com.anonym.module.message;

import com.anonym.common.controller.AppBaseController;
import com.anonym.common.domain.PageResultDTO;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.constant.SwaggerTagConst;
import com.anonym.module.message.domain.dto.MessageAddAppDTO;
import com.anonym.module.message.domain.dto.MessageAppQueryDTO;
import com.anonym.module.message.domain.vo.MessageAppVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 留言业务层控制层
 *
 * @author lizongliang
 * @date 2019-12-20 18:48
 */
@Api(tags = SwaggerTagConst.App.MESSAGE)
@RestController
public class MessageAppController extends AppBaseController {

    @Autowired
    private MessageService messageService;

    @ApiOperation("分页查询 @author lizongliang")
    @PostMapping("/message/page/query")
    public ResponseDTO<PageResultDTO<MessageAppVO>> queryByPage(@RequestBody MessageAppQueryDTO queryDTO) {
        return messageService.queryByPage(queryDTO);
    }

    @ApiOperation("添加留言 @author lizongliang")
    @PostMapping("/message/add")
    public ResponseDTO<String> add(@RequestBody @Valid MessageAddAppDTO addTO) {
        return messageService.add(addTO);
    }


    @ApiOperation("留言详情 @author lizongliang")
    @GetMapping("/message/detail/{messageId}")
    public ResponseDTO<MessageAppVO> detail(@PathVariable Long messageId) {
        return messageService.detail(messageId);
    }
}
