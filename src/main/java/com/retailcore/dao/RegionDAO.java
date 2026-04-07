package com.retailcore.dao;

import com.retailcore.entity.Region;
import com.retailcore.orm.BaseDAO;
import com.retailcore.orm.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

public class RegionDAO extends BaseDAO<Region> {

    private static RegionDAO instance;

    private RegionDAO() {
        super(Region.class);
    }

    public static synchronized RegionDAO getInstance() {
        if (instance == null) {
            instance = new RegionDAO();
        }
        return instance;
    }

    public Region findByRegionCode(String regionCode) throws SQLException {
        return findOneByColumn("RegionCode", regionCode);
    }

    public List<Region> findByCountry(String countryCode) throws SQLException {
        QueryBuilder qb = QueryBuilder.select()
                .from(metadata.getFullTableName())
                .whereEquals("CountryCode", countryCode)
                .whereEquals("IsActive", true)
                .orderBy("RegionName", QueryBuilder.SortDirection.ASC);
        return findByQuery(qb);
    }
}
