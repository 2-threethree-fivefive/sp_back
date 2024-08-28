package starbucks3355.starbucksServer.domainMember.entity;

import java.util.UUID;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EntityListeners(AuditingEntityListener.class)
public class MemberAddresses {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long deliveryId;
	@Column(nullable = false, length = 255)
	private String address;
	@Column(nullable = false)
	private Boolean basicAddress;
	@Column(length = 20)
	private String nickname;
	@Column(nullable = false, length = 30)
	private String receiver;
	@Column(nullable = false, length = 30)
	private String contact;
	@Column(length = 30)
	private String extraContact;
	@Column(length = 255)
	private String deliveryMemo;
	private UUID memberUuid;
}
