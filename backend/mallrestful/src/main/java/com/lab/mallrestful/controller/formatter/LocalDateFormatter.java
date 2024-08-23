package com.lab.mallrestful.controller.formatter;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

// LocalDate 객체를 문자열로 포맷하고, 문자열을 LocalDate 객체로 파싱하기 위함.
public class LocalDateFormatter implements Formatter<LocalDate> {

    @Override
    // LocalDate 객체를 문자열로 포맷
    public String print(LocalDate object, Locale locale) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd", locale).format(object);
    }

    @Override
    // 문자열을 LocalDate 객체로 변환
    public LocalDate parse(String text, Locale locale) throws ParseException {
        return LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd", locale));
    }
}
