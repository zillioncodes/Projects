package util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import DBUtils.servlets.User;

public class ServletUtils {

	@SuppressWarnings("unchecked")
	public static void addUserToSession(String userId, User userdata, HttpServletRequest request) {
		Map<String, User> usernameList = null;
		HttpSession session = request.getSession();
		if (!isUserInSession(userId, request)) {
			if (session.getAttribute("usersInSession") != null) {
				usernameList = (Map<String, User>) session.getAttribute("usersInSession");
				if (usernameList.containsKey(userId)) {
					System.err.println("User is already in session. This should not have happened.");
				}
			} else {
				usernameList = new HashMap<String, User>();
			}
		}
		usernameList.put(userId, userdata);
		session.setAttribute("usersInSession", usernameList);
	}

	@SuppressWarnings("unchecked")
	public static boolean isUserInSession(String userId, HttpServletRequest request) {
		boolean result = false;
		Map<String, User> usernameList = null;
		HttpSession session = request.getSession();
		if (session.getAttribute("usersInSession") != null) {
			usernameList = (Map<String, User>) session.getAttribute("usersInSession");
			if (usernameList.containsKey(userId)) {
				result = true;
			}
		}
		return result;
	}
}