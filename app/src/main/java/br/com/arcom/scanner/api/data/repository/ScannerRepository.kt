package br.com.arcom.scanner.api.data.repository


import br.com.arcom.scanner.api.ApiService
import br.com.arcom.scanner.api.model.Carga
import br.com.arcom.scanner.api.model.CargaInfo
import br.com.arcom.scanner.api.model.SolicitaDeviceTokenRequest
import br.com.arcom.scanner.api.model.SolicitaDeviceTokenResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScannerRepository @Inject constructor() {

    suspend fun buscarToken(idUsuario:Int, senha:String): SolicitaDeviceTokenResponse {
        val service = ApiService.create(null)
        val token = service.createDeviceToken(SolicitaDeviceTokenRequest(idUsuario = idUsuario, senha = senha))
        return token
    }

    suspend fun liberar(carga: Carga,token:String,idUsuario: Int){
        val service = ApiService.create(token)
        service.cargaInfos(CargaInfo(idUsuario = idUsuario, cargaPrincipal = carga.carga, boxCarregamento = carga.box, quantidadeRecipientesContagem = carga.volume))
    }

}