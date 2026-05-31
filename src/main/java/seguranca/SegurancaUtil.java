package seguranca;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class SegurancaUtil {

    private SegurancaUtil() {}

    public static String gerarHashSHA256(String texto) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("Texto para hash não pode ser vazio.");
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(texto.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar hash de segurança.");
        }
    }
}
