package image.impl;

import exception.EditorException;
import image.Image;

import java.io.*;
import java.util.Scanner;

/**
 * PPM изображение (Portable PixMap).
 */
public class PPMImage implements Image {
    private File file;
    private int width;
    private int height;
    private int[][][] pixels;
    private int maxColorValue;
    private final String format = "ppm";

    /**
     * Конструктор за PPM изображение.
     * @param file файлът на изображението
     */
    public PPMImage(File file) {
        this.file = file;
    }

    /**
     * Зарежда PPM изображение от файл.
     * @throws IOException при грешка при четене на файла
     * @throws EditorException при невалиден формат или данни
     */
    @Override
    public void load() throws IOException, EditorException {
        try (Scanner scanner = new Scanner(file)) {
            String magicNumber = scanner.nextLine().trim();
            while (magicNumber.startsWith("#")) magicNumber = scanner.nextLine().trim();
            if (!magicNumber.equals("P3")) throw new EditorException("Invalid PPM magic number");

            do {
                if (scanner.hasNextInt()) {
                    width = scanner.nextInt();
                    height = scanner.nextInt();
                } else {
                    scanner.nextLine();
                }
            } while (width <= 0 || height <= 0);

            maxColorValue = scanner.nextInt();
            if (maxColorValue <= 0 || maxColorValue > 65535)
                throw new EditorException("Invalid max color value");

            pixels = new int[height][width][3];

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    for (int c = 0; c < 3; c++) {
                        int val = scanner.nextInt();
                        validatePixelValue(val);
                        pixels[i][j][c] = val;
                    }
                }
            }
        }
        System.out.println("Loaded PPM: " + width + "x" + height);
    }

    /**
     * Записва PPM изображение във файл.
     * @param outputFile файлът за запис
     * @throws IOException при грешка при запис на файла
     */
    @Override
    public void save(File outputFile) throws IOException {
        try (PrintWriter writer = new PrintWriter(outputFile)) {
            writer.println("P3");
            writer.println(width + " " + height);
            writer.println(maxColorValue);

            for (int[][] row : pixels) {
                for (int[] pixel : row) {
                    writer.println(pixel[0] + " " + pixel[1] + " " + pixel[2]);
                }
            }
        }
        this.file = outputFile;
        System.out.println("Saved PPM to: " + outputFile.getName());
    }

    /**
     * Създава копие на изображението.
     * @return копие на изображението
     */
    @Override
    public Image cloneImage() {
        PPMImage clone = new PPMImage(this.file);
        clone.width = this.width;
        clone.height = this.height;
        clone.maxColorValue = this.maxColorValue;
        clone.pixels = new int[height][width][3];

        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                clone.pixels[i][j] = pixels[i][j].clone();

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
            case "grayscale" -> applyGrayscale();
            case "monochrome" -> applyMonochrome();
            case "negative" -> applyNegative();
            case "rotate_left" -> applyRotation("left");
            case "rotate_right" -> applyRotation("right");
            default -> throw new EditorException("Unknown transformation: " + transformation);
        }
    }

    /**
     * Прилага grayscale трансформация.
     */
    @Override
    public void applyGrayscale() {
        forEachPixel((r, g, b) -> {
            int gray = (int) (0.3 * r + 0.59 * g + 0.11 * b);
            return new int[]{gray, gray, gray};
        });
        System.out.println("Applied grayscale");
    }

    /**
     * Прилага monochrome трансформация.
     */
    @Override
    public void applyMonochrome() {
        forEachPixel((r, g, b) -> {
            int avg = (r + g + b) / 3;
            int val = (avg > maxColorValue / 2) ? maxColorValue : 0;
            return new int[]{val, val, val};
        });
        System.out.println("Applied monochrome");
    }

    /**
     * Прилага negative трансформация.
     */
    @Override
    public void applyNegative() {
        forEachPixel((r, g, b) -> new int[]{
                maxColorValue - r,
                maxColorValue - g,
                maxColorValue - b
        });
        System.out.println("Applied negative");
    }

    /**
     * Завърта изображението.
     * @param direction посоката на завъртане (left/right)
     * @throws EditorException при невалидна посока
     */
    @Override
    public void applyRotation(String direction) throws EditorException {
        int[][][] newPixels;
        if (direction.equals("left")) {
            newPixels = new int[width][height][3];
            for (int i = 0; i < height; i++)
                for (int j = 0; j < width; j++)
                    newPixels[width - 1 - j][i] = pixels[i][j];
        } else if (direction.equals("right")) {
            newPixels = new int[width][height][3];
            for (int i = 0; i < height; i++)
                for (int j = 0; j < width; j++)
                    newPixels[j][height - 1 - i] = pixels[i][j];
        } else throw new EditorException("Invalid rotation: " + direction);

        pixels = newPixels;
        int tmp = width;
        width = height;
        height = tmp;
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
        int[] apply(int r, int g, int b);
    }

    private void forEachPixel(PixelFunction f) {
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                pixels[i][j] = f.apply(pixels[i][j][0], pixels[i][j][1], pixels[i][j][2]);
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
     * @return триизмерен масив от пиксели
     */
    public int[][][] getPixels() { return pixels; }

    /**
     * Задава пикселите на изображението.
     * @param pixels новите пиксели
     */
    public void setPixels(int[][][] pixels) {
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
}