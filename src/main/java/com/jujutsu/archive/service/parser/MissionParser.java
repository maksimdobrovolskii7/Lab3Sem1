package com.jujutsu.archive.service.parser;

import com.jujutsu.archive.exception.InvalidMissionFormatException;
import java.io.File;

public interface MissionParser {
    Mission parse(File file) throws InvalidMissionFormatException;
    boolean supportsFormat(File file);
}