package com.gapidu.tracking;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.gapidu.context.VisitorContext;
import com.gapidu.identifiers.Identifier;
import com.gapidu.identifiers.IdentifierActions;

public class GpduHandler {

	final static Logger logger = Logger.getLogger(GpduHandler.class);
	private VisitorContext visitor;

	public GpduHandler(VisitorContext visitor) {

		this.visitor = visitor;
		this.storeVisitorIdentifiersInDb();

	}

	public VisitorContext getVisitor() {
		return visitor;
	}

	public void setVisitor(VisitorContext visitor) {
		this.visitor = visitor;
	}

	private void storeVisitorIdentifiersInDb() {

		List<String> sqlArr = new ArrayList<String>();

		try {

			for (String identType : visitor.getParametersMap().keySet()) {

				Identifier idMap = new IdentifierActions().mapToType(identType,
						visitor.getParametersMap().get(identType));

				if (idMap != null) {

					int identTypeId = idMap.getIdentifierTypeId();
					String ident = idMap.getIdentifier();

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
				//DbConnect conn = new DbConnect();
//				conn.multipleuUpdatesSql(sqlQueries);
//				System.out.println("done");
//				conn.close();
			}

		} catch (Exception e) {
			System.out.println(e);
		} 

	}

}
