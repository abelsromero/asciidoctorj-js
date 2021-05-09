package graal.js.runner;

import org.asciidoctor.graal.js.internal.IOHelper;
import org.asciidoctor.graal.js.internal.IncludeResolver;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotAccess;
import org.graalvm.polyglot.Value;

public class Runner {

    public static void main(String... args) {
        try (Context context = createContext()) {
            Value bindings = context.getBindings("js");
            bindings.putMember("IncludeResolver", new IncludeResolver());
            // NOTE: we could create a IOHelper with the reference of the input file
            bindings.putMember("IOHelper", new IOHelper());

            Value result = context.eval("js", "load('" + fromClasspath("app.js") + "')");

            System.out.println(result);
        }
    }

    private static Context createContext() {
        return Context.newBuilder("js")
                .allowAllAccess(true)
                .allowIO(true)
                .allowPolyglotAccess(PolyglotAccess.ALL)
                .build();
    }

    private static String fromClasspath(String file) {
        return Runner.class.getClassLoader().getResource(file).getFile();
    }

}
