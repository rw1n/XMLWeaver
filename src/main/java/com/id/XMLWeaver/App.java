package com.id.XMLWeaver;

import jakarta.annotation.PostConstruct;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class App extends Application {

    private ConfigurableApplicationContext springContext;

    @Override
    public void init() {
        springContext = new SpringApplicationBuilder(XmlWeaverApplication.class).run();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
        loader.setControllerFactory(springContext::getBean);
        Parent root = loader.load();

        stage.setTitle("XMLWeaver");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Override
    public void stop() {
        springContext.close();
    }

}
