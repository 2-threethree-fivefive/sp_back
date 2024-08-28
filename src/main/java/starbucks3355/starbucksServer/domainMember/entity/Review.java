package starbucks3355.starbucksServer.domainMember.entity;

import java.time.LocalDateTime;
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
public class Review {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long deliveryId;
	@Column(nullable = false, length = 255)
	private String reviewText;
	@Column(nullable = false)
	private Integer reviewScore = 0;
	@Column(nullable = false)
	private LocalDateTime reviewDate;
	@Column(nullable = false)
	private UUID memberUuid;
	@Column(nullable = false)
	private Long productCode;
}
