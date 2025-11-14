package com.egrevs.project.cart.dto.cart;

import java.util.List;

public record CreateCartRequest(
        List<CreateCartItemsRequest> requestList
) {
}
