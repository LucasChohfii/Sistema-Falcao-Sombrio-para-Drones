package modelo;

import enums.StatusMissao;

public class MissaoReconhecimento extends MissaoBase {

    private String areaAlvo;
    private double destinoLat;
    private double destinoLon;

    public MissaoReconhecimento(String id, String droneId, String operadorNome, String areaAlvo, double destinoLat, double destinoLon) {
        super(id, "RECONHECIMENTO", droneId, operadorNome);
        if (areaAlvo == null || areaAlvo.isBlank()) {
            throw new IllegalArgumentException("Área alvo não pode ser vazia.");
        }
        this.areaAlvo    = areaAlvo.trim();
        this.destinoLat  = destinoLat;
        this.destinoLon  = destinoLon;
    }

    @Override
    public void executar() {
        iniciar();
        System.out.println("[MISSÃO] Reconhecimento iniciado — Área: " + areaAlvo);
        System.out.println("[MISSÃO] Coordenadas: " + destinoLat + ", " + destinoLon);
    }

    public String getAreaAlvo()    { return areaAlvo; }
    public double getDestinoLat()  { return destinoLat; }
    public double getDestinoLon()  { return destinoLon; }
}
