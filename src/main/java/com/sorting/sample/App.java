package com.sorting.sample;

import com.sorting.sample.exception.ElementNotExists;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.Scanner;

import com.sorting.sample.helpers.SortHelper;
import org.apache.log4j.Logger;

public class App 
{

    static final Logger logger = Logger.getLogger(App.class);

    static boolean isFileExists(String filePathString) {
        boolean result = false;

        File f = new File(filePathString);
        if (f.exists() && !f.isDirectory()) {
            result = true;
        }
        return result;
    }

    public static void main( String[] args ) throws IOException, ElementNotExists {

        String inputFilePath ;
        String outputFile ;
        int sizeLimit= 100;

        Scanner scanner = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Enter file path  for sort :  ");

        inputFilePath = scanner.nextLine();  // Read user input

        if (!isFileExists(inputFilePath)) {
            System.out.printf("File %s doesn't exist. Program will be closed.", inputFilePath);
            logger.error("File " + inputFilePath + " does not exist.");
            System.exit(1);
        }

        System.out.println("Enter file path for sorted file  :  ");
        outputFile = scanner.nextLine();

        System.out.println("Enter memory size :  ");
        sizeLimit = Integer.parseInt(scanner.nextLine());

        Comparator<String> comparator = (s, t1) -> {
            Integer a = Integer.parseInt(s);
            Integer b = Integer.parseInt(t1);
            return a.compareTo(b);
        };

        SortHelper.sortFile(inputFilePath, outputFile,comparator,sizeLimit);

        System.out.println("file was sorted ");

        System.out.println("Press any key to continue...");
        scanner.nextLine();

    }
}
