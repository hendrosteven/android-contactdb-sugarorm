package brainmatics.com.entity;

import com.orm.SugarRecord;

/**
 * Created by Hendro Steven on 24/04/2018.
 */

public class Contact extends SugarRecord<Contact> {
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String photo;

    public Contact(){}


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
