package com.dataservicios.ttauditalicorppreventagalleta.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by jcdia on 26/05/2017.
 */

public class Poll {
    @DatabaseField(id = true)
    private int     id;
    @DatabaseField
    private int     company_audit_id;
    @DatabaseField
    private String question;
    @DatabaseField
    private int     order;
    @DatabaseField
    private int     sino;
    @DatabaseField
    private int     options;
    @DatabaseField
    private int     option_type;
    @DatabaseField
    private int     media;
    @DatabaseField
    private int     comment;
    @DatabaseField
    private int     comment_requiered;
    @DatabaseField
    private int     comentType;
    @DatabaseField
    private String comentTag;
    @DatabaseField
    private int     publicity_id;
    @DatabaseField
    private int     category_product_id;
    @DatabaseField
    private int     product_id;
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

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getSino() {
        return sino;
    }

    public void setSino(int sino) {
        this.sino = sino;
    }

    public int getOptions() {
        return options;
    }

    public void setOptions(int options) {
        this.options = options;
    }

    public int getOption_type() {
        return option_type;
    }

    public void setOption_type(int option_type) {
        this.option_type = option_type;
    }


    public int getMedia() {
        return media;
    }

    public void setMedia(int media) {
        this.media = media;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public int getComment_requiered() {
        return comment_requiered;
    }

    public void setComment_requiered(int comment_requiered) {
        this.comment_requiered = comment_requiered;
    }

    public int getComentType() {
        return comentType;
    }

    public void setComentType(int comentType) {
        this.comentType = comentType;
    }

    public String getComentTag() {
        return comentTag;
    }

    public void setComentTag(String comentTag) {
        this.comentTag = comentTag;
    }

    public int getPublicity_id() {
        return publicity_id;
    }

    public void setPublicity_id(int publicity_id) {
        this.publicity_id = publicity_id;
    }

    public int getCategory_product_id() {
        return category_product_id;
    }

    public void setCategory_product_id(int category_product_id) {
        this.category_product_id = category_product_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
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
