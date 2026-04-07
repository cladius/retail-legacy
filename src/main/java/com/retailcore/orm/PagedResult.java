package com.retailcore.orm;

import java.util.List;

public class PagedResult<T> {

    private List<T> items;
    private int page;
    private int pageSize;
    private int totalCount;
    private int totalPages;

    public PagedResult() {
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean hasNextPage() {
        return page < totalPages;
    }

    public boolean hasPreviousPage() {
        return page > 1;
    }

    public boolean isEmpty() {
        return items == null || items.isEmpty();
    }

    public int getItemCount() {
        return items != null ? items.size() : 0;
    }
}
