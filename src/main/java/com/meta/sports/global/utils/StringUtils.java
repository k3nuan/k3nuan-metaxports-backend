package com.meta.sports.global.utils;

public class StringUtils {

    public static String toCamelCaseWithSpace(String value) {

        String[] sections = value.toLowerCase().replaceAll("_", " ").split(" ");

        if (sections.length > 1) {
            value = new String();
            for (String valueSection: sections)
                value += (value.length() > 0 ? " " : "") + valueSection.substring(0,1).toUpperCase()
                      + (valueSection.length() > 1 ? valueSection.substring(1) : "");
        }
        else value = sections[0].substring(0,1).toUpperCase() + sections[0].substring(1);

        return value;
    }
}
