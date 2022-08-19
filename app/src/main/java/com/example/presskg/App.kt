package com.example.presskg

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.InitializationStatus

class App : Application(), Application.ActivityLifecycleCallbacks, LifecycleObserver {

    private val ACTIVITY_MOVES_TO_FOREGROUND_HANDLE = true

    private var appOpenAdManager: AppOpenAdManager? = null
    private var currentActivity: Activity? = null

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)
        MobileAds.initialize(this) { v: InitializationStatus? -> }

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this)
        appOpenAdManager = AppOpenAdManager(
            getString(R.string.admob_run_app_id))
    }

    /**
     * LifecycleObserver method that shows the app open ad when the app moves to foreground.
     */
    //    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    //    protected void onMoveToForeground() {
    //        // Show the ad (if available) when the app moves to foreground.
    //        appOpenAdManager.showAdIfAvailable(currentActivity);
    //    }
    fun onStart(owner: LifecycleOwner) {
        // Show the ad (if available) when the app moves to foreground.
        if (ACTIVITY_MOVES_TO_FOREGROUND_HANDLE) {
            appOpenAdManager!!.showAdIfAvailable(currentActivity!!)
        }
    }

    /**
     * ActivityLifecycleCallback methods.
     */
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {
        // An ad activity is started when an ad is showing, which could be AdActivity class from Google
        // SDK or another activity class implemented by a third party mediation partner. Updating the
        // currentActivity only when an ad is not showing will ensure it is not an ad activity, but the
        // one that shows the ad.
        if (!appOpenAdManager!!.isShowingAd) {
            currentActivity = activity
        }
    }

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}

    /**
     * Shows an app open ad.
     *
     * @param activity                 the activity that shows the app open ad
     * @param onShowAdCompleteListener the listener to be notified when an app open ad is complete
     */
    fun showAdIfAvailable(
        activity: Activity,
        onShowAdCompleteListener: OnShowAdCompleteListener
    ) {
        // We wrap the showAdIfAvailable to enforce that other classes only interact with MyApplication
        // class.
        appOpenAdManager?.showAdIfAvailable(activity, onShowAdCompleteListener)
    }

    /**
     * Interface definition for a callback to be invoked when an app open ad is complete
     * (i.e. dismissed or fails to show).
     */
    interface OnShowAdCompleteListener {
        fun onShowAdComplete()
        fun adAdDismissedBackPresed()
    }

}