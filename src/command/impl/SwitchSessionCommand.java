package command.impl;

import command.Command;
import exception.EditorException;
import session.Session;
import session.SessionManager;

/**
 * Команда за превключване между сесии.
 */
public class SwitchSessionCommand implements Command {
    private final SessionManager sessionManager;

    /**
     * Конструктор за SwitchSessionCommand.
     * @param sessionManager мениджър на сесии
     */
    public SwitchSessionCommand(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * Изпълнява командата за превключване на сесия.
     * @param args аргументи (ID на сесията)
     * @throws EditorException при невалидно ID или несъществуваща сесия
     */
    @Override
    public void execute(String[] args) throws EditorException {
        if (args.length != 1) {
            throw new EditorException("Switch command requires exactly one argument: session ID. " +
                    "Usage: switch <session_id>");
        }

        try {
            int sessionId = Integer.parseInt(args[0]);

            if (!sessionManager.sessionExists(sessionId)) {
                throw new EditorException("Session with ID " + sessionId + " does not exist");
            }

            sessionManager.switchSession(sessionId);
            Session session = sessionManager.getActiveSession();

            System.out.println("Switched to session ID: " + sessionId);
            System.out.println("Images in session: " + session.getImageCount());

            if (!session.getTransformations().isEmpty()) {
                System.out.println("Pending transformations: " +
                        String.join(", ", session.getTransformations()));
            }

        } catch (NumberFormatException e) {
            throw new EditorException("Session ID must be a valid number");
        }
    }
}