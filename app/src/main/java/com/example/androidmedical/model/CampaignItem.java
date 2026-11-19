package com.example.androidmedical.model;

import java.util.List;

public class CampaignItem {
    private int campaignId;
    private String title;
    private String location;
    private String eventDate;
    private int adminId;
    private String createdAt;
    private int staffCount;
    private List<StaffAssignment> staffAssignments;

    public int getCampaignId() {
        return campaignId;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getEventDate() {
        return eventDate;
    }

    public int getAdminId() {
        return adminId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public int getStaffCount() {
        return staffCount;
    }

    public List<StaffAssignment> getStaffAssignments() {
        return staffAssignments;
    }
}
