package com.example.login_proyecto

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
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
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class MainActivity : AppCompatActivity() {

    // Firebase Auth
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var binding: ActivityMainBinding

    // Botón, email y contraseñas
    lateinit var buttonSignUp : Button
    lateinit var textInputEmail: EditText
    lateinit var textInputPassword: EditText
    lateinit var textInputPassword2: EditText
    lateinit var textViewLogin : TextView


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this) // Inicialización de Firebase
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        //setContentView(R.layout.activity_main)
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }*/

        firebaseAuth = FirebaseAuth.getInstance()

        buttonSignUp = binding.buttonSignUp
        textInputEmail = binding.textInputEmail
        textInputPassword = binding.textInputPassword
        textInputPassword2 = binding.textInputPassword2
        textViewLogin = binding.textViewLogin

        /*buttonSignUp.setOnClickListener {

        }*/

        textViewLogin.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

    }

    fun onRegistro(view: View) {
        val email:String = binding.textInputEmail.text.toString().trim()
        val password : String = binding.textInputPassword.text.toString().trim()
        val password2 : String = binding.textInputPassword2.text.toString().trim()
        Log.i(TAG, "Email ${email}, Password ${password}, Password2 ${password2},")
        if (email.isNotEmpty()&&password.isNotEmpty()&&password2.isNotEmpty()) {
            if (password.equals(password2)) {
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, SignInActivity::class.java)
                        startActivity(intent)
                    } else {
                        it.exception?.let { exception ->
                            when (exception) {
                                is FirebaseAuthUserCollisionException ->
                                    Toast.makeText(this, "El correo ya está registrado", Toast.LENGTH_SHORT).show()
                                is FirebaseAuthWeakPasswordException ->
                                    Toast.makeText(this, "La contraseña es muy débil", Toast.LENGTH_SHORT).show()
                                else ->
                                    Toast.makeText(this, exception.localizedMessage, Toast.LENGTH_SHORT).show()
                            }
                        }                    }
                }
            } else {
                Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
            }

        }else {
            Toast.makeText(this,"Empty fields are not allowed !!", Toast.LENGTH_SHORT).show()
        }
    }
}