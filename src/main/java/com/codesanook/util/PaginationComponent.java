package com.codesanook.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PaginationComponent {

    private Log log = LogFactory.getLog(PaginationComponent.class);

    private int pageNumber;
    private int totalPageCount;
    private final int MAX_NUMBER_BUTTON = 9;
    private String[] urlParams;

    private String urlPattern;
    private final String HIDDEN_BUTTON = "<li class='disabled'><a> ... </a></li>";

    private String createButton(String label) {
        return createButton(label, null, false);
    }


    private String createButton(String label, String url) {
        return createButton(label, url, false);
    }


    private String createButton(String label, String url, boolean isActive) {
        String hrefAttribute = "";
        String activeClass = "";
        String disableClass = "";

        if (StringUtils.isEmpty(url)) {
            disableClass = "class='disabled'";
        } else {
            hrefAttribute = String.format("href='%s'", url);
        }

        if (isActive) {
            activeClass = "class='active'";
        }


        return String.format("\t<li %s %s ><a %s> %s </a ></i>\n", activeClass, disableClass, hrefAttribute, label);
    }

    private String getFirstButton() {
        String label = "first";
        if (pageNumber == 1) {
            return createButton(label);
        } else {
            urlParams[0] = String.valueOf(0);
            String url = StringExtensionUtils.format(urlPattern, urlParams);
            return createButton(label, url);
        }
    }

    private String getPreviousButton() {

        String label = "previous";
        if (pageNumber == 1) {
            return createButton(label);
        } else {
            int currentPageIndex = pageNumber - 1;
            int pageIndex = currentPageIndex - 1;
            urlParams[0] = String.valueOf(pageIndex);
            String url = StringExtensionUtils.format(urlPattern, urlParams);
            return createButton(label, url);
        }
    }


    private String getNextButton() {
        String label = "next";
        if (pageNumber == totalPageCount) {
            return createButton(label);
        } else {
            int currentPageIndex = pageNumber - 1;
            int pageIndex = currentPageIndex + 1;
            urlParams[0] = String.valueOf(pageIndex);
            String url = StringExtensionUtils.format(urlPattern, urlParams);
            return createButton(label, url);
        }
    }


    private String getLastButton() {
        String label = "last";
        if (pageNumber == totalPageCount) {
            return createButton(label);
        } else {
            int pageIndex = totalPageCount - 1;
            urlParams[0] = String.valueOf(pageIndex);
            String url = StringExtensionUtils.format(urlPattern, urlParams);
            return createButton(label, url);
        }
    }


    private String getNumberButton(int beginNumber, int endNumber) {
        StringBuffer buttons = new StringBuffer();

        log.debug(String.format("begin %d, end %d", beginNumber, endNumber));

        for (int startNumber = beginNumber; startNumber <= endNumber; startNumber++) {

            String label = String.valueOf(startNumber);
            if (startNumber == pageNumber) {
                buttons.append(createButton(label, null, true));
            } else {
                int startIndex = startNumber - 1;
                urlParams[0] = String.valueOf(startIndex);

                String url = StringExtensionUtils.format(urlPattern, urlParams);
                buttons.append(createButton(label, url));
            }
        }

        return buttons.toString();
    }

    private String getHiddenPrevious(int begin) {
        if (begin != 1) return HIDDEN_BUTTON;
        return "";
    }

    private String getHiddenNext(int end) {

        if (end != totalPageCount) return HIDDEN_BUTTON;
        return "";
    }


    private int[] getBeginEnd() {

        int begin = 1;
        int end = Math.min(totalPageCount,MAX_NUMBER_BUTTON);
        //(8 + 1) >= 9

        //on last page
        if (pageNumber >= totalPageCount) {
            end = totalPageCount; //30 - 9
            begin = totalPageCount - (MAX_NUMBER_BUTTON - 1);
            if (begin < 1) begin = 1;
            return new int[]{begin, end};
        }

        int medianOfMaxNumberButton = (int) Math.ceil((double) MAX_NUMBER_BUTTON / 2);
        if (pageNumber >= medianOfMaxNumberButton) {
            log.debug(String.format("median %s",medianOfMaxNumberButton));
            begin = pageNumber - (medianOfMaxNumberButton - 1);
            end = pageNumber + (medianOfMaxNumberButton - 1);

            if (end > totalPageCount) {
                end = totalPageCount;
                begin = end - (MAX_NUMBER_BUTTON - 1);
            }
            return new int[]{begin, end};
        }

        return new int[]{begin, end};
    }


    public PaginationComponent(int totalPageCount, String urlPattern, int pageIndex, String... params) {
        this.urlPattern = urlPattern;
        this.pageNumber = pageIndex + 1;
        this.totalPageCount = totalPageCount;

        urlParams = new String[params.length + 1];
        for (int i = 1; i < urlParams.length; i++) {
            urlParams[i] = params[i - 1];
        }
    }

    public String render() {
        int[] beginEnd = getBeginEnd();
        int begin = beginEnd[0];
        int end = beginEnd[1];
        String body = getFirstButton() + getPreviousButton() + getHiddenPrevious(begin) +
                getNumberButton(begin, end) +
                getHiddenNext(end) + getNextButton() + getLastButton();

        String pagination = "<ul class='pagination'>\n" + body + "\n</ul>";
        return pagination;
    }
}
