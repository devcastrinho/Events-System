Visão Geral
Sistema desenvolvido em Java utilizando o paradigma orientado a objetos e arquitetura MVC (Model-View-Controller). Permite o cadastro de usuários e eventos locais, confirmação de presença, e exibição de status dos eventos em tempo real.

Arquitetura – MVC
src/
├── Main.java                          ← Ponto de entrada
├── enums/
│   └── Categoria.java                 ← Enum com categorias de eventos
├── model/
│   ├── Usuario.java                   ← Modelo do usuário
│   └── Evento.java                    ← Modelo do evento
├── controller/
│   ├── UsuarioController.java         ← Lógica de negócio de usuários
│   └── EventoController.java          ← Lógica de negócio de eventos
├── view/
│   └── ConsoleView.java               ← Interface com o usuário (console)
└── util/
    └── ArquivoUtil.java               ← Persistência em arquivo
Funcionalidades
Funcionalidade	Implementada
Cadastro de usuário (5 atributos)	✅
Login por email	✅
Cadastro de eventos com todos os atributos obrigatórios	✅
Categorias delimitadas (enum)	✅
Listar eventos ordenados por horário	✅
Confirmar presença em evento	✅
Ver meus eventos confirmados	✅
Cancelar presença	✅
Detectar eventos em andamento	✅
Detectar eventos encerrados	✅
Persistência em events.data	✅
Carregar eventos ao iniciar	✅
Atributos dos Modelos
Usuário
Nome completo
Email (identificador único)
Telefone
Cidade
Bairro
Evento
ID (gerado automaticamente)
Nome
Endereço
Categoria (enum: Festa, Show, Esporte, Cultura, Gastronomia, Tecnologia, Educação, Religioso, Outros)
Horário (LocalDateTime)
Descrição
Email do criador
Como Executar
Pré-requisitos
Java 17 ou superior instalado
Compilar e rodar
chmod +x run.sh
./run.sh
Ou manualmente
mkdir -p out
javac -d out -sourcepath src $(find src -name "*.java")
java -cp out Main
Persistência de Dados
Os dados são salvos em dois arquivos no diretório de execução:

events.data – Armazena todos os eventos cadastrados
users.data – Armazena os usuários e suas presenças confirmadas
O formato é texto simples com separadores |, carregado automaticamente ao iniciar o programa.

Lógica de Status Temporal
Status	Condição
🟢 PRÓXIMO	Horário do evento ainda não chegou
🔴 EM ANDAMENTO	Horário passou há menos de 3 horas
⬛ ENCERRADO	Horário passou há mais de 3 horas
