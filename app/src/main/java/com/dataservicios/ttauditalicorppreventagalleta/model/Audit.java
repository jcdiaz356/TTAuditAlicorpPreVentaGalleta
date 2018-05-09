package com.dataservicios.ttauditalicorppreventagalleta.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by jcdia on 22/05/2017.
 */

public class Audit {
    @DatabaseField(id = true)
    private int         id;
    @DatabaseField
    private int         company_audit_id;
    @DatabaseField
    private String fullname;
    @DatabaseField
    private int         orden;
    @DatabaseField
    private int         audit;
    @DatabaseField
    private int         company_id;
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

    public int getCompany_audit_id() {
        return company_audit_id;
    }

    public void setCompany_audit_id(int company_audit_id) {
        this.company_audit_id = company_audit_id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }


    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public int getAudit() {
        return audit;
    }

    public void setAudit(int audit) {
        this.audit = audit;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
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
