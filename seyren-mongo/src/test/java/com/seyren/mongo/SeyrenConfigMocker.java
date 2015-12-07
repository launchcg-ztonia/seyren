package com.seyren.mongo;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;

import org.mockito.Mockito;

import com.seyren.core.util.config.SeyrenConfig;

public class SeyrenConfigMocker {
	/** */
	public static Properties properties;
	/** */
	public static final String ENVIRONEMNT_VARS = "ENVIRONEMNT_VARS";
	/** */
	public static final String BASE_URL = "BASE_URL";
	public static final String IS_BASE_URL_DEFAULT = "IS_BASE_URL_DEFAULT";
	public static final String MONGO_URL = "MONGO_URL";
	public static final String GRAPHS_ENABLED = "GRAPHS_ENABLED";
	public static final String NO_OF_THREADS = "NO_OF_THREADS";
	public static final String CHECK_EXEC_INDEX = "CHECK_EXEC_INDEX";
	public static final String CHECK_EXEC_INSTANCES = "CHECK_EXEC_INSTANCES";
	public static final String TWILIO_URL = "TWILIO_URL";
	public static final String TWILIO_ACCOUNT_SID = "TWILIO_ACCOUNT_SID";
	public static final String TWILIO_AUTH_TOKEN = "TWILIO_AUTH_TOKEN";
	public static final String OPS_GENIE_TEAMS = "OPS_GENIE_TEAMS";
	public static final String TWILIO_PHONE_NUMBER = "TWILIO_PHONE_NUMBER";
	public static final String HIPCHAT_BASE_URL = "HIPCHAT_BASE_URL";
	public static final String HIP_CHAT_AUTH_TOKEN = "HIP_CHAT_AUTH_TOKEN";
	public static final String HIPCHAT_USERNAME = "HIPCHAT_USERNAME";
	public static final String HUBOT_URL = "HUBOT_URL";
	public static final String FLOWDOCK_EXTERNAL_USERNAME = "FLOWDOCK_EXTERNAL_USERNAME";
	public static final String FLOWDOCK_TAGS = "FLOWDOCK_TAGS";
	public static final String FLOWDOC_EMOJIS = "FLOWDOC_EMOJIS";
	public static final String IRC_CAT_PORT = "IRC_CAT_PORT";
	public static final String PUSHOVER_APP_API_TOKEN = "PUSHOVER_APP_API_TOKEN";
	public static final String SMTP_FROM = "SMTP_FROM";
	public static final String SMTP_USERNAME = "SMTP_USERNAME";
	public static final String SMTP_PASSWORD = "SMTP_PASSWORD";
	public static final String SMTP_HOST = "SMTP_HOST";
	public static final String SMTP_PROTOCOL = "SMTP_PROTOCOL";
	public static final String SMTP_PORT = "SMTP_PORT";
	public static final String SNMP_COMMUNITY = "SNMP_COMMUNITY";
	public static final String SNMP_OID = "SNMP_OID";
	public static final String GRAPHITE_URL = "GRAPHITE_URL";
	public static final String GRAPHITE_USERNAME = "GRAPHITE_USERNAME";
	public static final String GRAPHITE_PASSWORD = "GRAPHITE_PASSWORD";
	public static final String GRAPHITE_SCHEME = "GRAPHITE_SCHEME";
	public static final String GRAPHITE_SSL_PORT = "GRAPHITE_SSL_PORT";
	public static final String GRAPHITE_HOST = "GRAPHITE_HOST";
	public static final String GRAPHITE_PATH = "GRAPHITE_PATH";
	public static final String GRAPHITE_KEY_STORE = "GRAPHITE_KEY_STORE";
	public static final String GRAPHITE_KEY_STORE_PASSWORD = "GRAPHITE_KEY_STORE_PASSWORD";
	public static final String GRAPHITE_TRUST_STORE = "GRAPHITE_TRUST_STORE";
	public static final String GRAPHITE_CARBON_PICKE_PORT = "GRAPHITE_CARBON_PICKE_PORT";
	public static final String GRAPHITE_CARBON_PICKLE_ENABLE = "GRAPHITE_CARBON_PICKLE_ENABLE";
	public static final String GRAPHITE_CONNECTION_REQ_TIMEOUT = "GRAPHITE_CONNECTION_REQ_TIMEOUT";
	public static final String GRAPHITE_TIMEOUT = "GRAPHITE_TIMEOUT";
	public static final String GRAPHITE_SOCKET_TIMEOUT = "GRAPHITE_SOCKET_TIMEOUT";
	public static final String SLACK_TOKEN = "SLACK_TOKEN";
	public static final String SLACK_USERNAME = "SLACK_USERNAME";
	public static final String SLACK_ICON_URL = "SLACK_ICON_URL";
	public static final String SLACK_EMOJIS = "SLACK_EMOJIS";
	public static final String HTTP_NOTIFICATION_URL = "HTTP_NOTIFICATION_URL";
	public static final String EMAIL_TEMPLATE_FILENAME = "EMAIL_TEMPLATE_FILENAME";
	public static final String EMAIL_SUBJECT_TEMPLATE_FILENAME = "EMAIL_SUBJECT_TEMPLATE_FILENAME";
	public static final String VICTOR_OPS_REST_ENDPOINT = "VICTOR_OPS_REST_ENDPOINT";
	public static final String SECURITY_ENABLED = "SECURITY_ENABLED";
	public static final String SCRIPT_PATH = "SCRIPT_PATH";
	public static final String SCRIPT_TYPE = "SCRIPT_TYPE";
	public static final String SCRIPT_RESOURCE_URLS = "SCRIPT_RESOURCE_URLS";
	public static final String ALERTS_TTL = "ALERTS_TTL";
	
	
	/** */
	String currentConfigProfileName = "";
	
	
	public static SeyrenConfig getConfig(String configFileName){
		return createConfig(configFileName);
	}
	
	private static SeyrenConfig createConfig(String configFileName){
		SeyrenConfig config = Mockito.mock(SeyrenConfig.class);
		loadConfigProperties(configFileName);
		Mockito.when(config.getBaseUrl()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.isBaseUrlSetToDefault()).thenReturn(Boolean.parseBoolean(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.IS_BASE_URL_DEFAULT)));
		Mockito.when(config.getMongoUrl()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.MONGO_URL));
		Mockito.when(config.isGraphsEnabled()).thenReturn(Boolean.parseBoolean(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.GRAPHS_ENABLED)));
		Mockito.when(config.getNoOfThreads()).thenReturn(Integer.parseInt(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.NO_OF_THREADS)));
		Mockito.when(config.getCheckExecutorInstanceIndex()).thenReturn(Integer.parseInt(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.CHECK_EXEC_INDEX)));
		Mockito.when(config.getCheckExecutorTotalInstances()).thenReturn(Integer.parseInt(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.CHECK_EXEC_INSTANCES)));
		Mockito.when(config.getTwilioUrl()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.TWILIO_URL));
		Mockito.when(config.getTwilioAccountSid()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.TWILIO_ACCOUNT_SID));
		Mockito.when(config.getTwilioAuthToken()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.TWILIO_AUTH_TOKEN));
		Mockito.when(config.getOpsGenieTeams()).thenReturn(new ArrayList());
		Mockito.when(config.getTwilioPhoneNumber()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.TWILIO_PHONE_NUMBER));
		Mockito.when(config.getHipChatBaseUrl()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.HIPCHAT_BASE_URL));
		Mockito.when(config.getHipChatAuthToken()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.HIP_CHAT_AUTH_TOKEN));
		Mockito.when(config.getHipChatUsername()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.HIPCHAT_USERNAME));
		Mockito.when(config.getHubotUrl()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.HUBOT_URL));
		Mockito.when(config.getFlowdockExternalUsername()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.FLOWDOCK_EXTERNAL_USERNAME));
		Mockito.when(config.getFlowdockTags()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.FLOWDOCK_TAGS));
		Mockito.when(config.getFlowdockEmojis()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.FLOWDOC_EMOJIS));
		Mockito.when(config.getIrcCatPort()).thenReturn(Integer.parseInt(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.IRC_CAT_PORT)));
		Mockito.when(config.getPushoverAppApiToken()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.PUSHOVER_APP_API_TOKEN));
		Mockito.when(config.getSmtpFrom()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.SMTP_FROM));
		Mockito.when(config.getSmtpUsername()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.SMTP_USERNAME));
		Mockito.when(config.getSmtpPassword()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.SMTP_PASSWORD));
		Mockito.when(config.getSmtpHost()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.SMTP_HOST));
		Mockito.when(config.getSmtpProtocol()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.SMTP_PROTOCOL));
		Mockito.when(config.getSmtpPort()).thenReturn(Integer.parseInt(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.SMTP_PORT)));
		Mockito.when(config.getSnmpHost()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.SMTP_HOST));
		Mockito.when(config.getSnmpCommunity()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.SNMP_COMMUNITY));
		Mockito.when(config.getSnmpOID()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.SNMP_OID));
		Mockito.when(config.getGraphiteUrl()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.GRAPHITE_URL));
		Mockito.when(config.getGraphiteUsername()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.GRAPHITE_USERNAME));
		Mockito.when(config.getGraphitePassword()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.GRAPHITE_PASSWORD));
		Mockito.when(config.getGraphiteScheme()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.GRAPHITE_SCHEME));
		Mockito.when(config.getGraphiteSSLPort()).thenReturn(Integer.parseInt(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.GRAPHITE_SSL_PORT)));
		Mockito.when(config.getGraphiteHost()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.GRAPHITE_HOST));
		Mockito.when(config.getGraphitePath()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.GRAPHITE_PATH));
		Mockito.when(config.getGraphiteKeyStore()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.GRAPHITE_KEY_STORE));
		Mockito.when(config.getGraphiteKeyStorePassword()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.GRAPHITE_KEY_STORE_PASSWORD));
		Mockito.when(config.getGraphiteTrustStore()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.GRAPHITE_TRUST_STORE));
		Mockito.when(config.getGraphiteCarbonPicklePort()).thenReturn(Integer.parseInt(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.GRAPHITE_CARBON_PICKE_PORT)));
		Mockito.when(config.getGraphiteCarbonPickleEnable()).thenReturn(Boolean.parseBoolean(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.GRAPHITE_CARBON_PICKLE_ENABLE)));
		Mockito.when(config.getGraphiteConnectionRequestTimeout()).thenReturn(Integer.parseInt(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.GRAPHITE_CONNECTION_REQ_TIMEOUT)));
		Mockito.when(config.getGraphiteConnectTimeout()).thenReturn(Integer.parseInt(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.GRAPHITE_TIMEOUT)));
		Mockito.when(config.getGraphiteSocketTimeout()).thenReturn(Integer.parseInt(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.GRAPHITE_SOCKET_TIMEOUT)));
		Mockito.when(config.getSlackToken()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.SLACK_TOKEN));
		Mockito.when(config.getSlackUsername()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.SLACK_USERNAME));
		Mockito.when(config.getSlackIconUrl()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.SLACK_ICON_URL));
		Mockito.when(config.getSlackEmojis()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.SLACK_EMOJIS));
		Mockito.when(config.getHttpNotificationUrl()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.HTTP_NOTIFICATION_URL));
		Mockito.when(config.getEmailTemplateFileName()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.EMAIL_TEMPLATE_FILENAME));
		Mockito.when(config.getEmailSubjectTemplateFileName()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.EMAIL_SUBJECT_TEMPLATE_FILENAME));
		Mockito.when(config.getVictorOpsRestEndpoint()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.VICTOR_OPS_REST_ENDPOINT));
		Mockito.when(config.isSecurityEnabled()).thenReturn(Boolean.parseBoolean(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.SECURITY_ENABLED)));
		Mockito.when(config.getScriptPath()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.SCRIPT_PATH));
		Mockito.when(config.getScriptType()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.SCRIPT_TYPE));
		Mockito.when(config.getScriptResourceUrls()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.SCRIPT_RESOURCE_URLS));
		Mockito.when(config.getAlertsTTL()).thenReturn(Integer.parseInt(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.ALERTS_TTL)));
		return config;
	}
	
	private static void loadConfigProperties(String fileName){
		SeyrenConfigMocker.properties = new Properties();
		URL url = SeyrenConfigMocker.class.getClassLoader().getResource("com/seyren/mongo/configs/" + fileName + ".properties");
		try {
			SeyrenConfigMocker.properties.load(url.openStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
