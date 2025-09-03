package command.impl;

import command.Command;
import session.Session;
import session.SessionManager;
import exception.EditorException;
import image.Image;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Команда за записване на всички изображения в текущата сесия.
 */
public class SaveCommand implements Command {
    private final SessionManager sessionManager;

    /**
     * Конструктор за SaveCommand.
     * @param sessionManager мениджър на сесии
     */
    public SaveCommand(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * Изпълнява командата за записване на изображенията.
     * @param args аргументи (не се използват)
     * @throws EditorException при липса на изображения или грешка при запис
     */
    @Override
    public void execute(String[] args) throws EditorException {
        if (args.length > 0) {
            throw new EditorException("Save command does not accept any arguments");
        }

        Session session = sessionManager.getValidatedActiveSession();

        if (session.getImages().isEmpty()) {
            throw new EditorException("No images to save in current session");
        }

        applyPendingTransformations(session);
        saveAllImages(session);
        session.getTransformations().clear();

        System.out.println("Saved all images successfully!");
    }

    /**
     * Прилага всички чакащи трансформации върху изображенията.
     * @param session сесията с изображенията
     * @throws EditorException при непозната трансформация
     */
    private void applyPendingTransformations(Session session) throws EditorException {
        List<String> transformations = session.getTransformations();

        if (transformations.isEmpty()) {
            return;
        }

        System.out.println("Applying pending transformations to all images...");

        for (Image image : session.getImages()) {
            for (String transformation : transformations) {
                applyTransformationToImage(image, transformation);
            }
        }
    }

    /**
     * Прилага конкретна трансформация върху изображение.
     * @param image изображението за трансформация
     * @param transformation името на трансформацията
     * @throws EditorException при непозната трансформация
     */
    private void applyTransformationToImage(Image image, String transformation) throws EditorException {
        switch (transformation) {
            case "grayscale":
                image.applyGrayscale();
                break;
            case "monochrome":
                image.applyMonochrome();
                break;
            case "negative":
                image.applyNegative();
                break;
            case "rotate_left":
                image.applyRotation("left");
                break;
            case "rotate_right":
                image.applyRotation("right");
                break;
            default:
                throw new EditorException("Unknown transformation: " + transformation);
        }
    }

    /**
     * Записва всички изображения от сесията.
     * @param session сесията с изображенията
     * @throws EditorException при грешка при запис на файл
     */
    private void saveAllImages(Session session) throws EditorException {
        File outputDir = new File("target_images/new images/");

        for (Image image : session.getImages()) {
            try {
                File outputFile = new File(outputDir, image.getFile().getName());
                image.save(outputFile);
            } catch (IOException e) {
                throw new EditorException("Failed to save image: " + image.getFile().getName());
            }
        }
    }
}