package model;

import enums.Categoria;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Evento implements Comparable<Evento> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter SERIALIZER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    private String id;
    private String nome;
    private String endereco;
    private Categoria categoria;
    private LocalDateTime horario;
    private String descricao;
    private String criadorEmail;

    public Evento(String id, String nome, String endereco, Categoria categoria,
                  LocalDateTime horario, String descricao, String criadorEmail) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.categoria = categoria;
        this.horario = horario;
        this.descricao = descricao;
        this.criadorEmail = criadorEmail;
    }

    // Getters
    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getEndereco() { return endereco; }
    public Categoria getCategoria() { return categoria; }
    public LocalDateTime getHorario() { return horario; }
    public String getDescricao() { return descricao; }
    public String getCriadorEmail() { return criadorEmail; }

    public String getStatusTemporal() {
        LocalDateTime agora = LocalDateTime.now();
        if (horario.isBefore(agora.minusHours(3))) {
            return "ENCERRADO";
        } else if (horario.isBefore(agora) && horario.isAfter(agora.minusHours(3))) {
            return "EM ANDAMENTO";
        } else {
            return "PRÓXIMO";
        }
    }

    public boolean jaOcorreu() {
        return horario.isBefore(LocalDateTime.now().minusHours(3));
    }

    public boolean estaOcorrendo() {
        LocalDateTime agora = LocalDateTime.now();
        return horario.isBefore(agora) && horario.isAfter(agora.minusHours(3));
    }

    @Override
    public int compareTo(Evento outro) {
        return this.horario.compareTo(outro.horario);
    }

    public void exibirDetalhes() {
        String status = getStatusTemporal();
        String icone = switch (status) {
            case "EM ANDAMENTO" -> "🔴 ";
            case "ENCERRADO"    -> "⬛ ";
            default             -> "🟢 ";
        };
        System.out.println("  ┌─────────────────────────────────────────────");
        System.out.printf("  │ %s[%s] %s%n", icone, status, nome);
        System.out.printf("  │ 📍 Endereço  : %s%n", endereco);
        System.out.printf("  │ 🏷  Categoria : %s%n", categoria.getDescricao());
        System.out.printf("  │ 🕐 Horário   : %s%n", horario.format(FORMATTER));
        System.out.printf("  │ 📝 Descrição : %s%n", descricao);
        System.out.printf("  │ 🆔 ID        : %s%n", id);
        System.out.println("  └─────────────────────────────────────────────");
    }

    // Serialização para salvar em arquivo
    public String serializar() {
        return id + "|" + nome + "|" + endereco + "|" +
               categoria.name() + "|" + horario.format(SERIALIZER) + "|" +
               descricao + "|" + criadorEmail;
    }

    // Desserialização ao ler do arquivo
    public static Evento desserializar(String linha) {
        String[] p = linha.split("\\|", 7);
        if (p.length < 7) return null;
        try {
            LocalDateTime horario = LocalDateTime.parse(p[4], SERIALIZER);
            Categoria cat = Categoria.valueOf(p[3]);
            return new Evento(p[0], p[1], p[2], cat, horario, p[5], p[6]);
        } catch (Exception e) {
            return null;
        }
    }
}
