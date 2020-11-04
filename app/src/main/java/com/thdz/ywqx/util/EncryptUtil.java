package com.thdz.ywqx.util;

import android.support.annotation.NonNull;
import android.util.Log;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * 为了使用https变化，使用openssl加密
 * 整个app，发送程序里，增加加密
 */
public class EncryptUtil {

    private static final String TAG = "EncryptUtil";


    // 公钥 ，不会变化
    private static final String DEFAULT_PUBLIC_KEY =
            "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEA3OF24jGmdTWOiOdZvNp0" +
                    "Sq9fCB730062bOu8CW1ANe8YadPWauIbSirs08xghAjNvytGypcJA2rtIEwKe4kl" +
                    "5++sRUqP2NCY5pZgDl+FLRFVYj8ysPX5OaKtvMOMX9Qr30lZVnomyP6teIlxHElL" +
                    "jbaP8LJ+k6lWfJ+Eq41f0nimVX0lajgNjon6SxYu0QaApHs0u4mZ4O+EGX8X8Pan" +
                    "csJKpVIAOqd1owMHcRLtXGUvUpPaPb7l1mVvQ1MupwzOq2ClCT/ZOmYQz0PCja3v" +
                    "5QJuBcIVqxLQmTqLCZaAaL01w/SiXAK0IHk7MK/wYN2W91T9N7Z1EFzSwFlZyk01" +
                    "txgK1S1d7z/9uPEwTymluavmHh/RVHQWKnXQvFnDr/Gzmt+MC46T1/kYSBCO2bf4" +
                    "2Pj7LTZ3WsGUuU9ZK9762uMUTTa2beaW4yfyAPjwAJMFvvjKIQFrZCOYjCDkS8sF" +
                    "GNn8qtlAC/BeZmYXA9o/WlAXMFuHTVgzvKUyrhyttldLLlVj6dwLdD8ysrxmXEn8" +
                    "u1h88Bi9sAx5B6tULMW2hR5tXN978eHr+Tq28hWHYxumvQxtDlfK1Q3xxKPpS/P1" +
                    "skClZzpNQMApeLgOAZ0hpnRnxtAg+wpF+Dzv91gMmhbe78fxDQHj3UKVhxeIXGdG" +
                    "bIL7jEuHuY/iLDZqdNSvGdcCAwEAAQ==";

    static {
        try {
            loadPublicKey(DEFAULT_PUBLIC_KEY);
            System.out.println("加载公钥成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static RSAPublicKey publicKey;

    /**
     * 加载公钥
     */
    private static void loadPublicKey(String publicKeyStr) throws Exception {
        try {
//            byte[] buffer = null;
//            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                buffer = Base64.getDecoder().decode(publicKeyStr);
//            } else {
//                buffer = android.util.Base64.decode(publicKeyStr, android.util.Base64.DEFAULT);
//            }
            byte[] buffer = android.util.Base64.decode(publicKeyStr, android.util.Base64.DEFAULT);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }

    /**
     * 加密过程
     *
     * @param publicKey     公钥
     * @param plainTextData 明文数据
     */
    private static byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData) throws Exception {
        if (publicKey == null) {
            throw new Exception("加密公钥为空, 请设置");
        }
        Cipher cipher = null;
        try {
            // TODO RSA加密在java和Android上的不同,
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding"); // android上声明
            // cipher = Cipher.getInstance("RSA"); // java上声明
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] output = cipher.doFinal(plainTextData);
            return output;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此加密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new Exception("加密公钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("明文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("明文数据已损坏");
        }
    }

    /**
     * 加密，
     * 加密内容：用户名和密码字符串都用'\0'补齐64位，“以秒为单位的整形时间”转为字符串然后补齐16位，最后把三个字符串拼接为src。 src的长度等于（64+64+16），字符串使用GB2312编码
     */
    public static String getEncryptString() {
        try {
            // 1 使用用户名、密码和时间拼接成一个字符串
            String username = SpUtil.getData(Finals.SP_USERNAME);
            String password = SpUtil.getData(Finals.SP_PWD);
            String time = (int) (System.currentTimeMillis() / 1000) + "";
            String mergeStr = extendToLength(username, 64) + extendToLength(password, 64) + extendToLength(time, 16);

            // 2 对拼接字符串使用OpenSsl加密后为byte数组
            byte[] cipher = encrypt(publicKey, mergeStr.getBytes("GB2312")); // StandardCharsets.UTF_8

            // 3 Base64加密
            return android.util.Base64.encodeToString(cipher, android.util.Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "加载公钥失败");
        }
        return "";
    }


    /**
     * 加密， 登录时，系统内尚未存储username和password，所以调用此方法
     * 加密内容：用户名和密码字符串都用'\0'补齐64位，“以秒为单位的整形时间”转为字符串然后补齐16位，最后把三个字符串拼接为src。 src的长度等于（64+64+16），字符串使用GB2312编码
     */
    public static String getEncryptString4Login(String username, String password) {
        try {
            // 1 使用用户名、密码和时间拼接成一个字符串
            String time = (int) (System.currentTimeMillis() / 1000) + "";
            String mergeStr = extendToLength(username, 64) + extendToLength(password, 64) + extendToLength(time, 16);

            // 2 对拼接字符串使用OpenSsl加密后为byte数组
            byte[] cipher = encrypt(publicKey, mergeStr.getBytes("GB2312")); // StandardCharsets.UTF_8

            // 3 Base64加密
            return android.util.Base64.encodeToString(cipher, android.util.Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "加载公钥失败");
        }
        return "";
    }


    // 不足 64位，补："\0"
    private static String extendToLength(@NonNull String str, int len) {
        if (str.length() >= len) {
            Log.i(TAG, "字符串超长了");
            return str;
        }
        int left = len - str.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < left; i++) {
            sb.append("\0");
        }
        return str + sb.toString();
    }

}
