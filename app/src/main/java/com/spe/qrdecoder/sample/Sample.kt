package com.spe.qrdecoder.sample

import com.spe.qrdecoder.QrDecoderHelper


/**
 * Created by Wildan Nafian on 25/07/2022.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */
internal class Sample {
    fun cpmDecode() {
        QrDecoderHelper().decode("your string") { model, rawJson ->
            val dataInModel = model.data
            val dataInString = rawJson.toString()
        }
    }
}