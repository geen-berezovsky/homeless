package ru.homeless.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.swing.JCheckBox;

import ru.homeless.interfaces.ICheckBoxAction;

@Entity
@Table(name = "ChronicDisease")
public class ChronicDisease implements ICheckBoxAction, Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String caption;
	private Set<Client> clients = new HashSet<Client>();
	
	public ChronicDisease() {
		
	}
	
	public ChronicDisease(String caption) {
		setCaption(caption);
		this.clients = new HashSet<Client>();
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

	@ManyToMany
	@JoinTable(name="link_chronicdisease_client")
	public Set<Client> getClients() {
		return clients;
	}

	public void setClients(Set<Client> clients) {
		this.clients = clients;
	}
	
	public void addClient(Client client) {
		getClients().add(client);
	}
	
	@Override
	public String toString() {
		return caption;
	}

	/*
	 * (non-Javadoc)
	 * @see ru.homeless.pojos.ICheckBoxAction#doAction()
	 * Returns array of objects:
	 * [0] - self caption (String)
	 * [1] - JCheckBox (checked or non checked)
	 * [2] - boolean value to add data
	 */
	/*
	@Override
	public Object[] doAction(Client actualClient, int correctControl) {
		Object[] oa = new Object[3];
		oa[0] = getCaption();
		oa[2] = true;
		JCheckBox jcb = null;
		TableData t = new TableData();
		Set<ChronicDisease> br = t.getClientsChronics(actualClient);
		for (ChronicDisease b : br) {
			if (b.getCaption().equals(getCaption())) {
				jcb = new JCheckBox(getCaption(), true);
				break;
			}
		}
		if (jcb == null) {
			jcb = new JCheckBox(getCaption(), false);
		}
		oa[1] = jcb;
		return oa;
	}
	*/
}
