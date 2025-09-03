package session;

import java.util.ArrayList;
import java.util.List;


import image.Image;

/**
 * Клас, представляващ сесия за работа с изображения.
 * Всяка сесия има уникално ID и съдържа списък с изображения
 * и списък с приложени трансформации.
 */
public class Session {
    private final int id;
    private final List<Image> images;
    private final List<String> transformations;
    /**
     * Създава нова сесия с дадено ID и празни списъци за изображения и трансформации.
     *
     * @param id уникално идентификационно число на сесията
     */
    public Session(int id) {
        this.id = id;
        this.images = new ArrayList<>();
        this.transformations = new ArrayList<>();
    }
    /**
     * Създава нова сесия с дадено ID и начална колекция от изображения.
     *
     * @param id уникално идентификационно число на сесията
     * @param images списък с изображения, които да се добавят в сесията
     */
    public Session(int id, List<Image> images) {
        this.id = id;
        this.images = new ArrayList<>(images);
        this.transformations = new ArrayList<>();
    }
    /**
     * Връща уникалното ID на сесията.
     *
     * @return ID на сесията
     */
    public int getId() {
        return id;
    }
    /**
     * Добавя изображение към сесията.
     *
     * @param image изображението за добавяне
     */
    public void addImage(Image image) {
        images.add(image);
    }
    /**
     * Връща списък с всички изображения в сесията.
     *
     * @return списък с изображения
     */
    public List<Image> getImages() {
        return new ArrayList<>(images);
    }
    /**
     * Намира изображение по име на файл.
     *
     * @param filename името на файла на изображението
     * @return обект Image, ако е намерен; null в противен случай
     */
    public Image getImage(String filename) {
        for (Image image : images) {
            if (image.getFile().getName().equals(filename)) {
                return image;
            }
        }
        return null;
    }
    /**
     * Добавя трансформация към сесията.
     *
     * @param transformation името на трансформацията
     */
    public void addTransformation(String transformation) {
        transformations.add(transformation);
    }
    /**
     * Връща списък с всички приложени трансформации в сесията.
     *
     * @return списък с трансформации
     */
    public List<String> getTransformations() {
        return new ArrayList<>(transformations);
    }

    /**
     * Премахва последната приложена трансформация, ако има такава.
     */
    public void removeLastTransformation() {
        if (!transformations.isEmpty()) {
            transformations.remove(transformations.size() - 1);
        }
    }

    /**
     * Проверява дали сесията е празна (няма изображения).
     *
     * @return true ако няма изображения, false в противен случай
     */
    public boolean isEmpty() {
        return images.isEmpty();
    }
    /**
     * Връща броя на изображенията в сесията.
     *
     * @return брой изображения
     */
    public int getImageCount() {
        return images.size();
    }
    /**
     * Проверява дали в сесията има приложени трансформации.
     *
     * @return true ако има трансформации, false в противен случай
     */
    public boolean hasTransformations() {
        return !transformations.isEmpty();
    }
}