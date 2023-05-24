package client;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "STBXmlResponseInsert")
public class STBXmlResponseInsert {
    private String id;
    private String status;

    public STBXmlResponseInsert() {
    }

    public STBXmlResponseInsert(String id, String status) {
        this.id = id;
        this.status = status;
    }

    @XmlElement(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlElement(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

