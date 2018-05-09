package com.dataservicios.ttauditalicorppreventagalleta.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by jcdia on 23/03/2017.
 */

public class District {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private int departament_id ;
    @DatabaseField
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDepartament_id() {
        return departament_id;
    }

    public void setDepartament_id(int departament_id) {
        this.departament_id = departament_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString () {
        return name;
    }

}
