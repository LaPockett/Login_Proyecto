package com.example.login_proyecto

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.login_proyecto.databinding.ActivityMainBinding
import com.example.login_proyecto.databinding.ActivitySignInBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class SignInActivity : AppCompatActivity() {

    // Firebase Auth
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var binding: ActivitySignInBinding
    // Botón, email y contraseña
    lateinit var buttonLogin : Button
    lateinit var textInputEmail: EditText
    lateinit var textInputPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this) // Inicialización de Firebase
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        //setContentView(R.layout.activity_sign_in)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        firebaseAuth = FirebaseAuth.getInstance()

        buttonLogin = binding.buttonSignUp
        textInputEmail = binding.textInputEmailLogin
        textInputPassword = binding.textInputPasswordLogin
    }

    fun onLogin(view: View) {
        val email:String = binding.textInputEmailLogin.text.toString().trim()
        val password : String = binding.textInputPasswordLogin.text.toString().trim()
        Log.i(TAG, "Email ${email}, Password ${password}")
        if (email.isNotEmpty()&&password.isNotEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    it.exception?.let { exception ->
                        when (exception) {
                            is FirebaseAuthUserCollisionException ->
                                Toast.makeText(this, "El correo ya está registrado", Toast.LENGTH_SHORT).show()
                            is FirebaseAuthInvalidCredentialsException ->
                                Toast.makeText(this, "La contraseña no es correcta", Toast.LENGTH_SHORT).show()
                            else ->
                                Toast.makeText(this, exception.localizedMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        }else {
            Toast.makeText(this,"Empty fields are not allowed !!", Toast.LENGTH_SHORT).show()
        }
    }
}