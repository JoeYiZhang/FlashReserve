package org.zuyi.flashreserve.VO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginVO {
    @NotBlank
    private String username;

    private String token;
}
