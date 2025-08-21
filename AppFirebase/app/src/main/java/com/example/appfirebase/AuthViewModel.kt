package com.example.appfirebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    private val _userEmail = MutableLiveData<String>()
    val userEmail: LiveData<String> = _userEmail

    init { refreshUser() }

    private fun refreshUser() {
        val user = auth.currentUser
        if (user == null) {
            _authState.value = AuthState.Unauthenticated
            _userEmail.value = ""
        } else {
            _authState.value = AuthState.Authenticated
            _userEmail.value = user.email
        }
    }

    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email ou senha não podem ser vazios")
            return
        }
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) refreshUser()
                else _authState.value = AuthState.Error(task.exception?.message ?: "Erro ao logar")
            }
    }

    fun signup(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email ou senha não podem ser vazios")
            return
        }
        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) refreshUser()
                else _authState.value = AuthState.Error(task.exception?.message ?: "Erro ao criar usuário")
            }
    }

    fun signout() {
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
        _userEmail.value = ""
    }

    fun updateEmail(newEmail: String, currentPassword: String) {
        val user = auth.currentUser ?: return
        val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)

        user.reauthenticate(credential).addOnCompleteListener { reauthTask ->
            if (reauthTask.isSuccessful) {
                user.updateEmail(newEmail).addOnCompleteListener { updateTask ->
                    if (updateTask.isSuccessful) refreshUser()
                    else _authState.value =
                        AuthState.Error(updateTask.exception?.message ?: "Erro ao atualizar email")
                }
            } else {
                _authState.value =
                    AuthState.Error(reauthTask.exception?.message ?: "Erro na reautenticação")
            }
        }
    }

    fun deleteAccount(currentPassword: String) {
        val user = auth.currentUser ?: return
        val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)

        user.reauthenticate(credential).addOnCompleteListener { reauthTask ->
            if (reauthTask.isSuccessful) {
                user.delete().addOnCompleteListener { deleteTask ->
                    if (deleteTask.isSuccessful) {
                        _authState.value = AuthState.Unauthenticated
                        _userEmail.value = ""
                    } else {
                        _authState.value =
                            AuthState.Error(deleteTask.exception?.message ?: "Erro ao excluir conta")
                    }
                }
            } else {
                _authState.value =
                    AuthState.Error(reauthTask.exception?.message ?: "Erro na reautenticação")
            }
        }
    }
}

sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}
