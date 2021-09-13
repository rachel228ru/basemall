package com.medusa.gruul.common.core.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.medusa.gruul.common.core.constant.enums.CommonEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>Descrition: http请求,分页列表响应主题</P>
 * <p>Version: 1.0</p>
 * <p>Author Mr.zhaozheng a</P>
 * <p>Date: 2019-11-21 13:18</p>
 */
@Data
@ApiModel("响应主体,需要分页的obj")
public class ResultPageList<T> extends ResultList<T> {

    @ApiModelProperty(value = "totalPage-总页数,totalCount-总条数,currentPage-当前页,perPage-当前页")
    private Long totalPage,
            totalCount,
            currentPage,
            perPage;

    @Deprecated
    public ResultPageList() {
    }

    @Deprecated
    public ResultPageList(Integer code, String msg) {
        super(code, msg);
    }

    public ResultPageList(CommonEnum commonEnum) {
        super(commonEnum);
    }

    public ResultPageList(CommonEnum commonEnum, IPage page, List list) {
        super(commonEnum);
        this.totalPage = page.getPages();
        this.totalCount = page.getTotal();
        this.currentPage = page.getCurrent();
        this.perPage = page.getSize();
        super.setData(list);
    }

    @Deprecated
    public ResultPageList(Integer code, String msg, IPage page, List list) {
        super(code, msg);
        this.totalPage = page.getPages();
        this.totalCount = page.getTotal();
        this.currentPage = page.getCurrent();
        this.perPage = page.getSize();
        super.setData(list);
    }

    @Override
    public ResultPageList<T> setData(List<T> data) {
        super.setData(data);
        return this;
    }
}
