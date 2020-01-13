package com.myproject.database;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.myproject.aplication.DBHelper;
import com.myproject.model.response.ModelPengguna;
import com.myproject.util.Constants;
import com.myproject.util.StringUtil;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Account {
    private Context ctx;
    String tag = null;
    private static DBHelper dbHelper;
    protected Dao<AccountEntity, ?> dao;

    SimpleDateFormat sdfTimeZone = new SimpleDateFormat(Constants.FORMAT_ISO);

    public Account(Context ctx) {
        // TODO Auto-generated constructor stub
        this.ctx = ctx;

        if (dbHelper == null) {
            dbHelper = new DBHelper(ctx);
        }

        try {
            if (dao == null) {
                dao = (Dao<AccountEntity, ?>) dbHelper.getDao(AccountEntity.class);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void parseAccount(ModelPengguna response) {
        AccountEntity entity = this.getLanguageById(response.getId_penduduk());

        if (entity == null) {
            entity = new AccountEntity();
            entity.setId(response.getId_penduduk());
        }

        entity.setIsDeleted(false);
        entity.setNik(StringUtil.checkNullString(response.getNik_penduduk()));
        entity.setNama(StringUtil.checkNullString(response.getNama_penduduk()));
        entity.setAlamat(StringUtil.checkNullString(response.getAlamat_penduduk()));
        entity.setTempat(StringUtil.checkNullString(response.getTempat_lahir()));
        entity.setTanggal(StringUtil.checkNullString(response.getTanggal_lahir()));
        entity.setStatus(StringUtil.checkNullString(response.getStatus()));
        entity.setPekerjaan(StringUtil.checkNullString(response.getPekerjaan()));

        entity.setPendidikan(StringUtil.checkNullString(response.getPendidikan()));
        entity.setJk(StringUtil.checkNullString(response.getJenis_kelamin()));
        entity.setKewarganegaraan(StringUtil.checkNullString(response.getKewarganegaraan()));
        entity.setId_user(StringUtil.checkNullString(response.getId_user()));
        this.upsertToDatabase(entity);
    }

    private void upsertToDatabase(AccountEntity artikel) {
        try {
            dao.createOrUpdate(artikel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveToDatabase(AccountEntity artikel) {
        try {
            dao.createIfNotExists(artikel);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteLanguageData(AccountEntity artikel) {
        try {
            dao.delete(artikel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void safeDeleteLanguage(String languageId) {
        AccountEntity artikel = getLanguageById(languageId);
        artikel.setIsDeleted(true);
        upsertToDatabase(artikel);
    }

    public void softDelete(AccountEntity artikel) {
        try {
            artikel.setIsDeleted(true);
            dao.update(artikel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public AccountEntity getLanguageById(String id) {
        List<AccountEntity> data = new ArrayList<AccountEntity>();
        try {
            data = dao.queryBuilder().where().eq(AccountEntity.ID, id).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(data.size()>0){
            return data.get(0);
        }
        return null;
    }

    public List<AccountEntity> getAllLanguage() {
        List<AccountEntity> data = new ArrayList<AccountEntity>();
        try {
            QueryBuilder<AccountEntity, ?> queryBuilder = dao.queryBuilder();
            queryBuilder.orderBy(AccountEntity.ID, true);
            data = queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public void deleteLanguage(AccountEntity entity){
        try {
            dao.delete(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAllReminder(String Id){

        List<AccountEntity> data = new ArrayList<AccountEntity>();
        try {
            data = dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(data != null && data.size()>0){
            for(AccountEntity entity:data) {
                if(entity.getId().equals(Id)){
                    deleteLanguage(entity);
                }
            }
        }
    }

    public void deleteAllInterval(){
        List<AccountEntity> listInterval = getAllLanguage();
        try {
            dao.delete(listInterval);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
