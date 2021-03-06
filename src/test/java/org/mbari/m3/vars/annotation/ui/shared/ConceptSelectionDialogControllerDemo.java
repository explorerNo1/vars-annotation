package org.mbari.m3.vars.annotation.ui.shared;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.mbari.m3.vars.annotation.Initializer;
import org.mbari.m3.vars.annotation.UIToolBox;

/**
 * @author Brian Schlining
 * @since 2017-09-19T11:16:00
 */
public class ConceptSelectionDialogControllerDemo extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        UIToolBox toolBox = Initializer.getToolBox();
        ConceptSelectionDialogController controller = new ConceptSelectionDialogController(toolBox);
        controller.setConcept(toolBox.getConfig().getString("app.annotation.upon.root"));
        Button button = new Button("Show dialog");
        button.setOnAction(e -> controller.getDialog().showAndWait());
        Scene scene = new Scene(button);
        scene.getStylesheets().addAll(toolBox.getStylesheets());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
