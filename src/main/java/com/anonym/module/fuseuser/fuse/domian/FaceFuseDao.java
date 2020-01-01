package com.anonym.module.fuseuser.fuse.domian;

import com.anonym.module.fuseuser.domain.FaceFuseQueryDTO;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lizongliang
 * @date 2019-11-16 17:45
 */
@Component
@Mapper
public interface FaceFuseDao extends BaseMapper<FaceFuseEntity> {


	/**
	 * 分页查询融合结果
	 *
	 * @param page
	 * @param queryDTO
	 * @return
	 */
	List<FaceFuseVO> queryByPage(Page page, @Param("queryDTO") FaceFuseQueryDTO queryDTO);


	/**
	 * 批量删除融合结果
	 *
	 * @param idList
	 * @return
	 */
	Integer batchUpdateDelete(@Param("idList") List<Long> idList);

}
