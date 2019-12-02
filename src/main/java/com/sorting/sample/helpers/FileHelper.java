package com.sorting.sample.helpers;

import com.sorting.sample.exception.ElementNotExists;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class FileHelper {

    private static final String TEMPORARY_FOLDER = "temporary";
    private static final String TMP_PREFIX = "tmp_";
    private static final int DELIMITER_SIZE = "\n".getBytes(StandardCharsets.UTF_8).length;


    public static boolean cleanTemporaryFolder() {
        File tmpDirectory = new File(TEMPORARY_FOLDER);
        return cleanDirectory(tmpDirectory);
    }


    private static boolean cleanDirectory(File directory) {
        File[] allContents = directory.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                cleanDirectory(file);
            }
        }
        return directory.delete();
    }

    static void createTmp() {
        File dir = new File(TEMPORARY_FOLDER);
        dir.mkdir();
    }


    public static List<String> splitToSubFiles(String fileName, Comparator<String> comparator, int mergeSize) throws IOException {
        List<String> subFilesList = new ArrayList<>();

        int fileCount = 0;
        int currentBlockSize = 0;

        List<String> readCharacters = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {

            String prevLine = bufferedReader.readLine();

            readCharacters.add(prevLine);
            currentBlockSize = getLentUtf8(prevLine);

            if (currentBlockSize > mergeSize) {
                return subFilesList;
            }

            String nextLine = bufferedReader.readLine();
            while (nextLine != null) {

                if ((currentBlockSize + getLentUtf8(nextLine)) > mergeSize) {
                    String filePath = sortAndSaveFile(readCharacters, comparator, fileCount);
                    subFilesList.add(filePath);
                    readCharacters.clear();
                    currentBlockSize = 0;

                    fileCount++;
                }

                prevLine = nextLine;
                readCharacters.add(prevLine);
                currentBlockSize += getLentUtf8(prevLine);

                nextLine = bufferedReader.readLine();
            }

            String filePath = sortAndSaveFile(readCharacters, comparator, fileCount);
            subFilesList.add(filePath);
        }

        return subFilesList;
    }

    private static int getLentUtf8(String str) {
        final byte[] utf8Bytes = str.getBytes(StandardCharsets.UTF_8);
        return utf8Bytes.length + DELIMITER_SIZE;
    }


    static String sortAndSaveFile(List<String> tmplist, Comparator<String> comparator, int number) throws IOException {
        Collections.sort(tmplist, comparator);

        createTmp();

        File file = new File(String.format("%s/%s%s", TEMPORARY_FOLDER, TMP_PREFIX, number));
        file.delete();
        file.createNewFile();

        try (FileWriter fileWriter = new FileWriter(file);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            for (String item : tmplist) {
                bufferedWriter.write(item);
                bufferedWriter.newLine();
            }
        }

        return file.getPath();
    }

    static int getFirstElementFromFile(String fileName) throws IOException, ElementNotExists {

        File file = new File(fileName);

        if (!file.exists()) {
            throw new IOException("File not found");
        }

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(fileName, "r")) {
            String line = randomAccessFile.readLine();
            if (line == null || line.length() == 0) {
                throw new ElementNotExists();
            }

            return Integer.parseInt(line);
        }
    }

    public static boolean isFileHasElements(String fileName) {
        File file = new File(fileName);
        return file.length() > 0;
    }

    static void moveToOneElement(String fileName) throws IOException {

        List<String> results = new ArrayList<>();
        File file = new File(fileName);

        if (!file.exists()) {
            throw new IOException("File not found");
        }

        if (file.length() < 1) {
            return;
        }

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(fileName, "r")) {
            boolean isFirst = true;

            String line;
            while ((line = randomAccessFile.readLine()) != null) {
                if (isFirst) {
                    isFirst = false;
                    continue;
                }

                results.add(line);
            }

            Files.write(file.toPath(), results, Charset.defaultCharset());
        }
    }

    public static void writeResult(final String resultFileName, final int content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(resultFileName, true) )) {
            writer.write(String.format("%s\n", Integer.toString(content)));
        }
    }

    public  static void generateFile(String fileName,  long fileSize ) throws IOException {

        int i = 1;
        Random random = new Random();
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        long wroteBytes = 0;
        while (wroteBytes < fileSize) {
            int randomInt = random.nextInt();
            String s = String.format("%d\n", randomInt);
            writer.write(s);

            if(i%100 == 0) {
                System.out.print(".");
              i=0;
            }
            int strLen = s.getBytes(StandardCharsets.UTF_8).length;
            wroteBytes += strLen;
            i++;
        }
        writer.close();
    }

    public static void generateTmpFile(String fileName, int rowNumber){

        int item = 0;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            long currentRow = 0;

            boolean first = true;
            while (currentRow < rowNumber) {
                StringBuilder builder = new StringBuilder();

                if (!first) {
                    builder.append("\n");
                }

                builder.append(String.format("%d", item));
                writer.write(builder.toString());
                currentRow++;

                first = false;
                item++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
