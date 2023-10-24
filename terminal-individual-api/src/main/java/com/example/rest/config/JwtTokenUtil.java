package com.example.rest.config;

import io.jsonwebtoken.*;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class JwtTokenUtil implements Serializable {

    private long jwtValidityPeriod;
    private String jwtSecretKey;

    public JwtTokenUtil(long jwtValidityPeriod, String jwtSecretKey){
        this.jwtValidityPeriod = jwtValidityPeriod;
        this.jwtSecretKey = jwtSecretKey;
    }

    private String doGenerateToken(Map<String, Object> claims, String issuer, String subject, String audience, String id){
        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(issuer)
                .setSubject(subject)
                .setAudience(audience)
                .setId(id)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + this.jwtValidityPeriod * 1000L))
                .signWith(SignatureAlgorithm.HS512, this.jwtSecretKey).compact();
    }

    private Claims getAllClaimsFromToken(String token){
        return Jwts.parser().setSigningKey(this.jwtSecretKey).parseClaimsJws(token).getBody();
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver){
        Claims claims = this.getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(String subject){
        Map<String, Object> claims = new HashMap<>();
        return this.doGenerateToken(claims, null, subject, null, null);
    }

    public String generateToken(String subject, String id){
        Map<String, Object> claims = new HashMap<>();
        return this.doGenerateToken(claims, null, subject, null, id);
    }

    public String getIssuerFromToken(String token){
        return getClaimFromToken(token, Claims::getIssuer);
    }

    public String getSubjectFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }

    public String getAudienceFromToken(String token){
        return getClaimFromToken(token, Claims::getAudience);
    }

    public Date getExpirationFromToken(String token){
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public Date getIssuedAtFromToken(String token){
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    public String getIdFromToken(String token){
        return getClaimFromToken(token, Claims::getId);
    }

    public Boolean validate(String token){
        try{
            this.getAllClaimsFromToken(token);
            return true;
        }catch(UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException | ExpiredJwtException var3){
            throw var3;
        }

    }



}
