package starbucks3355.starbucksServer.vendor.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import starbucks3355.starbucksServer.common.entity.BaseEntity;

@Entity
@Getter
@NoArgsConstructor
public class ProductByCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 50)
    private String productUuid;
    @Column(nullable = false, length = 50)
    private String topCategoryName;
    @Column(nullable = false, length = 50)
    private String middleCategoryName;
    @Column(nullable = false, length = 50)
    private String bottomCategoryName;

    @Builder
    public ProductByCategory(String productUuid, String topCategoryName, String middleCategoryName, String bottomCategoryName) {
        this.productUuid = productUuid;
        this.topCategoryName = topCategoryName;
        this.middleCategoryName = middleCategoryName;
        this.bottomCategoryName = bottomCategoryName;
    }

}
