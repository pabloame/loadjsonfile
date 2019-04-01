package com.pam.loadjsonfile;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.bind.JsonbBuilder;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

public class JsonLoader<X> implements InjectionTarget<X> {
    private static final String UNABLE_TO_LOAD_DATA = "Unable to load data into: {0}. Have you set a json file called: {1}.json?";
    private static final String ILLEGAL_ACCESS = "We cannot access: {0} on bean: {1}";
    private static final Logger logger = Logger.getLogger(JsonLoader.class.getSimpleName());
    private static final String FILE_FORMAT = ".json";
    private final InjectionTarget<X> injectionTarget;

    public JsonLoader(InjectionTarget<X> injectionTarget) {
        this.injectionTarget = injectionTarget;
    }

    @Override
    public void inject(X instance, CreationalContext<X> ctx) {
        injectionTarget.inject(instance, ctx);
        loadPropertiesIntoBean(instance);
    }

    @Override
    public void postConstruct(X instance) {
        injectionTarget.postConstruct(instance);
    }


    @Override
    public void preDestroy(X instance) {
        injectionTarget.dispose(instance);
    }


    @Override
    public void dispose(X instance) {
        injectionTarget.dispose(instance);
    }


    @Override
    public Set<InjectionPoint> getInjectionPoints() {
        return injectionTarget.getInjectionPoints();
    }


    @Override
    public X produce(CreationalContext<X> ctx) {
        return injectionTarget.produce(ctx);
    }

    private void loadPropertiesIntoBean(X instance) {
        Class clazz = instance.getClass();
        readFile(clazz, getFileName(clazz))
                .map(this::createJsonObject)
                .map(jsonObject -> createObjectFromJson(clazz, jsonObject))
                .ifPresentOrElse(target -> fillBean(instance, target),
                        () -> logger.severe(MessageFormat.format(UNABLE_TO_LOAD_DATA, instance.getClass().getSimpleName(), instance.getClass().getSimpleName())));
    }

    private void fillBean(X instance, Object target) {
        Arrays.stream(instance.getClass().getDeclaredFields())
                .peek(field -> field.setAccessible(true))
                .forEach(field -> fillField(instance, target, field));
    }

    private Object createObjectFromJson(Class<X> clazz, JsonObject jsonObject) {
        return JsonbBuilder
                .create()
                .fromJson(jsonObject.toString(), clazz);
    }

    private JsonObject createJsonObject(InputStream inputStream) {
        return Json
                .createReader(inputStream)
                .readObject();
    }

    private void fillField(X bean, Object loadedFromJson, Field fieldFromBean) {
        try {
            Field fieldLoadedFromJson = loadedFromJson.getClass().getDeclaredField(fieldFromBean.getName());
            fieldLoadedFromJson.setAccessible(true);
            fieldFromBean.set(bean, fieldLoadedFromJson.get(loadedFromJson));
        } catch (IllegalAccessException | NoSuchFieldException e) {
            logger.severe(MessageFormat.format(ILLEGAL_ACCESS, fieldFromBean.getName(), bean.getClass().getSimpleName()));
        }
    }

    private String getFileName(Class<?> aClass) {
        return Optional.of(aClass.getAnnotation(LoadJsonFile.class))
                .map(LoadJsonFile::fileName)
                .filter(fileName -> !fileName.isEmpty())
                .map(fileName -> fileName.endsWith(FILE_FORMAT) ? fileName : fileName + FILE_FORMAT)
                .orElse(aClass.getSimpleName() + FILE_FORMAT);
    }

    private Optional<InputStream> readFile(Class clazz, String fileName) {
        return Optional.ofNullable(clazz.getResourceAsStream(fileName));
    }
}
