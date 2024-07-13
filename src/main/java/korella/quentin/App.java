package korella.quentin;

public class App 
{
    public static void main( String[] args ) throws Exception
    {
        CloudFlareConfiguration cloudFlareConfiguration = new CloudFlareConfiguration();

        cloudFlareConfiguration.updateRecordsByName();

        // CloudFlareOld cloudFlare = new CloudFlareOld(configTest);

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
