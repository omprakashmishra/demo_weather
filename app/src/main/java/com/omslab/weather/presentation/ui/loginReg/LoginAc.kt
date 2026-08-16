package com.omslab.weather.presentation.ui.loginReg

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.omslab.weather.databinding.ActivityLoginBinding
import com.omslab.weather.presentation.ui.dashboard.ActivityDashboard
import com.omslab.weather.presentation.viewmodel.LoginViewModel
import com.omslab.weather.common.util.Constants.PrimaryEmail
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginAc : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    val viewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        binding.submitLogin.setOnClickListener {
            insertTb()
        }
        binding.screenSignIn.setOnClickListener {
            navigate(2)
        }

        viewModel.userList.observe(this) {
            if(it.size==1)
                navigate(4)
            else
                viewModel.validateMessage("Please register and continue")
        }
        viewModel.message.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }
    }

    private fun insertTb() {
        val email=binding.etEmail.text.toString()
        val pass=binding.etPassword.text.toString()
        viewModel.loginValidate(email,pass)
    }

    var mContext:Context = this@LoginAc
    fun navigate(flag:Int){
        var intent: Intent?=null
        when (flag) {
            1 -> {
                intent= Intent(mContext, LoginAc::class.java)
            }
            2 -> {
                intent= Intent(mContext, RegisterAc::class.java)
            }
            4 -> {
                intent= Intent(mContext, ActivityDashboard::class.java)
                viewModel.preference.setString(PrimaryEmail,binding.etEmail.text.toString())
            }
        }
        intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        finish()
        startActivity(intent)
    }


}


