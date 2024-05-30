package psi_hms.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Data
public class GenericException extends RuntimeException {

        private final int statusCode;
        private final String statusMessage;
        private final String errorMessage;

}
