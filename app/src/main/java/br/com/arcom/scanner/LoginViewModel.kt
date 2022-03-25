package br.com.arcom.scanner

import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings.Global.getString
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.arcom.scanner.api.data.repository.ScannerRepository
import br.com.arcom.scanner.api.model.Carga
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel
@Inject internal constructor(
    val scannerRepository: ScannerRepository,
    @ApplicationContext context: Context

) : ViewModel() {

    val context = context
    var token = ""


    fun logar(idUsuario:Int, senha:String):String{
        viewModelScope.launch {
            try {
                token =  scannerRepository.buscarToken(idUsuario = idUsuario, senha = senha)
            } catch (e: Exception) {
                Toast.makeText(context, "Falha no login, verifique a matrícula e/ou sua senha.", Toast.LENGTH_LONG).show()
            }
        }
        return token
    }
}