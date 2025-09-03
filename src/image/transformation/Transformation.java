package image.transformation;

import exception.EditorException;
import image.Image;

/**
 * Абстрактен базов клас за всички трансформации на изображения
 */
public abstract class Transformation {
    /**
     * Прилага трансформацията върху дадено изображение
     * @param image изображението за трансформация
     * @throws EditorException при грешка по време на трансформация
     */
    public abstract void apply(Image image) throws EditorException;

    /**
     * Връща името на трансформацията
     * @return име на трансформацията
     */
    public abstract String getName();
}