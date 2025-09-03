package command.impl;

import command.Command;
import session.SessionManager;
import exception.EditorException;
import image.Image;
import image.ImageLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Команда за зареждане на изображения и създаване на нова сесия.
 * Позволява зареждане на един или повече файлове.
 */
public class LoadCommand implements Command {
    private final SessionManager sessionManager;
    private final ImageLoader imageLoader;

    /**
     * Конструктор за LoadCommand.
     * @param sessionManager мениджър на сесии
     */
    public LoadCommand(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.imageLoader = new ImageLoader();
    }

    /**
     * Изпълнява командата за зареждане на изображения.
     * При успешно зареждане се създава нова сесия и изображенията
     * се добавят в нея.
     * @param args имена на файлове за зареждане
     * @throws EditorException при липсващи файлове, неподдържан формат
     *                         или ако не е заредено нито едно изображение
     */
    @Override
    public void execute(String[] args) throws EditorException {
        if (args.length == 0) {
            throw new EditorException("Load command requires at least one filename. " +
                    "Usage: load <filename1> [filename2 ...]");
        }

        List<Image> loadedImages = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();

        for (String filename : args) {
            try {
                File imageFile = findImageFile(filename);

                if (!ImageLoader.isSupportedFormat(imageFile)) {
                    errorMessages.add(filename + " (unsupported format)");
                    continue;
                }

                Image image = imageLoader.loadImage(imageFile);
                loadedImages.add(image);
                System.out.println("Image \"" + filename + "\" added");

            } catch (IOException | EditorException e) {
                errorMessages.add(filename + " (" + e.getMessage() + ")");
            }
        }

        if (loadedImages.isEmpty()) {
            throw new EditorException("Failed to load any images. Errors: " +
                    String.join(", ", errorMessages));
        }

        sessionManager.createSession();

        for (Image image : loadedImages) {
            sessionManager.addImageToActiveSession(image);
        }

        System.out.println("Session with ID: " + sessionManager.getActiveSessionId() + " started");

        if (!errorMessages.isEmpty()) {
            System.out.println("Warning: " + errorMessages.size() + " file(s) could not be loaded: " +
                    String.join(", ", errorMessages));
        }
    }

    /**
     * Намира файл по подадено име чрез проверка на различни директории и разширения.
     * @param filename име на файла
     * @return намерения файл
     * @throws EditorException ако файлът не бъде намерен
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

        File targetDir = new File("target_images/");
        if (targetDir.exists() && targetDir.isDirectory()) {
            File[] files = targetDir.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isFile() && f.getName().equalsIgnoreCase(filename)) {
                        return f;
                    }

                    String nameWithoutExt = removeExtension(f.getName());
                    if (nameWithoutExt.equalsIgnoreCase(filename)) {
                        return f;
                    }
                }
            }
        }

        throw new EditorException("File not found: " + filename +
                " (searched in target_images/ directory)");
    }

    /**
     * Премахва разширението от име на файл.
     * @param filename име на файла
     * @return името без разширение
     */
    private String removeExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex > 0) {
            return filename.substring(0, dotIndex);
        }
        return filename;
    }
}
