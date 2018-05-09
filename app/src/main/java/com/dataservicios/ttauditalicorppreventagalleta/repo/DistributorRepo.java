package com.dataservicios.ttauditalicorppreventagalleta.repo;

import android.content.Context;

import com.dataservicios.ttauditalicorppreventagalleta.db.DatabaseHelper;
import com.dataservicios.ttauditalicorppreventagalleta.db.DatabaseManager;
import com.dataservicios.ttauditalicorppreventagalleta.model.Distributor;

import java.sql.SQLException;
import java.util.List;

public class DistributorRepo implements Crud {
    private DatabaseHelper helper;

    public DistributorRepo(Context context) {

        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }

    @Override
    public int create(Object item) {
        int index = -1;
        Distributor object = (Distributor) item;
        try {
            index = helper.getDistributorDao().create(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return index;
    }


    @Override
    public int update(Object item) {

        int index = -1;

        Distributor object = (Distributor) item;

        try {
            helper.getDistributorDao().update(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }


    @Override
    public int delete(Object item) {

        int index = -1;

        Distributor object = (Distributor) item;

        try {
            helper.getDistributorDao().delete(object);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }

    @Override
    public int deleteAll() {

        List<Distributor> items = null;
        int counter = 0;
        try {
            items = helper.getDistributorDao().queryForAll();

            for (Distributor object : items) {
                // do something with object
                helper.getDistributorDao().deleteById(object.getId());
                counter ++ ;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return counter;
    }


    @Override
    public Object findById(int id) {

        Distributor wishList = null;
        try {
            wishList = helper.getDistributorDao().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }


    @Override
    public List<?> findAll() {

        List<Distributor> items = null;

        try {
            items = helper.getDistributorDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;

    }

    @Override
    public Object findFirstReg() {

        Object wishList = null;
        try {
            wishList = helper.getDistributorDao().queryBuilder().queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }

    @Override
    public long countReg() {
        long count = 0;
        try {
            count = helper.getDistributorDao().countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

}