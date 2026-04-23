/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.smartcampusapi;

import com.smartcampus.smartcampusapi.resources.Room;
import com.smartcampus.smartcampusapi.resources.Sensor;
import com.smartcampus.smartcampusapi.resources.SensorReading;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataStore {

    private static final Map<String, Room> rooms = new ConcurrentHashMap<>();
    private static final Map<String, Sensor> sensors = new ConcurrentHashMap<>();
    private static final Map<String, List<SensorReading>> readings = new ConcurrentHashMap<>();

    // ── Room operations ──────────────────────────────────────────────────────
    public static Map<String, Room> getRooms() {
        return rooms;
    }

    public static Room getRoomById(String id) {
        return rooms.get(id);
    }

    public static void addRoom(Room room) {
        rooms.put(room.getId(), room);
    }

    public static boolean deleteRoom(String id) {
        return rooms.remove(id) != null;
    }

    // ── Sensor operations ────────────────────────────────────────────────────
    public static Map<String, Sensor> getSensors() {
        return sensors;
    }

    public static Sensor getSensorById(String id) {
        return sensors.get(id);
    }

    public static void addSensor(Sensor sensor) {
        sensors.put(sensor.getId(), sensor);
        readings.put(sensor.getId(), new ArrayList<>());

        // Link sensor ID into its parent room's sensorIds list
        Room room = rooms.get(sensor.getRoomId());
        if (room != null) {
            room.getSensorIds().add(sensor.getId());
        }
    }

    // ── Reading operations ───────────────────────────────────────────────────
    public static List<SensorReading> getReadingsForSensor(String sensorId) {
        return readings.getOrDefault(sensorId, new ArrayList<>());
    }

    public static void addReading(String sensorId, SensorReading reading) {
        readings.computeIfAbsent(sensorId, k -> new ArrayList<>()).add(reading);

        // Side effect: keep parent sensor's currentValue in sync
        Sensor sensor = sensors.get(sensorId);
        if (sensor != null) {
            sensor.setCurrentValue(reading.getValue());
        }
    }
}
