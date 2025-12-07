package com.canopy.allocation.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class Models {

    // this class represent one investor's input values
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InvestorInput {

        @NotBlank // Investor name must not be empty
        private String name;

        @NotNull // Requested amount must not be null
        @Min(0) // Minimum value cannot be negative
        private Double requestedAmount;

        @NotNull
        @Min(0)
        private Double averageAmount;
    }

    // this class represent full request sent from frontend: total allocation + list
    // of investors
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AllocationRequest {

        @NotNull
        @Min(0)
        private Double allocationAmount;

        @NotNull
        private List<InvestorInput> investorAmounts;
    }
}
