package br.com.arcom.scanner

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import br.com.arcom.scanner.databinding.ActivityErroBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ErroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityErroBinding
    private lateinit var viewModel: ScannerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ScannerViewModel::class.java]
        setContentView(R.layout.activity_erro)
        binding = ActivityErroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnErrado.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
