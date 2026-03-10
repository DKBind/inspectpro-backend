package com.inspectpro.helper;

import lombok.Data;

@Data
public class DatabaseHelper {

    private String search;
    private String searchBy;
    private String sortBy;
    private String sortOrder;
    private int currentPage;
    private int itemsPerPage;

    public DatabaseHelper(String search, int currentPage, int itemsPerPage, String sortBy, String sortOrder) {
        this.search = search;
        this.currentPage = currentPage;
        this.itemsPerPage = itemsPerPage;
        this.sortBy = sortBy;
        this.sortOrder = sortOrder;
    }

    public DatabaseHelper(String search, String searchBy, int currentPage, int itemsPerPage, String sortBy,
            String sortOrder) {
        this.search = search;
        this.searchBy = searchBy;
        this.currentPage = currentPage;
        this.itemsPerPage = itemsPerPage;
        this.sortBy = sortBy;
        this.sortOrder = sortOrder;
    }

    // Copy Constructor for Pagination
    public DatabaseHelper(DatabaseHelper databaseHelper) {
        this.search = databaseHelper.search;
        this.currentPage = 0;
        this.itemsPerPage = 0;
        this.sortBy = databaseHelper.sortBy;
        this.sortOrder = databaseHelper.sortOrder;
    }

    @Override
    public String toString() {
        return "DatabaseHelper [search=" + search + ", searchBy=" + searchBy + ", currentPage=" + currentPage
                + ", itemsPerPage=" + itemsPerPage
                + ", sortBy=" + sortBy + ", sortOrder=" + sortOrder + "]";
    }

}
