package command;

import session.SessionManager;
import command.impl.*;
import exception.EditorException;

import java.util.HashMap;
import java.util.Map;

/**
 * Фабрика за създаване на команди.
 */
public class CommandFactory {
    private final SessionManager sessionManager;
    private final Map<String, Command> commands;

    /**
     * Конструктор за CommandFactory.
     * @param sessionManager мениджър на сесии
     */
    public CommandFactory(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.commands = new HashMap<>();
        initializeCommands();
    }

    /**
     * Метод, който регистрира поддържаните команди в колекцията {@code commands}.
     */
    private void initializeCommands() {
        commands.put("load", new LoadCommand(sessionManager));
        commands.put("grayscale", new GrayscaleCommand(sessionManager));
        commands.put("monochrome", new MonochromeCommand(sessionManager));
        commands.put("negative", new NegativeCommand(sessionManager));
        commands.put("rotate", new RotateCommand(sessionManager));
        commands.put("close", new CloseCommand(sessionManager));
        commands.put("save", new SaveCommand(sessionManager));
        commands.put("saveas", new SaveAsCommand(sessionManager));
        commands.put("switch", new SwitchSessionCommand(sessionManager));
        commands.put("sessioninfo", new SessionInfoCommand(sessionManager));
        commands.put("add", new AddCommand(sessionManager));
        commands.put("undo", new UndoCommand(sessionManager));
        commands.put("collage", new CollageCommand(sessionManager));
    }

    /**
     * Създава команда по име.
     * @param commandName името на командата
     * @return командата
     * @throws EditorException при непозната команда
     */
    public Command createCommand(String commandName) throws EditorException {
        Command command = commands.get(commandName.toLowerCase());
        if (command == null) {
            throw new EditorException("Unknown command: " + commandName +
                    ". Type 'help' for available commands.");
        }
        return command;
    }
}