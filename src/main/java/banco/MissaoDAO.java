package banco;

import modelo.MissaoBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class MissaoDAO {

    @Autowired
    private JdbcTemplate jdbc;

    public void salvar(MissaoBase missao, double destinoLat, double destinoLon, int duracaoSegundos) {
        String sql = "INSERT INTO missao (tipo, drone_id, operador_nome, destino_lat, destino_lon, duracao_segundos, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbc.update(sql,
                missao.getTipo(),
                missao.getDroneId(),
                missao.getOperadorNome(),
                destinoLat,
                destinoLon,
                duracaoSegundos,
                missao.getStatus().name());
    }

    public List<Map<String, Object>> listarTodas() {
        String sql = "SELECT * FROM missao ORDER BY registrado_em DESC";
        return jdbc.queryForList(sql);
    }
}
