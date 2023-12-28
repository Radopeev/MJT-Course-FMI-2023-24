package bg.sofia.uni.fmi.mjt.simcity.plot;

import bg.sofia.uni.fmi.mjt.simcity.exception.BuildableAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.simcity.exception.BuildableNotFoundException;
import bg.sofia.uni.fmi.mjt.simcity.exception.InsufficientPlotAreaException;
import bg.sofia.uni.fmi.mjt.simcity.property.buildable.Buildable;

import java.util.HashMap;
import java.util.Map;

public class Plot<E extends Buildable> implements PlotAPI<E> {
    private int buildableArea;
    private Map<String, E> buildings;

    public Plot(int buildableArea) {
        this.buildableArea = buildableArea;
        buildings = new HashMap<>();
    }

    @Override
    public void construct(String address, E buildable) {
        if (address == null || address.isBlank() || buildable == null) {
            throw new IllegalArgumentException("The address and buildable cannot be null.");
        }
        if (buildings.containsKey(address)) {
            throw new BuildableAlreadyExistsException("This buildable already exists.");
        }
        if (buildableArea - buildable.getArea() < 0) {
            throw new InsufficientPlotAreaException("There is no enough area.");
        }
        buildableArea -= buildable.getArea();
        buildings.put(address, buildable);
    }

    @Override
    public void constructAll(Map<String, E> buildables) {
        if (buildables == null || buildables.isEmpty()) {
            throw new IllegalArgumentException("Buildables cannot be null or empty");
        }
        for (Map.Entry<String, E> currBuildable : buildables.entrySet()) {
            construct(currBuildable.getKey(), currBuildable.getValue());
        }
    }

    @Override
    public void demolish(String address) {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Address cannot be null or blank.");
        }
        if (!buildings.containsKey(address)) {
            throw new BuildableNotFoundException("There is no buildable with this address");
        }
        buildableArea += buildings.get(address).getArea();
        buildings.remove(address);
    }

    @Override
    public void demolishAll() {
        for (E currBuildable: buildings.values()) {
            buildableArea += currBuildable.getArea();
        }
        buildings.clear();
    }

    @Override
    public Map<String, E> getAllBuildables() {
        return Map.copyOf(buildings);
    }

    @Override
    public int getRemainingBuildableArea() {
        return buildableArea;
    }
}
