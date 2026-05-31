package modelo;

import seguranca.SegurancaUtil;
import java.time.LocalDateTime;

public class LogAuditoria {

    private String evento;
    private String operadorNome;
    private String missaoId;
    private LocalDateTime registradoEm;
    private String hashAnterior;
    private String hashAtual;

    public LogAuditoria(String evento, String operadorNome, String missaoId, String hashAnterior) {
        if (evento == null || evento.isBlank()) {
            throw new IllegalArgumentException("Evento não pode ser vazio.");
        }
        if (operadorNome == null || operadorNome.isBlank()) {
            throw new IllegalArgumentException("Nome do operador não pode ser vazio.");
        }
        this.evento        = evento.trim();
        this.operadorNome  = operadorNome.trim();
        this.missaoId      = missaoId;
        this.registradoEm  = LocalDateTime.now();
        this.hashAnterior  = hashAnterior;
        this.hashAtual     = SegurancaUtil.gerarHashSHA256(evento + operadorNome + registradoEm + hashAnterior);
    }

    public String getEvento()            { return evento; }
    public String getOperadorNome()      { return operadorNome; }
    public String getMissaoId()          { return missaoId; }
    public LocalDateTime getRegistradoEm(){ return registradoEm; }
    public String getHashAnterior()      { return hashAnterior; }
    public String getHashAtual()         { return hashAtual; }
}
