package com.dataservicios.ttauditalicorppreventagalleta.repo;

import android.content.Context;

import com.dataservicios.ttauditalicorppreventagalleta.db.DatabaseHelper;
import com.dataservicios.ttauditalicorppreventagalleta.db.DatabaseManager;
import com.dataservicios.ttauditalicorppreventagalleta.model.OrderDetail;

import java.sql.SQLException;
import java.util.List;

public class OrderDetailRepo implements Crud {
    private DatabaseHelper helper;

    public OrderDetailRepo(Context context) {

        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }

    @Override
    public int create(Object item) {
        int index = -1;
        OrderDetail object = (OrderDetail) item;
        try {
            index = helper.getOrderDetailDao().create(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return index;
    }


    @Override
    public int update(Object item) {

        int index = -1;

        OrderDetail object = (OrderDetail) item;

        try {
            helper.getOrderDetailDao().update(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }


    @Override
    public int delete(Object item) {

        int index = -1;

        OrderDetail object = (OrderDetail) item;

        try {
            helper.getOrderDetailDao().delete(object);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }

    @Override
    public int deleteAll() {

        List<OrderDetail> items = null;
        int counter = 0;
        try {
            items = helper.getOrderDetailDao().queryForAll();

            for (OrderDetail object : items) {
                // do something with object
                helper.getOrderDetailDao().deleteById(object.getId());
                counter ++ ;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return counter;
    }


    @Override
    public Object findById(int id) {

        OrderDetail wishList = null;
        try {
            wishList = helper.getOrderDetailDao().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }


    @Override
    public List<?> findAll() {

        List<OrderDetail> items = null;

        try {
            items = helper.getOrderDetailDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;

    }

    @Override
    public Object findFirstReg() {

        Object wishList = null;
        try {
            wishList = helper.getOrderDetailDao().queryBuilder().queryForFirst();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }

    @Override
    public long countReg() {
        long count = 0;
        try {
            count = helper.getOrderDetailDao().countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     *
     * @param order_id
     * @return
     */
    public List<OrderDetail> findByOrderDetail(int order_id) {

        List<OrderDetail> wishList = null;
        try {
            wishList = helper.getOrderDetailDao().queryBuilder().where().eq("order_id",order_id).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }



}