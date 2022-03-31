package br.com.arcom.scanner

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.lifecycle.ViewModelProvider
import br.com.arcom.scanner.databinding.LoginBinding
import br.com.arcom.scanner.util.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        viewModel.verificaToken()
        setContentView(R.layout.login)
        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val idUsuario = binding.inputMatricula
        val senha = binding.inputSenha
        binding.btnLogin.setOnClickListener {
            if (idUsuario.text.toString().toInt() != null && senha.text.toString() != "") {
                viewModel.logar(idUsuario.text.toString().toInt(), senha.text.toString())

            } else {
                Toast.makeText(this, "Erro ao processar a requisicao $it!", Toast.LENGTH_LONG).show()
            }
        }
        viewModel.status.observe(this) {

            fun Intent.clearStack() {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            when (it) {
                is Result.Ok -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    intent.clearStack()
                }
                is Result.Error -> {
                    Toast.makeText(
                        this,
                        "Erro ao processar a requisicao $it!",
                        Toast.LENGTH_LONG
                    ).show()
                    binding.progressbar.visibility = View.INVISIBLE
                    // desabilitar o loading progress
                }
                is Result.Loading -> {
                    binding.progressbar.visibility = View.VISIBLE
                }// habilitar o progress
                is Result.Token -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    intent.clearStack()
                }
            }
        }
    }

}
