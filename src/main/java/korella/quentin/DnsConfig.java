package korella.quentin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import org.apache.commons.configuration2.XMLConfiguration;
import org.json.JSONObject;

public abstract class DnsConfig {
    private XMLConfiguration config = new XMLConfiguration();
    private String hostName;
    private String recordName;
    private JSONObject dnsRecordConfig;

    public DnsConfig(XMLConfiguration config){
        this.config = config;

        this.hostName = config.getString("dnsHostName");
        this.recordName = config.getString("dnsRecordName");
        this.dnsRecordConfig = RecordConfigToJSON();
    }

    public XMLConfiguration GetConfig(){
        return this.config;
    }

    public String GetHostName(){
        return this.hostName;
    }

    public String GetRecordName(){
        return this.recordName;
    }

    public void SetDnsRecordConfig(JSONObject dnsRecordConfig){
        this.dnsRecordConfig = dnsRecordConfig;
    }

    public JSONObject GetDnsRecordConfig(){
        return this.dnsRecordConfig;
    }

    public static String GetIP(String urlString){
        try {
            URL whatismyip = new URL(urlString);
            BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));

            String ip = in.readLine();

            return ip;
        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }

    public abstract String GetDNSRecords() throws Exception;
    public abstract String GetConfiguredDNSRecord() throws Exception;
    public abstract boolean DNSRecordExists() throws Exception;
    public abstract void CreateDNSRecord(HashMap<String, Object> args) throws Exception;
    public abstract void UpdateDNSRecord() throws Exception;
    public abstract JSONObject RecordConfigToJSON();
    public abstract void RefreshDNSRecordConfigFromConfig();
}
