package command.impl;

import command.Command;
import exception.EditorException;
import session.SessionManager;

/**
 * Команда за прилагане на трансформация "grayscale" (нюанси на сивото)
 * върху всички изображения в активната сесия.
 * <p>
 * Командата не приема аргументи.
 * </p>
 */
public class GrayscaleCommand implements Command {
    private final SessionManager sessionManager;

    public GrayscaleCommand(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void execute(String[] args) throws EditorException {
        if (args.length > 0) {
            throw new EditorException("Grayscale командата не приема аргументи");
        }

        sessionManager.getValidatedActiveSession().addTransformation("grayscale");
        System.out.println("Queued grayscale transformation for all images");
    }
}
