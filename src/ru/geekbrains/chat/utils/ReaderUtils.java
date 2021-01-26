package ru.geekbrains.chat.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReaderUtils {

    public static void writeToFile(File file, String value) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            bw.newLine();
            bw.write(value);
            bw.flush();
        } catch (Exception e) {
            throw new RuntimeException("SWW", e);
        }
    }

    public static String readFromFile(File file, int numberOfLines) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (Exception e) {
            throw new RuntimeException("SWW", e);
        }
        return lines.subList(Math.max(lines.size()-numberOfLines, 0), lines.size()).stream().collect(Collectors.joining("\n"));
    }
}
