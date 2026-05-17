@file:OptIn(ExperimentalMaterial3Api::class)

package com.top.example

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices


@Composable
fun rememberLocationPicker(activity: Activity?): Pair<() -> Unit, State<DetalheLocal?>> {

    val detalheLocal = remember { mutableStateOf<DetalheLocal?>(null) }

    if (activity == null) return ({} to detalheLocal)

    val ctx = activity

    val fused = remember(ctx) {
        LocationServices.getFusedLocationProviderClient(ctx)
    }

    fun buscarLocalizacao() {

        fused.lastLocation
            .addOnSuccessListener { loc ->

                if (loc != null) {

                    try {

                        if (!Geocoder.isPresent()) {
                            detalheLocal.value = DetalheLocal(
                                null,
                                null,
                                loc.latitude,
                                loc.longitude
                            )
                            return@addOnSuccessListener
                        }

                        val geocoder = Geocoder(ctx)

                        @Suppress("DEPRECATION")
                        val res = geocoder.getFromLocation(
                            loc.latitude,
                            loc.longitude,
                            1
                        )

                        val addr = res?.firstOrNull()

                        val cidade =
                            addr?.subAdminArea
                                ?: addr?.locality
                                ?: addr?.subLocality

                        val uf = addr?.adminArea

                        detalheLocal.value = DetalheLocal(
                            cidade,
                            uf,
                            loc.latitude,
                            loc.longitude
                        )

                    } catch (e: IOException) {

                        e.printStackTrace()

                        detalheLocal.value = DetalheLocal(
                            null,
                            null,
                            loc.latitude,
                            loc.longitude
                        )

                    } catch (e: Exception) {

                        e.printStackTrace()

                        detalheLocal.value = DetalheLocal(
                            null,
                            null,
                            loc.latitude,
                            loc.longitude
                        )
                    }

                } else {

                    detalheLocal.value = DetalheLocal(
                        null,
                        null,
                        null,
                        null
                    )
                }
            }
            .addOnFailureListener {

                detalheLocal.value = DetalheLocal(
                    null,
                    null,
                    null,
                    null
                )
            }
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->

        val ok =
            perms[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    perms[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (ok) {
            buscarLocalizacao()
        }
    }

    val pedir: () -> Unit = {

        val fine = ContextCompat.checkSelfPermission(
            ctx,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        val coarse = ContextCompat.checkSelfPermission(
            ctx,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (
            fine == PackageManager.PERMISSION_GRANTED ||
            coarse == PackageManager.PERMISSION_GRANTED
        ) {

            buscarLocalizacao()

        } else {

            launcher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    return pedir to detalheLocal
}


private fun distanceKm(
    lat1: Double,
    lon1: Double,
    lat2: Double,
    lon2: Double
): Double {
    val R = 6371.0
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)

    val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(Math.toRadians(lat1)) *
            Math.cos(Math.toRadians(lat2)) *
            Math.sin(dLon / 2) * Math.sin(dLon / 2)

    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
    return R * c
}


//  MainActivity

class MainActivity : ComponentActivity() {

    private var lastLat: Double? = null
    private var lastLng: Double? = null

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            val location = result.lastLocation ?: return

            val lat = location.latitude
            val lng = location.longitude

            sendLocationIfNeeded(lat, lng)
        }
    }



    private val askNotifPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        Log.d("Main", "POST_NOTIFICATIONS granted? $granted")
    }

    private val askLocationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        Log.d("Main", "LOCATION granted? $granted")

        if (granted) {
            startLocationUpdates()
        }
    }

    private fun isFirebaseReady(): Boolean = try {
        val ready = FirebaseApp.getApps(applicationContext).isNotEmpty()
        Log.d("Main", "isFirebaseReady=$ready")
        ready
    } catch (_: Exception) {
        Log.w("Main", "Firebase não inicializado")
        false
    }


    private fun createLocationRequest(): LocationRequest {
        return LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            10000L
        )
            .setMinUpdateDistanceMeters(50f)
            .setMinUpdateIntervalMillis(5000L)
            .build()
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        val client = LocationServices.getFusedLocationProviderClient(this)

        client.requestLocationUpdates(
            createLocationRequest(),
            locationCallback,
            mainLooper
        )
    }

    private fun stopLocationUpdates() {
        val client = LocationServices.getFusedLocationProviderClient(this)
        client.removeLocationUpdates(locationCallback)
    }


    private fun sendLocationIfNeeded(lat: Double, lng: Double) {
        val moved = lastLat == null ||
                distanceKm(lastLat!!, lastLng!!, lat, lng) > 0.05

        if (!moved) return

        lastLat = lat
        lastLng = lng

        lifecycleScope.launch {
            runCatching {
                ApiProvider.get(this@MainActivity)
                    .setDisponivel(
                        mapOf(
                            "lat" to lat,
                            "lng" to lng
                        )
                    )
            }.onSuccess {
                Log.d("Main", "Localização enviada (movimento)")
            }.onFailure {
                Log.e("Main", "Erro ao enviar localização: ${it.message}")
            }
        }
    }


    private fun fetchLocation() {
        getUserLocation(this) { location ->
            if (location != null) {
                val lat = location.latitude
                val lng = location.longitude

                Log.d("Main", "Localização inicial: $lat, $lng")

                sendLocationIfNeeded(lat, lng)
            } else {
                Log.w("Main", "Falha ao obter localização")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MobileAds.initialize(this)

        Notifier.ensureChannels(this)

        if (Build.VERSION.SDK_INT >= 33) {
            askNotifPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            askLocationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            fetchLocation()
            startLocationUpdates()
        }

        ProcessLifecycleOwner.get().lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                AppVisibility.isForeground = true
                Log.d("Main", "AppVisibility -> FOREGROUND")

                fun Intent.scopeToApp() = apply { setPackage(packageName) }
                sendBroadcast(Intent(AppFirebaseMessagingService.ACTION_REFRESH_INBOX).scopeToApp())
                sendBroadcast(Intent(AppFirebaseMessagingService.ACTION_REFRESH_ANY).scopeToApp())
            }

            override fun onStop(owner: LifecycleOwner) {
                AppVisibility.isForeground = false
                Log.d("Main", "AppVisibility -> BACKGROUND")
            }
        })

        if (isFirebaseReady()) {
            val hasJwt = SessionManager(this).getToken() != null
            if (hasJwt) {
                FirebaseMessaging.getInstance().token
                    .addOnSuccessListener { token ->
                        Log.d("Main", "FCM token(onCreate)=${token.take(12)}…")
                        if (token.isNotBlank()) {
                            lifecycleScope.launch(Dispatchers.IO) {
                                runCatching {
                                    ApiProvider.get(this@MainActivity)
                                        .saveMyPushToken(SavePushTokenReq(token))
                                }
                            }
                        }
                    }
            }
        }

        setContent {
            FacilityTheme {
                AppBootstrap {
                    FacilityApp()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (SessionManager(this).getToken() != null) {
            lifecycleScope.launch {
                finalizePendingPatientPurchaseIfAny(
                    ctx = this@MainActivity,
                    activity = this@MainActivity
                )
            }
        }
    }

    override fun onStop() {
        super.onStop()
        stopLocationUpdates()
    }

    fun onLoggedIn(userId: String) {
        AdsGateManager(this).reset()

        lifecycleScope.launch {
            finalizePendingPatientPurchaseIfAny(
                ctx = this@MainActivity,
                activity = this@MainActivity
            )
        }

        if (!isFirebaseReady()) return

        FirebaseMessaging.getInstance().subscribeToTopic("u_$userId")
    }

    fun onLogout() {
        val sm = SessionManager(this)
        val userId = sm.getUserId()

        if (userId != null && isFirebaseReady()) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("u_$userId")
        }


        sm.clear()


        ApiProvider.reset()
    }
}


// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//  Bootstrap/Permissão
// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

@Composable
fun AppBootstrap(content: @Composable () -> Unit) {
    val ctx = LocalContext.current
    val token = remember { SessionManager(ctx).getToken() }

    val ok = remember(token) {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val req = chain.request().newBuilder().apply {
                    if (!token.isNullOrBlank()) header("Authorization", "Bearer $token")
                }.build()
                chain.proceed(req)
            }.build()
    }

    val imageLoader = remember(ok) {
        ImageLoader.Builder(ctx).okHttpClient(ok).build()
    }

    LaunchedEffect(Unit) { Notifier.ensureChannels(ctx) }

    CompositionLocalProvider(LocalImageLoader provides imageLoader) {
        AskNotifPermissionOnce()
        content()
    }
}

@Composable
fun AskNotifPermissionOnce() {
    if (Build.VERSION.SDK_INT < 33) return
    val ctx = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {  }
    )
    LaunchedEffect(Unit) {
        val granted = ContextCompat.checkSelfPermission(
            ctx, Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
        if (!granted) launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }
}

fun backToHome(nav: NavController, ctx: Context) {
    val role = SessionManager(ctx).getRole()

    if (role == Role.PROFISSIONAL) {
        nav.navigate("dashboard/pro") {
            popUpTo("dashboard/pro") { inclusive = true }
            launchSingleTop = true
        }
    } else {
        nav.navigate("home/paciente") {
            popUpTo("home/paciente") { inclusive = true }
            launchSingleTop = true
        }
    }
}
