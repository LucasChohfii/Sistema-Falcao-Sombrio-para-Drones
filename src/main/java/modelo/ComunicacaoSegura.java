package modelo;

import interfaces.IComunicavel;
import java.util.LinkedList;
import java.util.Queue;

public class ComunicacaoSegura implements IComunicavel {

    private String protocolo;
    private String criptografia;
    private final Queue<String> fila;

    public ComunicacaoSegura(String protocolo, String criptografia) {
        if (protocolo == null || protocolo.isBlank()) {
            throw new IllegalArgumentException("Protocolo não pode ser vazio.");
        }
        this.protocolo    = protocolo;
        this.criptografia = criptografia;
        this.fila         = new LinkedList<>();
    }

    @Override
    public void enviarMensagem(String mensagem, String destino) {
        String registro = "[" + protocolo + " | " + criptografia + "] → " + destino + ": " + mensagem;
        fila.add(registro);
        System.out.println(registro);
    }

    @Override
    public String receberMensagem() {
        return fila.poll();
    }

    public String getProtocolo()    { return protocolo; }
    public String getCriptografia() { return criptografia; }
}
