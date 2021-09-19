package com.black.driver_service.model;

import javax.persistence.*;

@Entity
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "pick_up")
    private String pickUP;

    @Column(name = "drop_off")
    private String dropOff;

    @Column(name = "amount")
    private String amount;

    @Column(name = "lat_pic")
    private String pickUpLat;

    @Column(name = "lon_pic")
    private String pickUpLng;

    @Column(name = "lat_drop")
    private String dropOffLat;

    @Column(name = "lon_drop")
    private String dropOffLng;

    public Trip() {
    }

    public Trip(long id, String fullName, String phone, String email, String pickUP, String dropOff, String amount,
            String pickUpLat, String pickUpLng, String dropOffLat, String dropOffLng) {
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.pickUP = pickUP;
        this.dropOff = dropOff;
        this.amount = amount;
        this.pickUpLat = pickUpLat;
        this.pickUpLng = pickUpLng;
        this.dropOffLat = dropOffLat;
        this.dropOffLng = dropOffLng;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPickUP() {
        return pickUP;
    }

    public void setPickUP(String pickUP) {
        this.pickUP = pickUP;
    }

    public String getDropOff() {
        return dropOff;
    }

    public void setDropOff(String dropOff) {
        this.dropOff = dropOff;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPickUpLat() {
        return pickUpLat;
    }

    public void setPickUpLat(String pickUpLat) {
        this.pickUpLat = pickUpLat;
    }

    public String getPickUpLng() {
        return pickUpLng;
    }

    public void setPickUpLng(String pickUpLng) {
        this.pickUpLng = pickUpLng;
    }

    public String getDropOffLat() {
        return dropOffLat;
    }

    public void setDropOffLat(String dropOffLat) {
        this.dropOffLat = dropOffLat;
    }

    public String getDropOffLng() {
        return dropOffLng;
    }

    public void setDropOffLng(String dropOffLng) {
        this.dropOffLng = dropOffLng;
    }

    

    
}
