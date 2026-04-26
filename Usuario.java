package model;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private String nome;
    private String email;
    private String telefone;
    private String cidade;
    private String bairro;
    private List<String> eventosConfirmados; // IDs dos eventos

    public Usuario(String nome, String email, String telefone, String cidade, String bairro) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.cidade = cidade;
        this.bairro = bairro;
        this.eventosConfirmados = new ArrayList<>();
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public List<String> getEventosConfirmados() { return eventosConfirmados; }

    public void confirmarEvento(String eventoId) {
        if (!eventosConfirmados.contains(eventoId)) {
            eventosConfirmados.add(eventoId);
        }
    }

    public void cancelarPresenca(String eventoId) {
        eventosConfirmados.remove(eventoId);
    }

    public boolean participaDoEvento(String eventoId) {
        return eventosConfirmados.contains(eventoId);
    }

    @Override
    public String toString() {
        return String.format("Nome: %s | Email: %s | Tel: %s | Cidade: %s | Bairro: %s",
                nome, email, telefone, cidade, bairro);
    }

    // Serialização para salvar em arquivo
    public String serializar() {
        String eventos = String.join(";", eventosConfirmados);
        return nome + "|" + email + "|" + telefone + "|" + cidade + "|" + bairro + "|" + eventos;
    }

    // Desserialização ao ler do arquivo
    public static Usuario desserializar(String linha) {
        String[] partes = linha.split("\\|", -1);
        if (partes.length < 5) return null;
        Usuario u = new Usuario(partes[0], partes[1], partes[2], partes[3], partes[4]);
        if (partes.length == 6 && !partes[5].isEmpty()) {
            for (String id : partes[5].split(";")) {
                u.confirmarEvento(id);
            }
        }
        return u;
    }
}
