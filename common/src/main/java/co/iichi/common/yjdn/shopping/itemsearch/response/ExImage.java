package co.iichi.common.yjdn.shopping.itemsearch.response;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class ExImage {
    @XmlElement(name = "Url")
    protected String url;
}
