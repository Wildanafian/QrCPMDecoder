package com.spe.qrdecoder.decoder

import android.util.Base64
import org.json.JSONObject
import java.util.*


/**
 * Created by Wildan Nafian on 11/06/2022.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */
internal class QrDecoderImpl : QrDecoder {

    private val tagKey: LinkedHashMap<String, String> by lazy {
        LinkedHashMap<String, String>().apply {
            put("85", "string")
            put("61", "string")
            put("4f", "uppercase")
            put("50", "string")
            put("57", "string")
            put("5a", "replace f")
            put("5f20", "string")
            put("5f2d", "string")
            put("5f50", "string")
            put("9f08", "string")
            put("9f25", "")
            put("9f19", "")
            put("9f24", "string")
            put("63", "")
            put("9f74", "string")
            put("9f26", "")
            put("9f27", "")
            put("9f10", "")
            put("9f36", "")
            put("82", "")
            put("9f37", "")
            put("9f76", "string")
        }
    }

    override fun decodeString(rawData: String): String {
        val resultData = JSONObject()
        if (rawData.checkValidity()) {
            var iterator = tagKey.iterator()
            var decodedData = base64toHex(if (rawData.takeLast(2) == "==") rawData.dropLast(2) else rawData)
            if (decodedData.isNotEmpty()) {
                val tempResultData = JSONObject()
                var i = 0
                while (decodedData.isNotEmpty() && i < 3) {
                    if (iterator.hasNext()) {
                        val iteratorData = iterator.next()
                        val tag = iteratorData.key
                        val condition = iteratorData.value

                        if ((tag == "61" || tag == "63") && decodedData.take(2) == tag) {
                            decodedData = decodedData.drop(4)
                            iterator.remove()
                        } else if (decodedData.take(tag.length) == tag) {
                            decodedData = decodedData.drop(tag.length)
                            val tagLength = decodedData.take(2).hexToInt()
                            decodedData = decodedData.drop(2)
                            val tagValue = decodedData.take(tagLength)
                            decodedData = decodedData.drop(tagLength)

                            val realValue = convertHexToString(
                                tagValue,
                                condition
                            ).uppercase(Locale.getDefault())
                            tempResultData.put(tag, realValue)
                            iterator.remove()
                        }
                    } else {
                        iterator = tagKey.iterator()
                        i++
                    }
                }
                resultData.put("isValid", true)
                resultData.put("data", tempResultData)
            } else {
                resultData.put("isValid", false)
                resultData.put("data", "")
            }
        } else {
            resultData.put("isValid", false)
            resultData.put("data", "")
        }
        return resultData.toString()
    }

    private fun String.checkValidity(): Boolean = this.startsWith("hQVDUFY")

    private fun convertHexToString(hexData: String, condition: String): String {
        return when (condition) {
            "uppercase" -> hexData
            "string" -> hexData.hexToAscii()
            "replace f" -> hexData.replace("f", "")
            else -> hexData
        }
    }

    private fun String.hexToAscii(): String {
        require(length % 2 == 0) { "Must have an even length" }
        return String(
            chunked(2)
                .map { it.toInt(16).toByte() }
                .toByteArray()
        )
    }

    private fun String.hexToInt(): Int = this.toInt(16) * 2

    private fun base64toHex(base64Text: String): String {
        return try {
            Base64.decode(base64Text, Base64.DEFAULT).toHex()
        } catch (e: Exception) {
            ""
        }
    }

    private fun ByteArray.toHex(): String =
        joinToString(separator = "") { eachByte -> "%02x".format(eachByte) }
}