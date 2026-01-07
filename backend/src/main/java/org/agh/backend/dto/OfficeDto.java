package org.agh.backend.dto;

import lombok.Setter;
import org.agh.backend.model.Office;

import lombok.Getter;

@Getter
public class OfficeDto {
    private Long id;
    private String name;
    private String address;
    private String description;

    public OfficeDto(Office office) {
        this.id = office.getId();
        this.name = office.getName();
        this.address = office.getAddress();
        this.description = office.getDescription();
    }
}
