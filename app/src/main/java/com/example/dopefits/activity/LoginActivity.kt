package com.example.dopefits.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dopefits.R
import com.example.dopefits.ui.home.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var loginButton: Button
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        loginButton = findViewById(R.id.login_button)
        emailEditText = findViewById(R.id.login_email)
        passwordEditText = findViewById(R.id.login_password)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            if (validateInput(email, password)) {
                loginUser(email, password)
            }
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            emailEditText.error = "Email is required"
            emailEditText.requestFocus()
            return false
        }
        if (password.isEmpty()) {
            passwordEditText.error = "Password is required"
            passwordEditText.requestFocus()
            return false
        }
        return true
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        checkAndInitializeCartNode(userId)
                    }
                } else {
                    Toast.makeText(baseContext, "Login failed. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun checkAndInitializeCartNode(userId: String) {
        val cartRef = database.child("users").child(userId).child("Cart")
        cartRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (!task.result.exists()) {
                    cartRef.setValue(emptyMap<String, Any>()).addOnCompleteListener { cartTask ->
                        if (cartTask.isSuccessful) {
                            navigateToHome()
                        } else {
                            Toast.makeText(baseContext, "Failed to initialize cart.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    navigateToHome()
                }
            } else {
                Toast.makeText(baseContext, "Failed to check cart.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}