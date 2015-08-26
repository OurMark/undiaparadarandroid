package itba.undiaparadar.model;

public class Topic {
    private final int imageId;
    //TODO: Falta agregar la imagen en gris

    public Topic(final int imageId) {
        this.imageId = imageId;
    }

    public int getImageId() {
        return this.imageId;
    }
}
