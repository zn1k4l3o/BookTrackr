package production.enums;

public enum LibraryEnum {

    KUTINA("Knjižnica Kutina", "www.kutinaKnjiznica.hr"),

    SISAK("Knjižnica Sisak", "www.sisakKnjiznica.hr");

    public final String name;
    public final String webAddress;

    private LibraryEnum(String name, String webAddress) {
        this.name = name;
        this.webAddress = webAddress;
    }

    public String getName() {
        return name;
    }

    public String getWebAddress() {
        return webAddress;
    }
}
