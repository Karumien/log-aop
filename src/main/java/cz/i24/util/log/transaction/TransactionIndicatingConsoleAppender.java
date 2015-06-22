/*
 * Copyright (c) 2014 Karumien s.r.o.
 * 
 * The contractor, Karumien s.r.o., does not take any responsibility for defects
 * arising from unauthorized changes to the source code.
 */

package cz.i24.util.log.transaction;

import org.apache.log4j.ConsoleAppender;

/**
 * Pridani appenderu do konfigurace logovani, napr.
 * <p>
 * log4j.appender.console=cz.i24.util.log.transaction.TransactionIndicatingConsoleAppender
 * </p>
 *
 * @author <a href="miroslav.svoboda@karumien.com">Miroslav Svoboda</a>
 * @since 1.0, 13.04.2015 12:16:21
 */
public class TransactionIndicatingConsoleAppender extends ConsoleAppender {

    public TransactionIndicatingConsoleAppender() {
        this.addFilter(new TransactionIndicatingFilter());
    }

}
