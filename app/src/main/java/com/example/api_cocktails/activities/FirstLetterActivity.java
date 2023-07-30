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

                        Cocktail cocktail = new Cocktail();
                        cocktail.setTitle(title);
                        cocktail.setPictureUrl(pictureUrl);
                        cocktail.setCategory(category);
                        cocktail.setInstructions(instructions);

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