package korella.quentin;

import java.io.IOException;
import java.net.*;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;
import org.apache.commons.configuration2.XMLConfiguration;
import org.json.JSONArray;
import org.json.JSONObject;

public class CloudFlare extends DnsConfig  {
    String apiToken;
    String dnsZone;
    String baseURI;
    
    public CloudFlare(XMLConfiguration config){
        super(config);

        apiToken = config.getString("cloudFlareApiToken");
        dnsZone = config.getString("dnsZone");

        this.baseURI = "https://api.cloudflare.com/client/v4/";
    }

    private String GetRequest(URI uri) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
            .uri(uri)
            .header("Authorization", "Bearer " + apiToken)
            .GET()
            .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        return response.body();
    }

    @Override
    public String GetDNSRecords() throws Exception{
        return GetRequest(new URI(baseURI + "zones/" + dnsZone + "/dns_records"));
    }

    @Override
    public String GetConfiguredDNSRecord() throws Exception {
        String name = super.GetRecordName() + "."  + super.GetHostName();

        System.out.println(name);

        return GetRequest(new URI(baseURI + "zones/" + dnsZone + "/dns_records?name=" + name));
    }

    @Override
    public boolean DNSRecordExists() throws Exception{
        String DNSRecord = GetConfiguredDNSRecord();

        JSONObject result = new JSONObject(DNSRecord);

        JSONObject resultInfo = result.getJSONObject("result_info");

        return resultInfo.getInt("total_count") > 0;
    }


    @Override
    public void CreateDNSRecord(HashMap<String, Object> args) throws Exception {
        URI uri = new URI("https://api.cloudflare.com/client/v4/zones/fcac9d4d5010e515c1c978104c814081/dns_records");

        HttpClient client = HttpClient.newHttpClient();

        BodyPublisher body = HttpRequest.BodyPublishers.ofString(super.GetDnsRecordConfig().toString());

        HttpRequest request = HttpRequest.newBuilder()
            .uri(uri)
            .header("Authorization", "Bearer " + apiToken)
            .header("Content-Type", "text/plain")
            .POST(body)
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject responseBody = new JSONObject(response.body());

        JSONArray errors = responseBody.getJSONArray("errors");

        if(errors.length() > 0){
            JSONObject error = errors.getJSONObject(0);
            
            throw(new Exception(error.getString("message")));
        }
    }


    @Override
    public void UpdateDNSRecord() throws Exception {
        
    }

    @Override
    public JSONObject RecordConfigToJSON() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("content", "2a00:1f:c700:a01:8d4:774a:3e63:4009");
        map.put("name", "test.biologiker.com");
        map.put("type", "AAAA");
        map.put("proxied", true);
        map.put("tags", new String[] {});
        map.put("ttl", 1);

        return new JSONObject(map);
    }

    @Override
    public void RefreshDNSRecordConfigFromConfig(){
        super.SetDnsRecordConfig(RecordConfigToJSON());
    }
}
