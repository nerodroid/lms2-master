package project.com.lms.models;

public class Module {

    private String title ,id ;

    public Module(String title){
        this.title = title;

    }
    public Module(){

    }

    public String getCouseTitle() {
        return title;
    }
    public void setCourseTitle(String title){ this.title = title;}

    public String getCouseId() {
        return id;
    }
    public void setCouseId(String id){ this.id = id;}

}
