package sdk.POJO;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Fruits {

    @XmlElement(name = "employee")
    private List<Fruit> fruit = null;

    public List<Fruit> getFruit() {
        return fruit;
    }

    public void setFruit(List<Fruit> fruit) {
        this.fruit = fruit;
    }
}
