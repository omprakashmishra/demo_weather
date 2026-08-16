package com.omslab.weather.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omslab.weather.common.MySharedPreference
import com.omslab.weather.common.util.Constants
import com.omslab.weather.domain.models.User
import com.omslab.weather.domain.usecase.user.UserUseCase
import com.omslab.weather.domain.usecase.user.ValidateInputUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val preference: MySharedPreference,
    private val userUseCase: UserUseCase,
    private val validateInputUseCase: ValidateInputUseCase
) : ViewModel() {

    private val _userList = MutableLiveData<List<User>>()
    val userList: LiveData<List<User>> get() = _userList

    private val _isRegister = MutableLiveData<Boolean>()
    val isRegister: LiveData<Boolean> get() = _isRegister

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    fun validateMessage(message: String) {
        _message.postValue(message)
    }

    fun loginValidate(email: String, password: String) {
        val validationResult = validateInputUseCase("", email, password, password, true)
        if (!validationResult.isValid) {
            _message.postValue(validationResult.message)
            return
        }

        viewModelScope.launch {
            userUseCase.loginUser(email, password).collect { users ->
                _userList.postValue(users)
                if (users.isEmpty()) {
                    _message.postValue("Please register and continue")
                }
            }
        }
    }

    fun registerValidate(name: String, email: String, pass: String, cPass: String) {
        val validationResult = validateInputUseCase(name, email, pass, cPass, false)
        if (!validationResult.isValid) {
            _message.postValue(validationResult.message)
            return
        }

        val user = User(name = name, email = email, password = pass)
        registerUser(user)
    }

    private fun registerUser(user: User) = viewModelScope.launch {
        val result = userUseCase.registerUser(user)
        result.fold(
            onSuccess = { userId ->
                if (userId > 0) {
                    _userList.postValue(listOf(user))
                    _isRegister.postValue(true)
                    preference.setString(Constants.PrimaryEmail, user.email)
                } else {
                    _isRegister.postValue(false)
                    _message.postValue("Registration failed")
                }
            },
            onFailure = { error ->
                _isRegister.postValue(false)
                _message.postValue(error.message ?: "Registration failed")
            }
        )
    }
}