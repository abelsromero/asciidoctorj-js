package graal.js.runner;

import org.graalvm.polyglot.io.FileSystem;

import java.net.URI;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.FileAttribute;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Based on https://github.com/oracle/graaljs/blob/master/graal-js/src/com.oracle.truffle.js.test/src/com/oracle/truffle/js/test/builtins/LoadWithCustomFsTest.java
 */
class TestFS implements FileSystem {

    private static final Logger logger = Logger.getLogger(TestFS.class.getCanonicalName());

    private final Path dummyPath;
    private final String moduleBody;
    private final String expectedPath;

    protected final Set<String> uriSpecifiers;
    protected final Set<String> stringSpecifiers;

    TestFS(String expectedPath, String moduleBody) {
        this.expectedPath = expectedPath;
        this.moduleBody = moduleBody;
        this.dummyPath = Paths.get("/dummy");
        this.uriSpecifiers = new HashSet<>();
        this.stringSpecifiers = new HashSet<>();
    }

    @Override
    public Path parsePath(URI uri) {
        uriSpecifiers.add(uri.toString());
        if (expectedPath.equals(uri.toString())) {
            return dummyPath;
        } else {
            return Paths.get(uri);
        }
    }

    @Override
    public Path parsePath(String path) {
        stringSpecifiers.add(path);
        if (expectedPath.equals(path)) {
            return dummyPath;
        } else {
            return Paths.get(path);
        }
    }

    @Override
    public void checkAccess(Path path, Set<? extends AccessMode> modes, LinkOption... linkOptions) {
    }

    @Override
    public void createDirectory(Path dir, FileAttribute<?>... attrs) {
        throw new AssertionError();
    }

    @Override
    public void delete(Path path) {
        throw new AssertionError();
    }

    @Override
    public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) {
        logger.info("Loading: " + path.toAbsolutePath());
//        if (dummyPath.equals(path)) {
        if (path.endsWith(expectedPath)) {
            logger.info("Injecting moduleBody");
            return new ReadOnlySeekableByteArrayChannel(moduleBody.getBytes(StandardCharsets.UTF_8));
        } else {
            throw new AssertionError();
        }
    }

    @Override
    public DirectoryStream<Path> newDirectoryStream(Path dir, DirectoryStream.Filter<? super Path> filter) {
        throw new AssertionError();
    }

    @Override
    public Path toAbsolutePath(Path path) {
        return path.toAbsolutePath();
    }

    @Override
    public Path toRealPath(Path path, LinkOption... linkOptions) {
        return path;
    }

    @Override
    public Map<String, Object> readAttributes(Path path, String attributes, LinkOption... options) {
        throw new AssertionError();
    }
}
