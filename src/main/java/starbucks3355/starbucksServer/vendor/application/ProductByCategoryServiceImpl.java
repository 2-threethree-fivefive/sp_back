package starbucks3355.starbucksServer.vendor.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import starbucks3355.starbucksServer.common.utils.CursorPage;
import starbucks3355.starbucksServer.vendor.dto.in.ProductByCategoryRequestDto;
import starbucks3355.starbucksServer.vendor.infrastructure.ProductByCategoryCustomRepository;
import starbucks3355.starbucksServer.vendor.infrastructure.ProductByCategoryRepository;

@Service
@RequiredArgsConstructor
public class ProductByCategoryServiceImpl implements ProductByCategoryService {

    private final ProductByCategoryRepository productByCategoryRepository;
    private final ProductByCategoryCustomRepository productByCategoryCustomRepository;

    @Override
    public void addProductByCategory(ProductByCategoryRequestDto productCategoryDto) {
        productByCategoryRepository.save(productCategoryDto.toEntity());
    }

    @Override
    public CursorPage<String> getProductCategories(String topCategoryName, String middleCategoryName, String bottomCategoryName, Long lastId, Integer pageSize, Integer pageNumber) {
        return productByCategoryCustomRepository.getProductCategoryListByCategories(
                topCategoryName,
                middleCategoryName,
                bottomCategoryName,
                lastId,
                pageSize,
                pageNumber
        );
    }
}
