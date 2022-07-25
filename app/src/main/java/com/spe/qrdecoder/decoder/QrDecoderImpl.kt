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

    private val resultData = JSONObject()

    private val tag: LinkedHashMap<String, String> by lazy {
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

    override fun decodeString(data: String): JSONObject {
        if (data.checkQrValidity()) {

            var qrString = data.cleanUnnecessaryCharacter().convertToHex()
            val tempJsonData = JSONObject()
            var iterator = tag.iterator()
            var i = 0

            while (qrString.isNotEmpty() && i < 3) {
                if (iterator.hasNext()) {
                    val iteratorData = iterator.next()
                    val tag = iteratorData.key
                    val condition = iteratorData.value

                    if (qrString.checkFirstCharEqualsToTag()) {

                        qrString = qrString.removeValue(4)
                        iterator.remove()

                    } else if (qrString.checkFirstCharEqualsToTag(tag)) {

                        val valueLength = qrString.getTagLengthValue(tag)
                        val value = qrString.getValueFromTagLength(tag, valueLength)
                        tempJsonData.put(tag, value.convertToReadableValue(condition))

                        qrString = qrString.removeValue(tag.length + 2 + valueLength)
                        iterator.remove()

                    }
                } else {
                    iterator = resetIterator()
                    i++
                }
            }
            createJson(true, tempJsonData)
        } else {
            createJson()
        }

        return resultData
    }

    private fun resetIterator() = tag.iterator()

    private fun createJson(validity: Boolean = false, data: Any = "") {
        resultData.put("isValid", validity)
        resultData.put("data", data)
    }

    private fun String.checkFirstCharEqualsToTag(tag: String = "") =
        if (tag.isEmpty()) this.take(2) == "61" || this.take(2) == "63"
        else this.take(tag.length) == tag

    private fun String.removeValue(count: Int) = this.drop(count)

    private fun String.getTagLengthValue(tag: String) =
        this.substring(tag.length, tag.length + 2).hexToInt()

    private fun String.getValueFromTagLength(tag: String, count: Int) =
        this.substring(tag.length + 2, tag.length + 2 + count)

    private fun String.checkQrValidity(): Boolean = this.startsWith("hQVDUFY")

    private fun String.convertToReadableValue(condition: String): String = when (condition) {
        "uppercase" -> this
        "string" -> this.hexToAscii()
        "replace f" -> this.replace("f", "")
        else -> this
    }.uppercase(Locale.getDefault())

    private fun String.cleanUnnecessaryCharacter(): String =
        if (this.takeLast(2) == "==") this.dropLast(2) else this

    private fun String.hexToAscii(): String {
        require(length % 2 == 0) { "Must have an even length" }
        return String(
            chunked(2)
                .map { it.toInt(16).toByte() }
                .toByteArray()
        )
    }

    private fun String.hexToInt(): Int = this.toInt(16) * 2

    private fun String.convertToHex(): String = try {
        Base64.decode(this, Base64.DEFAULT).toHex()
    } catch (e: Exception) {
        ""
    }

    private fun ByteArray.toHex(): String =
        joinToString(separator = "") { eachByte -> "%02x".format(eachByte) }
}