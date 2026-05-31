package modelo;

import enums.StatusMissao;
import java.time.LocalDateTime;

public abstract class MissaoBase {

    private String id;
    private String tipo;
    private StatusMissao status;
    private LocalDateTime inicio;
    private LocalDateTime fim;
    private String droneId;
    private String operadorNome;

    public MissaoBase(String id, String tipo, String droneId, String operadorNome) {
        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("Tipo de missão não pode ser vazio.");
        }
        if (droneId == null || droneId.isBlank()) {
            throw new IllegalArgumentException("ID do drone não pode ser vazio.");
        }
        if (operadorNome == null || operadorNome.isBlank()) {
            throw new IllegalArgumentException("Nome do operador não pode ser vazio.");
        }
        this.id           = id;
        this.tipo         = tipo.trim();
        this.droneId      = droneId.trim();
        this.operadorNome = operadorNome.trim();
        this.status       = StatusMissao.PLANEJADA;
        this.inicio       = LocalDateTime.now();
    }

    public abstract void executar();

    public void iniciar() {
        this.status = StatusMissao.EM_ANDAMENTO;
    }

    public void encerrar(StatusMissao novoStatus) {
        if (novoStatus == StatusMissao.PLANEJADA || novoStatus == StatusMissao.EM_ANDAMENTO) {
            throw new IllegalArgumentException("Status de encerramento inválido.");
        }
        this.status = novoStatus;
        this.fim    = LocalDateTime.now();
    }

    public String getId()              { return id; }
    public String getTipo()            { return tipo; }
    public StatusMissao getStatus()    { return status; }
    public LocalDateTime getInicio()   { return inicio; }
    public LocalDateTime getFim()      { return fim; }
    public String getDroneId()         { return droneId; }
    public String getOperadorNome()    { return operadorNome; }
}
