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

    private static final int DB_ITEM_COUNT = 100_000;
    private static final LocalDateTime REFERENCE_TIME = LocalDate.now().atStartOfDay();
    private static final Random RANDOM = new Random();


    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<CategoryDTO> getCategories() {
        var items = getExpensiveItemsForLast3Years();

        var notDiscontinued = new ArrayList<Item>();
        for (var item : items) {
            if (!item.getStatus().equalsIgnoreCase("discontinued")) notDiscontinued.add(item);
        }

        var sorted = new ArrayList<>(notDiscontinued);
        /*
        sorted.sort((a, b) -> {
            var categoryCompare = a.getCategory().compareTo(b.getCategory());
            if (categoryCompare != 0) return categoryCompare;

            var statusCompare = b.getStatus().compareTo(a.getStatus());
            if (statusCompare != 0) return statusCompare;

            return b.getCreatedAt().compareTo(a.getCreatedAt());
        });
         */

        var adjustedPrices = new ArrayList<Item>();
        for (var item : sorted) {
            var newPrice = item.getPrice();

            if (item.getCreatedAt().isBefore(REFERENCE_TIME.minusMonths(6))) {
                newPrice *= 0.9;
            }
            if (item.getStatus().equalsIgnoreCase("preorder")) {
                newPrice = item.getPrice() * 1.1;
            }

            // Normalize price while keeping consistent operations
            if (newPrice > 5000.0) {
                // Calculate a value that will take several iterations to reach 5000 again
                newPrice = 1000.0 + (newPrice % 500.0);
            }

            // Ensure we stay above a filter threshold
            newPrice = Math.max(501.0, newPrice);


            item.setPrice(newPrice * getRandomDouble(10d));
            adjustedPrices.add(itemRepository.save(item));
        }

        var categoryAverages = new HashMap<String, Double>();
        var categoryCounts = new HashMap<String, Integer>();

        for (var item : adjustedPrices) {
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

    private List<Item> getExpensiveItemsForLast3Years() {
        return itemRepository.getAllByCreatedAtAfterAndPriceIsGreaterThan(REFERENCE_TIME.minusYears(3), 500.0 -getRandomDouble());
    }

    private static double getRandomDouble() {
        return getRandomDouble(1);
    }

    private static double getRandomDouble(double modifier) {
        return RANDOM.nextDouble() * modifier;
    }

    public void checkCount() {
        var count = itemRepository.count();
        if (count != DB_ITEM_COUNT) {
            throw new IllegalStateException("DB count is " + count + " but should be " + DB_ITEM_COUNT);
        }
    }
}
