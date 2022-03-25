package br.com.arcom.telemetria.api.vo

import com.google.gson.annotations.SerializedName

data class SolicitaDeviceTokenResponse(
        @field:SerializedName("token") val token: String?,
)


