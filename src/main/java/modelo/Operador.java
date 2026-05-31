package modelo;

import enums.NivelAcesso;
import interfaces.IAutenticavel;
import seguranca.SegurancaUtil;

public class Operador implements IAutenticavel {

    private String nome;
    private String credencialHash;
    private String biometriaHash;
    private NivelAcesso nivelAcesso;
    private boolean ativo;

    public Operador(String nome, String credencial, String biometria, NivelAcesso nivelAcesso) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do operador não pode ser vazio.");
        }
        if (credencial == null || credencial.length() < 6) {
            throw new IllegalArgumentException("Credencial deve ter ao menos 6 caracteres.");
        }
        if (biometria == null || biometria.isBlank()) {
            throw new IllegalArgumentException("Biometria não pode ser vazia.");
        }
        this.nome          = nome.trim();
        this.credencialHash = SegurancaUtil.gerarHashSHA256(credencial);
        this.biometriaHash  = SegurancaUtil.gerarHashSHA256(biometria);
        this.nivelAcesso   = nivelAcesso;
        this.ativo         = true;
    }

    @Override
    public boolean autenticar(String credencial, String biometria) {
        String hashCred = SegurancaUtil.gerarHashSHA256(credencial);
        String hashBio  = SegurancaUtil.gerarHashSHA256(biometria);
        return this.credencialHash.equals(hashCred) && this.biometriaHash.equals(hashBio);
    }

    public String getNome()             { return nome; }
    public String getCredencialHash()   { return credencialHash; }
    public String getBiometriaHash()    { return biometriaHash; }
    public NivelAcesso getNivelAcesso() { return nivelAcesso; }
    public boolean isAtivo()            { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}
