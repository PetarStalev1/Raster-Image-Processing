package ImageModels;

public class PPMImage extends Image{
    public PPMImage(String fileName) {
        super(fileName);
    }

    @Override
    public String getFileName() {
        return super.getFileName();
    }

    @Override
    public int applyGrayscale() {
        return 0;
    }

    @Override
    public int applyMonochrome() {
        return 0;
    }

    @Override
    public int applyNegative() {
        return 0;
    }

    @Override
    public int rotateLeft() {
        return 0;
    }

    @Override
    public int rotateRight() {
        return 0;
    }

    @Override
    public int save(String outputFilename) {
        return 0;
    }
}
