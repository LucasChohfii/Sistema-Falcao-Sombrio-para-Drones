package banco;

import modelo.LogAuditoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class LogAuditoriaDAO {

    @Autowired
    private JdbcTemplate jdbc;

    public void salvar(LogAuditoria log) {
        String sql = "INSERT INTO log_auditoria (evento, operador_nome, missao_id, hash_anterior, hash_atual) " +
                     "VALUES (?, ?, ?, ?, ?)";
        jdbc.update(sql,
                log.getEvento(),
                log.getOperadorNome(),
                log.getMissaoId(),
                log.getHashAnterior(),
                log.getHashAtual());
    }

    public List<Map<String, Object>> listarTodos() {
        String sql = "SELECT * FROM log_auditoria ORDER BY registrado_em DESC";
        return jdbc.queryForList(sql);
    }

    public String buscarUltimoHash() {
        String sql = "SELECT hash_atual FROM log_auditoria ORDER BY registrado_em DESC LIMIT 1";
        List<Map<String, Object>> resultado = jdbc.queryForList(sql);
        if (resultado.isEmpty()) {
            return "0000000000000000";
        }
        return (String) resultado.get(0).get("hash_atual");
    }
}
