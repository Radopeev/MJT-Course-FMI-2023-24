package bg.sofia.uni.fmi.mjt.simcity.utility;

import bg.sofia.uni.fmi.mjt.simcity.property.billable.Billable;

import java.util.HashMap;
import java.util.Map;

public class UtilityService implements UtilityServiceAPI {
    private Map<UtilityType, Double> taxRates;
    public UtilityService(Map<UtilityType, Double> taxRates) {
        this.taxRates = Map.copyOf(taxRates);
    }

    @Override
    public <T extends Billable> double getUtilityCosts(UtilityType utilityType, T billable) {
        if (utilityType == null || billable == null) {
            throw new IllegalArgumentException("The utility or billable cannot be null.");
        }
        switch (utilityType) {
            case WATER -> {
                return taxRates.get(utilityType) * billable.getWaterConsumption();
            }
            case ELECTRICITY -> {
                return taxRates.get(utilityType) * billable.getElectricityConsumption();
            }
            case NATURAL_GAS -> {
                return taxRates.get(utilityType) * billable.getNaturalGasConsumption();
            }
        }
        return 0.0;
    }

    @Override
    public <T extends Billable> double getTotalUtilityCosts(T billable) {
        if (billable == null) {
            throw new IllegalArgumentException("The billable cannot be null");
        }
        double sum = 0.0;
        sum += taxRates.get(UtilityType.WATER) * billable.getWaterConsumption();
        sum += taxRates.get(UtilityType.ELECTRICITY) * billable.getElectricityConsumption();
        sum += taxRates.get(UtilityType.NATURAL_GAS) * billable.getNaturalGasConsumption();
        return sum;
    }

    @Override
    public <T extends Billable> Map<UtilityType, Double> computeCostsDifference(T firstBillable, T secondBillable) {
        if (firstBillable == null || secondBillable == null) {
            throw new IllegalArgumentException("First and second billable cannot be null");
        }
        Map<UtilityType, Double> result = new HashMap<>();
        result.put(UtilityType.WATER, Math.abs(getUtilityCosts(UtilityType.WATER, firstBillable) -
                getUtilityCosts(UtilityType.WATER, secondBillable)));
        result.put(UtilityType.ELECTRICITY, Math.abs(getUtilityCosts(UtilityType.ELECTRICITY, firstBillable) -
                getUtilityCosts(UtilityType.ELECTRICITY, secondBillable)));
        result.put(UtilityType.NATURAL_GAS, Math.abs(getUtilityCosts(UtilityType.NATURAL_GAS, firstBillable) -
                getUtilityCosts(UtilityType.NATURAL_GAS, secondBillable)));
        return Map.copyOf(result);
    }
}
