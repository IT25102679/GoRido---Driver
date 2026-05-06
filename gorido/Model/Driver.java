package com.example.gorido.Model;
import jakarta.mail.Multipart;
import jakarta.persistence.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "driver")
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    private String nic_number;
    private String license_number;
    private LocalDate license_exp_date;
    private int experience;
    private String license_image;
    private String nic_image;
    private LocalDate registered_date;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status statusId;

    @ManyToOne
    @JoinColumn(name = "active_id")
    private Active activeId;

    @ManyToOne
    @JoinColumn(name = "district_id")
    private District district;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public User getUserId() {
        return userId;
    }

    public void setNic_number(String nic_number) {
        this.nic_number = nic_number;
    }

    public String getNic_number() {
        return nic_number;
    }

    public void setLicense_number(String license_number) {
        this.license_number = license_number;
    }

    public String getLicense_number() {
        return license_number;
    }

    public void setLicense_exp_date(LocalDate license_exp_date) {
        this.license_exp_date = license_exp_date;
    }

    public LocalDate getLicense_exp_date() {
        return license_exp_date;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getExperience() {
        return experience;
    }

    public void setLicense_image(String license_image) {
        this.license_image = license_image;
    }

    public String getLicense_image() {
        return license_image;
    }

    public void setNic_image(String nic_image) {
        this.nic_image = nic_image;
    }

    public String getNic_image() {
        return nic_image;
    }

    public void setRegistered_date(LocalDate registered_date) {
        this.registered_date = registered_date;
    }

    public LocalDate getRegistered_date() {
        return registered_date;
    }

    public void setStatusId(Status statusId) {
        this.statusId = statusId;
    }

    public Status getStatusId() {
        return statusId;
    }

    public void setActiveId(Active activeId) {
        this.activeId = activeId;
    }

    public Active getActiveId() {
        return activeId;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public District getDistrict() {
        return district;
    }

    @OneToMany(mappedBy = "driverId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vehicle> vehicles;

    public List<Vehicle> getVehicles() {
        return vehicles;
    }
}
