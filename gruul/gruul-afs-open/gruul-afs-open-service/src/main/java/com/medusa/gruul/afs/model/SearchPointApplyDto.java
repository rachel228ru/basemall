package com.medusa.gruul.afs.model;

import com.medusa.gruul.common.core.param.QueryParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * The type Search point apply dto.
 *
 * @author alan
 */
@Data
public class SearchPointApplyDto extends QueryParam {

    @ApiModelProperty(value = "售后编号，模糊搜索")
    private String no;

}
