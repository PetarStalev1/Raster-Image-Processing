package exception;

/**
 * Персонализирано изключение за грешки в графичния редактор.
 * Използва се за всички грешки, специфични за редактора.
 */
public class EditorException extends Exception {

    /**
     * Създава изключение със съобщение за грешка.
     */
    public EditorException(String message) {
        super(message);
    }

    /**
     * Създава изключение със съобщение и причина.
     */
    public EditorException(String message, Throwable cause) {
        super(message, cause);
    }
}
