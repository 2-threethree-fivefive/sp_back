package starbucks3355.starbucksServer.vendor.dto.in;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import starbucks3355.starbucksServer.vendor.domain.ProductByCategory;
import starbucks3355.starbucksServer.vendor.vo.in.ProductByCategoryRequestVo;

@Getter
@NoArgsConstructor
public class ProductByCategoryRequestDto {

    private String productUuid;
    private String topCategoryName;
    private String middleCategoryName;
    private String bottomCategoryName;

    @Builder
    public ProductByCategoryRequestDto(String productUuid, String topCategoryName, String middleCategoryName, String bottomCategoryName) {
        this.productUuid = productUuid;
        this.topCategoryName = topCategoryName;
        this.middleCategoryName = middleCategoryName;
        this.bottomCategoryName = bottomCategoryName;
    }

    public ProductByCategory toEntity() {
        return ProductByCategory.builder()
                .productUuid(productUuid)
                .topCategoryName(topCategoryName)
                .middleCategoryName(middleCategoryName)
                .bottomCategoryName(bottomCategoryName)
                .build();
    }

    public static ProductByCategoryRequestDto of(ProductByCategoryRequestVo vo) {
        return ProductByCategoryRequestDto.builder()
                .productUuid(vo.getProductUuid())
                .topCategoryName(vo.getTopCategoryName())
                .middleCategoryName(vo.getMiddleCategoryName())
                .bottomCategoryName(vo.getBottomCategoryName())
                .build();
    }

}
