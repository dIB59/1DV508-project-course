package org.example.features.campaign;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import org.example.database.EntityMapper;

public class CampaignMapper implements EntityMapper<Campaign> {

    @Override
    public Campaign map(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        CampaignType type = CampaignType.valueOf(rs.getString("type"));
        LocalDate startDate = rs.getDate("start_date").toLocalDate();
        LocalDate endDate = rs.getDate("end_date").toLocalDate();

        String imageUrl= rs.getString("image_url");
        return new Campaign(id, name, description, type, startDate, endDate, imageUrl);
    }
}
