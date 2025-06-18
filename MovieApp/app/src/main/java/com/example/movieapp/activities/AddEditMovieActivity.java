package com.example.movieapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.movieapp.R;
import com.example.movieapp.models.Movie;
import com.example.movieapp.viewmodels.MovieViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.auth.FirebaseAuth;

public class AddEditMovieActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextYear, editTextPosterUrl;
    private Button buttonSave, buttonCancel;
    private MovieViewModel viewModel;
    private Movie existingMovie;

    private static final String TAG = "AddEditMovieActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_movie);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextYear = findViewById(R.id.editTextYear);
        editTextPosterUrl = findViewById(R.id.editTextPosterUrl);
        buttonSave = findViewById(R.id.buttonSave);
        buttonCancel = findViewById(R.id.buttonCancel);

        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        existingMovie = (Movie) getIntent().getSerializableExtra(MovieListActivity.EXTRA_MOVIE);
        if (existingMovie != null) {
            editTextTitle.setText(existingMovie.getTitle());
            editTextYear.setText(existingMovie.getYear());
            editTextPosterUrl.setText(existingMovie.getPosterUrl());
            buttonSave.setText("Update Movie");
        }

        buttonSave.setOnClickListener(v -> {
            String title = editTextTitle.getText().toString().trim();
            String year = editTextYear.getText().toString().trim();
            String poster = editTextPosterUrl.getText().toString().trim();

            if (title.isEmpty() || year.isEmpty()) {
                Toast.makeText(this, "Title and Year are required", Toast.LENGTH_SHORT).show();
                return;
            }

            // Use a default image if no poster URL is provided
            if (poster.isEmpty()) {
                poster = "https://via.placeholder.com/150";
            }

            Log.d(TAG, "Current user: " + FirebaseAuth.getInstance().getCurrentUser());

            if (existingMovie != null) {
                existingMovie.setTitle(title);
                existingMovie.setYear(year);
                existingMovie.setPosterUrl(poster);

                viewModel.updateMovie(existingMovie, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Movie updated: " + title);
                            Toast.makeText(AddEditMovieActivity.this, "Movie updated", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Log.e(TAG, "Failed to update movie", task.getException());
                            Toast.makeText(AddEditMovieActivity.this, "Failed to update", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Movie newMovie = new Movie(null, title, year, poster);
                viewModel.addMovie(newMovie, new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Movie added: " + title);
                            Toast.makeText(AddEditMovieActivity.this, "Movie added", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Log.e(TAG, "Failed to add movie", task.getException());
                            Toast.makeText(AddEditMovieActivity.this, "Failed to save", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        buttonCancel.setOnClickListener(v -> finish());
    }
}
