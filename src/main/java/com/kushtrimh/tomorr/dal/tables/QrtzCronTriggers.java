/*
 * This file is generated by jOOQ.
 */
package com.kushtrimh.tomorr.dal.tables;


import com.kushtrimh.tomorr.dal.Keys;
import com.kushtrimh.tomorr.dal.Public;
import com.kushtrimh.tomorr.dal.tables.records.QrtzCronTriggersRecord;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row5;
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
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class QrtzCronTriggers extends TableImpl<QrtzCronTriggersRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.qrtz_cron_triggers</code>
     */
    public static final QrtzCronTriggers QRTZ_CRON_TRIGGERS = new QrtzCronTriggers();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<QrtzCronTriggersRecord> getRecordType() {
        return QrtzCronTriggersRecord.class;
    }

    /**
     * The column <code>public.qrtz_cron_triggers.sched_name</code>.
     */
    public final TableField<QrtzCronTriggersRecord, String> SCHED_NAME = createField(DSL.name("sched_name"), SQLDataType.VARCHAR(120).nullable(false), this, "");

    /**
     * The column <code>public.qrtz_cron_triggers.trigger_name</code>.
     */
    public final TableField<QrtzCronTriggersRecord, String> TRIGGER_NAME = createField(DSL.name("trigger_name"), SQLDataType.VARCHAR(200).nullable(false), this, "");

    /**
     * The column <code>public.qrtz_cron_triggers.trigger_group</code>.
     */
    public final TableField<QrtzCronTriggersRecord, String> TRIGGER_GROUP = createField(DSL.name("trigger_group"), SQLDataType.VARCHAR(200).nullable(false), this, "");

    /**
     * The column <code>public.qrtz_cron_triggers.cron_expression</code>.
     */
    public final TableField<QrtzCronTriggersRecord, String> CRON_EXPRESSION = createField(DSL.name("cron_expression"), SQLDataType.VARCHAR(120).nullable(false), this, "");

    /**
     * The column <code>public.qrtz_cron_triggers.time_zone_id</code>.
     */
    public final TableField<QrtzCronTriggersRecord, String> TIME_ZONE_ID = createField(DSL.name("time_zone_id"), SQLDataType.VARCHAR(80), this, "");

    private QrtzCronTriggers(Name alias, Table<QrtzCronTriggersRecord> aliased) {
        this(alias, aliased, null);
    }

    private QrtzCronTriggers(Name alias, Table<QrtzCronTriggersRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.qrtz_cron_triggers</code> table reference
     */
    public QrtzCronTriggers(String alias) {
        this(DSL.name(alias), QRTZ_CRON_TRIGGERS);
    }

    /**
     * Create an aliased <code>public.qrtz_cron_triggers</code> table reference
     */
    public QrtzCronTriggers(Name alias) {
        this(alias, QRTZ_CRON_TRIGGERS);
    }

    /**
     * Create a <code>public.qrtz_cron_triggers</code> table reference
     */
    public QrtzCronTriggers() {
        this(DSL.name("qrtz_cron_triggers"), null);
    }

    public <O extends Record> QrtzCronTriggers(Table<O> child, ForeignKey<O, QrtzCronTriggersRecord> key) {
        super(child, key, QRTZ_CRON_TRIGGERS);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public UniqueKey<QrtzCronTriggersRecord> getPrimaryKey() {
        return Keys.QRTZ_CRON_TRIGGERS_PKEY;
    }

    @Override
    public List<UniqueKey<QrtzCronTriggersRecord>> getKeys() {
        return Arrays.<UniqueKey<QrtzCronTriggersRecord>>asList(Keys.QRTZ_CRON_TRIGGERS_PKEY);
    }

    @Override
    public List<ForeignKey<QrtzCronTriggersRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<QrtzCronTriggersRecord, ?>>asList(Keys.QRTZ_CRON_TRIGGERS__QRTZ_CRON_TRIGGERS_SCHED_NAME_TRIGGER_NAME_TRIGGER_GROUP_FKEY);
    }

    private transient QrtzTriggers _qrtzTriggers;

    public QrtzTriggers qrtzTriggers() {
        if (_qrtzTriggers == null)
            _qrtzTriggers = new QrtzTriggers(this, Keys.QRTZ_CRON_TRIGGERS__QRTZ_CRON_TRIGGERS_SCHED_NAME_TRIGGER_NAME_TRIGGER_GROUP_FKEY);

        return _qrtzTriggers;
    }

    @Override
    public QrtzCronTriggers as(String alias) {
        return new QrtzCronTriggers(DSL.name(alias), this);
    }

    @Override
    public QrtzCronTriggers as(Name alias) {
        return new QrtzCronTriggers(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public QrtzCronTriggers rename(String name) {
        return new QrtzCronTriggers(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public QrtzCronTriggers rename(Name name) {
        return new QrtzCronTriggers(name, null);
    }

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<String, String, String, String, String> fieldsRow() {
        return (Row5) super.fieldsRow();
    }
}
