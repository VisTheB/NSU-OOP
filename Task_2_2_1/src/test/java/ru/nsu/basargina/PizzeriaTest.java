package ru.nsu.basargina;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;

class PizzeriaTest {
    @Test
    void testPizzeriaWorkflow() throws InterruptedException, IOException {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        PizzeriaConfig config = PizzeriaConfig.loadFromFile("src/test/resources/pizzeria-config.json");

        Pizzeria pizzeria = new Pizzeria(config);

        pizzeria.start();

        Thread.sleep(config.getWorkingTimeSeconds() * 1000L);

        pizzeria.stopPizzeria();

        assertTrue(pizzeria.orderQueue.isEmpty());
        assertEquals(0, pizzeria.warehouse.getCurrentCount());
        assertTrue(outContent.toString().contains("[Order 1 CREATED]"));
        assertTrue(outContent.toString().contains("[Order 1 READY_FOR_DELIVERY]"));
        assertTrue(outContent.toString().contains("[Order 1 DELIVERING]"));
        assertTrue(outContent.toString().contains("[Order 1 DELIVERED]"));

        System.setOut(originalOut);
    }
}