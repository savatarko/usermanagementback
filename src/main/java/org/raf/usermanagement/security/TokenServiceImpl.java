package org.raf.usermanagement.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.raf.usermanagement.db.UserRepository;
import org.raf.usermanagement.domain.User;
import org.raf.usermanagement.exceptions.NoUserException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TokenServiceImpl implements MyTokenService {

    @Value("${oauth.jwt.secret}")
    private String jwtSecret;

    private UserRepository userRepository;

    public TokenServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String generate(Claims claims) {
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    @Override
    public Claims parseToken(String jwt) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
        return claims;
    }

    public User getUserByJwt(String jwt){
        Long id = parseToken(jwt).get("id", Long.class);
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()){
            throw new NoUserException();
        }
        return user.get();
    }
}
