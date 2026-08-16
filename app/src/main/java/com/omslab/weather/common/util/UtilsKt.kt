package com.omslab.weather.common.util

object UtilsKt {
    private val NAME_REGEX = Regex(
        "^[a-zA-Z\\\\s](?=\\S+$).{2,20}$"
    )
    private val EMAIL_REGEX = Regex(
        "^([0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-\\w]*[0-9a-zA-Z]\\.)+[a-zA-Z]{2,9})$"
    )

    private val PASSWORD_REGEX = Regex(
        "^(?=.*[0-9]).{2,16}\$"
    )

    fun isValidName(target: CharSequence?): Boolean {
        return target.let {
            NAME_REGEX.matches(target!!)
        }
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return target.let {
            EMAIL_REGEX.matches(target!!)
        }
    }

    fun isValidPass(target: CharSequence?): Boolean {
        return target.let {
            PASSWORD_REGEX.matches(target!!)
        }

    }

}

