package com.dataservicios.ttauditalicorppreventagalleta.repo;

import android.content.Context;

import com.dataservicios.ttauditalicorppreventagalleta.db.DatabaseHelper;
import com.dataservicios.ttauditalicorppreventagalleta.db.DatabaseManager;
import com.dataservicios.ttauditalicorppreventagalleta.model.ImageTemp;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by jcdia on 11/10/2017.
 */

public class ImageTempRepo implements Crud {
    private DatabaseHelper helper;

    public ImageTempRepo(Context context) {

        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }

    @Override
    public int create(Object item) {
        int index = -1;
        ImageTemp object = (ImageTemp) item;
        try {
            index = helper.getImageTempDao().create(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return index;
    }


    @Override
    public int update(Object item) {

        int index = -1;

        ImageTemp object = (ImageTemp) item;

        try {
            helper.getImageTempDao().update(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }


    @Override
    public int delete(Object item) {

        int index = -1;

        ImageTemp object = (ImageTemp) item;

        try {
            helper.getImageTempDao().delete(object);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }

    @Override
    public int deleteAll() {

        List<ImageTemp> items = null;
        int counter = 0;
        try {
            items = helper.getImageTempDao().queryForAll();

            for (ImageTemp object : items) {
                // do something with object
                helper.getImageTempDao().deleteById(object.getId());
                counter ++ ;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return counter;
    }


    @Override
    public Object findById(int id) {

        ImageTemp wishList = null;
        try {
            wishList = helper.getImageTempDao().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }


    @Override
    public List<?> findAll() {

        List<ImageTemp> items = null;

        try {
            items = helper.getImageTempDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;

    }

    @Override
    public Object findFirstReg() {

        Object wishList = null;
        try {
            wishList = helper.getImageTempDao().queryBuilder().queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }

    @Override
    public long countReg() {
        long count = 0;
        try {
            count = helper.getImageTempDao().countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Busca una lista de ImageTemps por su Route
     * @param route_id
     * @return Retorna lista de stores
     */
    public List<ImageTemp> findByRouteId(int route_id) {

        List<ImageTemp> wishList = null;
        try {
            wishList = helper.getImageTempDao().queryBuilder().where().eq("route_id",route_id).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }



}