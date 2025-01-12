package starbucks3355.starbucksServer.category.sevice;

import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import starbucks3355.starbucksServer.category.dto.request.BottomCategoryRequestDto;
import starbucks3355.starbucksServer.category.dto.request.MiddleCategoryRequestDto;
import starbucks3355.starbucksServer.category.dto.request.TopCategoryRequestDto;
import starbucks3355.starbucksServer.category.dto.response.BottomCategoryResponseDto;
import starbucks3355.starbucksServer.category.dto.response.MiddleCategoryListResponseDto;
import starbucks3355.starbucksServer.category.dto.response.MiddleCategoryResponseDto;
import starbucks3355.starbucksServer.category.dto.response.TopCategoryResponseDto;
import starbucks3355.starbucksServer.category.entity.BottomCategory;
import starbucks3355.starbucksServer.category.entity.MiddleCategory;
import starbucks3355.starbucksServer.category.entity.TopCategory;
import starbucks3355.starbucksServer.category.repository.BottomCategoryRepository;
import starbucks3355.starbucksServer.category.repository.MiddleCategoryRepository;
import starbucks3355.starbucksServer.category.repository.TopCategoryRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

	private final TopCategoryRepository topCategoryRepository;
	private final MiddleCategoryRepository middleCategoryRepository;
	private final BottomCategoryRepository bottomCategoryRepository;

	@Transactional
	@Override
	public void createTopCategory(TopCategoryRequestDto topCategoryRequestDto) {
		if (topCategoryRepository.existsByCategoryName(topCategoryRequestDto.getTopCategoryName())) {
			throw new IllegalArgumentException("이미 존재하는 카테고리입니다.");
		}

		// DTO를 엔티티 변환해서 생성된 카테고리 코드 주입
		TopCategory topCategory = topCategoryRequestDto.toEntity(topCategoryRequestDto.getTopCategoryName());
		try {
			topCategoryRepository.save(topCategory);

		} catch (IllegalArgumentException e) {
			log.error(e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("Unexpected error occurred", e);
			throw new RuntimeException("카테고리 생성 중 오류가 발생했습니다.", e);
		}
	}

	@Transactional
	@Override
	public void createMiddleCategory(List<MiddleCategoryRequestDto> middleCategoryRequestDto) {
		// 중복체크 : topcategoryid와 middlecategoryname을 동시에 검사
		for (MiddleCategoryRequestDto dto : middleCategoryRequestDto) {
			log.info("topcategoryid : {}, categoryname: {}", dto.getTopCategoryId(), dto.getMiddleCategoryName());
			if (middleCategoryRepository.existsByTopCategoryIdAndCategoryName(
				dto.getTopCategoryId(),
				dto.getMiddleCategoryName())) {
				throw new IllegalArgumentException("이미 존재하는 카테고리입니다.");
			}

			try { // Top 카테고리 코드가 존재하는지 확인
				TopCategory topCategory = topCategoryRepository.findById(
					dto.getTopCategoryId()).orElseThrow(
					() -> new IllegalArgumentException("존재하지 않는 Top 카테고리 id 입니다.")
				); //

				// Topcategory도 객체화 시키는 이유는 middlecategory가 속할 topcategory를 지정해주는거임
				middleCategoryRepository.save(dto.toEntity(topCategory));
			} catch (IllegalArgumentException e) {
				log.error(e.getMessage());
				throw e;
			} catch (Exception e) {
				log.error("Unexpected error occurred", e);
				throw new RuntimeException("카테고리 생성 중 오류가 발생했습니다.", e);
			}
		}

	}

	@Transactional
	@Override
	public void createBottomCategory(List<BottomCategoryRequestDto> bottomCategoryRequestDto) {

		for (BottomCategoryRequestDto dto : bottomCategoryRequestDto) {
			log.info("middlecategoryid : {}, categoryname: {}", dto.getMiddleCategoryId(), dto.getBottomCategoryName());
			if (bottomCategoryRepository.existsByMiddleCategoryIdAndCategoryName(
				dto.getMiddleCategoryId(),
				dto.getBottomCategoryName())) {
				throw new IllegalArgumentException("이미 존재하는 카테고리입니다.");
			}

			try { // Middle 카테고리 코드가 존재하는지 확인
				MiddleCategory middleCategory = middleCategoryRepository.findById(
					dto.getMiddleCategoryId()).orElseThrow(
					() -> new IllegalArgumentException("존재하지 않는 Middle 카테고리 id 입니다.")
				);

				bottomCategoryRepository.save(dto.toEntity(middleCategory));
			} catch (IllegalArgumentException e) {
				log.error(e.getMessage());
				throw e;
			} catch (Exception e) {
				log.error("Unexpected error occurred", e);
				throw new RuntimeException("카테고리 생성 중 오류가 발생했습니다.", e);
			}
		}

	}

	// top 전체 카테고리 조회
	@Override
	// top 카테고리 조회
	// db에 저장 돼 있는 객체를 dto로 변환 시키는 과정
	public List<TopCategoryResponseDto> getTopCategories() {
		return topCategoryRepository.findAll(Sort.by(Sort.Direction.ASC, "id")).stream().map(
			topCategory -> TopCategoryResponseDto.builder()
				.id(topCategory.getId())
				.topCategoryName(topCategory.getCategoryName())
				.build()
		).toList();
	}

	// middle 전체 카테고리 조회
	@Override
	public List<MiddleCategoryResponseDto> getMiddleCategories() {

		return middleCategoryRepository.findAll().stream().map(
			middleCategory -> MiddleCategoryResponseDto.builder()
				.id(middleCategory.getId())
				.middleCategoryName(middleCategory.getCategoryName())
				.build()
		).toList();
	}

	// bottom 전체 카테고리 조회
	@Override
	public List<BottomCategoryResponseDto> getBottomCategories() {
		return bottomCategoryRepository.findAll().stream().map(
			bottomCategory -> BottomCategoryResponseDto.builder()
				.id(bottomCategory.getId())
				.bottomCategoryName(bottomCategory.getCategoryName())
				.build()
		).toList();
	}

	// 미들 카테고리 Name = '카테고리' 목록 조회
	@Override
	@Transactional(readOnly = true)
	public MiddleCategoryListResponseDto getMiddleCategoryByNameAndTopCategoryId(Integer topCategoryId,
		String middleCategoryName) {
		// try {
		// top 카테고리 id로 middle 카테고리 조회
		List<MiddleCategory> middleCategories = middleCategoryRepository.findByTopCategoryIdAndCategoryNameUsingQuery(
			topCategoryId, middleCategoryName);

		if (middleCategories.isEmpty()) {
			return new MiddleCategoryListResponseDto(Collections.emptyList());
		}
		// 각 MiddleCategory에 속한 BottomCategory 목록 조회 및 반환
		List<MiddleCategoryResponseDto> middleCategoryResponseDtos = middleCategories.stream().map(
			middleCategory -> {
				List<BottomCategory> bottomCategories = bottomCategoryRepository.findByMiddleCategoryId(
					middleCategory.getId());
				// BottomCategoryResponseDto로 변환
				List<BottomCategoryResponseDto> bottomCategoryResponseDtos = bottomCategories.stream().map(
					bottomCategory -> BottomCategoryResponseDto.builder()
						.id(bottomCategory.getId())
						.bottomCategoryName(bottomCategory.getCategoryName())
						.build()
				).toList();
				// MiddleCategoryResponseDto로 변환
				return MiddleCategoryResponseDto.builder()
					.id(middleCategory.getId())
					.middleCategoryName(middleCategory.getCategoryName())
					.bottomCategories(bottomCategoryResponseDtos)
					.build();

			}).toList();

		return new MiddleCategoryListResponseDto(middleCategoryResponseDtos);

	}

	@Override
	@Transactional(readOnly = true)
	public List<MiddleCategoryResponseDto> getMiddleCategoriesByTopCategoryId(Integer topCategoryId) {
		List<MiddleCategory> middleCategories = middleCategoryRepository.findByTopCategoryId(topCategoryId);
		// db에 저장된 객체를 dto로 변환해서 반환
		// middle entity를 dto로 변환
		try {
			TopCategory topCategory = topCategoryRepository.findById(topCategoryId)
				.orElseThrow(() -> new IllegalArgumentException("존재 않는 Top 카테고리 ID입니다."));
			log.info("topCategory : {}", topCategory);

			return middleCategories.stream().map(
				middleCategory -> MiddleCategoryResponseDto.builder()
					.id(middleCategory.getId())
					.middleCategoryName(middleCategory.getCategoryName())
					.build()
			).toList();

		} catch (IllegalArgumentException e) {
			log.error(e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("Unexpected error occurred", e);
			throw new RuntimeException("카테고리 조회 중 오류가 발생했습니다.", e);
		}
	}

	@Override
	public List<BottomCategoryResponseDto> getBottomCategoriesByMiddleCategoryId(Integer middleCategoryId) {
		List<BottomCategory> bottomCategories = bottomCategoryRepository.findByMiddleCategoryId(middleCategoryId);
		// db에 저장된 객체를 dto로 변환해서 반환

		try {
			MiddleCategory middleCategory = middleCategoryRepository.findById(middleCategoryId)
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Middle 카테고리 ID입니다."));
			log.info("middleCategory : {}", middleCategory);
			// bottom db entity를 dto로 변환
			return bottomCategories.stream().map(
				bottomCategory -> BottomCategoryResponseDto.builder()
					.id(bottomCategory.getId())
					.bottomCategoryName(bottomCategory.getCategoryName())
					.build()
			).toList();
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("Unexpected error occurred", e);
			throw new RuntimeException("카테고리 조회 중 오류가 발생했습니다.", e);
		}
	}

}
