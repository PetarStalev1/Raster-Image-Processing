package image.impl;

import image.Image;
import exception.EditorException;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * PBM изображение (Portable BitMap).
 */
public class PBMImage implements Image {
    private File file;
    private int width;
    private int height;
    private boolean[][] pixels;
    private final String format = "pbm";

    /**
     * Конструктор за PBM изображение.
     * @param file файлът на изображението
     */
    public PBMImage(File file) {
        this.file = file;
    }

    /**
     * Зарежда PBM изображение от файл.
     * @throws IOException при грешка при четене на файла
     * @throws EditorException при невалиден формат или данни
     */
    @Override
    public void load() throws IOException, EditorException {
        try (Scanner scanner = new Scanner(file)) {
            String magicNumber = scanner.nextLine().trim();
            while (magicNumber.startsWith("#")) magicNumber = scanner.nextLine().trim();
            if (!magicNumber.equals("P1")) throw new EditorException("Invalid PBM magic number");

            do {
                if (scanner.hasNextInt()) {
                    width = scanner.nextInt();
                    height = scanner.nextInt();
                } else {
                    scanner.nextLine();
                }
            } while (width <= 0 || height <= 0);

            pixels = new boolean[height][width];

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (!scanner.hasNextInt()) throw new EditorException("Unexpected end of PBM file");
                    int val = scanner.nextInt();
                    if (val != 0 && val != 1) throw new EditorException("Invalid pixel value: " + val);
                    pixels[i][j] = (val == 1);
                }
            }
        }
        System.out.println("Loaded PBM image: " + width + "x" + height);
    }

    /**
     * Записва PBM изображение във файл.
     * @param outputFile файлът за запис
     * @throws IOException при грешка при запис на файла
     */
    @Override
    public void save(File outputFile) throws IOException {
        try (PrintWriter writer = new PrintWriter(outputFile)) {
            writer.println("P1");
            writer.println(width + " " + height);
            for (boolean[] row : pixels) {
                for (boolean val : row) {
                    writer.print(val ? "1 " : "0 ");
                }
                writer.println();
            }
        }
        this.file = outputFile;
        System.out.println("Saved PBM image to: " + outputFile.getName());
    }

    /**
     * Създава копие на изображението.
     * @return копие на изображението
     */
    @Override
    public Image cloneImage() {
        PBMImage clone = new PBMImage(this.file);
        clone.width = this.width;
        clone.height = this.height;
        clone.pixels = new boolean[height][width];
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
        System.out.println("Grayscale transformation not applicable for PBM images");
    }

    /**
     * Прилага monochrome трансформация.
     */
    @Override
    public void applyMonochrome() {
        System.out.println("PBM images are already monochrome");
    }

    /**
     * Прилага negative трансформация.
     */
    @Override
    public void applyNegative() {
        forEachPixel(val -> !val);
        System.out.println("Applied negative transformation");
    }

    /**
     * Завърта изображението.
     * @param direction посоката на завъртане (left/right)
     * @throws EditorException при невалидна посока
     */
    @Override
    public void applyRotation(String direction) throws EditorException {
        boolean[][] newPixels = new boolean[width][height];
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

    private interface PixelFunction {
        boolean apply(boolean val);
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
    public boolean[][] getPixels() { return pixels; }

    /**
     * Задава пикселите на изображението.
     * @param pixels новите пиксели
     */
    public void setPixels(boolean[][] pixels) {
        this.pixels = pixels;
        this.height = pixels.length;
        this.width = pixels[0].length;
    }

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