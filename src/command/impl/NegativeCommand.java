package command.impl;

import command.Command;
import exception.EditorException;
import session.SessionManager;

/**
 * Команда за прилагане на негатив върху изображенията в текущата сесия.
 */
public class NegativeCommand implements Command {
    private final SessionManager sessionManager;

    /**
     * Конструктор за NegativeCommand.
     * @param sessionManager мениджър на сесии
     */
    public NegativeCommand(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * Изпълнява командата за добавяне на негативна трансформация.
     * Не приема аргументи.
     * @param args аргументи на командата
     * @throws EditorException ако са подадени аргументи
     *                         или няма активна сесия
     */
    @Override
    public void execute(String[] args) throws EditorException {
        if (args.length > 0) {
            throw new EditorException("Negative command does not accept any arguments");
        }

        sessionManager.getValidatedActiveSession().addTransformation("negative");
        System.out.println("Queued negative transformation for all images");
    }
}
