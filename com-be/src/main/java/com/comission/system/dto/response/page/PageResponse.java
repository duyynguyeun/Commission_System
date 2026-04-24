package com.comission.system.dto.response.page;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PageResponse<T> {
    private PageCustom<T> pagination;
    private List<T> data;

    public PageResponse(Page<T> page) {
        this.pagination = new PageCustom<T>(page);
        this.data = page.getContent();
    }
}
