package org.raf.usermanagement.security;

import io.jsonwebtoken.Claims;

public interface MyTokenService {
    String generate(Claims claims);

    Claims parseToken(String jwt);
}
