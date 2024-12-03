package com.example.quizfirebase

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizfirebase.databinding.ActivityRegistrationBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class RegistrationActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: ActivityRegistrationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()
        binding.bttnRegister.setOnClickListener { registerNewUser() }
    }

    private fun registerNewUser() {
        binding.progressbar.visibility = View.VISIBLE
        val email: String = binding.editTxtEmail.text.toString()
        val password: String = binding.editTxtPassw.text.toString()
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Please enter email!!", Toast.LENGTH_LONG).show()
            return
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, "Please enter password!!", Toast.LENGTH_LONG).show()
            return
        }
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    Toast.makeText(this@RegistrationActivity, "Registration successful!", Toast.LENGTH_LONG).show()
                    binding.progressbar.visibility = View.GONE
                    startActivity(Intent(this@RegistrationActivity, MainActivity::class.java))
                } else {
                    Toast.makeText(this@RegistrationActivity, "Registration failed!!" + " Please try again later", Toast.LENGTH_LONG).show()
                    binding.progressbar.visibility = View.GONE
                }
            }
    }
}