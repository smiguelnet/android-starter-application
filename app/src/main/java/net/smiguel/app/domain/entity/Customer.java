package net.smiguel.app.domain.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import net.smiguel.app.constants.AppConstants;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = AppConstants.Database.TABLE_CUSTOMER)
public class Customer implements Serializable {

    public Customer() {}

    @Ignore
    public Customer(long id, Long idRef, Date insertDate, Date updateDate, boolean active, Date insertLocalDate, Date updateLocalDate, boolean syncPending, String name, String email, Date birthday) {
        this.id = id;
        this.idRef = idRef;
        this.insertDate = insertDate;
        this.updateDate = updateDate;
        this.active = active;
        this.insertLocalDate = insertLocalDate;
        this.updateLocalDate = updateLocalDate;
        this.syncPending = syncPending;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
    }

    //region Common fields
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true)
    private long id;

    @ColumnInfo(name = "id_ref", index = true)
    private Long idRef;

    @ColumnInfo(name = "insert_date")
    private Date insertDate;

    @ColumnInfo(name = "update_date")
    private Date updateDate;

    @ColumnInfo(name = "active")
    private boolean active;

    @ColumnInfo(name = "insert_local_date")
    private Date insertLocalDate;

    @ColumnInfo(name = "update_local_date")
    private Date updateLocalDate;

    @ColumnInfo(name = "sync_pending")
    private boolean syncPending;
    //endregion

    //region Custom fields
    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "birthday")
    private Date birthday;
    //endregion


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getIdRef() {
        return idRef;
    }

    public void setIdRef(Long idRef) {
        this.idRef = idRef;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getInsertLocalDate() {
        return insertLocalDate;
    }

    public void setInsertLocalDate(Date insertLocalDate) {
        this.insertLocalDate = insertLocalDate;
    }

    public Date getUpdateLocalDate() {
        return updateLocalDate;
    }

    public void setUpdateLocalDate(Date updateLocalDate) {
        this.updateLocalDate = updateLocalDate;
    }

    public boolean isSyncPending() {
        return syncPending;
    }

    public void setSyncPending(boolean syncPending) {
        this.syncPending = syncPending;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Customer{");
        sb.append("id=").append(id);
        sb.append(", idRef=").append(idRef);
        sb.append(", insertDate=").append(insertDate);
        sb.append(", updateDate=").append(updateDate);
        sb.append(", active=").append(active);
        sb.append(", insertLocalDate=").append(insertLocalDate);
        sb.append(", updateLocalDate=").append(updateLocalDate);
        sb.append(", syncPending=").append(syncPending);
        sb.append(", name='").append(name).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", birthday=").append(birthday);
        sb.append('}');
        return sb.toString();
    }
}
