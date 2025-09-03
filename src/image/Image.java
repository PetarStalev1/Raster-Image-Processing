package image;

import exception.EditorException;

import java.io.File;
import java.io.IOException;

/**
 * Интерфейс, представляващ изображение.
 * Поддържа операции за зареждане, запис, клониране и прилагане на трансформации.
 */
public interface Image {

    /**
     * Зарежда изображението от файла.
     *
     * @throws IOException ако възникне проблем при четене на файла
     * @throws EditorException ако изображението е невалидно или формата не се поддържа
     */
    void load() throws IOException, EditorException;

    /**
     * Записва изображението във файл.
     *
     * @param outputFile файлът, в който да се запише изображението
     * @throws IOException ако възникне проблем при запис
     */
    void save(File outputFile) throws IOException;

    /**
     * Създава копие на изображението.
     *
     * @return нов обект Image, копие на текущото изображение
     */
    Image cloneImage();

    /**
     * Прилага трансформация към изображението.
     *
     * @param transformation име на трансформацията
     * @throws EditorException ако трансформацията не е валидна
     */
    void applyTransformation(String transformation) throws EditorException;

    /**
     * Връща формата на изображението (например "ppm", "pgm", "pbm").
     *
     * @return формат на изображението като низ
     */
    String getFormat();

    /**
     * Връща ширината на изображението в пиксели.
     *
     * @return ширина на изображението
     */
    int getWidth();

    /**
     * Връща височината на изображението в пиксели.
     *
     * @return височина на изображението
     */
    int getHeight();

    /**
     * Връща файла, от който е заредено изображението.
     *
     * @return файлът на изображението
     */
    File getFile();

    /**
     * Преобразува изображението в черно-бяло (градации на сивото).
     */
    void applyGrayscale();

    /**
     * Преобразува изображението в монохром (черно и бяло).
     */
    void applyMonochrome();

    /**
     * Преобразува изображението в негатово (инвертира цветовете).
     */
    void applyNegative();

    /**
     * Завърта изображението в посока "clockwise" или "counterclockwise".
     *
     * @param direction посоката на завъртане
     * @throws EditorException ако посоката не е валидна
     */
    void applyRotation(String direction) throws EditorException;
}
