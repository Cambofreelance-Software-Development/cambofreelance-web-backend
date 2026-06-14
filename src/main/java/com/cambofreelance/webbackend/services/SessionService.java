package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.response.SessionResponse;
import java.util.List;

public interface SessionService {
    List<SessionResponse> getMyActiveSessions(String userId, String currentDeviceId);
    void revokeMySession(String sessionId, String requestingUserId);
    void revokeAllOtherSessions(String userId, String currentDeviceId);
    List<SessionResponse> adminGetUserSessions(String userId);
    void adminRevokeSession(String sessionId);
    void adminRevokeAllSessions(String userId);
}
