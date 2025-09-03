package cli;

import command.Command;
import command.CommandFactory;
import session.SessionManager;
import exception.EditorException;

import java.util.Scanner;

/**
 * CommandLineInterface класът представлява основния потребителски интерфейс
 * на растерния графичен редактор. Той обработва потребителските команди
 * и ги изпълнява чрез съответните Command обекти.
 *
 * <p>Интерфейсът поддържа интерактивна комуникация с потребителя чрез конзолата,
 * предоставяйки възможност за работа с множествени сесии, трансформации на изображения
 * и управление на файлове.</p>
 *
 * @version 1.0
 * @see Command
 * @see CommandFactory
 * @see SessionManager
 * @see EditorException
 */
public class CommandLineInterface {
    private final SessionManager sessionManager;
    private final CommandFactory commandFactory;
    private boolean isRunning;

    /**
     * Конструктор по подразбиране, който инициализира CLI компонентите.
     * Създава нов SessionManager и CommandFactory.
     */
    public CommandLineInterface() {
        this.sessionManager = new SessionManager();
        this.commandFactory = new CommandFactory(sessionManager);
        this.isRunning = true;
    }

    /**
     * Стартира основния цикъл на приложението.
     * Показва начално съобщение и влиза в интерактивен режим за въвеждане на команди.
     *
     * <p>Методът обработва следните ситуации:
     * <ul>
     *   <li>Празен вход - пренебрегва се</li>
     *   <li>Валидни команди - изпълняват се чрез CommandFactory</li>
     *   <li>Грешки - показват се подходящи съобщения</li>
     * </ul>
     * </p>
     *
     * <p>Цикълът продължава, докато isRunning е true.</p>
     */
    public void start() {
        System.out.println("Welcome to Raster Image Editor!");
        System.out.println("Type 'help' for available commands.");

        try (Scanner scanner = new Scanner(System.in)) {
            while (isRunning) {
                try {
                    System.out.print("> ");
                    String input = scanner.nextLine().trim();

                    if (input.isEmpty()) continue;

                    processInput(input);
                } catch (EditorException e) {
                    System.out.println("Error: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Unexpected error: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Обработва потребителския вход, като го разделя на команда и аргументи.
     *
     * @param input пълният низ, въведен от потребителя
     * @throws EditorException ако възникне грешка при обработка на командата
     */
    private void processInput(String input) throws EditorException {
        String[] parts = input.split("\\s+");

        String commandName = parts[0].toLowerCase();
        String[] args = parts.length > 1 ?
                java.util.Arrays.copyOfRange(parts, 1, parts.length) : new String[0];

        if ("exit".equals(commandName)) {
            isRunning = false;
            System.out.println("Goodbye!");
            return;
        }

        if ("help".equals(commandName))
        {
            showHelp();
            return;
        }

        Command command = commandFactory.createCommand(commandName);
        command.execute(args);

        System.out.flush();
    }

    /**
     * Показва помощна информация за всички налични команди.
     * Форматира изхода за лесно четене и разбиране.
     */
    private void showHelp() {
        System.out.println("Available commands:");
        System.out.println("  load <file> [file2 ...]    - Start session with images");
        System.out.println("  add <file>                 - Add image to current session");
        System.out.println("  save                       - Save all images");
        System.out.println("  saveas <file>              - Save first image with new name");
        System.out.println("  grayscale                  - Apply grayscale transformation");
        System.out.println("  monochrome                 - Apply monochrome transformation");
        System.out.println("  negative                   - Apply negative transformation");
        System.out.println("  rotate <left|right>        - Rotate images 90°");
        System.out.println("  undo                       - Undo last transformation");
        System.out.println("  sessioninfo                - Show session details");
        System.out.println("  switch <session_id>        - Switch to different session");
        System.out.println("  collage <direction> <image1> <image2> <outimage> - Create collage");
        System.out.println("  close                      - Close current session");
        System.out.println("  help                       - Show this help");
        System.out.println("  exit                       - Exit the program");
    }
}