package cn.edu.tsinghua.yiyangdefeng;

import java.util.HashMap;
import java.util.Map;

public class Session {
	@SuppressWarnings("rawtypes")
	private Map objectContainer;
	private static Session session;

	// Attention here, DO NOT USE keyword 'new' to create this object.
	// Instead, use getSession method.
	@SuppressWarnings("rawtypes")
	private Session() {
		objectContainer = new HashMap();
	}

	public static Session getSession() {
		if (session == null) {
			session = new Session();
			return session;
		} else {
			return session;
		}
	}

	@SuppressWarnings("unchecked")
	public void put(Object key, Object value) {

		objectContainer.put(key, value);
	}

	public Object get(Object key) {

		return objectContainer.get(key);
	}

	public void cleanUpSession() {
		objectContainer.clear();
	}

	public void remove(Object key) {
		objectContainer.remove(key);
	}
}