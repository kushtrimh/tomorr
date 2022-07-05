/*
 * This file is generated by jOOQ.
 */
package com.kushtrimh.tomorr.dal.tables.records;


import com.kushtrimh.tomorr.dal.tables.QrtzCalendars;
import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Row3;
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
    name = "qrtz_calendars",
    schema = "public",
    uniqueConstraints = {
        @UniqueConstraint(name = "qrtz_calendars_pkey", columnNames = { "sched_name", "calendar_name" })
    }
)
public class QrtzCalendarsRecord extends UpdatableRecordImpl<QrtzCalendarsRecord> implements Record3<String, String, byte[]> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.qrtz_calendars.sched_name</code>.
     */
    public void setSchedName(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.qrtz_calendars.sched_name</code>.
     */
    @Column(name = "sched_name", nullable = false, length = 120)
    public String getSchedName() {
        return (String) get(0);
    }

    /**
     * Setter for <code>public.qrtz_calendars.calendar_name</code>.
     */
    public void setCalendarName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.qrtz_calendars.calendar_name</code>.
     */
    @Column(name = "calendar_name", nullable = false, length = 200)
    public String getCalendarName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.qrtz_calendars.calendar</code>.
     */
    public void setCalendar(byte[] value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.qrtz_calendars.calendar</code>.
     */
    @Column(name = "calendar", nullable = false)
    public byte[] getCalendar() {
        return (byte[]) get(2);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record2<String, String> key() {
        return (Record2) super.key();
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row3<String, String, byte[]> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    @Override
    public Row3<String, String, byte[]> valuesRow() {
        return (Row3) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return QrtzCalendars.QRTZ_CALENDARS.SCHED_NAME;
    }

    @Override
    public Field<String> field2() {
        return QrtzCalendars.QRTZ_CALENDARS.CALENDAR_NAME;
    }

    @Override
    public Field<byte[]> field3() {
        return QrtzCalendars.QRTZ_CALENDARS.CALENDAR;
    }

    @Override
    public String component1() {
        return getSchedName();
    }

    @Override
    public String component2() {
        return getCalendarName();
    }

    @Override
    public byte[] component3() {
        return getCalendar();
    }

    @Override
    public String value1() {
        return getSchedName();
    }

    @Override
    public String value2() {
        return getCalendarName();
    }

    @Override
    public byte[] value3() {
        return getCalendar();
    }

    @Override
    public QrtzCalendarsRecord value1(String value) {
        setSchedName(value);
        return this;
    }

    @Override
    public QrtzCalendarsRecord value2(String value) {
        setCalendarName(value);
        return this;
    }

    @Override
    public QrtzCalendarsRecord value3(byte[] value) {
        setCalendar(value);
        return this;
    }

    @Override
    public QrtzCalendarsRecord values(String value1, String value2, byte[] value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached QrtzCalendarsRecord
     */
    public QrtzCalendarsRecord() {
        super(QrtzCalendars.QRTZ_CALENDARS);
    }

    /**
     * Create a detached, initialised QrtzCalendarsRecord
     */
    public QrtzCalendarsRecord(String schedName, String calendarName, byte[] calendar) {
        super(QrtzCalendars.QRTZ_CALENDARS);

        setSchedName(schedName);
        setCalendarName(calendarName);
        setCalendar(calendar);
    }
}
