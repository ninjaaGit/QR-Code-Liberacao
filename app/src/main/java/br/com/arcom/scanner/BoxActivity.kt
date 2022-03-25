package br.com.arcom.scanner

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import br.com.arcom.scanner.api.model.Box
import br.com.arcom.scanner.api.model.Carga
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
    }

    private fun initScanner() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Aponte para um QR CODE para fazer a leitura do box")
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val boxIntent = intent.getStringExtra("Box")

        val listMainActivity = Gson().fromJson(boxIntent, Carga::class.java)

        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        val boxModel = Gson().fromJson(result.contents, Box::class.java)

        verificar(boxModel, listMainActivity)

        super.onActivityResult(requestCode, resultCode, data)

    }

    fun verificar(box: Box, carga: Carga) {
        if (box.box != carga.box) {
            initScanner()
            Toast.makeText(this, "O box informado está incorreto", Toast.LENGTH_LONG).show()
        } else {
            val intent = Intent(this, MainActivity::class.java)
            val sharedPref = getPreferences(Context.MODE_PRIVATE)
            val idUsuario = sharedPref.getInt(getString(R.string.id_usuario), 0)
            val token = sharedPref.getString(getString(R.string.token_login), "")
            if (token != null && idUsuario != null) {
                viewModel.verificar(carga, token, idUsuario)
                startActivity(intent)
            }else{
                Toast.makeText(this, "Erro ao achar o token", Toast.LENGTH_LONG).show()
            }

        }
    }

}