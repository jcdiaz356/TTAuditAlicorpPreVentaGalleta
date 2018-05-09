package com.dataservicios.ttauditalicorppreventagalleta.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by jcdia on 1/06/2017.
 */

public class RouteStoreTime {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private int store_id;
    @DatabaseField
    private int route_id;
    @DatabaseField
    private int company_id;
    @DatabaseField
    private int user_id;
    @DatabaseField
    private double lat_open;
    @DatabaseField
    private double lon_open;
    @DatabaseField
    private double lat_close;
    @DatabaseField
    private double lon_close;
    @DatabaseField
    private String time_open;
    @DatabaseField
    private String time_close;

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

    public int getRoute_id() {
        return route_id;
    }

    public void setRoute_id(int route_id) {
        this.route_id = route_id;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public double getLat_open() {
        return lat_open;
    }

    public void setLat_open(double lat_open) {
        this.lat_open = lat_open;
    }

    public double getLon_open() {
        return lon_open;
    }

    public void setLon_open(double lon_open) {
        this.lon_open = lon_open;
    }

    public double getLat_close() {
        return lat_close;
    }

    public void setLat_close(double lat_close) {
        this.lat_close = lat_close;
    }

    public double getLon_close() {
        return lon_close;
    }

    public void setLon_close(double lon_close) {
        this.lon_close = lon_close;
    }

    public String getTime_open() {
        return time_open;
    }

    public void setTime_open(String time_open) {
        this.time_open = time_open;
    }

    public String getTime_close() {
        return time_close;
    }

    public void setTime_close(String time_close) {
        this.time_close = time_close;
    }
}
