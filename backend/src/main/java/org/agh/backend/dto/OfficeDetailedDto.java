package org.agh.backend.dto;

import org.agh.backend.model.Office;

import java.util.List;

public class OfficeDetailedDto {

    private Long id;
    private String name;
    private String address;
    private List<DutyDto> duties;

    public OfficeDetailedDto(Office office) {
        this.id = office.getId();
        this.name = office.getName();
        this.address = office.getAddress();
        this.duties = office.getDuties().stream()
                .map(DutyDto::new)
                .toList();
    }
}
