package image;

import image.transformation.Transformation;
import exception.EditorException;

import java.io.File;

/**
 * Интерфейс за фабрика за създаване на изображения и трансформации.
 * Позволява създаване на изображения от файл или празни изображения
 * с определен формат и размер, както и създаване на трансформации.
 */
public interface ImageFactory {

    /**
     * Създава изображение от даден файл.
     *
     * @param file файлът, от който да се зареди изображението
     * @return нов обект Image
     */
    Image createImage(File file);

    /**
     * Създава празно изображение с даден формат, ширина и височина.
     *
     * @param format формат на изображението (например "ppm", "pgm", "pbm")
     * @param width ширина на изображението в пиксели
     * @param height височина на изображението в пиксели
     * @return нов обект Image
     * @throws EditorException ако форматът е невалиден или параметрите са грешни
     */
    Image createEmptyImage(String format, int width, int height) throws EditorException;

    /**
     * Създава трансформация за превръщане на изображение в градации на сивото.
     *
     * @return обект Transformation за grayscale
     */
    Transformation createGrayscaleTransformation();

    /**
     * Създава трансформация за превръщане на изображение в монохром (черно и бяло).
     *
     * @return обект Transformation за monochrome
     */
    Transformation createMonochromeTransformation();

    /**
     * Създава трансформация за инвертиране на цветовете на изображението (negative).
     *
     * @return обект Transformation за negative
     */
    Transformation createNegativeTransformation();

    /**
     * Създава трансформация за завъртане на изображението в зададена посока.
     *
     * @param direction посоката на завъртане (например "clockwise" или "counterclockwise")
     * @return обект Transformation за rotation
     */
    Transformation createRotationTransformation(String direction);
}
