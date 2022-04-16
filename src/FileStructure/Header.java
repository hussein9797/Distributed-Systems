package FileStructure;

import java.io.Serializable;

public class Header implements Serializable {



    private static final long serialVersionUID = -3884750658586381809L;


   private String name;

   private Integer owner;

    public Header() {
    }

    public Header(String name) {
        this.name = name;
        this.owner = -1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOwner() {
        return owner;
    }

    public void setOwner(Integer owner) {
        this.owner = owner;
    }
}
