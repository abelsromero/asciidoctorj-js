package graal.js.runner;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Attributes;
import org.asciidoctor.Options;
import org.asciidoctor.graal.js.internal.Utils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public class AsciidoctorGraalJsTest {

    static final String SIMPLE_ADOC = """
            = Simple

            Very simple.
            """;

    static final String SECTIONS_ADOC = """
            = Simple

            A preamble
                        
            == First Section
                        
            Some text.
                        
            == Second Section
                        
            Some more text.
            """;

    @Test
    void should_create_instance() {
        Asciidoctor asciidoctor = Asciidoctor.Factory.create();

        assertThat(asciidoctor).isNotNull();
    }

    @Test
    void should_convert_from_string() {
        Asciidoctor asciidoctor = Asciidoctor.Factory.create();

        Options options = Options.builder().build();
        String output = asciidoctor.convert(SIMPLE_ADOC, options);

        assertThat(output)
                .startsWith("<div class=\"paragraph\">")
                .contains("Very simple");
    }

    @Test
    void should_convert_from_string_and_pass_options() {
        Asciidoctor asciidoctor = Asciidoctor.Factory.create();

        Options options = Options.builder()
                .headerFooter(true)
                .build();

        String output = asciidoctor.convert(SIMPLE_ADOC, options);

        assertThat(output)
                .startsWith("<!DOCTYPE html>")
                .contains("<div class=\"paragraph\">")
                .contains("Very simple");
    }

    @Test
    void should_convert_from_string_and_pass_attributes() {
        Asciidoctor asciidoctor = Asciidoctor.Factory.create();

        Options options = Options.builder()
                .headerFooter(true)
                .attributes(Attributes.builder()
                        .attribute("toc", "right")
                        .build())
                .build();

        String output = asciidoctor.convert(SECTIONS_ADOC, options);

        assertThat(output)
                .startsWith("<!DOCTYPE html>")
                .contains("<h2 id=\"_first_section\">First Section</h2>")
                .contains("<h2 id=\"_second_section\">Second Section</h2>")
                .contains("<body class=\"article toc2 toc-right\">");
    }

    @Test
    void should_convert_inplace_from_file_and_pass_options() throws IOException {
        File source = new File(Utils.fromClasspath("simple.adoc"));

        Asciidoctor asciidoctor = Asciidoctor.Factory.create();

        Options options = Options.builder()
                .headerFooter(true)
                .build();

        asciidoctor.convertFile(source, options);

        File actual = new File(source.getParent(), "simple.html");
        String output = Files.readString(Path.of(actual.getPath()));
        assertThat(output)
                .startsWith("<!DOCTYPE html>")
                .contains("<div class=\"paragraph\">")
                .contains("Very simple");
    }

    @Test
    void should_convert_inplace_from_file_and_pass_attributes() throws IOException {
        File source = new File(Utils.fromClasspath("sections.adoc"));

        Asciidoctor asciidoctor = Asciidoctor.Factory.create();

        Options options = Options.builder()
                .headerFooter(true)
                .attributes(Attributes.builder()
                        .attribute("toc", "right")
                        .build())
                .build();

        asciidoctor.convertFile(source, options);

        File actual = new File(source.getParent(), "sections.html");
        String output = Files.readString(Path.of(actual.getPath()));
        assertThat(output)
                .startsWith("<!DOCTYPE html>")
                .contains("<h2 id=\"_first_section\">First Section</h2>")
                .contains("<h2 id=\"_second_section\">Second Section</h2>")
                .contains("<body class=\"article toc2 toc-right\">");
    }
}
