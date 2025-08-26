package com.example.appfirebase.pages

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.appfirebase.AuthState
import com.example.appfirebase.AuthViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

data class Cliente(
    val nome: String = "",
    val email: String = "",
    val telefone: String = "",
    val mensagem: String = ""
)

@Composable
fun CadastrarCliente(modifier: Modifier = Modifier, navController: NavController) {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var mensagem by remember { mutableStateOf("") }
    val db = Firebase.firestore
    var clientes by remember { mutableStateOf<List<Cliente>>(emptyList()) }

    Box(modifier = Modifier.fillMaxSize()) {
        Rodape()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Cadastro do Cliente",
                        fontSize = 28.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFFD21656)
                    )

                    OutlinedTextField(
                        value = nome,
                        onValueChange = { nome = it },
                        label = { Text("Nome") },
                        modifier = Modifier.fillMaxWidth()
                    )


                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = telefone,
                        onValueChange = { telefone = it },
                        label = { Text("Telefone") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = mensagem,
                        onValueChange = { mensagem = it },
                        label = { Text("Mensagem") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = { val cliente = hashMapOf(
                            "nome" to nome,
                            "email" to email,
                            "telefone" to telefone,
                            "mensagem" to mensagem

                        )

                            db.collection("clientes")
                            .add(cliente)
                            .addOnSuccessListener {
                                nome = ""
                                email = ""
                                telefone = ""
                                mensagem = ""
                                db.collection("clientes")
                                    .get()
                                    .addOnSuccessListener { result ->
                                        clientes = result.map { doc ->
                                            Cliente(
                                                nome = doc.getString("nome") ?: "",
                                                email = doc.getString("email") ?: "",
                                                telefone = doc.getString("telefone") ?: "",
                                                mensagem = doc.getString("mensagem") ?: ""
                                            )
                                        }
                                    }}},
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cadastrar")
                    }

                }
            }
        }
    }
}


