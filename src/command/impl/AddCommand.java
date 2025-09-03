package command.impl;

import command.Command;
import exception.EditorException;
import image.Image;
import image.ImageLoader;
import session.Session;
import session.SessionManager;

import java.io.File;
import java.io.IOException;

public class AddCommand implements Command {
    private final SessionManager sessionManager;
    private final ImageLoader imageLoader;
    /**
     * Създава AddCommand с даден SessionManager.
     *
     * @param sessionManager мениджър за сесии, който се използва за валидиране на активната сесия
     */
    public AddCommand(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.imageLoader = new ImageLoader();
    }
    /**
     * Изпълнява командата за добавяне на изображение към текущата сесия.
     *
     * @param args аргументите на командата (трябва да съдържат точно името на файла)
     * @throws EditorException ако аргументите са невалидни, изображението вече съществува
     *                         или файлът не може да бъде зареден/не е открит
     */
    @Override
    public void execute(String[] args) throws EditorException {
        if (args.length != 1) {
            throw new EditorException("Add command requires exactly one argument: filename. " +
                    "Usage: add <filename>");
        }

        String filename = args[0];
        Session session = sessionManager.getValidatedActiveSession();

        if (isImageAlreadyInSession(session, filename)) {
            throw new EditorException("Image '" + filename + "' already exists in current session");
        }

        try {
            File imageFile = findImageFile(filename);

            if (!ImageLoader.isSupportedFormat(imageFile)) {
                throw new EditorException("Unsupported file format: " + filename);
            }

            Image image = imageLoader.loadImage(imageFile);

            session.addImage(image);

            System.out.println("Image \"" + filename + "\" added to current session");

        } catch (IOException | EditorException e) {
            throw new EditorException("Failed to add image '" + filename + "': " + e.getMessage());
        }
    }

    /**
     * Проверява дали изображението вече съществува в текущата сесия.
     *
     * @param session текущата активна сесия
     * @param filename името на изображението
     * @return true, ако изображението вече съществува в сесията, false в противен случай
     */
    private boolean isImageAlreadyInSession(Session session, String filename) {
        for (Image image : session.getImages()) {
            if (image.getFile().getName().equals(filename)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Търси файла на изображението в директорията "target_images" или като директен път.
     * Проверява също и добавени разширения (.ppm, .pgm, .pbm).
     *
     * @param filename името на файла за търсене
     * @return намереният File обект
     * @throws EditorException ако файлът не е намерен
     */
    private File findImageFile(String filename) throws EditorException {
        File file = new File(filename);
        if (file.exists()) {
            return file;
        }

        File targetDirFile = new File("target_images/" + filename);
        if (targetDirFile.exists()) {
            return targetDirFile;
        }

        String[] extensions = {".ppm", ".pgm", ".pbm", ""};
        for (String ext : extensions) {
            File testFile = new File("target_images/" + filename + ext);
            if (testFile.exists() && testFile.isFile()) {
                return testFile;
            }
        }

        throw new EditorException("File not found: " + filename);
    }
}
