package starbucks3355.starbucksServer.domainOrders.vo.request;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequestVo {
	private LocalDateTime orderDate;
	private Integer totalAmount;
	private UUID uuid;
	private String userName;
	private String userPhoneNumber;
	private String userAddress;
}
