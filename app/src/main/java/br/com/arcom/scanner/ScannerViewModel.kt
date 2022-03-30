package br.com.arcom.scanner

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.arcom.scanner.api.data.repository.ScannerRepository
import br.com.arcom.scanner.api.model.Carga
import br.com.arcom.scanner.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named


@HiltViewModel
class ScannerViewModel
@Inject internal constructor(
    val scannerRepository: ScannerRepository,
    @Named("auth") val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _status = MutableLiveData<Result>()
    val status: LiveData<Result> = _status

    private val _dadosUsuario = MutableLiveData<String>()
    val dadosUsuario: LiveData<String> = _dadosUsuario

    init {
        viewModelScope.launch {
            val nomeUsuario = sharedPreferences.getString("nomeUsuario", "")
            _dadosUsuario.value = nomeUsuario!!
        }
    }

    fun deslogar(){
        sharedPreferences.edit().clear().apply()
        _status.value = Result.Unauthorized
    }

  fun verificar(carga: Carga){
      viewModelScope.launch {
          _status.value = Result.Loading
          try {
              val idUsuario = sharedPreferences.getInt("idUsuario", 0)
              val token = sharedPreferences.getString("token", null)
              val nomeUsuario = sharedPreferences.getString("nomeUsuario", "")
               scannerRepository.liberar(carga,token!!,idUsuario)
              _status.value = Result.Ok
              _dadosUsuario.value = nomeUsuario!!
          } catch (e: Exception){
              println("${e.message} aaaaaaaaaaaaaaaaaa")

              if (e.message!!.contains("Unauthorized")) {
                  sharedPreferences.edit().clear().apply()
                  _status.value = Result.Unauthorized

              }
              _status.value = Result.Error(e)
          }
      }
  }
}