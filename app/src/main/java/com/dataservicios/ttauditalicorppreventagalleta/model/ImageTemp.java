package com.dataservicios.ttauditalicorppreventagalleta.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by jcdia on 11/10/2017.
 */

public class ImageTemp {
    @DatabaseField(generatedId = true)
    private int     id;
    @DatabaseField
    private int  user_id;
    @DatabaseField
    private String image;
    @DatabaseField
    private int  type;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getType() {
        return type;
    }

    /**
     * tipo de imagen a almacenar
     * @param type 1:input ; 0: output
     */
    public void setType(int type) {
        this.type = type;
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
