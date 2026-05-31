package controle;

import banco.LogAuditoriaDAO;
import banco.MissaoDAO;
import banco.TelemetriaDAO;
import modelo.LogAuditoria;
import modelo.MissaoAtaque;
import modelo.MissaoBase;
import modelo.MissaoReconhecimento;
import modelo.Operador;
import modelo.Telemetria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class FalcaoController {

    @Autowired
    private MissaoDAO missaoDAO;

    @Autowired
    private TelemetriaDAO telemetriaDAO;

    @Autowired
    private LogAuditoriaDAO logAuditoriaDAO;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String nome, @RequestParam String senha) {
        try {
            if (nome == null || nome.isBlank() || senha == null || senha.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("erro", "Campos obrigatórios não preenchidos."));
            }
            if (!SistemaAutenticacao.validarCredenciais(nome, senha)) {
                return ResponseEntity.status(401).body(Map.of("erro", "Credenciais inválidas."));
            }
            String otp = SistemaAutenticacao.gerarOtp(nome);
            return ResponseEntity.ok(Map.of("status", "aguardando_mfa", "otp", otp));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("erro", "Erro interno do servidor."));
        }
    }

    @PostMapping("/renovar-otp")
    public ResponseEntity<?> renovarOtp(@RequestParam String nome) {
        try {
            if (nome == null || nome.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("erro", "Nome inválido."));
            }
            String otp = SistemaAutenticacao.gerarOtp(nome);
            return ResponseEntity.ok(Map.of("otp", otp));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("erro", "Erro interno do servidor."));
        }
    }

    @PostMapping("/verificar-mfa")
    public ResponseEntity<?> verificarMfa(@RequestParam String nome, @RequestParam String codigo) {
        try {
            if (!SistemaAutenticacao.validarOtp(nome, codigo)) {
                return ResponseEntity.status(401).body(Map.of("erro", "Código MFA inválido ou expirado."));
            }
            Operador op = SistemaAutenticacao.buscarOperador(nome);
            String hashAnterior = logAuditoriaDAO.buscarUltimoHash();
            LogAuditoria log = new LogAuditoria("Login realizado por " + op.getNome(), op.getNome(), null, hashAnterior);
            logAuditoriaDAO.salvar(log);
            return ResponseEntity.ok(Map.of(
                    "status", "autorizado",
                    "operador", op.getNome(),
                    "nivelAcesso", op.getNivelAcesso().name()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("erro", "Erro interno do servidor."));
        }
    }

    @PostMapping("/missao")
    public ResponseEntity<?> salvarMissao(@RequestBody Map<String, Object> body) {
        try {
            String tipo         = (String) body.get("tipo");
            String droneId      = (String) body.get("droneId");
            String operadorNome = (String) body.get("operadorNome");
            double destinoLat   = ((Number) body.get("destinoLat")).doubleValue();
            double destinoLon   = ((Number) body.get("destinoLon")).doubleValue();
            int duracao         = ((Number) body.get("duracaoSegundos")).intValue();

            if (tipo == null || droneId == null || operadorNome == null) {
                return ResponseEntity.badRequest().body(Map.of("erro", "Dados da missão incompletos."));
            }

            MissaoBase missao;
            String id = UUID.randomUUID().toString();

            if (tipo.equalsIgnoreCase("RECONHECIMENTO")) {
                String areaAlvo = (String) body.getOrDefault("areaAlvo", "Área desconhecida");
                missao = new MissaoReconhecimento(id, droneId, operadorNome, areaAlvo, destinoLat, destinoLon);
            } else if (tipo.equalsIgnoreCase("ATAQUE")) {
                String nivelAmeaca = (String) body.getOrDefault("nivelAmeaca", "ALTO");
                missao = new MissaoAtaque(id, droneId, operadorNome, destinoLat, destinoLon, nivelAmeaca);
            } else {
                return ResponseEntity.badRequest().body(Map.of("erro", "Tipo de missão inválido."));
            }

            missao.executar();
            missao.encerrar(enums.StatusMissao.CONCLUIDA);
            missaoDAO.salvar(missao, destinoLat, destinoLon, duracao);

            String hashAnterior = logAuditoriaDAO.buscarUltimoHash();
            LogAuditoria log = new LogAuditoria("Missão " + tipo + " concluída por " + operadorNome, operadorNome, null, hashAnterior);
            logAuditoriaDAO.salvar(log);

            return ResponseEntity.ok(Map.of("status", "salvo", "mensagem", "Missão registrada com sucesso."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("erro", "Erro ao salvar missão."));
        }
    }

    @PostMapping("/telemetria")
    public ResponseEntity<?> salvarTelemetria(@RequestBody Map<String, Object> body) {
        try {
            String droneId    = (String) body.get("droneId");
            double lat        = ((Number) body.get("posicaoLat")).doubleValue();
            double lon        = ((Number) body.get("posicaoLon")).doubleValue();
            float altitude    = ((Number) body.get("altitude")).floatValue();
            float velocidade  = ((Number) body.get("velocidade")).floatValue();

            Telemetria telemetria = new Telemetria(droneId, null, lat, lon, altitude, velocidade);
            telemetriaDAO.salvar(telemetria);

            return ResponseEntity.ok(Map.of("status", "salvo"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("erro", "Erro ao salvar telemetria."));
        }
    }

    @GetMapping("/missoes")
    public ResponseEntity<?> listarMissoes() {
        try {
            List<Map<String, Object>> missoes = missaoDAO.listarTodas();
            return ResponseEntity.ok(missoes);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("erro", "Erro ao consultar missões."));
        }
    }

    @GetMapping("/logs")
    public ResponseEntity<?> listarLogs() {
        try {
            List<Map<String, Object>> logs = logAuditoriaDAO.listarTodos();
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("erro", "Erro ao consultar logs."));
        }
    }
}
