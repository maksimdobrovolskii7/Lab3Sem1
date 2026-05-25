package parser;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jujutsu.archive.exception.InvalidMissionFormatException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class JsonParser implements MissionParser {
    private final ObjectMapper mapper;

    public JsonParser() {
        this.mapper = new ObjectMapper();
        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    public Mission parse(File file) throws InvalidMissionFormatException {
        try {
            Mission mission = mapper.readValue(file, Mission.class);
            if (mission.getExtensions() == null) {
                mission.setExtensions(new HashMap<>());
            }
            return mission;
        } catch (IOException e) {
            throw new InvalidMissionFormatException("JSON parse error: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean supportsFormat(File file) {
        return file.getName().toLowerCase().endsWith(".json");
    }
}