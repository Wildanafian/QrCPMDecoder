package com.spe.qrdecoder

import com.spe.qrdecoder.decoder.DecoderHelper
import com.spe.qrdecoder.model.QrData

/**
 * Created by Wildan Nafian on 11/06/2022.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */
class QrDecoderHelper : DecoderHelper() {

    fun decodeQR(data: String) = doDecode(data)

    fun getResult(): QrData = getResultModel()

    fun getResultInString(): String = getResultString()

}