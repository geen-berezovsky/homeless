package ru.homeless.entities;

/**
 * Created by maxim on 05.06.2016.
 */
public class CustDocEntity {

    private int id;
    private Object object;
    private String docNum;
    private String type;
    private String worker;

    private String issueDate;

    public String toString() {
        return "[ className = "+object.getClass().getName()+", docNum = "+docNum+", type = "+ type +", worker = "+worker +", releaseDate = "+issueDate+" ]";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDocNum() {
        return docNum;
    }

    public void setDocNum(String docNum) {
        this.docNum = docNum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWorker() {
        return worker;
    }

    public void setWorker(String worker) {
        this.worker = worker;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
