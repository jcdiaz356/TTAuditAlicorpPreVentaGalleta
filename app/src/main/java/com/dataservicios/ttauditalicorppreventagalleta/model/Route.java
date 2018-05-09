package com.dataservicios.ttauditalicorppreventagalleta.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by usuario on 10/01/2015.
 */
public class Route {
    @DatabaseField(id = true)
    private int id;
    @DatabaseField
    private String fullname;
    @DatabaseField
    private int total_store;
    @DatabaseField
    private int audit;

    public Route(int id, String fullname , int total_store, int audit) {
        this.id = id;
        this.fullname = fullname;
        this.total_store = total_store;
        this.audit = audit;
    }
    public Route() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public int getTotal_store() {
        return total_store;
    }

    public void setTotal_store(int total_store) {
        this.total_store = total_store;
    }

    public int getAudit() {
        return audit;
    }

    public void setAudit(int audit) {
        this.audit = audit;
    }
}
