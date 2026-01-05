package com.tyanns.luckyvalue.server;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;

public class LuckyValueManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final Type MAP_TYPE =
            new TypeToken<Map<String, Integer>>() {
            }.getType();

    private static final Path SAVE_PATH =
            FabricLoader.getInstance()
                    .getConfigDir()
                    .resolve("lucky_value")
                    .resolve("lucky_value.json");

    private static final Map<UUID, Integer> LUCKY_MAP = new HashMap<>();

    private static boolean loaded = false;

    private LuckyValueManager() {}

    public static void load() {
        if (loaded) return;
        loaded = true;

        try {
            if (Files.notExists(SAVE_PATH)) {
                Files.createDirectories(SAVE_PATH.getParent());
                save(); // 创建空文件
                return;
            }

            String json = Files.readString(SAVE_PATH);
            Map<String, Integer> raw = GSON.fromJson(json, MAP_TYPE);
            if (raw == null) return;

            raw.forEach((k, v) ->
                    LUCKY_MAP.put(UUID.fromString(k), Math.max(-100, Math.min(100, v))));

        } catch (Exception e) {
            System.err.println("[LuckyValue] Failed to load file");
            e.printStackTrace();
        }
    }

    public static void save() {
        try {
            Files.createDirectories(SAVE_PATH.getParent());

            Map<String, Integer> raw = new HashMap<>();
            LUCKY_MAP.forEach((k, v) -> raw.put(k.toString(), v));

            Files.writeString(
                    SAVE_PATH,
                    GSON.toJson(raw)
            );
        } catch (IOException e) {
            System.err.println("[LuckyValue] Failed to save file");
            e.printStackTrace();
        }
    }

    public static int getLuckyValue(UUID uuid) {
        return LUCKY_MAP.getOrDefault(uuid, 0);
    }

    public static void setLuckyValue(UUID uuid, int value) {
        LUCKY_MAP.put(uuid, value);
    }

    public static Map<UUID, Integer> getLuckyMap() {
        return LUCKY_MAP;
    }
}
