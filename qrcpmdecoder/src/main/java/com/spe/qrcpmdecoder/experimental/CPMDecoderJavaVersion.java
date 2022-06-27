package com.spe.qrcpmdecoder.experimental;

import android.os.Build;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;


import static android.util.Base64.DEFAULT;

/**
 * Created by Wildan Nafian on 13/06/2022.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */
public class CPMDecoderJavaVersion {

    private final LinkedHashMap<String, String> tagKey = new LinkedHashMap<String, String>() {{
        put("85", "string");
        put("61", "string");
        put("4f", "uppercase");
        put("50", "string");
        put("57", "string");
        put("5a", "replace f");
        put("5f20", "string");
        put("5f2d", "string");
        put("5f50", "string");
        put("9f08", "string");
        put("9f25", "");
        put("9f19", "");
        put("9f24", "string");
        put("63", "");
        put("9f74", "string");
        put("9f26", "");
        put("9f27", "");
        put("9f10", "");
        put("9f36", "");
        put("82", "");
        put("9f37", "");
        put("9f76", "string");
    }};

    public String decodeQR(String hexStringData) throws JSONException, UnsupportedEncodingException {
        JSONObject json = new JSONObject();
        if (hexStringData.startsWith("hQVDUFY")) {
            String tempData = "";

            if (hexStringData.endsWith("==")) tempData = base64toHex(hexStringData.substring(0, hexStringData.length() - 2));
            else tempData = base64toHex(hexStringData);

            while (!tempData.isEmpty()) {
                for (Map.Entry<String, String> entry : tagKey.entrySet()) {
                    String key = entry.getKey();
                    String condition = entry.getValue();

                    if ((key.equals("61") || key.equals("63")) && tempData.substring(0, 2).equals(key)) {
                        tempData = tempData.replaceFirst(tempData.substring(0, 4), "");
                    } else if (tempData.startsWith(key)) {

                        tempData = tempData.replaceFirst(tempData.substring(0, key.length()), "");
                        int takeValueLength = hexToInt(tempData.substring(0, 2)) * 2;
                        tempData = tempData.replaceFirst(tempData.substring(0, 2), "");
                        String takeValue = tempData.substring(0, takeValueLength);
                        tempData = tempData.replaceFirst(tempData.substring(0, takeValueLength), "");

                        String realValue = convertHexToString(takeValue, condition).toLowerCase(Locale.ROOT);
                        json.put(key, realValue);

                    }
                }
            }
            json.put("isValid", true);
        } else {
            json.put("isValid", false);
        }
        return json.toString();
    }

    private int hexToInt(String hexText) {
        return Integer.parseInt(hexText, 16);
    }

    private String convertHexToString(String hexData, String decodeType) throws UnsupportedEncodingException {
        switch (decodeType) {
            case "string":
                return hexToString(hexData);
            case "replace f":
                return hexData.replace("f", "");
            default:
                return hexData;
        }
    }

    private String hexToString(String hexStr) throws UnsupportedEncodingException {
        StringBuilder output = new StringBuilder("");

        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }

        return output.toString();
    }

    private String base64toHex(String base64Text) {
        byte[] decoded = Base64.decode(base64Text, DEFAULT);
        return HexUtil.toHex(decoded).toLowerCase(Locale.ROOT);
    }

    public static class HexUtil {
        private static final char[] DIGITS = {
                '0', '1', '2', '3', '4', '5', '6', '7',
                '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
        };

        public static final String toHex(byte[] data) {
            final StringBuilder sb = new StringBuilder();
            for (byte d : data) {
                sb.append(DIGITS[(d >>> 4) & 0x0F]);
                sb.append(DIGITS[d & 0x0F]);
            }
            return sb.toString();
        }
    }

}
