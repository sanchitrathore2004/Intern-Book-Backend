package com.internbook.internbook_backend.common.pagination;

import java.time.LocalDateTime;
import java.util.List;

public record CursorPage<T>(
        List<T> items,
        boolean hasNext,
        Integer nextLikeCount,
        LocalDateTime nextCreatedAt
) {
}
