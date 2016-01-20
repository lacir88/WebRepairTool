package com.ksoft.webrepairtool.Beans;

/**
 * Created by Laci on 2016.01.11..
 */
public class FileListEntry {

    String type;
    String name;

    public FileListEntry(String type, String name) {
        super();
        this.type = type;
        this.name = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return type + ", name=" + name;
    }

}
