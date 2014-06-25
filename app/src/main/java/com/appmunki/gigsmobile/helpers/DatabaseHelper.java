package com.appmunki.gigsmobile.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.appmunki.gigsmobile.models.Gig;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by radzell on 6/17/14.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper{
    public static final String DATABASE_NAME = "gigsDatabase.db";
    private static final int DATABASE_VERSION = 1;

    private RuntimeExceptionDao<Gig, Integer> gigRuntimeDao = null;
    private Dao<Gig,Integer> gigDao=null;
    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource){
        try {
            Log.i(DatabaseHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, Gig.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion,
                          int newVersion){
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, Gig.class, true);
            // after we drop the old databases, we create the new ones
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao for our Gig class.
     * It will
     * create it or just give the cached value. RuntimeExceptionDao only through RuntimeExceptions.
     */
    public RuntimeExceptionDao<Gig, Integer> getGigRuntimeDataDao() {
        if (gigRuntimeDao == null) {
            gigRuntimeDao = getRuntimeExceptionDao(Gig.class);
        }
        return gigRuntimeDao;
    }

    public Dao<Gig, Integer> getGigDataDao() throws SQLException{
        if (gigDao == null) {
            gigDao = getDao(Gig.class);
        }
        return gigDao;
    }

    @Override
    public void close(){
        super.close();
        gigDao=null;
        gigRuntimeDao=null;
    }
}
