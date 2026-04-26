package util;

import model.Evento;
import model.Usuario;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ArquivoUtil {
    private static final String ARQUIVO_EVENTOS = "events.data";
    private static final String ARQUIVO_USUARIOS = "users.data";
    private static final String SEP_SECAO = "---EVENTOS---";
    private static final String SEP_USUARIOS = "---USUARIOS---";

    public static void salvarEventos(List<Evento> eventos) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQUIVO_EVENTOS))) {
            pw.println(SEP_SECAO);
            for (Evento e : eventos) {
                pw.println(e.serializar());
            }
        } catch (IOException ex) {
            System.err.println("[ERRO] Não foi possível salvar eventos: " + ex.getMessage());
        }
    }

    public static List<Evento> carregarEventos() {
        List<Evento> lista = new ArrayList<>();
        File f = new File(ARQUIVO_EVENTOS);
        if (!f.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linha;
            boolean dentroSecao = false;
            while ((linha = br.readLine()) != null) {
                if (linha.equals(SEP_SECAO)) { dentroSecao = true; continue; }
                if (dentroSecao && !linha.isBlank()) {
                    Evento ev = Evento.desserializar(linha);
                    if (ev != null) lista.add(ev);
                }
            }
        } catch (IOException ex) {
            System.err.println("[ERRO] Não foi possível carregar eventos: " + ex.getMessage());
        }
        return lista;
    }

    public static void salvarUsuarios(List<Usuario> usuarios) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQUIVO_USUARIOS))) {
            pw.println(SEP_USUARIOS);
            for (Usuario u : usuarios) {
                pw.println(u.serializar());
            }
        } catch (IOException ex) {
            System.err.println("[ERRO] Não foi possível salvar usuários: " + ex.getMessage());
        }
    }

    public static List<Usuario> carregarUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        File f = new File(ARQUIVO_USUARIOS);
        if (!f.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linha;
            boolean dentroSecao = false;
            while ((linha = br.readLine()) != null) {
                if (linha.equals(SEP_USUARIOS)) { dentroSecao = true; continue; }
                if (dentroSecao && !linha.isBlank()) {
                    Usuario u = Usuario.desserializar(linha);
                    if (u != null) lista.add(u);
                }
            }
        } catch (IOException ex) {
            System.err.println("[ERRO] Não foi possível carregar usuários: " + ex.getMessage());
        }
        return lista;
    }
}
