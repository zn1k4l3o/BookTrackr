package production.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import production.model.User;
import production.model.Worker;
import tvz.hr.booktrackr.App;

import java.io.*;
import java.util.*;

public class FileUtils {

    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static final String USERS_FULL_TEXT_FILE_NAME = "dat/SQLcommands.txt";
    private static final String USERS_TEXT_FILE_NAME = "dat/users.txt";
    private static final String DATA_BIN_FILE_NAME = "dat/data.bin";
    private static final String DATA_CHANGES_BIN_FILE_NAME = "dat/dataChanges.bin";

    //*******************************************
    public static <T extends User> void writeSerializedList(List<T> lista, String location) {
        String filename = location;
        try
        {
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);

            out.writeObject(lista);
            out.close();
            file.close();
            System.out.println("Serijalizacija uspjela - T");

        }
        catch(IOException ex)
        {
            System.out.println("IOException is caught");
        }
    }

    public static void writeDataToFile(DataWrapper dataWrapper) {
        try
        {
            FileOutputStream file = new FileOutputStream(DATA_BIN_FILE_NAME);
            ObjectOutputStream out = new ObjectOutputStream(file);

            out.writeObject(dataWrapper);
            out.close();
            file.close();
            System.out.println("Serijalizacija uspjela DataWrapper");

        }
        catch(IOException ex)
        {
            System.out.println("IOException is caught");
        }
    }

    public static Optional<DataWrapper> readDataFromFile() {
        Optional<DataWrapper> dataWrapperOptional = Optional.empty();
        try
        {
            FileInputStream file = new FileInputStream(DATA_BIN_FILE_NAME);
            ObjectInputStream in = new ObjectInputStream(file);

            DataWrapper dataWrapper;
            dataWrapper = (DataWrapper) in.readObject();
            dataWrapperOptional = Optional.of(dataWrapper);
            in.close();
            file.close();
            System.out.println("DeSerijalizacija uspjela DataWrapper");

        }
        catch(IOException ex)
        {
            System.out.println("IOException is caught");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return dataWrapperOptional;
    }

    public static void writeDataChangeToFile(DataChangeWrapper dataChangeWrapper) {
        try
        {
            FileOutputStream file = new FileOutputStream(DATA_CHANGES_BIN_FILE_NAME);
            ObjectOutputStream out = new ObjectOutputStream(file);

            out.writeObject(dataChangeWrapper);
            out.close();
            file.close();
            System.out.println("Serijalizacija uspjela DataChangeWrapper");

        }
        catch(IOException ex)
        {
            System.out.println("IOException is caught");
        }
    }

    public static DataChangeWrapper readDataChangeFromFile() {
        DataChangeWrapper dataChangeWrapper = new DataChangeWrapper(new ArrayList<>());
        try
        {
            FileInputStream file = new FileInputStream(DATA_CHANGES_BIN_FILE_NAME);
            ObjectInputStream in = new ObjectInputStream(file);

            dataChangeWrapper = (DataChangeWrapper) in.readObject();
            in.close();
            file.close();
            System.out.println("DeSerijalizacija uspjela DataChangeWrapper");

        }
        catch(IOException ex)
        {
            System.out.println("IOException is caught");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return dataChangeWrapper;
    }

    public static void writeUserToFile(String username, String hashedPassword, Long id) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_TEXT_FILE_NAME, true))) {
            writer.write(username);
            writer.newLine();
            writer.write(hashedPassword);
            writer.newLine();
            writer.write(String.valueOf(id));
            writer.newLine();
            logger.info("User dodan u file.");

        } catch (IOException e) {
            System.err.println("Dodavanje usera nije uspjelo: " + e.getMessage());
        }
    }

    public static <T extends User> void writeFullUserToFile(T user) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FULL_TEXT_FILE_NAME, true))) {

            writer.write(user.getUsername());
            writer.newLine();
            writer.write(String.valueOf(user.getId()));
            writer.newLine();
            writer.write(user.getName());
            writer.newLine();
            writer.write(user.getLastName());
            writer.newLine();
            writer.write(user.getLibraryName());
            writer.newLine();
            if (user instanceof Worker) {
                writer.write(String.valueOf(((Worker)user).getWorker()));
            }
            else writer.write(String.valueOf(Boolean.FALSE));
            writer.newLine();
            writer.write(user.getHashedPassword());
            writer.newLine();
            logger.info("User dodan u file.");

        } catch (IOException e) {
            System.err.println("Dodavanje usera nije uspjelo: " + e.getMessage());
        }
    }

    public static <T extends User> List<T> getFullUsersFromFile() {
        List<T> userList = new ArrayList<>();
        File usersFile = new File(USERS_FULL_TEXT_FILE_NAME);

        try(BufferedReader reader = new BufferedReader(new FileReader(usersFile))){
            Optional<String> userOptional = Optional.empty();

            while((userOptional = Optional.ofNullable(reader.readLine())).isPresent()){

                Optional<T> newUserOptional = Optional.empty();

                String username = userOptional.get();
                Long id = Long.parseLong(reader.readLine());
                String name = reader.readLine();
                String lastName = reader.readLine();
                String library = reader.readLine();
                Boolean isWorker = Boolean.getBoolean(reader.readLine());
                String hashedPassword = reader.readLine();

                if (isWorker) {
                    newUserOptional = (Optional<T>) Optional.of(new Worker.Builder(username).withName(name).withLastName(lastName).withLibraryName(library).withHashedPassword(hashedPassword).withIsWorker(isWorker).withId(id).build());
                }
                else newUserOptional = (Optional<T>) Optional.of(new User.Builder(username).withName(name).withLastName(lastName).withLibraryName(library).withHashedPassword(hashedPassword).withId(id).build());
                newUserOptional.ifPresent(userList::add);
            }

        }
        catch (FileNotFoundException ex){
            String message="User file ne postoji";
            logger.error(message, ex);
        }
        catch(IOException ex){
            String message="File nije dobro napisan";
            logger.error(message, ex);
        }

        return userList;
    }

    public static <T extends User> List<T> getUsersFromFile() {
        List<T> userList = new ArrayList<>();
        File usersFile = new File(USERS_TEXT_FILE_NAME);

        try(BufferedReader reader = new BufferedReader(new FileReader(usersFile))){
            Optional<String> userOptional = Optional.empty();

            while((userOptional = Optional.ofNullable(reader.readLine())).isPresent()){

                Optional<T> newUserOptional = Optional.empty();

                String username = userOptional.get();
                String hashedPassword = reader.readLine();
                Long id = Long.valueOf(reader.readLine());

                newUserOptional = (Optional<T>) Optional.of(new Worker.Builder(username).withHashedPassword(hashedPassword).withId(id).build());
                newUserOptional.ifPresent(userList::add);
            }

        }
        catch (FileNotFoundException ex){
            String message="User file ne postoji";
            logger.error(message, ex);
        }
        catch(IOException ex){
            String message="File nije dobro napisan";
            logger.error(message, ex);
        }

        return userList;
    }

    //*****************OVO TREBA POVLAČIT IZ BAZE**************************
    public static Optional<User> getUserByUsernameFromFile(String username) {
        List<User> usersShort = getUsersFromFile();
        for (User user : usersShort) {
            if (user.getUsername().equals(username)) return Optional.of(user);
        }
        return Optional.empty();
    }
}
