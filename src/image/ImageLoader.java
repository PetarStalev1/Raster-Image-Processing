package image;
import exception.EditorException;
import image.impl.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
/**
 * Клас за зареждане на изображения от файлове.
 * Поддържа формати PBM (P1), PGM (P2) и PPM (P3).
 * Проверява валидността на файла и извлича "magic number", за да определи типа на изображението.
 */
public class ImageLoader {
    /**
     * Зарежда изображение от подадения файл.
     * Поддържат се PBM, PGM и PPM формати.
     *
     * @param file файлът, от който да се зареди изображението
     * @return зареденото изображение като обект Image
     * @throws IOException ако възникне проблем при четене на файла
     * @throws EditorException ако файлът е невалиден или форматът не се поддържа
     */
    public Image loadImage(File file) throws IOException, EditorException {
        validateFile(file);

        String magicNumber = extractMagicNumber(file);

        switch (magicNumber) {
            case "P1":
                PBMImage pbmImage = new PBMImage(file);
                pbmImage.load();
                return pbmImage;

            case "P2":
                PGMImage pgmImage = new PGMImage(file);
                pgmImage.load();
                return pgmImage;

            case "P3":
                PPMImage ppmImage = new PPMImage(file);
                ppmImage.load();
                return ppmImage;

            default:
                throw new EditorException("Unsupported file format. Magic number: " + magicNumber +
                        ". Supported: P1 (PBM), P2 (PGM), P3 (PPM)");
        }
    }
    /**
     * Проверява дали подаденият файл е валиден.
     *
     * @param file файлът за проверка
     * @throws EditorException ако файлът е null, не съществува, не е файл или не може да се чете
     */
    private void validateFile(File file) throws EditorException {
        if (file == null) {
            throw new EditorException("File cannot be null");
        }

        if (!file.exists()) {
            throw new EditorException("File does not exist: " + file.getAbsolutePath());
        }

        if (!file.isFile()) {
            throw new EditorException("Path is not a file: " + file.getAbsolutePath());
        }

        if (!file.canRead()) {
            throw new EditorException("Cannot read file: " + file.getAbsolutePath());
        }
    }
    /**
     * Извлича "magic number" от файла, който определя формата на изображението.
     * Игнорира празни редове и коментари.
     *
     * @param file файлът, от който се извлича magic number
     * @return magic number като низ ("P1", "P2" или "P3")
     * @throws IOException ако възникне проблем при четене на файла
     * @throws EditorException ако не е намерен magic number
     */
    private String extractMagicNumber(File file) throws IOException, EditorException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                String[] tokens = line.split("\\s+");
                if (tokens.length > 0) {
                    return tokens[0];
                }
            }
        }

        throw new EditorException("No magic number found in file: " + file.getName());
    }
    /**
     * Проверява дали файлът е поддържан формат.
     *
     * @param file файлът за проверка
     * @return true, ако файлът е PBM, PGM или PPM; false в противен случай
     */
    public static boolean isSupportedFormat(File file) {
        try {
            ImageLoader loader = new ImageLoader();
            String magicNumber = loader.extractMagicNumber(file);
            return magicNumber.equals("P1") || magicNumber.equals("P2") || magicNumber.equals("P3");
        } catch (Exception e) {
            return false;
        }
    }
}