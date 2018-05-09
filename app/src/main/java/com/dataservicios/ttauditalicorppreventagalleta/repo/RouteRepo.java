package com.dataservicios.ttauditalicorppreventagalleta.repo;

import android.content.Context;

import com.dataservicios.ttauditalicorppreventagalleta.db.DatabaseHelper;
import com.dataservicios.ttauditalicorppreventagalleta.db.DatabaseManager;
import com.dataservicios.ttauditalicorppreventagalleta.model.Route;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by jcdia on 11/05/2017.
 */

public class RouteRepo implements Crud {
    private DatabaseHelper helper;

    public RouteRepo(Context context) {

        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }

    @Override
    public int create(Object item) {
        int index = -1;
        Route object = (Route) item;
        try {
            index = helper.getRouteDao().create(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return index;
    }


    @Override
    public int update(Object item) {

        int index = -1;

        Route object = (Route) item;

        try {
            helper.getRouteDao().update(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }


    @Override
    public int delete(Object item) {

        int index = -1;

        Route object = (Route) item;

        try {
            helper.getRouteDao().delete(object);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }

    @Override
    public int deleteAll() {

        List<Route> items = null;
        int counter = 0;
        try {
            items = helper.getRouteDao().queryForAll();

            for (Route object : items) {
                // do something with object
                helper.getRouteDao().deleteById(object.getId());
                counter++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return counter;
    }


    @Override
    public Object findById(int id) {

        Route wishList = null;
        try {
            wishList = helper.getRouteDao().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }


    @Override
    public List<?> findAll() {

        List<Route> items = null;

        try {
            items = helper.getRouteDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;

    }

    @Override
    public Object findFirstReg() {

        Object wishList = null;
        try {
            wishList = helper.getRouteDao().queryBuilder().queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }
    @Override
    public long countReg() {
        long count = 0;
        try {
            count = helper.getRouteDao().countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

}



