package com.codesanook.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SciMeta on 1/16/2016.
 */
public class StringExtensionUtils {

    private  static Log log = LogFactory.getLog(StringExtensionUtils.class);

    public static String format(String pattern, String... args) {

        String regex = "\\{(\\d+)\\}";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(pattern);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            int index = Integer.valueOf(m.group(1));
            m.appendReplacement(sb, args[index]);
            log.debug(String.format("found index %s start %s, end %s", index, m.start(), m.end()));
        }

        return sb.toString();
    }
}
