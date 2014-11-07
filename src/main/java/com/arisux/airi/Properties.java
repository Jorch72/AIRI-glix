package com.arisux.airi;

import com.arisux.airi.lib.ModLib;

public class Properties
{
	public static final String MODID = "AIRI";
	public static final String CHANNEL = "CHANNEL_" + MODID;
	public static final String VERSION = "3.1.0";
	public final String DOMAIN = "airi:";
	public final String PUBLIC_SERVER_ADDRESS = "http://arisux.com";
	public final String LOCAL_SERVER_ADDRESS = "http://localhost:8080";
	public final String SERVER_ADDRESS = ModLib.isDevelopmentEnvironment() ? LOCAL_SERVER_ADDRESS : PUBLIC_SERVER_ADDRESS;
	public final String URL_MODPAGE = SERVER_ADDRESS + "/page/mods/airi";
	public final String URL_LATEST = URL_MODPAGE + "/latest.txt";
	public final String URL_CHANGELOG = URL_MODPAGE + "/changelog.txt";
	public final String URL_RETRIEVE_UUID = SERVER_ADDRESS + "/login/auth-functions.php?function=uuid&user=%s";
}
