package com.example.newsapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider

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
        initView(viewmodel.getRefs())
        initControl()
        observeMessage()
    }

    private fun observeMessage() {
        viewmodel.message.observe(this) { mess ->
            mess?.let {
                Toast.makeText(this, mess, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initControl() {
        binding.btnLogin.setOnClickListener {
            val text = binding.edtPassword.text.toString()
            if (viewmodel.checkPassCode(text)) openMainActivity()
        }

        binding.btnSave.setOnClickListener {
            val text = binding.edtPassword.text.toString()
            if (viewmodel.saveRefs(text)) {
                openMainActivity()
            }
        }
    }

    private fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
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

}