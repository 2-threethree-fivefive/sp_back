package starbucks3355.starbucksServer.vendor.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import starbucks3355.starbucksServer.vendor.domain.ProductByCategory;

public interface ProductByCategoryRepository extends JpaRepository<ProductByCategory, Long> {
}
