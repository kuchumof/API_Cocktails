package com.example.api_cocktails.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CocktailAdapter cocktailAdapter;
    private ArrayList<Cocktail> cocktails;
    private RequestQueue requestQueue;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


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
                        String strIngredient1 = jsonObject.getString("strIngredient1");

                        Cocktail cocktail = new Cocktail();
                        cocktail.setTitle(title);
                        cocktail.setPictureUrl(pictureUrl);
                        cocktail.setCategory(category);
                        cocktail.setInstructions(instructions);
                        cocktail.setIngredients(strIngredient1);

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

    public void clickLetterButton(View view) {

        switch (view.getId()){
            case R.id.button_a: openFirstLetterActivity("a"); break;
            case R.id.button_b: openFirstLetterActivity("b"); break;
            case R.id.button_c: openFirstLetterActivity("c"); break;
            case R.id.button_d: openFirstLetterActivity("d"); break;
            case R.id.button_e: openFirstLetterActivity("e"); break;
            case R.id.button_f: openFirstLetterActivity("f"); break;
            case R.id.button_g: openFirstLetterActivity("g"); break;
            case R.id.button_h: openFirstLetterActivity("h"); break;
            case R.id.button_i: openFirstLetterActivity("i"); break;
            case R.id.button_j: openFirstLetterActivity("j"); break;
            case R.id.button_k: openFirstLetterActivity("k"); break;
            case R.id.button_l: openFirstLetterActivity("l"); break;
            case R.id.button_m: openFirstLetterActivity("m"); break;
            case R.id.button_n: openFirstLetterActivity("n"); break;
            case R.id.button_o: openFirstLetterActivity("o"); break;
            case R.id.button_p: openFirstLetterActivity("p"); break;
            case R.id.button_q: openFirstLetterActivity("q"); break;
            case R.id.button_r: openFirstLetterActivity("r"); break;
            case R.id.button_s: openFirstLetterActivity("s"); break;
            case R.id.button_t: openFirstLetterActivity("t"); break;
            case R.id.button_v: openFirstLetterActivity("v"); break;
            case R.id.button_w: openFirstLetterActivity("w"); break;
            case R.id.button_y: openFirstLetterActivity("y"); break;
            case R.id.button_z: openFirstLetterActivity("z"); break;

        }


    }

    private void openFirstLetterActivity(String letter) {
        Intent intent = new Intent(MainActivity.this, FirstLetterActivity.class);
        intent.putExtra("letter", letter);
        startActivity(intent);
        finish();
    }
}