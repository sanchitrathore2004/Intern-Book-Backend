package com.internbook.internbook_backend.common.pagination;

import java.time.Instant;
import java.util.List;

public record CursorPage<T>(
        List<T> items,
        boolean hasNext,
        Integer nextLikeCount,
        Instant nextCreatedAt,
        Integer nextId
) {
}
