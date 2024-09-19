package starbucks3355.starbucksServer.vendor.infrastructure;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import starbucks3355.starbucksServer.common.utils.CursorPage;
import starbucks3355.starbucksServer.vendor.domain.ProductByCategory;
import starbucks3355.starbucksServer.vendor.domain.QProductByCategory;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ProductByCategoryRepositoryImpl implements ProductByCategoryCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;
    private final int DEFAULT_PAGE_SIZE = 20;
    private final int DEFAULT_PAGE_NUMBER = 0;

    @Override
    public CursorPage<String> getProductCategoryListByCategories(
            String topCategoryName,
            String middleCategoryName,
            String bottomCategoryName,
            Long lastId,
            Integer pageSize,
            Integer pageNumber
    ) {

        QProductByCategory productByCategory = QProductByCategory.productByCategory;
        BooleanBuilder builder = new BooleanBuilder();

        // 카테고리 필터 적용
        Optional.ofNullable(topCategoryName)
                .ifPresent(name -> builder.and(productByCategory.topCategoryName.eq(name)));

        Optional.ofNullable(middleCategoryName)
                .ifPresent(name -> builder.and(productByCategory.middleCategoryName.eq(name)));

        Optional.ofNullable(bottomCategoryName)
                .ifPresent(name -> builder.and(productByCategory.bottomCategoryName.eq(name)));

        // 마지막 ID 커서 적용
        Optional.ofNullable(lastId)
                .ifPresent(id -> builder.and(productByCategory.id.lt(id)));

        // 페이지와 페이지 크기 기본값 설정
        int currentPage = Optional.ofNullable(pageNumber).orElse(DEFAULT_PAGE_NUMBER);
        int currentPageSize = Optional.ofNullable(pageSize).orElse(DEFAULT_PAGE_SIZE);

        // offset 계산
        int offset = currentPage == 0 ? 0 : (currentPage - 1) * currentPageSize;

        List<ProductByCategory> content =  jpaQueryFactory.selectFrom(productByCategory)
                .where(builder)
                .orderBy(productByCategory.id.desc())
                .limit(currentPageSize + 1)
                .offset(currentPage)
                .fetch();

        Long nextCursor = null;
        boolean hasNext = false;

        if (content.size() > currentPageSize) {
            hasNext = true;
            content = content.subList(0, currentPageSize);  // 실제 페이지 사이즈 만큼 자르기
            nextCursor = content.get(content.size() - 1).getId();  // 마지막 항목의 ID를 커서로 설정
        }

        List<String> productUuids = content.stream()
                .map(ProductByCategory::getProductUuid)
                        .toList();

        return CursorPage.<String>builder()
                .content(productUuids)
                .nextCursor(nextCursor)
                .hasNext(hasNext)
                .pageSize(currentPageSize)
                .page(currentPage)
                .build();

    }
}
