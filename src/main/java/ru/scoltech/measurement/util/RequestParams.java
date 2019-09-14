package ru.scoltech.measurement.util;

import java.util.function.Predicate;

public class RequestParams {
    public static final String GAUGE_ID = "gauge";
    public static final String FROM = "from";
    public static final String TO = "to";
    public static final String SIZE = "size";
    public static final String NUMBER = "number";

    public static final Predicate<String> isGaugeId = RequestParams.GAUGE_ID::equals;
    public static final Predicate<String> isFrom = RequestParams.FROM::equals;
    public static final Predicate<String> isTo = RequestParams.TO::equals;
    public static final Predicate<String> isSize = RequestParams.SIZE::equals;
    public static final Predicate<String> isNumber = RequestParams.NUMBER::equals;
}
