package command;

import exception.EditorException;

/**
 * Интерфейс за команди в графичния редактор.
 */
public interface Command {
    /**
     * Изпълнява командата с подадените аргументи.
     * @param args аргументи на командата
     * @throws EditorException при грешка по време на изпълнение
     */
    void execute(String[] args) throws EditorException;
}