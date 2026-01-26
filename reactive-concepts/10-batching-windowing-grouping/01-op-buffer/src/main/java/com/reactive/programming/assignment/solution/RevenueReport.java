package com.reactive.programming.assignment.solution;

import java.time.LocalTime;
import java.util.Map;

public record RevenueReport(LocalTime time, Map<String, Integer> revenue) {
}
