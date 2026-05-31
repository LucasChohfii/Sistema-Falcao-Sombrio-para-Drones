package controle;

import modelo.Operador;
import enums.NivelAcesso;
import seguranca.SegurancaUtil;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SistemaAutenticacao {

    private static final Map<String, Operador> operadores = new HashMap<>();
    private static final Map<String, String> codigosOtp = new HashMap<>();
    private static final Map<String, LocalDateTime> expiracaoOtp = new HashMap<>();

    static {
        operadores.put("lucas", new Operador("Lucas", "falcao2026", "bio_lucas", NivelAcesso.ADMINISTRADOR));
        operadores.put("sofia", new Operador("Sofia", "falcao2026", "bio_sofia", NivelAcesso.OPERADOR));
    }

    public static boolean validarCredenciais(String nome, String senha) {
        if (nome == null || nome.isBlank() || senha == null || senha.isBlank()) {
            return false;
        }
        Operador op = operadores.get(nome.toLowerCase().trim());
        if (op == null) {
            return false;
        }
        String senhaHash = SegurancaUtil.gerarHashSHA256(senha);
        return op.getCredencialHash().equals(senhaHash);
    }

    public static String gerarOtp(String nome) {
        String codigo = String.format("%06d", new Random().nextInt(999999));
        codigosOtp.put(nome.toLowerCase(), codigo);
        expiracaoOtp.put(nome.toLowerCase(), LocalDateTime.now().plusSeconds(60));
        return codigo;
    }

    public static boolean validarOtp(String nome, String codigo) {
        if (nome == null || codigo == null) {
            return false;
        }
        String chave = nome.toLowerCase().trim();
        String codigoSalvo = codigosOtp.get(chave);
        LocalDateTime expiracao = expiracaoOtp.get(chave);

        if (codigoSalvo == null || expiracao == null) {
            return false;
        }
        if (LocalDateTime.now().isAfter(expiracao)) {
            codigosOtp.remove(chave);
            expiracaoOtp.remove(chave);
            return false;
        }
        return codigoSalvo.equals(codigo);
    }

    public static Operador buscarOperador(String nome) {
        return operadores.get(nome.toLowerCase().trim());
    }
}
