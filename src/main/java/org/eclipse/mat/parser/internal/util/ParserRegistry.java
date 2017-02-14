package org.eclipse.mat.parser.internal.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.eclipse.mat.hprof.HprofHeapObjectReader;
import org.eclipse.mat.hprof.HprofIndexBuilder;
import org.eclipse.mat.parser.IIndexBuilder;
import org.eclipse.mat.parser.IObjectReader;
import org.eclipse.mat.snapshot.SnapshotFormat;

public class ParserRegistry {
    public static final String INDEX_BUILDER = "indexBuilder";
    public static final String OBJECT_READER = "objectReader";
    private static ParserRegistry instance = new ParserRegistry();
    public List<Parser> parsers = new ArrayList();

    public static class Parser {
        private String id;
        private IIndexBuilder indexBuilder;
        private IObjectReader objectReader;
        private Pattern[] patterns;
        private SnapshotFormat snapshotFormat;

        public Parser(String id, SnapshotFormat snapshotFormat, IObjectReader objectReader, IIndexBuilder indexBuilder) {
            this.id = id;
            this.snapshotFormat = snapshotFormat;
            this.patterns = new Pattern[snapshotFormat.getFileExtensions().length];
            for (int ii = 0; ii < snapshotFormat.getFileExtensions().length; ii++) {
                this.patterns[ii] = Pattern.compile("(.*\\.)((?i)" + snapshotFormat.getFileExtensions()[ii] + ")(\\.[0-9]*)?");
            }
            this.objectReader = objectReader;
            this.indexBuilder = indexBuilder;
        }

        public IObjectReader getObjectReader() {
            return this.objectReader;
        }

        public IIndexBuilder getIndexBuilder() {
            return this.indexBuilder;
        }

        public String getId() {
            return this.id;
        }

        public String getUniqueIdentifier() {
            return this.id;
        }

        public SnapshotFormat getSnapshotFormat() {
            return this.snapshotFormat;
        }
    }

    static {
        addParser("hprof", "hprof", new String[]{"hprof", "bin"}, new HprofHeapObjectReader(), new HprofIndexBuilder());
    }

    private ParserRegistry() {
    }

    public static void addParser(String id, String snapshotFormat, String[] extensions, IObjectReader objectReader, IIndexBuilder indexBuilder) {
        instance.parsers.add(new Parser(id, new SnapshotFormat(snapshotFormat, extensions), objectReader, indexBuilder));
    }

    public static Parser lookupParser(String uniqueIdentifier) {
        for (Parser p : instance.parsers) {
            if (uniqueIdentifier.equals(p.getUniqueIdentifier())) {
                return p;
            }
        }
        return null;
    }

    public static List<Parser> matchParser(String fileName) {
        List<Parser> answer = new ArrayList();
        for (Parser p : instance.parsers) {
            for (Pattern regex : p.patterns) {
                if (regex.matcher(fileName).matches()) {
                    answer.add(p);
                    break;
                }
            }
        }
        return answer;
    }

    public static List<Parser> allParsers() {
        return instance.parsers;
    }
}
