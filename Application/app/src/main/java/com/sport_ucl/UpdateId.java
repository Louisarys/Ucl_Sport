package com.sport_ucl;

/**
 * Created by louis on 22/03/18.
 */

public class UpdateId {
    private long id;
    private String name;
    private String table;
    private long max_id;

    public UpdateId(long id, String name, String table, long max_id) {
        this.id = id;
        this.name = name;
        this.table = table;
        this.max_id = max_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public long getMax_id() {
        return max_id;
    }

    public void setMax_id(long max_id) {
        this.max_id = max_id;
    }
}
