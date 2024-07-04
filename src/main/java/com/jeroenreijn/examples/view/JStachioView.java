package com.jeroenreijn.examples.view;

import com.jeroenreijn.examples.model.Presentation;
import io.jstach.jstache.JStache;
import io.jstach.jstache.JStacheConfig;
import io.jstach.jstache.JStacheFlags;
import io.jstach.jstachio.escapers.PlainText;

import java.io.IOException;
import java.io.OutputStream;

public class JStachioView {

    public static JStachioPresentationsTemplate presentationsTemplate = JStachioPresentationsTemplate.of();

    public static void presentationsWrite(PresentationsModel model, OutputStream out) throws IOException {
        presentationsTemplate.write(model, out);
    }

    @JStache(path = "templates/jstachio/presentations.jstachio.html",
            name="JStachioPresentationsTemplate")
    @JStacheConfig(contentType= PlainText.class)
    @JStacheFlags(flags = {JStacheFlags.Flag.NO_NULL_CHECKING})
    public static class PresentationsModel{

        public final Iterable<Presentation> presentationItems;

        public PresentationsModel(Iterable<Presentation> presentationItems) {
            this.presentationItems = presentationItems;
        }
    }

}
