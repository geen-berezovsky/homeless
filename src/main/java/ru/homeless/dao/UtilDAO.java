package ru.homeless.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import ru.homeless.entities.Worker;
import ru.homeless.util.HibernateUtil;
import ru.homeless.util.Util;

public class UtilDAO {

	public static Logger log = Logger.getLogger(UtilDAO.class);

	@SuppressWarnings("unchecked")
	public static <T> T getElementByCaption(Class<T> clazz, String value) {
		Session session = null;
		List<T> tlist = new ArrayList<T>();
		session = HibernateUtil.getSession();
		try {
			tlist = session.createCriteria(clazz).add(Restrictions.eq("caption", value)).list();
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return tlist.get(0);
	}
	
}
