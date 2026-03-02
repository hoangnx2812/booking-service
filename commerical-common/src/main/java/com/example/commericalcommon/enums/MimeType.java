package com.example.commericalcommon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public enum MimeType {
    //  image
    JPG("image/jpeg", ".jpg"),
    PNG("image/png", ".png"),
    GIF("image/gif", ".gif"),
    BMP("image/bmp", ".bmp"),
    WEBP("image/webp", ".webp"),

    //  document
    PDF("application/pdf", ".pdf"),
    DOC("application/msword", ".doc"),
    DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document", ".docx"),
    XLS("application/vnd.ms-excel", ".xls"),
    XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", ".xlsx"),

    TXT("text/plain", ".txt"),
    CSV("text/csv", ".csv"),
    JSON("application/json", ".json"),
    XML("application/xml", ".xml"),

    //  audio
    MP3("audio/mpeg", ".mp3"),
    WAV("audio/wav", ".wav"),
    OGG("audio/ogg", ".ogg"),

    //  video
    MP4("video/mp4", ".mp4"),
    AVI("video/x-msvideo", ".avi"),
    MOV("video/quicktime", ".mov"),
    WEBM("video/webm", ".webm"),
    MKV("video/x-matroska", ".mkv"),

    //  archive
    ZIP("application/zip", ".zip"),
    RAR("application/x-rar-compressed", ".rar"),
    SEVEN_Z("application/x-7z-compressed", ".7z");

    String mime;
    String extension;

    public static String getExtensionByMime(String type) {
        if (!StringUtils.hasText(type)) {
            return "";
        }

        String lowerType = type.toLowerCase();

        if (lowerType.contains("xml")) {
            return ".xml";
        }

        MimeType mimeType = MIME_MAP.get(lowerType);
        return mimeType != null ? mimeType.extension : "";
    }

    public static boolean isSupported(String type) {
        if (!StringUtils.hasText(type)) {
            return false;
        }

        String lowerType = type.toLowerCase();
        return lowerType.contains("xml") || MIME_MAP.containsKey(lowerType);
    }

    private static final Map<String, MimeType> MIME_MAP =
            Stream.of(values())
                    .collect(Collectors.toUnmodifiableMap(
                            m -> m.mime.toLowerCase(),
                            m -> m
                    ));

}
