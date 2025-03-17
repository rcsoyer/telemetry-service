package org.acme.telemetryservice.fixtures;

import io.jsonwebtoken.Jwts;
import java.util.Base64;
import javax.crypto.SecretKey;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class PublicKeyUtils {

    public static SecretKey generateSecretKey() {
        return Jwts.SIG.HS512.key().build();
    }

    public static String generateSecretKeyEncoded() {
        return Base64.getEncoder()
                     .encodeToString(generateSecretKey().getEncoded());
    }
}
