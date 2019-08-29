package com.anonym.module.member;

import com.anonym.common.domain.PageInfoDTO;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.module.member.domian.*;
import com.anonym.module.serviceitem.ServiceItemDao;
import com.anonym.module.serviceitem.ServiceItemResponseCodeConst;
import com.anonym.module.serviceitem.domian.ServiceItemEntity;
import com.anonym.utils.PaginationUtil;
import com.anonym.utils.SmartBeanUtil;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author lizongliang
 * @date 2019-08-29 10:50
 */
@Service
public class MemberService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private ServiceItemDao serviceItemDao;

    /**
     * 分页查询会员
     *
     * @param queryDTO
     * @return
     */
    public ResponseDTO<PageInfoDTO<MemberVO>> queryByPage(MemberQueryDTO queryDTO) {
        Page page = PaginationUtil.convert2PageQueryInfo(queryDTO);
        List<MemberVO> memberVOList = memberDao.queryByPage(page, queryDTO);
        PageInfoDTO<MemberVO> pageInfoDTO = PaginationUtil.convert2PageInfoDTO(page, memberVOList);
        return ResponseDTO.succData(pageInfoDTO);
    }

    /**
     * 新增会员
     *
     * @param addDTO
     * @return
     */
    public ResponseDTO<String> add(MemberAddDTO addDTO) {
        MemberEntity byName = memberDao.getByName(addDTO.getName(), addDTO.getName(), addDTO.getServiceItemId(), null);
        if (Objects.nonNull(byName)) {
            return ResponseDTO.wrap(ServiceItemResponseCodeConst.MEMBER_SERVICE_ITEM_EXISTS);
        }
        ServiceItemEntity serviceItemEntity = serviceItemDao.selectById(addDTO.getServiceItemId());
        if (Objects.isNull(serviceItemEntity)) {
            return ResponseDTO.wrap(ServiceItemResponseCodeConst.ITEM_NAME_NOT_EXISTS);
        }
        addDTO.setTotalNum(serviceItemEntity.getMemberPriceNum());
        addDTO.setUnusedNum(serviceItemEntity.getMemberPriceNum());
        addDTO.setMemberPrice(serviceItemEntity.getMemberPrice());
        MemberEntity memberEntity = SmartBeanUtil.copy(addDTO, MemberEntity.class);
        memberDao.insert(memberEntity);
        // TODO: 2019-08-29 添加新增日志
        return ResponseDTO.succ();
    }

    /**
     * 修改会员信息
     *
     * @param updateDTO
     * @return
     */
    public ResponseDTO<String> update(MemberUpdateDTO updateDTO) {
        MemberEntity memberEntity = memberDao.selectById(updateDTO.getMemberId());
        if (Objects.isNull(memberEntity)) {
            return ResponseDTO.wrap(ServiceItemResponseCodeConst.MEMBER_NOT_EXISTS);
        }
        MemberEntity byName = memberDao.getByName(updateDTO.getName(), updateDTO.getPhone(), null, updateDTO.getMemberId());
        if (Objects.nonNull(byName)) {
            return ResponseDTO.wrap(ServiceItemResponseCodeConst.MEMBER_NAME_EXISTS);
        }
        MemberEntity entity = SmartBeanUtil.copy(updateDTO, MemberEntity.class);
        memberDao.updateById(entity);
        // TODO: 2019-08-29 添加修改日志
        return ResponseDTO.succ();
    }

    /**
     * 会员消费
     *
     * @param memberId
     * @return
     */
    public ResponseDTO<String> consume(Integer memberId) {

        // 1.判断未消费次数是否大于0
        MemberEntity memberEntity = memberDao.selectById(memberId);
        if (Objects.isNull(memberEntity)) {
            return ResponseDTO.wrap(ServiceItemResponseCodeConst.MEMBER_NOT_EXISTS);
        }
        if (Objects.equals(memberEntity.getUnusedNum(), 0)) {
            return ResponseDTO.wrap(ServiceItemResponseCodeConst.OVER);
        }
        // 2.更新

        MemberEntity consume = new MemberEntity();
        Integer num = memberEntity.getUnusedNum() - 1;
        consume.setMemberId(memberEntity.getMemberId());
        consume.setUnusedNum(num);

        memberDao.updateById(consume);

        if (Objects.equals(memberEntity.getUnusedNum() - 1, 0)) {
            return ResponseDTO.succMsg("消费成功！该用户次数已经使用完毕，请提醒用户！");
        }
        // TODO: 2019-08-29 添加消费日志

        return ResponseDTO.succMsg("该会员还可以再消费" + num + "次");
    }


}
