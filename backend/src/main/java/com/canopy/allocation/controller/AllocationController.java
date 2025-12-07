package com.canopy.allocation.controller;

import com.canopy.allocation.model.Models;
import com.canopy.allocation.service.AllocationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/allocations") // this is base url for all endpoints
@CrossOrigin(origins = "http://localhost:4200") // this allows angular frontend to call this API
public class AllocationController {

    private final AllocationService allocationService;

    public AllocationController(AllocationService allocationService) {
        this.allocationService = allocationService;
    }

    @PostMapping("/calculate")
    // this method handles post request
    // it takes allocation input, validates it, and returns calculated result
    public ResponseEntity<Map<String, Double>> calculate(
            @Valid @RequestBody Models.AllocationRequest request) {

        Map<String, Double> result = allocationService.calculateAllocations(request);
        return ResponseEntity.ok(result);
    }
}
