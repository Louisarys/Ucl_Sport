package com.sport_ucl;

/**
 * Created by louis on 22/03/18.
 */

public class UpdateIdWeb {
    private long version;
    private String name;
    private long max_id;

    public UpdateIdWeb(long version, String name, long max_id){
        this.name = name;
        this.version = version;
        this.max_id = max_id;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getMax_id() {
        return max_id;
    }

    public void setMax_id(long max_id) {
        this.max_id = max_id;
    }
}
