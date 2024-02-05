package org.raf.usermanagement.security;

import io.jsonwebtoken.Claims;
import org.raf.usermanagement.domain.User;

public interface MyTokenService {
    String generate(Claims claims);

    Claims parseToken(String jwt);

    User getUserByJwt(String jwt);
}
