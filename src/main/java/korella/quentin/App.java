package korella.quentin;

import java.io.File;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class App 
{
    public static XMLConfiguration configTest;
    public static Configuration config;

    public static void main( String[] args ) throws Exception
    {
        //"src/main/src/DynDNS.xml"

        Configurations configs = new Configurations();

        try
        {
            FileBasedConfigurationBuilder<XMLConfiguration> builder = configs.xmlBuilder("src/main/src/DynDNS.xml");
            configTest = builder.getConfiguration();
        }
        catch (ConfigurationException cex)
        {
            System.out.println(cex.getLocalizedMessage());
            // Something went wrong
        }    
        




        CloudFlare cloudFlare = new CloudFlare(configTest);

        // System.out.println(cloudFlare.GetDNSRecords());
        // System.out.println(cloudFlare.GetConfiguredDNSRecord());
        // System.out.println(cloudFlare.DNSRecordExists());

        // HashMap<String, Object> map = new HashMap<String, Object>();
        // map.put("content", "2a00:1f:c700:a01:8d4:774a:3e63:4009");
        // map.put("name", "test.biologiker.com");
        // map.put("type", "AAAA");
        // map.put("proxied", true);
        // map.put("tags", new String[] {});
        // map.put("ttl", 1);
        
        // cloudFlare.CreateDNSRecord(map);

        // System.out.println(config.getList("cloudflare"));
    }
}
