package com.example.api_cocktails.activities;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.api_cocktails.R;
import com.example.api_cocktails.data.CocktailAdapter;
import com.example.api_cocktails.model.Cocktail;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FirstLetterActivity extends AppCompatActivity {

    private InterstitialAd mInterstitialAd;
    private RecyclerView recyclerView;
    private CocktailAdapter cocktailAdapter;
    private ArrayList<Cocktail> cocktails;
    private RequestQueue requestQueue;
    String letter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_letter);


        adsLoad();


        Intent intent = getIntent();
        letter = intent.getStringExtra("letter");


        recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        cocktails = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        getCocktails();


    }

    /**
     * добавление рекламы - "Межстраничные объявления"
     */
    private void adsLoad() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i("Ads_inter", "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d("Ads_inter", loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });
    }
// String url = "https://www.thecocktaildb.com/api/json/v1/1/search.php?f=" + letter;
private void getCocktails() {
    String url = "https://www.thecocktaildb.com/api/json/v1/1/search.php?f=" + letter;

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

                    String[] ingredients = new String[15];
                    String strIngredient1 = jsonObject.getString("strIngredient1");
                    ingredients[0] = strIngredient1;
                    String strIngredient2 = jsonObject.getString("strIngredient2");
                    ingredients[1] = strIngredient2;
                    String strIngredient3 = jsonObject.getString("strIngredient3");
                    ingredients[2] = strIngredient3;
                    String strIngredient4 = jsonObject.getString("strIngredient4");
                    ingredients[3] = strIngredient4;
                    String strIngredient5 = jsonObject.getString("strIngredient5");
                    ingredients[4] = strIngredient5;
                    String strIngredient6 = jsonObject.getString("strIngredient6");
                    ingredients[5] = strIngredient6;
                    String strIngredient7 = jsonObject.getString("strIngredient7");
                    ingredients[6] = strIngredient7;
                    String strIngredient8 = jsonObject.getString("strIngredient8");
                    ingredients[7] = strIngredient8;
                    String strIngredient9 = jsonObject.getString("strIngredient9");
                    ingredients[8] = strIngredient9;
                    String strIngredient10 = jsonObject.getString("strIngredient10");
                    ingredients[9] = strIngredient10;
                    String strIngredient11 = jsonObject.getString("strIngredient11");
                    ingredients[10] = strIngredient11;
                    String strIngredient12 = jsonObject.getString("strIngredient12");
                    ingredients[11] = strIngredient12;
                    String strIngredient13 = jsonObject.getString("strIngredient13");
                    ingredients[12] = strIngredient13;
                    String strIngredient14 = jsonObject.getString("strIngredient14");
                    ingredients[13] = strIngredient14;
                    String strIngredient15 = jsonObject.getString("strIngredient15");
                    ingredients[14] = strIngredient15;

                    String ingredientAllStr = "Ingredients: " + ingredients[0];
                    for (int j = 1; j < ingredients.length; j++) {
                        if (ingredients[j] != "null") {
                            ingredientAllStr = ingredientAllStr + ", " + ingredients[j];
                        }
                    }
                    ingredientAllStr = ingredientAllStr + ".";

                    Cocktail cocktail = new Cocktail();
                    cocktail.setTitle(title);
                    cocktail.setPictureUrl(pictureUrl);
                    cocktail.setCategory(category);
                    cocktail.setInstructions(instructions);
                    cocktail.setIngredients(ingredientAllStr);

                    cocktails.add(cocktail);

                }

                cocktailAdapter = new CocktailAdapter(FirstLetterActivity.this, cocktails);
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

    /**
     * кнопка "назад"
     */
    @Override
    public void onBackPressed() {

        if (mInterstitialAd != null) {
            mInterstitialAd.show(FirstLetterActivity.this);

            actionsOnFullScreenAds();


        } else {
            startActivity(new Intent(FirstLetterActivity.this, MainActivity.class));
            finish();
            Log.d("Ads_inter", "The interstitial ad wasn't ready yet.");
        }


    }

    private void actionsOnFullScreenAds() {
        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdClicked() {
                // Called when a click is recorded for an ad.
                Log.d("Ads_inter", "Ad was clicked.");
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                Log.d("Ads_inter", "Ad dismissed fullscreen content.");
                mInterstitialAd = null;
                startActivity(new Intent(FirstLetterActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                // Called when ad fails to show.
                Log.e("Ads_inter", "Ad failed to show fullscreen content.");
                mInterstitialAd = null;
            }

            @Override
            public void onAdImpression() {
                // Called when an impression is recorded for an ad.
                Log.d("Ads_inter", "Ad recorded an impression.");
            }

            @Override
            public void onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d("Ads_inter", "Ad showed fullscreen content.");
            }
        });
    }
}