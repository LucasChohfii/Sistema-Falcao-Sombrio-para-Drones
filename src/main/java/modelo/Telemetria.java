package modelo;

import java.time.LocalDateTime;

public class Telemetria {

    private String droneId;
    private String missaoId;
    private double posicaoLat;
    private double posicaoLon;
    private float altitude;
    private float velocidade;
    private LocalDateTime registradoEm;

    public Telemetria(String droneId, String missaoId, double posicaoLat, double posicaoLon, float altitude, float velocidade) {
        if (droneId == null || droneId.isBlank()) {
            throw new IllegalArgumentException("ID do drone não pode ser vazio.");
        }
        if (velocidade < 0) {
            throw new IllegalArgumentException("Velocidade não pode ser negativa.");
        }
        if (altitude < 0) {
            throw new IllegalArgumentException("Altitude não pode ser negativa.");
        }
        this.droneId      = droneId.trim();
        this.missaoId     = missaoId;
        this.posicaoLat   = posicaoLat;
        this.posicaoLon   = posicaoLon;
        this.altitude     = altitude;
        this.velocidade   = velocidade;
        this.registradoEm = LocalDateTime.now();
    }

    public String getDroneId()            { return droneId; }
    public String getMissaoId()           { return missaoId; }
    public double getPosicaoLat()         { return posicaoLat; }
    public double getPosicaoLon()         { return posicaoLon; }
    public float getAltitude()            { return altitude; }
    public float getVelocidade()          { return velocidade; }
    public LocalDateTime getRegistradoEm(){ return registradoEm; }
}
