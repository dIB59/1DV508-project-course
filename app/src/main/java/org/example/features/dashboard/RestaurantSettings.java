package org.example.features.dashboard;

public class RestaurantSettings {
  private String name;
  private String address;
  private String contact;
  private byte[] logoBlob;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddress() {
    return address;
  }

  public void setaddress(String address) {
    this.address = address;
  }

  public byte[] getLogoBlob() {
    return logoBlob;
  }

  public void setLogoBlob(byte[] b) {
    this.logoBlob = b;
  }

  public void setContact( String contact) {
    this.contact = contact;
  }

  public String getContact() {
    return this.contact;
  }
}
