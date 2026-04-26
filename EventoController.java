package controller;

import enums.Categoria;
import model.Evento;
import model.Usuario;
import util.ArquivoUtil;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class EventoController {
    private List<Evento> eventos;

    public EventoController() {
        this.eventos = ArquivoUtil.carregarEventos();
    }

    public void adicionarEvento(Evento evento) {
        eventos.add(evento);
        ArquivoUtil.salvarEventos(eventos);
    }

    public List<Evento> listarOrdenadosPorHorario() {
        return eventos.stream()
                .filter(e -> !e.jaOcorreu())
                .sorted()
                .collect(Collectors.toList());
    }

    public List<Evento> listarEncerrados() {
        return eventos.stream()
                .filter(Evento::jaOcorreu)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    public List<Evento> listarEmAndamento() {
        return eventos.stream()
                .filter(Evento::estaOcorrendo)
                .collect(Collectors.toList());
    }

    public List<Evento> listarTodos() {
        return new ArrayList<>(eventos);
    }

    public Optional<Evento> buscarPorId(String id) {
        return eventos.stream().filter(e -> e.getId().equals(id)).findFirst();
    }

    public List<Evento> buscarPorCategoria(Categoria cat) {
        return eventos.stream()
                .filter(e -> e.getCategoria() == cat)
                .sorted()
                .collect(Collectors.toList());
    }

    public List<Evento> buscarEventosDoUsuario(Usuario usuario) {
        return usuario.getEventosConfirmados().stream()
                .map(id -> buscarPorId(id).orElse(null))
                .filter(Objects::nonNull)
                .sorted()
                .collect(Collectors.toList());
    }

    public String gerarNovoId() {
        return "EVT-" + System.currentTimeMillis();
    }

    public void salvar() {
        ArquivoUtil.salvarEventos(eventos);
    }

    public int totalEventos() {
        return eventos.size();
    }
}
