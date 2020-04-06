package com.bigbang.myfavoriteplaces.database;

import android.app.Application;

import com.bigbang.myfavoriteplaces.model.Location;
import com.bigbang.myfavoriteplaces.util.DebugLogger;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LocationRepository {

    private DAO locDao;

    private Flowable<List<Location>> faveLocs;

    private CompositeDisposable cDisposable = new CompositeDisposable();

    public LocationRepository(Application application){
        LocationDB db = LocationDB.getDatabase(application);
        locDao = db.dao();
        faveLocs = locDao.getFaveLocs();
    }


    public Flowable<List<Location>> getFaveLocs(){
        return faveLocs;
    }

    public void addLocation(Location location){
        Disposable disposable = Completable.fromAction(()-> locDao.addLocation(location))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(()-> DebugLogger.logDebug("Location added."),
                        throwable -> DebugLogger.logError(throwable));
        cDisposable.add(disposable);
        }

    public void deleteLocation(Location location){
        Disposable disposable = Completable.fromAction(()-> locDao.deleteLocation(location.getLocationId()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(()-> DebugLogger.logDebug("Location deleted."),
                        throwable -> DebugLogger.logError(throwable));
        cDisposable.add(disposable);
    }

    public void disposeDisposables(){
        cDisposable.dispose();
    }
}
