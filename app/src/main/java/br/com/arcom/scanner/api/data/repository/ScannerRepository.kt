package br.com.arcom.scanner.api.data.repository


import br.com.arcom.scanner.api.ApiService
import br.com.arcom.scanner.api.model.Carga
import br.com.arcom.scanner.api.vo.CargaInfo
import br.com.arcom.scanner.api.vo.SolicitaDeviceTokenRequest
import br.com.arcom.scanner.api.vo.buscaCargaLiberacaoResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScannerRepository @Inject constructor() {

    suspend fun buscarToken(idUsuario:Int, senha:String): String {
        val service = ApiService.create(null)
        val token = service.createDeviceToken(SolicitaDeviceTokenRequest(idUsuario = idUsuario, senha = senha, apenasToken = true))


        return token
    }

    suspend fun buscarCargasLiberacao(token:String) {
        val service = ApiService.create(token)
        val lista = service.buscaCargaLiberacao()

        println("$lista aaaaaaaaaaaaaa")

    }

    suspend fun liberar(carga: Carga,token:String,idUsuario: Int){
        val service1 = ApiService.create(null)
        val token = service1.createDeviceToken(SolicitaDeviceTokenRequest(idUsuario = 159616, senha = "Ninja1976", apenasToken = true))
        val service = ApiService.create(token)
        val lista = service.buscaCargaLiberacao()
        println("${lista.body()!!.map { it.modulo }} aaaaaaaaaaaaaa")
        service.cargaInfos(CargaInfo(idUsuario = 159616, cargaPrincipal = carga.carga, boxCarregamento = carga.box, quantidadeRecipientesContagem = carga.volume))
    }

}