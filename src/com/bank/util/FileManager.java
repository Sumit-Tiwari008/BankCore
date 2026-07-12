package com.bank.util;

import java.io.*;

/**
 * Handles reading and writing the {@link DataStore} to disk so that
 * registered users, accounts and transaction history persist between
 * runs of the application.
 */
public class FileManager {

    private static final String DATA_DIR = "data";
    private static final String DATA_FILE = DATA_DIR + File.separator + "bank_data.ser";

    public static DataStore load() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return new DataStore();
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (DataStore) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Warning: could not load existing data (" + e.getMessage() + "). Starting fresh.");
            return new DataStore();
        }
    }

    public static void save(DataStore dataStore) {
        try {
            File dir = new File(DATA_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
                out.writeObject(dataStore);
            }
        } catch (IOException e) {
            System.out.println("Warning: could not save data (" + e.getMessage() + ").");
        }
    }
}
