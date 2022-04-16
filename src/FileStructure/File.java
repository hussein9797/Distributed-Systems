package FileStructure;

import javax.xml.namespace.QName;
import java.io.Serializable;

public class File implements Serializable {

    private Header header =null;

    private Content content=null;


    public File() {
    }

    public File(String name) {
        this.header = new Header(name);
        this.content = new Content();
    }

    public File(String name, byte [] content) {
        this.header = new Header(name);
        this.content= new Content(content);

    }


    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }
}
