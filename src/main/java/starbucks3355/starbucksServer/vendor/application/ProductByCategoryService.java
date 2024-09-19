package starbucks3355.starbucksServer.vendor.application;

import starbucks3355.starbucksServer.common.utils.CursorPage;
import starbucks3355.starbucksServer.vendor.dto.in.ProductByCategoryRequestDto;
import starbucks3355.starbucksServer.vendor.dto.out.ProductByCategoryResponseDto;

import java.util.List;

public interface ProductByCategoryService {

    void addProductByCategory(ProductByCategoryRequestDto productCategoryDto);
    CursorPage<String> getProductCategories(
            String topCategoryName,
            String middleCategoryName,
            String bottomCategoryName,
            Long lastId,
            Integer pageSize,
            Integer pageNumber
    );

}
