package com.dataservicios.ttauditalicorppreventagalleta.repo;

import android.content.Context;

import com.dataservicios.ttauditalicorppreventagalleta.db.DatabaseHelper;
import com.dataservicios.ttauditalicorppreventagalleta.db.DatabaseManager;
import com.dataservicios.ttauditalicorppreventagalleta.model.PollOption;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by jcdia on 26/05/2017.
 */

public class PollOptionRepo implements Crud  {
    private DatabaseHelper helper;

    public PollOptionRepo(Context context) {
        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }

    @Override
    public int create(Object item) {
        int index = -1;
        PollOption object = (PollOption) item;
        try {
            index = helper.getPollOptionDao().create(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return index;
    }


    @Override
    public int update(Object item) {

        int index = -1;

        PollOption object = (PollOption) item;

        try {
            helper.getPollOptionDao().update(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }


    @Override
    public int delete(Object item) {

        int index = -1;

        PollOption object = (PollOption) item;

        try {
            helper.getPollOptionDao().delete(object);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }

    @Override
    public int deleteAll() {

        List<PollOption> items = null;
        int counter = 0;
        try {
            items = helper.getPollOptionDao().queryForAll();

            for (PollOption object : items) {
                // do something with object
                helper.getPollOptionDao().deleteById(object.getId());
                counter ++ ;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return counter;
    }


    @Override
    public Object findById(int id) {

        PollOption wishList = null;
        try {
            wishList = helper.getPollOptionDao().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }


    @Override
    public List<?> findAll() {

        List<PollOption> items = null;

        try {
            items = helper.getPollOptionDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;

    }

    @Override
    public Object findFirstReg() {

        Object wishList = null;
        try {
            wishList = helper.getPollOptionDao().queryBuilder().queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }

    @Override
    public long countReg() {
        long count = 0;
        try {
            count = helper.getPollOptionDao().countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }



    public List<PollOption> findByPollId(int poll_id) {

        List<PollOption> wishList = null;
        try {
            wishList = helper.getPollOptionDao().queryBuilder().where().eq("poll_id",poll_id).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishList;
    }
}
