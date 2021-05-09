package org.asciidoctor.graal.js;

import org.asciidoctor.graal.js.internal.FileAdapter;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotAccess;
import org.graalvm.polyglot.Value;

import static org.asciidoctor.graal.js.internal.Utils.fromClasspath;

public class GraalJsContextFactory {

    private static final String LANGUAGE = "js";


    public GraalContext getContext() {
        Context context = createContext();

        Value bindings = context.getBindings(LANGUAGE);
        bindings.putMember("IncludeResolver", new FileAdapter());
        // NOTE: we could create a IOHelper with the reference of the input file to reduce path searchews
        bindings.putMember("IOHelper", new FileAdapter());

        context.eval(LANGUAGE, "load('" + fromClasspath("graalvm/asciidoctor.js") + "')");

        return new GraalContext(context);
    }

    private Context createContext() {
        return Context.newBuilder("js")
                .allowAllAccess(true)
                .allowIO(true)
                .allowPolyglotAccess(PolyglotAccess.ALL)
                .build();
    }

}
