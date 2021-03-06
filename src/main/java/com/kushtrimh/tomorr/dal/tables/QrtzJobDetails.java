/*
 * This file is generated by jOOQ.
 */
package com.kushtrimh.tomorr.dal.tables;


import com.kushtrimh.tomorr.dal.Indexes;
import com.kushtrimh.tomorr.dal.Keys;
import com.kushtrimh.tomorr.dal.Public;
import com.kushtrimh.tomorr.dal.tables.records.QrtzJobDetailsRecord;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row10;
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
public class QrtzJobDetails extends TableImpl<QrtzJobDetailsRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.qrtz_job_details</code>
     */
    public static final QrtzJobDetails QRTZ_JOB_DETAILS = new QrtzJobDetails();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<QrtzJobDetailsRecord> getRecordType() {
        return QrtzJobDetailsRecord.class;
    }

    /**
     * The column <code>public.qrtz_job_details.sched_name</code>.
     */
    public final TableField<QrtzJobDetailsRecord, String> SCHED_NAME = createField(DSL.name("sched_name"), SQLDataType.VARCHAR(120).nullable(false), this, "");

    /**
     * The column <code>public.qrtz_job_details.job_name</code>.
     */
    public final TableField<QrtzJobDetailsRecord, String> JOB_NAME = createField(DSL.name("job_name"), SQLDataType.VARCHAR(200).nullable(false), this, "");

    /**
     * The column <code>public.qrtz_job_details.job_group</code>.
     */
    public final TableField<QrtzJobDetailsRecord, String> JOB_GROUP = createField(DSL.name("job_group"), SQLDataType.VARCHAR(200).nullable(false), this, "");

    /**
     * The column <code>public.qrtz_job_details.description</code>.
     */
    public final TableField<QrtzJobDetailsRecord, String> DESCRIPTION = createField(DSL.name("description"), SQLDataType.VARCHAR(250), this, "");

    /**
     * The column <code>public.qrtz_job_details.job_class_name</code>.
     */
    public final TableField<QrtzJobDetailsRecord, String> JOB_CLASS_NAME = createField(DSL.name("job_class_name"), SQLDataType.VARCHAR(250).nullable(false), this, "");

    /**
     * The column <code>public.qrtz_job_details.is_durable</code>.
     */
    public final TableField<QrtzJobDetailsRecord, Boolean> IS_DURABLE = createField(DSL.name("is_durable"), SQLDataType.BOOLEAN.nullable(false), this, "");

    /**
     * The column <code>public.qrtz_job_details.is_nonconcurrent</code>.
     */
    public final TableField<QrtzJobDetailsRecord, Boolean> IS_NONCONCURRENT = createField(DSL.name("is_nonconcurrent"), SQLDataType.BOOLEAN.nullable(false), this, "");

    /**
     * The column <code>public.qrtz_job_details.is_update_data</code>.
     */
    public final TableField<QrtzJobDetailsRecord, Boolean> IS_UPDATE_DATA = createField(DSL.name("is_update_data"), SQLDataType.BOOLEAN.nullable(false), this, "");

    /**
     * The column <code>public.qrtz_job_details.requests_recovery</code>.
     */
    public final TableField<QrtzJobDetailsRecord, Boolean> REQUESTS_RECOVERY = createField(DSL.name("requests_recovery"), SQLDataType.BOOLEAN.nullable(false), this, "");

    /**
     * The column <code>public.qrtz_job_details.job_data</code>.
     */
    public final TableField<QrtzJobDetailsRecord, byte[]> JOB_DATA = createField(DSL.name("job_data"), SQLDataType.BLOB, this, "");

    private QrtzJobDetails(Name alias, Table<QrtzJobDetailsRecord> aliased) {
        this(alias, aliased, null);
    }

    private QrtzJobDetails(Name alias, Table<QrtzJobDetailsRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.qrtz_job_details</code> table reference
     */
    public QrtzJobDetails(String alias) {
        this(DSL.name(alias), QRTZ_JOB_DETAILS);
    }

    /**
     * Create an aliased <code>public.qrtz_job_details</code> table reference
     */
    public QrtzJobDetails(Name alias) {
        this(alias, QRTZ_JOB_DETAILS);
    }

    /**
     * Create a <code>public.qrtz_job_details</code> table reference
     */
    public QrtzJobDetails() {
        this(DSL.name("qrtz_job_details"), null);
    }

    public <O extends Record> QrtzJobDetails(Table<O> child, ForeignKey<O, QrtzJobDetailsRecord> key) {
        super(child, key, QRTZ_JOB_DETAILS);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.IDX_QRTZ_J_GRP, Indexes.IDX_QRTZ_J_REQ_RECOVERY);
    }

    @Override
    public UniqueKey<QrtzJobDetailsRecord> getPrimaryKey() {
        return Keys.QRTZ_JOB_DETAILS_PKEY;
    }

    @Override
    public List<UniqueKey<QrtzJobDetailsRecord>> getKeys() {
        return Arrays.<UniqueKey<QrtzJobDetailsRecord>>asList(Keys.QRTZ_JOB_DETAILS_PKEY);
    }

    @Override
    public QrtzJobDetails as(String alias) {
        return new QrtzJobDetails(DSL.name(alias), this);
    }

    @Override
    public QrtzJobDetails as(Name alias) {
        return new QrtzJobDetails(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public QrtzJobDetails rename(String name) {
        return new QrtzJobDetails(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public QrtzJobDetails rename(Name name) {
        return new QrtzJobDetails(name, null);
    }

    // -------------------------------------------------------------------------
    // Row10 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row10<String, String, String, String, String, Boolean, Boolean, Boolean, Boolean, byte[]> fieldsRow() {
        return (Row10) super.fieldsRow();
    }
}
