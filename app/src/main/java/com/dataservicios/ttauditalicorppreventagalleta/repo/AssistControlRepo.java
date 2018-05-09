package com.dataservicios.ttauditalicorppreventagalleta.repo;

import android.content.Context;

import com.dataservicios.ttauditalicorppreventagalleta.db.DatabaseHelper;
import com.dataservicios.ttauditalicorppreventagalleta.db.DatabaseManager;
import com.dataservicios.ttauditalicorppreventagalleta.model.AssistControl;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by jcdia on 24/09/2017.
 */

public class AssistControlRepo implements Crud {
    private DatabaseHelper helper;

    public AssistControlRepo(Context context) {

        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }

    @Override
    public int create(Object item) {
        int index = -1;
        AssistControl object = (AssistControl) item;
        try {
            index = helper.getAssistControlDao().create(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return index;
    }


    @Override
    public int update(Object item) {

        int index = -1;

        AssistControl object = (AssistControl) item;

        try {
            helper.getAssistControlDao().update(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }


    @Override
    public int delete(Object item) {

        int index = -1;

        AssistControl object = (AssistControl) item;

        try {
            helper.getAssistControlDao().delete(object);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }

    @Override
    public int deleteAll() {

        List<AssistControl> items = null;
        int counter = 0;
        try {
            items = helper.getAssistControlDao().queryForAll();

            for (AssistControl object : items) {
                // do something with object
                helper.getAssistControlDao().deleteById(object.getId());
                counter ++ ;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return counter;
    }


    @Override
    public Object findById(int id) {

        AssistControl wishList = null;
        try {
            wishList = helper.getAssistControlDao().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }


    @Override
    public List<?> findAll() {

        List<AssistControl> items = null;

        try {
            items = helper.getAssistControlDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;

    }

    @Override
    public Object findFirstReg() {

        Object wishList = null;
        try {
            wishList = helper.getAssistControlDao().queryBuilder().queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }

    @Override
    public long countReg() {
        long count = 0;
        try {
            count = helper.getAssistControlDao().countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

}