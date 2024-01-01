package production.model;

public class User {

    private String username;
    private Long id;
    private String name;
    private String lastName;
    private String libraryName;
    private Boolean isWorker;

    public static class Builder {

        private String username;
        private Long id;
        private String name;
        private String lastName;
        private String libraryName;
        private Boolean isWorker;

        public Builder(String username) {
            this.username= username;
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder withLibraryName(String libraryName) {
            this.libraryName = libraryName;
            return this;
        }
        public Builder withIsWorker(Boolean isWorker) {
            this.isWorker = isWorker;
            return this;
        }

        public User build() {
            User user = new User();
            user.username = username;
            user.id = id;
            user.name = name;
            user.lastName = lastName;
            user.libraryName = libraryName;
            user.isWorker = isWorker;

            return user;
        }
    }

    private User() {}

    public User(String username, Long id, String name, String lastName, String libraryName, Boolean isWorker) {
        this.username = username;
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.libraryName = libraryName;
        this.isWorker = isWorker;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }

    public Boolean getWorker() {
        return isWorker;
    }

    public void setWorker(Boolean worker) {
        isWorker = worker;
    }

}
