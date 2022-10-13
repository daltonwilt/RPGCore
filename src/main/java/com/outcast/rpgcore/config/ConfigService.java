package com.outcast.rpgcore.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;

public abstract class ConfigService {

    private final Class<?> clazz;

    private ObjectMapper mapper;
    private Object obj;

    protected File configFile;
    protected FileType fileType;

    private boolean newFile = false;

    /**
     * @param directory The directory where the config file will be saved.
     * @param filename  The name of the config file.
     * @throws IOException when either the file or the directory could not be created.
     */
    protected ConfigService(String directory, String filename, FileType fileType) throws IOException {
        clazz = this.getClass();
        obj = new Object();

        configFile = new File(directory + "/" + filename);
        this.fileType = fileType;

        if (!configFile.exists()) {
            if (configFile.getParentFile().exists() || configFile.getParentFile().mkdirs()) {
                if (configFile.createNewFile()) {
                    newFile = true;
                } else {
                    throw new IOException("Failed to create " + filename);
                }
            } else {
                throw new IOException("Failed to create config directory " + directory);
            }
        }
    }

    /**
     * Save the contents of the object mapper to the config file. This will override config values
     * already-present in the file.
     */
    public void save() {
        mapper = getMapperFor(fileType);

        try{
            obj = clazz.newInstance();
        } catch(InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        try {
            mapper.writeValue(configFile, obj);
        } catch (IOException | InaccessibleObjectException e) {
            e.printStackTrace();
        }
    }

    /**
     * Populate the object mapper with the contents of the config file. This will override any default
     * values.
     */
    public void load() {
        mapper = getMapperFor(fileType);

        try {
            JavaType jType = mapper.constructType(clazz);
            obj = mapper.readValue(configFile, jType);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize the config. If the config file had already existed, this will load values from the
     * config file, overriding the defaults. If it did not, this will save to the file with the
     * default values provided.
     */
    public void init() {
        if (newFile) {
            this.save();
        } else {
            this.load();
        }
    }

    private static ObjectMapper getMapperFor(FileType fileType) {
        if(fileType == FileType.JSON) {
            ObjectMapper jsonMapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .enable(SerializationFeature.INDENT_OUTPUT);
            jsonMapper.addMixIn(Vector.class, VectorMixIn.class);
            jsonMapper.setPropertyNamingStrategy(PropertyNamingStrategies.KEBAB_CASE);
            jsonMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return jsonMapper;
        } else {
            ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER))
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            yamlMapper.addMixIn(Vector.class, VectorMixIn.class);
            yamlMapper.setPropertyNamingStrategy(PropertyNamingStrategies.KEBAB_CASE);
            yamlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return yamlMapper;
        }
    }

    private interface VectorMixIn {
        @JsonIgnore
        void setX(float x);

        @JsonIgnore
        void setY(float y);

        @JsonIgnore
        void setZ(float z);

        @JsonIgnore
        void setX(int x);

        @JsonIgnore
        void setY(int y);

        @JsonIgnore
        void setZ(int z);
    }

    public enum FileType {
        JSON, YAML
    }

}
