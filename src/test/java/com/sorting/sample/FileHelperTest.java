package com.sorting.sample;

import com.sorting.sample.exception.ElementNotExists;
import com.sorting.sample.helpers.FileHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class FileHelperTest {

    public static final String inputFileName = "input_file.txt";
    private static final String GENERATED_FILE = "Generated_file.txt";

    @Before
    public void init() throws IOException {
        generate_file_with_fixed_size();
    }

    public void generate_file_with_fixed_size() throws IOException {

        long oneGbSize = 1000 * 1000 * 1000;
        FileHelper.generateFile(GENERATED_FILE, oneGbSize);
    }


    @Test
    public void should_correct_split_tmp_file() throws IOException {

        List<String> list = new Random().ints(20, 10, 100)
                .mapToObj(n -> Integer.toString(n))
                .collect(Collectors.toList());

        Comparator<String> comparator = (s, t1) -> {
            Integer a = Integer.parseInt(s);
            Integer b = Integer.parseInt(t1);
            return a.compareTo(b);
        };

        String path = FileHelper.sortAndSaveFile(list, comparator, 0);
        File file = new File(path);
        Assert.assertTrue(file.exists());


        FileHelper.sortAndSaveFile(list, comparator, 1);
        file = new File(path);
        Assert.assertTrue(file.exists());

        FileHelper.sortAndSaveFile(list, comparator, 2);

        file = new File(path);
        Assert.assertTrue(file.exists());

        FileHelper.cleanTemporaryFolder();
    }

    @Test
    public void should_get_from_file() throws IOException, ElementNotExists {
        List<String> list = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7).stream().map(x -> Integer.toString(x))
                .collect(Collectors.toList());

        Comparator<String> comparator = (s, t1) -> {
            Integer a = Integer.parseInt(s);
            Integer b = Integer.parseInt(t1);
            return a.compareTo(b);
        };

        String fileName = FileHelper.sortAndSaveFile(list, comparator, 0);

        FileHelper.moveToOneElement(fileName);
        int e1 = FileHelper.getFirstElementFromFile(fileName);
        Assert.assertEquals(list.get(1), Integer.toString(e1));

        FileHelper.moveToOneElement(fileName);
        int e2 = FileHelper.getFirstElementFromFile(fileName);
        Assert.assertEquals(list.get(2), Integer.toString(e2));

        FileHelper.moveToOneElement(fileName);
        int e3 = FileHelper.getFirstElementFromFile(fileName);
        Assert.assertEquals(list.get(3), Integer.toString(e3));
    }

    @Test(expected = ElementNotExists.class)
    public void should_return_file_size_exception_by_position() throws IOException, ElementNotExists {
        List<String> list = new ArrayList<>();

        Comparator<String> comparator = (s, t1) -> {
            Integer a = Integer.parseInt(s);
            Integer b = Integer.parseInt(t1);
            return a.compareTo(b);
        };

        String fileName = FileHelper.sortAndSaveFile(list, comparator, 0);

        int e1 = FileHelper.getFirstElementFromFile(fileName);

        Assert.assertEquals(e1, 0);
        FileHelper.cleanTemporaryFolder();
    }


    @Test
    public void should_correct_merge() throws IOException {
        int rowNumber = 21;
        FileHelper.generateTmpFile(inputFileName, rowNumber);

        int size_per_file = 6 * 2;

        Comparator<String> comparator = (s, t1) -> {
            Integer a = Integer.parseInt(s);
            Integer b = Integer.parseInt(t1);
            return a.compareTo(b);
        };


        // get current file size
        File generatedFile = new File(inputFileName);
        double bytes = 0;

        if (generatedFile.exists()) {
            bytes = generatedFile.length();
        }

        int divisionRemainder = (int) bytes % size_per_file;
        int fileCount = (int) bytes / size_per_file;

        if (divisionRemainder > 0) {
            fileCount += 1;
        }

        List<String> splittedFiles = FileHelper.splitToSubFiles(inputFileName, comparator, size_per_file);
        Assert.assertEquals(splittedFiles.size(), fileCount);
    }

    @Test
    public void should_correct_sort_tmp_file() throws IOException, ElementNotExists {
        List<String> list = Arrays.asList(9, 8, 7, 5, 5, 3, 2).stream().map(x -> Integer.toString(x))
                .collect(Collectors.toList());

        Comparator<String> comparator = (s, t1) -> {
            Integer a = Integer.parseInt(s);
            Integer b = Integer.parseInt(t1);
            return a.compareTo(b);
        };

        List<String> sortedList = list.stream()
                .sorted(comparator)
                .collect(Collectors.toList());


        String fileName = FileHelper.sortAndSaveFile(list, comparator, 0);
        Assert.assertTrue(new File(fileName).exists());

        int el1 = FileHelper.getFirstElementFromFile(fileName);
        int actualElement1 = Integer.parseInt(sortedList.get(0));
        Assert.assertEquals(el1, actualElement1);

        FileHelper.moveToOneElement(fileName);
        int el2 = FileHelper.getFirstElementFromFile(fileName);
        int actualElement2 = Integer.parseInt(sortedList.get(1));
        Assert.assertEquals(el2, actualElement2);


        FileHelper.moveToOneElement(fileName);
        int el3 = FileHelper.getFirstElementFromFile(fileName);
        int actualElement3 = Integer.parseInt(sortedList.get(2));
        Assert.assertEquals(el3, actualElement3);

        FileHelper.moveToOneElement(fileName);
        int el4 = FileHelper.getFirstElementFromFile(fileName);
        int actualElement4 = Integer.parseInt(sortedList.get(3));
        Assert.assertEquals(el4, actualElement4);
    }

    @After
    public void after() {
        FileHelper.cleanTemporaryFolder();

        File inputFile = new File(inputFileName);

        if(inputFile.exists())
            inputFile.delete();

        File file = new File(GENERATED_FILE);
        if(file.exists()){
            file.delete();
        }
    }
}
