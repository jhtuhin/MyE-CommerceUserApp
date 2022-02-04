package com.example.mye_commerceuserapp.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mye_commerceuserapp.models.UserModel;
import com.example.mye_commerceuserapp.utils.UserConstants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginViewModel extends ViewModel {
    public enum AuthState {
        AUTHENTICATED, UNAUTHENTICATED
    }
    private MutableLiveData<AuthState> authLiveData;
    private MutableLiveData<String> errMsgLiveData;
    private FirebaseAuth auth;
    private FirebaseUser user;

    public LoginViewModel() {
        authLiveData = new MutableLiveData<>();
        errMsgLiveData = new MutableLiveData<>();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user == null) {
            authLiveData.postValue(AuthState.UNAUTHENTICATED);
        }else {
            authLiveData.postValue(AuthState.AUTHENTICATED);
        }
    }

    public LiveData<AuthState> getAuthLiveData() {
        return authLiveData;
    }

    public LiveData<String> getErrMsgLiveData() {
        return errMsgLiveData;
    }

    public FirebaseUser getUser() {
        return user;
    }

    public void login(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    user = authResult.getUser();
                    authLiveData.postValue(AuthState.AUTHENTICATED);
                }).addOnFailureListener(e -> {
            errMsgLiveData.postValue(e.getLocalizedMessage());
        });
    }

    public void register(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    user = authResult.getUser();
                    authLiveData.postValue(AuthState.AUTHENTICATED);
                    addUserToDatabase();
                }).addOnFailureListener(e -> {
            errMsgLiveData.postValue(e.getLocalizedMessage());
        });
    }

    private void addUserToDatabase() {
        final FirebaseFirestore db=FirebaseFirestore.getInstance();
        final DocumentReference doc=db.collection(UserConstants
                .dbCollections.COLLECTION_USER).document(user.getUid());
        final UserModel customer=new UserModel(null,user.getUid(),user.getEmail(),null);

    }

    public void logout() {
        if (user != null) {
            auth.signOut();
            authLiveData.postValue(AuthState.UNAUTHENTICATED);
        }
    }

    public boolean isEmailVerified() {
        return user.isEmailVerified();
    }

    public void sendVerificationMail() {
        user.sendEmailVerification()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

}
