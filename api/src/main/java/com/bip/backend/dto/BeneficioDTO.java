package com.bip.backend.dto;

import java.math.BigDecimal;

public record BeneficioDTO(long fromId, long toId, BigDecimal amount) {}
