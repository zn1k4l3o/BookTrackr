package production.model;

public class Library {

    Long id;
    String name;
    String hashedPassword;
    String webAddress;

    public static class Builder {

        Long id;
        String name;
        String hashedPassword;
        String webAddress;

        public Builder(Long id) {
            this.id = id;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withHashedPassword(String hashedPassword) {
            this.hashedPassword = hashedPassword;
            return this;
        }

        public Builder withWebAddress(String webAddress) {
            this.webAddress = webAddress;
            return this;
        }

        public Library build() {
            Library library = new Library();
            library.id = id;
            library.name = name;
            library.hashedPassword = hashedPassword;
            library.webAddress = webAddress;
            return library;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getWebAddress() {
        return webAddress;
    }

    public void setWebAddress(String webAddress) {
        this.webAddress = webAddress;
    }

    public Library() {}

    public Library(Long id, String name, String hashedPassword, String webAddress) {
        this.id = id;
        this.name = name;
        this.hashedPassword = hashedPassword;
        this.webAddress = webAddress;
    }

}
