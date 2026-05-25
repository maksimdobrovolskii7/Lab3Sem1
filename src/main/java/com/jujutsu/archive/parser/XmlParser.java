package com.jujutsu.archive.parser;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.jujutsu.archive.exception.InvalidMissionFormatException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class XmlParser implements MissionParser {
    private final XmlMapper xmlMapper;

    public XmlParser() {
        this.xmlMapper = XmlMapper.builder()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .build();
    }

    @Override
    public Mission parse(File file) throws InvalidMissionFormatException {
        try {
            Mission mission = xmlMapper.readValue(file, Mission.class);
            if (mission.getExtensions() == null) {
                mission.setExtensions(new HashMap<>());
            }
            return mission;
        } catch (IOException e) {
            throw new InvalidMissionFormatException("XML parse error: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean supportsFormat(File file) {
        return file.getName().toLowerCase().endsWith(".xml");
    }
}