package modelo;

import interfaces.IPersistivel;
import java.util.HashMap;
import java.util.Map;

public class BancoDeDados implements IPersistivel {

    private String tipo;
    private final Map<String, Object> armazenamento;

    public BancoDeDados(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("Tipo não pode ser vazio.");
        }
        this.tipo          = tipo;
        this.armazenamento = new HashMap<>();
    }

    @Override
    public void salvar(Object entidade) {
        if (entidade == null) {
            throw new IllegalArgumentException("Entidade não pode ser nula.");
        }
        String chave = entidade.getClass().getSimpleName() + "_" + System.currentTimeMillis();
        armazenamento.put(chave, entidade);
        System.out.println("[BD-" + tipo + "] Salvo: " + chave);
    }

    @Override
    public Object consultar(String id) {
        if (id == null || id.isBlank()) return null;
        return armazenamento.get(id);
    }

    public void replicar() {
        System.out.println("[BD-" + tipo + "] Replicando " + armazenamento.size() + " registros.");
    }

    public String getTipo() { return tipo; }
}
