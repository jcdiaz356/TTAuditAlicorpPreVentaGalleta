package com.dataservicios.ttauditalicorppreventagalleta.repo;

import android.content.Context;

import com.dataservicios.ttauditalicorppreventagalleta.db.DatabaseHelper;
import com.dataservicios.ttauditalicorppreventagalleta.db.DatabaseManager;
import com.dataservicios.ttauditalicorppreventagalleta.model.Departament;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by jcdia on 19/04/2017.
 */

public class DepartamentRepo implements Crud {
    private DatabaseHelper helper;

    public DepartamentRepo(Context context) {

        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }

    @Override
    public int create(Object item) {

        int index = -1;

        Departament object = (Departament) item;
        try {
            index = helper.getDepartamentDao().create(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;

    }


    @Override
    public int update(Object item) {

        int index = -1;

        Departament object = (Departament) item;

        try {
            helper.getDepartamentDao().update(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }


    @Override
    public int delete(Object item) {

        int index = -1;

        Departament object = (Departament) item;

        try {
            helper.getDepartamentDao().delete(object);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }

    @Override
    public int deleteAll() {

        List<Departament> items = null;
        int counter = 0;
        try {
            items = helper.getDepartamentDao().queryForAll();

            for (Departament object : items) {
                // do something with object
                helper.getDepartamentDao().deleteById(object.getId());
                counter ++ ;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return counter;
    }


    @Override
    public Object findById(int id) {

        Departament wishList = null;
        try {
            wishList = helper.getDepartamentDao().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }


    @Override
    public List<?> findAll() {

        List<Departament> items = null;

        try {
            items = helper.getDepartamentDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;

    }
    @Override
    public Object findFirstReg() {

        Object wishList = null;
        try {
            wishList = helper.getDepartamentDao().queryBuilder().queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }
    @Override
    public long countReg() {
        long count = 0;
        try {
            count = helper.getDepartamentDao().countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }


}