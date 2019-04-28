package project.com.lms.models;

public class Lectures {

    private String title ,date , module_id ;

    public Lectures(String title){
        this.title = title;
        this.date = date;
        this.module_id =module_id;

    }
    public Lectures(){
    }

    public String getLecTitle() {
        return title;
    }
    public void setLecTitle(String title){ this.title = title;}

    public String getLecDate() {
        return date;
    }
    public void setLecDate(String id){ this.date = date;}

    public String getMduleId() {
        return module_id;
    }
    public void setModuleId(String id){ this.module_id = module_id;}
}
