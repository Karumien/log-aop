/*
 * Copyright (c) 2014 Karumien s.r.o.
 * 
 * The contractor, Karumien s.r.o., does not take any responsibility for defects
 * arising from unauthorized changes to the source code.
 */

package cz.i24.util.log.transaction;

/**
 * Description of transaction.
 *
 * @author <a href="miroslav.svoboda@karumien.com">Miroslav Svoboda</a>
 * @since 1.0, 13.04.2015 12:16:21
 */
public class TransactionDescription {

    private Boolean readOnly;

    private Boolean active;

    private Integer isolation;

    private String name;

    /**
     * Vraci hodnotu atributu readOnly.
     *
     * @return readOnly
     */
    public Boolean getReadOnly() {
        return this.readOnly;
    }

    /**
     * Nastavuje hodnotu hodnotu atributu readOnly.
     *
     * @param readOnly
     *            nastavuje readOnly
     */
    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    /**
     * Vraci hodnotu atributu active.
     *
     * @return active
     */
    public Boolean getActive() {
        return this.active;
    }

    /**
     * Nastavuje hodnotu hodnotu atributu active.
     *
     * @param active
     *            nastavuje active
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     * Vraci hodnotu atributu isolation.
     *
     * @return isolation
     */
    public Integer getIsolation() {
        return this.isolation;
    }

    /**
     * Nastavuje hodnotu hodnotu atributu isolation.
     *
     * @param isolation
     *            nastavuje isolation
     */
    public void setIsolation(Integer isolation) {
        this.isolation = isolation;
    }

    /**
     * Vraci hodnotu atributu name.
     *
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Nastavuje hodnotu hodnotu atributu name.
     *
     * @param name
     *            nastavuje name
     */
    public void setName(String name) {
        this.name = name;
    }


}
