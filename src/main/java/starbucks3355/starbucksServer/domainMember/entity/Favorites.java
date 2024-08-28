package starbucks3355.starbucksServer.domainMember.entity;

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
public class Favorites {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Favorites;
	@Column(nullable = false)
	private Long address;
	@Column(nullable = false)
	private Long productCode;
	@Column(nullable = false)
	private Boolean isFavorites = false;
}
