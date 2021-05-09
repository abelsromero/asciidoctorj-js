package graal.js.runner;

import org.asciidoctor.graal.js.internal.FileAdapter;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotAccess;
import org.graalvm.polyglot.Value;

public class Runner {

    public static void main(String... args) {
        final FileAdapter fileAdapter = new FileAdapter();
            
        try (Context context = createContext()) {
            Value bindings = context.getBindings("js");

            bindings.putMember("IncludeResolver", fileAdapter);
            // NOTE: we could create a IOHelper with the reference of the input file
            bindings.putMember("IOHelper", fileAdapter);

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
