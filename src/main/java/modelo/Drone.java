package modelo;

import enums.StatusDrone;
import interfaces.IRastreavel;

public class Drone implements IRastreavel {

    private String id;
    private String modelo;
    private StatusDrone status;
    private float bateria;
    private String firmwareVersao;

    public Drone(String id, String modelo, String firmwareVersao, float bateria) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID do drone não pode ser vazio.");
        }
        if (modelo == null || modelo.isBlank()) {
            throw new IllegalArgumentException("Modelo do drone não pode ser vazio.");
        }
        if (bateria < 0 || bateria > 100) {
            throw new IllegalArgumentException("Bateria deve estar entre 0 e 100.");
        }
        this.id             = id.trim();
        this.modelo         = modelo.trim();
        this.firmwareVersao = firmwareVersao;
        this.bateria        = bateria;
        this.status         = StatusDrone.DISPONIVEL;
    }

    public void iniciarMissao() {
        this.status = StatusDrone.EM_MISSAO;
        System.out.println("[DRONE] " + id + " missão iniciada.");
    }

    public void retornarBase() {
        this.status = StatusDrone.RETORNANDO;
        System.out.println("[DRONE] " + id + " retornando à base.");
    }

    @Override
    public void enviarTelemetria() {
        System.out.println("[TELEMETRIA] Drone " + id + " | Status: " + status + " | Bateria: " + bateria + "%");
    }

    public String getId()               { return id; }
    public String getModelo()           { return modelo; }
    public StatusDrone getStatus()      { return status; }
    public float getBateria()           { return bateria; }
    public String getFirmwareVersao()   { return firmwareVersao; }

    public void setStatus(StatusDrone status) { this.status = status; }

    public void setBateria(float bateria) {
        if (bateria < 0 || bateria > 100) {
            throw new IllegalArgumentException("Bateria inválida.");
        }
        this.bateria = bateria;
    }
}
