package com.ksoft.webrepairtool.Activities.RemoteDirectoryBrowserPage;

/**
 * Created by Laci on 2016.01.11..
 */
public class FileListEntry {

    int typePicturePath;
    String name;

    public FileListEntry(int type, String name) {
        super();
        this.typePicturePath = type;
        this.name = name;
    }
    public int getTypePicturePath() {
        return typePicturePath;
    }
    public void setTypePicturePath(int typePicturePath) {
        this.typePicturePath = typePicturePath;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return typePicturePath + ", name=" + name;
    }

}
