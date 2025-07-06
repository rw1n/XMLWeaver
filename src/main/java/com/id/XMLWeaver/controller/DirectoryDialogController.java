package com.id.XMLWeaver.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;

@Component
public class DirectoryDialogController {
    @FXML
    private TextField baseLineOldField;
    @FXML
    private TextField baseLineNewField;
    @FXML
    private  TextField customerField;

    @Setter
    private Window ownerWindow;

    public Path getBaseLineOldDirectory() {
        return getDirectoryFromField(baseLineOldField);
    }

    public Path getBaseLineNewDirectory() {
        return getDirectoryFromField(baseLineNewField);
    }

    public Path getCustomerDirectory() {
        return getDirectoryFromField(customerField);
    }

    @FXML
    private void onChooseBaseLineOld() {
        chooseDirectoryForField(baseLineOldField);
    }

    @FXML
    private void onChooseBaseLineNew() {
        chooseDirectoryForField(baseLineNewField);
    }

    @FXML
    private void onChooseCustomer() {
        chooseDirectoryForField(customerField);
    }

    private void chooseDirectoryForField(TextField field) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Verzeichnis wählen");
        File dir = chooser.showDialog(ownerWindow);
        if (dir != null) {
            field.setText(dir.getAbsolutePath());
        }
    }

    private Path getDirectoryFromField(TextField field) {
        String text = field.getText();
        return text == null || text.isBlank() ? null : Path.of(text);
    }

}
