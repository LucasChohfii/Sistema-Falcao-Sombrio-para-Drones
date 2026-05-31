package modelo;

public class SistemaNavegacao {

    private String modo;

    public SistemaNavegacao(String modo) {
        if (modo == null || modo.isBlank()) {
            throw new IllegalArgumentException("Modo de navegação não pode ser vazio.");
        }
        this.modo = modo;
    }

    public void navegarAutonomo(double destinoLat, double destinoLon) {
        System.out.println("[NAV-" + modo + "] Navegando para (" + destinoLat + ", " + destinoLon + ").");
    }

    public boolean detectarAmeaca() {
        System.out.println("[NAV-" + modo + "] Escaneando área em busca de ameaças.");
        return false;
    }

    public void realizarEvasao() {
        System.out.println("[NAV-" + modo + "] Ameaça detectada. Evasão iniciada — rota alternativa calculada.");
    }

    public String getModo()        { return modo; }
    public void setModo(String modo) { this.modo = modo; }
}
