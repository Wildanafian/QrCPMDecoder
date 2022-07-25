package com.spe.qrdecoder.decoder

import com.google.gson.Gson
import com.spe.qrdecoder.model.QrData
import com.spe.qrdecoder.model.ResultQrCPM


/**
 * Created by Wildan Nafian on 11/06/2022.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */
internal object Core {

    fun doDecode(data: String): ResultQrCPM {
        val rawJson = QrDecoderImpl().decodeString(data)
        val model = Gson().fromJson(rawJson.toString(), QrData::class.java)
        return ResultQrCPM(model, rawJson)
    }

}