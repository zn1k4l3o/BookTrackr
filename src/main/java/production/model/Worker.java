package production.model;

public class Worker extends User {

    public Boolean getWorker() {
        return isWorker;
    }

    public void setWorker(Boolean worker) {
        isWorker = worker;
    }

    Boolean isWorker;

    private Worker() {
        super("", Long.valueOf(-1), "", "", "", "");
    }

    public Worker(String username, Long id, String name, String lastName, String libraryName, String hashedPassword, Boolean isWorker) {
        super(username, id, name, lastName, libraryName, hashedPassword);
        this.isWorker = isWorker;
    }

    public static class Builder {

        private String username;
        private Long id;
        private String name;
        private String lastName;
        private String libraryName;
        private String hashedPassword;
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

        public Builder withHashedPassword(String hashedPassword) {
            this.hashedPassword = hashedPassword;
            return this;
        }

        public Builder withIsWorker(Boolean isWorker) {
            this.isWorker =isWorker;
            return this;
        }

        public Worker build() {
            Worker worker = new Worker();
            worker.username = username;
            worker.id = id;
            worker.name = name;
            worker.lastName = lastName;
            worker.libraryName = libraryName;
            worker.hashedPassword = hashedPassword;
            worker.isWorker = isWorker;

            return worker;
        }
    }
}
