package com.medusa.gruul.payment.api.util;

import cn.hutool.crypto.SecureUtil;
import com.google.common.collect.Maps;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Method;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.openssl.PasswordFinder;
import org.bouncycastle.openssl.PEMReader;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


/**
 * @author create by zq
 * @date created in 2019/11/06
 */
@Log
public class GlobalTools {


    private GlobalTools() {

    }


    /**
     * AES证书加密
     */
    public static String encryptAes(String key, String data) {
        final int a = 16;
        final int b = 24;
        final int c = 32;
        final int one = 1;
        try {
            String charset = GlobalConstant.STRING_UTF_8;
            if (key == null) {
                throw new RuntimeException("key is null");
            } else if (a != key.length() && b != key.length() && c != key.length()) {
                throw new RuntimeException("key must be 16/24/32");
            } else {
                byte[] raw = key.getBytes(charset);
                SecretKeySpec sKeySpec = new SecretKeySpec(raw, GlobalConstant.STRING_AES);
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                cipher.init(one, sKeySpec);
                byte[] encrypted = cipher.doFinal(data.getBytes(charset));
                return parseByte2HexStr(encrypted);
            }
        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }


    /**
     * AES证书解密
     */
    public static String decryptAes(String key, String text) {
        final int a = 16;
        final int b = 24;
        final int c = 32;
        final int two = 2;
        try {
            if (key == null) {
                throw new RuntimeException("key is null");
            } else if (a != key.length() && b != key.length() && c != key.length()) {
                throw new RuntimeException("key must be 16/24/32");
            } else {
                byte[] raw = key.getBytes(GlobalConstant.STRING_UTF_8);
                SecretKeySpec sKeySpec = new SecretKeySpec(raw, "AES");
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                cipher.init(two, sKeySpec);

                try {
                    byte[] original = cipher.doFinal(parseHexStr2Byte(text));
                    return new String(original, GlobalConstant.STRING_UTF_8);
                } catch (Exception var9) {
                    throw new RuntimeException(var9);
                }
            }
        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }


    /**
     * 根据rsa私钥进行签名
     */
    public static String signSha256withRsa(String privateKey, String privateKeyPwd, String data) {
        return signSha256withRsa(privateKey, privateKeyPwd, data, GlobalConstant.STRING_UTF_8);
    }


    private static String signSha256withRsa(String privateKey, String privateKeyPwd, String data, String charset) {
        try {
            PrivateKey privateKeyObj = loadPrivateKey(privateKey, privateKeyPwd);
            return Hex.encodeHexString(signSha256(privateKeyObj, getBytes(data, charset)));
        } catch (Exception var4) {
            throw new RuntimeException(var4);
        }
    }


    private static PrivateKey loadPrivateKey(String privateKeyStr, final String pwd) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            PEMReader reader = new PEMReader(new StringReader(privateKeyStr), new PasswordFinder() {
                @Override
                public char[] getPassword() {
                    return pwd.toCharArray();
                }
            });
            KeyPair pair = (KeyPair) reader.readObject();
            Key privateKey = pair.getPrivate();
            reader.close();
            return (PrivateKey) privateKey;
        } catch (Exception var5) {
            throw new RuntimeException("load privateKey error:", var5);
        }
    }


    private static byte[] signSha256(PrivateKey privateKey, byte[] data) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(data);
            return signature.sign();
        } catch (Exception var5) {
            throw new RuntimeException(var5);
        }
    }


    public static byte[] sxfSign(String content, String privateKey) {
        try {
            PKCS8EncodedKeySpec priPkcs8 = new PKCS8EncodedKeySpec(decryptBase64(privateKey));
            PrivateKey priKey = KeyFactory.getInstance(GlobalConstant.STRING_RSA).generatePrivate(priPkcs8);
            java.security.Signature signature = java.security.Signature.getInstance(GlobalConstant.STRING_SHA1_WITH_RSA);
            signature.initSign(priKey);
            signature.update(content.getBytes());
            byte[] signed = signature.sign();
            return signed;
        } catch (Exception e) {
            log.warning(e.getMessage());
            return null;
        }
    }


    /**
     * 根据rsa公钥进行验签
     */
    public static boolean verifySha256withRsa(String publicKey, String data, String sign) {
        return verifySha256withRsa(publicKey, data, sign, GlobalConstant.STRING_UTF_8);
    }


    private static boolean verifySha256withRsa(String publicKey, String data, String sign, String charset) {
        try {
            PublicKey publicKeyObj;
            try {
                Security.addProvider(new BouncyCastleProvider());
                PEMReader reader = new PEMReader(new StringReader(publicKey));
                publicKeyObj = (PublicKey) reader.readObject();
            } catch (Exception var2) {
                throw new RuntimeException("load publicKey error:", var2);
            }
            try {
                Signature signature = Signature.getInstance("SHA256withRSA");
                signature.initVerify(publicKeyObj);
                signature.update(getBytes(data, charset));
                return signature.verify(Hex.decodeHex(sign.toCharArray()));
            } catch (Exception var5) {
                throw new RuntimeException(var5);
            }
        } catch (Exception var5) {
            throw new RuntimeException(var5);
        }
    }

    private static String parseByte2HexStr(byte[] buf) {
        StringBuilder sb = new StringBuilder();

        for (byte aBuf : buf) {
            String hex = Integer.toHexString(aBuf & 255);
            if (hex.length() == 1) {
                hex = GlobalConstant.STRING_ZERO + hex;
            }
            sb.append(hex.toLowerCase());
        }
        return sb.toString();
    }

    private static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        } else {
            byte[] result = new byte[hexStr.length() / 2];

            for (int i = 0; i < hexStr.length() / 2; ++i) {
                int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
                int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
                result[i] = (byte) (high * 16 + low);
            }
            return result;
        }
    }

    private static byte[] getBytes(String content, String charset) {
        if (content == null) {
            content = GlobalConstant.STRING_EMPTY;
        }
        if (StringUtils.isBlank(charset)) {
            throw new IllegalArgumentException("charset can not null");
        } else {
            try {
                return content.getBytes(charset);
            } catch (UnsupportedEncodingException var3) {
                throw new RuntimeException(String.format("charset is not valid,charset is: %s", charset));
            }
        }
    }


    public static Map<String, Object> getObjectMap(Object object) {
        Map<String, Object> map = Maps.newHashMap();
        try {
            if (object == null) {
                return null;
            }
            BeanInfo beanInfo;
            beanInfo = Introspector.getBeanInfo(object.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (key.compareToIgnoreCase("class") == 0) {
                    continue;
                }
                Method getter = property.getReadMethod();
                Object value = getter != null ? getter.invoke(object) : null;
                map.put(key, value);
            }
        } catch (Exception e) {
            log.warning(e.getMessage());
        }
        return map;
    }


    public static String encryptBase64(byte[] key) {
        return (new BASE64Encoder()).encode(key);
    }


    public static byte[] decryptBase64(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }


    /**
     * SXF RSA验签名检查
     *
     * @param content   待签名数据
     * @param sign      签名值
     * @param publicKey 分配给开发商公钥
     * @return 布尔值
     */
    public static boolean doSxfCheck(String content, String sign, String publicKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(GlobalConstant.STRING_RSA);
            byte[] encodedKey = decryptBase64(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            java.security.Signature signature = java.security.Signature.getInstance(GlobalConstant.STRING_SHA1_WITH_RSA);
            signature.initVerify(pubKey);
            signature.update(content.getBytes());
            return signature.verify(decryptBase64(sign));
        } catch (Exception e) {
            log.warning(e.getMessage());
            return false;
        }
    }


    /**
     * sft 校验
     *
     * @param content
     * @param sign
     * @param aliPublicKey
     * @param inputCharset
     * @return
     */
    public static boolean verify(String content, String sign, String aliPublicKey, String inputCharset) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(GlobalConstant.STRING_RSA);
            byte[] encodedKey = SecureUtil.decode(aliPublicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            java.security.Signature signature = java.security.Signature
                    .getInstance(GlobalConstant.STRING_SHA1_WITH_RSA);
            signature.initVerify(pubKey);
            signature.update(content.getBytes(inputCharset));
            return signature.verify(SecureUtil.decode(sign));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
