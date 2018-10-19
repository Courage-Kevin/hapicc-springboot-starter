package com.hapicc.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.pagehelper.PageInfo;
import org.springframework.data.domain.Page;

@JsonInclude(Include.NON_NULL)
public class JqGridResult {

    /**
     * 第几页
     */
    private Integer page;

    /**
     * 总页数
     */
    private Integer total;

    /**
     * 总记录数
     */
    private Long records;

    /**
     * 记录列表
     */
    private List<?> rows;

    /**
     * 用户自定义数据
     */
    private Object userdata;

    public JqGridResult() {
    }

    public JqGridResult(Page<?> page, boolean needTotal) {
        this.page = page.getNumber() + 1;
        this.rows = page.getContent();
        if (needTotal) {
            this.total = page.getTotalPages();
            this.records = page.getTotalElements();
        }
    }

    public JqGridResult(PageInfo<?> pageInfo, boolean needTotal) {
        this.page = pageInfo.getPageNum();
        this.rows = pageInfo.getList();
        if (needTotal) {
            this.total = pageInfo.getPages();
            this.records = pageInfo.getTotal();
        }
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Long getRecords() {
        return records;
    }

    public void setRecords(Long records) {
        this.records = records;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }

    public Object getUserdata() {
        return userdata;
    }

    public void setUserdata(Object userdata) {
        this.userdata = userdata;
    }
}
