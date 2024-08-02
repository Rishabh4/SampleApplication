package com.rishabh.SampleApplication.util;

import com.rishabh.SampleApplication.service.UserDetailsServiceImpl;
import com.rishabh.SampleApplication.model.dto.UserDto;
import com.rishabh.SampleApplication.exception.CustomException;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Log4j2
@RequiredArgsConstructor
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expiration;

    @PostConstruct
    protected void init() {
        // this is to avoid having the raw secret key available in the JVM
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    @Autowired
    private final UserDetailsServiceImpl userDetailsService;

    public String generateToken(UserDto user) {
        return Jwts.builder()
                .setSubject(user.getLogin())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (expiration * Constants.MINUTE_TO_MILLISECONDS)))
                .claim("firstName", user.getFirstName())
                .claim("lastName", user.getLastName())
                .claim("role", user.getRoles())
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public UsernamePasswordAuthenticationToken getAuthenticationToken(final String token) throws CustomException {
        try {
            Claims claim = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            final Collection<? extends GrantedAuthority> authorities =
                    Arrays.stream(claim.get("role").toString().split(","))
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
            return new UsernamePasswordAuthenticationToken(userDetailsService.loadUserByUsername(claim.getSubject()), "", authorities);
        } catch (ExpiredJwtException e) {
            log.error("Jwt expired", e);
            throw new CustomException("JWT token is expired", HttpStatus.BAD_REQUEST);
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            log.error("Error processing JWT token", e);
            throw new CustomException("Error processing JWT token", HttpStatus.BAD_REQUEST);
        }
    }

    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

}
