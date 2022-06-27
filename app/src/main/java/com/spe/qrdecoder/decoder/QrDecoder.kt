package com.spe.qrdecoder.decoder


/**
 * Created by Wildan Nafian on 11/06/2022.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */
interface QrDecoder {
    fun decodeString(rawData: String): String
}