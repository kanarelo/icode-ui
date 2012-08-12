package com.icode.view.component;

public class License {

    private String id;
    private String name;
    private String url;

    private License(String id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getShortName() {
        int i = url.indexOf("licenses/"), j = url.indexOf('/', i + 9);
        return (i == -1) ? name : url.substring(i + 9, (j != -1) ? j : url.length()).toUpperCase();
    }

}
