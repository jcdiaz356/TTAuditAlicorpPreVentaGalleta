package com.dataservicios.ttauditalicorppreventagalleta.repo;

import android.content.Context;

import com.dataservicios.ttauditalicorppreventagalleta.db.DatabaseHelper;
import com.dataservicios.ttauditalicorppreventagalleta.db.DatabaseManager;
import com.dataservicios.ttauditalicorppreventagalleta.model.Audit;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by jcdia on 22/05/2017.
 */

public class AuditRepo implements Crud {
    private DatabaseHelper helper;

    public AuditRepo(Context context) {

        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }

    @Override
    public int create(Object item) {
        int index = -1;
        Audit object = (Audit) item;
        try {
            index = helper.getAuditDao().create(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return index;
    }


    @Override
    public int update(Object item) {

        int index = -1;

        Audit object = (Audit) item;

        try {
            helper.getAuditDao().update(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }


    @Override
    public int delete(Object item) {

        int index = -1;

        Audit object = (Audit) item;

        try {
            helper.getAuditDao().delete(object);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }

    @Override
    public int deleteAll() {

        List<Audit> items = null;
        int counter = 0;
        try {
            items = helper.getAuditDao().queryForAll();

            for (Audit object : items) {
                // do something with object
                helper.getAuditDao().deleteById(object.getId());
                counter ++ ;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return counter;
    }


    @Override
    public Object findById(int id) {

        Audit wishList = null;
        try {
            wishList = helper.getAuditDao().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }


    @Override
    public List<?> findAll() {

        List<Audit> items = null;

        try {
            items = helper.getAuditDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;

    }
    @Override
    public Object findFirstReg() {

        Object wishList = null;
        try {
            wishList = helper.getAuditDao().queryBuilder().queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }
    @Override
    public long countReg() {
        long count = 0;
        try {
            count = helper.getAuditDao().countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

}