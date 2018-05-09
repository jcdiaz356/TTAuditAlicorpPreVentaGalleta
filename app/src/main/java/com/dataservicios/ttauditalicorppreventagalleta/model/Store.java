package com.dataservicios.ttauditalicorppreventagalleta.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by jcdia on 12/05/2017.
 */

public class Store {

    @DatabaseField(id = true)
    private int          id;
    @DatabaseField
    private int          route_id;
    @DatabaseField
    private String cadenRuc;
    @DatabaseField
    private String codCliente;
    @DatabaseField
    private String code;
    @DatabaseField
    private String document;
    @DatabaseField
    private String typo_document;
    @DatabaseField
    private String type;
    @DatabaseField
    private String fullname;
    @DatabaseField
    private String region;
    @DatabaseField
    private String typeBodega;
    @DatabaseField
    private String address;
    @DatabaseField
    private String departament;
    @DatabaseField
    private String district;
    @DatabaseField
    private String urbanization;
    @DatabaseField
    private String ejecutivo;
    @DatabaseField
    private double       latitude;
    @DatabaseField
    private double       longitude;
    @DatabaseField
    private String telephone;
    @DatabaseField
    private String cell;
    @DatabaseField
    private String phone;
    @DatabaseField
    private String owner;
    @DatabaseField
    private String executive;
    @DatabaseField
    private String giro;
    @DatabaseField
    private String dex;
    @DatabaseField
    private String fnac;
    @DatabaseField
    private int          status;
    @DatabaseField
    private String comment;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoute_id() {
        return route_id;
    }

    public void setRoute_id(int route_id) {
        this.route_id = route_id;
    }

    public String getCadenRuc() {
        return cadenRuc;
    }

    public void setCadenRuc(String cadenRuc) {
        this.cadenRuc = cadenRuc;
    }

    public String getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getTypo_document() {
        return typo_document;
    }

    public void setTypo_document(String typo_document) {
        this.typo_document = typo_document;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getTypeBodega() {
        return typeBodega;
    }

    public void setTypeBodega(String typeBodega) {
        this.typeBodega = typeBodega;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getUrbanization() {
        return urbanization;
    }

    public void setUrbanization(String urbanization) {
        this.urbanization = urbanization;
    }

    public String getEjecutivo() {
        return ejecutivo;
    }

    public void setEjecutivo(String ejecutivo) {
        this.ejecutivo = ejecutivo;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getFnac() {
        return fnac;
    }

    public void setFnac(String fnac) {
        this.fnac = fnac;
    }

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDepartament() {
        return departament;
    }

    public void setDepartament(String departament) {
        this.departament = departament;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getExecutive() {
        return executive;
    }

    public void setExecutive(String executive) {
        this.executive = executive;
    }

    public String getGiro() {
        return giro;
    }

    public void setGiro(String giro) {
        this.giro = giro;
    }

    public String getDex() {
        return dex;
    }

    public void setDex(String dex) {
        this.dex = dex;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
