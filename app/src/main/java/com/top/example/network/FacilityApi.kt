package com.top.example.network

import kotlinx.serialization.Serializable
import retrofit2.http.*

@Serializable
data class SignupReq(
    val nome: String,
    val email: String,
    val senha: String,
    val role: String,
    val uf: String? = null,
    val cidade: String? = null,
    val cpf: String? = null
)
@Serializable data class SignupRes(val userId: String)
@Serializable data class LoginReq(val email: String, val senha: String)
@Serializable data class LoginRes(val token: String)
@Serializable data class MeRes(val uid: String, val role: String)

interface FacilityApi {
    @POST("/auth/signup")
    suspend fun signup(@Body body: SignupReq): SignupRes

    @POST("/auth/login")
    suspend fun login(@Body body: LoginReq): LoginRes

    @GET("/me")
    suspend fun me(@Header("Authorization") bearer: String): MeRes
}
