package net.joshuabrandes.dbtest.boundary;

import net.joshuabrandes.dbtest.boundary.model.CategoryDTO;
import net.joshuabrandes.dbtest.boundary.model.ItemDTO;
import net.joshuabrandes.dbtest.control.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/v1/categories")
    public ResponseEntity<List<CategoryDTO>> getItems() {
        return ResponseEntity.ok(itemService.getCategories());
    }

    @GetMapping("/v1/items/expensive-by-category")
    public ResponseEntity<Map<String, ItemDTO>> getExpensiveItemsByCategory() {
        return ResponseEntity.ok(itemService.getExpensiveItemsByCategory());
    }
}
