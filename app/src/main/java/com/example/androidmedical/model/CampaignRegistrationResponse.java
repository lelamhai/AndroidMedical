package com.example.androidmedical.model;

import java.util.List;

public class CampaignRegistrationResponse {
    private String message;
    private List<CampaignItem> data;

    public String getMessage() {
        return message;
    }

    public List<CampaignItem> getData() {
        return data;
    }
}
