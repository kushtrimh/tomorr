/*
 * This file is generated by jOOQ.
 */
package com.kushtrimh.tomorr.dal.tables.records;


import com.kushtrimh.tomorr.dal.tables.QrtzBlobTriggers;
import org.jooq.Field;
import org.jooq.Record3;
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
    name = "qrtz_blob_triggers",
    schema = "public",
    uniqueConstraints = {
        @UniqueConstraint(name = "qrtz_blob_triggers_pkey", columnNames = { "sched_name", "trigger_name", "trigger_group" })
    }
)
public class QrtzBlobTriggersRecord extends UpdatableRecordImpl<QrtzBlobTriggersRecord> implements Record4<String, String, String, byte[]> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.qrtz_blob_triggers.sched_name</code>.
     */
    public void setSchedName(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.qrtz_blob_triggers.sched_name</code>.
     */
    @Column(name = "sched_name", nullable = false, length = 120)
    public String getSchedName() {
        return (String) get(0);
    }

    /**
     * Setter for <code>public.qrtz_blob_triggers.trigger_name</code>.
     */
    public void setTriggerName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.qrtz_blob_triggers.trigger_name</code>.
     */
    @Column(name = "trigger_name", nullable = false, length = 200)
    public String getTriggerName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.qrtz_blob_triggers.trigger_group</code>.
     */
    public void setTriggerGroup(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.qrtz_blob_triggers.trigger_group</code>.
     */
    @Column(name = "trigger_group", nullable = false, length = 200)
    public String getTriggerGroup() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.qrtz_blob_triggers.blob_data</code>.
     */
    public void setBlobData(byte[] value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.qrtz_blob_triggers.blob_data</code>.
     */
    @Column(name = "blob_data")
    public byte[] getBlobData() {
        return (byte[]) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record3<String, String, String> key() {
        return (Record3) super.key();
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row4<String, String, String, byte[]> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    @Override
    public Row4<String, String, String, byte[]> valuesRow() {
        return (Row4) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return QrtzBlobTriggers.QRTZ_BLOB_TRIGGERS.SCHED_NAME;
    }

    @Override
    public Field<String> field2() {
        return QrtzBlobTriggers.QRTZ_BLOB_TRIGGERS.TRIGGER_NAME;
    }

    @Override
    public Field<String> field3() {
        return QrtzBlobTriggers.QRTZ_BLOB_TRIGGERS.TRIGGER_GROUP;
    }

    @Override
    public Field<byte[]> field4() {
        return QrtzBlobTriggers.QRTZ_BLOB_TRIGGERS.BLOB_DATA;
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
    public byte[] component4() {
        return getBlobData();
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
    public byte[] value4() {
        return getBlobData();
    }

    @Override
    public QrtzBlobTriggersRecord value1(String value) {
        setSchedName(value);
        return this;
    }

    @Override
    public QrtzBlobTriggersRecord value2(String value) {
        setTriggerName(value);
        return this;
    }

    @Override
    public QrtzBlobTriggersRecord value3(String value) {
        setTriggerGroup(value);
        return this;
    }

    @Override
    public QrtzBlobTriggersRecord value4(byte[] value) {
        setBlobData(value);
        return this;
    }

    @Override
    public QrtzBlobTriggersRecord values(String value1, String value2, String value3, byte[] value4) {
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
     * Create a detached QrtzBlobTriggersRecord
     */
    public QrtzBlobTriggersRecord() {
        super(QrtzBlobTriggers.QRTZ_BLOB_TRIGGERS);
    }

    /**
     * Create a detached, initialised QrtzBlobTriggersRecord
     */
    public QrtzBlobTriggersRecord(String schedName, String triggerName, String triggerGroup, byte[] blobData) {
        super(QrtzBlobTriggers.QRTZ_BLOB_TRIGGERS);

        setSchedName(schedName);
        setTriggerName(triggerName);
        setTriggerGroup(triggerGroup);
        setBlobData(blobData);
    }
}
