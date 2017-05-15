package com.gapidu.main;

import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

import com.gapidu.CRUDS.LeadIds;

public class Engine {

	public static void main(String[] args){
		
		SessionFactory sf = new Configuration().configure("/hibernate/hibernate.cfg.xml").buildSessionFactory();
		Session session = sf.openSession();
		session.beginTransaction();
		System.out.println("Transaction began");

		LeadIds leadIds = session.get(LeadIds.class, 1);
		System.out.println(leadIds.getIdentifier());
		
	}
	
}
