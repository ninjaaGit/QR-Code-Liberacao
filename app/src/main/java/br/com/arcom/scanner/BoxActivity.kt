package br.com.arcom.scanner

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import br.com.arcom.scanner.api.model.Box
import br.com.arcom.scanner.api.model.Carga
import br.com.arcom.scanner.util.Result
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.zxing.integration.android.IntentIntegrator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BoxActivity : AppCompatActivity() {
    private lateinit var viewModel: ScannerViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initScanner()
        viewModel = ViewModelProvider(this)[ScannerViewModel::class.java]
        viewModel.status.observe(this) {
            when (it) {
                is Result.Ok -> {
                    val intent = Intent(this, SucessoActivity::class.java)
                    startActivity(intent)
                }
                is Result.Error -> {
                    val intent = Intent(this, ErroActivity::class.java)
                    startActivity(intent)
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
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    viewModel.deslogar()
                }
            }
        }
    }

    private fun initScanner() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Aponte para um QR CODE para fazer a leitura do box")
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result.contents == null) {
            Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            val boxIntent = intent.getStringExtra("Box")
            val listMainActivity = Gson().fromJson(boxIntent, Carga::class.java)
            val boxModel = Gson().fromJson(result.contents, Box::class.java)

            if (boxModel.id != null && boxModel.box != null || boxModel.box != null || boxModel.id != null) {
                if (boxModel.id == 4L) {
                    verificar(boxModel, listMainActivity)
                } else {
                    Toast.makeText(this, "QR Code inválido!", Toast.LENGTH_SHORT).show()
                    initScanner()
                }
            } else {
                Toast.makeText(this, "QR Code inválido!", Toast.LENGTH_SHORT).show()
                initScanner()
            }
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun verificar(box: Box, carga: Carga) {
        if (box.box != carga.box) {
            initScanner()
            Toast.makeText(this, "O box informado está incorreto", Toast.LENGTH_LONG).show()
        } else {
            openDialog(carga)
        }
    }

    private fun openDialog(carga: Carga) {

        try {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.dialog)
            val input = dialog.findViewById(R.id.txt_input) as TextInputEditText
            val btn = dialog.findViewById(R.id.btn_confirmar) as MaterialButton
            btn.setOnClickListener {
                if (input.text.toString() == carga.volume.toString()) {
                    viewModel.verificar(carga)
                } else {
                    Toast.makeText(this, "O volume informado está incorreto", Toast.LENGTH_LONG).show()
                }
            }
            dialog.show()
        } catch (e: Exception) {
            initScanner()
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

}