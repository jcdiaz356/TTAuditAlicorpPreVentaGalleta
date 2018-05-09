package com.dataservicios.ttauditalicorppreventagalleta.repo;

import android.content.Context;

import com.dataservicios.ttauditalicorppreventagalleta.db.DatabaseHelper;
import com.dataservicios.ttauditalicorppreventagalleta.db.DatabaseManager;
import com.dataservicios.ttauditalicorppreventagalleta.model.Company;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by jcdia on 13/05/2017.
 */

public class CompanyRepo implements Crud {
    private DatabaseHelper helper;

    public CompanyRepo(Context context) {

        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }

    @Override
    public int create(Object item) {
        int index = -1;
        Company object = (Company) item;
        try {
            index = helper.getCompanyDao().create(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return index;
    }


    @Override
    public int update(Object item) {

        int index = -1;

        Company object = (Company) item;

        try {
            helper.getCompanyDao().update(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }


    @Override
    public int delete(Object item) {

        int index = -1;

        Company object = (Company) item;

        try {
            helper.getCompanyDao().delete(object);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }

    @Override
    public int deleteAll() {

        List<Company> items = null;
        int counter = 0;
        try {
            items = helper.getCompanyDao().queryForAll();

            for (Company object : items) {
                // do something with object
                helper.getCompanyDao().deleteById(object.getId());
                counter ++ ;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return counter;
    }


    @Override
    public Object findById(int id) {

        Company wishList = null;
        try {
            wishList = helper.getCompanyDao().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }


    @Override
    public List<?> findAll() {

        List<Company> items = null;

        try {
            items = helper.getCompanyDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;

    }
    @Override
    public Object findFirstReg() {

        Object wishList = null;
        try {
            wishList = helper.getCompanyDao().queryBuilder().queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }
    @Override
    public long countReg() {
        long count = 0;
        try {
            count = helper.getCompanyDao().countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

}