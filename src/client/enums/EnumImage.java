package client.enums;

public enum EnumImage {

    LOCALIZAR("/img/search.png"),
    ICONE ("/img/computer.png"),
    PESQUISA ("/img/search.png"),
    FLASH ("/img/flash.png"),
    ABOUTIMAGE ("/img/AboutImage.png"),
    LOGIN("/img/uva.jpg"),

    ;

    private String caminho;

    private EnumImage(String caminho) {
        this.caminho = caminho;
    }

    public String getCaminho() {
        return caminho;
    }
}
