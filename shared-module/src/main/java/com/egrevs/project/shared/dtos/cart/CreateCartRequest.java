package com.egrevs.project.shared.dtos.cart;

import java.util.List;

public record CreateCartRequest(
        String userId,
        List<CreateCartItemsRequest> requestList
) {
}
