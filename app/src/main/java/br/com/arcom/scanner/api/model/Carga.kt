package br.com.arcom.scanner.api.model

import com.google.gson.annotations.SerializedName

data class Carga(
    @field:SerializedName("ID") val id:Long?,
    @field:SerializedName("Volume") val volume:Long?,
    @field:SerializedName("Carga") val carga:Long?,
    @field:SerializedName("Box") val box:String?,
)
