package com.aaronsalvador.gastos.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private static final String SECRET =
            Encoders.BASE64.encode(Jwts.SIG.HS256.key().build().getEncoded());

    @Test
    void generaYValidaUnToken() {
        JwtService service = new JwtService(SECRET, 60_000);

        String token = service.generateToken("aaron@test.com");

        assertTrue(service.isValid(token));
        assertEquals("aaron@test.com", service.extractEmail(token));
    }

    @Test
    void rechazaUnTokenManipulado() {
        JwtService service = new JwtService(SECRET, 60_000);
        String token = service.generateToken("aaron@test.com");

        assertFalse(service.isValid(token + "x"));
    }

    @Test
    void rechazaUnTokenExpirado() {
        JwtService service = new JwtService(SECRET, -1_000);
        String token = service.generateToken("aaron@test.com");

        assertFalse(service.isValid(token));
    }
}
