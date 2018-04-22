package com.mobile.yan.contextawareness;

public class ListItem {
    //announcementTitle
    private String head;
    //title
    private String desc;
    //largeImageUrl
    private String imageUrl;

    //More information
    private String dealUrl;
    private String shortAnnouncementTitle;
    private String smallImageUrl;
    private String mediumImageUrl;
    private String finePrint;
    private String highlightsHtml;
    private String pitchHtml;

    private String initialPrice;
    private String discountPrice;

    public ListItem(String head,
                    String desc,
                    String imageUrl,
                    String dealUrl,
                    String shortAnnouncementTitle,
                    String smallImageUrl,
                    String mediumImageUrl,
                    String finePrint,
                    String highlightsHtml,
                    String pitchHtml,
                    String initialPrice,
                    String discountPrice){
        this.head = head;
        this.desc = desc;
        this.imageUrl = imageUrl;
        this.dealUrl = dealUrl;
        this.shortAnnouncementTitle = shortAnnouncementTitle;
        this.smallImageUrl = smallImageUrl;
        this.mediumImageUrl = mediumImageUrl;
        this.finePrint = finePrint;
        this.highlightsHtml = highlightsHtml;
        this.pitchHtml = pitchHtml;
        this.initialPrice = initialPrice;
        this.discountPrice = discountPrice;
    }

    public String getHead(){
        return head;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDesc() {
        return desc;
    }

    public String getDealUrl() {
        return dealUrl;
    }

    public String getShortAnnouncementTitle() {
        return shortAnnouncementTitle;
    }

    public String getSmallImageUrl() {
        return smallImageUrl;
    }

    public String getMediumImageUrl() {
        return mediumImageUrl;
    }

    public String getFinePrint() {
        return finePrint;
    }

    public String getHighlightsHtml() {
        return highlightsHtml;
    }

    public String getPitchHtml() {
        return pitchHtml;
    }

    public String getInitialPrice() {
        return initialPrice;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }



}
