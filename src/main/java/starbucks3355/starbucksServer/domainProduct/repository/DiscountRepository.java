package starbucks3355.starbucksServer.domainProduct.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import starbucks3355.starbucksServer.domainProduct.entity.ProductDefaultDisCount;

@Repository
public interface DiscountRepository extends JpaRepository<ProductDefaultDisCount, Long> {

}