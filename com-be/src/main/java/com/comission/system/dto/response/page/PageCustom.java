package com.comission.system.dto.response.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
@Builder
public class PageCustom<T> {
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;

    public PageCustom(Page<T> page) {
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.currentPage = page.getNumber();
        this.pageSize = page.getSize();
    }
}
