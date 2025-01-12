package starbucks3355.starbucksServer.domainMember.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikesProductResponseVo {
	private Long id;
	private String uuid;
	private String productUuid;
	private boolean isLiked;

	@Builder
	public LikesProductResponseVo(Long id, String uuid, String productUuid, boolean isLiked) {
		this.id = id;
		this.uuid = uuid;
		this.productUuid = productUuid;
		this.isLiked = isLiked;
	}
}
