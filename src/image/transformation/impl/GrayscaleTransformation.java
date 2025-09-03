package image.transformation.impl;

import exception.EditorException;
import image.Image;
import image.transformation.Transformation;

/**
 * Трансформация за превръщане на изображение в градации на сивото (grayscale).
 *
 * Използва метода applyGrayscale() на изображението.
 */
public class GrayscaleTransformation extends Transformation {

    /**
     * Прилага трансформацията към дадено изображение.
     *
     * @param image изображението, към което се прилага трансформацията
     * @throws EditorException ако възникне проблем при прилагане на трансформацията
     */
    @Override
    public void apply(Image image) throws EditorException {
        image.applyGrayscale();
    }

    /**
     * Връща името на трансформацията.
     *
     * @return "grayscale"
     */
    @Override
    public String getName() {
        return "grayscale";
    }
}
