package com.dataservicios.ttauditalicorppreventagalleta.repo;

import android.content.Context;

import com.dataservicios.ttauditalicorppreventagalleta.db.DatabaseHelper;
import com.dataservicios.ttauditalicorppreventagalleta.db.DatabaseManager;
import com.dataservicios.ttauditalicorppreventagalleta.model.RouteStoreTime;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by jcdia on 1/06/2017.
 */

public class RouteStoreTimeRepo  implements Crud {
    private DatabaseHelper helper;

    public RouteStoreTimeRepo(Context context) {

        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }

    @Override
    public int create(Object item) {
        int index = -1;
        RouteStoreTime object = (RouteStoreTime) item;
        try {
            index = helper.getRouteStoreTimeDao().create(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return index;
    }


    @Override
    public int update(Object item) {

        int index = -1;

        RouteStoreTime object = (RouteStoreTime) item;

        try {
            helper.getRouteStoreTimeDao().update(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }


    @Override
    public int delete(Object item) {

        int index = -1;

        RouteStoreTime object = (RouteStoreTime) item;

        try {
            helper.getRouteStoreTimeDao().delete(object);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }

    @Override
    public int deleteAll() {

        List<RouteStoreTime> items = null;
        int counter = 0;
        try {
            items = helper.getRouteStoreTimeDao().queryForAll();

            for (RouteStoreTime object : items) {
                // do something with object
                helper.getRouteStoreTimeDao().deleteById(object.getId());
                counter++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return counter;
    }


    @Override
    public Object findById(int id) {

        RouteStoreTime wishList = null;
        try {
            wishList = helper.getRouteStoreTimeDao().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }


    @Override
    public List<?> findAll() {

        List<RouteStoreTime> items = null;

        try {
            items = helper.getRouteStoreTimeDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;

    }

    @Override
    public long countReg() {
        long count = 0;
        try {
            count = helper.getRouteStoreTimeDao().countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
    @Override
    public Object findFirstReg() {

        Object wishList = null;
        try {
            wishList = helper.getRouteStoreTimeDao().queryBuilder().queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }
}
