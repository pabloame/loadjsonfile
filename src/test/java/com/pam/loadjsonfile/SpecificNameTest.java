package com.pam.loadjsonfile;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.inject.Singleton;

import static org.testng.Assert.assertEquals;

/**
 * Created by Pablo on 08/01/2017.
 */
public class SpecificNameTest {
    private static final String NAME = "Name";
    private static final Integer AGE = 29;
    private static final boolean ACTIVE = false;
    private WeldContainer weldContainer;

    @BeforeTest
    public void setUp() {
        Weld weld = new Weld();
        weldContainer = weld.initialize();
    }

    @Test
    public void loadJsonWithSpecificFileName() {
        MyDummyClassSpecificFileName myDummyClassSpecificFileName = weldContainer.instance().select(MyDummyClassSpecificFileName.class).get();
        assertEquals(myDummyClassSpecificFileName.getName(), NAME);
        assertEquals(myDummyClassSpecificFileName.getAge(), AGE);
        assertEquals(myDummyClassSpecificFileName.isActive(), ACTIVE);
    }

    @AfterTest
    public void shutdown() {
        weldContainer.shutdown();
    }

    @Singleton
    @LoadJsonFile(fileName = "jsonFile.json")
    public static class MyDummyClassSpecificFileName {
        private String name;
        private Integer age;
        private boolean active;

        public String getName() {
            return name;
        }

        public Integer getAge() {
            return age;
        }

        public boolean isActive() {
            return active;
        }
    }

}
