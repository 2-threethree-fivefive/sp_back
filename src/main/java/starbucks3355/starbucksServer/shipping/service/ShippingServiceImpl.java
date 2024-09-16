package starbucks3355.starbucksServer.shipping.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import starbucks3355.starbucksServer.shipping.dto.request.ShippingAddRequestDto;
import starbucks3355.starbucksServer.shipping.dto.request.ShippingMemberRequestDto;
import starbucks3355.starbucksServer.shipping.dto.response.ShippingAllResponseDto;
import starbucks3355.starbucksServer.shipping.dto.response.ShippingBaseResponseDto;
import starbucks3355.starbucksServer.shipping.dto.response.ShippingListResponseDto;
import starbucks3355.starbucksServer.shipping.entity.ShippingAddress;
import starbucks3355.starbucksServer.shipping.entity.ShippingMemberAddress;
import starbucks3355.starbucksServer.shipping.repository.ShippingMemberRepository;
import starbucks3355.starbucksServer.shipping.repository.ShippingRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShippingServiceImpl implements ShippingService {

	private final ShippingRepository shippingRepository;
	private final ShippingMemberRepository shippingMemberRepository;

	// 배송지 추가 생성
	// @Override
	// public void createAddDelivery(DeliveryAddRequestDto deliveryAddRequestDto) {
	//
	// 	if (deliveryRepository.existsByAddress(deliveryAddRequestDto.getAddress())) {
	// 		throw new IllegalArgumentException("이미 등록된 주소입니다.");
	// 	}
	// 	Delivery delivery = deliveryAddRequestDto.toEntity();
	//
	// 	try {
	// 		deliveryRepository.save(delivery);
	// 	} catch (IllegalArgumentException e) {
	// 		throw e;
	// 	} catch (Exception e) {
	// 		throw new IllegalArgumentException("주소 등록에 실패했습니다.");
	// 	}
	// }

	// 배송지 추가
	@Override
	public void createShipping(String memberUuid, ShippingAddRequestDto shippingAddRequestDto) {

		int shippingAddressCount = shippingRepository.countByUuid(memberUuid);

		if (shippingAddressCount >= 5) {
			throw new IllegalArgumentException("배송지는 최대 5개까지 등록 가능합니다.");
		}

		if (shippingRepository.existsByDetailAddressAndUuid(shippingAddRequestDto.getDetailAddress(), memberUuid)) {
			throw new IllegalArgumentException("이미 등록된 주소입니다.");
		}

		ShippingAddress shippingAddress = shippingAddRequestDto.toEntity(memberUuid, shippingAddRequestDto);

		try {
			shippingRepository.save(shippingAddress);
		} catch (IllegalArgumentException e) {
			throw e;
		} catch (Exception e) {
			throw new IllegalArgumentException("주소 등록에 실패했습니다.");
		}
	}

	// 기본 배송지 변경(수정)
	@Override
	@Transactional
	public void modifyBaseAddress(String uuid, Long memberAddressId) {

		Optional<ShippingMemberAddress> existingBaseAddress = shippingMemberRepository
			.findByUuidAndBaseAddressTrue(
				uuid);

		ShippingMemberAddress shippingMemberAddress = shippingMemberRepository.findById(memberAddressId)
			.orElseThrow(() -> new IllegalArgumentException("해당 배송지가 없습니다."));

		existingBaseAddress.ifPresent(address -> {
			ShippingMemberAddress updateMemberAddress = ShippingMemberAddress.builder()
				.address(address.getAddress())
				.detailAddress(address.getDetailAddress())
				.nickname(address.getNickname())
				.phone1(address.getPhone1())
				.phone2(address.getPhone2())
				.receiver(address.getReceiver())
				.postNumber(address.getPostNumber())
				.uuid(address.getUuid())
				.baseAddress(false)
				.build();

			shippingMemberRepository.save(updateMemberAddress);
		});

		ShippingMemberAddress updateNewMem = ShippingMemberAddress.builder()
			.address(shippingMemberAddress.getAddress())
			.detailAddress(shippingMemberAddress.getDetailAddress())
			.nickname(shippingMemberAddress.getNickname())
			.phone1(shippingMemberAddress.getPhone1())
			.phone2(shippingMemberAddress.getPhone2())
			.receiver(shippingMemberAddress.getReceiver())
			.postNumber(shippingMemberAddress.getPostNumber())
			.uuid(shippingMemberAddress.getUuid())
			.baseAddress(true)
			.build();

		shippingMemberRepository.save(updateNewMem);

		// // 1. ShippingMemberRepository를 통해 현재 사용자의 기본 배송지를 찾는다.
		// ShippingMemberAddress currentMemberAddress = shippingMemberRepository.findByUuidAndDeliveryId(uuid, deliveryId)
		// 	.orElseThrow(() -> new IllegalArgumentException("기존 기본 배송지가 없습니다."));
		//
		// shippingMemberRepository.updateAllBaseAddressToFalse(uuid);
		//
		// // 선택한 주소를 기본 배송지로 설정 (빌더 사용해서)
		// ShippingMemberAddress updateMemberAddress = ShippingMemberAddress.builder()
		// 	.address(currentMemberAddress.getAddress())
		// 	.detailAddress(currentMemberAddress.getDetailAddress())
		// 	.nickname(currentMemberAddress.getNickname())
		// 	.phone1(currentMemberAddress.getPhone1())
		// 	.phone2(currentMemberAddress.getPhone2())
		// 	.receiver(currentMemberAddress.getReceiver())
		// 	.postNumber(currentMemberAddress.getPostNumber())
		// 	.uuid(uuid)
		// 	.baseAddress(true)
		// 	.build();
		//
		// // 새롭게 빌드된 주소 객체를 저장
		// shippingMemberRepository.save(updateMemberAddress);

	}

	// 회원의 주소 등록
	@Override
	@Transactional
	public void enrollAddress(String uuid, ShippingMemberRequestDto shippingMemberRequestDto) {
		log.info("UUID: {}", uuid);
		log.info("Detail Address: {}", shippingMemberRequestDto.getDetailAddress().trim().toLowerCase());

		if (shippingMemberRepository.existsByUuid(uuid)) {
			throw new IllegalArgumentException("이미 등록된 주소입니다.");
		}

		ShippingMemberAddress shippingMemberAddress = shippingMemberRequestDto.toEntity(uuid);
		shippingMemberRepository.save(shippingMemberAddress);

		// baseAddress가 true인 경우 ShippingAddress에 저장
		if (shippingMemberRequestDto.isBaseAddress()) {
			ShippingAddress shippingAddress = ShippingAddress.builder()
				.address(shippingMemberRequestDto.getAddress())
				.detailAddress(shippingMemberRequestDto.getDetailAddress())
				.nickname(shippingMemberRequestDto.getNickName())
				.phone1(shippingMemberRequestDto.getPhone1())
				.phone2(shippingMemberRequestDto.getPhone2())
				.uuid(uuid)
				.postNumber(shippingMemberRequestDto.getPostNumber())
				.baseAddress(true)
				.build();

			shippingRepository.save(shippingAddress);
		}
	}

	//배송지 목록 조회
	@Override
	public List<ShippingListResponseDto> getShippingList(String uuid) {
		List<ShippingAddress> shippingAddressList = shippingRepository.findByUuid(uuid);
		return shippingAddressList.stream()
			.map(
				shippingAddress -> ShippingListResponseDto.builder()
					.deliveryId(shippingAddress.getDeliveryId())
					.receiver(shippingAddress.getReceiver())
					.phone1(shippingAddress.getPhone1())
					.address(shippingAddress.getAddress())
					.detailAddress(shippingAddress.getDetailAddress())
					.build())
			.toList();
	}

	// 모든 배송지 조회
	@Override
	public List<ShippingAllResponseDto> getAllShippingAddress() {
		return shippingRepository.findAll(
				Sort.by(Sort.Direction.ASC, "deliveryId"))
			.stream()
			.map(
				shippingAddress -> ShippingAllResponseDto.builder()
					.deliveryId(shippingAddress.getDeliveryId())
					.nickname(shippingAddress.getNickname())
					.address(shippingAddress.getAddress())
					.detailAddress(shippingAddress.getDetailAddress())
					.build())
			.toList();

	}

	// 기본 배송지 조회
	@Override
	public ShippingBaseResponseDto getBaseShippingAddress(String uuid) {

		ShippingMemberAddress shippingMemberAddress = shippingMemberRepository.findByUuidAndBaseAddressTrue(uuid)
			.orElseThrow(() -> new IllegalArgumentException("기본 배송지가 없습니다. "));

		ShippingAddress shippingAddress = shippingRepository.findBaseDeliveryByUuid(uuid)
			.orElseThrow(() -> new IllegalArgumentException("기본 배송지가 없습니다. "));

		return ShippingBaseResponseDto.builder()
			.deliveryId(shippingAddress.getDeliveryId())
			.address(shippingAddress.getAddress())
			.detailAddress(shippingAddress.getDetailAddress())
			.build();

	}

}
