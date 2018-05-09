package com.dataservicios.ttauditalicorppreventagalleta.repo;

import android.content.Context;

import com.dataservicios.ttauditalicorppreventagalleta.db.DatabaseHelper;
import com.dataservicios.ttauditalicorppreventagalleta.db.DatabaseManager;
import com.dataservicios.ttauditalicorppreventagalleta.model.TypeStore;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by jcdia on 27/09/2017.
 */

public class TypeStoreRepo implements Crud {
    private DatabaseHelper helper;

    public TypeStoreRepo(Context context) {

        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }

    @Override
    public int create(Object item) {
        int index = -1;
        TypeStore object = (TypeStore) item;
        try {
            index = helper.getTypeStoreDao().create(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return index;
    }


    @Override
    public int update(Object item) {

        int index = -1;

        TypeStore object = (TypeStore) item;

        try {
            helper.getTypeStoreDao().update(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }


    @Override
    public int delete(Object item) {

        int index = -1;

        TypeStore object = (TypeStore) item;

        try {
            helper.getTypeStoreDao().delete(object);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }

    @Override
    public int deleteAll() {

        List<TypeStore> items = null;
        int counter = 0;
        try {
            items = helper.getTypeStoreDao().queryForAll();

            for (TypeStore object : items) {
                // do something with object
                helper.getTypeStoreDao().deleteById(object.getId());
                counter ++ ;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return counter;
    }


    @Override
    public Object findById(int id) {

        TypeStore wishList = null;
        try {
            wishList = helper.getTypeStoreDao().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }


    @Override
    public List<?> findAll() {

        List<TypeStore> items = null;

        try {
            items = helper.getTypeStoreDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;

    }

    @Override
    public Object findFirstReg() {

        Object wishList = null;
        try {
            wishList = helper.getTypeStoreDao().queryBuilder().queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }

    @Override
    public long countReg() {
        long count = 0;
        try {
            count = helper.getTypeStoreDao().countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

}