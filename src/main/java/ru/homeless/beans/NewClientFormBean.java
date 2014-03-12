package ru.homeless.beans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.log4j.Logger;

@ManagedBean (name = "newclientform")
@SessionScoped
public class NewClientFormBean extends ClientDataBean implements Serializable {

	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(NewClientFormBean.class);

}
