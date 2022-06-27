package com.spe.qrcpmdecoder.experimental;

//import org.apache.commons.codec.DecoderException;
//import org.apache.commons.codec.binary.Hex;
//import org.apache.tomcat.util.codec.binary.Base64;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;

import android.os.Build;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;


import static android.util.Base64.DEFAULT;

public class QRCpmHelper2 {
    private final HashMap<String, String> tag = new HashMap<String, String>(){{
       put("first-tag", "85");
       put("first-tag-lvl-1", "61");
       put("first-tag-lvl-2", "63");
    }};
    Set<String> subTagLevel1    = new HashSet<>(Arrays.asList("4f","50","57","5a","5f20","5f2d","5f50","9f08","9f25","9f19","9f24","63","9f76"));
    Set<String> subTagLevel2    = new HashSet<>(Arrays.asList("9f74","9f26","9f27","9f10","9f36","82","9f37"));
    Set<String> tag4digit       = new HashSet<>(Arrays.asList("9f", "5f"));


    private final HashMap<String, String> decodeType = new HashMap<String, String>(){{
        put("85", "string");
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

    public HashMap<String, Object> parseQr(String qrString) throws UnsupportedEncodingException {
        HashMap<String, Object> data = new HashMap<>();
        if (!qrString.startsWith("hQVDUFY")){
            return null;
        }
        qrString = base64toHex(qrString);
        //LOGGER.info(qrString);
        HashMap<String, Object> parsingResult = parsingTag(qrString);
        data.put("qr_data", parsingResult);
        return data;
    }

    private String base64toHex(String base64Text){
        byte[] decoded = Base64.decode(base64Text, DEFAULT);
        return HexUtil.toHex(decoded);
    }

    private String hexToString(String hexText) throws UnsupportedEncodingException {
//        byte[] bytes = Hex.decodeHex(hexText.toCharArray());
//        return new String(bytes, "UTF-8");

        return hexToAscii(hexText);
    }

    private int hexToInt(String hexText){
        return Integer.parseInt(hexText, 16);
    }

    private String getRealValue(String decodeType, String hexData) throws UnsupportedEncodingException {
        if (decodeType.equals("uppercase"))
            return hexData.toUpperCase();
        else if (decodeType.equals("string"))
            return hexToString(hexData);
        else if (decodeType.equals("replace f"))
            return hexData.replace("F", "");
        else
            return hexData;
    }

    public HashMap<String, Object> parsingTag(String qrString) throws UnsupportedEncodingException {
        int tagLen = 2;
        int tagData = 4;
        boolean state = true;
        HashMap<String, Object> qrData = new HashMap<>();
        String firstTag = qrString.substring(0, tagLen);
        if (!firstTag.equals("85")){
            return null;
        }

        int idx = 0;
        while (state && qrString.length() >= 8) {
            String tagId    = qrString.substring(0, tagLen);
            String tagInfo  = qrString.substring(0, tagData);
            int dataLen  =  hexToInt(tagInfo.substring(tagLen)) * 2;
            String decodeData =  qrString.substring(tagData, tagData + dataLen);

            if (tagId.equals("61")){
                qrData.put(tagId, parsingSubTagLevel1(decodeData));
            } else {
                decodeData = getRealValue(decodeType.get(tagId.toLowerCase()), decodeData);
                qrData.put(tagId, decodeData);
            }
            qrString = qrString.substring(tagData + dataLen);
            if (++idx == 2){
                state = false;
            }
        }
        return qrData;
    }

    public HashMap<String, Object> parsingSubTagLevel1(String qrString) throws UnsupportedEncodingException {
        boolean state = true;
        HashMap<String, Object> qrData = new HashMap<>();
        int idx = 0;
        while (state && qrString.length() >= 8) {
            String tagId    = qrString.substring(0, 2);
            if (tag4digit.contains(tagId.toLowerCase()) ){
                tagId = qrString.substring(0, 4);
            }
            int tagLen      = tagId.length();
            int tagData     = tagLen + 2;
            if (subTagLevel1.contains(tagId.toLowerCase())){
                String tagInfo      =  qrString.substring(0, tagData);
                int dataLen         =  hexToInt(tagInfo.substring(tagLen)) * 2;
                String decodeData   =  qrString.substring(tagData, tagData + dataLen);
                if (tagId.equals("63")){
                    qrData.put(tagId, parsingSubTagLevel2(decodeData));
//                    state = false;
                } else {
                    decodeData = getRealValue(decodeType.get(tagId.toLowerCase()), decodeData);
                    qrData.put(tagId, decodeData);
                }
                qrString = qrString.substring(tagData + dataLen);
            }
            if (++idx == 13){
                state = false;
            }
        }
        return new HashMap<>(qrData);
    }

    public HashMap<String, Object> parsingSubTagLevel2(String qrString) throws UnsupportedEncodingException {
        boolean state = true;
        HashMap<String, Object> qrData = new HashMap<>();
        int idx = 0;
        while (state && qrString.length() >= 8) {
            String tagId    = qrString.substring(0, 2);
            if (tag4digit.contains(tagId.toLowerCase()) ){
                tagId = qrString.substring(0, 4);
            }
            int tagLen      = tagId.length();
            int tagData     = tagLen + 2;
            if (subTagLevel2.contains(tagId.toLowerCase())){
                String tagInfo      =  qrString.substring(0, tagData);
                int dataLen         =  hexToInt(tagInfo.substring(tagLen)) * 2;
                String decodeData   =  qrString.substring(tagData, tagData + dataLen);
                decodeData = getRealValue(decodeType.get(tagId.toLowerCase()), decodeData);
                qrData.put(tagId, decodeData);
                qrString = qrString.substring(tagData + dataLen);
            }
            if (++idx == 8){
                state = false;
            }
        }

        return new HashMap<>(qrData);
    }


    public static class HexUtil{
        private static final char[] DIGITS = {
                '0', '1', '2', '3', '4', '5', '6', '7',
                '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
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

    private static String hexToAscii(String hexStr) {
        StringBuilder output = new StringBuilder("");

        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }

        return output.toString();
    }
}
