package com.dataservicios.ttauditalicorppreventagalleta.model;

import com.j256.ormlite.field.DatabaseField;

public class Order {

    @DatabaseField(generatedId = true)
    private int     id;
    @DatabaseField
    private int     store_id;
    @DatabaseField
    private int     visit_id;
    @DatabaseField
    private int     distributor_id;
    @DatabaseField
    private int     user_id;
    @DatabaseField
    private int     company_id;
    @DatabaseField
    private String  code;
    @DatabaseField
    private float  mount_total;
    @DatabaseField
    private int    payment_type;
    @DatabaseField
    private String  created_at;
    @DatabaseField
    private String  updated_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public int getVisit_id() {
        return visit_id;
    }

    public void setVisit_id(int visit_id) {
        this.visit_id = visit_id;
    }

    public int getDistributor_id() {
        return distributor_id;
    }

    public void setDistributor_id(int distributor_id) {
        this.distributor_id = distributor_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public float getMount_total() {
        return mount_total;
    }

    public void setMount_total(float mount_total) {
        this.mount_total = mount_total;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(int payment_type) {
        this.payment_type = payment_type;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
