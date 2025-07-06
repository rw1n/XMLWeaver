package com.id.XMLWeaver.util;

import javafx.fxml.FXMLLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FxmlLoaderFactory {
    private final ApplicationContext applicationContext;

    public FXMLLoader load(String fxmlPath) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        loader.setControllerFactory(applicationContext::getBean);
        return loader;
    }

    public <T> T loadAndGetController(String fxmlPath) throws IOException {
        FXMLLoader loader = load(fxmlPath);
        loader.load();
        return loader.getController();
    }
}
