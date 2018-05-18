package com.sport_ucl;

/**
 * Created by louis on 24/03/18.
 */

public class WebUpdate {
    private String branche;
    private long version;
    private long max_id;

    public WebUpdate(String branche, long version, long max_id){
        this.branche = branche;
        this.version = version;
        this.max_id = max_id;
    }

    public String getBranche() {
        return branche;
    }

    public void setBranche(String branche) {
        this.branche = branche;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public long getMax_id() {
        return max_id;
    }

    public void setMax_id(long max_id) {
        this.max_id = max_id;
    }
}
