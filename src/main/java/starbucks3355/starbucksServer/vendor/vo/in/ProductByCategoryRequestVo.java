package starbucks3355.starbucksServer.vendor.vo.in;

import lombok.Getter;

@Getter
public class ProductByCategoryRequestVo {

    private String productUuid;
    private String topCategoryName;
    private String middleCategoryName;
    private String bottomCategoryName;

}
