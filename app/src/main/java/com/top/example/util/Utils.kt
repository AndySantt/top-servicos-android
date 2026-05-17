package com.top.example.util

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.top.example.net.TipoPro


fun String?.withBust(stamp: Long): String? =
    this?.let { if (stamp == 0L) it else it + (if ('?' in it) "&" else "?") + "cb=$stamp" }


fun String?.toAbsolute(baseUrl: String): String? = this?.let {
    if (it.startsWith("http", ignoreCase = true)) it
    else baseUrl.trimEnd('/') + "/" + it.trimStart('/')
}




 val PinkBadge = Color(0xFF6A1B9A)
 val GreenBadge = Color(0xFF2E7D32)
 val BlueBadge = Color(0xFF1565C0)
 val PurpleBadge = Color(0xFF7B1FA2)
 val BluePrimary = Color(0xFF1565C0)
 val BlueSecondary = Color(0xFF42A5F5)
 val BlueTertiary = Color(0xFF90CAF9)
 val OnPrimary = Color(0xFFFFFFFF)
 val VerifiedGreen = Color(0xFF2E7D32)
 val YelowBadge = Color(0xf6e130)


fun labelFromTipo(tipo: TipoPro?): String = when (tipo) {
    TipoPro.DIARISTA -> "Diarista"
    TipoPro.FAXINEIRA -> "Faxineira"
    TipoPro.REFRIGERISTA -> "Refrigeração"
    TipoPro.PEDREIRO -> "Pedreiro"
    TipoPro.ELETRICISTA -> "Eletricista"
    TipoPro.COZINHEIRA -> "Cozinheira"
    TipoPro.GARCOM -> "Garçom"
    TipoPro.BARBEIRO -> "Barbeiro"
    TipoPro.CABELEIREIRA -> "Cabeleireira"
    TipoPro.CONFEITEIRA -> "Confeiteira"
    TipoPro.CARPINTEIRO -> "Marceneiro"
    TipoPro.PUBLICIDADE -> "Publicidade"
    TipoPro.AJUDANTE -> "Ajudante"
    TipoPro.MANICURE -> "Manicure"
    TipoPro.PINTOR -> "Pintor"
    TipoPro.ENTREGADOR -> "Entregador"
    TipoPro.JARDINEIRO -> "Jardineiro"
    TipoPro.MECANICO -> "Mecânico"
    TipoPro.ENCANADOR -> "Encanador"
    TipoPro.ESTETICA -> "Estética"
    TipoPro.PERSONAL -> "Personal"
    TipoPro.GESSEIRO -> "Gesseiro"
    TipoPro.SERRALHEIRO -> "Serralheiro"
    TipoPro.MARIDO_DE_ALUGUEL -> "Marido de Aluguel"
    TipoPro.INST_TRANSITO -> "Instrutor de Transito"
    TipoPro.ALPINISTA -> "Alpinista"
    TipoPro.MASSAGISTA -> "Massagista"
    TipoPro.ASSISTENCIA -> "Assistência Tecnica"
    TipoPro.EVENTOS -> "Festas e Eventos"
    TipoPro.FOTOGRAFO -> "Fotografo(a)"
    TipoPro.BUFFET -> "Buffet"
    TipoPro.DECORACAO -> "Decoração"
    TipoPro.BARTENDERS -> "Bartender"
    TipoPro.DESIGNER -> "Designer Digital"
    TipoPro.TECNOLOGIA -> "Tecnologia da Informação"
    TipoPro.MONTADORDEMOVEIS -> "Montador de Móveis"
    TipoPro.NUTRICIONISTA -> "Nutricionista"
    TipoPro.PSICOLOGO -> "Psicólogo(a)"
    TipoPro.DENTISTA -> "Dentista"
    TipoPro.GUINCHO -> "Guincho"
    TipoPro.CONTADOR -> "Contador"
    TipoPro.ADVOGADO -> "Advogado"
    TipoPro.VETERINARIO -> "Veterinário"
    TipoPro.PETSHOP -> "Petshop"
    TipoPro.MUDANCAS -> "Fretes/Mudanças"
    TipoPro.TATUADOR -> "Tatuador"
    TipoPro.SOLDADOR -> "Soldador"
    TipoPro.SEGURANCA -> "Segurança"
    TipoPro.TAXISTA -> "Taxista"
    TipoPro.LAVA_JATO -> "Lava Carros"
    TipoPro.DOCEIRA -> "Doceira"
    TipoPro.DESPACHANTE -> "Despachante"
    TipoPro.DEPILACAO-> "Depilação"
    TipoPro.CUIDADOR-> "Cuidador(a)"
    TipoPro.ACOMPANHANTE-> "Acompanhante Hospitalar"
    TipoPro.DOULA-> "Doula"
    TipoPro.GRAFICA-> "Gráfica"
    TipoPro.QUIROPRAXISTA-> "Quiropraxia"
    TipoPro.FISIOTERAPEUTA-> "Fisioterapia"
    TipoPro.VENDEDOR-> "Vendedor(a)"
    TipoPro.ENGENHEIRO-> "Engenheiro(a)"
    TipoPro.DESENTUPIDORA -> "Desentupidora"




    null -> "Profissional"
}


fun badgeFromTipo(tipo: TipoPro?): Color = when (tipo) {

    // limpeza / alimentação / saúde
    TipoPro.DIARISTA,
    TipoPro.COZINHEIRA,
    TipoPro.CONFEITEIRA,
    TipoPro.PERSONAL,
    TipoPro.NUTRICIONISTA,
    TipoPro.TATUADOR,
    TipoPro.DOCEIRA,
    TipoPro.FAXINEIRA,
    TipoPro.QUIROPRAXISTA,
    TipoPro.FISIOTERAPEUTA,
    TipoPro.DOULA,
    TipoPro.MASSAGISTA -> PinkBadge

    // construção / casa
    TipoPro.PEDREIRO,
    TipoPro.ELETRICISTA,
    TipoPro.CARPINTEIRO,
    TipoPro.AJUDANTE,
    TipoPro.PINTOR,
    TipoPro.JARDINEIRO,
    TipoPro.ENCANADOR,
    TipoPro.MARIDO_DE_ALUGUEL,
    TipoPro.SOLDADOR,
    TipoPro.SEGURANCA,
    TipoPro.CUIDADOR,
    TipoPro.VENDEDOR,
    TipoPro.MONTADORDEMOVEIS -> GreenBadge

    // técnico / mecânica
    TipoPro.REFRIGERISTA,
    TipoPro.ALPINISTA,
    TipoPro.MECANICO,
    TipoPro.GESSEIRO,
    TipoPro.SERRALHEIRO,
    TipoPro.GUINCHO,
    TipoPro.ENGENHEIRO,
    TipoPro.ACOMPANHANTE,
    TipoPro.DESENTUPIDORA,
    TipoPro.ASSISTENCIA -> BlueBadge

    // beleza / serviços pessoais
    TipoPro.PUBLICIDADE,
    TipoPro.INST_TRANSITO,
    TipoPro.ESTETICA,
    TipoPro.BARBEIRO,
    TipoPro.MANICURE,
    TipoPro.CABELEIREIRA,
    TipoPro.PSICOLOGO,
    TipoPro.DEPILACAO,
    TipoPro.GRAFICA,
    TipoPro.DENTISTA -> PurpleBadge

    // eventos
    TipoPro.EVENTOS,
    TipoPro.BUFFET,
    TipoPro.BARTENDERS,
    TipoPro.DECORACAO,
    TipoPro.FOTOGRAFO -> Color(0xFFFF6F00)

    // tecnologia
    TipoPro.DESIGNER,
    TipoPro.TECNOLOGIA,
    TipoPro.LAVA_JATO-> Color(0xFF00ACC1)

    // profissionais liberais
    TipoPro.ADVOGADO,
    TipoPro.CONTADOR,
    TipoPro.DESPACHANTE-> Color(0xFF5E35B1)

    // pet
    TipoPro.VETERINARIO,
    TipoPro.PETSHOP -> Color(0xFF43A047)

    // logística
    TipoPro.ENTREGADOR,
    TipoPro.MUDANCAS,
    TipoPro.TAXISTA,
    TipoPro.GARCOM -> YelowBadge

    null -> PurpleBadge
}

@Composable
 fun RolePill(
    text: String,
    bg: Color,
) {
    Surface(
        color = bg,
        shape = RoundedCornerShape(999.dp),
        border = BorderStroke(1.dp, bg.copy(alpha = 0.35f))
    ) {
        Text(
            text = text,
            color = Color.White,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

 fun bust(url: String?, version: String?): String? {
    if (url.isNullOrBlank()) return url
    if (version.isNullOrBlank()) return url
    val sep = if ('?' in url) '&' else '?'
    return "$url${sep}b=${version.hashCode()}"
}

 val Context.seenStore by preferencesDataStore("patient_seen_prefs")

 val SEEN_ACCEPTED_KEY   = stringSetPreferencesKey("seen_accepted")
 val RATED_BY_ME_KEY     = stringSetPreferencesKey("rated_by_me")
 val PROMPTED_BY_ME_KEY  = stringSetPreferencesKey("prompted_by_me")

// região (paciente)
 val REGION_UF_KEY       = stringPreferencesKey("region_uf")
 val REGION_CIDADE_KEY   = stringPreferencesKey("region_cidade")



 const val TERMS_VERSION = "1.0"


fun formatCpfCnpj(value: String): String {
    val digits = value.filter { it.isDigit() }

    return if (digits.length <= 11) {
        buildString {
            digits.forEachIndexed { i, c ->
                when (i) {
                    3, 6 -> append('.')
                    9 -> append('-')
                }
                append(c)
            }
        }
    } else {
        buildString {
            digits.forEachIndexed { i, c ->
                when (i) {
                    2, 5 -> append('.')
                    8 -> append('/')
                    12 -> append('-')
                }
                append(c)
            }
        }
    }
}