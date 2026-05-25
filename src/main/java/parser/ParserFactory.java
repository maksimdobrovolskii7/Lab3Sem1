package parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ParserFactory {
    private static final List<MissionParser> PARSERS = new ArrayList<>();
    static {
        PARSERS.add(new JsonParser());
        PARSERS.add(new XmlParser());
        PARSERS.add(new TxtParser());
        PARSERS.add(new YamlParser());
        PARSERS.add(new PipeParser());
    }
    public static MissionParser getParserForFile(File file) {
        for (MissionParser p : PARSERS) {
            if (p.supportsFormat(file)) return p;
        }
        return null;
    }
}