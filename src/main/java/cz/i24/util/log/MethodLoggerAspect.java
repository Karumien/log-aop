/*
 * Copyright (c) 2014 Karumien s.r.o.
 * 
 * The contractor, Karumien s.r.o., does not take any responsibility for defects
 * arising from unauthorized changes to the source code.
 */
package cz.i24.util.log;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Aspect pro logovani volani servisnich metod. Pri vstupu a pri vystupu z metody se zaloguje informace.
 *
 * @author <a href="miroslav.svoboda@karumien.com">Miroslav Svoboda</a>
 * @since 1.0, 13.4.2015 11:15:21
 */
public class MethodLoggerAspect {

    /** Logger */
    private static final Logger LOG = LoggerFactory.getLogger(MethodLoggerAspect.class);

    /**
     * Metoda loguje vstup a vystup z metody.
     *
     * @param pjp
     *            popis proxovane tridy
     * @return <code>Object</code> navratova hodnota volane metody
     * @throws Throwable
     *             uvolneni vyjimky volane metody, pokud nastane (a jeji nasledne zalogovani
     *             {@link MethodLoggerObject#logException(Exception)})
     */
    public Object logMethod(ProceedingJoinPoint pjp) throws Throwable {

        if (LOG.isDebugEnabled()) {

            Method method = ((MethodSignature) pjp.getSignature()).getMethod();
            Object[] args = pjp.getArgs();

            logBefore(method, args);
        }

        // provedeni cilove metody
        Object retVal = pjp.proceed();

        if (LOG.isDebugEnabled()) {

            Method method = ((MethodSignature) pjp.getSignature()).getMethod();
            logAfter(method, retVal);

        }

        return retVal;
    }

    /**
     * Sdilena metoda logovani pred spustenim metody.
     *
     * @param method
     *            log method around
     * @param args
     *            arguments
     */
    public static void logBefore(Method method, Object[] args) {

        // rozhodnuti dle poctu argumentu volane metody
        if (args.length == 0) {

            // zalogovani spusteni metody
            LOG.debug(getMethodFullInfoPath(method) + " executed.");

        } else {

            // zalogovani spusteni metody vcetne parametru
            LOG.debug(getMethodFullInfoPath(method)
                    + " executed with params: "
                    + MethodLoggerObject.logCollectionOfObjects(Arrays.asList(method.getParameterAnnotations()),
                            Arrays.asList(args)));
        }

    }

    /**
     * Sdilena metoda logovani po behu metody.
     *
     * @param method
     *            log method around
     * @param retVal
     *            return value
     */
    public static void logAfter(Method method, Object retVal) {

        LOG.debug(getMethodFullInfoPath(method)
                + " finished with value: "
                + MethodLoggerObject.logObject(method.isAnnotationPresent(AutomaticToString.class) ? MethodLoggerObject.IGNORED_LOG_VALUE
                        : retVal));

    }

    /**
     * Zjistuje nazev tridy a metody, kterou je treba obecne zalogovat. A pridavame informace o zalogovanem uzivateli,
     * pokud existuje.
     *
     * @param m
     *            metoda
     * @return <code>String</code> radek do logu
     */
    public static String getMethodFullInfoPath(Method m) {

        // sestaveni logovaciho radku
        return m.getDeclaringClass().getSimpleName() + "." + m.getName();
    }

    /**
     * Zalogovani vyjimky.
     *
     * @param ex
     *            vyjimka pro zalogovani
     */
    public static void logExceptionStatic(Throwable ex) {
        LOG.error("Not catched exception raised in application", ex);
    }

    /**
     * Metoda zaloguje vzniklou vyjimku.
     *
     * @param ex
     *            vznikla vyjimka
     */
    public void logException(Throwable ex) {
        logExceptionStatic(ex);
    }

    public void setAutoToString(Set<String> set) {
        MethodLoggerObject.AUTO_TO_STRING.addAll(set);
    }

}
