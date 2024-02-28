package ru.job4j.io.search;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;

public class Search {
    public static void main(String[] args) throws IOException {
        paramValidation(args);
        Path start = Paths.get(args[0]);
        search(start, path -> path.toFile().getName().endsWith(args[1])).forEach(System.out::println);
    }

    public static void paramValidation(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("No all arguments.");
        }
        Path start = Paths.get(args[0]);
        if (!Files.isDirectory(start) || !Files.exists(start)) {
            throw new IllegalArgumentException("Folder does not exist.");
        }
        if (!args[1].startsWith(".") && args[1].length() > 1) {
            throw new IllegalArgumentException("Parametr is not.");
        }
    }

    public static List<Path> search(Path root, Predicate<Path> condition) throws IOException {
        SearchFiles searcher = new SearchFiles(condition);
        Files.walkFileTree(root, searcher);
        return searcher.getPaths();
    }
}
