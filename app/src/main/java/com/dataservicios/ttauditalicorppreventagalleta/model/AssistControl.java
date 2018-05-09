package com.dataservicios.ttauditalicorppreventagalleta.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by jcdia on 24/09/2017.
 */

public class AssistControl {
    @DatabaseField(id = true)
    private int     id;
    @DatabaseField
    private int     user_id;
    @DatabaseField
    private int     company_id;
    @DatabaseField
    private String imei;
    @DatabaseField
    private String image_open;
    @DatabaseField
    private String image_close;
    @DatabaseField
    private String lat_open;
    @DatabaseField
    private String lon_open;
    @DatabaseField
    private String lat_close;
    @DatabaseField
    private String lon_close;
    @DatabaseField
    private String date_open;
    @DatabaseField
    private String date_close;
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

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getImage_open() {
        return image_open;
    }

    public void setImage_open(String image_open) {
        this.image_open = image_open;
    }

    public String getImage_close() {
        return image_close;
    }

    public void setImage_close(String image_close) {
        this.image_close = image_close;
    }

    public String getLat_open() {
        return lat_open;
    }

    public void setLat_open(String lat_open) {
        this.lat_open = lat_open;
    }

    public String getLon_open() {
        return lon_open;
    }

    public void setLon_open(String lon_open) {
        this.lon_open = lon_open;
    }

    public String getLat_close() {
        return lat_close;
    }

    public void setLat_close(String lat_close) {
        this.lat_close = lat_close;
    }

    public String getLon_close() {
        return lon_close;
    }

    public void setLon_close(String lon_close) {
        this.lon_close = lon_close;
    }

    public String getDate_open() {
        return date_open;
    }

    public void setDate_open(String date_open) {
        this.date_open = date_open;
    }

    public String getDate_close() {
        return date_close;
    }

    public void setDate_close(String date_close) {
        this.date_close = date_close;
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
