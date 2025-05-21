package org.example.features.campaign;

import java.time.LocalDate;
import org.example.database.Identifiable;

public class Campaign implements Identifiable<Integer> {
    private Integer id;
    private String name;
    private String description;
    private CampaignType type;
    private LocalDate startDate;
    private LocalDate endDate;
    private String imageUrl;

    public Campaign(String name, String description, CampaignType type, LocalDate startDate, LocalDate endDate, String imageUrl) {
        this.id = 0; // Default ID for new campaigns
        this.name = name;
        this.description = description;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.imageUrl = imageUrl;
    }

    public Campaign(Integer id, String name, String description, CampaignType type, LocalDate startDate, LocalDate endDate, String imageUrl) {
        this.id = id; // Default ID for new campaigns
        this.name = name;
        this.description = description;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.imageUrl = imageUrl;
    }

    public Integer getId() {
        return id;
    }

    public boolean isActive() {
        //LocalDate today = LocalDate.now();
        //return (startDate != null && endDate != null) && (today.isAfter(startDate) && today.isBefore(endDate));
        return true;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CampaignType getType() {
        return type;
    }

    public void setType(CampaignType type) {
        this.type = type;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
