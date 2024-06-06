package com.example.yellow;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {
    private final MutableLiveData<String> userType = new MutableLiveData<>();
    private final MutableLiveData<String> userName = new MutableLiveData<>();
    private final MutableLiveData<String> email = new MutableLiveData<>();

    public LiveData<String> getUserType() {
        return userType;
    }

    public LiveData<String> getUserName() {
        return userName;
    }

    public LiveData<String> getEmail() {
        return email;
    }

    public void setUserData(String name, String email) {
        this.userType.setValue("Company");
        this.userName.setValue(name);
        this.email.setValue(email);
    }
}
