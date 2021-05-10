package org.asciidoctor.graal.js;

import org.asciidoctor.graal.js.internal.FileAdapter;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotAccess;
import org.graalvm.polyglot.Value;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class GraalJsContextFactory {

    private static final String LANGUAGE = "js";


    public GraalContext getContext() {
        Context context = createContext();

        Value bindings = context.getBindings(LANGUAGE);
        bindings.putMember("FileAdapter", new FileAdapter());

        InputStream is = GraalContext.class.getClassLoader().getResourceAsStream("graalvm/asciidoctor_module.mjs");
        String script = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
        context.eval(LANGUAGE, script);

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
