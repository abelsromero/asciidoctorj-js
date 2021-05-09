package org.asciidoctor.graal.js;

import org.asciidoctor.Options;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

public class GraalContext implements AutoCloseable {

    private static final String LANGUAGE = "js";

    private final Context context;

    public GraalContext(Context context) {
        this.context = context;
    }

    @Override
    public void close() throws Exception {
        context.close();
    }

    public void bind(Options options) {
        bind("javaOptions", options);
    }

    public void bind(String name, Object value) {
        context.getBindings("js")
                .putMember(name, value);
    }

    public Value eval(String source) {
        return context.eval(LANGUAGE, source);
    }
}
