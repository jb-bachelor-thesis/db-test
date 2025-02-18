package net.joshuabrandes.dbtest.entity.db;

import net.joshuabrandes.dbtest.entity.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

// FIXME: values in env-file
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> getAllByCreatedAtBefore(LocalDateTime createdAtBefore);

    List<Item> getAllByCreatedAtAfterAndPriceIsGreaterThan(LocalDateTime createdAtAfter, Double priceIsGreaterThan);
}
