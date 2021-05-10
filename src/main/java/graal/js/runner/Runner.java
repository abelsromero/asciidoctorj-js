package graal.js.runner;

import org.asciidoctor.graal.js.internal.FileAdapter;
import org.asciidoctor.graal.js.internal.Utils;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotAccess;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Run from IDE with argument `plain_fs` to show it working and printing a small HTML.
 *
 * Run from JAR (build with `./gradlew jar`) with argument `plain_fs` to see error:
 *      Cannot load script: file:/.../asciidoctorj-js/build/libs/asciidoctorj-js.jar!/app_with_plain_fs_import.mjs
 */
public class Runner {

    private static final Logger logger = Logger.getLogger(Runner.class.getCanonicalName());

    private static final String CUSTOM_FS_OPTION = "custom_fs";
    private static final String PLAIN_FS_OPTION = "plain_fs";

    private static final String JS = "js";

    public static final String ASCIIDOCTOR_JS = "graalvm/asciidoctor-graalvm-module.js";

    // TODO: benchmark if imported modules are really cached

    public static void main(String... args) throws IOException {

        if (args.length == 0) {
            System.out.println("Invalid number of arguments");
            System.out.println("\tusage: <command> " + CUSTOM_FS_OPTION + "|" + PLAIN_FS_OPTION);
            System.exit(1);
        }

        try (Context context = createContext(isCustomFilesystem(args[0]))) {

            setBindings(context);

//            Source asciidoctorSource = Source.newBuilder(JS, Utils.fromClasspathUrl(ASCIIDOCTOR_JS)).build();
//            Value result = context.eval(asciidoctorSource);

            if (!isCustomFilesystem(args[0])) {
                final String app = "app_with_plain_fs_import.mjs";
                System.out.println("== Run loading app from classpath, then app imports ECMA module");
                Source appSource = Source.newBuilder(JS, Utils.fromClasspathUrl(app))
                        .name(app)
                        .build();
                System.out.println(context.eval(appSource));

                System.out.println("== Run loading app from classpath, then app imports ECMA module");
                System.out.println(context.eval(JS, "load('" + fromClasspath(app) + "')"));
            } else {
                final String app = "app_with_custom_fs_import.mjs";
                System.out.println("== Run loading app from classpath, then app imports ECMA module with custom FS");

                // set mimetype to avoid having to use .mjs extension
                // does not work to preload, even when name is the same as in the import in app
                Source moduleSource = Source.newBuilder(JS, Utils.fromClasspathUrl(ASCIIDOCTOR_JS))
                        .name("custom-fs-asciidoctor")
                        .mimeType("application/javascript+module")
                        .build();
                context.eval(moduleSource);

                Source appSource = Source.newBuilder(JS, Utils.fromClasspathUrl(app))
                        .name(app)
                        .build();
                System.out.println(context.eval(appSource));
                System.out.println(context.eval(appSource));
                System.out.println(context.eval(appSource));
                System.out.println(context.eval(appSource));
                
            }
            // TODO test load separated modules and setting a name in Asciidoctor Source element

        }
    }

    private static boolean isCustomFilesystem(String arg) {
        return arg.equals(CUSTOM_FS_OPTION);
    }

    private static void setBindings(Context context) {
        final FileAdapter fileAdapter = new FileAdapter();
        Value bindings = context.getBindings("js");
        bindings.putMember("IncludeResolver", fileAdapter);
        bindings.putMember("FileAdapter", fileAdapter);
    }

    private static Context createContext(boolean enableCustomFilesystem) {
        Context.Builder contextBuilder = Context.newBuilder("js")
                .allowAllAccess(true)
                .allowIO(true)
                .allowPolyglotAccess(PolyglotAccess.ALL);
        return enableCustomFilesystem
                ? contextBuilder.fileSystem(new TestFS("custom-fs-asciidoctor", Utils.readFromClasspath(ASCIIDOCTOR_JS))).build()
                : contextBuilder.build();
    }

    private static String fromClasspath(String file) {
        return Runner.class.getClassLoader().getResource(file).getFile();
    }

}
