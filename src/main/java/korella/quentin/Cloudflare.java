package korella.quentin;

import java.net.URI;
import java.util.HashMap;

import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class Cloudflare extends Base {
    private String apiToken;
    private String dnsZone;
    private String hostName;
    private String recordName;
    private String fullRecordName;

    public Cloudflare(Logger logger) {
        super("./CloudflareConfig.xml", logger);

        XMLConfiguration config = super.getConfig();

        hostName = config.getString("cloudFlareConfig.hostName");
        recordName = config.getString("cloudFlareConfig.recordName");
        apiToken = config.getString("cloudFlareConfig.apiToken");
        dnsZone = config.getString("cloudFlareConfig.dnsZone");
        fullRecordName = recordName + "." + hostName;
    }

    protected String getHostName() {
        return hostName;
    }

    protected String getRecordName() {
        return recordName;
    }

    protected String getApiToken() {
        return apiToken;
    }

    protected String getDnsZone() {
        return dnsZone;
    }

    @Override
    public JSONObject getRecordsByName() {
        try {
            URI uri = new URI("https://api.cloudflare.com/client/v4/zones/" + dnsZone + "/dns_records?name=" + fullRecordName);

            return super.httpGetRequest(uri, "Bearer " + apiToken, "text/plain");
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());

            return null;
        }
    }

    @Override
    public void updateRecordsByName() throws Exception {
        JSONObject records = getRecordsByName();
        int recordsCount = records.getJSONObject("result_info").getInt("total_count");
        String type = super.getUseIPv6() ? "AAAA" : "A";
        String ip = super.getPublicIP();

        if (recordsCount == 0) {
            throw (new Exception("No records with this name."));
        }

        JSONArray recordsArray = records.getJSONArray("result");

        for (int i = 0; i < recordsArray.length(); i++) {
            JSONObject record = recordsArray.getJSONObject(i);
            String recordID = record.getString("id");

            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("content", ip);
            map.put("name", fullRecordName);
            map.put("type", type);
            map.put("id", recordID);

            JSONObject body = new JSONObject(map);

            URI uri = new URI("https://api.cloudflare.com/client/v4/zones/" + dnsZone + "/dns_records/" + recordID);

            super.httpPutRequest(uri, "Bearer " + apiToken, "text/plain", body);
        }
    }
}
