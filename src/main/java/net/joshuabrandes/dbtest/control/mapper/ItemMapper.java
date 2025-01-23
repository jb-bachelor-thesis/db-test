package net.joshuabrandes.dbtest.control.mapper;

import net.joshuabrandes.dbtest.boundary.model.ItemDTO;
import net.joshuabrandes.dbtest.entity.model.Item;

public class ItemMapper {

    public static ItemDTO mapToItemDTO(Item item) {
        return new ItemDTO(
                item.getName(),
                item.getCategory(),
                item.getCreatedAt(),
                item.getPrice(),
                item.getStatus()
        );
    }

    public static Item mapToEntity(ItemDTO itemDTO) {
        return new Item(
                itemDTO.getName(),
                itemDTO.getCategory(),
                itemDTO.getCreatedAt(),
                itemDTO.getPrice(),
                itemDTO.getStatus()
        );
    }
}
