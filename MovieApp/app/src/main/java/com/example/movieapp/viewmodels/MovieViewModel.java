package com.example.movieapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.movieapp.models.Movie;
import com.example.movieapp.repositories.FirebaseRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;

import java.util.List;

public class MovieViewModel extends ViewModel {

    private final FirebaseRepository repository;

    public MovieViewModel() {
        repository = new FirebaseRepository();
    }

    // --------- Authentication ---------

    public void register(String email, String password, OnCompleteListener<AuthResult> listener) {
        repository.registerUser(email, password, listener);
    }

    public void login(String email, String password, OnCompleteListener<AuthResult> listener) {
        repository.loginUser(email, password, listener);
    }

    public void logout() {
        repository.logout();
    }

    public boolean isUserLoggedIn() {
        return repository.getCurrentUser() != null;
    }

    // --------- Firestore CRUD ---------

    public LiveData<List<Movie>> getMovies() {
        return repository.getMovies();
    }

    public void addMovie(Movie movie, OnCompleteListener listener) {
        repository.addMovie(movie, listener);
    }

    public void updateMovie(Movie movie, OnCompleteListener listener) {
        repository.updateMovie(movie, listener);
    }

    public void deleteMovie(String movieId, OnCompleteListener listener) {
        repository.deleteMovie(movieId, listener);
    }
}
