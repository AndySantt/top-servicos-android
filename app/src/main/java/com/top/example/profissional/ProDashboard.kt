@file:OptIn(ExperimentalMaterial3Api::class)

package com.top.example.profissional



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

import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp

import com.top.example.net.ApiService

import com.top.example.notify.Notifier

import com.top.example.net.MeProfileResp
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.CircularProgressIndicator
import coil.compose.AsyncImage
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import com.top.example.net.UpdateProfileReq
import com.top.example.net.ConversationSummary
import com.top.example.net.ProPublicDto

import com.top.example.net.JobPublicDto
import androidx.compose.material.icons.Icons

import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.getValue

import androidx.compose.runtime.mutableStateOf
import com.top.example.net.*
import kotlinx.coroutines.delay

import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import java.time.format.DateTimeFormatter
import androidx.compose.runtime.saveable.rememberSaveable

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.isActive
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.material.icons.filled.MoreVert
import java.time.Instant
import java.time.ZoneId
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.alpha
import android.location.Location
import coil.request.ImageRequest

import androidx.compose.material.icons.filled.Close

import kotlinx.coroutines.withTimeout

import androidx.compose.ui.graphics.graphicsLayer

import androidx.compose.material.icons.filled.Info

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
import androidx.compose.ui.zIndex
import androidx.compose.material.icons.filled.Edit
import com.top.example.util.withBust
import com.top.example.util.toAbsolute
import com.top.example.network.RetrofitProvider

import com.top.example.push.PushCache

import com.top.example.chatbadge.ChatBadgeStore

import androidx.lifecycle.*
import kotlinx.coroutines.*

import com.top.example.billing.restoreExistingSubscriptions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip

import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.rememberCoroutineScope

import com.top.example.util.shareApp
import androidx.compose.material.icons.filled.Share
import com.top.example.util.ReportUserDialog

import androidx.compose.animation.core.*
import androidx.compose.foundation.*

import com.top.example.settings.DeleteAccountDialog
import java.time.*

import java.time.ZonedDateTime

import androidx.compose.material3.Divider

import androidx.compose.ui.unit.sp
import com.top.example.feed.Post
import com.top.example.feed.togglePostStar
import com.top.example.feed.addPost
import com.top.example.feed.toDomain

import androidx.compose.foundation.layout.width

import androidx.compose.material.icons.outlined.People

import com.top.example.bus.AppBus
import com.top.example.bus.ChatEvent
import com.top.example.chatbadge.unseenChatIds
import retrofit2.HttpException

import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*

import android.widget.Toast

import com.top.example.ui.MaybeAskForReview

import androidx.compose.animation.*
import androidx.compose.ui.graphics.Color

import com.top.example.util.bust
import com.top.example.SessionManager
import com.top.example.R
import com.top.example.RatingBarMercadoLivre
import com.top.example.BoxTab
import com.top.example.FeedSection
import com.top.example.LockedOverlay
import com.top.example.FullscreenNoticeCard
import com.top.example.ClickZoomImage
import com.top.example.FollowersDialog
import com.top.example.roleLabelFromTipo
import com.top.example.AppTitlePillGradient
import com.top.example.parseNoticeThemeHeader
import com.top.example.rememberOnOpenBilling
import com.top.example.fmtDia
import com.top.example.rememberLocationPicker
import com.top.example.zone
import  com.top.example.util.getUserLocation
import android.util.Log

private enum class ProMenu { DASHBOARD, HISTORICO, ARQUIVADOS }







@Composable
private fun ConteudoHistorico(
    pad: PaddingValues,
    lista: List<JobPublicDto>,
    onClear: () -> Unit
) {
    val top30 = remember(lista) { lista.take(30) }

    Column(
        Modifier
            .padding(pad)
            .padding(16.dp)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Histórico de serviços", style = MaterialTheme.typography.titleMedium)
            OutlinedButton(onClick = onClear) { Text("Limpar histórico") }
        }
        Spacer(Modifier.height(8.dp))

        if (top30.isEmpty()) {
            Text("Nenhum serviço no histórico.")
            return@Column
        }

        LazyColumn {
            items(top30, key = { it.id }) { j ->
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text("Paciente: ${j.pacienteNome}", fontWeight = FontWeight.Bold)
                    j.idade?.let { Text("Idade: $it") }
                    j.endereco?.let { Text("Endereço: $it") }
                    j.observ?.let { Text("Obs: $it") }
                    Text("Criado em: ${j.createdAt}")
                    Text(
                        "Status: ${j.status}",
                        color = when (j.status) {
                            "FINALIZADO" -> Color(0xFF16A34A)
                            "RECUSADO", "CANCELADO" -> Color(0xFFEF4444)
                            else -> Color.Unspecified
                        }
                    )
                }
                Divider()
            }
        }
    }
}





@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContratanteProfileSheet(
    open: Boolean,
    onClose: () -> Unit,
    user: PublicUserDto?
) {
    if (!open) return
    ModalBottomSheet(onDismissRequest = onClose) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ClickZoomImage(
                    model = user?.avatarUrl,
                    sizeDp = 64.dp,
                    shape = CircleShape,
                    placeholderRes = R.drawable.ic_avatar_placeholder
                )
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(user?.nome ?: "Contratante", style = MaterialTheme.typography.titleMedium)
                    val loc = listOfNotNull(
                        user?.cidade?.takeIf { !it.isNullOrBlank() },
                        user?.uf?.takeIf { !it.isNullOrBlank() })
                        .joinToString("/")
                        .ifBlank { null }
                    loc?.let {
                        Text(
                            it,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            if (!user?.bio.isNullOrBlank()) {
                Text(user!!.bio!!, style = MaterialTheme.typography.bodyMedium)
            } else {
                Text(
                    "Sem biografia.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}








@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProDashboardScreen(
    onOpenSettings: () -> Unit,
    onLogout: () -> Unit,
    onOpenChat: (String) -> Unit,
    onOpenBilling: () -> Unit = {},
    onDeleteAccount: (password: String) -> Unit = {},
    autoRefresh: Boolean = true,
    onToggleStarRemote: suspend (String) -> Unit,
    onOpenCuidador: (userId: String, nome: String?, avatarUrl: String?) -> Unit,


    ) {
    val TAG = "ProDashboard"
    val ctx = LocalContext.current
    val activity = ctx as Activity



    val (pedirLocalizacao, detalheLocalState) = rememberLocationPicker(activity)
    val detalheLocal = detalheLocalState.value

    activity?.let { MaybeAskForReview(it, minOpens = 7, minActions = 3) }

    val api = ApiProvider.get(ctx)
    val scope = rememberCoroutineScope()


    //  assinatura
    val session = remember(ctx) { SessionManager(ctx) }
    var subStatus by remember { mutableStateOf<SubStatusResp?>(null) }
    var loadingSub by remember { mutableStateOf(true) }
    var subErr by remember { mutableStateOf<String?>(null) }
    var billingInProgress by remember { mutableStateOf(false) }
    val snackbar = remember { SnackbarHostState() }
    val locked by remember { derivedStateOf { subStatus?.canAccess == false } }

    // ===== perfil / UI =====
    var profile by remember { mutableStateOf<MeProfileResp?>(null) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    var selected by remember { mutableStateOf(ProMenu.DASHBOARD) }

    var avatarStamp by remember { mutableStateOf(0L) }
    var uploadingAvatar by remember { mutableStateOf(false) }
    var avatarErr by remember { mutableStateOf<String?>(null) }

    var notice by remember { mutableStateOf<NoticeDto?>(null) }
    var noticeTheme by remember { mutableStateOf<NoticeTheme?>(null) }
    var showingNotice by remember { mutableStateOf(false) }

    val lifecycleOwner = LocalLifecycleOwner.current

    //  chats / jobs
    var inbox by remember { mutableStateOf<List<ConversationSummary>>(emptyList()) }
    var inboxLoading by remember { mutableStateOf(true) }
    var archived by remember { mutableStateOf<List<ConversationSummary>>(emptyList()) }
    var archivedLoading by remember { mutableStateOf(true) }
    var jobs by remember { mutableStateOf<List<JobPublicDto>>(emptyList()) }
    var jobsLoading by remember { mutableStateOf(true) }

    var unseenJobIds by remember { mutableStateOf<Set<String>>(emptySet()) }
    var notifOpen by remember { mutableStateOf(false) }
    var jobDialog by remember { mutableStateOf<JobPublicDto?>(null) }
    var jobActionLoading by remember { mutableStateOf(false) }
    var jobActionError by remember { mutableStateOf<String?>(null) }

    var showDailyDialog by remember { mutableStateOf(false) }
    var dailyInput by remember { mutableStateOf("") }
    var savingDaily by remember { mutableStateOf(false) }
    var saveDailyErr by remember { mutableStateOf<String?>(null) }

    var confirmClear by remember { mutableStateOf(false) }
    var clearingHistory by remember { mutableStateOf(false) }
    var clearErr by remember { mutableStateOf<String?>(null) }

    var showRegionDialog by remember { mutableStateOf(false) }
    var tempUf by remember { mutableStateOf("") }
    var tempCidade by remember { mutableStateOf("") }
    var ufMenuExpanded by remember { mutableStateOf(false) }
    var savingRegion by remember { mutableStateOf(false) }
    var regionErr by remember { mutableStateOf<String?>(null) }

    val UFS = remember { BrazilRegion.UFS }

    var unreadCount by remember { mutableStateOf(0) }
    var chatUnseenIds by remember { mutableStateOf<Set<String>>(emptySet()) }
    val myId = remember(ctx) { SessionManager(ctx).getUserId().orEmpty() }
    var prevPending by remember { mutableStateOf<Set<String>>(emptySet()) }

    var triedRestore by remember { mutableStateOf(false) }
    var reportOpen by remember { mutableStateOf<String?>(null) }

    // Exclusão de conta (UI)
    var showDeleteAcc by remember { mutableStateOf(false) }
    var deletePwd by remember { mutableStateOf("") }
    var deletingAcc by remember { mutableStateOf(false) }
    var deleteErr by remember { mutableStateOf<String?>(null) }

    val publicCache = remember { mutableStateMapOf<String, PublicUserDto>() }

    // controle do sheet de perfil público
    var profileOpen by remember { mutableStateOf<PublicUserDto?>(null) }
    var posts by remember { mutableStateOf<List<Post>>(emptyList()) }
    var loadingFeed by remember { mutableStateOf(true) }
    var feedErr by remember { mutableStateOf<String?>(null) }

    var previewImg by remember { mutableStateOf<String?>(null) }
    var previewCaption by remember { mutableStateOf<String?>(null) }
    var previewStarred by remember { mutableStateOf(false) }


    var lista by remember { mutableStateOf<List<ProPublicDto>>(emptyList()) }
    var loadingPros by remember { mutableStateOf(false) }
    var erroPros by remember { mutableStateOf<String?>(null) }
    var showOthersModal by remember { mutableStateOf(false) }
    var ufFiltro by remember { mutableStateOf<String?>(null) }
    var cidadeFiltro by remember { mutableStateOf<String?>(null) }
    var busca by rememberSaveable { mutableStateOf("") }
    val role = remember { session.getRoleOrDefault() }
    var suggestionOpen by remember { mutableStateOf(false) }

    var showOtherPros by remember { mutableStateOf(false) }
    var isDisponivel by rememberSaveable { mutableStateOf(false) }
    var loadingDisponivel by remember { mutableStateOf(false) }


    val myName = profile?.nome
    val myAvatar = profile?.avatarUrl


    var cidadeInput by remember { mutableStateOf("") }
    var tempRegions by remember {
        mutableStateOf<List<Pair<String, String>>>(emptyList())
    }


    val populares = remember {
        listOf(
            TipoPro.DIARISTA,
            TipoPro.REFRIGERISTA,
            TipoPro.ENCANADOR,
            TipoPro.BARBEIRO
        )
    }

    val outras = remember {
        TipoPro.values().toList() - populares.toSet()
    }


    val ibgeApi = remember { IbgeService.create() }
    var listaCidades by remember { mutableStateOf<List<CidadeIbge>>(emptyList()) }
    var cidadeMenuExpanded by remember { mutableStateOf(false) }



    // util de datas
    fun formatOnlyDate(iso: String?): String? = runCatching {
        if (iso.isNullOrBlank()) return null
        val zdt = ZonedDateTime.ofInstant(Instant.parse(iso), zone)
        zdt.format(fmtDia)
    }.getOrNull()

    fun formatDateAndTime(iso: String): String? {
        return try {
            val instant = Instant.parse(iso)

            val local = instant.atZone(ZoneId.systemDefault())

            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

            local.format(formatter)
        } catch (e: Exception) {
            null
        }
    }

    LaunchedEffect(detalheLocal) {
        if (detalheLocal == null) {
            pedirLocalizacao()
        }
    }

    // ====== NOTICE ======
    LaunchedEffect(Unit) {
        runCatching { api.getMyNoticeResponse() }
            .onSuccess { resp ->
                if (resp.isSuccessful) {
                    resp.body()?.let { body ->
                        notice = body
                        noticeTheme = parseNoticeThemeHeader(resp.headers()["X-Notice-Theme"])
                            ?: NoticeTheme.AZUL_ESCURO
                        showingNotice = true
                    }
                }
            }
    }

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

    val computeUnseen: (List<ConversationSummary>) -> Set<String> = remember(myId) {
        { rows ->
            unseenChatIds(
                ctx = ctx,
                items = rows,
                id = { it.id },
                lastAt = { it.lastAt },
                lastFrom = { lastFromCompat(it as Any, myId) },
                myId = myId.takeIf { it.isNotBlank() }
            )
        }
    }

    // Billing callback
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

    // Avatar picker
    val pickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
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
                profile = profile?.copy(avatarUrl = resp.avatarUrl)
                avatarStamp = System.currentTimeMillis()
                snackbar.showSnackbar("Foto de perfil atualizada")
            } catch (t: Throwable) {
                avatarErr = t.message ?: "Falha ao enviar imagem"
                snackbar.showSnackbar("Erro ao enviar avatar")
            } finally {
                uploadingAvatar = false
            }
        }
    }

    suspend fun refreshProfileOnly(api: ApiService, onSet: (MeProfileResp) -> Unit) {
        runCatching { api.getMyProfile() }.onSuccess(onSet)
    }








    suspend fun refreshChats() {
        try {
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
            runCatching { withContext(Dispatchers.IO) { api.myArchivedChats() } }
                .onSuccess { list ->
                    withContext(Dispatchers.Main) {
                        archived = list; archivedLoading = false
                    }
                }
        } catch (_: Throwable) {
            withContext(Dispatchers.Main) { inboxLoading = false }
        }
    }

    PushRefreshEffect(
        onChat = { scope.launch { refreshChats() } },
        onJobs = { scope.launch { refreshChats() } },
        onAny = { scope.launch { refreshChats() } }
    )

    suspend fun refreshJobs() {
        runCatching { api.listMyJobs() }.onSuccess {
            jobs = it
            val pendingNow = it.filter { j -> j.status == "PENDENTE" }.map { j -> j.id }.toSet()
            unseenJobIds = (unseenJobIds + pendingNow).intersect(pendingNow)
            jobsLoading = false
        }
    }


    LaunchedEffect(Unit) {
        while (isActive) {
            refreshJobs()
            delay(5000)
        }
    }



    LaunchedEffect(Unit) {
        AppBus.events.collect { ev ->
            if (ev is ChatEvent.Incoming) {
                scope.launch { refreshChats() }
            }
        }
    }

    PushRefreshEffect(
        onChat = { _ -> scope.launch { refreshChats() } },
        onJobs = { scope.launch { refreshJobs() } },
        onSubs = {
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
        },
        onAny = { }
    )




    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            val new = runCatching { api.myChats() }.getOrDefault(emptyList())
            val unseen = computeUnseen(new)
            val pending = PushCache.getChat(ctx)
            inbox = new
            chatUnseenIds = unseen
            unreadCount = unseen.size + pending
            if (pending > 0) PushCache.clearChat(ctx)
        }
    }

    LaunchedEffect(profile, busca, ufFiltro, cidadeFiltro) {

        if (profile == null) return@LaunchedEffect
        val uf = (ufFiltro ?: profile!!.uf)?.trim().orEmpty().uppercase()
        val cid = (cidadeFiltro ?: profile!!.cidade)?.trim().orEmpty()
        if (uf.isBlank() || cid.isBlank()) {
            lista = emptyList()
            return@LaunchedEffect
        }

        loadingPros = true
        erroPros = null

        runCatching {
            api.listPros(
                q = busca.takeIf { it.isNotBlank() },
                uf = uf,
                cidade = cid
            )
        }.onSuccess { rows ->
            lista = rows
        }.onFailure { e ->
            erroPros = e.message ?: "Falha ao carregar profissionais"
            lista = emptyList()
        }

        loadingPros = false
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
        if (subStatus?.canAccess != true && !triedAutoRestore) {
            triedAutoRestore = true
            restoreExistingSubscriptions(
                context = ctx,
                scope = scope,
                onUiInfo = { /* opcional */ },
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
    }

    // Polling assinatura
    LaunchedEffect(autoRefresh, lifecycleOwner) {
        if (!autoRefresh) return@LaunchedEffect
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            var backoffMs = 15_000L
            val minMs = 8_000L
            val maxMs = 60_000L
            while (isActive && autoRefresh) {
                val ok = withContext(Dispatchers.IO) {
                    runCatching { withTimeout(4_000) { api.mySubscription() } }
                        .onSuccess { sub ->
                            subStatus = sub
                            session.setSubscriptionSnapshot(
                                subActive = sub.subActive,
                                canAccess = sub.canAccess,
                                trialEndsAtIso = sub.trialEndsAt,
                                trialClaimed = sub.trialClaimed
                            )
                        }
                        .isSuccess
                }
                backoffMs = if (ok) 15_000L else (backoffMs * 2).coerceIn(minMs, maxMs)
                delay(backoffMs)
            }
        }
    }





    // Carregamentos iniciais
    LaunchedEffect(Unit) {
        runCatching { api.myChats() }.onSuccess { new ->
            inbox = new; inboxLoading = false
            val unseen = computeUnseen(new)
            val pending = PushCache.getChat(ctx)
            chatUnseenIds = unseen
            unreadCount = unseen.size + pending
            if (pending > 0) PushCache.clearChat(ctx)
        }.onFailure { inboxLoading = false }

        runCatching { api.myArchivedChats() }
            .onSuccess { archived = it; archivedLoading = false }
            .onFailure { archivedLoading = false }
    }

    LaunchedEffect(Unit) {
        runCatching {
            api.listMyPosts() // chama /me/posts ou /feed/my-posts (o que você escolher)
        }.onSuccess { list ->
            posts = list.map { it.toDomain() }
        }.onFailure { e ->
            feedErr = e.message
        }
        loadingFeed = false
    }

    LaunchedEffect(Unit) { refreshProfileOnly(api) { profile = it } }
    LaunchedEffect(autoRefresh) {
        refreshProfileOnly(api) { profile = it }
        runCatching { api.myChats() }.onSuccess { new ->
            inbox = new; inboxLoading = false
            scope.launch {
                val unseen = computeUnseen(new)
                val pending = PushCache.getChat(ctx)
                chatUnseenIds = unseen
                unreadCount = unseen.size + pending
                if (pending > 0) PushCache.clearChat(ctx)
            }
        }
        runCatching { api.myArchivedChats() }.onSuccess {
            archived = it; archivedLoading = false
        }
        val first = runCatching { api.listMyJobs() }.getOrDefault(emptyList())
        jobs = first; jobsLoading = false
        unseenJobIds = first.filter { it.status == "PENDENTE" }.map { it.id }.toSet()
        prevPending = unseenJobIds

        if (!autoRefresh) return@LaunchedEffect
        while (isActive && autoRefresh) {
            refreshProfileOnly(api) { profile = it }
            runCatching { api.myChats() }.onSuccess { new ->
                scope.launch {
                    val unseen = computeUnseen(new)
                    val pending = PushCache.getChat(ctx)
                    inbox = new
                    chatUnseenIds = unseen
                    unreadCount = unseen.size + pending
                    if (pending > 0) PushCache.clearChat(ctx)
                }
            }
            runCatching { api.listMyJobs() }.onSuccess { jobs = it }
            delay(10_000)
        }
    }

    LaunchedEffect(jobs) {
        val pending = jobs.filter { it.status == "PENDENTE" }.map { it.id }.toSet()
        val newOnes = pending - prevPending
        if (newOnes.isNotEmpty()) {
            Notifier.newJob(ctx, newOnes.size)
        }
        prevPending = pending
    }

    val ativos =
        remember(jobs) { jobs.filter { it.status == "PENDENTE" || it.status == "ACEITO" } }
    val historico = remember(jobs) {
        jobs.filter { it.status in listOf("FINALIZADO", "RECUSADO", "CANCELADO") }
            .sortedByDescending { it.createdAt }
    }

    // ===================== SOMENTE ABRE MENSAGENS PELO ÍCONE =====================
    var messagesSheetOpen by rememberSaveable { mutableStateOf(false) }
    var messagesTab by rememberSaveable { mutableStateOf(BoxTab.INBOX) }

    // helpers para arquivar/desarquivar/excluir dentro do sheet
    val archiveChat: suspend (String) -> Unit =
        { id -> withTimeout(6_000) { api.archiveChat(id) } }
    val unarchiveChat: suspend (String) -> Unit =
        { id -> withTimeout(6_000) { api.unarchiveChat(id) } }
    val deleteChat: suspend (String) -> Unit =
        { id -> withTimeout(6_000) { api.deleteChat(id) } }

    // ============================================================================

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

                    Column {
                        Spacer(Modifier.height(12.dp))

                        NavigationDrawerItem(
                            label = { Text("Inicio") },
                            selected = selected == ProMenu.DASHBOARD,
                            icon = {
                                Icon(
                                    imageVector = if (selected == ProMenu.DASHBOARD)
                                        Icons.Filled.Home
                                    else
                                        Icons.Outlined.Home,
                                    contentDescription = null
                                )
                            },
                            onClick = {
                                selected = ProMenu.DASHBOARD
                                scope.launch { drawerState.close() }
                            }
                        )



                        NavigationDrawerItem(
                            label = { Text("Histórico de serviços") },
                            selected = selected == ProMenu.HISTORICO,
                            icon = {
                                Icon(
                                    imageVector = if (selected == ProMenu.HISTORICO)
                                        Icons.Filled.History
                                    else
                                        Icons.Outlined.History,
                                    contentDescription = null
                                )
                            },
                            onClick = {
                                selected = ProMenu.HISTORICO
                                scope.launch { drawerState.close() }
                            }
                        )



                        NavigationDrawerItem(
                            label = { Text("Chats arquivados") },
                            selected = false,
                            icon = {
                                Icon(Icons.Outlined.Archive, contentDescription = null)
                            },
                            onClick = {
                                messagesTab = BoxTab.ARQUIVADOS
                                messagesSheetOpen = true
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
                            onClick = onLogout
                        )

                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .padding(bottom = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
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
                                .height(36.dp) // levemente menor = mais elegante
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
                    title = { AppTitlePillGradient(maxFontSize = 16.sp, minFontSize = 11.sp) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        // Badge de chats -> abre/fecha o ModalBottomSheet
                        BadgedBox(badge = { if (unreadCount > 0) Badge { Text("$unreadCount") } }) {
                            IconButton(onClick = { messagesSheetOpen = !messagesSheetOpen }) {
                                Icon(Icons.Filled.Chat, contentDescription = "Mensagens")
                            }
                        }
                        Spacer(Modifier.width(4.dp))





                        val unseenJobs =
                            jobs.filter { it.id in unseenJobIds && it.status == "PENDENTE" }

                        val notifCount = unseenJobs.size







                        BadgedBox(
                            badge = {
                                if (notifCount > 0) {
                                    Badge { Text("$notifCount") }
                                }
                            }
                        ) {
                            IconButton(onClick = { notifOpen = !notifOpen }) {
                                Icon(
                                    Icons.Filled.Notifications,
                                    contentDescription = "Notificações"
                                )
                            }
                        }

                        IconButton(onClick = { shareApp(ctx) }) {
                            Icon(Icons.Filled.Share, contentDescription = "Compartilhar app")
                        }

                        DropdownMenu(
                            expanded = notifOpen,
                            onDismissRequest = { notifOpen = false }
                        ) {



                            // ======= SERVIÇOS =======
                            if (unseenJobs.isNotEmpty()) {
                                Text(
                                    "Serviços",
                                    modifier = Modifier.padding(
                                        horizontal = 12.dp,
                                        vertical = 6.dp
                                    ),
                                    style = MaterialTheme.typography.labelMedium
                                )

                                unseenJobs.forEach { j ->
                                    DropdownMenuItem(
                                        text = {
                                            Column {
                                                Text(
                                                    "Novo pedido de ${j.pacienteNome}",
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                                Text(
                                                    "Criado em: ${formatOnlyDate(j.createdAt) ?: j.createdAt}",
                                                    style = MaterialTheme.typography.bodySmall
                                                )
                                                j.scheduledAt?.let { iso ->
                                                    formatDateAndTime(iso)?.let { bonitinho ->
                                                        Text(
                                                            "Agendado para: $bonitinho",
                                                            style = MaterialTheme.typography.bodySmall
                                                        )
                                                    }
                                                }
                                            }
                                        },
                                        onClick = {
                                            notifOpen = false
                                            jobDialog = j
                                        }
                                    )
                                }

                                Divider()

                                DropdownMenuItem(
                                    text = { Text("Marcar serviços como vistos") },
                                    onClick = {
                                        unseenJobIds =
                                            unseenJobIds - unseenJobs.map { it.id }.toSet()
                                        notifOpen = false
                                    }
                                )
                            }



                        }
                    })
            }
        ) { pad ->
            Box(Modifier.fillMaxSize()) {
                Column(
                    Modifier
                        .padding(pad)
                        .padding(16.dp)
                        .then(if (locked) Modifier.graphicsLayer(alpha = 0.55f) else Modifier)
                ) {
                    if (locked && !loadingSub) {
                        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Filled.Info, contentDescription = null)
                                Spacer(Modifier.width(8.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(
                                        "assinatura necessaria",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                                Spacer(Modifier.width(8.dp))
                                Button(
                                    onClick = onOpenBillingImpl,
                                    enabled = !billingInProgress
                                ) {
                                    if (billingInProgress) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(18.dp),
                                            strokeWidth = 2.dp
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Text("Abrindo…")
                                    } else {
                                        Text("Renovar")
                                    }
                                }
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                    }

                    Card(
                        shape = RoundedCornerShape(24.dp),
                        elevation = CardDefaults.cardElevation(6.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(16.dp)) {

                            // ===== HEADER =====
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                val ctx = LocalContext.current
                                val api = ApiProvider.get(ctx)

                                // estado do diálogo e do contador
                                var followersCount by remember { mutableStateOf<Int?>(null) }
                                var followersOpen by remember { mutableStateOf(false) }

                                // carrega contadores quando tiver o id
                                LaunchedEffect(profile?.id) {
                                    val uid = profile?.id
                                    if (!uid.isNullOrBlank()) {
                                        runCatching { api.getUserCounters(uid) }
                                            .onSuccess { followersCount = it.followers }
                                            .onFailure { /* opcional: log/ignore */ }
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .size(56.dp)
                                        .clip(CircleShape)
                                        .clickable(enabled = !uploadingAvatar) { pickImage.launch("image/*") }
                                ) {
                                    val absoluteBusted = remember(profile?.avatarUrl, avatarStamp) {
                                        profile?.avatarUrl
                                            .toAbsolute(RetrofitProvider.baseUrl)
                                            ?.withBust(avatarStamp)
                                    }
                                    key(avatarStamp) {
                                        AsyncImage(
                                            model = ImageRequest.Builder(ctx).data(absoluteBusted)
                                                .crossfade(true).build(),
                                            contentDescription = null,
                                            modifier = Modifier.matchParentSize(),
                                            contentScale = ContentScale.Crop,
                                            placeholder = painterResource(R.drawable.ic_avatar_placeholder),
                                            error = painterResource(R.drawable.ic_avatar_placeholder),
                                            fallback = painterResource(R.drawable.ic_avatar_placeholder)
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.BottomEnd)
                                            .zIndex(1f)
                                            .size(18.dp)
                                            .background(
                                                MaterialTheme.colorScheme.primary,
                                                CircleShape
                                            )
                                            .padding(2.dp)
                                    ) {
                                        Icon(
                                            Icons.Filled.Edit,
                                            contentDescription = "Trocar foto",
                                            tint = MaterialTheme.colorScheme.onPrimary,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                    if (uploadingAvatar) {
                                        Box(
                                            Modifier
                                                .zIndex(2f)
                                                .matchParentSize()
                                                .background(
                                                    MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                                                    CircleShape
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator(
                                                strokeWidth = 2.dp,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(Modifier.width(12.dp))

                                Column {
                                    Text(
                                        profile?.nome ?: "Profissional",
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    val currentProfile = profile
                                    if (currentProfile != null && !currentProfile.id.isNullOrBlank()) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            currentProfile.tipo?.let { tipo ->
                                                AssistChip(
                                                    onClick = { /* ação se precisar */ },
                                                    label = { Text(roleLabelFromTipo(tipo)) }
                                                )
                                            }
                                            Spacer(Modifier.width(8.dp))
                                            // aqui ficava o antigo botão de seguidores (mantive caso queira)
                                        }

                                        profile?.bio?.takeIf { it.isNotBlank() }?.let {
                                            Spacer(Modifier.height(4.dp))
                                            Text(
                                                it,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurface.copy(
                                                    alpha = 0.6f
                                                )
                                            )
                                        }

                                        Spacer(Modifier.height(4.dp))

                                        // NOVA LINHA: seguidores à esquerda, rating à direita com contador visível
                                        val avg = currentProfile.ratingAvg ?: 0.0
                                        val cnt = currentProfile.ratingCount ?: 0

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            // botão seguidores (esquerda)
                                            if (followersCount != null) {
                                                Surface(
                                                    tonalElevation = 2.dp,
                                                    shape = RoundedCornerShape(16.dp),
                                                    modifier = Modifier.clickable {
                                                        followersOpen = true
                                                    }
                                                ) {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        modifier = Modifier
                                                            .padding(
                                                                horizontal = 10.dp,
                                                                vertical = 6.dp
                                                            )
                                                            .sizeIn(
                                                                minWidth = 72.dp,
                                                                minHeight = 36.dp
                                                            )
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Outlined.People,
                                                            contentDescription = "Seguidores",
                                                            tint = MaterialTheme.colorScheme.primary,
                                                            modifier = Modifier.size(18.dp)
                                                        )
                                                        Spacer(Modifier.width(6.dp))
                                                        Text(
                                                            text = "$followersCount seguidores",
                                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                                fontWeight = FontWeight.SemiBold
                                                            ),
                                                            color = MaterialTheme.colorScheme.primary
                                                        )
                                                    }
                                                }
                                            } else {
                                                // reserva pequeno espaço caso não tenha contagem carregada ainda
                                                Spacer(Modifier.width(4.dp))
                                            }

                                            Spacer(modifier = Modifier.weight(1f))

                                            // rating (estrelas) + contador de avaliações (direita)
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                RatingBarMercadoLivre(rating = avg, count = cnt)
                                                Spacer(Modifier.width(8.dp))
                                            }
                                        }
                                    }

                                    avatarErr?.let {
                                        Spacer(Modifier.height(4.dp))
                                        Text(
                                            "Erro no avatar: $it",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }

                                // diálogo de seguidores
                                if (followersOpen && !profile?.id.isNullOrBlank()) {
                                    FollowersDialog(
                                        userId = profile!!.id!!,
                                        onDismiss = { followersOpen = false }
                                    )
                                }


                            }

                            Spacer(Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    onOpenCuidador(myId, myName, myAvatar)
                                },
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .height(48.dp),
                                shape = RoundedCornerShape(14.dp),


                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF218bc3),
                                    contentColor = Color.White // cor do texto/ícone
                                )

                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Visibility,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )

                                Spacer(Modifier.width(6.dp))

                                Text(
                                    "Ver meu perfil publico",
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                        }

                        }





                    Spacer(Modifier.height(16.dp))



                    when (selected) {
                        ProMenu.DASHBOARD -> {

                            // ======= PEDIDOS ATIVOS =======
                            Text("Pedidos ativos", style = MaterialTheme.typography.titleMedium)

                            Spacer(Modifier.height(8.dp))

                            when {
                                jobsLoading -> Box(
                                    Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) { CircularProgressIndicator() }

                                ativos.isEmpty() ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        shape = RoundedCornerShape(16.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                                        )
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {


                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.Center,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Assignment,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(22.dp),
                                                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                                                )

                                                Spacer(Modifier.width(8.dp))

                                                Text(
                                                    "Você não possui pedidos ativos",
                                                    style = MaterialTheme.typography.titleMedium,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                            }

                                            Spacer(Modifier.height(6.dp))

                                            Text(
                                                "Quando receber um pedido, ele aparecerá aqui.",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color.Gray,
                                                textAlign = TextAlign.Center
                                            )

                                            Spacer(Modifier.height(12.dp))

                                            Button(
                                                onClick = {
                                                    if (loadingDisponivel) return@Button

                                                    scope.launch {
                                                        loadingDisponivel = true

                                                        try {
                                                            val loc = suspendCancellableCoroutine<Location?> { cont ->
                                                                getUserLocation(ctx) {
                                                                    cont.resume(it, null)
                                                                }
                                                            }

                                                            if (loc == null) {
                                                                Toast.makeText(ctx, "Ative a localização", Toast.LENGTH_SHORT).show()
                                                                return@launch
                                                            }

                                                            if (!isDisponivel) {

                                                                val response = api.setDisponivel(
                                                                    mapOf(
                                                                        "lat" to loc.latitude,
                                                                        "lng" to loc.longitude
                                                                    )
                                                                )

                                                                if (response.isSuccessful) {
                                                                    isDisponivel = true
                                                                }

                                                            } else {

                                                                val response = api.setIndisponivel()
                                                                if (response.isSuccessful) {
                                                                    isDisponivel = false
                                                                }
                                                            }

                                                        } catch (e: Exception) {
                                                            Log.e("Disponivel", "Erro: ${e.message}")
                                                        } finally {
                                                            loadingDisponivel = false
                                                        }
                                                    }
                                                },


                                                enabled = !loadingDisponivel,

                                                shape = RoundedCornerShape(50),

                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = if (isDisponivel)
                                                        Color(0xFF22C55E)
                                                    else
                                                        MaterialTheme.colorScheme.primary
                                                ),

                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(46.dp)
                                            ) {
                                                Text(
                                                    when {
                                                        loadingDisponivel -> "Carregando..."
                                                        isDisponivel -> "Disponível"
                                                        else -> "Ficar disponível"
                                                    },
                                                    color = Color.White,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                            }
                                        }
                                    }
                                else -> LazyColumn {
                                    items(ativos, key = { it.id }) { j ->
                                        val userId = j.patientId
                                        val cached = publicCache[userId]

                                        LaunchedEffect(userId) {
                                            if (!publicCache.containsKey(userId)) {
                                                runCatching { api.getPublicUser(userId) }
                                                    .onSuccess { publicCache[userId] = it }
                                                    .onFailure {
                                                        publicCache[userId] =
                                                            PublicUserDto(id = userId)
                                                    }
                                            }
                                        }

                                        Card(
                                            modifier = Modifier.fillMaxWidth(),
                                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                                        ) {
                                            Column(Modifier.padding(12.dp)) {

                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .clickable {
                                                            val cachedNow = publicCache[userId]
                                                            if (cachedNow != null) {
                                                                profileOpen = cachedNow
                                                            } else {
                                                                scope.launch {
                                                                    runCatching {
                                                                        api.getPublicUser(
                                                                            userId
                                                                        )
                                                                    }
                                                                        .onSuccess { u ->
                                                                            publicCache[userId] =
                                                                                u; profileOpen =
                                                                            u
                                                                        }
                                                                }
                                                            }
                                                        },
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    val cachedNow = publicCache[j.patientId]
                                                    ClickZoomImage(
                                                        model = bust(
                                                            cachedNow?.avatarUrl,
                                                            j.createdAt
                                                        ),
                                                        sizeDp = 48.dp,
                                                        shape = CircleShape,
                                                        placeholderRes = R.drawable.ic_avatar_placeholder
                                                    )
                                                    Spacer(Modifier.width(10.dp))
                                                    Column(Modifier.weight(1f)) {
                                                        Text(
                                                            j.pacienteNome,
                                                            fontWeight = FontWeight.SemiBold
                                                        )
                                                        val cid =
                                                            cachedNow?.cidade?.takeIf { it.isNotBlank() }
                                                        val uf =
                                                            cachedNow?.uf?.takeIf { it.isNotBlank() }
                                                        if (cid != null || uf != null) {
                                                            Text(
                                                                "${cid ?: "—"}/${uf ?: "—"}",
                                                                style = MaterialTheme.typography.bodySmall,
                                                                color = MaterialTheme.colorScheme.onSurface.copy(
                                                                    alpha = 0.6f
                                                                )
                                                            )
                                                        }
                                                        cachedNow?.bio?.takeIf { it.isNotBlank() }
                                                            ?.let {
                                                                Spacer(Modifier.height(2.dp))
                                                                Text(
                                                                    it,
                                                                    style = MaterialTheme.typography.bodySmall,
                                                                    maxLines = 2
                                                                )
                                                            }
                                                    }
                                                }

                                                Spacer(Modifier.height(8.dp))

                                                j.endereco?.let {
                                                    Text(
                                                        "Endereço: $it",
                                                        style = MaterialTheme.typography.bodySmall
                                                    )
                                                }

                                                j.cidade?.let {
                                                    Text(
                                                        "Cidade: $it",
                                                        style = MaterialTheme.typography.bodySmall
                                                    )
                                                }
                                                j.bairro?.let {
                                                    Text(
                                                        "Bairro: $it",
                                                        style = MaterialTheme.typography.bodySmall
                                                    )
                                                }

                                                j.observ?.let {
                                                    Text(
                                                        "Obs.: $it",
                                                        style = MaterialTheme.typography.bodySmall
                                                    )
                                                }
                                                Text(
                                                    text = "Criado em: ${formatOnlyDate(j.createdAt) ?: j.createdAt}",
                                                    style = MaterialTheme.typography.bodySmall
                                                )
                                                j.scheduledAt?.let { iso ->
                                                    val bonito = formatDateAndTime(iso)
                                                    if (bonito != null) {
                                                        Text(
                                                            "Agendado para: $bonito",
                                                            style = MaterialTheme.typography.bodySmall
                                                        )
                                                    }
                                                }

                                                when (j.tipoServico) {

                                                    "DIARISTA" -> {
                                                        Text("Residência: ${j.tipoResidencia ?: "-"}")
                                                        Text("Tamanho: ${j.tamanhoResidencia ?: "-"}")
                                                        Text("Cômodos: ${j.numeroComodos ?: "-"}")
                                                        Text("Banheiros: ${j.numeroBanheiros ?: "-"}")
                                                    }

                                                    "REFRIGERISTA" -> {
                                                        Text("Aparelho: ${j.tipoAparelho ?: "-"}")
                                                        Text("Marca: ${j.marcaAparelho ?: "-"}")
                                                        Text("Modelo: ${j.modeloAparelho ?: "-"}")
                                                        Text("Precisando de: ${j.tipoProblema ?: "-"}")
                                                    }

                                                    "ASSISTENCIA" -> {
                                                        Text("Aparelho: ${j.tipoAparelho ?: "-"}")
                                                        Text("Marca: ${j.marcaAparelho ?: "-"}")
                                                        Text("Modelo: ${j.modeloAparelho ?: "-"}")
                                                        Text("Precisando de: ${j.tipoProblema ?: "-"}")
                                                    }

                                                    "MONTADORDEMOVEIS" -> {
                                                        Text("Tipo de Movel: ${j.tipoAparelho ?: "-"}")
                                                        Text("Largura: ${j.marcaAparelho ?: "-"}")
                                                        Text("Altura: ${j.modeloAparelho ?: "-"}")
                                                        Text("Precisando de : ${j.tipoProblema ?: "-"}")
                                                    }

                                                    "MARIDO_DE_ALUGUEL" -> {
                                                        j.descricaoServico?.let {
                                                            Text("Serviço solicitado: $it")
                                                        }
                                                    }
                                                }

                                                Spacer(Modifier.height(8.dp))
                                                Row(
                                                    horizontalArrangement = Arrangement.spacedBy(
                                                        8.dp
                                                    )
                                                ) {
                                                    if (j.status == "PENDENTE") {
                                                        Button(
                                                            enabled = !locked && !jobActionLoading,
                                                            onClick = {
                                                                scope.launch {
                                                                    jobActionLoading =
                                                                        true; jobActionError =
                                                                    null
                                                                    runCatching {
                                                                        withTimeout(
                                                                            6_000
                                                                        ) { api.acceptJob(j.id) }
                                                                    }
                                                                        .onFailure {
                                                                            jobActionError =
                                                                                it.message
                                                                        }
                                                                    runCatching { refreshJobs() }
                                                                    jobActionLoading = false
                                                                    unseenJobIds =
                                                                        unseenJobIds - j.id
                                                                }
                                                            }
                                                        ) { Text(if (jobActionLoading) "..." else "Aceitar") }

                                                        OutlinedButton(
                                                            enabled = !locked && !jobActionLoading,
                                                            onClick = {
                                                                scope.launch {
                                                                    jobActionLoading =
                                                                        true; jobActionError =
                                                                    null
                                                                    runCatching {
                                                                        withTimeout(
                                                                            6_000
                                                                        ) { api.declineJob(j.id) }
                                                                    }
                                                                        .onFailure {
                                                                            jobActionError =
                                                                                it.message
                                                                        }
                                                                    runCatching { refreshJobs() }
                                                                    jobActionLoading = false
                                                                    unseenJobIds =
                                                                        unseenJobIds - j.id
                                                                }
                                                            }
                                                        ) { Text("Recusar") }
                                                    } else if (j.status == "ACEITO") {
                                                        AssistChip(
                                                            onClick = {},
                                                            label = { Text("Em andamento") })
                                                        Button(
                                                            enabled = !locked && !jobActionLoading,
                                                            onClick = {
                                                                scope.launch {
                                                                    jobActionLoading =
                                                                        true; jobActionError =
                                                                    null
                                                                    runCatching {
                                                                        withTimeout(
                                                                            6_000
                                                                        ) { api.finishJob(j.id) }
                                                                    }
                                                                        .onFailure {
                                                                            jobActionError =
                                                                                it.message
                                                                        }
                                                                    runCatching { refreshJobs() }
                                                                    jobActionLoading = false
                                                                }
                                                            }
                                                        ) { Text(if (jobActionLoading) "..." else "Finalizar") }
                                                    }
                                                }
                                                jobActionError?.let {
                                                    Spacer(Modifier.height(6.dp))
                                                    Text(
                                                        "Erro: $it",
                                                        color = MaterialTheme.colorScheme.error,
                                                        style = MaterialTheme.typography.bodySmall
                                                    )
                                                }
                                            }
                                        }
                                        Spacer(Modifier.height(10.dp))
                                    }
                                }
                            }


                        }

                        ProMenu.HISTORICO -> {
                            ConteudoHistorico(
                                pad = PaddingValues(0.dp),
                                lista = historico,
                                onClear = { confirmClear = true })
                            clearErr?.let {
                                Spacer(Modifier.height(8.dp))
                                Text("Erro: $it", color = MaterialTheme.colorScheme.error)
                            }
                        }


                        ProMenu.ARQUIVADOS -> {
                            Text(
                                "Abra o ícone de mensagens para ver os chats arquivados.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }








                    }




                    Spacer(Modifier.height(16.dp))


                    when {
                        loadingFeed -> Box(
                            Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) { CircularProgressIndicator() }

                        feedErr != null -> Text(
                            "Erro ao carregar feed: $feedErr",
                            color = MaterialTheme.colorScheme.error
                        )

                        else -> FeedSection(
                            posts = posts,
                            onAddPost = { uri, caption ->
                                if (uri != null) {
                                    scope.launch {
                                        val newPost =
                                            addPost(ApiProvider.get(ctx), ctx, uri, caption)
                                        posts = posts + newPost
                                    }
                                }
                            },
                            onToggleStar = { postId ->
                                scope.launch {
                                    posts = posts.map { p ->
                                        if (p.id == postId) togglePostStar(
                                            ApiProvider.get(ctx),
                                            p
                                        ) else p
                                    }
                                }
                            },
                            onDeletePost = { postId ->
                                scope.launch {
                                    ApiProvider.get(ctx).deletePost(postId)
                                    posts = posts.filterNot { it.id == postId }
                                }
                            }
                        )

                    }

                }

                if (locked && !loadingSub) {
                    LockedOverlay(
                        role = role,
                        onManageClick = onOpenBillingImpl,
                        loading = billingInProgress,
                        enabled = !billingInProgress,
                        snackbarHost = snackbar,
                        onSubUpdated = { sub ->
                            subStatus = sub
                            session.setSubscriptionSnapshot(
                                subActive = sub.subActive,
                                canAccess = sub.canAccess,
                                trialEndsAtIso = sub.trialEndsAt,
                                trialClaimed = sub.trialClaimed
                            )
                        }
                    )
                }

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

        if (confirmClear) {
            AlertDialog(
                onDismissRequest = { if (!clearingHistory) confirmClear = false },
                title = { Text("Limpar histórico") },
                text = { Text("Tem certeza que deseja remover todos os serviços finalizados/recusados/cancelados do seu histórico?") },
                confirmButton = {
                    TextButton(
                        enabled = !clearingHistory && !locked,
                        onClick = {
                            scope.launch {
                                try {
                                    clearingHistory = true
                                    clearErr = null
                                    withTimeout(6_000) { api.clearProHistory() }
                                    withContext(Dispatchers.IO) {
                                        runCatching { api.listMyJobs() }.onSuccess {
                                            jobs = it
                                        }
                                    }
                                    confirmClear = false
                                } catch (e: Exception) {
                                    clearErr = e.message ?: "Falha ao limpar histórico"
                                } finally {
                                    clearingHistory = false
                                }
                            }
                        }
                    ) { Text(if (clearingHistory) "Limpando..." else "Sim, limpar") }
                },
                dismissButton = {
                    TextButton(
                        enabled = !clearingHistory,
                        onClick = { confirmClear = false }) { Text("Cancelar") }
                }
            )
        }


        jobDialog?.let { j ->
            AlertDialog(
                onDismissRequest = { jobDialog = null },
                title = { Text("Confirmar pedido") },
                text = {
                    Column {
                        Text("Cliente: ${j.pacienteNome}", fontWeight = FontWeight.SemiBold)
                        j.endereco?.let { Text("Endereço: $it") }
                        j.observ?.let { Text("Obs.: $it") }
                        Text(
                            text = "Criado em: ${formatOnlyDate(j.createdAt) ?: j.createdAt}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        j.scheduledAt?.let { iso ->
                            val bonito = formatDateAndTime(iso)
                            if (bonito != null) {
                                Text(
                                    "Agendado para: $bonito",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(
                        enabled = !locked && !jobActionLoading,
                        onClick = {
                            scope.launch {
                                jobActionLoading = true; jobActionError = null
                                runCatching { withTimeout(6_000) { api.acceptJob(j.id) } }.onFailure {
                                    jobActionError = it.message
                                }
                                withContext(Dispatchers.IO) { refreshJobs() }
                                jobActionLoading = false
                                unseenJobIds = unseenJobIds - j.id
                                jobDialog = null
                            }
                        }
                    ) { Text(if (jobActionLoading) "..." else "Aceitar") }
                },
                dismissButton = {
                    Row {
                        TextButton(onClick = { jobDialog = null }) { Text("Cancelar") }
                        Spacer(Modifier.width(8.dp))
                        TextButton(
                            enabled = !locked && !jobActionLoading,
                            onClick = {
                                scope.launch {
                                    jobActionLoading = true; jobActionError = null
                                    runCatching { withTimeout(6_000) { api.declineJob(j.id) } }.onFailure {
                                        jobActionError = it.message
                                    }
                                    withContext(Dispatchers.IO) { refreshJobs() }
                                    jobActionLoading = false
                                    unseenJobIds = unseenJobIds - j.id
                                    jobDialog = null
                                }
                            }
                        ) { Text("Recusar") }
                    }
                }
            )
        }

        if (showDailyDialog) {
            AlertDialog(
                onDismissRequest = { if (!savingDaily) showDailyDialog = false },
                title = { Text("Definir valor da diária") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = dailyInput,
                            onValueChange = {
                                dailyInput =
                                    it.filter { ch -> ch.isDigit() || ch == ',' || ch == '.' }
                            },
                            label = { Text("R$ (ex.: 250,00)") },
                            singleLine = true,
                            supportingText = {
                                saveDailyErr?.let {
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
                        enabled = !savingDaily && !locked,
                        onClick = {
                            val normalized = dailyInput.replace(",", ".").trim()
                            val valorReais = normalized.toDoubleOrNull()
                            if (valorReais == null || valorReais <= 0.0) {
                                saveDailyErr = "Informe um valor válido."
                                return@TextButton
                            }
                            val cents = (valorReais * 100).toInt()
                            scope.launch {
                                try {
                                    savingDaily = true
                                    saveDailyErr = null
                                    withTimeout(6_000) {
                                        api.updateProfile(
                                            UpdateProfileReq(
                                                dailyRate = cents
                                            )
                                        )
                                    }
                                    refreshProfileOnly(api) { profile = it }
                                    showDailyDialog = false
                                } catch (e: Exception) {
                                    saveDailyErr = e.message ?: "Falha ao salvar"
                                } finally {
                                    savingDaily = false
                                }
                            }
                        }
                    ) { Text(if (savingDaily) "Salvando..." else "Salvar") }
                },
                dismissButton = {
                    TextButton(
                        enabled = !savingDaily,
                        onClick = { showDailyDialog = false }) { Text("Cancelar") }
                }
            )
        }

        if (showRegionDialog) {

            val maxCities = 6
            val canAddMore = tempRegions.size < maxCities

            val cidadesFiltradas = listaCidades
                .filter { it.nome.contains(tempCidade, ignoreCase = true) }
                .sortedBy { !it.nome.startsWith(tempCidade, ignoreCase = true) }

            AlertDialog(
                onDismissRequest = { if (!savingRegion) showRegionDialog = false },
                title = { Text("Definir regiões de atendimento") },
                text = {
                    Column {


                        @OptIn(ExperimentalLayoutApi::class)
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            tempRegions.forEachIndexed { index, pair ->

                                val label = if (index == 0)
                                    "📍 ${pair.second}"
                                else
                                    pair.second

                                AssistChip(
                                    onClick = {},
                                    label = { Text(label, color = Color.White) },
                                    colors = AssistChipDefaults.assistChipColors(
                                        containerColor = Color(0xFF2E7D32)
                                    ),
                                    trailingIcon = {
                                        Icon(
                                            Icons.Default.Close,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.clickable {
                                                if (!savingRegion) {
                                                    tempRegions = tempRegions.toMutableList()
                                                        .also { it.removeAt(index) }
                                                }
                                            }
                                        )
                                    }
                                )
                            }
                        }

                        if (tempRegions.isNotEmpty()) {
                            Spacer(Modifier.height(8.dp))
                            Divider()
                            Spacer(Modifier.height(8.dp))
                        }


                        ExposedDropdownMenuBox(
                            expanded = ufMenuExpanded,
                            onExpandedChange = {
                                if (!savingRegion) ufMenuExpanded = !ufMenuExpanded
                            }
                        ) {
                            OutlinedTextField(
                                value = tempUf,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("UF") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(ufMenuExpanded)
                                },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )

                            ExposedDropdownMenu(
                                expanded = ufMenuExpanded,
                                onDismissRequest = { ufMenuExpanded = false }
                            ) {
                                UFS.forEach { uf ->
                                    DropdownMenuItem(
                                        text = { Text(uf) },
                                        onClick = {
                                            tempUf = uf
                                            ufMenuExpanded = false


                                            scope.launch {
                                                try {
                                                    listaCidades = ibgeApi.getCidades(uf)
                                                    tempRegions = emptyList()
                                                } catch (e: Exception) {
                                                    regionErr = "Erro ao carregar cidades"
                                                }
                                            }
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(8.dp))

                        // ===== CIDADE (AUTOCOMPLETE IBGE)
                        ExposedDropdownMenuBox(
                            expanded = cidadeMenuExpanded,
                            onExpandedChange = { cidadeMenuExpanded = it }
                        ) {
                            OutlinedTextField(
                                value = tempCidade,
                                onValueChange = {
                                    if (savingRegion) return@OutlinedTextField
                                    tempCidade = it
                                    cidadeMenuExpanded = true
                                },
                                label = { Text("Digite cidade") },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )

                            ExposedDropdownMenu(
                                expanded = cidadeMenuExpanded &&
                                        tempCidade.length >= 2 &&
                                        cidadesFiltradas.isNotEmpty(),
                                onDismissRequest = { cidadeMenuExpanded = false }
                            ) {
                                cidadesFiltradas.take(20).forEach { cidadeItem ->

                                    DropdownMenuItem(
                                        text = { Text(cidadeItem.nome) },
                                        onClick = {

                                            val uf = tempUf.trim().uppercase()

                                            if (uf.length != 2) {
                                                regionErr = "Selecione um estado primeiro"
                                                return@DropdownMenuItem
                                            }

                                            if (tempRegions.size >= maxCities) return@DropdownMenuItem

                                            if (tempRegions.isNotEmpty() && tempRegions.first().first != uf) {
                                                regionErr = "Todas as cidades devem ser do mesmo estado"
                                                return@DropdownMenuItem
                                            }

                                            val exists = tempRegions.any {
                                                it.first == uf && it.second.equals(cidadeItem.nome, true)
                                            }

                                            if (!exists) {
                                                tempRegions = tempRegions + (uf to cidadeItem.nome)
                                                regionErr = null
                                            }

                                            tempCidade = ""
                                            cidadeMenuExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(8.dp))

                        regionErr?.let {
                            Text(it, color = MaterialTheme.colorScheme.error)
                        }
                    }
                },

                confirmButton = {
                    TextButton(
                        enabled = tempRegions.isNotEmpty() && !savingRegion && !locked,
                        onClick = {
                            val uf = tempRegions.first().first
                            val cidades = tempRegions.map { it.second }

                            scope.launch {
                                savingRegion = true
                                regionErr = null
                                try {
                                    api.updateMyRegions(
                                        UpdateRegionsReq(
                                            uf = uf,
                                            cidades = cidades
                                        )
                                    )
                                    refreshProfileOnly(api) { profile = it }
                                    showRegionDialog = false
                                } catch (e: retrofit2.HttpException) {
                                    regionErr = e.response()?.errorBody()?.string()
                                        ?: "Erro ao salvar regiões"
                                } catch (e: Exception) {
                                    regionErr = e.message ?: "Erro inesperado"
                                } finally {
                                    savingRegion = false
                                }
                            }
                        }
                    ) {
                        Text(if (savingRegion) "Salvando..." else "Aplicar")
                    }
                },

                dismissButton = {
                    TextButton(
                        enabled = !savingRegion,
                        onClick = { showRegionDialog = false }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
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

    }





    DeleteAccountDialog(
        show = showDeleteAcc,
        onDismiss = { showDeleteAcc = false },
        onSuccess = {
            showDeleteAcc = false
            onLogout()
        }
    )

    ContratanteProfileSheet(
        open = profileOpen != null,
        onClose = { profileOpen = null },
        user = profileOpen
    )


    if (messagesSheetOpen) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { messagesSheetOpen = false },
            sheetState = sheetState,
            dragHandle = { Text("Mensagens", Modifier.padding(12.dp)) }
        ) {
            // Abas
            Row(Modifier.padding(horizontal = 16.dp)) {
                FilterChip(
                    selected = messagesTab == BoxTab.INBOX,
                    onClick = { messagesTab = BoxTab.INBOX },
                    label = { Text("Mensagens") }
                )
                Spacer(Modifier.width(8.dp))
                FilterChip(
                    selected = messagesTab == BoxTab.ARQUIVADOS,
                    onClick = { messagesTab = BoxTab.ARQUIVADOS },
                    label = { Text("Arquivados") }
                )
            }
            Spacer(Modifier.height(8.dp))

            val isLoading = if (messagesTab == BoxTab.INBOX) inboxLoading else archivedLoading
            val showList = if (messagesTab == BoxTab.INBOX) inbox else archived

            when {
                isLoading -> Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }

                showList.isEmpty() -> Text(
                    if (messagesTab == BoxTab.INBOX) "Sem mensagens ainda." else "Sem arquivados.",
                    Modifier.padding(16.dp)
                )

                else -> LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 520.dp)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    items(showList, key = { it.id }) { c ->
                        var menuOpen by remember { mutableStateOf(false) }

                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    // marca como lido e abre conversa
                                    val stamp = c.lastAt ?: Instant.now().toString()
                                    ChatBadgeStore
                                        .setLastRead(ctx, c.id, stamp, myUserId = myId)
                                    chatUnseenIds = chatUnseenIds - c.id
                                    unreadCount = chatUnseenIds.size
                                    messagesSheetOpen = false
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
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                }
                            }
                            IconButton(onClick = { menuOpen = true }) {
                                Icon(Icons.Filled.MoreVert, contentDescription = "Mais")
                            }
                            DropdownMenu(
                                expanded = menuOpen,
                                onDismissRequest = { menuOpen = false }) {
                                if (messagesTab == BoxTab.INBOX) {
                                    DropdownMenuItem(
                                        enabled = !locked,
                                        text = { Text("Arquivar") },
                                        onClick = {
                                            menuOpen = false
                                            scope.launch {
                                                runCatching { archiveChat(c.id) }
                                                refreshChats()
                                            }
                                        }
                                    )
                                } else {
                                    DropdownMenuItem(
                                        enabled = !locked,
                                        text = { Text("Desarquivar") },
                                        onClick = {
                                            menuOpen = false
                                            scope.launch {
                                                runCatching { unarchiveChat(c.id) }
                                                refreshChats()
                                            }
                                        }
                                    )
                                }
                                Divider()
                                DropdownMenuItem(
                                    enabled = !locked,
                                    text = { Text("Excluir conversa") },
                                    onClick = {
                                        menuOpen = false
                                        scope.launch {
                                            runCatching { deleteChat(c.id) }
                                            refreshChats()
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

            Spacer(Modifier.height(12.dp))


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

        }
    }


}