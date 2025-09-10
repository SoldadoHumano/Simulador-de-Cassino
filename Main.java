import java.util.Random;
import java.util.Scanner;

/**
 * Classe para armazenar estatísticas de simulação de jogos de azar.
 *
 * <p>Esta classe mantém os resultados de uma simulação de jogo, incluindo:
 * <ul>
 *   <li>Saldo final do jogador</li>
 *   <li>Número de vitórias do jogador</li>
 *   <li>Número de vitórias do cassino</li>
 *   <li>Total de rodadas jogadas</li>
 * </ul>
 */
class Estatisticas {
    double saldoJogador;
    int vitoriasJogador;
    int vitoriasCassino;
    int rodadasJogadas;

    /**
     * Construtor para inicializar estatísticas.
     *
     * @param saldoJogador Saldo final do jogador
     * @param vitoriasJogador Número de vitórias do jogador
     * @param vitoriasCassino Número de vitórias do cassino
     * @param rodadasJogadas Total de rodadas jogadas
     */
    Estatisticas(double saldoJogador, int vitoriasJogador, int vitoriasCassino, int rodadasJogadas) {
        this.saldoJogador = saldoJogador;
        this.vitoriasJogador = vitoriasJogador;
        this.vitoriasCassino = vitoriasCassino;
        this.rodadasJogadas = rodadasJogadas;
    }
}

/**
 * Classe principal para simulação de jogos de cassino.
 *
 * <p>Este programa simula dois tipos de jogos de cassino:
 * <ul>
 *   <li>Caça-níqueis: Jogo com probabilidade personalizável de vitória</li>
 *   <li>Roleta: Disponíveis as versões Europeia (37 números) e Americana (38 números)</li>
 * </ul>
 *
 * <p>O programa permite simular múltiplos jogadores e oferece a opção de "Apostas Extras"
 * onde jogadores com saldo positivo podem continuar jogando até decidirem parar ou zerarem o saldo.
 */
public class Main {
    private static final Random RANDOM = new Random(); // Instância única de Random para melhor performance

    /**
     * Simula um jogo de caça-níqueis com apostas opcionais após vitórias.
     *
     * <p>O caça-níqueis possui uma probabilidade fixa de vitória configurável.
     * A cada rodada, o jogador aposta um valor fixo e tem uma chance percentual de ganhar.
     *
     * @param aposta Valor da aposta por rodada
     * @param nApostas Número inicial de apostas
     * @param chanceGanhar Chance de vitória do jogador em porcentagem (0-100)
     * @param apostasExtras Se devem ser realizadas apostas extras após ganhos
     * @return Objeto Estatisticas com resultados da simulação
     */
    public static Estatisticas simularCacaNiqueis(double aposta, int nApostas, double chanceGanhar, boolean apostasExtras) {
        double saldoJogador = 0.0;
        int vitoriasJogador = 0;
        int vitoriasCassino = 0;
        int rodadasJogadas = 0;

        // Fase inicial de apostas
        for (int i = 0; i < nApostas; i++) {
            rodadasJogadas++;
            if (RANDOM.nextDouble() * 100 < chanceGanhar) {
                saldoJogador += aposta;
                vitoriasJogador++;
            } else {
                saldoJogador -= aposta;
                vitoriasCassino++;
            }
        }

        // Fase de apostas extras (se ativada e se jogador está com saldo positivo)
        if (apostasExtras && saldoJogador > 0) {
            System.out.println("\nJogador com saldo positivo! Iniciando apostas extras...");
            while (saldoJogador > 0) {
                // 50% de chance do jogador parar a cada rodada
                if (RANDOM.nextDouble() < 0.5) {
                    System.out.println("Jogador parou por decisão.");
                    break;
                }
                rodadasJogadas++;
                if (RANDOM.nextDouble() * 100 < chanceGanhar) {
                    saldoJogador += aposta;
                    vitoriasJogador++;
                } else {
                    saldoJogador -= aposta;
                    vitoriasCassino++;
                }
            }
        }

        exibirResultados("Caça-Níqueis", saldoJogador, vitoriasJogador, vitoriasCassino, rodadasJogadas, aposta);
        return new Estatisticas(saldoJogador, vitoriasJogador, vitoriasCassino, rodadasJogadas);
    }

    /**
     * Simula um jogo de roleta com diferentes configurações.
     *
     * <p>Tipos de roleta disponíveis:
     * <ul>
     *   <li>Europeia: 37 números (0-36), vantagem da casa ≈ 2.7%</li>
     *   <li>Americana: 38 números (0, 00 e 1-36), vantagem da casa ≈ 5.26%</li>
     * </ul>
     *
     * <p>O jogador aposta consistentemente nos números 1-18 (aposta de baixa).
     *
     * @param aposta Valor da aposta por rodada
     * @param nApostas Número inicial de apostas
     * @param tipoRoleta Tipo de roleta ("europeia" ou "americana")
     * @param apostasExtras Se devem ser realizadas apostas extras após ganhos
     * @return Objeto Estatisticas com resultados da simulação
     */
    public static Estatisticas simularRoleta(double aposta, int nApostas, String tipoRoleta, boolean apostasExtras) {
        double saldoJogador = 0.0;
        int vitoriasJogador = 0;
        int vitoriasCassino = 0;
        int rodadasJogadas = 0;

        // Configuração baseada no tipo de roleta
        int nCasas;
        double chanceVitoria;

        if (tipoRoleta.equalsIgnoreCase("europeia")) {
            nCasas = 37; // 0-36
            chanceVitoria = 18.0 / 37 * 100; // Probabilidade real de vitória para apostas em 1-18
            System.out.println("Roleta Europeia selecionada (37 números, 0-36)");
            System.out.printf("Chance real de vitória: %.2f%%%n", chanceVitoria);
        } else if (tipoRoleta.equalsIgnoreCase("americana")) {
            nCasas = 38; // 0, 00, 1-36
            chanceVitoria = 18.0 / 38 * 100; // Probabilidade real de vitória para apostas em 1-18
            System.out.println("Roleta Americana selecionada (38 números, 0, 00, 1-36)");
            System.out.printf("Chance real de vitória: %.2f%%%n", chanceVitoria);
        } else {
            throw new IllegalArgumentException("Tipo de roleta inválido. Use 'europeia' ou 'americana'.");
        }

        // Fase inicial de apostas
        for (int i = 0; i < nApostas; i++) {
            rodadasJogadas++;
            int numeroSorteado = RANDOM.nextInt(nCasas);

            // Verifica se o jogador ganhou (números 1-18)
            boolean ganhou = (numeroSorteado >= 1 && numeroSorteado <= 18);

            if (ganhou) {
                saldoJogador += aposta;
                vitoriasJogador++;
            } else {
                saldoJogador -= aposta;
                vitoriasCassino++;
            }
        }

        // Fase de apostas extras (se ativada e se jogador está com saldo positivo)
        if (apostasExtras && saldoJogador > 0) {
            System.out.println("\nJogador com saldo positivo! Iniciando apostas extras...");
            while (saldoJogador > 0) {
                // 50% de chance do jogador parar a cada rodada
                if (RANDOM.nextDouble() < 0.5) {
                    System.out.println("Jogador parou por decisão.");
                    break;
                }
                rodadasJogadas++;
                int numeroSorteado = RANDOM.nextInt(nCasas);
                boolean ganhou = (numeroSorteado >= 1 && numeroSorteado <= 18);

                if (ganhou) {
                    saldoJogador += aposta;
                    vitoriasJogador++;
                } else {
                    saldoJogador -= aposta;
                    vitoriasCassino++;
                }
            }
        }

        exibirResultados("Roleta " + tipoRoleta, saldoJogador, vitoriasJogador, vitoriasCassino, rodadasJogadas, aposta);
        return new Estatisticas(saldoJogador, vitoriasJogador, vitoriasCassino, rodadasJogadas);
    }

    /**
     * Exibe os resultados detalhados de uma simulação de jogo.
     *
     * @param jogo Nome do jogo simulado
     * @param saldoJogador Saldo final do jogador
     * @param vitoriasJogador Vitórias do jogador
     * @param vitoriasCassino Vitórias do cassino
     * @param rodadasJogadas Total de rodadas
     * @param aposta Valor da aposta por rodada
     */
    private static void exibirResultados(String jogo, double saldoJogador, int vitoriasJogador,
                                         int vitoriasCassino, int rodadasJogadas, double aposta) {
        double perdaJogador = vitoriasCassino * aposta;
        double lucroCassino = -saldoJogador;

        System.out.println("\n=== Resultados Finais: " + jogo + " ===");
        System.out.printf("Rodadas Jogadas: %d%n", rodadasJogadas);
        System.out.println("--- Estatísticas do Jogador ---");
        System.out.printf("Vitórias do Jogador: %d%n", vitoriasJogador);
        System.out.printf("Dinheiro Perdido pelo Jogador: R$ %.2f%n", perdaJogador);
        System.out.println("--- Estatísticas do Cassino ---");
        System.out.printf("Vitórias do Cassino: %d%n", vitoriasCassino);
        System.out.printf("Lucro do Cassino: R$ %.2f%n", perdaJogador);
        System.out.println("------------------------------------");
        System.out.printf("Resultado Final do Jogador: R$ %.2f%n", saldoJogador);
        System.out.printf("Lucro Líquido do Cassino: R$ %.2f%n", lucroCassino);
    }

    /**
     * Método principal que inicia a simulação.
     *
     * <p>O método permite configurar:
     * <ul>
     *   <li>Número de jogadores a simular</li>
     *   <li>Tipo de jogo (Caça-Níqueis ou Roleta)</li>
     *   <li>Para roleta: tipo europeia ou americana</li>
     *   <li>Habilitar ou desabilitar apostas extras</li>
     * </ul>
     *
     * @param args Argumentos de linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Configurações padrão
        final double VALOR_APOSTA = 200.0;
        final int NUMERO_DE_APOSTAS_C = 10;
        final double CHANCE_DE_GANHAR_C = 48.5; // Chance ligeiramente menor que 50% para vantagem da casa

        System.out.println("==== SIMULADOR DE CASSINO ====");
        System.out.print("Número de jogadores: ");
        int numeroDeJogadores = scanner.nextInt();

        System.out.println("\nEscolha o jogo:");
        System.out.println("1. Caça-Níqueis");
        System.out.println("2. Roleta");
        System.out.print("Opção: ");
        int escolha = scanner.nextInt();

        String tipoRoleta = "europeia";
        if (escolha == 2) {
            System.out.println("\nEscolha o tipo de roleta:");
            System.out.println("1. Europeia (37 números, 0-36) - Vantagem da casa: 2.7%");
            System.out.println("2. Americana (38 números, 0, 00, 1-36) - Vantagem da casa: 5.26%");
            System.out.print("Opção: ");
            int tipo = scanner.nextInt();
            tipoRoleta = (tipo == 1) ? "europeia" : "americana";
        }

        System.out.print("\nAtivar apostas extras? (S/N): ");
        boolean apostasExtras = scanner.next().equalsIgnoreCase("S");

        // Acumuladores para estatísticas consolidadas
        double saldoTotal = 0.0;
        int totalVitoriasJogador = 0;
        int totalVitoriasCassino = 0;
        int totalRodadas = 0;

        // Executar simulações para cada jogador
        for (int i = 1; i <= numeroDeJogadores; i++) {
            System.out.printf("%n============================================%n");
            System.out.printf("      Simulação para o Jogador %d%n", i);
            System.out.printf("============================================%n");

            Estatisticas estatisticas;
            if (escolha == 1) {
                System.out.printf("Caça-Níqueis - Chance configurada: %.1f%%%n", CHANCE_DE_GANHAR_C);
                estatisticas = simularCacaNiqueis(VALOR_APOSTA, NUMERO_DE_APOSTAS_C, CHANCE_DE_GANHAR_C, apostasExtras);
            } else {
                estatisticas = simularRoleta(VALOR_APOSTA, NUMERO_DE_APOSTAS_C, tipoRoleta, apostasExtras);
            }

            // Consolida resultados
            saldoTotal += estatisticas.saldoJogador;
            totalVitoriasJogador += estatisticas.vitoriasJogador;
            totalVitoriasCassino += estatisticas.vitoriasCassino;
            totalRodadas += estatisticas.rodadasJogadas;
        }

        // Exibe consolidado final
        System.out.println("\n=========== ESTATÍSTICAS GERAIS ===========");
        System.out.printf("Total de Rodadas: %d%n", totalRodadas);
        System.out.printf("Total de Vitórias do Jogador: %d%n", totalVitoriasJogador);
        System.out.printf("Total de Vitórias do Cassino: %d%n", totalVitoriasCassino);
        System.out.printf("Resultado Consolidado dos Jogadores: R$ %.2f%n", saldoTotal);

        // Análise de performance
        double vantagemCassino = (double) totalVitoriasCassino / totalRodadas * 100;
        System.out.printf("Vantagem do Cassino: %.2f%%%n", vantagemCassino);

        if (saldoTotal > 0) {
            System.out.println("Os jogadores coletivamente tiveram lucro (raro!)");
        } else {
            System.out.printf("Prejuízo médio por jogador: R$ %.2f%n", Math.abs(saldoTotal) / numeroDeJogadores);
        }

        System.out.println("=========================================");

        scanner.close();
    }
}