package controle;

import enums.StatusDrone;
import modelo.ComunicacaoSegura;
import modelo.Drone;
import modelo.MissaoBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CentralDeControle {

    private String id;
    private String status;
    private final List<Drone> frota;
    private final ComunicacaoSegura canal;

    public CentralDeControle() {
        this.id     = "CENTRAL-FALCAO-01";
        this.status = "OPERACIONAL";
        this.frota  = new ArrayList<>();
        this.canal  = new ComunicacaoSegura("REST-HTTPS", "SHA-256");
        inicializarFrota();
    }

    private void inicializarFrota() {
        frota.add(new Drone("DRONE-1", "AQUILA-X1", "v2.4.1", 100f));
        frota.add(new Drone("DRONE-2", "AQUILA-X2", "v2.4.1", 100f));
        canal.enviarMensagem("Frota inicializada com " + frota.size() + " drones.", id);
    }

    public void registrarDrone(Drone drone) {
        if (drone == null) {
            throw new IllegalArgumentException("Drone não pode ser nulo.");
        }
        frota.add(drone);
        canal.enviarMensagem("Drone " + drone.getId() + " registrado na frota.", id);
    }

    public Optional<Drone> buscarDrone(String droneId) {
        if (droneId == null || droneId.isBlank()) return Optional.empty();
        return frota.stream()
                .filter(d -> d.getId().equalsIgnoreCase(droneId.trim()))
                .findFirst();
    }

    public List<Drone> listarDronesDisponiveis() {
        return frota.stream()
                .filter(d -> d.getStatus() == StatusDrone.DISPONIVEL)
                .toList();
    }

    public boolean iniciarMissao(String droneId, MissaoBase missao) {
        Optional<Drone> encontrado = buscarDrone(droneId);
        if (encontrado.isEmpty() || encontrado.get().getStatus() != StatusDrone.DISPONIVEL) {
            canal.enviarMensagem("Falha ao iniciar: drone " + droneId + " indisponível.", id);
            return false;
        }
        Drone drone = encontrado.get();
        drone.iniciarMissao();
        missao.iniciar();
        missao.executar();
        drone.enviarTelemetria();
        canal.enviarMensagem("Missão " + missao.getTipo() + " iniciada no drone " + droneId + ".", id);
        return true;
    }

    public void encerrarMissao(String droneId) {
        buscarDrone(droneId).ifPresent(d -> {
            d.retornarBase();
            canal.enviarMensagem("Drone " + droneId + " retornando à base.", id);
        });
    }

    public String monitorarDrones() {
        StringBuilder sb = new StringBuilder();
        for (Drone d : frota) {
            sb.append("[").append(d.getId()).append("] ")
              .append(d.getModelo()).append(" | ")
              .append(d.getStatus()).append(" | Bateria: ")
              .append(d.getBateria()).append("%\n");
        }
        return sb.toString();
    }

    public String getId()     { return id; }
    public String getStatus() { return status; }
    public List<Drone> getFrota() { return frota; }
}
