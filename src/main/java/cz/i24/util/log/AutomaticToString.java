/*
 * Copyright (c) 2014 Karumien s.r.o.
 * 
 * The contractor, Karumien s.r.o., does not take any responsibility for defects
 * arising from unauthorized changes to the source code.
 */
package cz.i24.util.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotace potlacujici tisk hodnoty do logu (napr. heslo, apod.).
 * <p>
 * Priklad pouziti: &lt;li&gt;parametr metody - <code>public List&lt;SpsObjektTO&gt;
 * findSpsObjekt(@MethodLoggerIgnoreParamValue SpsObjektFilter sof)</code> - potlaci se hodnota instance sof
 * &lt;li&gt;metoda - <code>@MethodLoggerIgnoreParamValue public UzivatelTO
 * getLoggedUzivatel()</code> - potlaci se navratova hodnota metody
 *
 * @author <a href="miroslav.svoboda@karumien.com">Miroslav Svoboda</a>
 * @since 1.0, 13.4.2015 11:15:21
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface AutomaticToString {
}
