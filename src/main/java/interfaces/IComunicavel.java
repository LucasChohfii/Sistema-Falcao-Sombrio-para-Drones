package interfaces;

public interface IComunicavel {
    void enviarMensagem(String mensagem, String destino);
    String receberMensagem();
}
