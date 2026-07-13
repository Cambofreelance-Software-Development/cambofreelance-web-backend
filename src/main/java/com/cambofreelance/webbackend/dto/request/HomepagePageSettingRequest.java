package com.cambofreelance.webbackend.dto.request;

import lombok.Data;

@Data
public class HomepagePageSettingRequest {

    // Hero
    private String heroTitle;
    private String heroTitleKh;
    private String heroSubtitle;
    private String heroSubtitleKh;
    private String heroCtaLabel;
    private String heroCtaLabelKh;
    private String heroCtaLink;
    private String heroVideoLabel;
    private String heroVideoLabelKh;
    private String heroVideoUrl;
    private String heroImageUrl;

    // "Empowering businesses worldwide" band + the 3 lifestyle photos
    private String metricsTitle;
    private String metricsTitleKh;
    private String metricsSubtitle;
    private String metricsSubtitleKh;
    private String lifestyleImage1Url;
    private String lifestyleImage2Url;
    private String lifestyleImage3Url;

    // "Get the help you need" (3 cards: live chat, help center, community)
    private String helpTitle;
    private String helpTitleKh;

    private String helpLivechatTitle;
    private String helpLivechatTitleKh;
    private String helpLivechatDescription;
    private String helpLivechatDescriptionKh;
    private String helpLivechatCtaLabel;
    private String helpLivechatCtaLabelKh;
    private String helpLivechatCtaLink;

    private String helpCenterTitle;
    private String helpCenterTitleKh;
    private String helpCenterDescription;
    private String helpCenterDescriptionKh;
    private String helpCenterCtaLabel;
    private String helpCenterCtaLabelKh;
    private String helpCenterCtaLink;

    private String helpCommunityTitle;
    private String helpCommunityTitleKh;
    private String helpCommunityDescription;
    private String helpCommunityDescriptionKh;
    private String helpCommunityCtaLabel;
    private String helpCommunityCtaLabelKh;
    private String helpCommunityCtaLink;

    // "Become a SOPPOS Partner"
    private String partnerTitle;
    private String partnerTitleKh;
    private String partnerDescription;
    private String partnerDescriptionKh;
    private String partnerCtaLabel;
    private String partnerCtaLabelKh;
    private String partnerCtaLink;
}
