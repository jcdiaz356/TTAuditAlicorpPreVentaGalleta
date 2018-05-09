package com.dataservicios.ttauditalicorppreventagalleta.repo;

import android.content.Context;

import com.dataservicios.ttauditalicorppreventagalleta.db.DatabaseHelper;
import com.dataservicios.ttauditalicorppreventagalleta.db.DatabaseManager;
import com.dataservicios.ttauditalicorppreventagalleta.model.Product;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by jcdia on 7/06/2017.
 */

public class ProductRepo implements Crud {
    private DatabaseHelper helper;

    public ProductRepo(Context context) {

        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }

    @Override
    public int create(Object item) {
        int index = -1;
        Product object = (Product) item;
        try {
            index = helper.getProductDao().create(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return index;
    }


    @Override
    public int update(Object item) {

        int index = -1;

        Product object = (Product) item;

        try {
            helper.getProductDao().update(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }


    @Override
    public int delete(Object item) {

        int index = -1;

        Product object = (Product) item;

        try {
            helper.getProductDao().delete(object);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }

    @Override
    public int deleteAll() {

        List<Product> items = null;
        int counter = 0;
        try {
            items = helper.getProductDao().queryForAll();

            for (Product object : items) {
                // do something with object
                helper.getProductDao().deleteById(object.getId());
                counter ++ ;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return counter;
    }


    @Override
    public Object findById(int id) {

        Product wishList = null;
        try {
            wishList = helper.getProductDao().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }


    @Override
    public List<?> findAll() {

        List<Product> items = null;

        try {
            items = helper.getProductDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;

    }

    @Override
    public Object findFirstReg() {

        Object wishList = null;
        try {
            wishList = helper.getProductDao().queryBuilder().queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }

    @Override
    public long countReg() {
        long count = 0;
        try {
            count = helper.getProductDao().countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Busca una lista de Product por su company_id
     * @param company_id
     * @return Retorna lista de Products
     */
    public List<Product> findByCompanyId(int company_id) {

        List<Product> wishList = null;
        try {
            wishList = helper.getProductDao().queryBuilder().where().eq("company_id",company_id).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }

    /**
     * Busca una lista de Product por su type
     * @param type tipo pe producto: Propio o competencia
     * @return Retorna lista de Products
     */
    public List<Product> findByTypeCompetity(int type) {

        List<Product> wishList = null;
        try {
            wishList = helper.getProductDao().queryBuilder().where().eq("type",type).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }


}