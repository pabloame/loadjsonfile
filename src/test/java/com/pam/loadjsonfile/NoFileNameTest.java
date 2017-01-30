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
public class NoFileNameTest {
    private static final String NAME = "Name";
    private static final int AGE = 29;
    private static final boolean ACTIVE = true;
    private WeldContainer weldContainer;

    @BeforeTest
    public void setUp() {
        Weld weld = new Weld();
        weldContainer = weld.initialize();
    }

    @Test
    public void loadJsonWithSameNameAsClass() {
        MyDummyClassNoFileName myDummyClassNoFileName = weldContainer.instance().select(MyDummyClassNoFileName.class).get();
        assertEquals(myDummyClassNoFileName.getName(), NAME);
        assertEquals(myDummyClassNoFileName.getAge(), AGE);
        assertEquals(myDummyClassNoFileName.isActive(), ACTIVE);
    }

    @AfterTest
    public void shutdown() {
        weldContainer.shutdown();
    }

    @Singleton
    @LoadJsonFile
    public static class MyDummyClassNoFileName {
        private String name;
        private int age;
        private boolean active;

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public boolean isActive() {
            return active;
        }
    }
}
