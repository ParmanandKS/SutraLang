package com.sutralang.ide.engine;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SutraEngineTest {
    private SutraEngine engine;

    @Before
    public void setUp() {
        engine = new SutraEngine();
    }

    @Test
    public void testAssignmentAndPrint() {
        String code = "rakho x = 10; dikhao x;";
        String result = engine.run(code);
        assertEquals("10\n", result);
    }

    @Test
    public void testIfStatement() {
        String code = "rakho x = 10; agar (x == 10) { dikhao \"true\"; }";
        String result = engine.run(code);
        assertEquals("true\n", result);
    }

    @Test
    public void testIfElseIf() {
        String code = "rakho x = 7; agar (x > 10) { dikhao 1; } nahi to (x > 5) { dikhao 2; } varna { dikhao 3; }";
        String result = engine.run(code);
        assertEquals("2\n", result);
    }

    @Test
    public void testIncrementDecrement() {
        String code = "rakho x = 5; ++x; dikhao x; --x; dikhao x; x++; dikhao x; x--; dikhao x;";
        String result = engine.run(code);
        assertEquals("6\n5\n6\n5\n", result);
    }

    @Test
    public void testWhileLoop() {
        String code = "rakho i = 3; jabtak (i > 0) { dikhao i; --i; }";
        String result = engine.run(code);
        assertEquals("3\n2\n1\n", result);
    }

    @Test
    public void testNewForLoop() {
        String code = "i=0 se 2 tak { dikhao i; }";
        String result = engine.run(code);
        assertEquals("0\n1\n2\n", result);
    }

    @Test
    public void testBooleanDataTypes() {
        String code = "rakho a = sahi; rakho b = galat; agar (a == sahi) { dikhao a; } agar (b == galat) { dikhao b; }";
        String result = engine.run(code);
        assertEquals("sahi\ngalat\n", result);
    }

    @Test
    public void testFunctionAndReturn() {
        String code = "kaam jod(a, b) { lautao a + b; } rakho result = jod(5, 10); dikhao result;";
        String result = engine.run(code);
        assertEquals("15\n", result);
    }

    @Test
    public void testIfElse() {
        String code = "rakho x = 5; agar (x > 10) { dikhao \"Bada\"; } varna { dikhao \"Chota\"; }";
        String result = engine.run(code);
        assertEquals("Chota\n", result);
    }

    @Test
    public void testStringConcatenation() {
        String code = "rakho greeting = \"Namaste \" + \"Sutra\"; dikhao greeting;";
        String result = engine.run(code);
        assertEquals("Namaste Sutra\n", result);
    }
}
