package br.com.arcom.scanner.api.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class SolicitaDeviceTokenRequest (
    @field:SerializedName("idUsuario") val idUsuario: Int,
    @field:SerializedName("senha") val senha: String?,

)
