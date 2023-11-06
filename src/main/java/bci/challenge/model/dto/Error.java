package bci.challenge.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
public class Error {
    private Timestamp timestamp;
    private Integer code;
    private String detail;
}
