package br.com.arcom.scanner.api.vo

import com.google.gson.annotations.SerializedName

data class CargaInfo (
    @field:SerializedName("idUsuario") val idUsuario: Int,
    @field:SerializedName("cargaPrincipal") val cargaPrincipal: Long?,
    @field:SerializedName("boxCarregamento") val boxCarregamento: String?,
    @field:SerializedName("quantidadeRecipientesContagem") val quantidadeRecipientesContagem: Long?,
)