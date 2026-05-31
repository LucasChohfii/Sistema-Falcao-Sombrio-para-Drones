package banco;

import modelo.Telemetria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class TelemetriaDAO {

    @Autowired
    private JdbcTemplate jdbc;

    public void salvar(Telemetria telemetria) {
        String sql = "INSERT INTO telemetria (drone_id, missao_id, posicao_lat, posicao_lon, altitude, velocidade) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        jdbc.update(sql,
                telemetria.getDroneId(),
                telemetria.getMissaoId(),
                telemetria.getPosicaoLat(),
                telemetria.getPosicaoLon(),
                telemetria.getAltitude(),
                telemetria.getVelocidade());
    }

    public List<Map<String, Object>> buscarPorMissao(int missaoId) {
        String sql = "SELECT * FROM telemetria WHERE missao_id = ? ORDER BY registrado_em ASC";
        return jdbc.queryForList(sql, missaoId);
    }

    public List<Map<String, Object>> buscarPorDrone(String droneId) {
        String sql = "SELECT * FROM telemetria WHERE drone_id = ? ORDER BY registrado_em DESC";
        return jdbc.queryForList(sql, droneId);
    }
}
