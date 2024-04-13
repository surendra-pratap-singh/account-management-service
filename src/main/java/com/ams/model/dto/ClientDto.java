package com.ams.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientDto {

    @Schema(description = "Unique ID for client", example = "7029eba7-bfd1-48ca-a436-e745b9868fd1")
    private Long clientId;

    @Schema(description = "First name of the client", example = "Sam")
    private String firstName;

    @Schema(description = "Last name of the client", example = "Dario")
    private String lastName;

    @Schema(description = "Client email", required = true, example = "demo@demo.com")
    private String email;

}
