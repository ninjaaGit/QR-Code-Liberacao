package br.com.arcom.scanner.api.vo

import com.google.gson.annotations.SerializedName

data class buscaCargaLiberacaoResponse(
    @field:SerializedName("idCarga") val idCarga: Long,
    @field:SerializedName("boxDestino") val boxDestino: String,
    @field:SerializedName("qtdMovimentada") val qtdMovimentada: Long,
    @field:SerializedName("digitoBoxDestino") val digitoBoxDestino: Long,
    @field:SerializedName("filtroOrigemEtiqueta") val filtroOrigemEtiqueta: String,
    @field:SerializedName("origemEtiqueta") val origemEtiqueta: String,
    @field:SerializedName("statusLiberacao") val statusLiberacao: Long,
    @field:SerializedName("modulo") val modulo: String,

    )


