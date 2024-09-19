package starbucks3355.starbucksServer.vendor.infrastructure;

import org.springframework.stereotype.Repository;
import starbucks3355.starbucksServer.common.utils.CursorPage;

@Repository
public interface ProductByCategoryCustomRepository {

//    todo cursor page with querydsl
    CursorPage<String> getProductCategoryListByCategories(
            String topCategoryName,
            String middleCategoryName,
            String bottomCategoryName,
            Long lastId,
            Integer pageSize,
            Integer pageNumber
    );

}
