package org.mbari.m3.vars.annotation.ui.buttons;

import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.utils.MaterialIconFactory;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Text;
import org.mbari.m3.vars.annotation.UIToolBox;
import org.mbari.m3.vars.annotation.commands.DeleteAnnotationsCmd;
import org.mbari.m3.vars.annotation.events.AnnotationsSelectedEvent;
import org.mbari.m3.vars.annotation.messages.DeleteAnnotationsMsg;
import org.mbari.m3.vars.annotation.model.Annotation;
import org.mbari.m3.vars.annotation.model.User;

import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author Brian Schlining
 * @since 2017-08-22T14:15:00
 */
public class DeleteSelectedAnnotationsBC {

    private final Button button;
    private final UIToolBox toolBox;
    private Alert alert;

    // TODO register keyhandling using http://docs.oracle.com/javafx/2/api/javafx/scene/input/Mnemonic.html
    public DeleteSelectedAnnotationsBC(Button button, UIToolBox toolBox) {
        this.button = button;
        this.toolBox = toolBox;
        ResourceBundle i18n = toolBox.getI18nBundle();
        Platform.runLater(() -> {
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(i18n.getString("buttons.delete.dialog.title"));
            alert.setHeaderText(i18n.getString("buttons.delete.dialog.header"));
            alert.getDialogPane()
                    .getStylesheets()
                    .addAll(toolBox.getStylesheets());
            init();
        });

    }

    public void init() {
        button.setTooltip(new Tooltip(toolBox.getI18nBundle().getString("buttons.delete")));
        MaterialIconFactory iconFactory = MaterialIconFactory.get();
        Text deleteIcon = iconFactory.createIcon(MaterialIcon.DELETE, "30px");
        button.setText(null);
        button.setGraphic(deleteIcon);
        button.setDisable(true);

        toolBox.getEventBus()
                .toObserverable()
                .ofType(AnnotationsSelectedEvent.class)
                .subscribe(e -> {
                    User user = toolBox.getData().getUser();
                    boolean enabled = (user != null) && e.get().size() > 0;
                    button.setDisable(!enabled);
                });

        toolBox.getEventBus()
                .toObserverable()
                .ofType(DeleteAnnotationsMsg.class)
                .subscribe(m -> apply());

        button.setOnAction(e -> apply());
    }


    private void apply() {
        ObservableList<Annotation> annotations = toolBox.getData().getSelectedAnnotations();
        final int count = annotations.size();
        ResourceBundle i18n = toolBox.getI18nBundle();
        String content = i18n.getString("buttons.delete.dialog.content1") + " " +
                count + " " + i18n.getString("buttons.delete.dialog.content2");
        alert.setContentText(content);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get() == ButtonType.OK) {
            toolBox.getEventBus()
                    .send(new DeleteAnnotationsCmd(annotations));
        }
    }
}
