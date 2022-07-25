package com.spe.qrdecoder.decoder

import org.json.JSONObject


/**
 * Created by Wildan Nafian on 11/06/2022.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */
internal interface QrDecoder {

    fun decodeString(data: String): JSONObject

}