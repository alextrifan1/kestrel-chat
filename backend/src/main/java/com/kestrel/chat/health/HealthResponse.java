package com.kestrel.chat.health;

public record HealthResponse(String status) {
    public static HealthResponse ok() {
        return new HealthResponse("ok");
    }
}
