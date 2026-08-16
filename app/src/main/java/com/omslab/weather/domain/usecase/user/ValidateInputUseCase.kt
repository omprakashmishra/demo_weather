package com.omslab.weather.domain.usecase.user

import com.omslab.weather.common.util.UtilsKt
import javax.inject.Inject

class ValidateInputUseCase @Inject constructor() {
    operator fun invoke(
        userName: String,
        emailAddress: String,
        password: String,
        confirmPassword: String,
        isLogin: Boolean
    ): ValidationResult {
        if (!isLogin && !UtilsKt.isValidName(userName)) {
            return ValidationResult(false, "Enter valid name")
        }
        if (!UtilsKt.isValidEmail(emailAddress)) {
            return ValidationResult(false, "Email is invalid")
        }
        if (!isLogin && (!UtilsKt.isValidPass(password) || !UtilsKt.isValidPass(confirmPassword) || password != confirmPassword)) {
            return ValidationResult(false, "Password is not valid or doesn't match")
        }
        if (isLogin && !UtilsKt.isValidPass(password)) {
            return ValidationResult(false, "Password is not valid")
        }
        return ValidationResult(true, "")
    }
}

data class ValidationResult(
    val isValid: Boolean,
    val message: String
)