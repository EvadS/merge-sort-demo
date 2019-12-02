package com.sorting.sample;

import com.sorting.sample.exception.ElementNotExists;
import com.sorting.sample.helpers.FileHelper;
import com.sorting.sample.helpers.SortHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;

public class SortHelpeTest {


    public static final String inputFileName = "SORTED_HELPER_TEST_FILE.txt";
    private static final String destinationFile = "DESTINATION_FILE";

    public Comparator<String> comparator;
    int sizeLimit = 100 * 1024;

    @Before
    public void init() throws IOException {
        comparator = (s, t1) -> {
            Integer a = Integer.parseInt(s);
            Integer b = Integer.parseInt(t1);
            return a.compareTo(b);
        };

        FileHelper.generateFile(inputFileName, sizeLimit);
    }


    @Test
    public void should_correct_sort()throws IOException, ElementNotExists {

        double inputFileSize = new File(inputFileName).length();

        SortHelper.sortFile(inputFileName, destinationFile, comparator, sizeLimit);

        double destinationFileSize = new File(destinationFile).length();
        Assert.assertEquals(inputFileSize, destinationFileSize,0.000001);
    }

    @After
    public void clean() {
        File inputFile = new File(inputFileName);

        if (inputFile.exists()) {
            inputFile.delete();
        }

        File outputFile = new File(destinationFile);

        if (outputFile.exists()) {
            outputFile.delete();
        }
    }
}
