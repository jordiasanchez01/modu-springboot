package com.laberit.Modu.domain.model;

import java.util.List;

public record PagedResult<T> (
    List<T> content,
    int page,
    int size,
    int totalPages,
    boolean hasNext) {}

