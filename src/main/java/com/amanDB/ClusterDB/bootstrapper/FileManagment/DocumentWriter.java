package com.amanDB.ClusterDB.bootstrapper.FileManagment;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DocumentWriter extends Thread {
    private String path;
    private String jsonString;

    private static FileWriter fileWriter;
    private final Object lock = new Object();

    private final static DocumentWriter documentWriter = new DocumentWriter();
    public Object getLock(){
        return this.lock;
    }
    private DocumentWriter() {
    }

    public static DocumentWriter getDocumentWriter() {
        return documentWriter;
    }

    public void write(String path, String  jsonString) throws Exception {
        Path filePath = Paths.get(path);
        System.out.println(path);
        if (Files.exists(filePath)) {
            throw new FileAlreadyExistsException("file already exists.");
        } else {
            this.path = path;
            this.jsonString= jsonString;
            this.start();
        }
    }

    @Override
    public void start() {
        synchronized (lock) {
            File file = new File(path);
            try {
                fileWriter = new FileWriter(file);
                fileWriter.write(jsonString);
                System.out.println("Successfully Copied JSON Object to File...");
                System.out.println("\nJSON Object: " + jsonString);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    fileWriter.flush();
                    fileWriter.close();
                    lock.notifyAll();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


