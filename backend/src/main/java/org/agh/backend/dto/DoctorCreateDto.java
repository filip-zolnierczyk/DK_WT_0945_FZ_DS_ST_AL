package org.agh.backend.dto;

public class DoctorCreateDto {
    public String name;
    public String surname;
    public String pesel;
    public String specializationName;
    public String address;

    public DoctorCreateDto(String name, String surname, String pesel, String specialization, String address) {
        this.name = name;
        this.surname = surname;
        this.pesel = pesel;
        this.specializationName = specialization;
        this.address = address;
    }

    public String getName() {
        return name;
    }
    public String getSurname() {
        return surname;
    }
    public String getPesel() {
        return pesel;
    }
    public String getSpecializationName() {
        return specializationName;
    }
    public String getAddress() {
        return address;
    }
}
