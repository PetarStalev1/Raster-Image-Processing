package image.transformation.impl;

import exception.EditorException;
import image.Image;
import image.transformation.Transformation;

/**
 * Трансформация за превръщане на изображение в монохром (черно и бяло).
 *
 * Използва метода applyMonochrome() на изображението.
 */
public class MonochromeTransformation extends Transformation {

    /**
     * Прилага трансформацията към дадено изображение.
     *
     * @param image изображението, към което се прилага трансформацията
     * @throws EditorException ако възникне проблем при прилагане на трансформацията
     */
    @Override
    public void apply(Image image) throws EditorException {
        image.applyMonochrome();
    }

    /**
     * Връща името на трансформацията.
     *
     * @return "monochrome"
     */
    @Override
    public String getName() {
        return "monochrome";
    }
}
