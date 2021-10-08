/*
 * This file is generated by jOOQ.
 */
package com.kushtrimh.tomorr.dal.tables;


import com.kushtrimh.tomorr.dal.Keys;
import com.kushtrimh.tomorr.dal.Public;
import com.kushtrimh.tomorr.dal.tables.records.QrtzBlobTriggersRecord;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row4;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import java.util.Arrays;
import java.util.List;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class QrtzBlobTriggers extends TableImpl<QrtzBlobTriggersRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.qrtz_blob_triggers</code>
     */
    public static final QrtzBlobTriggers QRTZ_BLOB_TRIGGERS = new QrtzBlobTriggers();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<QrtzBlobTriggersRecord> getRecordType() {
        return QrtzBlobTriggersRecord.class;
    }

    /**
     * The column <code>public.qrtz_blob_triggers.sched_name</code>.
     */
    public final TableField<QrtzBlobTriggersRecord, String> SCHED_NAME = createField(DSL.name("sched_name"), SQLDataType.VARCHAR(120).nullable(false), this, "");

    /**
     * The column <code>public.qrtz_blob_triggers.trigger_name</code>.
     */
    public final TableField<QrtzBlobTriggersRecord, String> TRIGGER_NAME = createField(DSL.name("trigger_name"), SQLDataType.VARCHAR(200).nullable(false), this, "");

    /**
     * The column <code>public.qrtz_blob_triggers.trigger_group</code>.
     */
    public final TableField<QrtzBlobTriggersRecord, String> TRIGGER_GROUP = createField(DSL.name("trigger_group"), SQLDataType.VARCHAR(200).nullable(false), this, "");

    /**
     * The column <code>public.qrtz_blob_triggers.blob_data</code>.
     */
    public final TableField<QrtzBlobTriggersRecord, byte[]> BLOB_DATA = createField(DSL.name("blob_data"), SQLDataType.BLOB, this, "");

    private QrtzBlobTriggers(Name alias, Table<QrtzBlobTriggersRecord> aliased) {
        this(alias, aliased, null);
    }

    private QrtzBlobTriggers(Name alias, Table<QrtzBlobTriggersRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.qrtz_blob_triggers</code> table reference
     */
    public QrtzBlobTriggers(String alias) {
        this(DSL.name(alias), QRTZ_BLOB_TRIGGERS);
    }

    /**
     * Create an aliased <code>public.qrtz_blob_triggers</code> table reference
     */
    public QrtzBlobTriggers(Name alias) {
        this(alias, QRTZ_BLOB_TRIGGERS);
    }

    /**
     * Create a <code>public.qrtz_blob_triggers</code> table reference
     */
    public QrtzBlobTriggers() {
        this(DSL.name("qrtz_blob_triggers"), null);
    }

    public <O extends Record> QrtzBlobTriggers(Table<O> child, ForeignKey<O, QrtzBlobTriggersRecord> key) {
        super(child, key, QRTZ_BLOB_TRIGGERS);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public UniqueKey<QrtzBlobTriggersRecord> getPrimaryKey() {
        return Keys.QRTZ_BLOB_TRIGGERS_PKEY;
    }

    @Override
    public List<UniqueKey<QrtzBlobTriggersRecord>> getKeys() {
        return Arrays.<UniqueKey<QrtzBlobTriggersRecord>>asList(Keys.QRTZ_BLOB_TRIGGERS_PKEY);
    }

    @Override
    public List<ForeignKey<QrtzBlobTriggersRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<QrtzBlobTriggersRecord, ?>>asList(Keys.QRTZ_BLOB_TRIGGERS__QRTZ_BLOB_TRIGGERS_SCHED_NAME_TRIGGER_NAME_TRIGGER_GROUP_FKEY);
    }

    private transient QrtzTriggers _qrtzTriggers;

    public QrtzTriggers qrtzTriggers() {
        if (_qrtzTriggers == null)
            _qrtzTriggers = new QrtzTriggers(this, Keys.QRTZ_BLOB_TRIGGERS__QRTZ_BLOB_TRIGGERS_SCHED_NAME_TRIGGER_NAME_TRIGGER_GROUP_FKEY);

        return _qrtzTriggers;
    }

    @Override
    public QrtzBlobTriggers as(String alias) {
        return new QrtzBlobTriggers(DSL.name(alias), this);
    }

    @Override
    public QrtzBlobTriggers as(Name alias) {
        return new QrtzBlobTriggers(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public QrtzBlobTriggers rename(String name) {
        return new QrtzBlobTriggers(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public QrtzBlobTriggers rename(Name name) {
        return new QrtzBlobTriggers(name, null);
    }

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row4<String, String, String, byte[]> fieldsRow() {
        return (Row4) super.fieldsRow();
    }
}
