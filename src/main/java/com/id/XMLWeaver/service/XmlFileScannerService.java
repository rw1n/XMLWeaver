package com.id.XMLWeaver.service;

import javafx.scene.control.TreeItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class XmlFileScannerService {
    private static final Set<String> IGNORED_DIRECTORIES = Set.of(".git", "target", "build", ".idea");
    private static final Set<String> SUPPORTED_EXTENSIONS = Set.of(".xml", ".xdata", ".config", ".xmerge");

    public TreeItem<Path> buildXmlFileTree(Path rootDir) {
        if (rootDir == null || !Files.exists(rootDir) || !Files.isDirectory(rootDir)) {
            throw new IllegalArgumentException("Ungültiges Wurzelverzeichnis: " + rootDir);
        }
        return buildTreeRecursive(rootDir);
    }

    private TreeItem<Path> buildTreeRecursive(Path dir) {
        TreeItem<Path> rootItem = new TreeItem<>(dir);

        try (Stream<Path> children = Files.list(dir)) {
            List<Path> sorted = children.sorted().collect(Collectors.toList());
            for (Path child : sorted) {
                if (Files.isDirectory(child)) {
                    if (IGNORED_DIRECTORIES.contains(child.getFileName().toString())) continue;
                    rootItem.getChildren().add(buildTreeRecursive(child));
                } else if (isSupportedXmlFile(child)) {
                    rootItem.getChildren().add(new TreeItem<>(child));
                }
            }
        } catch (IOException e) {
            // Fehler beim Zugriff auf Verzeichnisinhalt
            e.printStackTrace();
        }

        return rootItem;
    }

    public List<Path> scanXmlFiles(Path rootDir) {
        List<Path> result = new ArrayList<>();
        scanRecursive(rootDir, result);
        return result;
    }

    private void scanRecursive(Path dir, List<Path> result) {
        if (dir == null || !Files.exists(dir)) return;
        try (Stream<Path> paths = Files.list(dir)) {
            for (Path path : paths.toList()) {
                if (Files.isDirectory(path)) {
                    if (IGNORED_DIRECTORIES.contains(path.getFileName().toString())) continue;
                    scanRecursive(path, result);
                } else if (isSupportedXmlFile(path)) {
                    result.add(path);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isSupportedXmlFile(Path path) {
        String name = path.getFileName().toString().toLowerCase();
        return SUPPORTED_EXTENSIONS.stream().anyMatch(name::endsWith);
    }
}
