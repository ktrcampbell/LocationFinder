package com.bigbang.myfavoriteplaces.viewmodel;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.bigbang.myfavoriteplaces.database.LocationDB;
import com.bigbang.myfavoriteplaces.database.LocationRepository;
import com.bigbang.myfavoriteplaces.model.Location;
import com.bigbang.myfavoriteplaces.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.List;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;

public class LocationViewModel extends AndroidViewModel {

    private DatabaseReference firebaseReference;
    private LocationDB locDatabase;
    private LocationRepository locationRepository;
    private Flowable<List<Location>> faveLocations;
    private Observable<Boolean> loginObserver;

    public Boolean getUserLoggedIn() {
        if (FirebaseAuth.getInstance()
                .getCurrentUser() != null && FirebaseAuth.getInstance().getCurrentUser().isEmailVerified())
            return true;
        else
            return false;
    }

    private MutableLiveData<Boolean> regMLD = new MutableLiveData<>();
    private MutableLiveData<Boolean> loginMLD = new MutableLiveData<>();

    public LocationViewModel(@NonNull Application application) {
        super(application);
        locDatabase = LocationDB.getDatabase(application);
        locationRepository = new LocationRepository(application);
        faveLocations = locationRepository.getFaveLocs();

    }

    public Flowable<List<Location>> getFaveLocs(){
        return locationRepository.getFaveLocs();
    }

    public void addLocation(Location location){
        locationRepository.addLocation(location);
    }

    public void deleteLocation(Location location){
        locationRepository.deleteLocation(location);
    }

    public void disposeDisposables(){
        locationRepository.disposeDisposables();

    }

    public void loginUser(User user) {
        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(user.getUserName(), user.getUserPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isComplete() && task.isSuccessful()) {

                            if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                                loginMLD.setValue(true);
                            } else
                                Toast.makeText(getApplication(), "Please verify email sent to " + user.getUserName(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplication(), "Login failed " + task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            loginMLD.setValue(false);
                        }
                    }
                });
    }

    public void registerUser(User user) {

        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(user.getUserName(), user.getUserPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isComplete() && task.isSuccessful()) {
                            Toast.makeText(getApplication(), "User Creation Successful: Verification email sent.", Toast.LENGTH_LONG).show();
                            FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                            regMLD.setValue(true);
                        } else {

                            Toast.makeText(getApplication(), task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            regMLD.setValue(false);
                        }
                    }
                });
    }


    public MutableLiveData<Boolean> getRegistrationStatus() {
        return regMLD;
    }

    public MutableLiveData<Boolean> getLoginStatus() {
        return loginMLD;
    }
}
