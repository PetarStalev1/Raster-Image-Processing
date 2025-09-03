package command.impl;

import command.Command;
import exception.EditorException;
import image.Image;
import image.impl.*;
import session.Session;
import session.SessionManager;

import java.io.File;

public class CollageCommand implements Command {
    private final SessionManager sessionManager;
    /**
     * Създава CollageCommand с даден SessionManager.
     *
     * @param sessionManager мениджър за сесии, който се използва за валидиране на активната сесия
     */
    public CollageCommand(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }
    /**
     * Изпълнява командата за колаж с подадените аргументи.
     *
     * @param args аргументите на командата
     * @throws EditorException ако аргументите са невалидни, изображенията не са съвместими
     *                         или името на изходния файл е неправилно
     */
    @Override
    public void execute(String[] args) throws EditorException {
        if (args.length != 4) {
            throw new EditorException("Collage command requires 4 arguments. " +
                    "Usage: collage <horizontal|vertical> <image1> <image2> <outimage>");
        }

        String direction = args[0].toLowerCase();
        String image1Name = args[1];
        String image2Name = args[2];
        String outputImageName = args[3];

        Session session = sessionManager.getValidatedActiveSession();

        if (!direction.equals("horizontal") && !direction.equals("vertical")) {
            throw new EditorException("Direction must be 'horizontal' or 'vertical'");
        }

        Image image1 = findImageInSession(session, image1Name);
        Image image2 = findImageInSession(session, image2Name);

        validateImagesCompatibility(image1, image2);

        validateOutputFilename(image1, outputImageName);

        Image collage = createCollage(direction, image1, image2, outputImageName);

        session.addImage(collage);

        System.out.println("Created collage '" + outputImageName + "' (" + direction + ")");
    }
    /**
     * Намира изображение в текущата сесия по име.
     *
     * @param session текущата активна сесия
     * @param imageName името на изображението за търсене
     * @return намереното изображение
     * @throws EditorException ако изображението не е намерено в сесията
     */
    private Image findImageInSession(Session session, String imageName) throws EditorException {
        for (Image image : session.getImages()) {
            if (image.getFile().getName().equals(imageName)) {
                return image;
            }
        }
        throw new EditorException("Image not found in session: " + imageName);
    }
    /**
     * Проверява дали двете изображения са съвместими за колаж.
     * Изображенията трябва да са от един и същ формат и с еднакви размери.
     *
     * @param image1 първото изображение
     * @param image2 второто изображение
     * @throws EditorException ако изображенията не са съвместими
     */
    private void validateImagesCompatibility(Image image1, Image image2) throws EditorException {
        if (!image1.getFormat().equals(image2.getFormat())) {
            throw new EditorException("Cannot make a collage from different types! (." +
                    image1.getFormat() + " and ." + image2.getFormat() + ")");
        }

        if (image1.getWidth() != image2.getWidth() || image1.getHeight() != image2.getHeight()) {
            throw new EditorException("Images must have identical dimensions");
        }
    }
    /**
     * Проверява дали името на изходния файл е валидно спрямо формата на изображението.
     *
     * @param image изображението, чийто формат трябва да съвпада с изходния файл
     * @param filename името на изходния файл
     * @throws EditorException ако името на файла не съвпада с формата
     */
    private void validateOutputFilename(Image image, String filename) throws EditorException {
        String expectedExtension = "." + image.getFormat();
        if (!filename.toLowerCase().endsWith(expectedExtension)) {
            throw new EditorException("Output filename must end with " + expectedExtension);
        }
    }
    /**
     * Създава колаж от две изображения в дадена посока и с дадено име на изходния файл.
     *
     * @param direction посоката на колажа ("horizontal" или "vertical")
     * @param image1 първото изображение
     * @param image2 второто изображение
     * @param outputName името на изходния файл
     * @return създаденият колаж като изображение
     * @throws EditorException ако форматът на изображението не се поддържа или възникне вътрешна грешка
     */
    private Image createCollage(String direction, Image image1, Image image2, String outputName) throws EditorException {
        try {
            Image collage;

            switch (image1.getFormat()) {
                case "ppm":
                    collage = createPPMCollage(direction, (PPMImage) image1, (PPMImage) image2, outputName);
                    break;
                case "pgm":
                    collage = createPGMCollage(direction, (PGMImage) image1, (PGMImage) image2, outputName);
                    break;
                case "pbm":
                    collage = createPBMCollage(direction, (PBMImage) image1, (PBMImage) image2, outputName);
                    break;
                default:
                    throw new EditorException("Unsupported format for collage: " + image1.getFormat());
            }

            return collage;

        } catch (ClassCastException e) {
            throw new EditorException("Internal error: Image type mismatch");
        }
    }
    /**
     * Създава колаж от два PPM формата изображения.
     *
     * @param direction посоката на колажа ("horizontal" или "vertical")
     * @param image1 първото PPM изображение
     * @param image2 второто PPM изображение
     * @param outputName името на изходния файл
     * @return новото PPM изображение, представляващо колажа
     */
    private PPMImage createPPMCollage(String direction, PPMImage image1, PPMImage image2, String outputName) {
        int newWidth, newHeight;
        int[][][] newPixels;

        if (direction.equals("horizontal")) {
            newWidth = image1.getWidth() * 2;
            newHeight = image1.getHeight();
            newPixels = new int[newHeight][newWidth][3];

            for (int y = 0; y < newHeight; y++) {
                for (int x = 0; x < image1.getWidth(); x++) {
                    newPixels[y][x] = image1.getPixels()[y][x];
                }
            }

            for (int y = 0; y < newHeight; y++) {
                for (int x = 0; x < image2.getWidth(); x++) {
                    newPixels[y][x + image1.getWidth()] = image2.getPixels()[y][x];
                }
            }

        } else {
            newWidth = image1.getWidth();
            newHeight = image1.getHeight() * 2;
            newPixels = new int[newHeight][newWidth][3];

            for (int y = 0; y < image1.getHeight(); y++) {
                for (int x = 0; x < newWidth; x++) {
                    newPixels[y][x] = image1.getPixels()[y][x];
                }
            }

            for (int y = 0; y < image2.getHeight(); y++) {
                for (int x = 0; x < newWidth; x++) {
                    newPixels[y + image1.getHeight()][x] = image2.getPixels()[y][x];
                }
            }
        }

        PPMImage collage = new PPMImage(new File(outputName));
        collage.setPixels(newPixels);
        collage.setMaxColorValue(Math.max(image1.getMaxColorValue(), image2.getMaxColorValue()));
        return collage;
    }
    /**
     * Създава колаж от два PGM формата изображения.
     *
     * @param direction посоката на колажа ("horizontal" или "vertical")
     * @param image1 първото PGM изображение
     * @param image2 второто PGM изображение
     * @param outputName името на изходния файл
     * @return новото PGM изображение, представляващо колажа
     */
    private PGMImage createPGMCollage(String direction, PGMImage image1, PGMImage image2, String outputName) {
        int newWidth, newHeight;
        int[][] newPixels;

        if (direction.equals("horizontal")) {
            newWidth = image1.getWidth() * 2;
            newHeight = image1.getHeight();
            newPixels = new int[newHeight][newWidth];

            for (int y = 0; y < newHeight; y++) {
                for (int x = 0; x < image1.getWidth(); x++) {
                    newPixels[y][x] = image1.getPixels()[y][x];
                }
            }

            for (int y = 0; y < newHeight; y++) {
                for (int x = 0; x < image2.getWidth(); x++) {
                    newPixels[y][x + image1.getWidth()] = image2.getPixels()[y][x];
                }
            }

        } else {
            newWidth = image1.getWidth();
            newHeight = image1.getHeight() * 2;
            newPixels = new int[newHeight][newWidth];

            for (int y = 0; y < image1.getHeight(); y++) {
                for (int x = 0; x < newWidth; x++) {
                    newPixels[y][x] = image1.getPixels()[y][x];
                }
            }

            for (int y = 0; y < image2.getHeight(); y++) {
                for (int x = 0; x < newWidth; x++) {
                    newPixels[y + image1.getHeight()][x] = image2.getPixels()[y][x];
                }
            }
        }

        PGMImage collage = new PGMImage(new File(outputName));
        collage.setPixels(newPixels);
        collage.setMaxColorValue(Math.max(image1.getMaxColorValue(), image2.getMaxColorValue()));
        return collage;
    }
    /**
     * Създава колаж от два PBM формата изображения.
     *
     * @param direction посоката на колажа ("horizontal" или "vertical")
     * @param image1 първото PBM изображение
     * @param image2 второто PBM изображение
     * @param outputName името на изходния файл
     * @return новото PBM изображение, представляващо колажа
     */
    private PBMImage createPBMCollage(String direction, PBMImage image1, PBMImage image2, String outputName) {
        int newWidth, newHeight;
        boolean[][] newPixels;

        if (direction.equals("horizontal")) {
            newWidth = image1.getWidth() * 2;
            newHeight = image1.getHeight();
            newPixels = new boolean[newHeight][newWidth];

            for (int y = 0; y < newHeight; y++) {
                for (int x = 0; x < image1.getWidth(); x++) {
                    newPixels[y][x] = image1.getPixels()[y][x];
                }
            }

            for (int y = 0; y < newHeight; y++) {
                for (int x = 0; x < image2.getWidth(); x++) {
                    newPixels[y][x + image1.getWidth()] = image2.getPixels()[y][x];
                }
            }

        } else {
            newWidth = image1.getWidth();
            newHeight = image1.getHeight() * 2;
            newPixels = new boolean[newHeight][newWidth];

            for (int y = 0; y < image1.getHeight(); y++) {
                for (int x = 0; x < newWidth; x++) {
                    newPixels[y][x] = image1.getPixels()[y][x];
                }
            }

            for (int y = 0; y < image2.getHeight(); y++) {
                for (int x = 0; x < newWidth; x++) {
                    newPixels[y + image1.getHeight()][x] = image2.getPixels()[y][x];
                }
            }
        }

        PBMImage collage = new PBMImage(new File(outputName));
        collage.setPixels(newPixels);
        return collage;
    }
}