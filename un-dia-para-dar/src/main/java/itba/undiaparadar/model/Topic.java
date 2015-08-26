package itba.undiaparadar.model;

public class Topic {
    private final int imageId;
    private final String name;
    //TODO: Falta agregar la imagen en gris

    public Topic(final int imageId, final String name) {
        this.imageId = imageId;
        this.name = name;
    }

    public int getImageId() {
        return this.imageId;
    }

    public String getName() {
        return this.name;
    }
}
