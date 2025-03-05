package ru.nsu.basargina;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Class that parses pizzeria config from json.
 */
public class PizzeriaConfig {
    private int warehouseCapacity;
    private int workingTimeSeconds;
    private List<Baker> bakers;
    private List<Courier> couriers;

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
     * Getter for warehouse capacity.
     *
     * @return warehouse capacity
     */
    public int getWarehouseCapacity() {
        return warehouseCapacity;
    }

    /**
     * Getter for pizzeria working time.
     *
     * @return working time in seconds
     */
    public int getWorkingTimeSeconds() {
        return workingTimeSeconds;
    }

    /**
     * Getter for bakers list.
     *
     * @return bakers list
     */
    public List<Baker> getBakers() {
        return bakers;
    }

    /**
     * Getter for couriers list.
     *
     * @return couriers list
     */
    public List<Courier> getCouriers() {
        return couriers;
    }

    /**
     * Internal class for baker for correct json parsing.
     */
    public static class Baker {
        private int bakerId;
        private String bakerName;
        private double cookingTimePerPizza;

        /**
         * Getter for baker id.
         *
         * @return baker id
         */
        public int getBakerId() {
            return bakerId;
        }

        /**
         * Getter for baker's name.
         *
         * @return baker's name
         */
        public String getBakerName() {
            return bakerName;
        }

        /**
         * Getter for time needed to cook one pizza.
         *
         * @return cooking time for pizza
         */
        public double getCookingTimePerPizza() {
            return cookingTimePerPizza;
        }
    }

    /**
     * Internal class for courier for correct json parsing.
     */
    public static class Courier {
        private int courierId;
        private String courierName;
        private int trunkCapacity;

        /**
         * Getter for courier id.
         *
         * @return courier id
         */
        public int getCourierId() {
            return courierId;
        }

        /**
         * Getter for courier's name.
         *
         * @return courier's name
         */
        public String getCourierName() {
            return courierName;
        }

        /**
         * Getter for courier's trunk capacity.
         *
         * @return trunk capacity
         */
        public int getTrunkCapacity() {
            return trunkCapacity;
        }
    }
}