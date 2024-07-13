package korella.quentin;

import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublisher;
import java.util.Scanner;

import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.json.JSONArray;
import org.json.JSONObject;

public abstract class BaseConfiguration {
    private static XMLConfiguration config;
    private boolean useIPv6;

    public BaseConfiguration(String configPath){
        Configurations configs = new Configurations();

        try
        {
            FileBasedConfigurationBuilder<XMLConfiguration> builder = configs.xmlBuilder(configPath);
            config = builder.getConfiguration();
        }
        catch (ConfigurationException cex)
        {
            System.out.println(cex.getLocalizedMessage());
        }    

        this.useIPv6 = config.getBoolean("commonConfig.useIPv6");
    }

    protected boolean getUseIPv6(){
        return useIPv6;
    }

    protected XMLConfiguration getConfig(){
        return config;
    }

    public String getPublicIP(){
        String url = "https://api4.ipify.org?format=json";

        if(useIPv6){
            url = "https://api6.ipify.org?format=json";
        }

        try {
            Scanner scanner =  new Scanner(new URL(url).openStream(), "UTF-8");

            String ip = scanner.nextLine();

            scanner.close();

            return new JSONObject(ip).getString("ip");
        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }

    public JSONObject httpGetRequest(URI uri, String auth, String contentType){
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Authorization", auth)
                .header("Content-Type", contentType)
                .GET()
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject responseBody = new JSONObject(response.body());

            JSONArray errors = responseBody.getJSONArray("errors");

            if(errors.length() > 0){
                JSONObject error = errors.getJSONObject(0);
                
                throw(new Exception(error.getString("message")));
            }

            return responseBody;
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());

            return null;
        }        
    }

    public JSONObject httpPutRequest(URI uri, String auth, String contentType, JSONObject bodyContent){
        try {
            HttpClient client = HttpClient.newHttpClient();

            BodyPublisher body = HttpRequest.BodyPublishers.ofString(bodyContent.toString());

            HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Authorization", auth)
                .header("Content-Type", contentType)
                .PUT(body)
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject responseBody = new JSONObject(response.body());

            JSONArray errors = responseBody.getJSONArray("errors");

            if(errors.length() > 0){
                JSONObject error = errors.getJSONObject(0);
                
                throw(new Exception(error.getString("message")));
            }

            return responseBody;
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());

            return null;
        }        
    }

    public abstract JSONObject getRecordsByName();
    public abstract void updateRecordsByName() throws Exception;
}
