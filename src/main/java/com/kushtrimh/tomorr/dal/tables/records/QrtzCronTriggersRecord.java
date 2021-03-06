/*
 * This file is generated by jOOQ.
 */
package com.kushtrimh.tomorr.dal.tables.records;


import com.kushtrimh.tomorr.dal.tables.QrtzCronTriggers;
import org.jooq.Field;
import org.jooq.Record3;
import org.jooq.Record5;
import org.jooq.Row5;
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
    name = "qrtz_cron_triggers",
    schema = "public",
    uniqueConstraints = {
        @UniqueConstraint(name = "qrtz_cron_triggers_pkey", columnNames = { "sched_name", "trigger_name", "trigger_group" })
    }
)
public class QrtzCronTriggersRecord extends UpdatableRecordImpl<QrtzCronTriggersRecord> implements Record5<String, String, String, String, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.qrtz_cron_triggers.sched_name</code>.
     */
    public void setSchedName(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.qrtz_cron_triggers.sched_name</code>.
     */
    @Column(name = "sched_name", nullable = false, length = 120)
    public String getSchedName() {
        return (String) get(0);
    }

    /**
     * Setter for <code>public.qrtz_cron_triggers.trigger_name</code>.
     */
    public void setTriggerName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.qrtz_cron_triggers.trigger_name</code>.
     */
    @Column(name = "trigger_name", nullable = false, length = 200)
    public String getTriggerName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.qrtz_cron_triggers.trigger_group</code>.
     */
    public void setTriggerGroup(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.qrtz_cron_triggers.trigger_group</code>.
     */
    @Column(name = "trigger_group", nullable = false, length = 200)
    public String getTriggerGroup() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.qrtz_cron_triggers.cron_expression</code>.
     */
    public void setCronExpression(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.qrtz_cron_triggers.cron_expression</code>.
     */
    @Column(name = "cron_expression", nullable = false, length = 120)
    public String getCronExpression() {
        return (String) get(3);
    }

    /**
     * Setter for <code>public.qrtz_cron_triggers.time_zone_id</code>.
     */
    public void setTimeZoneId(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.qrtz_cron_triggers.time_zone_id</code>.
     */
    @Column(name = "time_zone_id", length = 80)
    public String getTimeZoneId() {
        return (String) get(4);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record3<String, String, String> key() {
        return (Record3) super.key();
    }

    // -------------------------------------------------------------------------
    // Record5 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row5<String, String, String, String, String> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    @Override
    public Row5<String, String, String, String, String> valuesRow() {
        return (Row5) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return QrtzCronTriggers.QRTZ_CRON_TRIGGERS.SCHED_NAME;
    }

    @Override
    public Field<String> field2() {
        return QrtzCronTriggers.QRTZ_CRON_TRIGGERS.TRIGGER_NAME;
    }

    @Override
    public Field<String> field3() {
        return QrtzCronTriggers.QRTZ_CRON_TRIGGERS.TRIGGER_GROUP;
    }

    @Override
    public Field<String> field4() {
        return QrtzCronTriggers.QRTZ_CRON_TRIGGERS.CRON_EXPRESSION;
    }

    @Override
    public Field<String> field5() {
        return QrtzCronTriggers.QRTZ_CRON_TRIGGERS.TIME_ZONE_ID;
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
    public String component4() {
        return getCronExpression();
    }

    @Override
    public String component5() {
        return getTimeZoneId();
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
    public String value4() {
        return getCronExpression();
    }

    @Override
    public String value5() {
        return getTimeZoneId();
    }

    @Override
    public QrtzCronTriggersRecord value1(String value) {
        setSchedName(value);
        return this;
    }

    @Override
    public QrtzCronTriggersRecord value2(String value) {
        setTriggerName(value);
        return this;
    }

    @Override
    public QrtzCronTriggersRecord value3(String value) {
        setTriggerGroup(value);
        return this;
    }

    @Override
    public QrtzCronTriggersRecord value4(String value) {
        setCronExpression(value);
        return this;
    }

    @Override
    public QrtzCronTriggersRecord value5(String value) {
        setTimeZoneId(value);
        return this;
    }

    @Override
    public QrtzCronTriggersRecord values(String value1, String value2, String value3, String value4, String value5) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached QrtzCronTriggersRecord
     */
    public QrtzCronTriggersRecord() {
        super(QrtzCronTriggers.QRTZ_CRON_TRIGGERS);
    }

    /**
     * Create a detached, initialised QrtzCronTriggersRecord
     */
    public QrtzCronTriggersRecord(String schedName, String triggerName, String triggerGroup, String cronExpression, String timeZoneId) {
        super(QrtzCronTriggers.QRTZ_CRON_TRIGGERS);

        setSchedName(schedName);
        setTriggerName(triggerName);
        setTriggerGroup(triggerGroup);
        setCronExpression(cronExpression);
        setTimeZoneId(timeZoneId);
    }
}
