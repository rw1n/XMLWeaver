package com.id.XMLWeaver.controller;

import com.id.XMLWeaver.logging.TextAreaAppender;
import com.id.XMLWeaver.service.DirectorySelectionService;
import com.id.XMLWeaver.service.XmlFileScannerService;
import com.id.XMLWeaver.util.FxmlLoaderFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MainViewController {
    private final FxmlLoaderFactory fxmlLoaderFactory;
    private final DirectorySelectionService directorySelectionService;
    private final XmlFileScannerService xmlFileScannerService;

    private static final Logger log = LoggerFactory.getLogger(MainViewController.class);

    @FXML
    private TextArea logArea;

    @FXML
    private Label statusLabel;

    @FXML
    private TreeView<Path> fileTreeView;

    @FXML
    public void initialize() {
        TextAreaAppender.setTextArea(logArea);
    }

    private void loadCustomerFileTree(Path customerRoot) {
        if (customerRoot != null && Files.exists(customerRoot)) {
            TreeItem<Path> root = xmlFileScannerService.buildXmlFileTree(customerRoot);
            root.setExpanded(true);

            fileTreeView.setRoot(root);
            fileTreeView.setShowRoot(true);

            fileTreeView.setCellFactory(tv -> new TreeCell<>() {
                @Override
                protected void updateItem(Path path, boolean empty) {
                    super.updateItem(path, empty);
                    setText(empty || path == null ? "" : path.getFileName().toString());
                }
            });

            fileTreeView.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
                if (selected != null && Files.isRegularFile(selected.getValue())) {
                    Path selectedPath = selected.getValue();
                    log.info("Ausgewählt: {}", selectedPath.toAbsolutePath());

                    // TODO: Vorschau oder Vergleich
                }
            });
        }
    }


    @FXML
    private void onOpenDirectoryDialog() {
        try {
            FXMLLoader loader = fxmlLoaderFactory.load("/fxml/DirectoryDialog.fxml");
            DialogPane dialogPane = loader.load();

            DirectoryDialogController controller = loader.getController();
            controller.setOwnerWindow(getWindow());

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Verzeichnisse auswählen");
            dialog.setDialogPane(dialogPane);

            Optional<ButtonType> result = dialog.showAndWait();
            if(result.isPresent() && result.get().getButtonData().isDefaultButton()) {
                directorySelectionService.setBaseLineOld(controller.getBaseLineOldDirectory());
                directorySelectionService.setBaseLineNew(controller.getBaseLineNewDirectory());
                directorySelectionService.setCustomer(controller.getCustomerDirectory());
                statusLabel.setText("Bereit");

                log.info("Verzeichnisse gesetzt");
                log.info("BaseLine Alt: {}", directorySelectionService.getBaseLineOld());
                log.info("BaseLine Neu: {}", directorySelectionService.getBaseLineNew());
                log.info("Kundenmodell: {}", directorySelectionService.getCustomer());

                loadCustomerFileTree(directorySelectionService.getCustomer());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Window getWindow() {
        return Stage.getWindows().stream()
                .filter(Window::isShowing)
                .findFirst()
                .orElse(null);
    }

}
