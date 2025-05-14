package org.example.features.campaign;

import org.example.database.EntityMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CampaignMapper implements EntityMapper<Campaign> {

    @Override
    public Campaign map(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        CampaignType type = CampaignType.valueOf(rs.getString("type"));
        LocalDate startDate = rs.getDate("start_date").toLocalDate();
        LocalDate endDate = rs.getDate("end_date").toLocalDate();

        String imageUrlsStr = rs.getString("image_urls"); // May be null if no images
        List<String> imageUrls = imageUrlsStr != null
                ? Arrays.stream(imageUrlsStr.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toCollection(ArrayList::new))
                : new ArrayList<>();

        return new Campaign(id, name, description, type, startDate, endDate, imageUrls);
    }
}
