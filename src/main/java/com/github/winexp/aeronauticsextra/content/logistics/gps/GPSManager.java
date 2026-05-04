package com.github.winexp.aeronauticsextra.content.logistics.gps;

import com.github.winexp.aeronauticsextra.registry.AeroExtraDataComponents;
import com.github.winexp.aeronauticsextra.content.blocks.gps.GPSSatelliteBlockEntity;
import dev.ryanhcode.sable.Sable;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GPSManager {
    private static final LinkedList<GPSRequest> gpsRequests = new LinkedList<>();
    private static final LinkedList<GPSSatelliteBlockEntity> gpsSatellites = new LinkedList<>();

    public static void registerSatellite(GPSSatelliteBlockEntity satellite) {
        gpsSatellites.add(satellite);
    }

    public static void unregisterSatellite(GPSSatelliteBlockEntity satellite) {
        gpsSatellites.remove(satellite);
    }

    public static void request(GPSRequest request) {
        if (request.isAlive() && !request.getLevel().isClientSide) {
            gpsRequests.add(request);
        }
    }

    public static List<GPSRequest> getRequests() {
        return List.copyOf(gpsRequests);
    }

    public static void tick() {
        var iterator = gpsRequests.iterator();
        while (iterator.hasNext()) {
            GPSRequest request = iterator.next();
            request.tick();
            if (!request.isAlive()) iterator.remove();
        }
    }

    private static boolean isSatelliteAvailable(GPSSatelliteBlockEntity satellite, Level targetLevel) {
        return satellite.getLevel() == targetLevel && satellite.canLocate();
    }

    private static SatelliteResponse locate(GPSSatelliteBlockEntity satellite, GPSRequest request) {
        Level level = request.getLevel();
        double distance = Math.sqrt(Sable.HELPER.distanceSquaredWithSubLevels(level, satellite.getBlockPos().getCenter(), request.getReceiverPos()));
        Double error = satellite.getCore().get(AeroExtraDataComponents.GPS_ERROR);
        if (error != null) {
            distance += (level.random.nextDouble() * 2 - 1) * error;
        }
        Integer cooldown = satellite.getCore().get(AeroExtraDataComponents.GPS_COOLDOWN);
        if (cooldown != null) {
            satellite.setCooldown(cooldown);
        }
        return new SatelliteResponse(satellite.getPosition(), distance);
    }

    public static void levelTick(Level level) {
        var iterator = gpsRequests.iterator();
        while (iterator.hasNext()) {
            GPSRequest request = iterator.next();
            if (level == request.getLevel()) {
                ArrayList<GPSSatelliteBlockEntity> availableSatellites = new ArrayList<>();
                for (GPSSatelliteBlockEntity satellite : gpsSatellites) {
                    if (isSatelliteAvailable(satellite, level)) availableSatellites.add(satellite);
                }
                if (availableSatellites.isEmpty() || (availableSatellites.size() < 4 && request.getAliveTime() > 1)) continue;
                ArrayList<SatelliteResponse> responses = new ArrayList<>();
                for (GPSSatelliteBlockEntity satellite : availableSatellites) {
                    responses.add(locate(satellite, request));
                }
                request.getCallback().accept(responses);
                iterator.remove();
            }
        }
    }
}
