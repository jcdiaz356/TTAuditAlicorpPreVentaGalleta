package com.dataservicios.ttauditalicorppreventagalleta.repo;

import android.content.Context;

import com.dataservicios.ttauditalicorppreventagalleta.db.DatabaseHelper;
import com.dataservicios.ttauditalicorppreventagalleta.db.DatabaseManager;
import com.dataservicios.ttauditalicorppreventagalleta.model.OrderDetailTemp;

import java.sql.SQLException;
import java.util.List;

public class OrderDetailTempRepo  implements Crud {
    private DatabaseHelper helper;

    public OrderDetailTempRepo(Context context) {

        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }

    @Override
    public int create(Object item) {
        int index = -1;
        OrderDetailTemp object = (OrderDetailTemp) item;
        try {
            index = helper.getOrderDetailTempDao().create(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return index;
    }


    @Override
    public int update(Object item) {

        int index = -1;

        OrderDetailTemp object = (OrderDetailTemp) item;

        try {
            helper.getOrderDetailTempDao().update(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }


    @Override
    public int delete(Object item) {

        int index = -1;

        OrderDetailTemp object = (OrderDetailTemp) item;

        try {
            helper.getOrderDetailTempDao().delete(object);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }

    @Override
    public int deleteAll() {

        List<OrderDetailTemp> items = null;
        int counter = 0;
        try {
            items = helper.getOrderDetailTempDao().queryForAll();

            for (OrderDetailTemp object : items) {
                // do something with object
                helper.getOrderDetailTempDao().deleteById(object.getId());
                counter ++ ;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return counter;
    }


    @Override
    public Object findById(int id) {

        OrderDetailTemp wishList = null;
        try {
            wishList = helper.getOrderDetailTempDao().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }


    @Override
    public List<?> findAll() {

        List<OrderDetailTemp> items = null;

        try {
            items = helper.getOrderDetailTempDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;

    }

    @Override
    public Object findFirstReg() {

        Object wishList = null;
        try {
            wishList = helper.getOrderDetailTempDao().queryBuilder().queryForFirst();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }

    @Override
    public long countReg() {
        long count = 0;
        try {
            count = helper.getOrderDetailTempDao().countOf();
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
    public List<OrderDetailTemp> findByOrderDetailTemp(int company_id, int store_id, int visit_id) {

        List<OrderDetailTemp> wishList = null;
        try {
            wishList = helper.getOrderDetailTempDao().queryBuilder().where().eq("company_id",company_id).and().eq("store_id",store_id).and().eq("visit_id",visit_id).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }

    public Object findByProductId(int product_id) {

        Object wishList = null;
        try {
            wishList = helper.getOrderDetailTempDao().queryBuilder().where().eq("product_id",product_id).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }
}

