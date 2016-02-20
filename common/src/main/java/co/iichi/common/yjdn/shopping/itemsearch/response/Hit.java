package co.iichi.common.yjdn.shopping.itemsearch.response;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Hit {
    @XmlElement(name = "Name")
    protected String name;

    @XmlElement(name = "Url")
    protected String url;

    @XmlElement(name = "Price")
    protected String price;

    @XmlElement(name = "ExImage")
    protected ExImage exImage;
}
