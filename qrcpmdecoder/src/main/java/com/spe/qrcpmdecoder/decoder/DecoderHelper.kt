package com.spe.qrcpmdecoder.decoder

import com.google.gson.Gson
import com.spe.qrcpmdecoder.model.QrData


/**
 * Created by Wildan Nafian on 11/06/2022.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */
abstract class DecoderHelper {

    private var decodeResultString = ""

    protected fun doDecode(data: String) {
        decodeResultString = QrDecoderImpl().decodeString(data)
    }

    protected fun getResultModel(): QrData = Gson().fromJson(decodeResultString, QrData::class.java)

    protected fun getResultString(): String = decodeResultString
}