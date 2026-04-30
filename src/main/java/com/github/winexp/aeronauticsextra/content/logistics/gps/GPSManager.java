package com.github.winexp.aeronauticsextra.content.logistics.gps;

import com.github.winexp.aeronauticsextra.content.blocks.gps.GPSSatelliteBlockEntity;
import dev.ryanhcode.sable.Sable;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.LinkedList;

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
        if (request.isAlive()) {
            gpsRequests.add(request);
        }
    }

    public static void tick() {
        var iterator = gpsRequests.iterator();
        while (iterator.hasNext()) {
            GPSRequest request = iterator.next();
            request.tick();
            if (!request.isAlive()) iterator.remove();
        }
    }

    public static void tickLevel(Level level) {
        var iterator = gpsRequests.iterator();
        while (iterator.hasNext()) {
            GPSRequest request = iterator.next();
            if (level == request.getLevel()) {
                ArrayList<GPSSatelliteBlockEntity> levelSatellites = new ArrayList<>();
                for (GPSSatelliteBlockEntity satellite : gpsSatellites) {
                    if (satellite.getLevel() == level) levelSatellites.add(satellite);
                }
                ArrayList<SatelliteResponse> responses = new ArrayList<>();
                for (GPSSatelliteBlockEntity satellite : levelSatellites) {
                    double distance = Math.sqrt(Sable.HELPER.distanceSquaredWithSubLevels(level, satellite.getBlockPos().getBottomCenter(), request.getPosition()));
                    responses.add(new SatelliteResponse(satellite.getLocation(), distance));
                }
                request.getCallback().accept(responses);
                iterator.remove();
            }
        }
    }
}
