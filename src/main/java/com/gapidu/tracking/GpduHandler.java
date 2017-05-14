package com.gapidu.tracking;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.gapidu.context.VisitorContext;

public class GpduHandler {

	final static Logger logger = Logger.getLogger(GpduHandler.class);
	private VisitorContext visitor;
	
	public GpduHandler(VisitorContext visitor){

		this.visitor = visitor;
		
		

	}

	private KeyValuePair handleIdentifiers(String type, String id) {

		switch (type) {
		case "gvid":
			// TODO
			type = IdentifierType.GPDU_COOKIE;
			id = id.split(":")[0];
			return new KeyValuePair(type, id);

		case "gpdugbl":
			// TODO
			type = IdentifierType.GPDU_GLOBAL_UUID;
			return new KeyValuePair(type, id);
			
		case "_ga":
			type = IdentifierType.GUA_COOKIE;
			System.out.println(id);
			id = id.split("\\.")[2] + "." + id.split("\\.")[3];
			return new KeyValuePair(type, id);

		case "gclid":
			type = IdentifierType.ADWORDS_CLICK_ID;
			System.out.println(id);
			return new KeyValuePair(type, id);

		case "_mkto_trk":
			type = IdentifierType.MKTO_COOKIE;
			id = id.split(":")[2];
			return new KeyValuePair(type, id);

		case "email":
			type = IdentifierType.EMAIL;
			return new KeyValuePair(type, id.toLowerCase());
			
		case "ajs_anonymous_id":
			type = IdentifierType.SEGMENT_ANON_ID;
			if(id.length() > 30 && !id.equals("null")){
				id = id.replaceAll("%22", "").replaceAll("\"", "");
				return new KeyValuePair(type, id);
			}
			
		case "ajs_user_id":
			type = IdentifierType.SEGMENT_USER_ID;
			if(id.length() > 30 && !id.equals("null")){
				id = id.replaceAll("%22", "").replaceAll("\"", "");
				return new KeyValuePair(type, id);
			}

		case "_bizo_bzid":
			type = IdentifierType.BIZO_ID;
			if(id.length() > 20 && !id.equals("null")){
				return new KeyValuePair(type, id);
			}
			
		case "optimizelyEndUserId":
			type = IdentifierType.OPTIMIZELY_END_USER_ID;
			if(id.length() > 20 && !id.equals("null")){
				return new KeyValuePair(type, id);
			}

		case "__distillery":
			type = IdentifierType.WISTIA_ID;
			if(id.length() > 30 && !id.equals("null")){
				return new KeyValuePair(type, id);
			}

		default:
			return null;
		}
		// utils.cookies.getCookie("_ga").split(".")[2] + "." +
		// utils.cookies.getCookie("_ga").split(".")[3];
	}

	private void recordInGa(VisitorContext visitor) {
		String url = "http://www.google-analytics.com/r/collect" + "?tid=UA-81125672-3&v=1"
		// + "&uip=" + visitor.getIp()
				+ "&t=event" + "&ec=Tracking" + "&ea=" + visitor.getTid() + "&el=" + ""// visitor.getLandingPage()
				+ "&ev=" + visitor.getParametersMap().size() + "&cid=" + visitor.getTid()
				+ (int) (Math.random() * (999999999 - 100000000)) + 100000000;

		Rest.GET(url);
	}

	public VisitorContext getVisitor() {
		return visitor;
	}

	public void setVisitor(VisitorContext visitor) {
		this.visitor = visitor;
	}
	
	private void storeVisitorIdentifiersInDb(){
		
		List<String> sqlArr = new ArrayList<String>();
		
		try {

			for (String identType : visitor.getParametersMap().keySet()) {

				KeyVa idMap = handleIdentifiers(identType, visitor.getParametersMap().get(identType));

				// String identTypeId = getIdentifierTypeId(identType);
				// String ident = visitor.getParametersMap().get(identType);

				if (idMap != null) {

					String identTypeId = idMap.getKey();
					String ident = idMap.getValue();

					String sql = "( (SELECT account_id FROM tags WHERE name = '" + visitor.getTid() + "') , '"
							+ visitor.getTid() + "','" + visitor.getVid() + "','" + identTypeId + "','" + ident + "')";
					System.out.println(sql);
					sqlArr.add(sql);
				}

			}

			if (!sqlArr.isEmpty()) {

				String sqlArrStr = sqlArr.toString().replace("[", "").replace("]", "");

				List<String> sqlQueries = new ArrayList<String>();

				sqlQueries
						.add("INSERT INTO lead_ids_raw (account_id, tag_name , vid , identifier_type, identifier) VALUES "
								+ sqlArrStr + ";");

				if (!visitor.getLandingPage().equals("") && visitor.getLandingPage() != null
						&& visitor.getReferral() != null) {

					sqlQueries
							.add(" INSERT INTO visitors (account_id, vid, ids_count, landing_page, referral, user_agent_id, ip, day_bucket, engaged)"
									+ " VALUES ((SELECT account_id FROM tags WHERE name = '" + visitor.getTid()
									+ "'), '" + visitor.getVid() + "', '" + sqlArr.size() + "', '"
									+ visitor.getLandingPage() + "', '" + visitor.getReferral() + "', '"
									+ visitor.getAgent() + "', '" + visitor.getIp() + "', '" + visitor.getDay() + "', "
									+ visitor.getEngaged() + ");");

				}
				

				// System.out.println(sqlQueries);
				DbConnect conn = new DbConnect();
				conn.multipleuUpdatesSql(sqlQueries);
				System.out.println("done");
				conn.close();
			}

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			recordInGa(visitor);
		}
		
	}
	
}
