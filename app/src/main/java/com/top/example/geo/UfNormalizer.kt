
package com.top.example.geo

private val NAMES = mapOf(
    "ACRE" to "AC",
    "ALAGOAS" to "AL",
    "AMAPA" to "AP", "AMAPÁ" to "AP",
    "AMAZONAS" to "AM",
    "BAHIA" to "BA",
    "CEARA" to "CE", "CEARÁ" to "CE",
    "DISTRITOFEDERAL" to "DF", "BRASILIA" to "DF", "BRASÍLIA" to "DF",
    "ESPIRITOSANTO" to "ES", "ESPÍRITOSANTO" to "ES",
    "GOIAS" to "GO", "GOIÁS" to "GO",
    "MARANHAO" to "MA", "MARANHÃO" to "MA",
    "MATOGROSSO" to "MT",
    "MATOGROSSODOSUL" to "MS",
    "MINASGERAIS" to "MG",
    "PARA" to "PA", "PARÁ" to "PA",
    "PARAIBA" to "PB", "PARAÍBA" to "PB",
    "PARANA" to "PR", "PARANÁ" to "PR",
    "PERNAMBUCO" to "PE",
    "PIAUI" to "PI", "PIAUÍ" to "PI",
    "RIODEJANEIRO" to "RJ",
    "RIOGRANDEDONORTE" to "RN",
    "RIOGRANDEDOSUL" to "RS",
    "RONDONIA" to "RO", "RONDÔNIA" to "RO",
    "RORAIMA" to "RR",
    "SANTACATARINA" to "SC",
    "SAOPAULO" to "SP", "SÃOPAULO" to "SP",
    "SERGIPE" to "SE",
    "TOCANTINS" to "TO"
)

fun normalizeUf(input: String): String? {
    val s = input.trim().uppercase()
    if (s.length == 2) return s
    val norm = s
        .replace("[\\s\\-_.]".toRegex(), "")
        .replace("Á", "A").replace("Â", "A").replace("Ã", "A")
        .replace("É", "E").replace("Ê", "E")
        .replace("Í", "I")
        .replace("Ó", "O").replace("Ô", "O").replace("Õ", "O")
        .replace("Ú", "U")
        .replace("Ç", "C")
    return NAMES[norm]
}
