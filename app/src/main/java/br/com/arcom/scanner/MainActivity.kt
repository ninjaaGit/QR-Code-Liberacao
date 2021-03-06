package br.com.arcom.scanner

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Toast
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

        binding.btnDeslogar.setOnClickListener {
            viewModel.deslogar()

        }

        viewModel.status.observe(this) {
            when(it) {
                is Result.Ok -> {
                    Toast.makeText(this, "Deu certo!", Toast.LENGTH_SHORT).show()
                }
                is Result.Error -> {
                    Toast.makeText(this, "Erro ao processar a requisicao $it!", Toast.LENGTH_LONG).show()
                    // desabilitar o loading progress
                }
                is Result.Loading -> { }// habilitar o progress

                is Result.Unauthorized -> {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
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
            Toast.makeText(this, "${result.contents}", Toast.LENGTH_LONG).show()
            openDialog(result.contents)
        }
        Log.d("teste", result.contents)

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun openDialog(result: String) {

        try {
            val list = Gson().fromJson(result, Carga::class.java)

            println("$list aaaaaaaaaa")
            if (list.box != null && list.carga != null && list.id != null && list.volume != null) {
                val dialog = Dialog(this)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(true)
                dialog.setContentView(R.layout.dialog)
                val input = dialog.findViewById(R.id.txt_input) as TextInputEditText
                val btn = dialog.findViewById(R.id.btn_confirmar) as MaterialButton
                btn.setOnClickListener {
                    if (input.text.toString() == list.volume.toString()) {
                        val intent = Intent(this, BoxActivity::class.java)
                        intent.putExtra("Box", result)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "O valor inserido n??o corresponde", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                dialog.show()
            } else {
                initScanner()
                Toast.makeText(this, "O QR Code inserido ?? inv??lido", Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) {
            initScanner()
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }



    }
}