package com.id.XMLWeaver.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import lombok.Setter;

public class TextAreaAppender extends AppenderBase<ILoggingEvent> {

    @Setter
    private static TextArea textArea;

    @Override
    protected void append(ILoggingEvent eventObject) {
        if(textArea == null) return;

        String message = eventObject.getFormattedMessage() + System.lineSeparator();
        Platform.runLater(() -> textArea.appendText(message));
    }
}
