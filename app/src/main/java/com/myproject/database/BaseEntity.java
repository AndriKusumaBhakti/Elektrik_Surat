package com.myproject.database;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;
import java.util.Date;

public class BaseEntity implements Serializable {

    public static final String ID = "id";
    public static final String UPDATED_DATE = "updated_date";
    public static final String CREATED_DATE = "created_date";
    public static final String IS_DELETED = "is_deleted";

    @DatabaseField(columnName = ID, id = true)
    protected String id;
    @DatabaseField(columnName = UPDATED_DATE)
    protected Date updateDate;
    @DatabaseField(columnName = CREATED_DATE)
    protected Date createdDate;
    @DatabaseField(columnName = IS_DELETED)
    protected boolean isDeleted;

    public BaseEntity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
