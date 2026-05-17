package com.top.example.ads

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import  com.top.example.billing.findActivity

@Composable
fun AdMobInterstitial(
    adUnitId: String,
    onAdClosed: () -> Unit
) {
    val context = LocalContext.current
    var interstitialAd by remember { mutableStateOf<InterstitialAd?>(null) }

    LaunchedEffect(Unit) {
        val request = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            adUnitId,
            request,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    ad.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                onAdClosed()
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                onAdClosed()
                            }
                        }
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    onAdClosed()
                }
            }
        )
    }

    LaunchedEffect(interstitialAd) {
        val activity = context.findActivity()
        if (interstitialAd != null && activity != null) {
            interstitialAd?.show(activity)
        }
    }
}
