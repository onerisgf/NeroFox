package sample.model;

public class Favoritos {

    private String link;

    public String getLink() {
        return link;
    }

    public Favoritos(int i, String link){
        this.link = link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return getLink();
    }

}
