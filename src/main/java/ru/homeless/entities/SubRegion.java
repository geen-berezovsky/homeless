package ru.homeless.entities;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class SubRegion implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
    private Region region;
	private String caption;

	public SubRegion() {

	}

	public SubRegion(String caption, Region region) {
        this.region = region;
        setCaption(caption);
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	public String toString() {
		return caption;
	}

    @ManyToOne(targetEntity = Region.class)
    @JoinColumn(name="region")
    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }
}
