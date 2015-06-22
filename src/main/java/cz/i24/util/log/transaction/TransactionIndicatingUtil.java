/*
 * Copyright (c) 2014 Karumien s.r.o.
 * 
 * The contractor, Karumien s.r.o., does not take any responsibility for defects
 * arising from unauthorized changes to the source code.
 */

package cz.i24.util.log.transaction;

import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i24.util.log.MethodLoggerObject;

/**
 * Nastroj pro zjisteni popisu transakce {@link TransactionDescription}.
 *
 * @author <a href="miroslav.svoboda@karumien.com">Miroslav Svoboda</a>
 * @since 1.0, 13.04.2015 12:16:21
 */
public class TransactionIndicatingUtil {

    /** LOG */
    private static final Logger LOG = LoggerFactory.getLogger(MethodLoggerObject.class);

    /** Klic pro ulozeni TransactionManagera */
    private static final String TSM_KEY = "org.springframework.transaction.support.TransactionSynchronizationManager";

    @SuppressWarnings("rawtypes")
    private static final Class[] emptyClasses = null;

    private static final Object[] emptyObjects = null;

    private TransactionIndicatingUtil() {
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static TransactionDescription getTransactionDescription() {

        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        TransactionDescription desc = new TransactionDescription();

        if (contextClassLoader != null) {

            try {

                Class tsmClass = contextClassLoader.loadClass(TSM_KEY);

                Boolean isActive = (Boolean) tsmClass.getMethod("isActualTransactionActive", emptyClasses).invoke(null,
                        emptyObjects);

                if (!Boolean.TRUE.equals(isActive)) {
                    desc.setIsolation(0);
                    desc.setReadOnly(true);
                    desc.setName("no transaction");
                    return desc;
                }

                String transactionName;

                transactionName = (String) tsmClass.getMethod("getCurrentTransactionName", emptyClasses).invoke(null,
                        emptyObjects);


                Integer transactionIsolation = (Integer) tsmClass.getMethod("getCurrentTransactionIsolationLevel",
                        emptyClasses).invoke(null, emptyObjects);

                Boolean isReadOnly = (Boolean) tsmClass.getMethod("isCurrentTransactionReadOnly", emptyClasses).invoke(
                        null, emptyObjects);

                desc.setName(transactionName == null ? "unnamed transaction" : transactionName);
                desc.setActive(isActive);
                desc.setIsolation(transactionIsolation == null ? 0 : transactionIsolation);
                desc.setReadOnly(isReadOnly);

            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
                    | NoSuchMethodException | SecurityException | ClassNotFoundException e) {

                LOG.debug("!spring unavailable ", e);
                desc.setName("!spring unavailable " + e.getMessage());
            }

        } else {
            desc.setName("!ccl unavailable");
        }

        return desc;
    }

}
