package com.anonym.module.message.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author lizongliang
 * @date 2019-12-31 18:51
 */
@Data
public class MessageBatchDeleteAdminDTO {

    @ApiModelProperty("留言id集合")
    @Size(min = 1, message = "最少一个留言id")
    private List<Long> messageIdList;
}
