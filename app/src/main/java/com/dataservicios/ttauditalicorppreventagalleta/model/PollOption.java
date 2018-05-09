package com.dataservicios.ttauditalicorppreventagalleta.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by jcdia on 26/05/2017.
 */

public class PollOption {

    @DatabaseField(id = true)
    private int id;
    @DatabaseField
    private int poll_id;
    @DatabaseField
    private String options;
    @DatabaseField
    private String options_abreviado;
    @DatabaseField
    private String codigo;
    @DatabaseField
    private int product_id;
    @DatabaseField
    private String region;
    @DatabaseField
    private int    option_yes_no;
    @DatabaseField
    private int comment;
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

    public int getPoll_id() {
        return poll_id;
    }

    public void setPoll_id(int poll_id) {
        this.poll_id = poll_id;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getOptions_abreviado() {
        return options_abreviado;
    }

    public void setOptions_abreviado(String options_abreviado) {
        this.options_abreviado = options_abreviado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getOption_yes_no() {
        return option_yes_no;
    }

    public void setOption_yes_no(int option_yes_no) {
        this.option_yes_no = option_yes_no;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
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
