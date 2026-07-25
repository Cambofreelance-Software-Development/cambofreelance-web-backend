package com.cambofreelance.webbackend.constants;

import java.util.Set;

public final class MediaType {

    public static final String IMAGE    = "IMAGE";
    public static final String VIDEO    = "VIDEO";
    public static final String DOCUMENT = "DOCUMENT";
    public static final String ARCHIVE  = "ARCHIVE";

    public static final Set<String> IMAGE_EXTENSIONS    = Set.of("jpg", "jpeg", "png", "gif", "webp", "svg", "bmp");
    public static final Set<String> VIDEO_EXTENSIONS    = Set.of("mp4", "avi", "mov", "mkv", "webm", "flv");
    public static final Set<String> DOCUMENT_EXTENSIONS = Set.of("pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt", "csv");
    // App installers/packages — APK/AAB (Android), IPA (iOS), EXE/MSI (Windows), DMG/PKG (macOS)
    public static final Set<String> ARCHIVE_EXTENSIONS  = Set.of("apk", "aab", "ipa", "exe", "msi", "dmg", "pkg", "zip");

    public static String detect(String extension) {
        String ext = extension.toLowerCase();
        if (IMAGE_EXTENSIONS.contains(ext))    return IMAGE;
        if (VIDEO_EXTENSIONS.contains(ext))    return VIDEO;
        if (DOCUMENT_EXTENSIONS.contains(ext)) return DOCUMENT;
        if (ARCHIVE_EXTENSIONS.contains(ext))  return ARCHIVE;
        return null;
    }

    private MediaType() {}
}
