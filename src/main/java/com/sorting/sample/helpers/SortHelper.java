package com.sorting.sample.helpers;

import com.sorting.sample.exception.ElementNotExists;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class SortHelper {


    public static void merge(List<String> subFilesList, String resultFileName) throws  IOException {

        boolean wasChanged = true;
        String tmoFilePath = "";

        File file = new File(resultFileName);

        if (file.delete())
            file.createNewFile();

        while (wasChanged) {

            int tmpValue = Integer.MAX_VALUE;
            wasChanged = false;

            for (String itemFilePath : subFilesList) {
                try {

                    if (!FileHelper.isFileHasElements(itemFilePath)) {
                        continue;
                    }

                    int item = FileHelper.getFirstElementFromFile(itemFilePath);

                    if (item < tmpValue) {
                        tmpValue = item;
                        tmoFilePath = itemFilePath;
                        wasChanged = true;
                    }
                } catch (IOException | ElementNotExists e) {
                    // we can just ignore file
                    e.printStackTrace();
                    continue;
                }
            }

            if (wasChanged) {
                writeToResultsFile(resultFileName, tmoFilePath);
            }
        }
    }

    public static void sortFile(String inputFileName, String distinationFile, Comparator comparator, int sizeLimit) throws IOException, ElementNotExists {

        FileHelper.createTmp();

        List<String> splicedFiles = FileHelper.splitToSubFiles(inputFileName,comparator,sizeLimit);
        SortHelper.merge(splicedFiles, distinationFile);
        FileHelper.cleanTemporaryFolder();
    }

    private static void writeToResultsFile(String resultFileName, String tmoFilePath) throws IOException {
        try {
            // file be able to  change
            int val = FileHelper.getFirstElementFromFile(tmoFilePath);
            FileHelper.moveToOneElement(tmoFilePath);
            FileHelper.writeResult(resultFileName, val);
        }catch (ElementNotExists ex){
            System.out.printf("Can't find value on file  %s", resultFileName );
        }
    }

}
