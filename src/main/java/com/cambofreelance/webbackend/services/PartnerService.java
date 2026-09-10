package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.PartnerApplicationRequest;
import com.cambofreelance.webbackend.dto.request.PartnerPayoutRequest;
import com.cambofreelance.webbackend.dto.request.PartnerReviewRequest;
import com.cambofreelance.webbackend.dto.response.AdminReferredClientResponse;
import com.cambofreelance.webbackend.dto.response.PartnerAdminDetailResponse;
import com.cambofreelance.webbackend.dto.response.PartnerApplicationResponse;
import com.cambofreelance.webbackend.dto.response.PartnerPayoutResponse;
import com.cambofreelance.webbackend.dto.response.PartnerPortalResponse;
import com.cambofreelance.webbackend.dto.response.PartnerReferredClientDetailResponse;
import java.util.List;
import org.springframework.data.domain.Page;

public interface PartnerService {

    // ── Applicant ───────────────────────────────────────────────────────────

    /** The caller's application, or null if they have never applied. */
    PartnerApplicationResponse getMyApplication(String userId);

    PartnerApplicationResponse submitApplication(String userId, PartnerApplicationRequest request);

    PartnerApplicationResponse updateApplication(String userId, PartnerApplicationRequest request);

    PartnerApplicationResponse withdrawApplication(String userId);

    /** Partner Portal — only for an APPROVED application. */
    PartnerPortalResponse getMyPortal(String userId);

    /** Full detail behind "View details" on a referred-clients row — {@code clientUserId} must
     *  actually be one of {@code partnerId}'s referrals. */
    PartnerReferredClientDetailResponse getReferredClientDetail(String partnerId, String clientUserId);

    // ── Admin ───────────────────────────────────────────────────────────────

    Page<PartnerApplicationResponse> adminList(String appStatus, int page, int size);

    PartnerAdminDetailResponse adminGet(String id);

    PartnerApplicationResponse adminReview(String id, PartnerReviewRequest request, String adminId);

    PartnerPayoutResponse recordPayout(String applicationId, PartnerPayoutRequest request, String adminId);

    List<PartnerPayoutResponse> listPayouts(String applicationId);

    /** Referred clients for the admin "Approved Partners" tab. Pass {@code applicationId} to
     *  scope to one partner's "show all clients" drill-down; omit it for the flat directory
     *  across every APPROVED partner. Search matches client/company name or partner ref/company. */
    Page<AdminReferredClientResponse> adminListAllReferredClients(
        String applicationId, String search, String subStatus, int page, int size);

    /** Same detail as the partner-facing "View details", resolved admin-side from the client alone. */
    PartnerReferredClientDetailResponse adminGetReferredClientDetail(String clientUserId);
}
