package command.impl;

import command.Command;
import exception.EditorException;
import session.SessionManager;

/**
 * Команда за затваряне на текущата активна сесия.
 *
 * Командата не приема аргументи.
 * Ако няма активна сесия, се хвърля EditorException.
 * След затваряне на сесията, се показва съобщение с ID на затворената сесия
 * и информация за останалите сесии, ако има такива.
 */
public class CloseCommand implements Command {
    private final SessionManager sessionManager;

    /**
     * Създава CloseCommand с даден SessionManager.
     *
     * @param sessionManager мениджър за сесии, който се използва за управление на активната сесия
     */
    public CloseCommand(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }
    /**
     * Изпълнява командата за затваряне на текущата активна сесия.
     *
     * @param args аргументите на командата (трябва да са празни)
     * @throws EditorException ако са подадени аргументи или няма активна сесия за затваряне
     */
    @Override
    public void execute(String[] args) throws EditorException {
        if (args.length > 0) {
            throw new EditorException("Close command does not accept any arguments");
        }

        if (sessionManager.getActiveSession() == null) {
            throw new EditorException("No active session to close");
        }

        int sessionId = sessionManager.getActiveSession().getId();

        sessionManager.closeCurrentSession();

        System.out.println("Session " + sessionId + " closed successfully");

        int remainingSessions = sessionManager.getSessions().size();
        if (remainingSessions > 0) {
            System.out.println("Remaining sessions: " + remainingSessions);
            System.out.print("Available sessions (ID): ");
            sessionManager.getSessions().keySet().forEach(id -> System.out.print(id + " "));
            System.out.println();
        } else {
            System.out.println("No active sessions remaining");
        }
    }


}
