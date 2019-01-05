package be.sam.application.Database;

public class Api {


    private int id;
    private String name;
    private String ip;
    private int rack;
    private int slot;
    private int type;
    private String databloc;

    public Api() { }

    public Api(String name, String ip, int rack, int slot, int type, String databloc) {
        this.name = name;
        this.ip = ip;
        this.rack = rack;
        this.slot = slot;
        this.type = type;
        this.databloc = databloc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getRack() {
        return rack;
    }

    public void setRack(int rack) {
        this.rack = rack;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDatabloc() {
        return databloc;
    }

    public void setDatabloc(String databloc) {
        this.databloc = databloc;
    }

    public String toString() {
        String type;
        if(getType() == 0) type = "Condtionnement de comprimés";
        else type = "Asservissement de liquides";
        return "Name : "+this.getName()+"\n"
                +"IP : "+this.getIp()+"\n"
                +"Rack : "+this.getRack()+"\n"
                +"Slot : "+this.getSlot()+"\n"
                +"Type : "+type+"\n"
                +"Databloc : "+this.getDatabloc();
    }





}
