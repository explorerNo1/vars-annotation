package org.mbari.m3.vars.annotation.ui.roweditor;

import io.reactivex.Observable;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.mbari.m3.vars.annotation.EventBus;
import org.mbari.m3.vars.annotation.Initializer;
import org.mbari.m3.vars.annotation.UIToolBox;
import org.mbari.m3.vars.annotation.commands.CreateAssociationsCmd;
import org.mbari.m3.vars.annotation.events.AnnotationsSelectedEvent;
import org.mbari.m3.vars.annotation.model.Annotation;
import org.mbari.m3.vars.annotation.model.Association;

import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

/**
 * @author Brian Schlining
 * @since 2017-07-20T08:58:00
 */
public class RowEditorControllerDemo extends Application {

    Annotation a = new Annotation("Grimpoteuthis", "brian");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        UIToolBox toolBox = Initializer.getToolBox();

        toolBox.getEventBus()
                .toObserverable()
                .subscribe(obj -> System.out.println(obj));

        RowEditorController rowEditor = new RowEditorController();
        Scene scene = new Scene(rowEditor.getRoot());
        scene.getStylesheets().addAll("/css/roweditor.css");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
        primaryStage.setWidth(600);
        primaryStage.setHeight(400);
        primaryStage.show();

        // TODO create new annotation and fire off to servie

        a.setRecordedTimestamp(Instant.now());
        a.setObservationUuid(UUID.randomUUID());
        Association ass = new Association("eating", "Nanomia bijuga", "self");
        a.setAssociations(Arrays.asList(ass));

        new Thread(() -> {
            try {
                Thread.sleep(4000);
                rowEditor.setAnnotation(a);
                toolBox
                        .getEventBus()
                        .send(new AnnotationsSelectedEvent(a));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }

    private void initializeEventHandling() {
        EventBus eventBus = Initializer.getToolBox().getEventBus();
        Observable<Object> obs = eventBus.toObserverable();
        obs.ofType(AnnotationsSelectedEvent.class)
                .subscribe(annos -> {

                });

        obs.ofType(CreateAssociationsCmd.class)
                .subscribe(ca -> {
                    Association at = ca.getAssociationTemplate();
                    a.getAssociations().add(at);
                    eventBus.send(new AnnotationsSelectedEvent(Arrays.asList(a)));
                });
    }
}
