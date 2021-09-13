package com.medusa.gruul.common.core.util;

import com.medusa.gruul.common.core.constant.enums.CommonEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>Descrition: http请求,列表不需要分页响应主题</P>
 * <p>Version: 1.0</p>
 * <p>Author Mr.zhaozheng a</P>
 * <p>Date: 2019-11-21 13:18</p>
 */
@Data
@ApiModel(description = "响应信息主体,不需要分页的obj")
public class ResultList<T> implements Serializable {
    @ApiModelProperty(value = "返回标记：200=成功,500/400=失败")
    private Integer code;
    @ApiModelProperty(value = "返回标记对应提示：")
    private String msg;
    @ApiModelProperty(value = "返回具体数据内容")
    private List<T> data;

    @Deprecated
    public ResultList() {
    }

    @Deprecated
    public ResultList(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultList(CommonEnum commonEnum) {
        this.code = commonEnum.getCode();
        this.msg = commonEnum.getMsg();
    }

    public List<T> getData() {
        return data;
    }

    public ResultList<T> setData(List<T> data) {
        this.data = data;
        return this;
    }
}
