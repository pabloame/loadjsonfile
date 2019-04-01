package com.pam.loadjsonfile;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.inject.Singleton;
import javax.json.bind.JsonbBuilder;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class NoFileNameTest {
    private static final String NAME = "Name";
    private static final int AGE = 29;
    private static final boolean ACTIVE = true;
    private static final int EXPEXTED = 2;
    private WeldContainer weldContainer;

    @BeforeTest
    public void setUp() {
        Weld weld = new Weld();
        weldContainer = weld.initialize();
    }

    @Test
    public void loadJsonWithSameNameAsClass() {
        MyDummyClassNoFileName myDummyClassNoFileName = weldContainer.select(MyDummyClassNoFileName.class).get();
        assertEquals(myDummyClassNoFileName.getName(), NAME);
        assertEquals(myDummyClassNoFileName.getAge(), AGE);
        assertEquals(myDummyClassNoFileName.isActive(), ACTIVE);
        assertEquals(myDummyClassNoFileName.getInnerClass().getTest(), EXPEXTED);
        System.out.println(JsonbBuilder.create().toJson(myDummyClassNoFileName));
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
        private InnerClass innerClass;

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public boolean isActive() {
            return active;
        }

        public InnerClass getInnerClass() {
            return innerClass;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public void setInnerClass(InnerClass innerClass) {
            this.innerClass = innerClass;
        }
    }

    public static class InnerClass {
        private int test;
        private List<Integer> list;

        public InnerClass() {
        }

        public void setTest(int test) {
            this.test = test;
        }

        public void setList(List<Integer> list) {
            this.list = list;
        }

        public int getTest() {
            return test;
        }

        public List<Integer> getList() {
            return list;
        }
    }
}
