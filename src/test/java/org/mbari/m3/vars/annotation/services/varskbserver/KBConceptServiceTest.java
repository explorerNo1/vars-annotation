package org.mbari.m3.vars.annotation.services.varskbserver;

import org.junit.Test;
import static org.junit.Assert.*;
import org.mbari.m3.vars.annotation.model.Concept;
import org.mbari.m3.vars.annotation.model.ConceptDetails;
import org.mbari.m3.vars.annotation.services.ConceptService;
import org.mbari.m3.vars.annotation.services.varskbserver.v1.KBConceptService;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author Brian Schlining
 * @since 2017-05-16T09:33:00
 */
public class KBConceptServiceTest {

    String endpoint = "http://m3.shore.mbari.org/kb/v1/";
    KBConceptService conceptService = new KBConceptService(endpoint);

    @Test
    public void testFetchConceptTree() throws InterruptedException, ExecutionException {
        CompletableFuture<Concept> f = conceptService.fetchConceptTree();
        while (!f.isDone()) {
            Thread.sleep(20);
        }
        Concept c = f.get();
        assertNotNull(c);
    }

    @Test
    public void testFindDetails() throws InterruptedException, ExecutionException  {
        CompletableFuture<Concept> f0 = conceptService.fetchConceptTree();
        while (!f0.isDone()) {
            Thread.sleep(20);
        }
        Concept c = f0.get();
        assertNotNull(c);

        CompletableFuture<Optional<ConceptDetails>> f1 = conceptService.findDetails(c.getName());
        while (!f1.isDone()) {
            Thread.sleep(20);
        }
        Optional<ConceptDetails> cd = f1.get();
        assertTrue(cd.isPresent());


        CompletableFuture<Optional<ConceptDetails>> f2 = conceptService.findDetails("Pandalus platyceros");
        while (!f2.isDone()) {
            Thread.sleep(20);
        }
        Optional<ConceptDetails> n = f2.get();
        assertTrue(n.isPresent());
    }
}