/*
 * Copyright (c) 2014 Karumien s.r.o.
 * 
 * The contractor, Karumien s.r.o., does not take any responsibility for defects
 * arising from unauthorized changes to the source code.
 */

package cz.i24.util.log.transaction;

import java.util.Objects;

import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;
import org.slf4j.MDC;

/**
 * Filtr pro zjisteni stavu pusteno v transakci, vypousti logovani UID. Priklad pouziti:
 * <p>
 * log4j.appender.console.filter.1=cz.i24.util.log.transaction.TransactionIndicatingFilter<br>
 * log4j.appender.console.filter.1.ignorePackage=cz.i24.
 * </p>
 *
 * @author <a href="miroslav.svoboda@karumien.com">Miroslav Svoboda</a>
 * @since 1.0, 13.04.2015 12:16:21
 */
public class TransactionIndicatingFilter extends Filter {

    /** Package pro ignorovani tohoto filtru */
    private String ignorePackage;

    /** Rekurzivni zamek */
    private ThreadLocal<Boolean> recursiveCheck = new ThreadLocal<Boolean>();

    @Override
    public int decide(LoggingEvent loggingEvent) {
        final Boolean isRecursiveCall = this.recursiveCheck.get();

        if (isRecursiveCall == null || !isRecursiveCall) {
            try {
                this.recursiveCheck.set(Boolean.TRUE);

                MDC.remove("xaName");
                MDC.remove("xaStatus");
                MDC.remove("xaIsolation");
                MDC.remove("xaReadOnly");

                TransactionDescription desc = TransactionIndicatingUtil.getTransactionDescription();

                if (desc.getName() != null && this.ignorePackage != null && desc.getName().startsWith(this.ignorePackage)) {
                    return Filter.DENY;
                }

                if (Boolean.TRUE.equals(desc.getActive())) {
                    MDC.put("xaName", desc.getName());
                    MDC.put("xaStatus", Boolean.TRUE.equals(desc.getActive()) ? "+" : "-");
                    MDC.put("xaIsolation", Objects.toString(desc.getIsolation()));
                    MDC.put("xaReadOnly", Boolean.TRUE.equals(desc.getReadOnly()) ? "r" : "w");
                }

            } finally {
                this.recursiveCheck.set(Boolean.FALSE);
            }
        }

        return Filter.NEUTRAL;
    }


    public String getIgnorePackage() {
        return this.ignorePackage;
    }

    public void setIgnorePackage(String ignorePackage) {
        this.ignorePackage = ignorePackage;
    }

}
