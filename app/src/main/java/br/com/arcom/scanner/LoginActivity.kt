package br.com.arcom.scanner

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import br.com.arcom.scanner.databinding.LoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginBinding
    private lateinit var viewModel: LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        val idUsuario = binding.inputMatricula
        val senha = binding.inputSenha

        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        binding.btnLogin.setOnClickListener {
            editor.putString(getString(R.string.token_login),viewModel.logar(idUsuario.text.toString().toInt(),senha.text.toString()))
            editor.putInt(getString(R.string.id_usuario),idUsuario.text.toString().toInt())
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}