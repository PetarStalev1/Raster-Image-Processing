package ImageModels;

public abstract class Image {
    protected String fileName;

    public Image(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public abstract int applyGrayscale();
    public abstract int applyMonochrome();
    public abstract int applyNegative();
    public abstract int rotateLeft();
    public abstract int rotateRight();
    public abstract int save(String outputFilename);
}
