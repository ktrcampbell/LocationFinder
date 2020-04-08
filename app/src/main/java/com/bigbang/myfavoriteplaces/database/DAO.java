package com.bigbang.myfavoriteplaces.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.bigbang.myfavoriteplaces.model.Location;


import java.util.List;

import io.reactivex.Flowable;


@Dao
public interface DAO {

    @Insert
    void addLocation(Location location);

    @Query("SELECT * FROM Location")
    Flowable<List<Location>> getFaveLocs();

    @Query("DELETE FROM Location WHERE id = :locationId")
    void deleteLocation(int locationId);

}
