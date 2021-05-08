package graal.js.runner;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotAccess;
import org.graalvm.polyglot.Value;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Runner {

    public static void main(String... args) {
        try (Context context = createContext()) {
            context.getBindings("js")
                    .putMember("IncludeResolver", new IncludeResolver());
// ERROR: Exception in thread "main" ReferenceError: Polyglot is not defined
//            context.getPolyglotBindings()
//                    .putMember("IncludeResolver", new IncludeResolver());
//            context.eval("js", "var IncludeResolver = Polyglot.import('IncludeResolver');");

            ClassLoader classLoader = Runner.class.getClassLoader();
//            context.eval("js", "require('fs').writeFileSync('foo.txt', 'bar', 'utf8')");
            context.eval("js", "load('" + "node_modules/@asciidoctor/core/dist/graalvm/asciidoctor.js" + "')");
            Value result = context.eval("js", "load('" + classLoader.getResource("app.js").getFile() + "')");

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

    public static class IncludeResolver {

        public String read(String path) throws IOException, URISyntaxException {
            Path filePath = Paths.get(path);

            if (filePath.toFile().exists()) {
                return Files.readString(filePath, StandardCharsets.UTF_8);
            } else {
                Path fileName = filePath.getFileName();
                URL url = this.getClass().getClassLoader().getResource(fileName.toString());
                if (url != null) {
                    return Files.readString(Paths.get(url.toURI()), StandardCharsets.UTF_8);
                } else {
                    return "";
                }
            }
        }

        public String pwd() {
            return Paths.get("").toAbsolutePath().toString();
        }
    }

}
