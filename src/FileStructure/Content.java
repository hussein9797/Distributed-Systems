package FileStructure;

import java.io.Serializable;

public class Content implements Serializable {

    private static final long serialVersionUID = 5619314497772762182L;

    private byte [] content;

    public Content() {
    }

    public Content(byte[] content) {
        this.content = content;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
