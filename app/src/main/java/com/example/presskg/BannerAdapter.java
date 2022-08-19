package com.example.presskg;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class BannerAdapter {

    private static final String TAG = "@@@";

    private final String BANNER_ID;
    private final Context context;
    private InterstitialAd mInterstitialAd;


    public BannerAdapter(Context context, String BANNER_ID) {
        this.context = context;
        this.BANNER_ID = BANNER_ID;
    }

    private final FullScreenContentCallback callback = new FullScreenContentCallback() {
        @Override
        public void onAdClicked() {
            // Called when a click is recorded for an ad.
            Log.d(TAG, "Ad was clicked.");
        }

        @Override
        public void onAdDismissedFullScreenContent() {
            // Called when ad is dismissed.
            // Set the ad reference to null so you don't show the ad a second time.
            Log.d(TAG, "Ad dismissed fullscreen content.");
            mInterstitialAd = null;
            loadNewBanner(context);
        }

        @Override
        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
            // Called when ad fails to show.
            Log.e(TAG, "Ad failed to show fullscreen content.");
            mInterstitialAd = null;
        }

        @Override
        public void onAdImpression() {
            // Called when an impression is recorded for an ad.
            Log.d(TAG, "Ad recorded an impression.");
        }

        @Override
        public void onAdShowedFullScreenContent() {
            // Called when ad is shown.
            Log.d(TAG, "Ad showed fullscreen content.");
        }
    };

    public void loadNewBanner(Context context) {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(context, BANNER_ID,
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        mInterstitialAd.setFullScreenContentCallback(callback);
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        //loadAdError.getCode() - 3 error_code_no_fill
                        Log.d(TAG, ""+loadAdError.getCode());//3
                        Log.d(TAG, ""+loadAdError.getMessage());//No ad config.
                        mInterstitialAd = null;

                    }
                });
    }

    public void showBanner(MainActivity mainActivity) {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(mainActivity);
        } else {
            Log.d(TAG, "The interstitial ad wasn't ready yet.");
        }
    }
}
