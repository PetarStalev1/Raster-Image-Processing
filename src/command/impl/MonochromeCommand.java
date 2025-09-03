package command.impl;

import command.Command;
import session.SessionManager;
import exception.EditorException;

/**
 * Команда за прилагане на монохромен филтър върху изображенията в текущата сесия.
 */
public class MonochromeCommand implements Command {
    private final SessionManager sessionManager;

    /**
     * Конструктор за MonochromeCommand.
     * @param sessionManager мениджър на сесии
     */
    public MonochromeCommand(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * Изпълнява командата за добавяне на монохромна трансформация.
     * Не приема аргументи.
     * @param args аргументи на командата
     * @throws EditorException ако са подадени аргументи
     *                         или ако няма активна сесия
     */
    @Override
    public void execute(String[] args) throws EditorException {
        if (args.length > 0) {
            throw new EditorException("Monochrome command does not accept any arguments");
        }

        sessionManager.getValidatedActiveSession().addTransformation("monochrome");
        System.out.println("Queued monochrome transformation for all images");
    }
}
