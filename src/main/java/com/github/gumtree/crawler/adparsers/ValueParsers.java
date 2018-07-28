package com.github.gumtree.crawler.adparsers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

import static com.github.gumtree.crawler.model.Advertisement.VALUE_NOT_SET;

public class ValueParsers {

    private static final Logger log = LoggerFactory.getLogger(ValueParsers.class);

    public static double parseValue(String value) {
        DecimalFormat df = new DecimalFormat();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        df.setDecimalFormatSymbols(symbols);
        try {
            return df.parse(value).doubleValue();
        } catch (ParseException e) {
            log.error("Failed to parse area from string {}", value);
        }
        return VALUE_NOT_SET;
    }
}
