package com.dataservicios.ttauditalicorppreventagalleta.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by jcdia on 13/05/2017.
 */

public class Company {
    @DatabaseField(id = true)
    private int     id;
    @DatabaseField
    private String fullname;
    @DatabaseField
    private int     active;
    @DatabaseField
    private int     customer_id;
    @DatabaseField
    private int     visible;
    @DatabaseField
    private int     auditory;
    @DatabaseField
    private String app_id;
    @DatabaseField
    private String logo;
    @DatabaseField
    private String markerPoint;
    @DatabaseField
    private String created_at;
    @DatabaseField
    private String updated_at;


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

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public int getAuditory() {
        return auditory;
    }

    public void setAuditory(int auditory) {
        this.auditory = auditory;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getMarkerPoint() {
        return markerPoint;
    }

    public void setMarkerPoint(String markerPoint) {
        this.markerPoint = markerPoint;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
