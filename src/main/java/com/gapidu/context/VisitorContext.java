package com.gapidu.context;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.bitwalker.useragentutils.UserAgent;

public class VisitorContext {

	private HttpServletRequest request;
	private HttpServletResponse response;
	private String tid;
	private String vid;
	private String referral;
	private String landingPage;
	private String ip;
	private String globalCookie;
	private int day;
	private boolean engaged;
	private UserAgent agent;
	private Map<String, String> parametersMap;

	public VisitorContext(HttpServletRequest request, HttpServletResponse response) {

		Cookie[] requestCookies = request.getCookies();

		System.out.println(requestCookies);

		try {

			this.request = request;
			this.response = response;

			this.setAgent(UserAgent.parseUserAgentString(request.getParameter("userAgent")));
			this.setLandingPage(request.getParameter("url"));
			this.setReferral(request.getParameter("referrer"));
			this.setEngaged(landingPage.contains("gpdu="));
			this.setIp(request.getHeader("X-FORWARDED-FOR"), request.getRemoteAddr());
//			this.setDay(Time.TODAY_NUM());

			Map<String, String> idsMap = new HashMap<String, String>();

			String cookiesStr = request.getParameter("cookies");

			// handle cookies
			if (cookiesStr != null) {
				String[] cookies = cookiesStr.split("; ");

				for (String cookie : cookies) {
					System.out.println(cookie);
					try {
						String key = cookie.split("=")[0];
						String val = cookie.split("=")[1];
						idsMap.put(key, val);
					} catch (Exception e) {
						System.out.println("invalid cookie");
					}
				}
			}

			// handle URL params
			if (landingPage != null) {

				try {

					String[] params = landingPage.split("\\?")[1].split("\\&");

					for (String param : params) {
						System.out.println(param);
						String key = param.split("=")[0].toLowerCase();
						String val = param.split("=")[1];
						idsMap.put(key, val);
					}

				} catch (Exception e) {
					// TODO: handle exception
				}

			}

			this.setTid(request.getParameter("tid"));
			this.setVid(request.getParameter("vid"));

			idsMap.put("gvid", this.getVid());
			// idsMap.put("gpdugbl", this.getGlobalCookie());

			if (request.getParameter("email") != null) {
				idsMap.put("email", request.getParameter("email"));
			}

			// this.setParametersMap(request.getParameterMap());
			this.setParametersMap(idsMap);

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

	public UserAgent getAgent() {
		return agent;
	}

	public void setAgent(UserAgent agent) {
		this.agent = agent;
	}

	public Map<String, String> getParametersMap() {
		return parametersMap;
	}

	public void setParametersMap(Map<String, String> map) {
		this.parametersMap = map;
	}

	public String getReferral() {
		return referral;
	}

	public void setReferral(String referral) {
		this.referral = referral;
	}

	public boolean getEngaged() {
		return engaged;
	}

	public void setEngaged(boolean b) {
		this.engaged = b;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String xForwordedFor, String remoteAddr) {

		if (xForwordedFor != null) {
			this.ip = xForwordedFor;
		} else {
			this.ip = remoteAddr;
		}
	}

	public int getDay() {
		return day;
	}

	public void setDay(int i) {
		this.day = i;
	}

	public String getLandingPage() {
		return landingPage;
	}

	public void setLandingPage(String landingPage) {
		if (landingPage != null) {
			this.landingPage = landingPage;
		} else {
			this.landingPage = "";
		}
	}

	public String getGlobalCookie() {

		if (this.globalCookie == null) {
			this.setGlobalCookie();
		}

		return this.globalCookie;
	}

	public void setGlobalCookie() {

		String cookieName = "__gpdugbl";
		String cookieVal = "";
		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			for (Cookie cookie : cookies) {

				if (cookie.getName().equals(cookieName)) {

					System.out.println(cookie.getName() + " > " + cookie.getValue());
					cookieVal = cookie.getValue();
					break;

				}
			}
		}

		if (cookieVal.equals("")) {
			cookieVal = UUID.randomUUID().toString() + "-" + System.currentTimeMillis();

		}

		System.out.println(cookieVal);

		Cookie cookie = new Cookie(cookieName, cookieVal);
		cookie.setPath("/");
		cookie.setDomain("trk.gapidu.com");
		cookie.setMaxAge(60 * 60 * 24 * 365 * 2);
		response.addCookie(cookie);

		this.globalCookie = cookieVal;

	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

}
