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
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;

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

    public static class IOHelper {

        public class JsDate {
            private final TimeZone timeZone;
            private final ZonedDateTime zonedDateTime;

            public JsDate(FileTime fileTime, TimeZone timeZone) {
                this.timeZone = timeZone;
                this.zonedDateTime = fileTime.toInstant().atZone(timeZone.toZoneId());
            }

            /**
             * Returns the time difference between UTC time and local time, in minutes
             */
            public int getTimezoneOffset() {
                return timeZone.getOffset(new Date().getTime()) / 60000;
            }

            // Returns the year
            public int getFullYear() {
                return zonedDateTime.getYear();
            }

            // Returns the month (from 0-11)
            public int getMonth() {
                return zonedDateTime.getMonthValue() - 1;
            }

            // Returns the day of the month (from 1-31)
            public int getDate() {
                return zonedDateTime.getDayOfMonth();
            }

            // Returns the hour (from 0-23)
            public int getHours() {
                return zonedDateTime.getHour();
            }

            // Returns the minutes (from 0-59)
            public int getMinutes() {
                return zonedDateTime.getMinute();
            }

            // Returns the seconds (from 0-59)
            public int getSeconds() {
                return zonedDateTime.getSecond();
            }
        }

        public Object mtime(String filePath) throws IOException {
            BasicFileAttributes basicFileAttributes = Files.readAttributes(Path.of(filePath), BasicFileAttributes.class);
            return new JsDate(basicFileAttributes.lastModifiedTime(), TimeZone.getDefault());
        }

        public void write(String target, String output) throws IOException {
            // Force UTF-8 to comply with Asciidoctor Ruby with uses mode: FILE_WRITE_MODE
            Files.writeString(Path.of(target), output,StandardCharsets.UTF_8);
        }
    }
}
