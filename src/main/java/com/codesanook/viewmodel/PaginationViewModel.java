package com.codesanook.viewmodel;

import com.codesanook.util.MapFormat;

import java.util.Map;

public class PaginationViewModel {

    private int currentPageIndex;
    private int itemsPerPage;
    private int totalPagesCount;
    private Map params;
    private String linkPattern;

    private String createCurrentPageButton(int pageNumber) {
        return String.format("\n<span class=\"current\">%d</span>", pageNumber);
    }

    private String createNewPageButton(int pageNumber, Object label) {
        params.put("pageIndex", pageNumber - 1);
        String link = MapFormat.format(linkPattern, params);
        return String.format("\n<a href=\"%s\">%s</a>", link, label.toString());

    }

    public String render() {

        int page = currentPageIndex + 1;

        int adjacents = 1;
        //other vars
        int prev = page - 1;                                    //previous page is page - 1
        int next = page + 1;                                    //next page is page + 1
        int lastpage = totalPagesCount;
        //lastpage is = total items / items per page, rounded up.
        int lpm1 = lastpage - 1;                                //last page minus 1

	/*
        Now we apply our rules and draw the pagination object.
		We're actually saving the code to a variable in case we want to draw it more than once.
	*/
        String pagination = "";
        pagination += "<div class=\"pagination\" >";

        if (lastpage > 1) {

            //previous button
            if (page > 1) {
                pagination += createNewPageButton(prev, "<< prev");
            } else {
                pagination += "\n<span class=\"disabled\"><< prev</span>";
            }

            //pages
            if (lastpage < 7 + (adjacents * 2)) {    //not enough pages to bother breaking it up
                for (int counter = 1; counter <= lastpage; counter++) {
                    if (counter == page)
                        pagination += createCurrentPageButton(counter);
                    else {
                        pagination += createNewPageButton(counter, counter);
                    }
                }
            } else if (lastpage >= 7 + (adjacents * 2)) {    //enough pages to hide some
                //close to beginning; only hide later pages
                if (page < 1 + (adjacents * 3)) {
                    for (int counter = 1; counter < 4 + (adjacents * 2); counter++) {
                        if (counter == page)
                            pagination += createCurrentPageButton(counter);
                        else
                            pagination += createNewPageButton(counter, counter);
                    }
                    pagination += "\n<span class=\"elipses\">...</span>";
                    pagination += createNewPageButton(lpm1, lpm1);
                    pagination += createNewPageButton(lastpage, lastpage);

                } else if (lastpage - (adjacents * 2) > page && page > (adjacents * 2)) {//in middle; hide some front and some back
                    pagination += createNewPageButton(1, 1);
                    pagination += createNewPageButton(2, 2);
                    pagination += "\n<span class=\"elipses\">...</span>";
                    for (int counter = page - adjacents; counter <= page + adjacents; counter++) {
                        if (counter == page) {
                            pagination += createCurrentPageButton(counter);
                        } else {
                            pagination += createNewPageButton(counter, counter);
                        }
                    }
                    //pagination. = "...";
                    pagination += "\n<span class=\"elipses\">...</span>";
                    pagination += createNewPageButton(lpm1, lpm1);
                    pagination += createNewPageButton(lastpage, lastpage);
                } else { //close to end; only hide early pages

                    pagination += createNewPageButton(1, 1);
                    pagination += createNewPageButton(2, 2);
                    pagination += "\n<span class=\"elipses\">...</span>";
                    for (int counter = lastpage - (1 + (adjacents * 3)); counter <= lastpage; counter++) {
                        if (counter == page)
                            pagination += createCurrentPageButton(counter);
                        else
                            pagination += createNewPageButton(counter, counter);
                    }
                }

            }

            //next button
            if (page < lastpage) {
                pagination += createNewPageButton(next, "next >>");
            } else {
                pagination += "\n<span class=\"disabled\">next »</span>";
            }
        }
        pagination += "\n</div>\n";
        return pagination;
    }


    public int getPageIndex() {
        return currentPageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.currentPageIndex = pageIndex;
    }

    public int getTotalPagesCount() {
        return totalPagesCount;
    }

    public void setTotalPagesCount(int totalPagesCount) {
        this.totalPagesCount = totalPagesCount;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public void setLinkPattern(String linkPattern, Map params) {

        params.put("itemsPerPage", itemsPerPage);
        params.put("sort", "desc");
        this.linkPattern = linkPattern;
        this.params = params;
    }
}
