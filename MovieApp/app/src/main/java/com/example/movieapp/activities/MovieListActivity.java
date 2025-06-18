package com.example.movieapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.R;
import com.example.movieapp.adapters.MovieAdapter;
import com.example.movieapp.models.Movie;
import com.example.movieapp.viewmodels.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

public class MovieListActivity extends AppCompatActivity {

    private MovieViewModel viewModel;
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private List<Movie> movieList = new ArrayList<>();
    public static final String EXTRA_MOVIE = "movie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        recyclerView = findViewById(R.id.recyclerViewMovies);
        Button buttonAdd = findViewById(R.id.buttonAddMovie);
        Button buttonLogout = findViewById(R.id.buttonLogout);

        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        adapter = new MovieAdapter(movieList, new MovieAdapter.MovieClickListener() {
            @Override
            public void onEdit(Movie movie) {
                Intent intent = new Intent(MovieListActivity.this, AddEditMovieActivity.class);
                intent.putExtra(EXTRA_MOVIE, movie);
                startActivity(intent);
            }

            @Override
            public void onDelete(Movie movie) {
                viewModel.deleteMovie(movie.getId(), task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(MovieListActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                        loadMovies();
                    }
                });
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        buttonAdd.setOnClickListener(v -> {
            startActivity(new Intent(MovieListActivity.this, AddEditMovieActivity.class));
        });

        buttonLogout.setOnClickListener(v -> {
            viewModel.logout();
            startActivity(new Intent(MovieListActivity.this, LoginActivity.class));
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMovies(); // Refresh movies every time this screen appears
    }

    private void loadMovies() {
        viewModel.getMovies().observe(this, movies -> adapter.updateList(movies));
    }
}
