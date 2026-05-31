package modelo;

public class MissaoAtaque extends MissaoBase {

    private double coordenadasLat;
    private double coordenadasLon;
    private String nivelAmeaca;

    public MissaoAtaque(String id, String droneId, String operadorNome, double coordenadasLat, double coordenadasLon, String nivelAmeaca) {
        super(id, "ATAQUE", droneId, operadorNome);
        if (nivelAmeaca == null || nivelAmeaca.isBlank()) {
            throw new IllegalArgumentException("Nível de ameaça não pode ser vazio.");
        }
        this.coordenadasLat = coordenadasLat;
        this.coordenadasLon = coordenadasLon;
        this.nivelAmeaca    = nivelAmeaca.trim();
    }

    @Override
    public void executar() {
        iniciar();
        System.out.println("[MISSÃO] Ataque iniciado — Nível de ameaça: " + nivelAmeaca);
        System.out.println("[MISSÃO] Coordenadas do alvo: " + coordenadasLat + ", " + coordenadasLon);
    }

    public double getCoordenadasLat() { return coordenadasLat; }
    public double getCoordenadasLon() { return coordenadasLon; }
    public String getNivelAmeaca()    { return nivelAmeaca; }
}
