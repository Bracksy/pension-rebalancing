package ch.immi.pension.persistence;

import java.util.prefs.Preferences;

/**
 * Read/Write data for current user stored in regedit
 */
public class Data {
	private final static String basePath = "rebalance";

	private Preferences mPreferences = Preferences.userRoot().node(basePath); //$NON-NLS-1$

	private static Data msSingleton = new Data();

	public static Data getInstance() {
		return msSingleton;
	}

	private Preferences getPreferences() {
		return mPreferences;
	}

	public void store(final String pKey, final double pValue) {
		getPreferences().putDouble(pKey, pValue);
	}

	public void store(final String pKey, final int pValue) {
		getPreferences().putInt(pKey, pValue);
	}

	public void store(final String pKey, final boolean pValue) {
		getPreferences().putBoolean(pKey, pValue);
	}

	public void store(final String pKey, final String pValue) {
		getPreferences().put(pKey, pValue);
	}

	public double getDouble(final String pKey, final double pDefaultValue) {
		return getPreferences().getDouble(pKey, pDefaultValue);
	}

	public int getInteger(final String pKey, final int pDefaultValue) {
		return getPreferences().getInt(pKey, pDefaultValue);
	}

	public boolean getBool(final String pKey, final boolean pDefaultValue) {
		return getPreferences().getBoolean(pKey, pDefaultValue);
	}

	public String getString(final String pKey, final String pDefaultValue) {
		return getPreferences().get(pKey, pDefaultValue);
	}
}
