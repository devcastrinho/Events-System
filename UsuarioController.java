package controller;

import model.Usuario;
import util.ArquivoUtil;

import java.util.List;
import java.util.Optional;

public class UsuarioController {
    private List<Usuario> usuarios;
    private Usuario usuarioAtual;

    public UsuarioController() {
        this.usuarios = ArquivoUtil.carregarUsuarios();
    }

    public boolean cadastrar(Usuario usuario) {
        boolean jaExiste = usuarios.stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(usuario.getEmail()));
        if (jaExiste) return false;
        usuarios.add(usuario);
        ArquivoUtil.salvarUsuarios(usuarios);
        return true;
    }

    public Optional<Usuario> login(String email) {
        return usuarios.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public void setUsuarioAtual(Usuario u) { this.usuarioAtual = u; }
    public Usuario getUsuarioAtual() { return usuarioAtual; }
    public boolean estaLogado() { return usuarioAtual != null; }

    public void logout() { this.usuarioAtual = null; }

    public void salvar() {
        ArquivoUtil.salvarUsuarios(usuarios);
    }

    public List<Usuario> listarTodos() { return usuarios; }

    public void confirmarPresenca(String eventoId) {
        if (usuarioAtual != null) {
            usuarioAtual.confirmarEvento(eventoId);
            salvar();
        }
    }

    public void cancelarPresenca(String eventoId) {
        if (usuarioAtual != null) {
            usuarioAtual.cancelarPresenca(eventoId);
            salvar();
        }
    }
}
