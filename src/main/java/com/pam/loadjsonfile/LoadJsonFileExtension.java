package com.pam.loadjsonfile;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessInjectionTarget;

public class LoadJsonFileExtension implements Extension {

    <X> void processInjectionTarget(@Observes ProcessInjectionTarget<X> processInjectionTarget) {
        if (annotationPresent(processInjectionTarget)) {
            processInjectionTarget.setInjectionTarget(new JsonLoader<>(processInjectionTarget.getInjectionTarget()));
        }
    }

    private <X> boolean annotationPresent(ProcessInjectionTarget<X> processInjectionTarget) {
        return processInjectionTarget.getAnnotatedType().getAnnotation(LoadJsonFile.class) != null;
    }

}
