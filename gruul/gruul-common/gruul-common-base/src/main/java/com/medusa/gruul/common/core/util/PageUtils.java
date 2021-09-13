package com.medusa.gruul.common.core.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 分页工具类
 * @Author: alan
 * @Date: 2019/8/29 21:40
 */
@Data
public class PageUtils<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 总记录数
	 */
	private int total;
	/**
	 * 每页记录数
	 */
	private int size;
	/**
	 * 总页数
	 */
	private int pages;
	/**
	 * 当前页数
	 */
	private int current;
	/**
	 * 列表数据
	 */
	private List<T> list;

    /**
     * 分页
     *
     * @param list    列表数据
     * @param total   总记录数
     * @param size    每页记录数
     * @param current 当前页数
     */
    public PageUtils(List<T> list, int total, int size, int current) {
        this.list = list;
        this.total = total;
        this.size = size;
        this.current = current;
        this.pages = (int) Math.ceil((double) total / size);
    }

    /**
     * 分页
     */
    public PageUtils(IPage<T> page) {
        this.list = page.getRecords();
        this.total = (int) page.getTotal();
        this.size = (int) page.getSize();
        this.current = (int) page.getCurrent();
        this.pages = (int) page.getPages();
    }

    public PageUtils() {
    }
}