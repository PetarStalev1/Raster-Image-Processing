package image.transformation.impl;

import exception.EditorException;
import image.Image;
import image.transformation.Transformation;

/**
 * Трансформация за създаване на негатив на изображение.
 *
 * Използва метода applyNegative() на изображението за инвертиране на цветовете.
 */
public class NegativeTransformation extends Transformation {

    /**
     * Прилага негативната трансформация към дадено изображение.
     *
     * @param image изображението, към което се прилага трансформацията
     * @throws EditorException ако възникне проблем при прилагане на трансформацията
     */
    @Override
    public void apply(Image image) throws EditorException {
        image.applyNegative();
    }

    /**
     * Връща името на трансформацията.
     *
     * @return "negative"
     */
    @Override
    public String getName() {
        return "negative";
    }
}
