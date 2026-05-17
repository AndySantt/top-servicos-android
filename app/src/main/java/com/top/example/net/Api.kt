package com.top.example.net

import android.content.Context
import com.top.example.R
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import kotlinx.serialization.SerialName
import android.os.Parcelable
import kotlinx.parcelize.Parcelize



@Serializable
enum class Role { CONTRATANTE, PROFISSIONAL }
@Serializable
enum class VerificationStatus {
    PENDENTE,
    VERIFICADO,
    @SerialName("REJEITADO") REPROVADO, // backend manda "REJEITADO"
    EXPIRADO
}







@Serializable
enum class TipoPro { DIARISTA,FAXINEIRA, COZINHEIRA, PEDREIRO, REFRIGERISTA, ELETRICISTA,
    GARCOM, BARBEIRO, CABELEIREIRA, CONFEITEIRA, PUBLICIDADE,
    MANICURE, AJUDANTE, CARPINTEIRO, PINTOR, ENTREGADOR,
    JARDINEIRO, GESSEIRO, PERSONAL, MECANICO, ENCANADOR, SERRALHEIRO, ESTETICA, MARIDO_DE_ALUGUEL,INST_TRANSITO, ALPINISTA, MASSAGISTA,
ASSISTENCIA, EVENTOS, FOTOGRAFO, BUFFET, DECORACAO, BARTENDERS, DESIGNER, TECNOLOGIA, MONTADORDEMOVEIS, NUTRICIONISTA, PSICOLOGO, DENTISTA, GUINCHO, CONTADOR, ADVOGADO, VETERINARIO, PETSHOP, MUDANCAS,
    TATUADOR, SOLDADOR, SEGURANCA, DOCEIRA, DEPILACAO, DESPACHANTE, LAVA_JATO, TAXISTA,CUIDADOR,ACOMPANHANTE,DOULA,GRAFICA,QUIROPRAXISTA,FISIOTERAPEUTA,VENDEDOR,ENGENHEIRO,DESENTUPIDORA
}


@Serializable
enum class NoticeMediaType {
    IMAGE,
    VIDEO
}

@Serializable
enum class NoticeTheme {
    VINHO,
    AZUL_ESCURO,
    VERDE_ESCURO,
    ROXO_ESCURO,
    PRETO
}





@Serializable
data class SimpleMsg(
    val ok: Boolean = true,
    val message: String = ""
)


@Serializable
data class LoginReq(val email: String, val senha: String, val expectedTipo: TipoPro? = null)

@Serializable
data class LoginResp(
    val token: String,
    val role: Role,
    val userId: String,
    val emailVerified: Boolean,
    val proStatus: VerificationStatus? = null,
    val tipo: TipoPro? = null,

    val subActive: Boolean = false,
    val canAccess: Boolean = false,
    val trialClaimed: Boolean = false,
    val trialEndsAt: String? = null
)

@Serializable
data class SignupReq(
    val nome: String,
    val email: String,
    val senha: String,
    val role: Role,
    val uf: String,
    val cidade: String? = null,
    val cidades: List<String>? = null,
    val cpf: String,
    val coren: String?,
    val dailyRate: Int?,
    val profession: TipoPro?,
    val acceptedTerms: Boolean,
    val acceptedAt: String,
    val termsVersion: String,
    val genero: String?
)

@Serializable data class VerifyEmailReq(val email: String, val code: String)
@Serializable data class ForgotReq(val email: String)
@Serializable data class ResetReq(val token: String, val newPassword: String)


@Serializable
data class MeProfileResp(
    val id: String? = null,
    val nome: String,
    val cpf: String? = null,
    val role: String? = null,
    val avatarUrl: String? = null,
    val uf: String? = null,
    val cidade: String? = null,
    val bio: String = "",
    val tipo: TipoPro? = null,
    val verified: Boolean? = null,
    val dailyRate: Int? = null,
    val ratingAvg: Double? = null,
    val ratingCount: Int = 0,
    val subscription: SubscriptionDto? = null,
    val genero: String? = null

)


@Serializable
data class SubscriptionDto(
    val trialClaimed: Boolean,
    val trialEndsAt: String? = null,
    val subActive: Boolean,
    val paidUntil: String? = null
)

@Serializable
data class UpdateProfileReq(
    val nome: String? = null,
    val cpf: String? = null,
    val bio: String? = null,
    val avatarUrl: String? = null,
    val dailyRate: Int? = null,
    val uf: String? = null,
    val cidade: String? = null
)

@Serializable
data class SubStatusResp(
    val verified: Boolean? = null,
    val trialClaimed: Boolean,
    val trialEndsAt: String?,
    val subActive: Boolean,
    val canAccess: Boolean,
    val tipo: TipoPro? = null
)

@Serializable data class ClaimTrialResp(val ok: Boolean, val trialEndsAt: String)


@Serializable
@Parcelize
data class ProPublicDto(
    val userId: String,
    val nome: String,
    val cidadeExibida: String,
    val uf: String? = null,
    val bio: String? = null,
    val avatarUrl: String? = null,
    val hourlyRate: Int? = null,
    val ratingAvg: Double? = null,
    val ratingCount: Int = 0,
    val busyToday: Boolean = false,
    val dailyRate: Int? = null,
    val verified: Boolean = false,
    val tipo: TipoPro? = null,
    val distanciaKm: Double? = null,


    val lastPostAt: Long? = null,
    val disponivel: Boolean = false,
    val temLocalizacao: Boolean = false

) : Parcelable


@Serializable
data class UserRatingsDto(

    val avg: Double,

    val count: Int,

    val distribution: Map<Int, Int>,

    val comments: List<RatingCommentDto>

)

@Serializable
data class RatingCommentDto(

    val id: String,

    val patientName: String,

    val avatarUrl: String? = null,

    val stars: Int,

    val comment: String? = null,

    val response: String? = null,

    val createdAt: String,

    val likes: Int = 0,

    val likedByMe: Boolean = false
)


@Serializable data class StartChatReq(val withUserId: String)
@Serializable data class ChatIdResp(val conversationId: String)
@Serializable
data class SendMsgReq(
    val text: String? = null,
    val type: String = "TEXT",
    val fileUrl: String? = null,
    val fileName: String? = null,
    val mimeType: String? = null,
    val duration: Int? = null
)
@Serializable
data class MsgDto(
    val id: String,
    val senderId: String,
    val text: String? = null,
    val type: String,
    val fileUrl: String? = null,
    val fileName: String? = null,
    val mimeType: String? = null,
    val duration: Int? = null,
    val createdAt: String
)
@Serializable
data class ConversationSummary(
    val id: String,
    val withUserId: String,
    val withName: String,
    val lastText: String? = null,
    val lastAt: String? = null,
    val withAvatarUrl: String? = null
)


@Serializable
data class CreateJobReq(
    val caregiverId: String,
    val pacienteNome: String,
    val idade: Int? = null,
    val sexo: String? = null,
    val endereco: String? = null,
    val observ: String? = null,
    val date: String? = null,
    val time: String? = null,
    val tipoServico: String? = null,
    val bairro: String? = null,
    val cidade: String? = null,

    val tipoResidencia: String? = null,
    val tamanhoResidencia: String? = null,
    val numeroComodos: Int? = null,
    val numeroBanheiros: Int? = null,


    val tipoAparelho: String? = null,
    val marcaAparelho: String? = null,
    val modeloAparelho: String? = null,
    val tipoProblema: String? = null,


    val descricaoServico: String? = null,
)
@Serializable data class CreateJobResp(val jobId: String)

@kotlinx.serialization.Serializable
data class AvatarResp(val avatarUrl: String)

@Serializable
data class JobPublicDto(

    val id: String,
    val patientId: String,
    val pacienteNome: String,
    val bairro: String? = null,
    val cidade: String? = null,
    val idade: Int? = null,
    val sexo: String? = null,
    val endereco: String? = null,
    val observ: String? = null,

    val createdAt: String,
    val status: String,
    val scheduledAt: String? = null,
    val tipoServico: String? = null,

    val tipoResidencia: String? = null,
    val tamanhoResidencia: String? = null,
    val numeroComodos: Int? = null,
    val numeroBanheiros: Int? = null,

    val tipoAparelho: String? = null,
    val marcaAparelho: String? = null,
    val modeloAparelho: String? = null,
    val tipoProblema: String? = null,

    val descricaoServico: String? = null
)

@Serializable
data class JobDto(
    val id: String,
    val caregiverId: String,
    val status: String,
    val scheduledAt: String? = null,
    val ratedByPatient: Boolean = false,
    val ratingDismissed: Boolean = false
)

@Serializable
data class JobMineDto(
    val id: String,
    val caregiverId: String,
    val caregiverName: String,
    val status: String,
    val scheduledAt: String? = null,
    val createdAt: String,
    val ratedByPatient: Boolean = false,
    val ratingDismissed: Boolean = false
)

@Serializable data class RateReq(val stars: Int, val comment: String? = null)

@Serializable
data class RatingSummaryResp(
    val avg: Double,
    val count: Int
)

@Serializable
data class UpdateRegionReq(val uf: String, val cidade: String)



@Serializable
data class UpdateRegionsReq(
    val uf: String,
    val cidades: List<String>
)

@Serializable
data class UpdateRegionResp(val ok: Boolean, val uf: String, val cidade: String)

@Serializable
data class ConfirmGoogleReq(val purchaseToken: String, val productId: String)

@Serializable
data class SavePushTokenReq(val token: String)

@Serializable
data class AvatarUploadResp(val avatarUrl: String)

@Serializable
data class NoticeDto(
    val id: String,
    val message: String? = null,
    val mediaUrl: String? = null,
    val mediaType: NoticeMediaType? = null,
    val version: Int,
    val theme: NoticeTheme? = null
)


@kotlinx.serialization.Serializable
data class ReportReq(
    val reportedUserId: String? = null,
    val reason: String,
    val details: String? = null
)
@kotlinx.serialization.Serializable
data class ReportResp(val ok: Boolean)

@kotlinx.serialization.Serializable
data class DeleteAccountReq(val password: String)


@kotlinx.serialization.Serializable
data class PublicUserDto(
    val id: String,
    val nome: String? = null,
    val avatarUrl: String? = null,
    val bio: String? = null,
    val cidade: String? = null,
    val uf: String? = null,
    val tipo: TipoPro? = null
)





@kotlinx.serialization.Serializable
data class StarResultDto(
    @SerialName("post_id") val postId: String,
    val starred: Boolean,
    @SerialName("star_count") val starCount: Int
)

@Serializable
data class ToggleFollowResp(
    @SerialName("user_id") val userId: String,
    val following: Boolean,
    @SerialName("followers_count") val followersCount: Int
)

@Serializable
data class PostDto(
    val id: String? = null,
    @SerialName("image_url") val imageUrl: String? = null,
    val caption: String? = null,
    @SerialName("star_count") val starCount: Int = 0,
    @SerialName("starred_by_me") val starredByMe: Boolean = false,
    @SerialName("created_at") val createdAt: String? = null
)

@kotlinx.serialization.Serializable
data class CreatePostResp(
    val id: String,
    @SerialName("image_url") val imageUrl: String? = null,
    val message: String? = null
)

@kotlinx.serialization.Serializable
data class ToggleStarResp(
    val starred: Boolean,
    val starCount: Int
)

@kotlinx.serialization.Serializable
data class PublicStatsResp(
    val followers: Int,
    val posts: Int,
    val ratingAvg: Double,
    val ratingCount: Int
)

@Serializable
data class UserCountersDto(
    val followers: Int,
    val posts: Int,
    @SerialName("stars_total") val starsTotal: Int,
    @SerialName("is_following") val isFollowing: Boolean
)

@kotlinx.serialization.Serializable
data class FollowerDto(
    val id: String,
    val name: String,
    @kotlinx.serialization.SerialName("avatar_url")
    val avatarUrl: String? = null
)

@Serializable
data class StoryDto(
    val userId: String,
    val userName: String,
    val avatarUrl: String?,
    val lastPostId: String?,
    val lastImageUrl: String?,
    val lastPostAt: Long?,
    val hasPosts: Boolean,
    val hasUnseen: Boolean
)

@kotlinx.serialization.Serializable
data class CaregiverAgendaDto(
    val scheduled: List<String>
)


@Serializable
data class ResponseReq(
    val response: String
)

@Serializable
data class LikeResp(
    val liked: Boolean,
    val likes: Long
)

@Serializable
data class CidadeIbge(
    val id: Int,
    val nome: String
)


interface ApiService {

    @POST("auth/signup") suspend fun signup(@Body b: SignupReq): SimpleMsg
    @POST("auth/verify-email") suspend fun verifyEmail(@Body b: VerifyEmailReq): SimpleMsg
    @POST("auth/resend-code") suspend fun resendCode(@Body b: ForgotReq): SimpleMsg
    @POST("auth/login") suspend fun login(@Body b: LoginReq): LoginResp
    @POST("auth/forgot") suspend fun forgot(@Body b: ForgotReq): SimpleMsg
    @POST("auth/reset") suspend fun reset(@Body b: ResetReq): SimpleMsg


    @GET("me/profile") suspend fun getMyProfile(): MeProfileResp
    @GET("me/subscription") suspend fun mySubscription(): SubStatusResp
    @POST("me/pro/claim-trial") suspend fun claimTrial(): SimpleMsg


    @POST("billing/google/confirm")
    suspend fun confirmGooglePurchase(@Body b: ConfirmGoogleReq): SimpleMsg


    @Multipart
    @POST("me/avatar")
    suspend fun uploadAvatar(@Part file: MultipartBody.Part): AvatarResp


    @POST("me/profile")
    suspend fun updateProfile(@Body req: UpdateProfileReq): Unit


    @GET("me/ratings/summary") suspend fun myRatingSummary(): RatingSummaryResp


    @GET("/pros")
    suspend fun listPros(
        @Query("q") q: String? = null,
        @Query("tipo") tipo: String? = null,
        @Query("uf") uf: String,
        @Query("cidade") cidade: String,
        @Query("lat") lat: Double? = null,
        @Query("lng") lng: Double? = null
    ): List<ProPublicDto>

    @POST("pros/disponivel")
    suspend fun setDisponivel(
        @Body body: Map<String, Double>
    ): retrofit2.Response<Unit>

    @POST("pros/indisponivel")
    suspend fun setIndisponivel(): retrofit2.Response<Unit>


    // CHAT
    @POST("chat/start") suspend fun startChat(@Body b: StartChatReq): ChatIdResp
    @GET("chat/{id}/messages") suspend fun getMessages(@Path("id") conversationId: String): List<MsgDto>
    @POST("chat/{id}/messages") suspend fun sendMessage(@Path("id") conversationId: String, @Body b: SendMsgReq): MsgDto
    @GET("chat/my") suspend fun myChats(): List<ConversationSummary>
    @DELETE("chat/{id}/messages") suspend fun clearChat(@Path("id") conversationId: String): SimpleMsg
    @DELETE("chat/{id}") suspend fun deleteChat(@Path("id") conversationId: String): SimpleMsg
    @POST("chat/{id}/archive") suspend fun archiveChat(@Path("id") conversationId: String): SimpleMsg
    @POST("chat/{id}/unarchive") suspend fun unarchiveChat(@Path("id") conversationId: String): SimpleMsg
    @GET("chat/archived") suspend fun myArchivedChats(): List<ConversationSummary>


        @Multipart
        @POST("chat/{id}/upload")
        suspend fun uploadFile(
            @Path("id") conversationId: String,
            @Part file: MultipartBody.Part
        ): Map<String, String>



    @GET("me/pro/jobs") suspend fun listMyJobs(): List<JobPublicDto>
    @GET("me/patient/jobs") suspend fun listMyPatientJobs(): List<JobMineDto>
    @POST("jobs") suspend fun createJob(@Body b: CreateJobReq): CreateJobResp
    @GET("jobs/{id}") suspend fun getJob(@Path("id") jobId: String): JobDto
    @POST("jobs/{id}/accept") suspend fun acceptJob(@Path("id") jobId: String): SimpleMsg
    @POST("jobs/{id}/decline") suspend fun declineJob(@Path("id") jobId: String): SimpleMsg
    @POST("jobs/{id}/finish") suspend fun finishJob(@Path("id") jobId: String): SimpleMsg
    @POST("jobs/{id}/rate") suspend fun rateJob(@Path("id") jobId: String, @Body b: RateReq): SimpleMsg
    @POST("jobs/{id}/skip-rating")
    suspend fun skipRating(
        @Path("id") jobId: String
    ): SimpleMsg




    @GET("users/public/{id}")
    suspend fun getPublicUser(@Path("id") userId: String): PublicUserDto

    @POST("me/pro/clear-history") suspend fun clearProHistory(): SimpleMsg
    @POST("me/patient/clear-history") suspend fun clearPatientHistory(): SimpleMsg

    @POST("profile/region") suspend fun updateMyRegion(@Body b: UpdateRegionReq): UpdateRegionResp

    @POST("/profile/regions")
    suspend fun updateMyRegions(
        @Body body: UpdateRegionsReq
    )


    @POST("me/push-token") suspend fun saveMyPushToken(@Body b: SavePushTokenReq): SimpleMsg


    @GET("me/notice")
    suspend fun getMyNotice(): NoticeDto?

    @POST("me/notice/{id}/dismiss")
    suspend fun dismissNotice(@Path("id") id: String): SimpleMsg


    @GET("me/notice")
    suspend fun getMyNoticeResponse(): retrofit2.Response<NoticeDto?>

    @POST("reports")
    suspend fun reportUser(@Body req: ReportReq): ReportResp

    @POST("me/delete-account")
    suspend fun deleteMyAccount(@Body req: DeleteAccountReq): SimpleMsg





    @GET("feed/my-posts")
    suspend fun listMyPosts(): List<PostDto>


    @GET("feed/users/{userId}/posts")
    suspend fun listUserPosts(@Path("userId") userId: String): List<PostDto>

    @Multipart
    @POST("feed/posts")
    suspend fun createPost(
        @Part image: MultipartBody.Part,
        @Part("caption") caption: okhttp3.RequestBody
    ): CreatePostResp

    @POST("feed/posts/{postId}/toggle-star")
    suspend fun toggleStar(@Path("postId") postId: String): StarResultDto


    @GET("users/{id}/posts")
    suspend fun listPosts(@Path("id") userId: String): List<PostDto>








    @GET("feed/users/{userId}/posts")
    suspend fun getUserPosts(@Path("userId") userId: String): List<PostDto>


    @POST("social/users/{userId}/toggle-follow")
    suspend fun toggleFollow(@Path("userId") userId: String): ToggleFollowResp


    @GET("me/posts")
    suspend fun getMyPosts(): List<PostDto>


    @GET("social/users/{userId}/followers")
    suspend fun getFollowers(@Path("userId") userId: String): List<FollowerDto>

    @DELETE("feed/posts/{postId}")
    suspend fun deletePost(@Path("postId") postId: String): retrofit2.Response<SimpleMsg>

    @GET("feed/stories")
    suspend fun listStories(): List<StoryDto>

    @POST("feed/posts/{postId}/view")
    suspend fun registerView(
        @Path("postId") postId: String
    )

    @POST("feed/stories/{userId}/view")
    suspend fun registerStoryView(
        @Path("userId") userId: String
    )

    @GET("/caregivers/{id}/agenda")
    suspend fun getCaregiverAgenda(
        @Path("id") caregiverId: String
    ): CaregiverAgendaDto

    @GET("users/{id}/stats")
    suspend fun getUserStats(@Path("id") userId: String): PublicStatsResp

    @GET("users/{id}/ratings")
    suspend fun getUserRatings(
        @Path("id") userId: String
    ): UserRatingsDto

    @GET("feed/users/{userId}/counters")
    suspend fun getUserCounters(@Path("userId") userId: String): UserCountersDto

    @POST("ratings/{id}/like")
    suspend fun likeRating(
        @Path("id") ratingId: String
    ): LikeResp

    @POST("ratings/{id}/response")
    suspend fun respondRating(
        @Path("id") ratingId: String,
        @Body body: ResponseReq
    ): SimpleMsg

    companion object {
        fun create(
            baseUrl: String,
            tokenProvider: () -> String? = { null }
        ): ApiService {
            val json = Json {
                ignoreUnknownKeys = true
                isLenient = true
                encodeDefaults = true
            }
            val contentType = "application/json".toMediaType()

            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val auth = okhttp3.Interceptor { chain ->
                val token = tokenProvider()

                android.util.Log.d("AUTH_DEBUG", "TOKEN ENVIADO = $token")

                val req = if (!token.isNullOrBlank()) {
                    chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                } else {
                    chain.request()
                }

                chain.proceed(req)
            }

            val netLogger = okhttp3.Interceptor { chain ->
                val req = chain.request()
                android.util.Log.d("API", "→ ${req.method} ${req.url}")
                val res = chain.proceed(req)
                android.util.Log.d("API", "← ${res.code} ${req.url}")
                res
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(auth)
                .addInterceptor(logging)
                .addNetworkInterceptor(netLogger)
                .callTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(json.asConverterFactory(contentType))
                .client(client)
                .build()
                .create(ApiService::class.java)
        }

        fun createFromContext(
            ctx: Context,
            tokenProvider: () -> String? = { null }
        ): ApiService {
            val base = ctx.getString(R.string.base_url)
            android.util.Log.d("API", "BASE_URL = $base")
            return create(baseUrl = base, tokenProvider = tokenProvider)
        }
    }
}



interface IbgeService {

    @GET("localidades/estados/{uf}/municipios")
    suspend fun getCidades(
        @Path("uf") uf: String
    ): List<CidadeIbge>

    companion object {
        fun create(): IbgeService {

            val json = Json {
                ignoreUnknownKeys = true
                isLenient = true
            }

            val contentType = "application/json".toMediaType()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://servicodados.ibge.gov.br/api/v1/")
                .addConverterFactory(json.asConverterFactory(contentType))
                .build()

            return retrofit.create(IbgeService::class.java)
        }
    }
}



fun formatDateAndTime(iso: String?): String? {
    if (iso == null) return null
    return iso.replace("T", " ").substring(0, 16)
}



object BrazilRegion {
    val UFS: List<String> = listOf(
        "AC","AL","AP","AM","BA","CE","DF","ES","GO","MA",
        "MT","MS","MG","PA","PB","PR","PE","PI","RJ","RN",
        "RS","RO","RR","SC","SP","SE","TO"
    )
}
