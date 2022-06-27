package com.spe.qrcpmdecoder.model

import com.google.gson.annotations.SerializedName

data class QrData(

    @field:SerializedName("data")
    val data: BodyData? = null,

    @field:SerializedName("isValid")
    val isValid: Boolean = false
)

data class BodyData(

    @field:SerializedName("85")
    val tag85: String? = "",

    @field:SerializedName("4f")
    val tag4f: String? = "",

    @field:SerializedName("50")
    val tag50: String? = "",

    @field:SerializedName("57")
    val tag57: String? = "",

    @field:SerializedName("5a")
    val tag5a: String? = "",

    @field:SerializedName("5f20")
    val tag5f20: String? = "",

    @field:SerializedName("5f2d")
    val tag5f2d: String? = "",

    @field:SerializedName("5f50")
    val tag5f50: String? = "",

    @field:SerializedName("9f08")
    val tag9f08: String? = "",

    @field:SerializedName("9f25")
    val tag9f25: String? = "",

    @field:SerializedName("9f19")
    val tag9f19: String? = "",

    @field:SerializedName("9f24")
    val tag9f24: String? = "",

    @field:SerializedName("9f74")
    val tag9f74: String? = "",

    @field:SerializedName("9f26")
    val tag9f26: String? = "",

    @field:SerializedName("9f27")
    val tag9f27: String? = "",

    @field:SerializedName("9f10")
    val tag9f10: String? = "",

    @field:SerializedName("9f36")
    val tag9f36: String? = "",

    @field:SerializedName("82")
    val tag82: String? = "",

    @field:SerializedName("9f37")
    val tag9f37: String? = "",

    @field:SerializedName("9f76")
    val tag9f76: String? = ""
)
