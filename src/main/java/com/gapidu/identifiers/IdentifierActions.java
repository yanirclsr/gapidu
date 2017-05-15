package com.gapidu.identifiers;

public class IdentifierActions {

	public Identifier mapToType(String type, String id) {

		int typeId;

		switch (type) {
		case "gvid":
			typeId = IdentifierTypes.GPDU_COOKIE;
			id = id.split(":")[0];
			return new Identifier(typeId, id);

		case "gpdugbl":
			typeId = IdentifierTypes.GPDU_GLOBAL_UUID;
			return new Identifier(typeId, id);

		case "_ga":
			typeId = IdentifierTypes.GUA_COOKIE;
			System.out.println(id);
			id = id.split("\\.")[2] + "." + id.split("\\.")[3];
			return new Identifier(typeId, id);

		case "gclid":
			typeId = IdentifierTypes.ADWORDS_CLICK_ID;
			System.out.println(id);
			return new Identifier(typeId, id);

		case "_mkto_trk":
			typeId = IdentifierTypes.MKTO_COOKIE;
			id = id.split(":")[2];
			return new Identifier(typeId, id);

		case "email":
			typeId = IdentifierTypes.EMAIL;
			return new Identifier(typeId, id);

		case "ajs_anonymous_id":
			typeId = IdentifierTypes.SEGMENT_ANON_ID;
			if (id.length() > 30 && !id.equals("null")) {
				id = id.replaceAll("%22", "").replaceAll("\"", "");
				return new Identifier(typeId, id);
			}

		case "ajs_user_id":
			typeId = IdentifierTypes.SEGMENT_USER_ID;
			if (id.length() > 30 && !id.equals("null")) {
				id = id.replaceAll("%22", "").replaceAll("\"", "");
				return new Identifier(typeId, id);
			}

		case "_bizo_bzid":
			typeId = IdentifierTypes.BIZO_ID;
			if (id.length() > 20 && !id.equals("null")) {
				return new Identifier(typeId, id);
			}

		case "optimizelyEndUserId":
			typeId = IdentifierTypes.OPTIMIZELY_END_USER_ID;
			if (id.length() > 20 && !id.equals("null")) {
				return new Identifier(typeId, id);
			}

		case "__distillery":
			typeId = IdentifierTypes.WISTIA_ID;
			if (id.length() > 30 && !id.equals("null")) {
				return new Identifier(typeId, id);
			}

		default:
			return null;
		}
	}

}
