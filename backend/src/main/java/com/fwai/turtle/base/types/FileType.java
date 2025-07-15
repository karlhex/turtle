package com.fwai.turtle.base.types;

public enum FileType {
    DOCUMENT("文档"),
    IMAGE("图片"),
    VIDEO("视频"),
    AUDIO("音频"),
    OTHER("其他");

    private final String description;

    FileType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static FileType get(String name) {
        for (FileType e : values()) {
            if (e.name().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }
}
