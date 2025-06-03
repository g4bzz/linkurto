package com.g4bzz.linkurto.core.engines;

import com.g4bzz.linkurto.exception.BadRequestException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashingEngine extends Engine {

    @Override
    public String getUniqueKey(String url) {
        this.daysToExpire = 25;
        return getMd5Hash(url).substring(0, 7);
    }

    private String getMd5Hash(String url) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(url.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
