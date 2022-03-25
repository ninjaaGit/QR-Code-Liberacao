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
class ScannerViewModel
@Inject internal constructor(
    val scannerRepository: ScannerRepository,
    @ApplicationContext context: Context

) : ViewModel() {

    val context = context

  fun verificar(carga: Carga,token: String, idUsuario: Int){
      viewModelScope.launch {
          try {
              scannerRepository.liberar(carga,token,idUsuario)
              Toast.makeText(context, "Carga liberada com sucesso!", Toast.LENGTH_LONG).show()
          } catch (e: Exception){
              if(e.message == "HTTP 400 Bad Request") {
                  Toast.makeText(context, "Esta carga não tem pendência de liberação!", Toast.LENGTH_LONG).show()
              } else {
                  Toast.makeText(context, "${e.message}", Toast.LENGTH_LONG).show()
              }
          }
      }
  }

}