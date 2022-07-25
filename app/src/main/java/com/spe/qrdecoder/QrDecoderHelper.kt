package com.spe.qrdecoder

import com.spe.qrdecoder.decoder.Core.doDecode
import com.spe.qrdecoder.model.QrData
import org.json.JSONObject
import com.spe.qrdecoder.sample.Sample

/**
 * Created by Wildan Nafian on 11/06/2022.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */
class QrDecoderHelper {


    /**
     * Decode qr cpm string to readable and useable format
     *
     * @param data  Qr string to decode
     * @param result    Callback to get the decoded string result
     *
     * @return model and json format if successfully decoded. if failed will return
     *  { "isValid" : false,
     *    "data" : {}
     *  }
     *
     *  @see QrData for data model
     *
     *  @sample Sample.cpmDecode
     *
     */
    fun decode(data: String, result: (QrData, JSONObject) -> Unit) {
        with(doDecode(data)) {
            result.invoke(this.model, this.rawJson)
        }
    }

}