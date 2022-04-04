package br.com.arcom.scanner

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import br.com.arcom.scanner.api.model.Carga
import br.com.arcom.scanner.databinding.ActivityMainBinding
import br.com.arcom.scanner.util.Result
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.zxing.integration.android.IntentIntegrator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ScannerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.nomeUsuario.text = "Visitante"
        setContentView(binding.root)
        binding.btnScan.setOnClickListener { initScanner() }
        viewModel = ViewModelProvider(this)[ScannerViewModel::class.java]

        viewModel.dadosUsuario.observe(this) {
            binding.nomeUsuario.text = it
        }

        if (viewModel.sharedPreferences.getString(
                "token",
                null
            ) == null || viewModel.sharedPreferences.getString("token_login", null) == ""
        ) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnDeslogar.setOnClickListener {
            viewModel.deslogar()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        viewModel.status.observe(this) {
            when (it) {
                is Result.Ok -> {
                }
                is Result.Error -> {
                    Toast.makeText(this, "Erro ao processar a requisicao $it!", Toast.LENGTH_LONG)
                        .show()
                    // desabilitar o loading progress
                }
                is Result.Loading -> {

                }// habilitar o progress

                is Result.Unauthorized -> {
                    Toast.makeText(
                        this,
                        "Erro ao verificar o token, faça login novamente! $it!",
                        Toast.LENGTH_LONG
                    ).show()
                    viewModel.deslogar()
                }
            }
        }
    }

    private fun initScanner() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Aponte para um QR CODE para fazer a leitura.")
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result.contents == null) {
            Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show()
        } else {
            openDialog(result.contents)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun openDialog(result: String) {

        try {
            val list = Gson().fromJson(result, Carga::class.java)
            if (list.box != null && list.carga != null && list.id != null && list.volume != null) {
                if (list.id == 3L) {
                    val dialog = Dialog(this)
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog.setCancelable(true)
                    dialog.setContentView(R.layout.dialog_box)
                    val box = dialog.findViewById(R.id.txt_box) as TextView
                    val btn = dialog.findViewById(R.id.btn_confirmar) as MaterialButton
                    box.text = "" + list.box
                    btn.setOnClickListener {
                        val intent = Intent(this, BoxActivity::class.java)
                        intent.putExtra("Box", result)
                        startActivity(intent)
                    }
                    dialog.show()
                }
            } else {
                initScanner()
                Toast.makeText(this, "O QR Code inserido é inválido", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            initScanner()
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }
}