package com.anonym.module.member;

import com.anonym.common.constant.SwaggerTagConst;
import com.anonym.common.domain.PageInfoDTO;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.module.member.domian.MemberAddDTO;
import com.anonym.module.member.domian.MemberQueryDTO;
import com.anonym.module.member.domian.MemberUpdateDTO;
import com.anonym.module.member.domian.MemberVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author lizongliang
 * @date 2019-08-29 10:39
 */
@RestController
@Api(tags = {SwaggerTagConst.MEMBER})
public class MemberController {

    @Autowired
    private MemberService memberService;

    @ApiOperation(value = "分页查询会员", notes = "@author lizongliang")
    @PostMapping("/member/page/query")
    public ResponseDTO<PageInfoDTO<MemberVO>> queryByPage(@RequestBody @Valid MemberQueryDTO queryDTO) {
        return memberService.queryByPage(queryDTO);
    }

    @ApiOperation(value = "新增会员", notes = "@author lizongliang")
    @PostMapping("/member/add")
    public ResponseDTO<String> add(@RequestBody @Valid MemberAddDTO addDTO) {
        return memberService.add(addDTO);
    }

    @ApiOperation(value = "修改会员信息", notes = "@author lizongliang")
    @PostMapping("/member/update")
    public ResponseDTO<String> update(@RequestBody @Valid MemberUpdateDTO updateDTO) {
        return memberService.update(updateDTO);
    }

    @ApiOperation(value = "会员消费", notes = "@author lizongliang")
    @GetMapping("/member/consume/{memberId}")
    public ResponseDTO<String> consume(@PathVariable Integer memberId) {
        return memberService.consume(memberId);
    }


}
