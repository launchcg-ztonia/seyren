package com.seyren.mongo;

import java.util.List;
import java.util.Properties;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.seyren.core.util.config.SeyrenConfig;

public class SeyrenConfigMocker {

	@Mock private static SeyrenConfig config;
	
	private static Properties properties;
	
	public static final String ENVIRONEMNT_VARS = "ENVIRONEMNT_VARS";
	
	public static final String BASE_URL = "BASE_URL";
	
	String currentConfigProfileName = "";
	
	
	
	
	
	public static SeyrenConfig getConfig(String configProfileName){
		
		return config;
	}
	
	private static SeyrenConfig createConfig(){
		Mockito.when(config.getBaseUrl()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.isBaseUrlSetToDefault()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.getMongoUrl()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.isGraphsEnabled()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.getNoOfThreads()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.getCheckExecutorInstanceIndex()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.getCheckExecutorTotalInstances()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.getTwilioUrl()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.getTwilioAccountSid()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.getTwilioAuthToken()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.getOpsGenieTeams()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.twilioPhoneNumber()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.hipChatBaseUrl()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.hipChatAuthToken()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.hipChatUsername()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.hubotUrl()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.flowdockExternalUsername()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.flowdockTags()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.getFlowdockEmojis()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.getIrcCatPort()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.getPushoverAppApiToken()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.getSmtpFrom()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.getSmtpUsername()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.getSmtpPassword()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.getSmtpHost()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.getSmtpProtocol()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.getSmtpPort()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.getSnmpHost()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.getSnmpCommunity()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.getSmtpPort()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.getSnmpOID()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.getGraphiteUrl()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.getGraphiteUsername()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.getGraphitePassword()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.getGraphiteScheme()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.getGraphiteSSLPort()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.getGraphiteHost()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));

		Mockito.when(config.getGraphiteHost()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.getGraphiteHost()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.getGraphiteHost()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.getGraphiteHost()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.getGraphiteHost()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.getGraphiteHost()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.getGraphiteHost()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.getGraphiteHost()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));
		Mockito.when(config.getGraphiteHost()).thenReturn(SeyrenConfigMocker.properties.getProperty(SeyrenConfigMocker.BASE_URL));

	    
	    @JsonIgnore
	    public String getGraphitePath() {
	        return splitBaseUrl(graphiteUrl)[3];
	    }
	    
	    @JsonIgnore
	    public String getGraphiteKeyStore() {
	        return graphiteKeyStore;
	    }
	    
	    @JsonIgnore
	    public String getGraphiteKeyStorePassword() {
	        return graphiteKeyStorePassword;
	    }

	    @JsonIgnore
	    public String getGraphiteTrustStore() {
	        return graphiteTrustStore;
	    }

	    @JsonIgnore
	    public int getGraphiteCarbonPicklePort() {
	        return Integer.valueOf(graphiteCarbonPicklePort);
	    }

	    @JsonProperty("graphiteCarbonPickleEnabled")
	    public boolean getGraphiteCarbonPickleEnable() {
	        return Boolean.valueOf(graphiteCarbonPickleEnable);
	    }
	    
	    @JsonIgnore
	    public int getGraphiteConnectionRequestTimeout() {
	        return graphiteConnectionRequestTimeout;
	    }
	    
	    @JsonIgnore
	    public int getGraphiteConnectTimeout() {
	        return graphiteConnectTimeout;
	    }
	    
	    @JsonIgnore
	    public int getGraphiteSocketTimeout() {
	        return graphiteSocketTimeout;
	    }

	    @JsonIgnore
	    public String getSlackToken() {
	      return slackToken;
	    }

	    @JsonIgnore
	    public String getSlackUsername() {
	      return slackUsername;
	    }

	    @JsonIgnore
	    public String getSlackIconUrl() {
	      return slackIconUrl;
	    }

	    @JsonIgnore
	    public String getSlackEmojis() {
	      return slackEmojis;
	    }

	    @JsonIgnore
	    public String getHttpNotificationUrl() {
	        return httpNotificationUrl;
	    }

	    @JsonIgnore
	    public String getEmailTemplateFileName() { return emailTemplateFileName; }

	    @JsonIgnore
	    public String getEmailSubjectTemplateFileName() { return emailSubjectTemplateFileName; }

	    @JsonIgnore
	    public String getVictorOpsRestEndpoint() {
	        return victorOpsRestAPIEndpoint;
	    }

	    @JsonIgnore
	      public boolean isSecurityEnabled() {
	        return securityEnabled;
	    }

	    @JsonProperty("scriptPath")
	    public String getScriptPath() {
	        return scriptPath;
	    }

	    @JsonIgnore
	    public String getScriptType() {
	        return scriptType;
	    }
	    
	    @JsonProperty("scriptResourceUrls")
	    public String getScriptResourceUrls() {
	        return scriptResourceUrls;
	    }
	    
	    /**
	     * Get the Alerts 'time to live' in seconds
	     * @return The time to live, in seconds, or -1 if it is infinite.
	     */
	    public int getAlertsTTL() {
			return alertsTTL;
		}
	}
}
