package com.example.api_cocktails.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.api_cocktails.R;
import com.example.api_cocktails.model.Cocktail;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CocktailAdapter extends RecyclerView.Adapter<CocktailAdapter.CocktailViewHolder> {

    private Context context;
    private ArrayList<Cocktail> cocktails;

    public CocktailAdapter(Context context, ArrayList<Cocktail> cocktails) {
        this.context = context;
        this.cocktails = cocktails;
    }

    @NonNull
    @Override
    public CocktailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cocktail_item, parent, false);
        return new CocktailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CocktailViewHolder holder, int position) {
        Cocktail currentCocktail = cocktails.get(position);
        String title = currentCocktail.getTitle();
        String pictureUrl = currentCocktail.getPictureUrl();
        String category = currentCocktail.getCategory();
        String instructions = currentCocktail.getInstructions();
        String ingredients = currentCocktail.getIngredients();

        holder.titleTextView.setText(title);
        Picasso.get().load(pictureUrl).fit().centerInside().into(holder.pictureImageView);
        holder.categoryTextView.setText(category);
        holder.instructionsTextView.setText(instructions);
        holder.ingredientTextView.setText(ingredients);


    }

    @Override
    public int getItemCount() {

        return cocktails.size();
    }

    public class CocktailViewHolder extends RecyclerView.ViewHolder {

        ImageView pictureImageView;
        TextView titleTextView;
        TextView categoryTextView;
        TextView instructionsTextView;
        TextView ingredientTextView;


        public CocktailViewHolder(@NonNull View itemView) {
            super(itemView);
            pictureImageView = itemView.findViewById(R.id.pictureImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
            instructionsTextView = itemView.findViewById(R.id.instructionsTextView);
            ingredientTextView = itemView.findViewById(R.id.ingredientTextView);


        }
    }
}
