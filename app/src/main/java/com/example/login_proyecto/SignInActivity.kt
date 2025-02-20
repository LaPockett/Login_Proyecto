package com.example.login_proyecto

import android.R
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.login_proyecto.databinding.ActivitySignInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider


class SignInActivity : AppCompatActivity() {

    // Firebase Auth
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var binding: ActivitySignInBinding
    // Botón, email y contraseña
    lateinit var buttonLogin : Button
    lateinit var buttonLoginWithGoogle : Button
    lateinit var textInputEmail: EditText
    lateinit var textInputPassword: EditText

    private lateinit var googleSignInClient: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this) // Inicialización de Firebase
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        //setContentView(R.layout.activity_sign_in)

        firebaseAuth = FirebaseAuth.getInstance()
        buttonLoginWithGoogle= binding.buttonLoginWithGoogle

        buttonLogin = binding.buttonLogin
        textInputEmail = binding.textInputEmailLogin
        textInputPassword = binding.textInputPasswordLogin

        // Configuración de Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(com.example.login_proyecto.R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        binding.buttonLoginWithGoogle.setOnClickListener { signInWithGoogle() }


    }

    /**
     * Inicia sesión con Google.
     */
    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    /**
     * Maneja el resultado del inicio de sesión con Google.
     */
    private companion object {
        const val RC_SIGN_IN = 1001
    }

    /**
     * Maneja el resultado de la actividad de inicio de sesión con Google.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Autentica al usuario con credenciales de Google.
     */
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Inicio de sesión exitoso
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Authentication Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
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

    fun onSigInWithGoogle(view: View) {}
}