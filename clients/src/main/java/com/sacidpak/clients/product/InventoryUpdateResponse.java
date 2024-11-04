package com.sacidpak.clients.product;

public record InventoryUpdateResponse(boolean isSuccess,
                                      String failureMessage) {
}
