package com.example.api_cocktails.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.api_cocktails.R;
import com.example.api_cocktails.data.CocktailAdapter;
import com.example.api_cocktails.model.Cocktail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CocktailAdapter cocktailAdapter;
    private ArrayList<Cocktail> cocktails;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        cocktails = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        getCocktails();


    }

    private void getCocktails() {
        String url = "https://www.thecocktaildb.com/api/json/v1/1/random.php";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("drinks");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String title = jsonObject.getString("strDrink");
                        String pictureUrl = jsonObject.getString("strDrinkThumb");
                        String category = jsonObject.getString("strCategory");
                        String instructions = jsonObject.getString("strInstructions");

                        Cocktail cocktail = new Cocktail();
                        cocktail.setTitle(title);
                        cocktail.setPictureUrl(pictureUrl);
                        cocktail.setCategory(category);
                        cocktail.setInstructions(instructions);

                        cocktails.add(cocktail);

                    }

                    cocktailAdapter = new CocktailAdapter(MainActivity.this, cocktails);
                    recyclerView.setAdapter(cocktailAdapter);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);
    }
}