package com.bigbang.myfavoriteplaces.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.bigbang.myfavoriteplaces.model.Location;

@Database(entities = {Location.class}, version = 1)
public abstract class LocationDB extends RoomDatabase {

    public abstract DAO dao();

    private static LocationDB instance;

    public static LocationDB getDatabase(Context context) {
        if (instance == null) {
            synchronized (LocationDB.class) {
                instance = Room.databaseBuilder(context, LocationDB.class, "location_database")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .addCallback(lRoomDatabaseCallback)
                        .build();
            }
        }
        return instance;
    }

    private static RoomDatabase.Callback lRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };

        @NonNull
        @Override
        protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
            return null;
        }

        @NonNull
        @Override
        protected InvalidationTracker createInvalidationTracker() {
            return null;
        }

        @Override
        public void clearAllTables() {

        }

}
