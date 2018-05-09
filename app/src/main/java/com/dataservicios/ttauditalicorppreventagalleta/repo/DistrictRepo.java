package com.dataservicios.ttauditalicorppreventagalleta.repo;

import android.content.Context;

import com.dataservicios.ttauditalicorppreventagalleta.db.DatabaseHelper;
import com.dataservicios.ttauditalicorppreventagalleta.db.DatabaseManager;
import com.dataservicios.ttauditalicorppreventagalleta.model.District;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by jcdia on 19/04/2017.
 */

public class DistrictRepo implements Crud {
    private DatabaseHelper helper;

    public DistrictRepo(Context context) {

        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }

    @Override
    public int create(Object item) {

        int index = -1;

        District object = (District) item;
        try {
            index = helper.getDistrictDao().create(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;

    }


    @Override
    public int update(Object item) {

        int index = -1;

        District object = (District) item;

        try {
            helper.getDistrictDao().update(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }


    @Override
    public int delete(Object item) {

        int index = -1;

        District object = (District) item;

        try {
            helper.getDistrictDao().delete(object);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }

    @Override
    public int deleteAll() {

        List<District> items = null;
        int counter = 0;
        try {
            items = helper.getDistrictDao().queryForAll();

            for (District object : items) {
                // do something with object
                helper.getDistrictDao().deleteById(object.getId());
                counter ++ ;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return counter;
    }


    @Override
    public Object findById(int id) {

        District wishList = null;
        try {
            wishList = helper.getDistrictDao().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }




    @Override
    public List<?> findAll() {

        List<District> items = null;

        try {
            items = helper.getDistrictDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;

    }
    @Override
    public Object findFirstReg() {

        Object wishList = null;
        try {
            wishList = helper.getDistrictDao().queryBuilder().queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }

    @Override
    public long countReg() {
        long count = 0;
        try {
            count = helper.getDistrictDao().countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public List<District> findByForDepartamentId(int departament_id) {

        List<District> wishList = null;
        try {
            wishList = helper.getDistrictDao().queryBuilder().where().eq("departament_id",departament_id).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }

}