package net.joshuabrandes.dbtest.control.service;

import net.joshuabrandes.dbtest.boundary.model.ItemDTO;
import net.joshuabrandes.dbtest.entity.db.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<ItemDTO> getItems() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
