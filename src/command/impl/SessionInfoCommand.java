package command.impl;

import command.Command;
import exception.EditorException;
import image.Image;
import session.Session;
import session.SessionManager;

import java.util.List;

/**
 * Команда за показване на информация за текущата сесия.
 */
public class SessionInfoCommand implements Command {
    private final SessionManager sessionManager;

    /**
     * Конструктор за SessionInfoCommand.
     * @param sessionManager мениджър на сесии
     */
    public SessionInfoCommand(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * Изпълнява командата за показване на информация за сесията.
     * @param args аргументи (не се използват)
     * @throws EditorException при грешка при достъп до сесията
     */
    @Override
    public void execute(String[] args) throws EditorException {
        if (args.length > 0) {
            throw new EditorException("Sessioninfo command does not accept any arguments");
        }

        Session session = sessionManager.getValidatedActiveSession();
        displaySessionInfo(session);
    }

    /**
     * Показва информация за сесията.
     * @param session сесията за показване
     */
    private void displaySessionInfo(Session session) {
        System.out.println("=== Session Information ===");
        System.out.println("Session ID: " + session.getId());

        List<Image> images = session.getImages();
        System.out.println("Images in session (" + images.size() + "):");
        for (int i = 0; i < images.size(); i++) {
            Image image = images.get(i);
            System.out.printf("  %d. %s (%s, %dx%d)%n",
                    i + 1,
                    image.getFile().getName(),
                    image.getFormat().toUpperCase(),
                    image.getWidth(),
                    image.getHeight());
        }

        List<String> transformations = session.getTransformations();
        if (transformations.isEmpty()) {
            System.out.println("Pending transformations: None");
        } else {
            System.out.println("Pending transformations (" + transformations.size() + "): " +
                    String.join(", ", transformations));
        }

        System.out.println("===========================");
    }
}