package view;

import controller.EventoController;
import controller.UsuarioController;
import enums.Categoria;
import model.Evento;
import model.Usuario;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleView {
    private static final String LINHA = "═══════════════════════════════════════════════════════";
    private static final DateTimeFormatter FMT_ENTRADA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter FMT_EXIBIR  = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final Scanner sc = new Scanner(System.in);
    private final EventoController eventoCtrl;
    private final UsuarioController usuarioCtrl;

    public ConsoleView(EventoController eventoCtrl, UsuarioController usuarioCtrl) {
        this.eventoCtrl = eventoCtrl;
        this.usuarioCtrl = usuarioCtrl;
    }

    // ─────────────────── LOOP PRINCIPAL ───────────────────
    public void iniciar() {
        limpar();
        exibirBanner();
        System.out.printf("  %d evento(s) carregado(s) de events.data%n%n", eventoCtrl.totalEventos());

        while (true) {
            if (!usuarioCtrl.estaLogado()) {
                menuInicial();
            } else {
                menuPrincipal();
            }
        }
    }

    // ─────────────────── MENUS ────────────────────────────
    private void menuInicial() {
        System.out.println(LINHA);
        System.out.println("  ACESSO AO SISTEMA");
        System.out.println(LINHA);
        System.out.println("  1. Fazer login");
        System.out.println("  2. Cadastrar novo usuário");
        System.out.println("  0. Sair");
        System.out.println(LINHA);

        int op = lerInteiro("  Opção: ");
        switch (op) {
            case 1 -> telaLogin();
            case 2 -> telaCadastroUsuario();
            case 0 -> sair();
            default -> aviso("Opção inválida.");
        }
    }

    private void menuPrincipal() {
        Usuario u = usuarioCtrl.getUsuarioAtual();
        System.out.println("\n" + LINHA);
        System.out.printf("  Olá, %s! | %s%n", u.getNome(), u.getCidade());
        System.out.println(LINHA);
        System.out.println("  1. Ver eventos disponíveis");
        System.out.println("  2. Ver eventos em andamento AGORA");
        System.out.println("  3. Ver eventos encerrados");
        System.out.println("  4. Buscar eventos por categoria");
        System.out.println("  5. Meus eventos confirmados");
        System.out.println("  6. Cadastrar novo evento");
        System.out.println("  7. Meu perfil");
        System.out.println("  8. Logout");
        System.out.println("  0. Sair");
        System.out.println(LINHA);

        int op = lerInteiro("  Opção: ");
        switch (op) {
            case 1 -> telaEventosDisponiveis();
            case 2 -> telaEventosAoVivo();
            case 3 -> telaEventosEncerrados();
            case 4 -> telaBuscarCategoria();
            case 5 -> telaMeusEventos();
            case 6 -> telaCadastrarEvento();
            case 7 -> telaMeuPerfil();
            case 8 -> { usuarioCtrl.logout(); sucesso("Logout realizado."); }
            case 0 -> sair();
            default -> aviso("Opção inválida.");
        }
    }

    // ─────────────────── TELAS ────────────────────────────
    private void telaLogin() {
        titulo("LOGIN");
        System.out.print("  Email: ");
        String email = sc.nextLine().trim();

        Optional<Usuario> resultado = usuarioCtrl.login(email);
        if (resultado.isPresent()) {
            usuarioCtrl.setUsuarioAtual(resultado.get());
            sucesso("Bem-vindo(a), " + resultado.get().getNome() + "!");
        } else {
            aviso("Usuário não encontrado. Verifique o email ou cadastre-se.");
        }
    }

    private void telaCadastroUsuario() {
        titulo("CADASTRO DE USUÁRIO");
        System.out.print("  Nome completo  : ");
        String nome = sc.nextLine().trim();

        System.out.print("  Email          : ");
        String email = sc.nextLine().trim();

        System.out.print("  Telefone       : ");
        String tel = sc.nextLine().trim();

        System.out.print("  Cidade         : ");
        String cidade = sc.nextLine().trim();

        System.out.print("  Bairro         : ");
        String bairro = sc.nextLine().trim();

        if (nome.isEmpty() || email.isEmpty()) {
            aviso("Nome e email são obrigatórios.");
            return;
        }

        Usuario novo = new Usuario(nome, email, tel, cidade, bairro);
        if (usuarioCtrl.cadastrar(novo)) {
            sucesso("Usuário cadastrado com sucesso!");
            usuarioCtrl.setUsuarioAtual(novo);
        } else {
            aviso("Já existe um usuário com este email.");
        }
    }

    private void telaEventosDisponiveis() {
        titulo("PRÓXIMOS EVENTOS");
        List<Evento> lista = eventoCtrl.listarOrdenadosPorHorario();

        if (lista.isEmpty()) {
            aviso("Nenhum evento futuro cadastrado.");
            return;
        }

        for (int i = 0; i < lista.size(); i++) {
            System.out.printf("%n  [%d]%n", i + 1);
            lista.get(i).exibirDetalhes();
        }

        System.out.println("\n  Deseja confirmar presença em algum evento?");
        System.out.println("  Digite o número do evento ou 0 para voltar.");
        int escolha = lerInteiro("  Opção: ");

        if (escolha > 0 && escolha <= lista.size()) {
            Evento ev = lista.get(escolha - 1);
            Usuario u = usuarioCtrl.getUsuarioAtual();
            if (u.participaDoEvento(ev.getId())) {
                aviso("Você já confirmou presença neste evento.");
            } else {
                usuarioCtrl.confirmarPresenca(ev.getId());
                sucesso("Presença confirmada em: " + ev.getNome());
            }
        }
    }

    private void telaEventosAoVivo() {
        titulo("🔴 EVENTOS EM ANDAMENTO AGORA");
        List<Evento> lista = eventoCtrl.listarEmAndamento();

        if (lista.isEmpty()) {
            System.out.println("  Nenhum evento está ocorrendo no momento.");
        } else {
            System.out.println("  Os seguintes eventos estão acontecendo agora:\n");
            lista.forEach(Evento::exibirDetalhes);
        }
        pausar();
    }

    private void telaEventosEncerrados() {
        titulo("EVENTOS ENCERRADOS");
        List<Evento> lista = eventoCtrl.listarEncerrados();

        if (lista.isEmpty()) {
            aviso("Nenhum evento encerrado registrado.");
            return;
        }

        lista.forEach(e -> {
            System.out.println();
            e.exibirDetalhes();
        });
        pausar();
    }

    private void telaBuscarCategoria() {
        titulo("BUSCAR POR CATEGORIA");
        Categoria.listar();
        int op = lerInteiro("  Escolha uma categoria: ");

        try {
            Categoria cat = Categoria.fromIndice(op);
            List<Evento> lista = eventoCtrl.buscarPorCategoria(cat);
            System.out.printf("%n  Eventos na categoria \"%s\":%n", cat.getDescricao());

            if (lista.isEmpty()) {
                aviso("Nenhum evento nesta categoria.");
                return;
            }

            for (int i = 0; i < lista.size(); i++) {
                System.out.printf("%n  [%d]%n", i + 1);
                lista.get(i).exibirDetalhes();
            }

            System.out.println("\n  Confirmar presença? (número do evento, ou 0 para voltar)");
            int escolha = lerInteiro("  Opção: ");
            if (escolha > 0 && escolha <= lista.size()) {
                Evento ev = lista.get(escolha - 1);
                if (usuarioCtrl.getUsuarioAtual().participaDoEvento(ev.getId())) {
                    aviso("Você já está inscrito neste evento.");
                } else {
                    usuarioCtrl.confirmarPresenca(ev.getId());
                    sucesso("Presença confirmada em: " + ev.getNome());
                }
            }
        } catch (IllegalArgumentException e) {
            aviso("Categoria inválida.");
        }
    }

    private void telaMeusEventos() {
        titulo("MEUS EVENTOS CONFIRMADOS");
        List<Evento> lista = eventoCtrl.buscarEventosDoUsuario(usuarioCtrl.getUsuarioAtual());

        if (lista.isEmpty()) {
            aviso("Você não confirmou presença em nenhum evento.");
            return;
        }

        for (int i = 0; i < lista.size(); i++) {
            System.out.printf("%n  [%d]%n", i + 1);
            lista.get(i).exibirDetalhes();
        }

        System.out.println("\n  Deseja cancelar presença em algum evento?");
        System.out.println("  Digite o número do evento ou 0 para voltar.");
        int escolha = lerInteiro("  Opção: ");

        if (escolha > 0 && escolha <= lista.size()) {
            Evento ev = lista.get(escolha - 1);
            usuarioCtrl.cancelarPresenca(ev.getId());
            sucesso("Presença cancelada em: " + ev.getNome());
        }
    }

    private void telaCadastrarEvento() {
        titulo("CADASTRAR NOVO EVENTO");

        System.out.print("  Nome do evento    : ");
        String nome = sc.nextLine().trim();

        System.out.print("  Endereço          : ");
        String endereco = sc.nextLine().trim();

        System.out.println("  Categoria:");
        Categoria.listar();
        int catOp = lerInteiro("  Escolha a categoria: ");
        Categoria categoria;
        try {
            categoria = Categoria.fromIndice(catOp);
        } catch (IllegalArgumentException e) {
            aviso("Categoria inválida.");
            return;
        }

        System.out.print("  Horário (dd/MM/yyyy HH:mm): ");
        String horarioStr = sc.nextLine().trim();
        LocalDateTime horario;
        try {
            horario = LocalDateTime.parse(horarioStr, FMT_ENTRADA);
        } catch (DateTimeParseException e) {
            aviso("Formato de data inválido. Use dd/MM/yyyy HH:mm");
            return;
        }

        System.out.print("  Descrição         : ");
        String descricao = sc.nextLine().trim();

        if (nome.isEmpty() || endereco.isEmpty() || descricao.isEmpty()) {
            aviso("Todos os campos são obrigatórios.");
            return;
        }

        String id = eventoCtrl.gerarNovoId();
        String criador = usuarioCtrl.getUsuarioAtual().getEmail();
        Evento novo = new Evento(id, nome, endereco, categoria, horario, descricao, criador);
        eventoCtrl.adicionarEvento(novo);
        sucesso("Evento \"" + nome + "\" cadastrado com sucesso! ID: " + id);
    }

    private void telaMeuPerfil() {
        titulo("MEU PERFIL");
        Usuario u = usuarioCtrl.getUsuarioAtual();
        System.out.println("  " + u);
        System.out.printf("  Eventos confirmados: %d%n", u.getEventosConfirmados().size());
        pausar();
    }

    // ─────────────────── HELPERS ──────────────────────────
    private void titulo(String texto) {
        System.out.println("\n" + LINHA);
        System.out.println("  " + texto);
        System.out.println(LINHA);
    }

    private void sucesso(String msg) {
        System.out.println("  ✅ " + msg);
    }

    private void aviso(String msg) {
        System.out.println("  ⚠️  " + msg);
    }

    private void pausar() {
        System.out.print("\n  Pressione ENTER para continuar...");
        sc.nextLine();
    }

    private void limpar() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private int lerInteiro(String prompt) {
        System.out.print(prompt);
        try {
            String linha = sc.nextLine().trim();
            return Integer.parseInt(linha);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void sair() {
        System.out.println("\n  Até logo! 👋\n");
        System.exit(0);
    }

    private void exibirBanner() {
        System.out.println(LINHA);
        System.out.println("        SISTEMA DE EVENTOS DA CIDADE");
        System.out.println("        Barueri / São Paulo - SP");
        System.out.println(LINHA);
        System.out.printf("  Data/Hora: %s%n", LocalDateTime.now().format(FMT_EXIBIR));
        System.out.println(LINHA);
        System.out.println();
    }
}
