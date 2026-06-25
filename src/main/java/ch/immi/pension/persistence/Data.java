package ch.immi.pension.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Read/Write data for current user stored in regedit
 */
public class Data {
	public final static String BASEPATH = "rebalance";
	private final static int UNDEFINED = -1;

	public static class KeyValue {
		String key;
		String value;

		public KeyValue(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}
		public String toString() {
			return value;
		}
	}

	public void store(final String configuration,final String pKey, final double pValue) {
		getPreferences(configuration).putDouble(pKey, pValue);
	}

	public void store(final String configuration,final String pKey, final int pValue) {
		getPreferences(configuration).putInt(pKey, pValue);
	}

	public void store(final String configuration, final String pKey, final String pValue) {
		getPreferences(configuration).put(pKey, pValue);
	}

	public Integer getInteger(final String configuration,final String pKey) {
		int value = getPreferences(configuration).getInt(pKey, UNDEFINED);
		if (value == UNDEFINED) {
			return null;
		}
		return value;
	}

	public Double getDouble(final String configuration,final String pKey) {
		double value = getDouble(configuration, pKey, UNDEFINED);
		if (value == UNDEFINED) {
			return null;
		}
		return value;
	}

	public Double getDouble(final String configuration, final String pKey, double defaultValue) {
		return getPreferences(configuration).getDouble(pKey, defaultValue);
	}

	public String getString(final String configuration, final String pKey) {
		return getPreferences(configuration).get(pKey, null);
	}

	public void deleteConfiguration(String configuration) {
		try {
			getPreferences(configuration).removeNode();
		} catch(BackingStoreException bse) {
			System.out.println("ERROR: " + bse.getMessage());
		}
	}

	public List<KeyValue> getChildrenValues(final String configuration, String valueKey) {
		List<KeyValue> keyValueList = new ArrayList<>();
		try {
			String[] keys = getPreferences(configuration).childrenNames();
			for (int i=0; i<keys.length; i++) {
				String value = getPreferences(configuration).node(keys[i]).get(valueKey, "-");
				keyValueList.add(new KeyValue(keys[i], value));
			}
		} catch(BackingStoreException bse) {
			System.out.println("ERROR: " + bse.getMessage());
		}
		return keyValueList;
	}

	private Preferences getPreferences(final String configuration) {
		if (configuration == null || configuration.isBlank() || configuration.equals(BASEPATH)) {
			return Preferences.userRoot().node(BASEPATH);
		}
		return Preferences.userRoot().node(BASEPATH).node(configuration);
	}
}
