package com.dataservicios.ttauditalicorppreventagalleta.repo;

import android.content.Context;

import com.dataservicios.ttauditalicorppreventagalleta.db.DatabaseHelper;
import com.dataservicios.ttauditalicorppreventagalleta.db.DatabaseManager;
import com.dataservicios.ttauditalicorppreventagalleta.model.Order;

import java.sql.SQLException;
import java.util.List;

public class OrderRepo implements Crud {
    private DatabaseHelper helper;

    public OrderRepo(Context context) {

        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }

    @Override
    public int create(Object item) {
        int index = -1;
        Order object = (Order) item;
        try {
//            helper.getOrderDao().create(object);
//            index = ((Order) item).getId();
            index = helper.getOrderDao().create(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return index;
    }


    @Override
    public int update(Object item) {

        int index = -1;

        Order object = (Order) item;

        try {
            helper.getOrderDao().update(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }


    @Override
    public int delete(Object item) {

        int index = -1;

        Order object = (Order) item;

        try {
            helper.getOrderDao().delete(object);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }

    @Override
    public int deleteAll() {

        List<Order> items = null;
        int counter = 0;
        try {
            items = helper.getOrderDao().queryForAll();

            for (Order object : items) {
                // do something with object
                helper.getOrderDao().deleteById(object.getId());
                counter ++ ;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return counter;
    }


    @Override
    public Object findById(int id) {

        Order wishList = null;
        try {
            wishList = helper.getOrderDao().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }


    @Override
    public List<?> findAll() {

        List<Order> items = null;

        try {
            items = helper.getOrderDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;

    }

    @Override
    public Object findFirstReg() {

        Object wishList = null;
        try {
            wishList = helper.getOrderDao().queryBuilder().queryForFirst();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }

    @Override
    public long countReg() {
        long count = 0;
        try {
            count = helper.getOrderDao().countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Obtiene los pedidos de la orden
     * @param company_id
     * @param store_id
     * @param visit_id
     * @return
     */
    public List<Order> findByOrder(int company_id, int store_id, int visit_id) {

        List<Order> wishList = null;
        try {
            wishList = helper.getOrderDao().queryBuilder().where().eq("company_id",company_id).and().eq("store_id",store_id).and().eq("visit_id",visit_id).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }



}