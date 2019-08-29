package com.anonym.module.member;

import com.anonym.module.member.domian.MemberEntity;
import com.anonym.module.member.domian.MemberQueryDTO;
import com.anonym.module.member.domian.MemberVO;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lizongliang
 * @date 2019-08-29 10:49
 */
@Mapper
@Component
public interface MemberDao extends BaseMapper<MemberEntity> {

    /**
     * 分页查询会员
     *
     * @param page
     * @param queryDTO
     * @return
     */
    List<MemberVO> queryByPage(Page page, @Param("queryDTO") MemberQueryDTO queryDTO);

    /**
     * 根据名字+服务项查询
     *
     * @param name
     * @param excludeId
     * @return
     */
    MemberEntity getByName(@Param("name") String name, @Param("phone") String phone, @Param("serviceItemId") Integer serviceItemId, @Param("excludeId") Integer excludeId);


}
