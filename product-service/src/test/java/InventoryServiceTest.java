import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.sacidpak.clients.product.InventoryOrderItemDto;
import com.sacidpak.clients.product.InventoryUpdateRequest;
import com.sacidpak.product.domain.Inventory;
import com.sacidpak.product.domain.InventoryTransaction;
import com.sacidpak.product.domain.Product;
import com.sacidpak.product.repository.InventoryRepository;
import com.sacidpak.product.repository.InventoryTransactionRepository;
import com.sacidpak.product.service.InventoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private InventoryTransactionRepository inventoryTransactionRepository;

    @InjectMocks
    private InventoryService inventoryService;

    @Test
    @DisplayName("Should update inventory and return success response")
    void shouldUpdateInventory_AndSaveTransaction_whenGivenValidRequest() {
        // given
        var product1 = new InventoryOrderItemDto("barcode1", BigDecimal.valueOf(5));
        var product2 = new InventoryOrderItemDto("barcode2", BigDecimal.valueOf(3));
        var request = new InventoryUpdateRequest("55","NEW_ORDER", Arrays.asList(product1, product2));

        var productEntity1 = Product.builder()
                .barcode("barcode1")
                .build();

        var productEntity2 = Product.builder()
                .barcode("barcode2")
                .build();

        var inventory1 = new Inventory(productEntity1, BigDecimal.valueOf(10));
        var inventory2 = new Inventory(productEntity2, BigDecimal.valueOf(8));
        var inventories = Arrays.asList(inventory1, inventory2);

        when(inventoryRepository.findByBarcodes(Arrays.asList("barcode1", "barcode2"))).thenReturn(inventories);

        // when
        var response = inventoryService.inventoryUpdate(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        verify(inventoryRepository, times(2)).save(any(Inventory.class));
        verify(inventoryTransactionRepository, times(2)).save(any(InventoryTransaction.class));
    }

    @Test
    @DisplayName("Should log error and return failure response when inventory update fails")
    void shouldReturnFailureResponse_whenInventoriesNotSufficient() {
        // given
        var product1 = new InventoryOrderItemDto("barcode1", BigDecimal.valueOf(15)); // Not sufficient
        var request = new InventoryUpdateRequest("55", "NEW_ORDER", Arrays.asList(product1));
        var productEntity1 = Product.builder()
                .barcode("barcode1")
                .build();

        var inventory1 = new Inventory(productEntity1, BigDecimal.valueOf(10));
        var inventories = Arrays.asList(inventory1);

        when(inventoryRepository.findByBarcodes(Arrays.asList("barcode1"))).thenReturn(inventories);

        // when
        var response = inventoryService.inventoryUpdate(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isFalse();
        verify(inventoryRepository, never()).save(any(Inventory.class));
        verify(inventoryTransactionRepository, never()).save(any(InventoryTransaction.class));
    }

    @Test
    @DisplayName("Should return inventory DTOs by barcodes")
    void shouldReturnInventoriesByBarcodes() {
        // given
        var barcodeList = Arrays.asList("barcode1", "barcode2");
        var productEntity1 = Product.builder()
                .barcode("barcode1")
                .build();

        var productEntity2 = Product.builder()
                .barcode("barcode2")
                .build();
        var inventory1 = new Inventory(productEntity1, BigDecimal.valueOf(10));
        var inventory2 = new Inventory(productEntity2, BigDecimal.valueOf(5));
        var inventories = Arrays.asList(inventory1, inventory2);

        when(inventoryRepository.findByBarcodes(barcodeList)).thenReturn(inventories);

        // when
        var response = inventoryService.getInventoriesByBarcodes(barcodeList);

        // then
        assertThat(response).isNotNull();
        assertThat(response.size()).isEqualTo(2);
        verify(inventoryRepository, times(1)).findByBarcodes(barcodeList);
    }
}


