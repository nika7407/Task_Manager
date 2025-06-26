package hexlet.code.component;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@ConfigurationProperties(prefix = "rsa")
@Getter
@Setter
@Slf4j
public class RsaKeyProperties {

    private Resource publicKey;
    private Resource privateKey;

    private RSAPublicKey parsedPublicKey;
    private RSAPrivateKey parsedPrivateKey;

    @PostConstruct
    public void init() {
        try {
            this.parsedPublicKey = readPublicKey(publicKey);
            this.parsedPrivateKey = readPrivateKey(privateKey);
        } catch (Exception e) {
            log.error("Failed to load RSA keys", e);
            throw new IllegalStateException("Cannot load RSA keys", e);
        }
    }

    private RSAPublicKey readPublicKey(Resource resource) throws Exception {
        String key = readPemContent(resource.getInputStream());
        byte[] decoded = Base64.getDecoder().decode(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
        return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(keySpec);
    }

    private RSAPrivateKey readPrivateKey(Resource resource) throws Exception {
        String key = readPemContent(resource.getInputStream());
        byte[] decoded = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
        return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(keySpec);
    }

    private String readPemContent(InputStream inputStream) throws Exception {
        String pem = new String(inputStream.readAllBytes());
        return pem
                .replaceAll("-----BEGIN (.*)-----", "")
                .replaceAll("-----END (.*)-----", "")
                .replaceAll("\\s", "");
    }
}
