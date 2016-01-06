package ru.homeless.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "DocumentScan")
public class DocumentScan implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
    private DocType doctype;
    private String path;
    private Date uploadingDate;
    private String comments;
    private Client client;
    private Worker worker;

    public DocumentScan(DocType doctype, String path, Date uploadingDate, String comments, Client client, Worker worker) {
        this.doctype = doctype;
        this.path = path;
        this.uploadingDate = uploadingDate;
        this.comments = comments;
        this.client = client;
        this.worker = worker;
    }

    public DocumentScan() {

	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(targetEntity = DocType.class)
	@JoinColumn(name = "doctype")
	public DocType getDoctype() {
		return doctype;
	}

	public void setDoctype(DocType doctype) {
		this.doctype = doctype;
	}

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getUploadingDate() {
        return uploadingDate;
    }

    public void setUploadingDate(Date uploadingDate) {
        this.uploadingDate = uploadingDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @ManyToOne(targetEntity = Client.class)
    @JoinColumn(name = "client")
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @ManyToOne(targetEntity = Worker.class)
    @JoinColumn(name = "worker")
    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }


    public boolean equals(Object o) {
        if (o == null) return false;
        if (! (o instanceof DocumentScan)) {
            return false;
        }
        DocumentScan d = (DocumentScan) o;
        if (new Integer(getId()).equals(new Integer(d.getId()))) {
            return true;
        } else {
            return false;
        }
    }

}
