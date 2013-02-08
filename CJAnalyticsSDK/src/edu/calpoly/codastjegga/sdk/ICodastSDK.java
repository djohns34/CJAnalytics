package edu.calpoly.codastjegga.sdk;


public interface ICodastSDK {
	public static final String APP_NAME = "CODAST-J3GGA-SDK";
		
	public void trackData(EventType type, String metric, Object data);
	public void sendData();
}
