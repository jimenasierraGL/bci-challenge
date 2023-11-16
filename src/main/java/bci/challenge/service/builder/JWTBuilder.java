package bci.challenge.service.builder;


import bci.challenge.exception.ApiException;
import bci.challenge.exception.JWTException;
import bci.challenge.model.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class JWTBuilder {
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    private final ObjectMapper mapper;
    public String createToken(User user) {

        try {
            String jsonUser = mapper.writeValueAsString(user);
            SecretKeySpec secretKey = getSecretKey();
            return Jwts.builder()
                    .content(jsonUser)
                    .signWith(secretKey)
                    .compact();

        } catch (JsonProcessingException e) {
            log.error(e.toString());
            throw new ApiException(e.getMessage());
        } catch (Exception e) {
            log.error(e.toString());
            throw new JWTException("JWT creation failed: " + e.getMessage());
        }
    }

    private SecretKeySpec getSecretKey() {
        byte[] hash = secretKey.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(hash, "HmacSHA256");
    }

    public User getUserFromJWT(String token) {
        try {
            if (token == null || token.isEmpty()) {
                throw new JWTException("Token must not be null");
            }
            String[] split_string = token.split("\\.");
            String base64EncodedBody = split_string[1];
            Base64 base64Url = new Base64(true);
            String body = new String(base64Url.decode(base64EncodedBody));
            return mapper.readValue(body, User.class);
        } catch (Exception e) {
            log.error(e.toString());
            throw new JWTException("JWT creation failed: " + e.getMessage());
        }
    }
}
