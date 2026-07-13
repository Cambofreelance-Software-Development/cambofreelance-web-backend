package com.cambofreelance.webbackend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HomepagePageSettingResponse {

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

    private String metricsTitle;
    private String metricsTitleKh;
    private String metricsSubtitle;
    private String metricsSubtitleKh;
    private String lifestyleImage1Url;
    private String lifestyleImage2Url;
    private String lifestyleImage3Url;

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

    private String partnerTitle;
    private String partnerTitleKh;
    private String partnerDescription;
    private String partnerDescriptionKh;
    private String partnerCtaLabel;
    private String partnerCtaLabelKh;
    private String partnerCtaLink;
}
