package com.top.example.ads


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.unit.dp
import com.google.android.gms.ads.AdRequest
import android.view.LayoutInflater
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView

import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.nativead.NativeAdView


@Composable
fun LoadingWithAdOverlay(
    show: Boolean,
    adUnitId: String
) {
    if (!show) return

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.35f)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                CircularProgressIndicator()

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Aguarde um momento...",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                RealNativeAd(adUnitId)
            }
        }
    }
}









@Composable
fun RealNativeAd(adUnitId: String) {
    val context = LocalContext.current

    AndroidView(
        factory = { ctx ->

            val container = FrameLayout(ctx)

            val adLoader = AdLoader.Builder(ctx, adUnitId)
                .forNativeAd { nativeAd ->

                    val adView = LayoutInflater.from(ctx)
                        .inflate(R.layout.native_ad_layout, container, false) as NativeAdView

                    val headline = adView.findViewById<TextView>(R.id.ad_headline)
                    val body = adView.findViewById<TextView>(R.id.ad_body)
                    val cta = adView.findViewById<Button>(R.id.ad_call_to_action)

                    headline.text = nativeAd.headline
                    body.text = nativeAd.body ?: ""
                    cta.text = nativeAd.callToAction ?: "Ver mais"

                    adView.headlineView = headline
                    adView.bodyView = body
                    adView.callToActionView = cta

                    adView.setNativeAd(nativeAd)

                    container.removeAllViews()
                    container.addView(adView)
                }
                .build()

            adLoader.loadAd(AdRequest.Builder().build())

            container
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    )
}