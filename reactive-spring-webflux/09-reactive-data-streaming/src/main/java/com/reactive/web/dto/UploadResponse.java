package com.reactive.web.dto;

import java.util.UUID;

public record UploadResponse(UUID confirmationId, Long productsCount) {
}
