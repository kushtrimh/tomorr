/*
 * This file is generated by jOOQ.
 */
package com.kushtrimh.tomorr.dal.tables.records;


import com.kushtrimh.tomorr.dal.tables.QrtzSchedulerState;
import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Entity
@Table(
    name = "qrtz_scheduler_state",
    schema = "public",
    uniqueConstraints = {
        @UniqueConstraint(name = "qrtz_scheduler_state_pkey", columnNames = { "sched_name", "instance_name" })
    }
)
public class QrtzSchedulerStateRecord extends UpdatableRecordImpl<QrtzSchedulerStateRecord> implements Record4<String, String, Long, Long> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.qrtz_scheduler_state.sched_name</code>.
     */
    public void setSchedName(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.qrtz_scheduler_state.sched_name</code>.
     */
    @Column(name = "sched_name", nullable = false, length = 120)
    public String getSchedName() {
        return (String) get(0);
    }

    /**
     * Setter for <code>public.qrtz_scheduler_state.instance_name</code>.
     */
    public void setInstanceName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.qrtz_scheduler_state.instance_name</code>.
     */
    @Column(name = "instance_name", nullable = false, length = 200)
    public String getInstanceName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.qrtz_scheduler_state.last_checkin_time</code>.
     */
    public void setLastCheckinTime(Long value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.qrtz_scheduler_state.last_checkin_time</code>.
     */
    @Column(name = "last_checkin_time", nullable = false, precision = 64)
    public Long getLastCheckinTime() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>public.qrtz_scheduler_state.checkin_interval</code>.
     */
    public void setCheckinInterval(Long value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.qrtz_scheduler_state.checkin_interval</code>.
     */
    @Column(name = "checkin_interval", nullable = false, precision = 64)
    public Long getCheckinInterval() {
        return (Long) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record2<String, String> key() {
        return (Record2) super.key();
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row4<String, String, Long, Long> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    @Override
    public Row4<String, String, Long, Long> valuesRow() {
        return (Row4) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return QrtzSchedulerState.QRTZ_SCHEDULER_STATE.SCHED_NAME;
    }

    @Override
    public Field<String> field2() {
        return QrtzSchedulerState.QRTZ_SCHEDULER_STATE.INSTANCE_NAME;
    }

    @Override
    public Field<Long> field3() {
        return QrtzSchedulerState.QRTZ_SCHEDULER_STATE.LAST_CHECKIN_TIME;
    }

    @Override
    public Field<Long> field4() {
        return QrtzSchedulerState.QRTZ_SCHEDULER_STATE.CHECKIN_INTERVAL;
    }

    @Override
    public String component1() {
        return getSchedName();
    }

    @Override
    public String component2() {
        return getInstanceName();
    }

    @Override
    public Long component3() {
        return getLastCheckinTime();
    }

    @Override
    public Long component4() {
        return getCheckinInterval();
    }

    @Override
    public String value1() {
        return getSchedName();
    }

    @Override
    public String value2() {
        return getInstanceName();
    }

    @Override
    public Long value3() {
        return getLastCheckinTime();
    }

    @Override
    public Long value4() {
        return getCheckinInterval();
    }

    @Override
    public QrtzSchedulerStateRecord value1(String value) {
        setSchedName(value);
        return this;
    }

    @Override
    public QrtzSchedulerStateRecord value2(String value) {
        setInstanceName(value);
        return this;
    }

    @Override
    public QrtzSchedulerStateRecord value3(Long value) {
        setLastCheckinTime(value);
        return this;
    }

    @Override
    public QrtzSchedulerStateRecord value4(Long value) {
        setCheckinInterval(value);
        return this;
    }

    @Override
    public QrtzSchedulerStateRecord values(String value1, String value2, Long value3, Long value4) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached QrtzSchedulerStateRecord
     */
    public QrtzSchedulerStateRecord() {
        super(QrtzSchedulerState.QRTZ_SCHEDULER_STATE);
    }

    /**
     * Create a detached, initialised QrtzSchedulerStateRecord
     */
    public QrtzSchedulerStateRecord(String schedName, String instanceName, Long lastCheckinTime, Long checkinInterval) {
        super(QrtzSchedulerState.QRTZ_SCHEDULER_STATE);

        setSchedName(schedName);
        setInstanceName(instanceName);
        setLastCheckinTime(lastCheckinTime);
        setCheckinInterval(checkinInterval);
    }
}
