package co.iichi.common.yjdn.shopping.itemsearch.response;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Result {
    @XmlElement(name = "Hit")
    protected List<Hit> hitList;
}
