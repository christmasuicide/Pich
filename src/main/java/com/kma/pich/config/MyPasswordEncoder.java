package com.kma.pich.config;

import lombok.SneakyThrows;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;

@Component
public class MyPasswordEncoder implements PasswordEncoder {

    @SneakyThrows
    @Override
    public String encode(final CharSequence rawPassword) {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(rawPassword.toString().getBytes());
        byte[] digest = md.digest();
        return DatatypeConverter
                .printHexBinary(digest).toUpperCase();
    }

    @Override
    public boolean matches(final CharSequence rawPassword, final String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }
}
