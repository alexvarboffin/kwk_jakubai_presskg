package com.example.presskg;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;


import java.util.Date;

/**
 * Inner class that loads and shows app open ads.
 */
public class AppOpenAdManager {


    private static final String TAG = "@@@";

    private final String AD_UNIT_ID;

    private AppOpenAd appOpenAd = null;
    private boolean isLoadingAd = false;
    public boolean isShowingAd = false;

    /**
     * Keep track of the time an app open ad is loaded to ensure you don't show an expired ad.
     */
    private long loadTime = 0;

    /**
     * Constructor.
     */
    public AppOpenAdManager(String ad_unit_id) {
        this.AD_UNIT_ID = ad_unit_id;
        Log.d(TAG, "@@@: " + ad_unit_id);
    }

    /**
     * Load an ad.
     *
     * @param context the context of the activity that loads the ad
     */
    private void loadAd(Context context) {
        // Do not load ad if there is an unused ad or one is already loading.
        if (isLoadingAd || isAdAvailable()) {
            return;
        }

        isLoadingAd = true;
        AdRequest request = new AdRequest.Builder().build();
        AppOpenAd.load(
                context,
                AD_UNIT_ID,
                request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                new AppOpenAd.AppOpenAdLoadCallback() {
                    /**
                     * Called when an app open ad has loaded.
                     *
                     * @param ad the loaded app open ad.
                     */
                    @Override
                    public void onAdLoaded(@NonNull AppOpenAd ad) {
                        appOpenAd = ad;
                        isLoadingAd = false;
                        loadTime = (new Date()).getTime();
                        Log.d(TAG, "onAdLoaded.");
                    }

                    /**
                     * Called when an app open ad has failed to load.
                     *
                     * @param loadAdError the error.
                     */
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        isLoadingAd = false;
                        Log.d(TAG, "onAdFailedToLoad: " + loadAdError.getMessage());
                    }
                });
    }

    /**
     * Check if ad was loaded more than n hours ago.
     */
    private boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
        long dateDifference = (new Date()).getTime() - loadTime;
        long numMilliSecondsPerHour = 3_600_000;
        return (dateDifference < (numMilliSecondsPerHour * numHours));
    }

    /**
     * Check if ad exists and can be shown.
     */
    private boolean isAdAvailable() {
        // Ad references in the app open beta will time out after four hours, but this time limit
        // may change in future beta versions. For details, see:
        // https://support.google.com/admob/answer/9341964?hl=en
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
    }

    /**
     * Show the ad if one isn't already showing.
     *
     * @param activity the activity that shows the app open ad
     */
    public void showAdIfAvailable(@NonNull final Activity activity) {
        showAdIfAvailable(
                activity,
                new App.OnShowAdCompleteListener() {
                    @Override
                    public void onShowAdComplete() {
                        // Empty because the user will go back to the activity that shows the ad.

                    }

                    @Override
                    public void adAdDismissedBackPresed() {
                        // Empty because the user will go back to the activity that shows the ad.
                    }
                });
    }

    /**
     * Show the ad if one isn't already showing.
     *
     * @param activity                  the activity that shows the app open ad
     * @param onShowAdCompleteListener0 the listener to be notified when an app open ad is complete
     */
    public void showAdIfAvailable(
            @NonNull final Activity activity,
            @NonNull App.OnShowAdCompleteListener onShowAdCompleteListener0) {
        // If the app open ad is already showing, do not show the ad again.
        if (isShowingAd) {
            Log.d(TAG, "The app open ad is already showing.");
            return;
        }

        // If the app open ad is not available yet, invoke the callback then load the ad.
        if (!isAdAvailable()) {
            Log.d(TAG, "The app open ad is not ready yet.");
            onShowAdCompleteListener0.onShowAdComplete();
            loadAd(activity);
            return;
        }

        Log.d(TAG, "Will show ad.");

        appOpenAd.setFullScreenContentCallback(
                new FullScreenContentCallback() {
                    /** Called when full screen content is dismissed. */
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Set the reference to null so isAdAvailable() returns false.
                        appOpenAd = null;
                        isShowingAd = false;

                        onShowAdCompleteListener0.adAdDismissedBackPresed();
                        loadAd(activity);
                    }

                    /** Called when fullscreen content failed to show. */
                    @Override
                    public void onAdFailedToShowFullScreenContent(
                            @NonNull AdError adError) {
                        appOpenAd = null;
                        isShowingAd = false;

                        Log.d(TAG, "onAdFailedToShowFullScreenContent: " + adError.getMessage());
                        onShowAdCompleteListener0.onShowAdComplete();
                        loadAd(activity);
                    }

                    /** Called when fullscreen content is shown. */
                    @Override
                    public void onAdShowedFullScreenContent() {
                        Log.d(TAG, "onAdShowedFullScreenContent.");
                    }
                });

        isShowingAd = true;
        appOpenAd.show(activity);
    }
}