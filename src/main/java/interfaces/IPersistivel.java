package interfaces;

public interface IPersistivel {
    void salvar(Object entidade);
    Object consultar(String id);
}
