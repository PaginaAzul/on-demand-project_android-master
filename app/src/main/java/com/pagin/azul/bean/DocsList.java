package com.pagin.azul.bean;

import java.io.Serializable;
import java.util.List;

public class DocsList implements Serializable {

    private String _id;
    private List<AddressList> addresses;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public List<AddressList> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressList> addresses) {
        this.addresses = addresses;
    }
}
