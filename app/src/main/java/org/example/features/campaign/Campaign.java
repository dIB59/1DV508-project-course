package org.example.features.campaign;

import org.example.database.Identifiable;

import java.time.LocalDate;
import java.util.List;


public class Campaign implements Identifiable<Integer> {
    private Integer id;
    private String name;
    private String description;
    private CampaignType type;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<String> imageUrls;

    public Campaign(String name, String description, CampaignType type, LocalDate startDate, LocalDate endDate, List<String> imageUrls) {
        this.id = 0; // Default ID for new campaigns
        this.name = name;
        this.description = description;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.imageUrls = imageUrls;
    }

    public Campaign(Integer id, String name, String description, CampaignType type, LocalDate startDate, LocalDate endDate, List<String> imageUrls) {
        this.id = id; // Default ID for new campaigns
        this.name = name;
        this.description = description;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.imageUrls = imageUrls;
    }

    public Integer getId() {
        return id;
    }

    public boolean isActive() {
        LocalDate today = LocalDate.now();
        return (startDate != null && endDate != null) && (today.isAfter(startDate) && today.isBefore(endDate));
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

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public void addImageUrl(String imageUrl) {
        this.imageUrls.add(imageUrl);
    }
}
