package com.example.pathfindergo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pathfindergo.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    // This holds the current status of the Auth process
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    // Check if user is already logged in on app start
    val currentUser: FirebaseUser? get() = repository.currentUser

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = repository.signUp(email, password)
            result.onSuccess { user ->
                _authState.value = AuthState.Success(user?.email ?: "")
            }.onFailure { error ->
                _authState.value = AuthState.Error(error.message ?: "Signup Failed")
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = repository.login(email, password)
            result.onSuccess { user ->
                _authState.value = AuthState.Success(user?.email ?: "")
            }.onFailure { error ->
                _authState.value = AuthState.Error(error.message ?: "Login Failed")
            }
        }
    }

    fun logout() {
        repository.logout()
        _authState.value = AuthState.Idle
    }
}