import java.io.*;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Tarefas {
    private static final String ARQUIVO_TAREFAS = "tarefas.txt";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {
        List<Tarefa> tarefas = carregarTarefas();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== GERENCIADOR DE TAREFAS ===");
            System.out.println("1. Adicionar Tarefa");
            System.out.println("2. Listar Tarefas");
            System.out.println("3. Marcar como Concluída");
            System.out.println("4. Remover Tarefa");
            System.out.println("5. Sair");
            System.out.print("Escolha uma opção: ");

            try {
                int opcao = Integer.parseInt(scanner.nextLine());

                switch (opcao) {
                    case 1:
                        adicionarTarefa(scanner, tarefas);
                        break;
                    case 2:
                        listarTarefas(tarefas);
                        break;
                    case 3:
                        marcarComoConcluida(scanner, tarefas);
                        break;
                    case 4:
                        removerTarefa(scanner, tarefas);
                        break;
                    case 5:
                        salvarTarefas(tarefas);
                        System.out.println("Saindo...");
                        return;
                    default:
                        System.out.println("Opção inválida!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, digite um número válido!");
            }
        }
    }

    private static void adicionarTarefa(Scanner scanner, List<Tarefa> tarefas) {
        System.out.print("\nDescrição da tarefa: ");
        String descricao = scanner.nextLine();

        System.out.print("Data de vencimento (dd/mm/aaaa): ");
        LocalDate dataVencimento = LocalDate.parse(scanner.nextLine(), formatter);

        tarefas.add(new Tarefa(descricao, dataVencimento));
        System.out.println("Tarefa adicionada com sucesso!");
    }

    private static void listarTarefas(List<Tarefa> tarefas) {
        if (tarefas.isEmpty()) {
            System.out.println("\nNenhuma tarefa cadastrada.");
            return;
        }

        System.out.println("\n=== LISTA DE TAREFAS ===");
        for (int i = 0; i < tarefas.size(); i++) {
            Tarefa t = tarefas.get(i);
            System.out.printf("%d. %s (Vencimento: %s) - %s%n",
                    i + 1,
                    t.getDescricao(),
                    t.getDataVencimento().format(formatter),
                    t.isConcluida() ? "[CONCLUÍDA]" : "[PENDENTE]");
        }
    }

    private static void marcarComoConcluida(Scanner scanner, List<Tarefa> tarefas) {
        listarTarefas(tarefas);
        if (tarefas.isEmpty()) return;

        System.out.print("\nDigite o número da tarefa concluída: ");
        int indice = Integer.parseInt(scanner.nextLine()) - 1;

        if (indice >= 0 && indice < tarefas.size()) {
            tarefas.get(indice).setConcluida(true);
            System.out.println("Tarefa marcada como concluída!");
        } else {
            System.out.println("Índice inválido!");
        }
    }

    private static void removerTarefa(Scanner scanner, List<Tarefa> tarefas) {
        listarTarefas(tarefas);
        if (tarefas.isEmpty()) return;

        System.out.print("\nDigite o número da tarefa a remover: ");
        int indice = Integer.parseInt(scanner.nextLine()) - 1;

        if (indice >= 0 && indice < tarefas.size()) {
            tarefas.remove(indice);
            System.out.println("Tarefa removida com sucesso!");
        } else {
            System.out.println("Índice inválido!");
        }
    }

    private static List<Tarefa> carregarTarefas() {
        List<Tarefa> tarefas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_TAREFAS))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                LocalDate data = LocalDate.parse(dados[1], formatter);
                boolean concluida = Boolean.parseBoolean(dados[2]);
                tarefas.add(new Tarefa(dados[0], data, concluida));
            }
        } catch (IOException e) {}
        return tarefas;
    }

    private static void salvarTarefas(List<Tarefa> tarefas) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQUIVO_TAREFAS))) {
            for (Tarefa t : tarefas) {
                pw.printf("%s;%s;%s%n",
                        t.getDescricao(),
                        t.getDataVencimento().format(formatter),
                        t.isConcluida());
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar tarefas: " + e.getMessage());
        }
    }

    static class Tarefa {
        private String descricao;
        private LocalDate dataVencimento;
        private boolean concluida;

      public Tarefa(String descricao, LocalDate dataVencimento) {
            this(descricao, dataVencimento, false);
       }

        public Tarefa(String descricao, LocalDate dataVencimento, boolean concluida) {
           this.descricao = descricao;
           this.dataVencimento = dataVencimento;
           this.concluida = concluida;
       }

        public String getDescricao() { return descricao; }
        public LocalDate getDataVencimento() { return dataVencimento; }
        public boolean isConcluida() { return concluida; }
        public void setConcluida(boolean concluida) { this.concluida = concluida; }
    }
}