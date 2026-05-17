package com.top.example.util

fun mapError(code: Int): String {
    return when (code) {
        400 -> "Dados inválidos"
        401 -> "Sessão expirada"
        404 -> "Não encontrado"
        409 -> "Já cadastrado"
        500 -> "Erro no servidor"
        else -> "Erro inesperado"
    }
}