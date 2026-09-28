public class student{
    private int id;
    private String name;
    private String regnum;
    private String dept;
    private String address;
    private double mark;

    public student(String name, String regnum,String dept, String address , double mark){
        this.name=name;
        this.regnum=regnum;
        this.dept=dept;
        this.address=address;
        this.mark=mark;
    }
    public String getName(){
        return name;
    }
    public String getregnum(){
        return regnum;
    }
    public String getdept(){
        return dept;
    }
    public String getaddress(){
        return address;
    }
    public double getmark(){
        return mark;
    }
}
