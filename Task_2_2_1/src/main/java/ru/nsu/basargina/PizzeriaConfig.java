package ru.nsu.basargina;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Class that parses pizzeria config from json.
 */
public class PizzeriaConfig {
    public long generationInterval;
    public int warehouseCapacity;
    public int workingTimeSeconds;
    public List<Baker> bakers;
    public List<Courier> couriers;

    /**
     * Use mapper to map json keys to class fields.
     *
     * @param filePath - path to json
     * @return pizzeria config
     * @throws IOException if something went wrong with json file
     */
    public static PizzeriaConfig loadFromFile(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(filePath), PizzeriaConfig.class);
    }

    /**
     * Internal class for baker for correct json parsing.
     */
    public static class Baker {
        public String bakerName;
        public double cookingTimePerPizza;
    }

    /**
     * Internal class for courier for correct json parsing.
     */
    public static class Courier {
        public String courierName;
        public int trunkCapacity;
    }
}