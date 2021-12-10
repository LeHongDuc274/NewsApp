package com.example.newsapp.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.Contains.IS_BACKGROUND
import com.example.newsapp.Contains.KEY_GET_BOOL

import com.example.newsapp.databinding.ActivityLoginBinding
import com.example.newsapp.ui.viewmodels.LoginViewModel

class LoginActivity : AppCompatActivity() {
    lateinit private var binding: ActivityLoginBinding
    lateinit private var viewmodel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        viewmodel = ViewModelProvider(this)[LoginViewModel::class.java]
        setContentView(binding.root)
        val isBackGround = intent.getBooleanExtra(IS_BACKGROUND, false)
        initView(viewmodel.getRefs())
        initControl(isBackGround)
        observeMessage()
    }

    private fun observeMessage() {
        viewmodel.message.observe(this) { mess ->
            mess?.let {
                Toast.makeText(this, mess, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initControl(isBackGround: Boolean) {
        binding.btnLogin.setOnClickListener {
            val text = binding.edtPassword.text.toString()
            if (viewmodel.checkPassCode(text)) openMainActivity(isBackGround)
        }

        binding.btnSave.setOnClickListener {
            val text = binding.edtPassword.text.toString()
            if (viewmodel.saveRefs(text)) {
                openMainActivity(isBackGround)
            }
        }
    }

    private fun openMainActivity(isBackGround: Boolean) {
        if (isBackGround) {
            val sharedPrefs =
                this.getSharedPreferences(IS_BACKGROUND, Context.MODE_PRIVATE) ?: null
            val bool = sharedPrefs?.edit()
            bool?.putBoolean(KEY_GET_BOOL, false)
            bool?.apply()
            finish()
        } else {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun initView(oldPass: String?) {
        if (oldPass == null) {
            binding.btnLogin.visibility = View.GONE
            binding.btnSave.visibility = View.VISIBLE
            binding.tvMessage.visibility = View.VISIBLE
        } else {
            binding.btnLogin.visibility = View.VISIBLE
            binding.btnSave.visibility = View.GONE
            binding.tvMessage.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}