package com.myproject.aplication;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.myproject.database.AccountEntity;

import java.sql.SQLException;

public class DBHelper extends OrmLiteSqliteOpenHelper {

    private static final int DATABASE_VERSION = 20;
    private Context context;

    public DBHelper(Context context) {
        super(context, Config.DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        createTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        resetDatabase(oldVersion);
        createTable();

    }

    private void createTable() {
        try {
            TableUtils.createTableIfNotExists(connectionSource, AccountEntity.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resetDatabase(int oldVersion) {
        try {
            if(oldVersion<16){
                TableUtils.dropTable(connectionSource, AccountEntity.class, true);
                APP.removePreference(context, Preference.TOKEN);
            }
            if(oldVersion<17){

                try {
                    getDao(AccountEntity.class).executeRaw("ALTER TABLE `heart_rate_setting` ADD COLUMN " + AccountEntity.FOTO + " VARCHAR;");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getDatabaseVersion(){
        return DATABASE_VERSION;
    }

    @Override
    public <D extends Dao<T, ?>, T> D getDao(Class<T> clazz)
            throws SQLException {
        return super.getDao(clazz);
    }

    @Override
    public void close() {
        super.close();
    }

    public void resetDatabase() {
        try {
            TableUtils.dropTable(connectionSource, AccountEntity.class, true);
            APP.removePreference(context, Preference.TOKEN);
            createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
