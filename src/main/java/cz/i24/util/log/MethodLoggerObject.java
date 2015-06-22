/*
 * Copyright (c) 2014 Karumien s.r.o.
 * 
 * The contractor, Karumien s.r.o., does not take any responsibility for defects
 * arising from unauthorized changes to the source code.
 */

package cz.i24.util.log;


import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Tento aspekt loguje kazdou vyjimku {@link #logException(Exception)} a parametry metod vcetne navratovych hodnot -
 * jednoduche tridy pomoci {@link #logObject(Object)}, kolekce pomoci {@link #logCollectionOfObjects(Collection)}.
 *
 * @author <a href="miroslav.svoboda@karumien.com">Miroslav Svoboda</a>
 * @since 1.0, 13.4.2015 11:15:21
 */
public class MethodLoggerObject {

    /** Konstanta pro oddeleni textove reprezentace parametru */
    public static final String PARAMS_SEPARATOR = ", ";

    /** Konstanta pro textovou reprezentaci skryte hodnoty, napr. hesla */
    public static final String IGNORED_LOG_VALUE = "{*****}";

    /** Format pro datum a cas */
    public static final String FORMAT_DDMMYYY_HHMMSS = "dd.MM.yyyy HH:mm:ss";

    /** Seznam anotaci pro vlastni transformaci toString() */
    public static final Set<String> AUTO_TO_STRING;

    static {
        AUTO_TO_STRING = new HashSet<String>();
    }

    /**
     * Metoda zaloguje vzniklou vyjimku.
     *
     * @param ex
     *            vznikla vyjimka
     */
    public void logException(Exception ex) {
        MethodLoggerAspect.logExceptionStatic(ex);
    }

    /**
     * Metoda dle typu objektu vrati informaci o danem objektu.
     *
     * @param obj
     *            objekt k zalogovani
     * @param hideValue
     *            skryt hodnotu v logu
     * @return <code>String</code> retezec s informaci o objektu
     */
    public static String logObject(Object obj, boolean hideValue) {

        // test vstupnich podminek
        if (obj == null) {
            return null;
        }

        // definice nazvu tridy z objektu
        String info = obj.getClass().getSimpleName();

        // potlacime hodnotu do logu
        if (hideValue) {
            return info + "[" + IGNORED_LOG_VALUE + "]";
        }

        // rozhodnuti, zda se jedna o kalendar, kolekci ci pole
        if (obj instanceof Date) {
            return info + "[" + toString(obj) + "]";
        } else if (obj instanceof byte[]) {
            return info + "[ byte[](" + ((byte[]) obj).length + ")" + "]";
        } else if (obj instanceof Collection<?>) {
            return info + "[" + logCollectionOfObjects((Collection<?>) obj) + "]";
        } else if (obj instanceof Object[]) {
            return info + "[" + logCollectionOfObjects(Arrays.asList((Object[]) obj)) + "]";
        }

        // navraceni retezce s informaci o objektu
        return info + "[" + toString(obj) + "]";
    }

    /**
     * Prevadi instanci na textovou reprezentaci.
     *
     * @param object
     *            instance
     * @return String textova reprezentace
     */
    public static String toString(Object object) {

        if (object instanceof Date) {
            DateFormat df = new SimpleDateFormat(FORMAT_DDMMYYY_HHMMSS);
            return df.format(object);
        }

        String stringValue = object.toString();

        if (object instanceof Date
                || stringValue.startsWith(object.getClass().getName() + "@")
                && (isAutoToString(object.getClass().getAnnotations()) || Serializable.class.isAssignableFrom(object
                        .getClass()))) {
            stringValue = ToStringBuilder.reflectionToString(object, ToStringStyle.SHORT_PREFIX_STYLE, false);
        }

        return stringValue;
    }

    /**
     * Zjisteni, zda se jedna o anotovany atribut.
     *
     * @param annotations
     *            seznam anotaci
     * @return boolean <code>true</code> pokud je na zaklade anotaci
     */
    protected static boolean isAutoToString(Annotation[] annotations) {

        if (ArrayUtils.isEmpty(annotations) || AUTO_TO_STRING.isEmpty()) {
            return false;
        }

        boolean isIn = false;

        for (Annotation annotation : annotations) {
            if (AUTO_TO_STRING.contains(annotation.annotationType().getName())) {
                isIn = true;
                break;
            }
        }

        return isIn;
    }

    /**
     * Metoda vytvori textovou informaci o vstupnich argumentech.
     *
     * @param declarations
     *            deklarace anotaci
     * @param objs
     *            kolekce objektu
     * @return <code>String</code> textova informace
     */
    public static String logCollectionOfObjects(List<Annotation[]> declarations, Collection<?> objs) {

        // optimalizace skladani retezcu
        StringBuilder msg = new StringBuilder();

        int i = 0;

        // iterace pres vsechny atributy kolekce
        for (Object o : objs) {

            // doplnime oddelovac, pokud je objektu vice
            if (msg.length() > 0) {
                msg.append(PARAMS_SEPARATOR);
            }

            boolean hideValue = false;

            // zpracujeme deklarace anotaci
            if (declarations != null) {

                List<Annotation> declarationList = Arrays.asList(declarations.get(i++));

                // vsechny anotace daneho parametru
                for (Annotation annotation : declarationList) {

                    // hledame potlaceni hodnoty do logu
                    if (annotation.annotationType().equals(MethodLoggerIgnoreParamValue.class)) {
                        hideValue = true;
                    }
                }

            }

            // zalogujeme hodnotu
            msg.append(logObject(o, hideValue));
        }

        // navratime textovou informaci
        return msg.toString();
    }

    // ========== aliasy

    public static String logCollectionOfObjects(Collection<?> objs) {
        return logCollectionOfObjects(null, objs);
    }

    public static String logObject(Object obj) {
        return logObject(obj, false);
    }

}