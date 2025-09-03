package command.impl;

import command.Command;
import exception.EditorException;
import session.Session;
import session.SessionManager;

/**
 * Команда за отмяна на последната трансформация.
 */
public class UndoCommand implements Command {
    private final SessionManager sessionManager;

    /**
     * Конструктор за UndoCommand.
     * @param sessionManager мениджър на сесии
     */
    public UndoCommand(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * Изпълнява командата за отмяна.
     * @param args аргументи (не се използват)
     * @throws EditorException при липса на трансформации или грешка
     */
    @Override
    public void execute(String[] args) throws EditorException {
        if (args.length > 0) {
            throw new EditorException("Undo command does not accept any arguments");
        }

        Session session = sessionManager.getValidatedActiveSession();

        if (session.getTransformations().isEmpty()) {
            throw new EditorException("No transformations to undo");
        }

        String removedTransformation = session.getTransformations().remove(session.getTransformations().size() - 1);

        System.out.println("Undid last transformation: " + removedTransformation);
        System.out.println("Remaining transformations: " + session.getTransformations().size());
    }
}