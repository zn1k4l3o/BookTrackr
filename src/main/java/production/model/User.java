package production.model;

import java.io.Serializable;

public class User implements Serializable {

    String username;
    Long id;
    String name;
    String lastName;
    String libraryName;

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    String hashedPassword;

    public static class Builder {

        private String username;
        private Long id;
        private String name;
        private String lastName;
        private String libraryName;
        private String hashedPassword;


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

        public Builder withHashedPassword(String hashedPassword) {
            this.hashedPassword = hashedPassword;
            return this;
        }

        public User build() {
            User user = new User();
            user.username = username;
            user.id = id;
            user.name = name;
            user.lastName = lastName;
            user.libraryName = libraryName;
            user.hashedPassword = hashedPassword;

            return user;
        }
    }

    public User() {}

    public User(String username, Long id, String name, String lastName, String libraryName, String hashedPassword) {
        this.username = username;
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.libraryName = libraryName;
        this.hashedPassword = hashedPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void addNewToUsername() {
        username = "***-" + username + "-NEW";
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

    @Override
    public String toString() {
        return "User - " + username + " - ID " + id;
    }
}
