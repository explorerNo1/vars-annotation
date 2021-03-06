package org.mbari.m3.vars.annotation.ui.shared;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TreeItem;

import java.util.function.Predicate;

/**
 * @author Brian Schlining
 * @since 2018-04-04T15:14:00
 */
public class FilterableTreeItem<T> extends TreeItem<T> {
    private final ObservableList<TreeItem<T>> sourceChildren = FXCollections.observableArrayList();
    private final FilteredList<TreeItem<T>> filteredChildren = new FilteredList<>(sourceChildren);
    private final ObjectProperty<Predicate<T>> predicate = new SimpleObjectProperty<>();

    public FilterableTreeItem(T value) {
        super(value);

        filteredChildren.predicateProperty().bind(Bindings.createObjectBinding(() -> {
            Predicate<TreeItem<T>> p = child -> {
                if (child instanceof FilterableTreeItem) {
                    ((FilterableTreeItem<T>) child).predicateProperty().set(predicate.get());
                }
                if (predicate.get() == null || !child.getChildren().isEmpty()) {
                    return true;
                }
                return predicate.get().test(child.getValue());
            };
            return p;
        } , predicate));

        filteredChildren.addListener((ListChangeListener<TreeItem<T>>) c -> {
            while (c.next()) {
                getChildren().removeAll(c.getRemoved());
                getChildren().addAll(c.getAddedSubList());
            }
        });
    }

    public ObservableList<TreeItem<T>> getSourceChildren() {
        return sourceChildren;
    }

    public ObjectProperty<Predicate<T>> predicateProperty() {
        return predicate;
    }

}

