package com.canopy.allocation;

import com.canopy.allocation.model.Models;
import com.canopy.allocation.service.AllocationService;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AllocationServiceTests {

    private final AllocationService service = new AllocationService();

    @Test
    // checking test cases for simple input 1
    public void simple1Input() {
        List<Models.InvestorInput> investors = Arrays.asList(
                new Models.InvestorInput("Investor A", 100.0, 100.0),
                new Models.InvestorInput("Investor B", 25.0, 25.0));
        Models.AllocationRequest req = new Models.AllocationRequest(100.0, investors);

        Map<String, Double> result = service.calculateAllocations(req);
        assertEquals(80.0, result.get("Investor A"));
        assertEquals(20.0, result.get("Investor B"));

        System.out.println("Test simple1Input PASSED");
    }

    @Test
    // checking test cases for simple input 2
    public void simple2Input() {
        List<Models.InvestorInput> investors = Arrays.asList(
                new Models.InvestorInput("Investor A", 100.0, 100.0),
                new Models.InvestorInput("Investor B", 25.0, 25.0));
        Models.AllocationRequest req = new Models.AllocationRequest(200.0, investors);

        Map<String, Double> result = service.calculateAllocations(req);
        assertEquals(100.0, result.get("Investor A"));
        assertEquals(25.0, result.get("Investor B"));
        System.out.println("Test simple2Input PASSED");
    }

    @Test
    // checking test cases for complex input 1
    public void complex1Input() {
        List<Models.InvestorInput> investors = Arrays.asList(
                new Models.InvestorInput("Investor A", 100.0, 95.0),
                new Models.InvestorInput("Investor B", 2.0, 1.0),
                new Models.InvestorInput("Investor C", 1.0, 4.0));
        Models.AllocationRequest req = new Models.AllocationRequest(100.0, investors);

        Map<String, Double> result = service.calculateAllocations(req);
        assertEquals(97.96875, result.get("Investor A"));
        assertEquals(1.03125, result.get("Investor B"));
        assertEquals(1.0, result.get("Investor C"));
        System.out.println("Test complex1Input PASSED");
    }

    @Test
    // checking test cases for complex input 2
    public void complex2Input() {
        List<Models.InvestorInput> investors = Arrays.asList(
                new Models.InvestorInput("Investor A", 100.0, 95.0),
                new Models.InvestorInput("Investor B", 1.0, 1.0),
                new Models.InvestorInput("Investor C", 1.0, 4.0));
        Models.AllocationRequest req = new Models.AllocationRequest(100.0, investors);

        Map<String, Double> result = service.calculateAllocations(req);
        assertEquals(98.0, result.get("Investor A"));
        assertEquals(1.0, result.get("Investor B"));
        assertEquals(1.0, result.get("Investor C"));
        System.out.println("Test complex2Input PASSED");
    }
}
