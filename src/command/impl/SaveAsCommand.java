package command.impl;

import command.Command;
import exception.EditorException;
import image.Image;
import session.Session;
import session.SessionManager;

import java.io.File;
import java.io.IOException;

/**
 * Команда за записване на първото изображение от сесията под ново име.
 */
public class SaveAsCommand implements Command {
    private final SessionManager sessionManager;

    /**
     * Конструктор за SaveAsCommand.
     * @param sessionManager мениджър на сесии
     */
    public SaveAsCommand(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * Изпълнява командата за записване на изображение под ново име.
     * @param args аргументи (име на новия файл)
     * @throws EditorException при липса на изображения или грешка при запис
     */
    @Override
    public void execute(String[] args) throws EditorException {
        if (args.length != 1) {
            throw new EditorException("Saveas command requires exactly one argument: output filename. " +
                    "Usage: saveas <filename>");
        }

        Session session = sessionManager.getValidatedActiveSession();

        if (session.getImages().isEmpty()) {
            throw new EditorException("No images in session to save");
        }

        Image original = session.getImages().get(0);
        String outputFilename = args[0];

        validateOutputFilename(original, outputFilename);
        Image clone = original.cloneImage();
        applyTransformations(session, clone);
        saveImageToFile(clone, outputFilename);

        System.out.println("Successfully saved as " + outputFilename);
    }

    /**
     * Валидира името на изходния файл.
     * @param image изображението за запис
     * @param filename име на файла
     * @throws EditorException при несъответствие на формата
     */
    private void validateOutputFilename(Image image, String filename) throws EditorException {
        String expectedExtension = "." + image.getFormat();
        if (!filename.toLowerCase().endsWith(expectedExtension)) {
            throw new EditorException("Output filename must end with " + expectedExtension +
                    " to match image format");
        }
    }

    /**
     * Прилага всички трансформации върху изображението.
     * @param session сесията с трансформациите
     * @param image изображението за трансформация
     * @throws EditorException при непозната трансформация
     */
    private void applyTransformations(Session session, Image image) throws EditorException {
        for (String transformation : session.getTransformations()) {
            applySingleTransformation(image, transformation);
        }
    }

    /**
     * Прилага конкретна трансформация върху изображение.
     * @param image изображението за трансформация
     * @param transformation името на трансформацията
     * @throws EditorException при непозната трансформация
     */
    private void applySingleTransformation(Image image, String transformation) throws EditorException {
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
     * Записва изображението във файл.
     * @param image изображението за запис
     * @param filename име на файла
     * @throws EditorException при грешка при запис
     */
    private void saveImageToFile(Image image, String filename) throws EditorException {
        File outputFile = new File("target_images/new images/" + filename);
        try {
            image.save(outputFile);
        } catch (IOException e) {
            throw new EditorException("Failed to save image to: " + filename);
        }
    }
}