package ru.homeless.entities;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "DocType")
public class DocType {
	private int id;
	private String caption;
	private boolean photoProof;
	private boolean addressProof;
	private boolean birthProof;
	private int audience;

	public DocType() {

	}

	public DocType(String caption, boolean photoProof, boolean addressProof, boolean birthProof, int audience) {
		setCaption(caption);
		setPhotoProof(photoProof);
		setAddressProof(addressProof);
		setAudience(audience);
		setBirthProof(birthProof);
	}
	
	public boolean equals(Object o) {
		if (o == this || o == null || getClass() != o.getClass()) {
			return false;
		}
		DocType d = (DocType) o;
		if (getId() == d.getId()) {
			return true;
		}
		
		return false;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	@Basic
	@Column(columnDefinition = "BIT")
	public boolean isPhotoProof() {
		return photoProof;
	}

	public void setPhotoProof(boolean photoProof) {
		this.photoProof = photoProof;
	}

	@Basic
	@Column(columnDefinition = "BIT")
	public boolean isAddressProof() {
		return addressProof;
	}

	public void setAddressProof(boolean addressProof) {
		this.addressProof = addressProof;
	}

	public int getAudience() {
		return audience;
	}

	public void setAudience(int audience) {
		this.audience = audience;
	}

	@Override
	public String toString() {
		return caption;
	}

	@Basic
	@Column(columnDefinition = "BIT")
	public boolean isBirthProof() {
		return birthProof;
	}

	public void setBirthProof(boolean birthProof) {
		this.birthProof = birthProof;
	}
}
