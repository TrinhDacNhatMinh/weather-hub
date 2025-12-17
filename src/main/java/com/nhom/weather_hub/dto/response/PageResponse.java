package com.nhom.weather_hub.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Generic paginated response")
public record PageResponse<T>(

        @Schema(description = "List of items in the current page")
        List<T> data,

        @Schema(description = "Current page index (0-based)", example = "0")
        int page,

        @Schema(description = "Number of items per page", example = "10")
        int size,

        @Schema(description = "Total number of elements", example = "125")
        long totalElements,

        @Schema(description = "Total number of pages", example = "13")
        int totalPages,

        @Schema(description = "Indicates whether this is the last page", example = "false")
        boolean last

) {
}
