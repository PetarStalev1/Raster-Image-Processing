package image.transformation.impl;

import exception.EditorException;
import image.Image;
import image.transformation.Transformation;

/**
 * Трансформация за завъртане на изображение.
 *
 * Поддържа завъртане на ляво ("left") или дясно ("right").
 * Използва метода applyRotation() на изображението.
 */
public class RotateTransformation extends Transformation {

    private final String direction;

    /**
     * Създава нова трансформация за завъртане в зададена посока.
     *
     * @param direction посоката на завъртане ("left" или "right")
     * @throws EditorException ако посоката е невалидна
     */
    public RotateTransformation(String direction) throws EditorException {
        if (!direction.equals("left") && !direction.equals("right")) {
            throw new EditorException("Invalid rotation direction. Use 'left' or 'right'.");
        }
        this.direction = direction;
    }

    /**
     * Прилага трансформацията за завъртане към дадено изображение.
     *
     * @param image изображението, към което се прилага трансформацията
     * @throws EditorException ако възникне проблем при завъртане
     */
    @Override
    public void apply(Image image) throws EditorException {
        image.applyRotation(direction);
    }

    /**
     * Връща името на трансформацията, включително посоката.
     *
     * @return име на трансформацията, напр. "rotate_left" или "rotate_right"
     */
    @Override
    public String getName() {
        return "rotate_" + direction;
    }

    /**
     * Връща посоката на завъртане.
     *
     * @return "left" или "right"
     */
    public String getDirection() {
        return direction;
    }
}
