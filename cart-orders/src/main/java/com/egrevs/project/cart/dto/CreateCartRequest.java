package com.egrevs.project.cart.dto;

import java.util.List;

public record CreateCartRequest(
        List<CreateCartItemsRequest> requestList
) {
}
