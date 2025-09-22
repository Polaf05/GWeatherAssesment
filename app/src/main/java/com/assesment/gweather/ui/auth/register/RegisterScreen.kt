package com.assesment.gweather.ui.auth.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.assesment.gweather.ui.model.AuthUiState
import com.google.firebase.auth.FirebaseUser

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    onRegisterSuccess: (FirebaseUser) -> Unit,
    onNavigateBack: () -> Unit
) {
    val authState by viewModel.registerState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val isValid =
        email.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                password.length >= 6

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Create Account", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = if (it.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(it)
                        .matches()
                ) {
                    "Invalid email"
                } else null
            },
            label = { Text("Email") },
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            isError = emailError != null,
            modifier = Modifier.fillMaxWidth()
        )
        emailError?.let {
            Text(
                it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError =
                    if (it.length < 6) "Password must be at least 6 characters" else null
            },
            label = { Text("Password") },
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            isError = passwordError != null,
            modifier = Modifier.fillMaxWidth()
        )
        passwordError?.let {
            Text(
                it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Button(
            onClick = { viewModel.register(email, password) },
            enabled = isValid && authState !is AuthUiState.Loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (authState is AuthUiState.Loading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text("Register")
            }
        }

        if (authState is AuthUiState.Error) {
            Text(
                (authState as AuthUiState.Error).message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        TextButton(onClick = onNavigateBack) {
            Text("Already have an account? Login")
        }
    }

    LaunchedEffect(authState) {
        if (authState is AuthUiState.Success) {
            onRegisterSuccess((authState as AuthUiState.Success).user)
        }
    }
}
