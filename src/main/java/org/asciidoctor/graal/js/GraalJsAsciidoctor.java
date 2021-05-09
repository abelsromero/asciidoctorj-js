package org.asciidoctor.graal.js;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.ast.Document;
import org.asciidoctor.ast.DocumentHeader;
import org.asciidoctor.converter.JavaConverterRegistry;
import org.asciidoctor.extension.ExtensionGroup;
import org.asciidoctor.extension.JavaExtensionRegistry;
import org.asciidoctor.extension.RubyExtensionRegistry;
import org.asciidoctor.log.LogHandler;
import org.asciidoctor.syntaxhighlighter.SyntaxHighlighterRegistry;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;

public class GraalJsAsciidoctor implements Asciidoctor {

    private final GraalContext context;


    private final String ASCIIDOCTORJS_INITIALIZATION = """                         
            var asciidoctor = Asciidoctor();
                            
            let jsOptions = Object.fromEntries(javaOptions.map());
            if ('attributes' in jsOptions) {
              jsOptions.attributes = Object.fromEntries(jsOptions.attributes)
            }

            """;

    public GraalJsAsciidoctor() {
        GraalJsContextFactory asciidoctorJsLoader = new GraalJsContextFactory();
        this.context = asciidoctorJsLoader.getContext();
    }

    @Override
    public String convert(String content, Map<String, Object> options) {
        return null;
    }

    @Override
    public <T> T convert(String content, Map<String, Object> options, Class<T> expectedResult) {
        return null;
    }

    @Override
    public String convert(String content, Options options) {

        context.bind("content", content);
        context.bind(options);

        String methodCall = "asciidoctor.convert(content, jsOptions)";
        return context.eval(ASCIIDOCTORJS_INITIALIZATION + methodCall).toString();
    }

    @Override
    public <T> T convert(String content, Options options, Class<T> expectedResult) {
        return null;
    }

    @Override
    public String convert(String content, OptionsBuilder options) {
        return null;
    }

    @Override
    public <T> T convert(String content, OptionsBuilder options, Class<T> expectedResult) {
        return null;
    }

    @Override
    public void convert(Reader contentReader, Writer rendererWriter, Map<String, Object> options) throws IOException {

    }

    @Override
    public void convert(Reader contentReader, Writer rendererWriter, Options options) throws IOException {

    }

    @Override
    public void convert(Reader contentReader, Writer rendererWriter, OptionsBuilder options) throws IOException {

    }

    @Override
    public String convertFile(File file, Map<String, Object> options) {
        return null;
    }

    @Override
    public <T> T convertFile(File file, Map<String, Object> options, Class<T> expectedResult) {
        return null;
    }

    @Override
    public String convertFile(File file, Options options) {

        context.bind("sourceFile", file.getAbsolutePath());
        context.bind(options);

        String methodCall = "asciidoctor.convertFile(sourceFile, jsOptions);";
        return String.valueOf(context.eval(ASCIIDOCTORJS_INITIALIZATION + methodCall));
    }

    @Override
    public <T> T convertFile(File file, Options options, Class<T> expectedResult) {
        return null;
    }

    @Override
    public String convertFile(File file, OptionsBuilder options) {
        return null;
    }

    @Override
    public <T> T convertFile(File file, OptionsBuilder options, Class<T> expectedResult) {
        return null;
    }

    @Override
    public String[] convertDirectory(Iterable<File> directoryWalker, Map<String, Object> options) {
        return new String[0];
    }

    @Override
    public String[] convertDirectory(Iterable<File> directoryWalker, Options options) {
        return new String[0];
    }

    @Override
    public String[] convertDirectory(Iterable<File> directoryWalker, OptionsBuilder options) {
        return new String[0];
    }

    @Override
    public String[] convertFiles(Collection<File> files, Map<String, Object> options) {
        return new String[0];
    }

    @Override
    public String[] convertFiles(Collection<File> asciidoctorFiles, Options options) {
        return new String[0];
    }

    @Override
    public String[] convertFiles(Collection<File> files, OptionsBuilder options) {
        return new String[0];
    }

    @Override
    public void requireLibrary(String... requiredLibraries) {

    }

    @Override
    public void requireLibraries(Collection<String> requiredLibraries) {

    }

    @Override
    public DocumentHeader readDocumentHeader(File file) {
        return null;
    }

    @Override
    public DocumentHeader readDocumentHeader(String content) {
        return null;
    }

    @Override
    public DocumentHeader readDocumentHeader(Reader contentReader) {
        return null;
    }

    @Override
    public JavaExtensionRegistry javaExtensionRegistry() {
        return null;
    }

    @Override
    public RubyExtensionRegistry rubyExtensionRegistry() {
        return null;
    }

    @Override
    public JavaConverterRegistry javaConverterRegistry() {
        return null;
    }

    @Override
    public SyntaxHighlighterRegistry syntaxHighlighterRegistry() {
        return null;
    }

    @Override
    public ExtensionGroup createGroup() {
        return null;
    }

    @Override
    public ExtensionGroup createGroup(String groupName) {
        return null;
    }

    @Override
    public void unregisterAllExtensions() {

    }

    @Override
    public void shutdown() {

    }

    @Override
    public String asciidoctorVersion() {
        return null;
    }

    @Override
    public Document load(String content, Map<String, Object> options) {
        return null;
    }

    @Override
    public Document load(String content, Options options) {
        return null;
    }

    @Override
    public Document loadFile(File file, Map<String, Object> options) {
        return null;
    }

    @Override
    public Document loadFile(File file, Options options) {
        return null;
    }

    @Override
    public void registerLogHandler(LogHandler logHandler) {

    }

    @Override
    public void unregisterLogHandler(LogHandler logHandler) {

    }
}
