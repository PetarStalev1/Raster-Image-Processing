package image.impl;

import image.Image;
import exception.EditorException;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * PGM изображение (Portable GrayMap).
 */
public class PGMImage implements Image {
    private File file;
    private int width;
    private int height;
    private int[][] pixels;
    private int maxColorValue;
    private final String format = "pgm";

    /**
     * Конструктор за PGM изображение.
     * @param file файлът на изображението
     */
    public PGMImage(File file) {
        this.file = file;
    }

    /**
     * Зарежда PGM изображение от файл.
     * @throws IOException при грешка при четене на файла
     * @throws EditorException при невалиден формат или данни
     */
    @Override
    public void load() throws IOException, EditorException {
        try (Scanner scanner = new Scanner(file)) {
            String magicNumber = scanner.nextLine().trim();
            while (magicNumber.startsWith("#")) magicNumber = scanner.nextLine().trim();
            if (!magicNumber.equals("P2")) throw new EditorException("Invalid PGM magic number");

            do {
                if (scanner.hasNextInt()) {
                    width = scanner.nextInt();
                    height = scanner.nextInt();
                } else {
                    scanner.nextLine();
                }
            } while (width <= 0 || height <= 0);

            maxColorValue = scanner.nextInt();
            if (maxColorValue <= 0) throw new EditorException("Invalid max color value");

            pixels = new int[height][width];

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int val = scanner.nextInt();
                    validatePixelValue(val);
                    pixels[i][j] = val;
                }
            }
        }
        System.out.println("Loaded PGM image: " + width + "x" + height);
    }

    /**
     * Записва PGM изображение във файл.
     * @param outputFile файлът за запис
     * @throws IOException при грешка при запис на файла
     */
    @Override
    public void save(File outputFile) throws IOException {
        try (PrintWriter writer = new PrintWriter(outputFile)) {
            writer.println("P2");
            writer.println(width + " " + height);
            writer.println(maxColorValue);

            for (int[] row : pixels) {
                for (int val : row) {
                    writer.print(val + " ");
                }
                writer.println();
            }
        }
        this.file = outputFile;
        System.out.println("Saved PGM image to: " + outputFile.getName());
    }

    /**
     * Създава копие на изображението.
     * @return копие на изображението
     */
    @Override
    public Image cloneImage() {
        PGMImage clone = new PGMImage(this.file);
        clone.width = this.width;
        clone.height = this.height;
        clone.maxColorValue = this.maxColorValue;
        clone.pixels = new int[height][width];

        for (int i = 0; i < height; i++) {
            System.arraycopy(this.pixels[i], 0, clone.pixels[i], 0, width);
        }

        return clone;
    }

    /**
     * Прилага трансформация върху изображението.
     * @param transformation името на трансформацията
     * @throws EditorException при непозната трансформация
     */
    @Override
    public void applyTransformation(String transformation) throws EditorException {
        switch (transformation.toLowerCase()) {
            case "grayscale" -> System.out.println("PGM images are already grayscale");
            case "monochrome" -> applyMonochrome();
            case "negative" -> applyNegative();
            case "rotate_left" -> applyRotation("left");
            case "rotate_right" -> applyRotation("right");
            default -> throw new EditorException("Unknown transformation: " + transformation);
        }
    }

    /**
     * Прилага monochrome трансформация.
     */
    @Override
    public void applyMonochrome() {
        int threshold = maxColorValue / 2;
        forEachPixel(val -> val > threshold ? maxColorValue : 0);
        System.out.println("Applied monochrome transformation");
    }

    /**
     * Прилага negative трансформация.
     */
    @Override
    public void applyNegative() {
        forEachPixel(val -> maxColorValue - val);
        System.out.println("Applied negative transformation");
    }

    /**
     * Завърта изображението.
     * @param direction посоката на завъртане (left/right)
     * @throws EditorException при невалидна посока
     */
    @Override
    public void applyRotation(String direction) throws EditorException {
        int[][] newPixels = new int[width][height];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (direction.equalsIgnoreCase("left")) {
                    newPixels[width - 1 - j][i] = pixels[i][j];
                } else if (direction.equalsIgnoreCase("right")) {
                    newPixels[j][height - 1 - i] = pixels[i][j];
                } else {
                    throw new EditorException("Invalid rotation direction: " + direction + ". Use 'left' or 'right'");
                }
            }
        }

        pixels = newPixels;
        int temp = width;
        width = height;
        height = temp;

        System.out.println("Applied " + direction + " rotation");
    }

    /**
     * Валидира стойността на пиксела.
     * @param val стойността за валидиране
     * @throws EditorException при невалидна стойност
     */
    private void validatePixelValue(int val) throws EditorException {
        if (val < 0 || val > maxColorValue)
            throw new EditorException("Pixel value out of range: " + val);
    }

    private interface PixelFunction {
        int apply(int val);
    }

    private void forEachPixel(PixelFunction f) {
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                pixels[i][j] = f.apply(pixels[i][j]);
    }

    /**
     * Връща формата на изображението.
     * @return формата на изображението
     */
    @Override public String getFormat() { return format; }

    /**
     * Връща ширината на изображението.
     * @return ширината на изображението
     */
    @Override public int getWidth() { return width; }

    /**
     * Връща височината на изображението.
     * @return височината на изображението
     */
    @Override public int getHeight() { return height; }

    /**
     * Връща файла на изображението.
     * @return файлът на изображението
     */
    @Override public File getFile() { return file; }

    /**
     * Връща пикселите на изображението.
     * @return масив от пиксели
     */
    public int[][] getPixels() { return pixels; }

    /**
     * Задава пикселите на изображението.
     * @param pixels новите пиксели
     */
    public void setPixels(int[][] pixels) {
        this.pixels = pixels;
        this.height = pixels.length;
        this.width = pixels[0].length;
    }

    /**
     * Връща максималната стойност на цвета.
     * @return максималната стойност на цвета
     */
    public int getMaxColorValue() { return maxColorValue; }

    /**
     * Задава максималната стойност на цвета.
     * @param maxColorValue новата максимална стойност
     */
    public void setMaxColorValue(int maxColorValue) { this.maxColorValue = maxColorValue; }

    /**
     * Задава размерите на изображението.
     * @param width новата ширина
     * @param height новата височина
     */
    public void setDimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Прилага grayscale трансформация.
     */
    public void applyGrayscale() {
        System.out.println("PGM images are already grayscale");
    }
}