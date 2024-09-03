package starbucks3355.starbucksServer.common.entity;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponseEntity<T> {
	private HttpStatus status;
	private String message;
	private T data;
}
