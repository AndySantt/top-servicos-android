package com.top.example.settings

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.top.example.net.ApiProvider
import androidx.compose.ui.platform.LocalContext

@Composable
fun DeleteAccountDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onSuccess: () -> Unit,
) {
    if (!show) return

    val ctx = LocalContext.current
    val api = remember { ApiProvider.get(ctx) }
    val vm: SettingsViewModel = viewModel(factory = SettingsVmFactory(api))

    var deletePwd by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { if (!vm.deleting) onDismiss() },
        title = { Text("Excluir minha conta") },
        text = {
            Column {
                Text("Para confirmar, digite sua senha. Essa ação não pode ser desfeita.")
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = deletePwd,
                    onValueChange = { deletePwd = it },
                    singleLine = true,
                    label = { Text("Senha") },
                    visualTransformation = PasswordVisualTransformation()
                )
                vm.error?.let {
                    Spacer(Modifier.height(6.dp))
                    Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
            }
        },
        confirmButton = {
            TextButton(
                enabled = deletePwd.isNotBlank() && !vm.deleting,
                onClick = {
                    vm.deleteAccount(deletePwd) {
                        deletePwd = ""
                        onDismiss()
                        onSuccess()
                    }
                }
            ) { Text(if (vm.deleting) "Excluindo..." else "Excluir") }
        },
        dismissButton = {
            TextButton(enabled = !vm.deleting, onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
