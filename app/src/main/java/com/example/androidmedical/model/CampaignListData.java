package com.example.androidmedical.model;

import java.util.List;

public class CampaignListData {
    private int page;
    private int pageSize;
    private int totalItems;
    private int totalPages;
    private List<CampaignItem> items;

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<CampaignItem> getItems() {
        return items;
    }
}
