package com.sacidpak.order;

import com.sacidpak.clients.product.InventoryUpdateRequest;
import com.sacidpak.clients.product.ProductClient;
import com.sacidpak.order.domain.Outbox;
import com.sacidpak.order.enums.OutboxEventType;
import com.sacidpak.order.repository.OutboxRepository;
import com.sacidpak.order.service.OutboxProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OutboxProcessorTest {

    @InjectMocks
    private OutboxProcessor outboxProcessor;

    @Mock
    private OutboxRepository outboxMessageRepository;

    @Mock
    private ProductClient productClient;

    private static final String payload = "{\n" +
            "  \"orderNumber\": \"ORD12345\",\n" +
            "  \"status\": \"NEW_ORDER\",\n" +
            "  \"products\": [\n" +
            "    {\n" +
            "      \"barcode\": \"1234567890123\",\n" +
            "      \"quantity\": 5\n" +
            "    },\n" +
            "    {\n" +
            "      \"barcode\": \"9876543210987\",\n" +
            "      \"quantity\": 10\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    @Test
    @DisplayName("Should process and update inventory when messages exist")
    void shouldProcessAndUpdateInventory_whenMessagesExist() {
        // given
        var outboxMessage = new Outbox();
        outboxMessage.setPayload(payload);
        outboxMessage.setProcessed(Boolean.FALSE);
        outboxMessage.setEventType(OutboxEventType.INVENTORY);
        var outboxList = new ArrayList<Outbox>();
        outboxList.add(outboxMessage);
        when(outboxMessageRepository.findFailedInventoryMessage()).thenReturn(outboxList);

        // when
        outboxProcessor.processOutboxMessages();

        // then
        verify(productClient, times(1)).updateInventory(any(InventoryUpdateRequest.class));
        verify(outboxMessageRepository, times(1)).save(outboxList.get(0));
    }

    @Test
    @DisplayName("Should log error when inventory update fails")
    void shouldLogError_whenInventoryUpdateFails() {
        // given
        var outboxMessage = new Outbox();
        outboxMessage.setPayload(payload);
        outboxMessage.setProcessed(Boolean.FALSE);
        outboxMessage.setEventType(OutboxEventType.INVENTORY);
        var outboxList = new ArrayList<Outbox>();
        outboxList.add(outboxMessage);
        when(outboxMessageRepository.findFailedInventoryMessage()).thenReturn(outboxList);
        doThrow(new RuntimeException("Inventory update failed")).when(productClient).updateInventory(any(InventoryUpdateRequest.class));

        // when
        outboxProcessor.processOutboxMessages();

        // then
        verify(productClient, times(1)).updateInventory(any(InventoryUpdateRequest.class));
        verify(outboxMessageRepository, never()).save(outboxList.get(0));
    }
}
