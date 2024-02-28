package ru.job4j.io.search;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

public class UtilSeachFiles {
    public void writeFile(List<Path> sources, String name) {
        try (PrintWriter output = new PrintWriter(new FileWriter(name))) {
                    output.print(sources);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static Predicate<Path> predicateType(ArgsName argsName) {
        Predicate<Path> predicate = null;
        String type = argsName.get("t");
        if (type == "mask") {
            String regex = argsName.get("n").replace(".", "\\.")
                    .replace("*", ".*")
                    .replace("?", ".");
            predicate = f -> f.toString().matches(regex);
        } else if (type == "name") {
            predicate = f -> f.toString().equals(argsName.get("n"));
        } else if (type == "regex") {
            predicate = f -> f.toString().matches(argsName.get("n"));
        }
        return predicate;
    }

    private static void validateArgs(ArgsName argsName) {
            if (Files.isDirectory(Path.of(argsName.get("d")))) {
                throw new IllegalArgumentException("It is not directory");
            }
            if (!(argsName.get("n").length() < 3)) {
                throw new IllegalArgumentException("Wrong format");
            }
            if (!argsName.get("t").contains("mask")
                    || !argsName.get("t").contains("name") || !argsName.get("t").contains("regex")) {
                 throw new IllegalArgumentException("Did not specify search type");
            }
            if (!argsName.get("o").contains(".txt")) {
                throw new IllegalArgumentException("Incorrect format file");
            }
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 4) {
            throw new IllegalArgumentException("Incorrect extension");
        }
        UtilSeachFiles util = new UtilSeachFiles();
        ArgsName arguments = ArgsName.of(args);
        String directory = arguments.get("d");
        String output = arguments.get("o");
        validateArgs(arguments);
        Search search = new Search();
        List<Path> fileList = search.search(Path.of(directory), util.predicateType(arguments));
        util.writeFile(fileList, output);
    }
}

