package co.iichi.common.yjdn.shopping.itemsearch.response;


import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(
        name = "ResultSet",
        namespace = "urn:yahoo:jp:itemSearch")
public class ResultSet {
    @XmlElement(name = "Result")
    protected Result result;
}
