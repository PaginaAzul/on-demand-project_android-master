package com.pagin.azul.bean;

import java.io.Serializable;
import java.util.List;

public class AddressData implements Serializable {
    private List<DocsList> docs;
    private String total;
    private String limit;
    private String page;
    private String pages;

    public List<DocsList> getDocs() {
        return docs;
    }

    public void setDocs(List<DocsList> docs) {
        this.docs = docs;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }
}
