package org.mbari.m3.vars.annotation.ui.buttons;

import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.utils.MaterialIconFactory;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.text.Text;
import org.mbari.m3.vars.annotation.UIToolBox;
import org.mbari.m3.vars.annotation.commands.CreateAssociationsCmd;
import org.mbari.m3.vars.annotation.model.*;
import org.mbari.m3.vars.annotation.ui.shared.ConceptSelectionDialogController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author Brian Schlining
 * @since 2017-09-13T09:21:00
 */
public class UponBC extends AbstractBC {

    private final String associationKey;
    private final String uponRoot;
    private ConceptSelectionDialogController dialogController;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public UponBC(Button button, UIToolBox toolBox) {
        super(button, toolBox);
        this.associationKey = toolBox.getConfig()
                .getString("app.annotation.upon.linkname");
        this.uponRoot = toolBox.getConfig().getString("app.annotation.upon.root");
        dialogController = new ConceptSelectionDialogController(toolBox);
        dialogController.setConcept(this.uponRoot);
    }

    protected void init() {

        String tooltip = toolBox.getI18nBundle().getString("buttons.upon");
        MaterialIconFactory iconFactory = MaterialIconFactory.get();
        Text icon = iconFactory.createIcon(MaterialIcon.VERTICAL_ALIGN_BOTTOM, "30px");
        Text icon2 = iconFactory.createIcon(MaterialIcon.VERTICAL_ALIGN_BOTTOM, "30px");
        initializeButton(tooltip, icon);

        ResourceBundle i18n = toolBox.getI18nBundle();
        Dialog<String> dialog = dialogController.getDialog();
        dialog.setTitle(i18n.getString("buttons.upon.dialog.title"));
        dialog.setHeaderText(i18n.getString("buttons.upon.dialog.header"));
        dialog.setContentText(i18n.getString("buttons.upon.dialog.content"));
        dialog.setGraphic(icon2);
        dialog.getDialogPane().getStylesheets().addAll(toolBox.getStylesheets());

    }

    protected void apply() {
        Dialog<String> dialog = dialogController.getDialog();
        dialogController.requestFocus();
        Optional<String> opt = dialog.showAndWait();
        opt.ifPresent(selectedItem -> {
            log.debug("Select upon substrate of " + selectedItem);
            Association association = new Association(associationKey,
                    Association.VALUE_SELF,
                    selectedItem);
            List<Annotation> selectedAnnotations = new ArrayList<>(toolBox.getData().getSelectedAnnotations());
            toolBox.getEventBus()
                    .send(new CreateAssociationsCmd(association, selectedAnnotations));
        });
    }


}
