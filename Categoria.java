package enums;

public enum Categoria {
    FESTA("Festa"),
    SHOW("Show / Concerto"),
    ESPORTE("Evento Esportivo"),
    CULTURA("Cultura / Arte"),
    GASTRONOMIA("Gastronomia / Festival de Comida"),
    TECNOLOGIA("Tecnologia / Inovação"),
    EDUCACAO("Educação / Palestra"),
    RELIGIOSO("Evento Religioso"),
    OUTROS("Outros");

    private final String descricao;

    Categoria(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static Categoria fromIndice(int indice) {
        Categoria[] valores = Categoria.values();
        if (indice < 1 || indice > valores.length) {
            throw new IllegalArgumentException("Índice inválido: " + indice);
        }
        return valores[indice - 1];
    }

    public static void listar() {
        Categoria[] valores = Categoria.values();
        for (int i = 0; i < valores.length; i++) {
            System.out.printf("  %d. %s%n", i + 1, valores[i].getDescricao());
        }
    }
}
