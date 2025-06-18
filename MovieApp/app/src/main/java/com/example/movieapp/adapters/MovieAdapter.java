package com.example.movieapp.adapters;

import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.movieapp.R;
import com.example.movieapp.models.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    public interface MovieClickListener {
        void onEdit(Movie movie);
        void onDelete(Movie movie);
    }

    private List<Movie> movieList;
    private MovieClickListener listener;

    public MovieAdapter(List<Movie> movieList, MovieClickListener listener) {
        this.movieList = movieList;
        this.listener = listener;
    }

    public void updateList(List<Movie> newList) {
        this.movieList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.textTitle.setText(movie.getTitle());
        holder.textYear.setText("Year: " + movie.getYear());

        Glide.with(holder.itemView.getContext())
                .load(movie.getPosterUrl())
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.imagePoster);

        holder.buttonEdit.setOnClickListener(v -> listener.onEdit(movie));
        holder.buttonDelete.setOnClickListener(v -> listener.onDelete(movie));
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imagePoster;
        TextView textTitle, textYear;
        ImageButton buttonEdit, buttonDelete;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imagePoster = itemView.findViewById(R.id.imagePoster);
            textTitle = itemView.findViewById(R.id.textTitle);
            textYear = itemView.findViewById(R.id.textYear);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
