package com.example.movieapp.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movieapp.models.Movie;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.List;

public class FirebaseRepository {
    private final FirebaseAuth auth;
    private final FirebaseFirestore firestore;
    private String userId;

    private static final String TAG = "FirebaseRepository";

    public FirebaseRepository() {
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        refreshUserId();
    }

    private void refreshUserId() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            userId = user.getUid();
        } else {
            userId = null;
            Log.w(TAG, "User is not logged in — userId is null");
        }
    }

    // ---------------- Authentication ----------------

    public void registerUser(String email, String password, OnCompleteListener<AuthResult> listener) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            refreshUserId(); // refresh after register
            listener.onComplete(task);
        });
    }

    public void loginUser(String email, String password, OnCompleteListener<AuthResult> listener) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            refreshUserId(); // refresh after login
            listener.onComplete(task);
        });
    }

    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    public void logout() {
        auth.signOut();
        userId = null;
    }

    // ---------------- Firestore CRUD ----------------

    public LiveData<List<Movie>> getMovies() {
        MutableLiveData<List<Movie>> moviesLiveData = new MutableLiveData<>();

        refreshUserId(); // ensure up-to-date

        if (userId == null) {
            Log.e(TAG, "getMovies: userId is null. Returning empty list.");
            moviesLiveData.setValue(new ArrayList<>());
            return moviesLiveData;
        }

        firestore.collection("users").document(userId).collection("movies")
                .get().addOnCompleteListener(task -> {
                    List<Movie> movieList = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            Movie movie = doc.toObject(Movie.class);
                            if (movie != null) {
                                movie.setId(doc.getId());
                                movieList.add(movie);
                            }
                        }
                    } else {
                        Log.e(TAG, "getMovies failed: ", task.getException());
                    }
                    moviesLiveData.setValue(movieList);
                });

        return moviesLiveData;
    }

    public void addMovie(Movie movie, OnCompleteListener<DocumentReference> listener) {
        refreshUserId(); // ensure up-to-date
        if (userId == null) {
            Log.e(TAG, "addMovie: userId is null. Cannot add movie.");
            return;
        }

        firestore.collection("users").document(userId).collection("movies")
                .add(movie)
                .addOnCompleteListener(listener);
    }

    public void updateMovie(Movie movie, OnCompleteListener<Void> listener) {
        refreshUserId(); // ensure up-to-date
        if (userId == null) {
            Log.e(TAG, "updateMovie: userId is null. Cannot update movie.");
            return;
        }

        firestore.collection("users").document(userId).collection("movies")
                .document(movie.getId()).set(movie)
                .addOnCompleteListener(listener);
    }

    public void deleteMovie(String movieId, OnCompleteListener<Void> listener) {
        refreshUserId(); // ensure up-to-date
        if (userId == null) {
            Log.e(TAG, "deleteMovie: userId is null. Cannot delete movie.");
            return;
        }

        firestore.collection("users").document(userId).collection("movies")
                .document(movieId).delete()
                .addOnCompleteListener(listener);
    }
}
