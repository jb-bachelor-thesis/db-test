package net.joshuabrandes.dbtest.control.service;

import net.joshuabrandes.dbtest.boundary.model.CategoryDTO;
import net.joshuabrandes.dbtest.boundary.model.ItemDTO;
import net.joshuabrandes.dbtest.control.mapper.ItemMapper;
import net.joshuabrandes.dbtest.entity.db.ItemRepository;
import net.joshuabrandes.dbtest.entity.model.Item;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ItemService {

    private static final LocalDateTime REFERENCE_TIME = LocalDate.now().atStartOfDay();

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<CategoryDTO> getCategories() {
        var items = getItemsForLast10Years();

        var itemsExpensive = new ArrayList<Item>();
        for (var item : items) {
            if (item.getPrice() > 100) itemsExpensive.add(item);
        }

        var notDiscontinued = new ArrayList<Item>();
        for (var item : itemsExpensive) {
            if (!item.getStatus().equalsIgnoreCase("discontinued")) notDiscontinued.add(item);
        }

        var filteredExpensiveTechAndCheapJewelry = new ArrayList<Item>();
        for (var item : notDiscontinued) {
            var add = true;
            if (item.getCategory().equalsIgnoreCase("technology")) {
                if (item.getPrice() >= 1000) add = false;
            } else if (item.getCategory().equalsIgnoreCase("jewelry")) {
                if (item.getPrice() <= 250) add = false;
            }
            if (add) filteredExpensiveTechAndCheapJewelry.add(item);
        }

        var sorted = new ArrayList<>(filteredExpensiveTechAndCheapJewelry);
        sorted.sort((a, b) -> {
            var categoryCompare = a.getCategory().compareTo(b.getCategory());
            if (categoryCompare != 0) return categoryCompare;

            var statusCompare = b.getStatus().compareTo(a.getStatus());
            if (statusCompare != 0) return statusCompare;

            return b.getCreatedAt().compareTo(a.getCreatedAt());
        });

        var adjustedPrices = new ArrayList<Item>();
        for (var item : sorted) {
            var newPrice = item.getPrice();

            if (item.getCreatedAt().isBefore(REFERENCE_TIME.minusMonths(6))) {
                newPrice *= 0.9;
            }
            if (item.getStatus().equalsIgnoreCase("preorder")) {
                newPrice = item.getPrice() * 1.1;
            }

            var adjustedItem = new Item(
                    item.getName(),
                    item.getCategory(),
                    item.getCreatedAt(),
                    newPrice,
                    item.getStatus()
            );
            adjustedPrices.add(adjustedItem);
        }

        var finalPriceFiltered = new ArrayList<Item>();
        for (var item : adjustedPrices) {
            if (item.getPrice() < 850.0) finalPriceFiltered.add(item);
        }

        var categoryAverages = new HashMap<String, Double>();
        var categoryCounts = new HashMap<String, Integer>();

        for (var item : finalPriceFiltered) {
            var category = item.getCategory();
            var currentSum = categoryAverages.getOrDefault(category, 0.0);
            var currentCount = categoryCounts.getOrDefault(category, 0);

            categoryAverages.put(category, currentSum + item.getPrice());
            categoryCounts.put(category, currentCount + 1);
        }

        var result = new ArrayList<CategoryDTO>();
        for (var entry : categoryAverages.entrySet()) {
            var category = entry.getKey();
            var sum = entry.getValue();
            var count = categoryCounts.get(category);

            var avg = sum / count;
            result.add(new CategoryDTO(category, avg));
        }

        return result;
    }

    public Map<String, ItemDTO> getExpensiveItemsByCategory() {
        var items = itemRepository.findAll();

        var categoryMap = new HashMap<String, List<Item>>();
        for (var item : items) {
            var category = item.getCategory();
            var categoryList = categoryMap.getOrDefault(category, new ArrayList<>());
            categoryList.add(item);
            categoryMap.put(category, categoryList);
        }

        var result = new HashMap<String, ItemDTO>();
        for (var category : categoryMap.keySet()) {
            var categoryList = categoryMap.get(category);
            categoryList.sort((a, b) -> b.getPrice().compareTo(a.getPrice()));

            var firstItem = categoryList.getFirst();
            result.put(category, ItemMapper.mapToItemDTO(firstItem));
        }

        return result;
    }

    private List<Item> getItemsForLast10Years() {
        // you may want to change REFERENCE_TIME depending on your dataset
        var date = REFERENCE_TIME.minusYears(10);
        return itemRepository.getAllByCreatedAtBefore(date);
    }
}
