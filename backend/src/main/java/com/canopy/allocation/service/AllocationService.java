package com.canopy.allocation.service;

import com.canopy.allocation.model.Models.AllocationRequest;
import com.canopy.allocation.model.Models.InvestorInput;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AllocationService {

    // A tiny number used to safely compare decimal values.Because double
    // calculations can have small rounding errors,
    // we treat two numbers as equal if their difference is smaller than EPS
    // (0.000000001)
    private static final double EPS = 1e-9;

    /**
     * - If the total money available is enough to cover all investor requests,
     * everyone receives exactly what they asked for.
     * 
     * 
     * - If there isn't enough money, we distribute the allocation based on each
     * investors average investment history. No investor can receive more than
     * their requested amount, and any leftover amount gets redistributed among
     * the remaining investors using the same rule.
     **/
    public Map<String, Double> calculateAllocations(AllocationRequest request) {
        double allocation = request.getAllocationAmount();
        List<InvestorInput> investors = request.getInvestorAmounts();

        // getting total amount as requested value
        double totalRequested = investors.stream()
                .mapToDouble(InvestorInput::getRequestedAmount)
                .sum();

        Map<String, Double> result = new LinkedHashMap<>();

        // Simple case: not oversubscribed
        if (allocation >= totalRequested - EPS) {
            for (InvestorInput inv : investors) {
                result.put(inv.getName(), inv.getRequestedAmount());
            }
            return result;
        }

        // Oversubscribed: this will run average-based and capped proration
        List<InvestorInput> remaining = new ArrayList<>(investors);
        Map<String, Double> allocs = new LinkedHashMap<>();
        investors.forEach(inv -> allocs.put(inv.getName(), 0.0));

        double remainingAllocation = allocation;

        while (!remaining.isEmpty() && remainingAllocation > EPS) {
            double totalAverage = remaining.stream()
                    .mapToDouble(InvestorInput::getAverageAmount)
                    .sum();

            Map<String, Double> shares = new HashMap<>();
            if (Math.abs(totalAverage) < EPS) {
                // if all averages are zero then split equally
                double equalShare = remainingAllocation / remaining.size();
                for (InvestorInput inv : remaining) {
                    shares.put(inv.getName(), equalShare);
                }
            } else {
                for (InvestorInput inv : remaining) {
                    // othersiee, give based on averageAmount
                    double share = remainingAllocation * inv.getAverageAmount() / totalAverage;
                    shares.put(inv.getName(), share);
                }
            }

            List<InvestorInput> nextRemaining = new ArrayList<>();
            double newRemainingAllocation = 0.0;

            for (InvestorInput inv : remaining) {
                String name = inv.getName();
                double currentAlloc = allocs.get(name);
                double cap = inv.getRequestedAmount() - currentAlloc;
                double share = shares.get(name);
                double proposed = currentAlloc + share;

                if (proposed >= inv.getRequestedAmount() - EPS) {
                    // We hit or exceed the requested amount. Cap it.
                    double give = cap;
                    allocs.put(name, currentAlloc + give);
                    // unused portion of the share gets redistributed
                    newRemainingAllocation += Math.max(0.0, share - give);
                } else {
                    // allocate the share without reaching the cap
                    allocs.put(name, proposed);
                }
            }

            // now keep investors that are still under their requested amount
            for (InvestorInput inv : remaining) {
                String name = inv.getName();
                if (allocs.get(name) < inv.getRequestedAmount() - EPS) {
                    nextRemaining.add(inv);
                }
            }

            remaining = nextRemaining;
            remainingAllocation = newRemainingAllocation;
        }

        // Final rounding to 6 decimal places for stable JSON output
        for (InvestorInput inv : investors) {
            String name = inv.getName();
            double value = allocs.getOrDefault(name, 0.0);
            // Round the value to 6 decimal places forclean and consistent output.
            double rounded = Math.round(value * 1_000_000d) / 1_000_000d;
            result.put(name, rounded);
        }

        return result;
    }
}