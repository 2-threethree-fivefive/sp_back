package starbucks3355.starbucksServer.vendor.dto.out;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ProductByCategoryResponseDto {

    private Long lastId;
    private Integer pageSize;
    private Boolean hasNext;
    private List<String> productUuid;

    @Builder
    public ProductByCategoryResponseDto(
            Long lastId,
            Integer pageSize,
            Boolean hasNext,
            List<String> productUuid
    ) {
        this.lastId = lastId;
        this.pageSize = pageSize;
        this.hasNext = hasNext;
        this.productUuid = productUuid;
    }

}
