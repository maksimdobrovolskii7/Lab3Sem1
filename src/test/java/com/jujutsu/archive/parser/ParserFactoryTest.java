package com.jujutsu.archive.parser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ParserFactoryTest {

    @TempDir
    Path tempDir;

    @Test
    void getParserForFile_JsonFile_ShouldReturnJsonParser() throws IOException {
        File jsonFile = tempDir.resolve("mission.json").toFile();
        jsonFile.createNewFile();

        MissionParser parser = ParserFactory.getParserForFile(jsonFile);

        assertNotNull(parser);
        assertTrue(parser instanceof JsonParser);
    }

    @Test
    void getParserForFile_XmlFile_ShouldReturnXmlParser() throws IOException {
        File xmlFile = tempDir.resolve("mission.xml").toFile();
        xmlFile.createNewFile();

        MissionParser parser = ParserFactory.getParserForFile(xmlFile);

        assertNotNull(parser);
        assertTrue(parser instanceof XmlParser);
    }

    @Test
    void getParserForFile_YamlFile_ShouldReturnYamlParser() throws IOException {
        File yamlFile = tempDir.resolve("mission.yaml").toFile();
        yamlFile.createNewFile();

        MissionParser parser = ParserFactory.getParserForFile(yamlFile);

        assertNotNull(parser);
        assertTrue(parser instanceof YamlParser);
    }

    @Test
    void getParserForFile_TxtFile_ShouldReturnTxtParser() throws IOException {
        File txtFile = tempDir.resolve("mission.txt").toFile();
        txtFile.createNewFile();

        MissionParser parser = ParserFactory.getParserForFile(txtFile);

        assertNotNull(parser);
        assertTrue(parser instanceof TxtParser);
    }

    @Test
    void getParserForFile_UnsupportedFormat_ShouldReturnNull() throws IOException {
        File unsupportedFile = tempDir.resolve("mission.unsupported").toFile();
        unsupportedFile.createNewFile();

        MissionParser parser = ParserFactory.getParserForFile(unsupportedFile);

        assertNull(parser);
    }
}