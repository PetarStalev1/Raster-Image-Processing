package command.impl;

import command.Command;
import exception.EditorException;
import session.SessionManager;

/**
 * Команда за завъртане на изображенията в текущата сесия.
 */
public class RotateCommand implements Command {
    private final SessionManager sessionManager;

    /**
     * Конструктор за RotateCommand.
     * @param sessionManager мениджър на сесии
     */
    public RotateCommand(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * Изпълнява командата за завъртане на изображения.
     * Позволените аргументи са "left" и "right".
     * @param args аргументи на командата
     * @throws EditorException при липсващи или невалидни аргументи,
     *                         или ако няма активна сесия
     */
    @Override
    public void execute(String[] args) throws EditorException {
        if (args.length != 1) {
            throw new EditorException("Invalid arguments. Use 'rotate left' or 'rotate right'.");
        }

        String direction = args[0];
        if (!direction.equals("left") && !direction.equals("right")) {
            throw new EditorException("Invalid rotation direction. Use 'left' or 'right'.");
        }

        sessionManager.getValidatedActiveSession().addTransformation("rotate_" + direction);
        System.out.println("Queued " + direction + " rotation transformation");
    }
}
