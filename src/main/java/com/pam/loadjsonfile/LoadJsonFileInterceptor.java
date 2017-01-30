package com.pam.loadjsonfile;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.PostConstruct;
import javax.annotation.Priority;
import javax.enterprise.inject.Intercepted;
import javax.enterprise.inject.spi.Bean;
import javax.inject.Inject;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.json.Json;
import javax.json.JsonObject;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.logging.Logger;

import static java.text.MessageFormat.format;
import static java.util.logging.Level.SEVERE;
import static javax.interceptor.Interceptor.Priority.LIBRARY_BEFORE;

/**
 * Created by Pablo on 08/01/2017.
 */
@Interceptor
@LoadJsonFile
@Priority(LIBRARY_BEFORE)
public class LoadJsonFileInterceptor {
    private static Logger logger;
    private static final String FILE_TYPE = ".json";
    private static final String UNKNOWN_FIELD = "@LoadJsonFile: Unknown field \"{0}\" was omitted.";
    private static final String UNABLE_ACCESS = "@LoadJsonFile: Unable to access {0} field";
    private static final String IO_ERROR = "@LoadJsonFile: Unable to map {0} field to a {1} instance.";
    private static final String NOT_FOUND = "@LoadJsonFile: Unable to find {0} file to load";

    @Inject
    @Intercepted
    Bean<?> intercepted;

    @PostConstruct
    public void generateObject(final InvocationContext context) throws NoSuchFieldException {
        logger = Logger.getLogger(intercepted.getBeanClass().getName());
        try {
            JsonObject jsonObject = Json.createReader(readFile(getFileName())).readObject();
            for (String key : jsonObject.keySet()) {
                processJsonKey(context, jsonObject, key);
            }
        } catch (FileNotFoundException e) {
            logger.log(SEVERE, format(NOT_FOUND, getFileName()), e);
        }
    }

    private InputStream readFile(String fileName) throws FileNotFoundException {
        InputStream inputStream = intercepted.getBeanClass().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new FileNotFoundException(fileName);
        }
        return inputStream;
    }

    private void processJsonKey(InvocationContext context, JsonObject jsonObject, String key) throws NoSuchFieldException {
        try {
            Field field = intercepted.getBeanClass().getDeclaredField(key);
            field.setAccessible(true);
            processField(field, context.getTarget(), jsonObject);
        } catch (NoSuchFieldException e) {
            logger.info(format(UNKNOWN_FIELD, key));
        } catch (IllegalAccessException e) {
            logger.log(SEVERE, format(UNABLE_ACCESS, key), e);
        }
    }

    private String getFileName() {
        LoadJsonFile annotation = intercepted.getBeanClass().getAnnotation(LoadJsonFile.class);
        String fileName = annotation.fileName();
        if (fileName == null || fileName.length() == 0) {
            fileName = new StringBuilder()
                    .append(intercepted.getBeanClass().getSimpleName())
                    .append(FILE_TYPE)
                    .toString();
        }
        return fileName;
    }

    private void processField(Field field, Object target, JsonObject jsonObject) throws IllegalAccessException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            field.set(target, objectMapper.readValue(jsonObject.get(field.getName()).toString(), field.getType()));
        } catch (IOException e) {
            logger.log(SEVERE, format(IO_ERROR, field.getName(), field.getType()), e);
        }
    }
}
