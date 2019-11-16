package com.anonym.module.log.orderoperatelog;

import com.anonym.module.log.orderoperatelog.domain.entity.OrderOperateLogEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 各种单据操作记录
 * Mapper 接口
 */
@Mapper
@Component
public interface OrderOperateLogDao extends BaseMapper<OrderOperateLogEntity> {

    List<OrderOperateLogEntity> listOrderOperateLogsByOrderTypeAndOrderId(@Param("orderId") Long orderId, @Param("orderTypeList") List<Integer> orderTypeList);

    List<OrderOperateLogEntity> listOrderOperateLogsByOrderTypeAndOrderIds(@Param("orderIds") List<Long> orderIds, @Param("orderTypeList") List<Integer> orderTypeList);

    void batchInsert(List<OrderOperateLogEntity> list);

    List<OrderOperateLogEntity> selectByOrderTypeAndOrderId(Page page, @Param("orderType") Integer orderType, @Param("orderId") Long orderId);
}
