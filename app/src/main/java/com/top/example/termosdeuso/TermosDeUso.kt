@file:OptIn(ExperimentalMaterial3Api::class)


package com.top.example.termosdeuso




import androidx.compose.foundation.layout.*

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp

import androidx.compose.material.icons.Icons

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

import androidx.compose.runtime.getValue

import androidx.compose.runtime.mutableStateOf

import androidx.compose.material.icons.filled.Close

import androidx.compose.runtime.Composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height

import androidx.compose.material3.Text

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button

import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.saveable.rememberSaveable



@Composable
fun TermsScreen(
    onAccept: () -> Unit,
    onClose: () -> Unit
) {
    var accepted by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Termos de Uso") },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.Close, contentDescription = null)
                    }
                }
            )
        },
        bottomBar = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = accepted,
                        onCheckedChange = { accepted = it }
                    )
                    Text("Li e concordo com os termos")
                }

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = onAccept,
                    enabled = accepted,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Continuar")
                }
            }
        }
    ) { pad ->

        Column(
            modifier = Modifier
                .padding(pad)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Text(
                "TERMOS DE USO – TOP SERVIÇOS",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(12.dp))

            Text("""

Versão 1.0 – 2026

1. OBJETO
A plataforma Top Serviços atua exclusivamente como intermediadora digital, permitindo a conexão entre usuários contratantes e profissionais independentes.

2. AUSÊNCIA DE VÍNCULO
A plataforma não possui qualquer vínculo empregatício, societário ou de representação com os profissionais cadastrados.

3. RESPONSABILIDADE EXCLUSIVA DOS USUÁRIOS
Toda negociação, contratação e execução de serviços ocorre exclusivamente entre os usuários, sendo estes integralmente responsáveis por seus atos.

4. ISENÇÃO DE RESPONSABILIDADE
A plataforma não se responsabiliza, em nenhuma hipótese, por:
- danos materiais ou morais
- prejuízos financeiros
- condutas ilícitas (furtos, agressões, fraudes)
- serviços mal executados
- descumprimento de acordos

5. RISCO DA CONTRATAÇÃO
O usuário declara estar ciente de que a contratação de terceiros envolve riscos, assumindo integral responsabilidade por suas escolhas.

6. VERIFICAÇÃO DE USUÁRIOS
A plataforma poderá realizar verificações, mas não garante a veracidade das informações fornecidas.

7. CONDUTA
É proibido:
- praticar crimes
- usar dados falsos
- assediar ou agredir usuários

8. PENALIDADES
A plataforma pode suspender ou excluir contas sem aviso prévio.

9. LIMITAÇÃO DE RESPONSABILIDADE
A responsabilidade da plataforma limita-se ao funcionamento do sistema, não abrangendo relações entre usuários.

10. ACEITE
Ao prosseguir, o usuário declara ter lido, compreendido e aceito integralmente estes termos.
            """.trimIndent())

            Spacer(Modifier.height(80.dp))
        }
    }
}