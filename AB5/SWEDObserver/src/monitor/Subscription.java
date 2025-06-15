package monitor;

public class Subscription {
    private String subId;
    private String url;
    private int frequency; // in seconds
    private User owner;
    
    
    private String lastEtag;
    private String lastContentHash;

    public Subscription(String subId,
            String url,
            int frequency,
            User owner) {
        this.subId     = subId;
        this.url       = url;
        this.frequency = frequency;
        this.owner     = owner;   
    }

    
    // Getter & Setter für lastEtag und lastContentHash
    public String getLastEtag()       { return lastEtag; }
    public void   setLastEtag(String e){ this.lastEtag = e; }

    public String getLastContentHash(){ return lastContentHash; }
    public void   setLastContentHash(String h){ this.lastContentHash = h; }
    
    // Getter/Setter
    public String getSubId()    { return subId; }
    public String getUrl()      { return url; }
    public int    getFrequency(){ return frequency; }
    public User   getOwner()    { return owner; }
    
}
