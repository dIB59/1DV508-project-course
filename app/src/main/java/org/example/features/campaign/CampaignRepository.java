package org.example.features.campaign;

import org.example.database.CrudRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CampaignRepository {
    private final CampaignMapper campaignMapper;
    private final Connection connection;

    public CampaignRepository(Connection connection, CampaignMapper campaignMapper) {
        this.connection = connection;
        this.campaignMapper = campaignMapper;
    }

    public List<Campaign> findActiveCampaigns() {
        String sql = """
                SELECT
                id,
                name,
                description,
                type,
                start_date,
                end_date,
                image_url
            FROM Campaign
            WHERE start_date <= CURRENT_DATE AND end_date >= CURRENT_DATE
            """;

        try (PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
            List<Campaign> campaigns = new ArrayList<>();
            while (rs.next()) {
                Campaign campaign = campaignMapper.map(rs);
                if (campaign.isActive()) {
                    campaigns.add(campaign);
                }
            }
            return campaigns;
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching active campaigns", e);
        }
    }
}
