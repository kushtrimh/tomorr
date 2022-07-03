/*
 * This file is generated by jOOQ.
 */
package com.kushtrimh.tomorr.dal.tables.records;


import com.kushtrimh.tomorr.dal.tables.QrtzSimpleTriggers;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.jooq.Field;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.Row6;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Entity
@Table(
    name = "qrtz_simple_triggers",
    schema = "public",
    uniqueConstraints = {
        @UniqueConstraint(name = "qrtz_simple_triggers_pkey", columnNames = { "sched_name", "trigger_name", "trigger_group" })
    }
)
public class QrtzSimpleTriggersRecord extends UpdatableRecordImpl<QrtzSimpleTriggersRecord> implements Record6<String, String, String, Long, Long, Long> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.qrtz_simple_triggers.sched_name</code>.
     */
    public void setSchedName(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.qrtz_simple_triggers.sched_name</code>.
     */
    @Column(name = "sched_name", nullable = false, length = 120)
    public String getSchedName() {
        return (String) get(0);
    }

    /**
     * Setter for <code>public.qrtz_simple_triggers.trigger_name</code>.
     */
    public void setTriggerName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.qrtz_simple_triggers.trigger_name</code>.
     */
    @Column(name = "trigger_name", nullable = false, length = 200)
    public String getTriggerName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.qrtz_simple_triggers.trigger_group</code>.
     */
    public void setTriggerGroup(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.qrtz_simple_triggers.trigger_group</code>.
     */
    @Column(name = "trigger_group", nullable = false, length = 200)
    public String getTriggerGroup() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.qrtz_simple_triggers.repeat_count</code>.
     */
    public void setRepeatCount(Long value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.qrtz_simple_triggers.repeat_count</code>.
     */
    @Column(name = "repeat_count", nullable = false, precision = 64)
    public Long getRepeatCount() {
        return (Long) get(3);
    }

    /**
     * Setter for <code>public.qrtz_simple_triggers.repeat_interval</code>.
     */
    public void setRepeatInterval(Long value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.qrtz_simple_triggers.repeat_interval</code>.
     */
    @Column(name = "repeat_interval", nullable = false, precision = 64)
    public Long getRepeatInterval() {
        return (Long) get(4);
    }

    /**
     * Setter for <code>public.qrtz_simple_triggers.times_triggered</code>.
     */
    public void setTimesTriggered(Long value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.qrtz_simple_triggers.times_triggered</code>.
     */
    @Column(name = "times_triggered", nullable = false, precision = 64)
    public Long getTimesTriggered() {
        return (Long) get(5);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record3<String, String, String> key() {
        return (Record3) super.key();
    }

    // -------------------------------------------------------------------------
    // Record6 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row6<String, String, String, Long, Long, Long> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    @Override
    public Row6<String, String, String, Long, Long, Long> valuesRow() {
        return (Row6) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return QrtzSimpleTriggers.QRTZ_SIMPLE_TRIGGERS.SCHED_NAME;
    }

    @Override
    public Field<String> field2() {
        return QrtzSimpleTriggers.QRTZ_SIMPLE_TRIGGERS.TRIGGER_NAME;
    }

    @Override
    public Field<String> field3() {
        return QrtzSimpleTriggers.QRTZ_SIMPLE_TRIGGERS.TRIGGER_GROUP;
    }

    @Override
    public Field<Long> field4() {
        return QrtzSimpleTriggers.QRTZ_SIMPLE_TRIGGERS.REPEAT_COUNT;
    }

    @Override
    public Field<Long> field5() {
        return QrtzSimpleTriggers.QRTZ_SIMPLE_TRIGGERS.REPEAT_INTERVAL;
    }

    @Override
    public Field<Long> field6() {
        return QrtzSimpleTriggers.QRTZ_SIMPLE_TRIGGERS.TIMES_TRIGGERED;
    }

    @Override
    public String component1() {
        return getSchedName();
    }

    @Override
    public String component2() {
        return getTriggerName();
    }

    @Override
    public String component3() {
        return getTriggerGroup();
    }

    @Override
    public Long component4() {
        return getRepeatCount();
    }

    @Override
    public Long component5() {
        return getRepeatInterval();
    }

    @Override
    public Long component6() {
        return getTimesTriggered();
    }

    @Override
    public String value1() {
        return getSchedName();
    }

    @Override
    public String value2() {
        return getTriggerName();
    }

    @Override
    public String value3() {
        return getTriggerGroup();
    }

    @Override
    public Long value4() {
        return getRepeatCount();
    }

    @Override
    public Long value5() {
        return getRepeatInterval();
    }

    @Override
    public Long value6() {
        return getTimesTriggered();
    }

    @Override
    public QrtzSimpleTriggersRecord value1(String value) {
        setSchedName(value);
        return this;
    }

    @Override
    public QrtzSimpleTriggersRecord value2(String value) {
        setTriggerName(value);
        return this;
    }

    @Override
    public QrtzSimpleTriggersRecord value3(String value) {
        setTriggerGroup(value);
        return this;
    }

    @Override
    public QrtzSimpleTriggersRecord value4(Long value) {
        setRepeatCount(value);
        return this;
    }

    @Override
    public QrtzSimpleTriggersRecord value5(Long value) {
        setRepeatInterval(value);
        return this;
    }

    @Override
    public QrtzSimpleTriggersRecord value6(Long value) {
        setTimesTriggered(value);
        return this;
    }

    @Override
    public QrtzSimpleTriggersRecord values(String value1, String value2, String value3, Long value4, Long value5, Long value6) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached QrtzSimpleTriggersRecord
     */
    public QrtzSimpleTriggersRecord() {
        super(QrtzSimpleTriggers.QRTZ_SIMPLE_TRIGGERS);
    }

    /**
     * Create a detached, initialised QrtzSimpleTriggersRecord
     */
    public QrtzSimpleTriggersRecord(String schedName, String triggerName, String triggerGroup, Long repeatCount, Long repeatInterval, Long timesTriggered) {
        super(QrtzSimpleTriggers.QRTZ_SIMPLE_TRIGGERS);

        setSchedName(schedName);
        setTriggerName(triggerName);
        setTriggerGroup(triggerGroup);
        setRepeatCount(repeatCount);
        setRepeatInterval(repeatInterval);
        setTimesTriggered(timesTriggered);
    }
}
