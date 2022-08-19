package com.example.presskg.utils

import android.app.AlertDialog
import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.presskg.R

object Objects {

    fun showToast(context: Context, int: Int) {
        Toast.makeText(
            context,
            (int),
            Toast.LENGTH_LONG
        ).show()
    }

    fun isGpsEnabled(context: Context): Boolean {
        val service =
            context.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        return service.isProviderEnabled(LocationManager.GPS_PROVIDER) && service.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    fun isNetworkAvailable(context: Context) =
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).run {
            getNetworkCapabilities(activeNetwork)?.run {
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            } ?: false
        }

    fun alertGps(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(R.string.gps_message)
            .setCancelable(false)
            .setPositiveButton(android.R.string.ok) { dialog, id ->
                dialog.cancel()
            }
        builder.show()
    }
}
