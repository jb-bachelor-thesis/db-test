package net.joshuabrandes.dbtest.boundary;

import net.joshuabrandes.dbtest.boundary.model.ItemDTO;
import net.joshuabrandes.dbtest.control.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/v1/items")
    public ResponseEntity<List<ItemDTO>> getItems() {
        return ResponseEntity.ok(itemService.getItems());
    }
}
