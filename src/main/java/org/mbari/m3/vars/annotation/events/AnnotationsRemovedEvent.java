package org.mbari.m3.vars.annotation.events;

import com.google.common.collect.ImmutableList;
import org.mbari.m3.vars.annotation.model.Annotation;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Brian Schlining
 * @since 2017-07-20T17:20:00
 */
public class AnnotationsRemovedEvent extends UIEvent<Collection<Annotation>> {

    public AnnotationsRemovedEvent(Object source, Collection<Annotation> annotations) {
        super(source, ImmutableList.copyOf(annotations));
    }

    public AnnotationsRemovedEvent(Collection<Annotation> annotations) {
        this(null, annotations);
    }

    public AnnotationsRemovedEvent(Annotation annotation) {
        this(null, Arrays.asList(annotation));
    }
}
