@file:OptIn(ExperimentalMaterial3Api::class)

package com.top.example.contratante


import android.app.Activity

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.alpha

import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import com.top.example.net.MeProfileResp
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import com.top.example.net.ConversationSummary
import com.top.example.net.ProPublicDto

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star

import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.getValue
import com.top.example.net.JobMineDto
import androidx.compose.runtime.mutableStateOf
import com.top.example.net.*
import kotlinx.coroutines.delay

import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem

import androidx.compose.runtime.saveable.rememberSaveable
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import android.content.Context
import kotlinx.coroutines.flow.first
import com.top.example.net.startOrFindChat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.isActive
import com.top.example.net.Role
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.MoreVert
import java.time.Instant

import androidx.compose.material.icons.filled.ArrowBack

import com.top.example.net.UpdateRegionReq

import com.top.example.data.seenStore

import android.util.Log

import kotlinx.coroutines.withTimeout

import androidx.compose.foundation.interaction.MutableInteractionSource

import kotlinx.coroutines.withTimeoutOrNull

import androidx.compose.ui.text.style.TextOverflow

import androidx.compose.runtime.Composable

import androidx.compose.material.icons.filled.Chat
import androidx.compose.runtime.derivedStateOf
import com.top.example.push.PushRefreshEffect
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text

import android.net.Uri

import androidx.compose.animation.core.tween

import com.top.example.push.PushCache

import com.top.example.util.calcUnreadOnly

import com.top.example.chatbadge.ChatBadgeStore
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items


import com.top.example.billing.restoreExistingSubscriptions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.FilterChip

import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.rememberCoroutineScope

import com.top.example.util.shareApp
import androidx.compose.material.icons.filled.Share
import com.top.example.util.ReportUserDialog
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.draw.shadow
import androidx.compose.animation.core.*

import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.ui.graphics.Brush

import com.top.example.settings.DeleteAccountDialog


import androidx.compose.material3.Divider

import androidx.compose.foundation.border

import androidx.compose.ui.unit.sp

import androidx.compose.foundation.layout.width

import com.top.example.bus.AppBus
import com.top.example.bus.ChatEvent
import com.top.example.chatbadge.unseenChatIds
import retrofit2.HttpException
import  com.top.example.util.badgeFromTipo

import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*

import com.top.example.ads.AdMobIds
import com.top.example.ads.AdMobInterstitial

import com.top.example.ads.rememberAdTimer


import com.top.example.util.Story

import com.top.example.data.toDomain

import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.SolidColor

import com.top.example.ui.MaybeAskForReview

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut



import androidx.activity.compose.BackHandler


import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically

import com.top.example.R

import com.top.example.AdsGateManager
import com.top.example.AppTitlePillGradient
import com.top.example.BoxTab
import com.top.example.ClickZoomImage
import com.top.example.FullscreenNoticeCard
import com.top.example.GridProfissoes
import com.top.example.LockedOverlay
import com.top.example.MockRepo
import com.top.example.PROFISSOES_UI
import com.top.example.ProfissaoBottomSheet
import com.top.example.ProfissaoUi

import com.top.example.RatingBarMercadoLivre

import com.top.example.SessionManager
import com.top.example.StoryUser
import com.top.example.StoryViewer

import com.top.example.label
import com.top.example.lightBackgroundFrom
import com.top.example.parseNoticeThemeHeader
import com.top.example.rememberOnOpenBilling
import com.top.example.util.RolePill
import com.top.example.util.bust
import com.top.example.util.SEEN_ACCEPTED_KEY
import com.top.example.util.PROMPTED_BY_ME_KEY
import com.top.example.util.RATED_BY_ME_KEY
import com.top.example.util.REGION_CIDADE_KEY
import com.top.example.util.REGION_UF_KEY
import  com.top.example.rememberLocationPicker




private val Context.seenStore by preferencesDataStore("patient_seen_prefs")








private const val TAG = "PatientHome"




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContratanteHomeScreen(
    onOpenCuidador: (id: String, name: String?, avatar: String?, tipo: TipoPro) -> Unit,
    onOpenSettings: () -> Unit,
    onLogout: () -> Unit,
    onOpenChat: (String) -> Unit,
    onGoToPedido: (String, String, TipoPro) -> Unit,
    onOpenChatByUser: (String) -> Unit = {},
    onDeleteAccount: (password: String) -> Unit = {},
    autoRefresh: Boolean = true
) {
    val TAG = "PatientHome"
    val ctx = LocalContext.current
    val activity = ctx as? Activity

    val (pedirLocalizacao, detalheLocalState) = rememberLocationPicker(activity)
    val detalheLocal = detalheLocalState.value

    activity?.let { MaybeAskForReview(it, minOpens = 7, minActions = 3) }

    val api = ApiProvider.get(ctx)
    val scope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current
    val drawerState = rememberDrawerState(DrawerValue.Closed)




    var profile by remember { mutableStateOf<MeProfileResp?>(null) }
    var ufFiltro by remember { mutableStateOf<String?>(null) }
    var cidadeFiltro by remember { mutableStateOf<String?>(null) }

    var savedUf by rememberSaveable { mutableStateOf<String?>(null) }
    var savedCidade by rememberSaveable { mutableStateOf<String?>(null) }


    val UFS = remember { BrazilRegion.UFS }


    var showRegionDialog by remember { mutableStateOf(false) }
    var tempUf by remember { mutableStateOf("") }
    var tempCidade by remember { mutableStateOf("") }
    var ufMenuExpanded by remember { mutableStateOf(false) }
    var savingRegion by remember { mutableStateOf(false) }
    var regionErr by remember { mutableStateOf<String?>(null) }


    var uploadingAvatar by remember { mutableStateOf(false) }
    var avatarErr by remember { mutableStateOf<String?>(null) }
    var avatarStamp by remember { mutableStateOf(0L) }


    var selectedTab by rememberSaveable { mutableStateOf(BoxTab.INBOX) }
    var inbox by remember { mutableStateOf<List<ConversationSummary>>(emptyList()) }
    var inboxLoading by remember { mutableStateOf(true) }
    var archived by remember { mutableStateOf<List<ConversationSummary>>(emptyList()) }
    var archivedLoading by remember { mutableStateOf(true) }
    var pausePolling by remember { mutableStateOf(false) }
    var chatUnseenIds by remember { mutableStateOf<Set<String>>(emptySet()) }
    var unreadCount by remember { mutableStateOf(0) }
    val myId = remember(ctx) { SessionManager(ctx).getUserId().orEmpty() }
    val isPatient by remember { derivedStateOf { MockRepo.roleEscolhida == Role.CONTRATANTE } }


    var messagesSheetOpen by rememberSaveable { mutableStateOf(false) }



    var lista by rememberSaveable { mutableStateOf<List<ProPublicDto>>(emptyList()) }
    var loadingPros by remember { mutableStateOf(false) }
    var erroPros by remember { mutableStateOf<String?>(null) }


    var seenAcceptedIds by rememberSaveable { mutableStateOf<Set<String>>(emptySet()) }
    var unseenAcceptedIds by remember { mutableStateOf<Set<String>>(emptySet()) }
    var dialogJob by remember { mutableStateOf<JobMineDto?>(null) }

    var rateDialog by remember { mutableStateOf<JobMineDto?>(null) }
    var ratingByUser by remember { mutableStateOf(0) }
    var sendingRating by remember { mutableStateOf(false) }
    var sendRatingErr by remember { mutableStateOf<String?>(null) }

    var ratedByMe by rememberSaveable { mutableStateOf<Set<String>>(emptySet()) }
    var promptedByMe by rememberSaveable { mutableStateOf<Set<String>>(emptySet()) }


    var notice by remember { mutableStateOf<NoticeDto?>(null) }
    var noticeTheme by remember { mutableStateOf<NoticeTheme?>(null) }
    var showingNotice by remember { mutableStateOf(false) }


    val snackbar = remember { SnackbarHostState() }
    val session = remember(ctx) { SessionManager(ctx) }
    var subStatus by remember { mutableStateOf<SubStatusResp?>(null) }
    var loadingSub by remember { mutableStateOf(true) }
    var subErr by remember { mutableStateOf<String?>(null) }
    var billingInProgress by remember { mutableStateOf(false) }
    val locked by remember { derivedStateOf { subStatus?.canAccess == false } }
    var triedRestore by remember { mutableStateOf(false) }

    var reportOpen by remember { mutableStateOf<String?>(null) }


    var showDeleteAcc by remember { mutableStateOf(false) }
    var deletePwd by remember { mutableStateOf("") }
    var deletingAcc by remember { mutableStateOf(false) }
    var deleteErr by remember { mutableStateOf<String?>(null) }

    var selectedTipo by remember { mutableStateOf<TipoPro?>(null) }
    var busca by rememberSaveable { mutableStateOf("") }

    var showSheet by remember { mutableStateOf(false) }

    var showAllProfissoes by remember { mutableStateOf(false) }

    val adsManager = remember { AdsGateManager(ctx) }

    var showAd by remember { mutableStateOf(false) }
    var removerAd by remember { mutableStateOf(false) }
    var showPaywall by remember { mutableStateOf(false) }
    var adCanClose by remember { mutableStateOf(false) }

    val role = session.getRoleOrDefault()
    val canAccess = subStatus?.canAccess == true

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    var suggestionOpen by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(true) }


    var stories by remember { mutableStateOf<List<Story>>(emptyList()) }
    var selectedStory by remember { mutableStateOf<Story?>(null) }
    var storiesState by remember { mutableStateOf<List<Story>>(emptyList()) }


    val deleteChat: suspend (String) -> Unit =
        { id -> withTimeout(6_000) { api.deleteChat(id) } }

    var selectedStoryUserId by remember { mutableStateOf<String?>(null) }



    val ACCEPTED_HANDLED_KEY =
        stringSetPreferencesKey("accepted_handled_ids")

    var profissoesExpanded by remember { mutableStateOf(false) }

    var ratingComment by remember { mutableStateOf("") }
    var showAll by remember { mutableStateOf(false) }

    var ultimaCidade by remember { mutableStateOf<String?>(null) }
    var ultimaUf by remember { mutableStateOf<String?>(null) }
    var ultimoTipo by remember { mutableStateOf<TipoPro?>(null) }






    val onOpenBillingImpl = rememberOnOpenBilling(
        ctx = ctx, api = api, scope = scope,
        onSubStatus = { sub ->
            subStatus = sub
            session.setSubscriptionSnapshot(
                subActive = sub.subActive, canAccess = sub.canAccess,
                trialEndsAtIso = sub.trialEndsAt, trialClaimed = sub.trialClaimed
            )
        },
        setBillingInProgress = { billingInProgress = it },
        snackbar = snackbar
    )


    LaunchedEffect(Unit) {
        storiesState = api.listStories().map { it.toDomain() }
    }


    LaunchedEffect(detalheLocal) {
        if (detalheLocal == null) {
            pedirLocalizacao()
        }
    }


    val AD_INTERVAL = 5 * 60 * 1000L // 5 minutos



    rememberAdTimer(
        enabled = role == Role.CONTRATANTE && !canAccess,
        intervalMillis = AD_INTERVAL
    ) {

        if (!showAd && !showPaywall) {
            showAd = true
        }
    }



    suspend fun markAcceptedHandled(ctx: Context, jobId: String) {
        seenAcceptedIds = seenAcceptedIds + jobId
        unseenAcceptedIds = unseenAcceptedIds - jobId

        ctx.seenStore.edit { prefs ->
            val current = prefs[SEEN_ACCEPTED_KEY] ?: emptySet()
            prefs[SEEN_ACCEPTED_KEY] = current + jobId
        }
    }





    val pickImagePatient = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri == null) return@rememberLauncherForActivityResult
        scope.launch {
            try {
                uploadingAvatar = true
                avatarErr = null
                val part = contentUriToSmartPart(
                    resolver = ctx.contentResolver,
                    uri = uri,
                    fieldName = "file",
                    compressThresholdBytes = 400 * 1024L,
                    maxSizePx = 1280,
                    quality = 82
                )
                val resp = api.uploadAvatar(part)
                profile = profile?.copy(avatarUrl = resp.avatarUrl) ?: profile
                avatarStamp = System.currentTimeMillis()
            } catch (t: Throwable) {
                avatarErr = t.message ?: "Falha ao enviar imagem"
            } finally {
                uploadingAvatar = false
            }
        }
    }


    suspend fun refreshInbox() {
        fun lastFromCompat(row: Any, myId: String?): String? {
            for (name in arrayOf("lastFromMe", "fromMe")) {
                try {
                    val f = row.javaClass.getDeclaredField(name)
                    f.isAccessible = true
                    val v = f.get(row)
                    val b = when (v) {
                        is Boolean -> v; is java.lang.Boolean -> v.booleanValue(); else -> null
                    }
                    if (b == true) return myId
                } catch (_: Throwable) {
                }
            }
            for (name in arrayOf(
                "lastFromUserId",
                "fromUserId",
                "lastFrom",
                "from",
                "authorId",
                "lastSenderId"
            )) {
                try {
                    val f = row.javaClass.getDeclaredField(name)
                    f.isAccessible = true
                    val v = f.get(row) as? String
                    if (!v.isNullOrBlank()) return v
                } catch (_: Throwable) {
                }
            }
            return null
        }



        runCatching {
            val new = withContext(Dispatchers.IO) { api.myChats() }
            val unseen = unseenChatIds(
                ctx = ctx,
                items = new,
                id = { it.id },
                lastAt = { it.lastAt },
                lastFrom = { lastFromCompat(it as Any, myId) },
                myId = myId.takeIf { it.isNotBlank() }
            )
            withContext(Dispatchers.Main) {
                inbox = new
                chatUnseenIds = unseen
                unreadCount = unseen.size
                inboxLoading = false
            }
        }.onFailure { withContext(Dispatchers.Main) { inboxLoading = false } }
    }


    LaunchedEffect(Unit) {
        AppBus.events.collect { ev ->
            if (ev is ChatEvent.Incoming) {
                scope.launch { refreshInbox() }
            }
        }
    }

    LaunchedEffect(Unit) {
        try {
            val resp = api.listStories()

            Log.d("STORIES_DEBUG", "Stories recebidos: ${resp.size}")

            resp.forEach {
                Log.d(
                    "STORIES_DEBUG",
                    "Story user=${it.userName}, " +
                            "hasPosts=${it.hasPosts}, " +
                            "hasUnseen=${it.hasUnseen}, " +
                            "lastPostId=${it.lastPostId}"
                )
            }

            stories = resp.map { it.toDomain() }

        } catch (e: Exception) {
            Log.e("STORIES_DEBUG", "Erro ao carregar stories", e)
        }
    }


    //  PushRefreshEffect
    PushRefreshEffect(
        onChat = { scope.launch { refreshInbox() } },
        onJobs = { },
        onAny = { scope.launch { refreshInbox() } }
    )

    suspend fun refreshArchived() {
        archived = runCatching { api.myArchivedChats() }.getOrDefault(emptyList())
    }

    fun applyJobsSnapshot(mine: List<JobMineDto>) {
        val acceptedNow =
            mine.asSequence().filter { it.status == "ACEITO" }.map { it.id }.toSet()
        val newUnseen = acceptedNow - seenAcceptedIds
        unseenAcceptedIds = newUnseen
        if (dialogJob == null && newUnseen.isNotEmpty()) {
            dialogJob = mine.firstOrNull { it.id in newUnseen }
        }
        val toRate = mine.asSequence()
            .filter {
                it.status == "FINALIZADO" &&
                        !it.ratedByPatient &&
                        !it.ratingDismissed &&
                        it.id !in promptedByMe &&
                        it.id !in ratedByMe
            }
            .map { it.id }
            .toSet()
        val firstToRate = toRate.firstOrNull()
        if (firstToRate != null && rateDialog == null) {
            rateDialog = mine.firstOrNull { it.id == firstToRate }
            promptedByMe = promptedByMe + firstToRate
        }
    }

    suspend fun refreshJobsNow() {
        val mine = runCatching { api.listMyPatientJobs() }.getOrDefault(emptyList())
        applyJobsSnapshot(mine)
    }

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            val new = runCatching { api.myChats() }.getOrDefault(emptyList())
            val base = calcUnreadOnly(ctx, new)
            val pending = PushCache.getChat(ctx)
            unreadCount = base + pending
            inbox = new
            if (pending > 0) PushCache.clearChat(ctx)
        }
    }

    LaunchedEffect(cidadeFiltro, ufFiltro, selectedTipo) {
        while (true) {

            val cidade = cidadeFiltro ?: detalheLocal?.cidade
            val uf = ufFiltro ?: detalheLocal?.uf

            if (!cidade.isNullOrBlank() && !uf.isNullOrBlank()) {

                lista = runCatching {
                    api.listPros(
                        uf = uf,
                        cidade = cidade,
                        tipo = selectedTipo?.name
                    )
                }.getOrElse {
                    emptyList()
                }
            }

            delay(5000)
        }
    }

    // DataStore + perfil
    LaunchedEffect(Unit) {
        runCatching {
            val pref = ctx.seenStore.data.first()
            savedUf = pref[REGION_UF_KEY]
            savedCidade = pref[REGION_CIDADE_KEY]
            seenAcceptedIds = pref[SEEN_ACCEPTED_KEY] ?: emptySet()
            ratedByMe = pref[RATED_BY_ME_KEY] ?: emptySet()
            promptedByMe = pref[PROMPTED_BY_ME_KEY] ?: emptySet()
        }
        runCatching { api.getMyProfile() }.onSuccess { p ->
            profile = p
            if (ufFiltro.isNullOrBlank()) {
                ufFiltro = savedUf?.trim()?.uppercase() ?: p.uf?.trim()?.uppercase()
            }
            if (cidadeFiltro.isNullOrBlank()) {
                cidadeFiltro = savedCidade?.trim() ?: p.cidade?.trim()
            }
        }
    }

    LaunchedEffect(detalheLocal, ufFiltro, cidadeFiltro, selectedTipo) {

        val cidade = cidadeFiltro ?: detalheLocal?.cidade
        val uf = ufFiltro ?: detalheLocal?.uf
        val lat = detalheLocal?.lat
        val lng = detalheLocal?.lng

        if (cidade.isNullOrBlank() || uf.isNullOrBlank()) return@LaunchedEffect



        // salva estado atual
        ultimaCidade = cidade
        ultimaUf = uf
        ultimoTipo = selectedTipo

        loadingPros = true
        erroPros = null

        runCatching {
            api.listPros(
                uf = uf,
                cidade = cidade,
                tipo = selectedTipo?.name,
                q = null,
                lat = lat,
                lng = lng
            )
        }.onSuccess {
            lista = it
        }.onFailure {
            erroPros = it.message
        }

        loadingPros = false
    }

    // Aviso admin
    LaunchedEffect(Unit) {
        runCatching { api.getMyNoticeResponse() }
            .onSuccess { resp ->
                if (resp.isSuccessful) {
                    resp.body()?.let { b ->
                        notice = b
                        noticeTheme = parseNoticeThemeHeader(resp.headers()["X-Notice-Theme"])
                        showingNotice = true
                    }
                }
            }
    }





    fun markAcceptedSeen(jobId: String) {
        seenAcceptedIds = seenAcceptedIds + jobId
        unseenAcceptedIds = unseenAcceptedIds - setOf(jobId)
    }

    val onRated: (jobId: String, stars: Int, comment: String?) -> Unit =
        { jobId, stars, comment ->
            scope.launch {
                try {
                    sendingRating = true
                    sendRatingErr = null
                    withTimeout(6_000) { api.rateJob(jobId, RateReq(stars, comment)) }
                    ratedByMe = ratedByMe + jobId
                    promptedByMe = promptedByMe - jobId
                    rateDialog = null
                    ratingByUser = 0
                } catch (e: Exception) {
                    val msg = e.message ?: ""
                    val isAlready =
                        msg.contains("409") || msg.contains("já avaliado", ignoreCase = true)
                    if (isAlready) {
                        ratedByMe = ratedByMe + jobId
                        promptedByMe = promptedByMe - jobId
                        rateDialog = null
                        ratingByUser = 0
                    } else {
                        sendRatingErr = "Falha ao enviar avaliação. $msg"
                    }
                } finally {
                    sendingRating = false
                }
            }
        }

    // Hidrata snapshot
    LaunchedEffect(Unit) {
        session.getSubSnapshotOrNull()?.let { snap ->
            subStatus = SubStatusResp(
                verified = null,
                trialClaimed = snap.trialClaimed,
                trialEndsAt = snap.trialEndsAtIso,
                subActive = snap.subActive,
                canAccess = snap.canAccess,
                tipo = null
            )
            loadingSub = false
        }
    }
    // Busca status
    LaunchedEffect(Unit) {
        runCatching { api.mySubscription() }
            .onSuccess { sub ->
                subStatus = sub
                session.setSubscriptionSnapshot(
                    subActive = sub.subActive,
                    canAccess = sub.canAccess,
                    trialEndsAtIso = sub.trialEndsAt,
                    trialClaimed = sub.trialClaimed
                )
            }
            .onFailure { subErr = it.message }
        loadingSub = false
    }

    // Auto-restore
    var triedAutoRestore by remember { mutableStateOf(false) }
    LaunchedEffect(subStatus?.canAccess) {
        runCatching {
            if (subStatus?.canAccess != true && !triedAutoRestore) {
                triedAutoRestore = true
                restoreExistingSubscriptions(
                    context = ctx,
                    scope = scope,
                    onUiInfo = { },
                    onAnyUpdated = {
                        scope.launch {
                            runCatching { api.mySubscription() }
                                .onSuccess { sub ->
                                    subStatus = sub
                                    session.setSubscriptionSnapshot(
                                        subActive = sub.subActive,
                                        canAccess = sub.canAccess,
                                        trialEndsAtIso = sub.trialEndsAt,
                                        trialClaimed = sub.trialClaimed
                                    )
                                }
                        }
                    }
                )
            }
        }.onFailure { Log.e(TAG, "auto-restore fail", it) }
    }

    // Polling
    LaunchedEffect(selectedTab, autoRefresh, lifecycleOwner) {
        when (selectedTab) {
            BoxTab.INBOX -> refreshInbox()
            BoxTab.ARQUIVADOS -> refreshArchived()
        }
        inboxLoading = false; archivedLoading = false
        if (!autoRefresh) return@LaunchedEffect
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            while (isActive && autoRefresh) {
                if (!pausePolling) {
                    when (selectedTab) {
                        BoxTab.INBOX -> refreshInbox()
                        BoxTab.ARQUIVADOS -> refreshArchived()
                    }
                }
                delay(10_000)
            }
        }
    }
    LaunchedEffect(autoRefresh, lifecycleOwner) {
        if (!autoRefresh) return@LaunchedEffect
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            while (isActive && autoRefresh) {
                refreshJobsNow()
                delay(15_000)
            }
        }
    }

    // ================== UI ==================
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.fillMaxHeight()
            ) {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {

                    // 🔝 CONTEÚDO DO MENU
                    Column {
                        Spacer(Modifier.height(12.dp))

                        // 🏠 INICIO
                        NavigationDrawerItem(
                            label = { Text("Inicio") },
                            selected = selectedTab == BoxTab.INBOX,
                            icon = {
                                Icon(Icons.Outlined.Home, contentDescription = null)
                            },
                            onClick = {
                                selectedTab = BoxTab.INBOX
                                scope.launch { drawerState.close() }
                            }
                        )

                        Divider(Modifier.padding(vertical = 8.dp))


                        NavigationDrawerItem(
                            label = { Text("Configurar perfil") },
                            selected = false,
                            icon = {
                                Icon(Icons.Outlined.Settings, contentDescription = null)
                            },
                            onClick = onOpenSettings
                        )


                        NavigationDrawerItem(
                            label = { Text("Minha região") },
                            selected = false,
                            icon = {
                                Icon(Icons.Outlined.LocationOn, contentDescription = null)
                            },
                            onClick = {
                                tempUf = profile?.uf?.trim().orEmpty().uppercase()
                                tempCidade = profile?.cidade?.trim().orEmpty()
                                regionErr = null
                                showRegionDialog = true
                                scope.launch { drawerState.close() }
                            }
                        )


                        NavigationDrawerItem(
                            label = { Text("Excluir minha conta…") },
                            selected = false,
                            icon = {
                                Icon(Icons.Outlined.Delete, contentDescription = null)
                            },
                            onClick = {
                                showDeleteAcc = true
                                scope.launch { drawerState.close() }
                            }
                        )


                        NavigationDrawerItem(
                            label = { Text("Sugestões de melhorias") },
                            selected = false,
                            icon = {
                                Icon(Icons.Outlined.Lightbulb, contentDescription = null)
                            },
                            onClick = {
                                suggestionOpen = true
                                scope.launch { drawerState.close() }
                            }
                        )


                        NavigationDrawerItem(
                            label = { Text("Sair") },
                            selected = false,
                            icon = {
                                Icon(Icons.Outlined.ExitToApp, contentDescription = null)
                            },
                            onClick = {
                                scope.launch { drawerState.close() }
                                onLogout()
                            }
                        )


                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFDCFCE7)
                            ),
                            elevation = CardDefaults.cardElevation(2.dp),
                            onClick = {
                                removerAd = true
                                scope.launch { drawerState.close() }
                            }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {


                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(
                                            Color(0xFF16A34A),
                                            shape = RoundedCornerShape(10.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.WorkspacePremium,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                Spacer(Modifier.width(10.dp))


                                Text(
                                    text = "Remover anúncios",
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.weight(1f)
                                )


                                Text(
                                    text = "PRO",
                                    fontSize = 10.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .background(
                                            brush = Brush.horizontalGradient(
                                                listOf(
                                                    Color(0xFF16A34A),
                                                    Color(0xFF22C55E)
                                                )
                                            ),
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }


                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .padding(bottom = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp, vertical = 8.dp),
                            thickness = 0.5.dp,
                            color = Color.Gray.copy(alpha = 0.3f)
                        )

                        Text(
                            text = "from",
                            color = Color.DarkGray.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.labelSmall,
                            letterSpacing = 1.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Image(
                            painter = painterResource(id = R.drawable.logo_lester),
                            contentDescription = "Logo",
                            modifier = Modifier
                                .height(36.dp)
                                .alpha(0.9f)
                        )
                    }
                }
            }
        }
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbar) },
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    },
                    title = { AppTitlePillGradient(maxFontSize = 16.sp, minFontSize = 11.sp) },
                    actions = {
                        // Badge de mensagens -> abre/fecha o overlay (NOVIDADE)
                        BadgedBox(badge = { if (unreadCount > 0) Badge { Text("$unreadCount") } }) {
                            IconButton(onClick = { messagesSheetOpen = !messagesSheetOpen }) {
                                Icon(Icons.Filled.Chat, contentDescription = "Mensagens")
                            }
                        }

                        BadgedBox(badge = { if (unseenAcceptedIds.isNotEmpty()) Badge { Text("${unseenAcceptedIds.size}") } }) {
                            IconButton(
                                enabled = unseenAcceptedIds.isNotEmpty(),
                                onClick = {
                                    scope.launch {
                                        val mine =
                                            runCatching { api.listMyPatientJobs() }.getOrDefault(
                                                emptyList()
                                            )
                                        val first =
                                            mine.firstOrNull { it.id in unseenAcceptedIds }
                                        if (first != null) dialogJob = first
                                    }
                                }
                            ) {
                                Icon(
                                    Icons.Filled.Notifications,
                                    contentDescription = "Notificações"
                                )
                            }
                        }
                        // Compartilhar
                        IconButton(onClick = { shareApp(ctx) }) {
                            Icon(Icons.Filled.Share, contentDescription = "Compartilhar app")
                        }
                    }
                )
            }
        ) { pad ->
            Box(
                Modifier
                    .padding(pad)
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier

                ) {

                    val imagemHeader = when (profile?.genero) {
                        "F" -> R.drawable.girl_header
                        "M" -> R.drawable.boy_header
                        else -> null
                    }

                    val gradientColors = when (profile?.genero) {

                        "F" -> listOf(
                            Color(0xFFF3E8FF), // lilás claro
                            Color(0xFFE9D5FF)
                        )

                        "M" -> listOf(
                            Color(0xFFE0F2FE), // azul claro
                            Color(0xFFBAE6FD)
                        )

                        else -> listOf(
                            Color(0xFFF3F4F6),
                            Color(0xFFE5E7EB)
                        )
                    }
                    // ===== Header =====
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        colors = gradientColors
                                    )
                                )
                                .height(120.dp)
                        ) {

                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 16.dp),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "Olá, ${profile?.nome ?: ""} 👋",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(Modifier.height(4.dp))

                                    Text(
                                        text = "Encontre o profissional ideal\npara o que você precisa.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray
                                    )
                                }
                            }

                            imagemHeader?.let { img ->
                                Image(
                                    painter = painterResource(img),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .height(100.dp)
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(2.dp))





                    val profissoesUi = remember {
                        PROFISSOES_UI.sortedWith(
                            compareByDescending<ProfissaoUi> { it.popular }
                                .thenBy { it.label }
                        )
                    }


                    val lifecycle = lifecycleOwner.lifecycle

                    LaunchedEffect(lifecycleOwner, selectedTipo) {
                        snapshotFlow { lifecycle.currentState }
                            .collect { state ->
                                if (state == Lifecycle.State.RESUMED && selectedTipo != null) {

                                    val uf = (ufFiltro ?: profile?.uf)?.trim().orEmpty().uppercase()
                                    val cid = (cidadeFiltro ?: profile?.cidade)?.trim().orEmpty()

                                    if (uf.isBlank() || cid.isBlank()) return@collect

                                    loadingPros = true

                                    lista = runCatching {
                                        api.listPros(
                                            tipo = selectedTipo?.name,
                                            q = busca.takeIf { it.isNotBlank() },
                                            uf = uf,
                                            cidade = cid
                                        )
                                    }.getOrElse {
                                        erroPros = it.message
                                        emptyList()
                                    }

                                    loadingPros = false
                                }
                            }
                    }


                    var searching by remember { mutableStateOf(false) }

                    val profissoesFiltradas = remember(busca) {
                        if (busca.isBlank()) profissoesUi
                        else profissoesUi.filter {
                            it.label.contains(
                                busca,
                                ignoreCase = true
                            )
                        }
                    }

                    val showProfessionGrid = selectedTipo == null
                    val showAvailable = selectedTipo == null && busca.isBlank()

                    val populares = profissoesFiltradas.filter { it.popular }
                    val outras = profissoesFiltradas.filterNot { it.popular }

                    val gridState = rememberLazyGridState()
                    val infiniteTransition = rememberInfiniteTransition()
                    val rotation by infiniteTransition.animateFloat(
                        initialValue = 0f,
                        targetValue = 360f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(2000, easing = LinearEasing)
                        )
                    )

                    // Lista ordenada de profissionais
                    val sorted = remember(lista) {
                        lista.sortedWith(
                            compareByDescending<ProPublicDto> { it.ratingAvg ?: 0.0 }
                                .thenByDescending { it.ratingCount }
                        )
                    }
                    val disponiveis = sorted.filter { it.disponivel }
                    // =======================
                    // BACK HANDLER GLOBAL
                    // =======================
                    BackHandler(enabled = selectedTipo != null || searching || showAll) {
                        when {
                            showAll -> showAll = false
                            searching -> { searching = false; busca = "" }
                            selectedTipo != null -> selectedTipo = null
                        }
                    }




                    if (selectedTipo == null) {
                        Column {

                            Spacer(Modifier.height(1.dp))

                            if (showAll || searching) {
                                TextButton(
                                    onClick = {
                                        when {
                                            showAll -> showAll = false
                                            searching -> {
                                                searching = false
                                                busca = ""
                                            }
                                        }
                                    }
                                ) {
                                    Text("← Voltar")
                                }

                                Spacer(Modifier.height(8.dp))
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(4.dp, RoundedCornerShape(14.dp))
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color.White)
                                    .clickable { searching = true }
                                    .padding(horizontal = 16.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = null,
                                    tint = Color.Gray
                                )
                                Spacer(Modifier.width(8.dp))

                                if (!searching) {
                                    Text(
                                        "Buscar profissão (ex: pedreiro, diarista)",
                                        color = Color.Gray,
                                        modifier = Modifier.weight(1f)
                                    )
                                } else {
                                    TextField(
                                        value = busca,
                                        onValueChange = { busca = it },
                                        placeholder = {
                                            Text(
                                                "Digite uma profissão...",
                                                color = Color.Gray.copy(alpha = 0.7f)
                                            )
                                        },
                                        singleLine = true,
                                        colors = TextFieldDefaults.colors(
                                            focusedContainerColor = Color.Transparent,
                                            unfocusedContainerColor = Color.Transparent,
                                            focusedTextColor = Color.Black,
                                            unfocusedTextColor = Color.Black,
                                            cursorColor = Color.Black
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }



                            Spacer(Modifier.height(12.dp))

                            Text(
                                "   Populares",
                                style = MaterialTheme.typography.titleSmall
                            )

                            Spacer(Modifier.height(8.dp))

                            GridProfissoes(
                                items = if (showAll) profissoesFiltradas else populares,
                                showAll = true, //
                                onShowAll = {}, //
                                onSelect = {
                                    selectedTipo = it
                                    busca = ""
                                    lista = emptyList()
                                    erroPros = null
                                    loadingPros = true
                                }
                            )


                            Spacer(Modifier.height(8.dp))


                        }
                    }

                    Spacer(Modifier.height(4.dp))
                    var disponiveisAnimados by remember {
                        mutableStateOf(disponiveis)
                    }

                    LaunchedEffect(disponiveis) {
                        disponiveisAnimados = disponiveis.shuffled()
                    }

                    LaunchedEffect(disponiveisAnimados) {

                        while (true) {

                            delay(4000)

                            if (disponiveisAnimados.size > 1) {

                                disponiveisAnimados =
                                    disponiveisAnimados.drop(1) +
                                            disponiveisAnimados.first()
                            }
                        }
                    }

                    if (showAvailable && disponiveis.isNotEmpty())  {

                        Text(
                            "Profissionais disponíveis agora",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        Spacer(Modifier.height(8.dp))

                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(disponiveisAnimados, key = { it.userId }) { p ->

                                Card(
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier
                                        .animateItem()
                                        .width(100.dp)
                                        .clickable(enabled = p.tipo != null) {
                                            p.tipo?.let { tipo ->
                                                onOpenCuidador(
                                                    p.userId,
                                                    p.nome,
                                                    p.avatarUrl,
                                                    tipo
                                                )
                                            }
                                        },
                                    elevation = CardDefaults.cardElevation(3.dp)
                                ) {

                                    Column {


                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(110.dp)
                                                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                                        ) {

                                            ClickZoomImage(
                                                model = p.avatarUrl,
                                                sizeDp = 110.dp,
                                                modifier = Modifier.fillMaxSize(),
                                                shape = RectangleShape,
                                                placeholderRes = R.drawable.ic_avatar_placeholder
                                            )

                                            Box(
                                                modifier = Modifier
                                                    .align(Alignment.BottomStart)
                                                    .padding(6.dp)
                                                    .background(
                                                        Color.Black.copy(alpha = 0.6f),
                                                        RoundedCornerShape(8.dp)
                                                    )
                                                    .padding(horizontal = 6.dp, vertical = 3.dp)
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {

                                                    Icon(
                                                        imageVector = Icons.Default.Star,
                                                        contentDescription = null,
                                                        tint = Color(0xFFFFC107),
                                                        modifier = Modifier.size(12.dp)
                                                    )

                                                    Spacer(Modifier.width(3.dp))

                                                    Text(
                                                        text = p.ratingAvg?.let { "%.1f".format(it) } ?: "0.0",
                                                        color = Color.White,
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.SemiBold
                                                    )
                                                }
                                            }


                                            Box(
                                                modifier = Modifier
                                                    .align(Alignment.TopEnd)
                                                    .padding(2.dp)
                                                    .background(
                                                        (Color(0xFF22C55E).copy(alpha = 0.9f)),
                                                        RoundedCornerShape(6.dp)
                                                    )
                                                    .padding(horizontal = 3.dp, vertical = 1.dp)
                                            ) {
                                                Text(
                                                    "Disponível",
                                                    color = Color.White,
                                                    fontSize = 10.sp
                                                )
                                            }
                                        }


                                        Column(
                                            modifier = Modifier.padding(3.dp)
                                        ) {

                                            Text(
                                                p.nome,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.SemiBold
                                            )


                                            p.tipo?.let {
                                                Text(
                                                    it.label(),
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.primary
                                                )
                                            }



                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Default.LocationOn,
                                                    contentDescription = null,
                                                    tint = Color.Gray,
                                                    modifier = Modifier.size(14.dp)
                                                )

                                                Spacer(Modifier.width(2.dp))

                                                Text(

                                                    text = when {

                                                        p.distanciaKm != null ->
                                                            "%.1f km".format(p.distanciaKm)

                                                        !p.temLocalizacao ->
                                                            "Perto"

                                                        else -> "Perto"
                                                    },

                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = Color.Gray
                                                )


                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(16.dp))
                    }

                    if (selectedTipo == null) {
                        Text(
                            if (showAll) "  Todas as profissões" else "  Outras profissões",
                            style = MaterialTheme.typography.titleSmall
                        )

                        Spacer(Modifier.height(8.dp))

                        GridProfissoes(
                            items = if (showAll) outras else outras.take(7),
                            showAll = showAll,
                            onShowAll = { showAll = true },
                            onSelect = {
                                selectedTipo = it
                                busca = ""
                                lista = emptyList()
                                erroPros = null
                                loadingPros = true
                            }
                        )
                     }

                    Box(
                        Modifier
                            .fillMaxSize()
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                if (searching) {
                                    searching = false
                                    busca = ""
                                }
                            }
                    ) {







                        // Overlay de profissionais

                        androidx.compose.animation.AnimatedVisibility(
                            visible = selectedTipo != null,
                            enter = fadeIn() + slideInVertically { it },
                            exit = fadeOut() + slideOutVertically { it }
                        ) {
                            Box(Modifier.fillMaxSize()) {

                                Column {



                                    Row(
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        IconButton(onClick = { selectedTipo = null }) {
                                            Icon(
                                                Icons.Default.ArrowBack,
                                                contentDescription = "Voltar"
                                            )
                                        }
                                        Text(
                                            selectedTipo?.label() ?: "",
                                            style = MaterialTheme.typography.titleMedium,
                                            modifier = Modifier.padding(start = 8.dp)
                                        )
                                    }






                                    Spacer(Modifier.height(8.dp))

                                    LazyVerticalGrid(
                                        columns = GridCells.Fixed(3),
                                        state = gridState,
                                        contentPadding = PaddingValues(8.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        items(sorted, key = { it.userId }) { p ->

                                            val baseColor = badgeFromTipo(p.tipo)
                                            val cardBg = lightBackgroundFrom(baseColor)

                                            Card(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(190.dp)
                                                    .clickable(enabled = p.tipo != null) {
                                                        p.tipo?.let { tipo ->
                                                            onOpenCuidador(
                                                                p.userId,
                                                                p.nome,
                                                                p.avatarUrl,
                                                                tipo
                                                            )
                                                        }
                                                    },
                                                colors = CardDefaults.cardColors(
                                                    containerColor = cardBg
                                                )
                                            ) {
                                                Column(
                                                    Modifier
                                                        .fillMaxSize()
                                                        .padding(8.dp),
                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                    verticalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

                                                        val hasRecentPost =
                                                            p.lastPostAt != null

                                                        Box(
                                                            modifier = Modifier.size(56.dp),
                                                            contentAlignment = Alignment.Center
                                                        ) {

                                                            Box(
                                                                modifier = Modifier
                                                                    .matchParentSize()
                                                                    .rotate(if (hasRecentPost) rotation else 0f)
                                                                    .border(
                                                                        width = 3.dp,
                                                                        brush = if (hasRecentPost)
                                                                            Brush.sweepGradient(
                                                                                listOf(
                                                                                    Color(
                                                                                        0xFF4A00E0
                                                                                    ),
                                                                                    Color(
                                                                                        0xFF00C6FF
                                                                                    )
                                                                                )
                                                                            )
                                                                        else SolidColor(
                                                                            Color.Gray
                                                                        ),
                                                                        shape = CircleShape
                                                                    )
                                                            )

                                                            ClickZoomImage(
                                                                model = p.avatarUrl,
                                                                sizeDp = 50.dp,
                                                                shape = CircleShape,
                                                                placeholderRes = R.drawable.ic_avatar_placeholder
                                                            )
                                                        }

                                                        Spacer(Modifier.height(6.dp))

                                                        Text(
                                                            p.nome,
                                                            maxLines = 1,
                                                            overflow = TextOverflow.Ellipsis,
                                                            style = MaterialTheme.typography.bodyMedium,
                                                            fontWeight = FontWeight.SemiBold
                                                        )

                                                        Text(
                                                            "${p.cidadeExibida}/${p.uf}",
                                                            style = MaterialTheme.typography.bodySmall,
                                                            color = MaterialTheme.colorScheme.onSurface.copy(
                                                                alpha = .6f
                                                            ),
                                                            maxLines = 1,
                                                            overflow = TextOverflow.Ellipsis
                                                        )

                                                        Spacer(Modifier.height(6.dp))

                                                        RatingBarMercadoLivre(
                                                            rating = p.ratingAvg ?: 0.0,
                                                            count = p.ratingCount
                                                        )
                                                    }

                                                    RolePill(
                                                        p.tipo.label(),
                                                        bg = badgeFromTipo(p.tipo)
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    when {



                                        loadingPros -> Box(
                                            Modifier.fillMaxSize(),
                                            Alignment.Center
                                        ) {
                                            CircularProgressIndicator()
                                        }

                                        erroPros != null -> Box(
                                            Modifier.fillMaxSize(),
                                            Alignment.Center
                                        ) {
                                            Text("Erro: $erroPros", color = Color.Red)
                                        }

                                        sorted.isEmpty() && selectedTipo != null -> Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {

                                                Text(
                                                    text = "🤦‍♀️",
                                                    fontSize = 48.sp
                                                )

                                                Spacer(modifier = Modifier.height(8.dp))

                                                Text(
                                                    text = "Desculpe, ainda não existem profissionais cadastrados na sua região",
                                                    color = Color.Gray,
                                                    textAlign = TextAlign.Center
                                                )
                                            }
                                        }



                                    }
                                }
                            }
                        }
                    }

                }

                selectedStoryUserId?.let { openedUserId ->

                    val users = storiesState.map {
                        StoryUser(
                            userId = it.userId,
                            userName = it.userName,
                            avatarUrl = it.avatarUrl
                        )
                    }

                    val startIndex = users.indexOfFirst { it.userId == openedUserId }

                    if (startIndex >= 0) {
                        StoryViewer(
                            users = users,
                            startUserIndex = startIndex,
                            onToggleStarRemote = { api.toggleStar(it) },
                            onReportUser = { api.reportUser(it) },
                            onClose = {
                                storiesState = storiesState.map {
                                    if (it.userId == openedUserId)
                                        it.copy(hasUnseen = false)
                                    else it
                                }
                                selectedStoryUserId = null
                            }
                        )
                    }
                }



                if (messagesSheetOpen) {
                    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ModalBottomSheet(
                        sheetState = sheetState,
                        onDismissRequest = { messagesSheetOpen = false },
                        dragHandle = { Text("Mensagens", Modifier.padding(12.dp)) }
                    ) {
                        // Abas
                        Row(Modifier.padding(horizontal = 16.dp)) {
                            FilterChip(
                                selected = selectedTab == BoxTab.INBOX,
                                onClick = { selectedTab = BoxTab.INBOX },
                                label = { Text("Mensagens") }
                            )
                            Spacer(Modifier.width(8.dp))
                            FilterChip(
                                selected = selectedTab == BoxTab.ARQUIVADOS,
                                onClick = { selectedTab = BoxTab.ARQUIVADOS },
                                label = { Text("Arquivados") }
                            )
                        }
                        Spacer(Modifier.height(8.dp))

                        val showList = if (selectedTab == BoxTab.INBOX) inbox else archived
                        val isLoading =
                            if (selectedTab == BoxTab.INBOX) inboxLoading else archivedLoading

                        when {
                            isLoading -> Box(
                                Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }

                            showList.isEmpty() -> Text(
                                if (selectedTab == BoxTab.INBOX) "Sem mensagens ainda." else "Sem arquivados.",
                                Modifier.padding(16.dp)
                            )

                            else -> LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 520.dp) // limita altura do sheet
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                items(showList, key = { it.id }) { c ->
                                    var menuOpen by remember { mutableStateOf(false) }
                                    Row(
                                        Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                val stamp = c.lastAt ?: Instant.now().toString()
                                                ChatBadgeStore
                                                    .setLastRead(
                                                        ctx,
                                                        c.id,
                                                        stamp,
                                                        myUserId = myId
                                                    )
                                                chatUnseenIds = chatUnseenIds - c.id
                                                unreadCount = chatUnseenIds.size
                                                messagesSheetOpen =
                                                    false          // fecha ao abrir o chat
                                                onOpenChat(c.id)
                                            }
                                            .padding(vertical = 10.dp, horizontal = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        ClickZoomImage(
                                            model = bust(c.withAvatarUrl, c.lastAt),
                                            sizeDp = 48.dp,
                                            shape = CircleShape
                                        )
                                        Spacer(Modifier.width(10.dp))
                                        Column(Modifier.weight(1f)) {
                                            Text(c.withName, fontWeight = FontWeight.SemiBold)
                                            c.lastText?.let {
                                                Text(
                                                    it,
                                                    maxLines = 1,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurface.copy(
                                                        alpha = 0.6f
                                                    )
                                                )
                                            }
                                        }
                                        IconButton(onClick = {
                                            menuOpen = true
                                        }) {
                                            Icon(
                                                Icons.Filled.MoreVert,
                                                contentDescription = "Mais"
                                            )
                                        }
                                        DropdownMenu(
                                            expanded = menuOpen,
                                            onDismissRequest = { menuOpen = false }) {
                                            if (selectedTab == BoxTab.INBOX) {
                                                DropdownMenuItem(
                                                    text = { Text("Arquivar") },
                                                    onClick = {
                                                        menuOpen = false
                                                        scope.launch {
                                                            runCatching {
                                                                withTimeout(6_000) {
                                                                    api.archiveChat(
                                                                        c.id
                                                                    )
                                                                }
                                                            }
                                                            refreshInbox(); refreshArchived()
                                                        }
                                                    }
                                                )
                                            } else {
                                                DropdownMenuItem(
                                                    text = { Text("Desarquivar") },
                                                    onClick = {
                                                        menuOpen = false
                                                        scope.launch {
                                                            runCatching {
                                                                withTimeout(6_000) {
                                                                    api.unarchiveChat(
                                                                        c.id
                                                                    )
                                                                }
                                                            }
                                                            refreshInbox(); refreshArchived()
                                                        }
                                                    }
                                                )
                                            }
                                            Divider()
                                            DropdownMenuItem(
                                                enabled = true,
                                                text = { Text("Excluir conversa") },
                                                onClick = {
                                                    menuOpen = false
                                                    scope.launch {
                                                        deleteChat(c.id)
                                                        delay(150) // deixa o menu sair da árvore
                                                        refreshInbox()
                                                        refreshArchived()

                                                    }
                                                }
                                            )
                                            Divider()
                                            DropdownMenuItem(
                                                text = { Text("Reportar usuário") },
                                                onClick = { reportOpen = c.withUserId }
                                            )
                                        }
                                    }
                                    Divider()
                                }
                            }
                        }

                        // Diálogo de reporte (reuso)
                        reportOpen?.let { targetUserId ->
                            ReportUserDialog(
                                withName = inbox.firstOrNull { it.withUserId == targetUserId }?.withName,
                                onDismiss = { reportOpen = null },
                                onConfirm = { reason, details ->
                                    reportOpen = null
                                    scope.launch {
                                        try {
                                            api.reportUser(
                                                ReportReq(
                                                    reportedUserId = targetUserId,
                                                    reason = reason,
                                                    details = details
                                                )
                                            )
                                            snackbar.showSnackbar("Reporte enviado. Obrigado!")
                                        } catch (e: HttpException) {
                                            val code = e.code()
                                            val body = e.response()?.errorBody()?.string()
                                            snackbar.showSnackbar("Falha ($code): ${body ?: e.message()}")
                                        } catch (e: Exception) {
                                            snackbar.showSnackbar("Falha de rede: ${e.message ?: "erro desconhecido"}")
                                        }
                                    }
                                }
                            )
                        }

                        Spacer(Modifier.height(12.dp))
                    }
                }

                if (suggestionOpen) {
                    ReportUserDialog(
                        withName = null,
                        dialogTitle = "Enviar sugestão de melhoria",
                        forceDetailsMode = true,
                        onDismiss = { suggestionOpen = false },
                        onConfirm = { _reasonIgnored, details ->
                            suggestionOpen = false
                            scope.launch {
                                try {
                                    api.reportUser(
                                        ReportReq(
                                            reportedUserId = null,
                                            reason = "SUGESTAO",
                                            details = details ?: ""
                                        )
                                    )
                                    snackbar.showSnackbar("Sugestão enviada com sucesso. Obrigado!")
                                } catch (e: retrofit2.HttpException) {
                                    val code = e.code()
                                    val body = e.response()?.errorBody()?.string()
                                    snackbar.showSnackbar("Falha ($code): ${body ?: e.message()}")
                                } catch (e: Exception) {
                                    snackbar.showSnackbar("Falha de rede: ${e.message ?: "erro desconhecido"}")
                                }
                            }
                        }
                    )
                }

                // ====== Overlays/Diálogos ======
                dialogJob?.let { job ->
                    AlertDialog(
                        onDismissRequest = {
                            dialogJob = null
                            scope.launch {
                                markAcceptedHandled(ctx, job.id)
                            }
                        },
                        title = { Text("Pedido aceito") },
                        text = {
                            Text("Sua solicitação foi aceita por ${job.caregiverName}.")
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    dialogJob = null
                                    pausePolling = true

                                    scope.launch {
                                        val cid = api.startOrFindChat(job.caregiverId)
                                            ?: return@launch

                                        // MARCA COMO TRATADO (nunca mais aparece)
                                        markAcceptedHandled(ctx, job.id)

                                        onOpenChat(cid)
                                    }
                                }
                            ) {
                                Text("Abrir chat")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    dialogJob = null

                                    scope.launch {
                                        // Usuário escolheu não abrir agora,
                                        // mas o aviso NÃO deve reaparecer
                                        markAcceptedHandled(ctx, job.id)
                                    }
                                }
                            ) {
                                Text("Agora não")
                            }
                        }
                    )
                }





                rateDialog?.let { job ->
                    AlertDialog(
                        onDismissRequest = {
                            if (!sendingRating) {
                                rateDialog = null
                                ratingByUser = 0
                                ratingComment = ""
                                sendRatingErr = null
                            }
                        },
                        title = { Text("Avaliar profissional") },

                        text = {
                            Column {

                                Text("Como foi o atendimento de ${job.caregiverName}?")

                                Spacer(Modifier.height(8.dp))

                                Row {
                                    (1..5).forEach { star ->
                                        Icon(
                                            imageVector =
                                                if (star <= ratingByUser)
                                                    Icons.Filled.Star
                                                else
                                                    Icons.Filled.StarBorder,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(32.dp)
                                                .clickable { ratingByUser = star }
                                        )
                                    }
                                }

                                Spacer(Modifier.height(12.dp))

                                OutlinedTextField(
                                    value = ratingComment,
                                    onValueChange = { ratingComment = it },
                                    label = { Text("Comentário (opcional)") },
                                    modifier = Modifier.fillMaxWidth(),
                                    maxLines = 4
                                )

                                sendRatingErr?.let {
                                    Spacer(Modifier.height(6.dp))
                                    Text(it, color = MaterialTheme.colorScheme.error)
                                }
                            }
                        },

                        confirmButton = {
                            TextButton(
                                enabled = ratingByUser in 1..5 && !sendingRating,
                                onClick = {

                                    scope.launch {

                                        try {

                                            api.rateJob(
                                                job.id,
                                                RateReq(
                                                    stars = ratingByUser,
                                                    comment = ratingComment.ifBlank { null }
                                                )
                                            )

                                            ratedByMe = ratedByMe + job.id
                                            promptedByMe = promptedByMe + job.id

                                        } catch (_: Exception) {}

                                        rateDialog = null
                                        ratingByUser = 0
                                        ratingComment = ""
                                        sendRatingErr = null
                                    }
                                }
                            ) {
                                Text(if (sendingRating) "Enviando..." else "Enviar")
                            }
                        },

                        dismissButton = {
                            TextButton(
                                enabled = !sendingRating,
                                onClick = {

                                    scope.launch {

                                        try {
                                            api.skipRating(job.id)
                                        } catch (_: Exception) {}

                                        promptedByMe = promptedByMe + job.id

                                        rateDialog = null
                                        ratingByUser = 0
                                        ratingComment = ""
                                        sendRatingErr = null
                                    }
                                }
                            ) {
                                Text("Agora não")
                            }
                        }
                    )
                }

                val currentNotice = notice

                if (showingNotice && currentNotice != null) {
                    FullscreenNoticeCard(
                        message = currentNotice.message,
                        mediaUrl = currentNotice.mediaUrl,
                        mediaType = currentNotice.mediaType,
                        theme = currentNotice.theme,
                        onClose = {
                            scope.launch {
                                runCatching { api.dismissNotice(currentNotice.id) }
                                showingNotice = false
                            }
                        }
                    )
                }


            }
        }
    }

    //  Diálogo de Região
    if (showRegionDialog) {
        val canApply = tempUf.trim().uppercase().length == 2 && tempCidade.trim().length >= 2
        AlertDialog(
            onDismissRequest = { if (!savingRegion) showRegionDialog = false },
            title = { Text("Definir minha região") },
            text = {
                Column {
                    ExposedDropdownMenuBox(
                        expanded = ufMenuExpanded,
                        onExpandedChange = {
                            if (!savingRegion) ufMenuExpanded = !ufMenuExpanded
                        }
                    ) {
                        OutlinedTextField(
                            value = tempUf, onValueChange = {}, readOnly = true,
                            label = { Text("UF") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = ufMenuExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = ufMenuExpanded,
                            onDismissRequest = { ufMenuExpanded = false }) {
                            UFS.forEach { uf ->
                                DropdownMenuItem(
                                    text = { Text(uf) },
                                    onClick = { tempUf = uf; ufMenuExpanded = false })
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = tempCidade,
                        onValueChange = { if (!savingRegion) tempCidade = it },
                        label = { Text("Cidade") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        supportingText = {
                            regionErr?.let {
                                Text(
                                    it,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    enabled = canApply && !savingRegion,
                    onClick = {
                        val newUf = tempUf.trim().uppercase()
                        val newCid = tempCidade.trim()
                        profile = (profile?.copy(uf = newUf, cidade = newCid)) ?: profile
                        ufFiltro = newUf
                        cidadeFiltro = newCid
                        scope.launch {
                            try {
                                savingRegion = true
                                regionErr = null
                                val postJob = launch(Dispatchers.IO) {
                                    runCatching {
                                        api.updateMyRegion(
                                            UpdateRegionReq(
                                                newUf,
                                                newCid
                                            )
                                        )
                                    }
                                }
                                withTimeoutOrNull(6_000) { postJob.join(); true }
                                withTimeoutOrNull(4_000) {
                                    runCatching { api.getMyProfile() }.onSuccess {
                                        profile = it
                                    }
                                }
                                ctx.seenStore.edit {
                                    it[REGION_UF_KEY] = newUf
                                    it[REGION_CIDADE_KEY] = newCid
                                }
                                showRegionDialog = false
                            } catch (e: Exception) {
                                regionErr =
                                    "Falha ao atualizar região. ${e.message ?: ""}".trim()
                            } finally {
                                savingRegion = false
                            }
                        }
                    }
                ) { Text(if (savingRegion) "Salvando..." else "Aplicar") }
            },
            dismissButton = {
                TextButton(
                    enabled = !savingRegion,
                    onClick = { showRegionDialog = false }) { Text("Cancelar") }
            }
        )
    }

    //  Dialogo Excluir Conta
    DeleteAccountDialog(
        show = showDeleteAcc,
        onDismiss = { showDeleteAcc = false },
        onSuccess = {
            showDeleteAcc = false
            onLogout()
        }
    )

    if (showSheet) {
        ProfissaoBottomSheet(
            selected = selectedTipo,
            onSelect = { selectedTipo = it },
            onDismiss = { showSheet = false }
        )
    }

    // INTERSTITIAL
    if (showAd) {
        AdMobInterstitial(
            adUnitId = AdMobIds.INTERSTITIAL
        ) {
            showAd = false

            val shouldShowPaywall = adsManager.incrementAdsSeen()
            if (shouldShowPaywall && role == Role.CONTRATANTE) {
                showPaywall = true
            }
        }
    }


// ===== PAYWALL =====
    if (showPaywall) {
        LockedOverlay(
            role = role,
            loading = billingInProgress,
            enabled = !billingInProgress,
            snackbarHost = snackbar,
            onManageClick = {
                showPaywall = false
                onOpenBillingImpl()
            },
            onClose = if (role == Role.CONTRATANTE) {
                { showPaywall = false }
            } else null,
            onSubUpdated = { sub ->
                subStatus = sub
                session.setSubscriptionSnapshot(
                    subActive = sub.subActive,
                    canAccess = sub.canAccess,
                    trialEndsAtIso = sub.trialEndsAt,
                    trialClaimed = sub.trialClaimed
                )

                if (sub.canAccess) {
                    adsManager.reset()
                    showAd = false
                    showPaywall = false
                }
            }
        )
    }

    if (removerAd) {
        LockedOverlay(
            role = Role.CONTRATANTE,
            loading = billingInProgress,
            enabled = !billingInProgress,
            snackbarHost = snackbar,
            onManageClick = {
                removerAd = false
                onOpenBillingImpl()
            },
            onClose = {
                removerAd = false
            },
            onSubUpdated = { sub ->
                subStatus = sub
                session.setSubscriptionSnapshot(
                    subActive = sub.subActive,
                    canAccess = sub.canAccess,
                    trialEndsAtIso = sub.trialEndsAt,
                    trialClaimed = sub.trialClaimed
                )

                if (sub.canAccess) {
                    adsManager.reset()
                    showAd = false
                    showPaywall = false
                    removerAd = false
                }
            }
        )
    }





}