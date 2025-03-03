package sdk.POJO;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "example")
@XmlAccessorType(XmlAccessType.FIELD)
public class Example {
    private String name;
    private int age;
    private Address address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Example(String name, int age, Address address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Address {
        private String street;
        private String city;
        private String state;
        private int zip;

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public int getZip() {
            return zip;
        }

        public void setZip(int zip) {
            this.zip = zip;
        }

        public Address(String street, String city, String state, int zip) {
            this.street = street;
            this.city = city;
            this.state = state;
            this.zip = zip;
        }
    }
}
