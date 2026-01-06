package org.agh.backend.dto;

import org.agh.backend.model.Office;

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
