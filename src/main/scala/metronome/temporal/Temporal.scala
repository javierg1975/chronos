package metronome.temporal

/*
 * Copyright (c) 2012, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
/*
 * Copyright (c) 2012, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


/**
 * A standard set of fields.
 * <p>
 * This set of fields provide field-based access to manipulate a date, time or date-time.
 * The standard set of fields can be extended by implementing {@link TemporalField}.
 * <p>
 * These fields are intended to be applicable in multiple calendar systems.
 * For example, most non-ISO calendar systems define dates as a year, month and day,
 * just with slightly different rules.
 * The documentation of each field explains how it operates.
 *
 * @implSpec
 * This is a final, immutable and thread-safe enum.
 *
 * @since 1.8
 */
final object ChronoField {
  /**
   * The nano-of-second.
   * <p>
   * This counts the nanosecond within the second, from 0 to 999,999,999.
   * This field has the same meaning for all calendar systems.
   * <p>
   * This field is used to represent the nano-of-second handling any fraction of the second.
   * Implementations of {@code TemporalAccessor} should provide a value for this field if
   * they can return a value for {@link #SECOND_OF_MINUTE}, {@link #SECOND_OF_DAY} or
   * {@link #INSTANT_SECONDS} filling unknown precision with zero.
   * <p>
   * When this field is used for setting a value, it should set as much precision as the
   * object stores, using integer division to remove excess precision.
   * For example, if the {@code TemporalAccessor} stores time to millisecond precision,
   * then the nano-of-second must be divided by 1,000,000 before replacing the milli-of-second.
   * <p>
   * When parsing this field it behaves equivalent to the following:
   * The value is validated in strict and smart mode but not in lenient mode.
   * The field is resolved in combination with {@code MILLI_OF_SECOND} and {@code MICRO_OF_SECOND}.
   */
  final val NANO_OF_SECOND: = null
  /**
   * The nano-of-day.
   * <p>
   * This counts the nanosecond within the day, from 0 to (24 * 60 * 60 * 1,000,000,000) - 1.
   * This field has the same meaning for all calendar systems.
   * <p>
   * This field is used to represent the nano-of-day handling any fraction of the second.
   * Implementations of {@code TemporalAccessor} should provide a value for this field if
   * they can return a value for {@link #SECOND_OF_DAY} filling unknown precision with zero.
   * <p>
   * When parsing this field it behaves equivalent to the following:
   * The value is validated in strict and smart mode but not in lenient mode.
   * The value is split to form {@code NANO_OF_SECOND}, {@code SECOND_OF_MINUTE},
   * {@code MINUTE_OF_HOUR} and {@code HOUR_OF_DAY} fields.
   */
  final val NANO_OF_DAY: = null
  /**
   * The micro-of-second.
   * <p>
   * This counts the microsecond within the second, from 0 to 999,999.
   * This field has the same meaning for all calendar systems.
   * <p>
   * This field is used to represent the micro-of-second handling any fraction of the second.
   * Implementations of {@code TemporalAccessor} should provide a value for this field if
   * they can return a value for {@link #SECOND_OF_MINUTE}, {@link #SECOND_OF_DAY} or
   * {@link #INSTANT_SECONDS} filling unknown precision with zero.
   * <p>
   * When this field is used for setting a value, it should behave in the same way as
   * setting {@link #NANO_OF_SECOND} with the value multiplied by 1,000.
   * <p>
   * When parsing this field it behaves equivalent to the following:
   * The value is validated in strict and smart mode but not in lenient mode.
   * The field is resolved in combination with {@code MILLI_OF_SECOND} to produce
   * {@code NANO_OF_SECOND}.
   */
  final val MICRO_OF_SECOND: = null
  /**
   * The micro-of-day.
   * <p>
   * This counts the microsecond within the day, from 0 to (24 * 60 * 60 * 1,000,000) - 1.
   * This field has the same meaning for all calendar systems.
   * <p>
   * This field is used to represent the micro-of-day handling any fraction of the second.
   * Implementations of {@code TemporalAccessor} should provide a value for this field if
   * they can return a value for {@link #SECOND_OF_DAY} filling unknown precision with zero.
   * <p>
   * When this field is used for setting a value, it should behave in the same way as
   * setting {@link #NANO_OF_DAY} with the value multiplied by 1,000.
   * <p>
   * When parsing this field it behaves equivalent to the following:
   * The value is validated in strict and smart mode but not in lenient mode.
   * The value is split to form {@code MICRO_OF_SECOND}, {@code SECOND_OF_MINUTE},
   * {@code MINUTE_OF_HOUR} and {@code HOUR_OF_DAY} fields.
   */
  final val MICRO_OF_DAY: = null
  /**
   * The milli-of-second.
   * <p>
   * This counts the millisecond within the second, from 0 to 999.
   * This field has the same meaning for all calendar systems.
   * <p>
   * This field is used to represent the milli-of-second handling any fraction of the second.
   * Implementations of {@code TemporalAccessor} should provide a value for this field if
   * they can return a value for {@link #SECOND_OF_MINUTE}, {@link #SECOND_OF_DAY} or
   * {@link #INSTANT_SECONDS} filling unknown precision with zero.
   * <p>
   * When this field is used for setting a value, it should behave in the same way as
   * setting {@link #NANO_OF_SECOND} with the value multiplied by 1,000,000.
   * <p>
   * When parsing this field it behaves equivalent to the following:
   * The value is validated in strict and smart mode but not in lenient mode.
   * The field is resolved in combination with {@code MICRO_OF_SECOND} to produce
   * {@code NANO_OF_SECOND}.
   */
  final val MILLI_OF_SECOND: = null
  /**
   * The milli-of-day.
   * <p>
   * This counts the millisecond within the day, from 0 to (24 * 60 * 60 * 1,000) - 1.
   * This field has the same meaning for all calendar systems.
   * <p>
   * This field is used to represent the milli-of-day handling any fraction of the second.
   * Implementations of {@code TemporalAccessor} should provide a value for this field if
   * they can return a value for {@link #SECOND_OF_DAY} filling unknown precision with zero.
   * <p>
   * When this field is used for setting a value, it should behave in the same way as
   * setting {@link #NANO_OF_DAY} with the value multiplied by 1,000,000.
   * <p>
   * When parsing this field it behaves equivalent to the following:
   * The value is validated in strict and smart mode but not in lenient mode.
   * The value is split to form {@code MILLI_OF_SECOND}, {@code SECOND_OF_MINUTE},
   * {@code MINUTE_OF_HOUR} and {@code HOUR_OF_DAY} fields.
   */
  final val MILLI_OF_DAY: = null
  /**
   * The second-of-minute.
   * <p>
   * This counts the second within the minute, from 0 to 59.
   * This field has the same meaning for all calendar systems.
   * <p>
   * When parsing this field it behaves equivalent to the following:
   * The value is validated in strict and smart mode but not in lenient mode.
   */
  final val SECOND_OF_MINUTE: = null
  /**
   * The second-of-day.
   * <p>
   * This counts the second within the day, from 0 to (24 * 60 * 60) - 1.
   * This field has the same meaning for all calendar systems.
   * <p>
   * When parsing this field it behaves equivalent to the following:
   * The value is validated in strict and smart mode but not in lenient mode.
   * The value is split to form {@code SECOND_OF_MINUTE}, {@code MINUTE_OF_HOUR}
   * and {@code HOUR_OF_DAY} fields.
   */
  final val SECOND_OF_DAY: = null
  /**
   * The minute-of-hour.
   * <p>
   * This counts the minute within the hour, from 0 to 59.
   * This field has the same meaning for all calendar systems.
   * <p>
   * When parsing this field it behaves equivalent to the following:
   * The value is validated in strict and smart mode but not in lenient mode.
   */
  final val MINUTE_OF_HOUR: = null
  /**
   * The minute-of-day.
   * <p>
   * This counts the minute within the day, from 0 to (24 * 60) - 1.
   * This field has the same meaning for all calendar systems.
   * <p>
   * When parsing this field it behaves equivalent to the following:
   * The value is validated in strict and smart mode but not in lenient mode.
   * The value is split to form {@code MINUTE_OF_HOUR} and {@code HOUR_OF_DAY} fields.
   */
  final val MINUTE_OF_DAY: = null
  /**
   * The hour-of-am-pm.
   * <p>
   * This counts the hour within the AM/PM, from 0 to 11.
   * This is the hour that would be observed on a standard 12-hour digital clock.
   * This field has the same meaning for all calendar systems.
   * <p>
   * When parsing this field it behaves equivalent to the following:
   * The value is validated from 0 to 11 in strict and smart mode.
   * In lenient mode the value is not validated. It is combined with
   * {@code AMPM_OF_DAY} to form {@code HOUR_OF_DAY} by multiplying
   * the {AMPM_OF_DAY} value by 12.
   */
  final val HOUR_OF_AMPM: = null
  /**
   * The clock-hour-of-am-pm.
   * <p>
   * This counts the hour within the AM/PM, from 1 to 12.
   * This is the hour that would be observed on a standard 12-hour analog wall clock.
   * This field has the same meaning for all calendar systems.
   * <p>
   * When parsing this field it behaves equivalent to the following:
   * The value is validated from 1 to 12 in strict mode and from
   * 0 to 12 in smart mode. In lenient mode the value is not validated.
   * The field is converted to an {@code HOUR_OF_AMPM} with the same value,
   * unless the value is 12, in which case it is converted to 0.
   */
  final val CLOCK_HOUR_OF_AMPM: = null
  /**
   * The hour-of-day.
   * <p>
   * This counts the hour within the day, from 0 to 23.
   * This is the hour that would be observed on a standard 24-hour digital clock.
   * This field has the same meaning for all calendar systems.
   * <p>
   * When parsing this field it behaves equivalent to the following:
   * The value is validated in strict and smart mode but not in lenient mode.
   * The field is combined with {@code MINUTE_OF_HOUR}, {@code SECOND_OF_MINUTE} and
   * {@code NANO_OF_SECOND} to produce a {@code Time}.
   * In lenient mode, any excess days are added to the parsed date, or
   * made available via {@link java.time.format.DateTimeFormatter#parsedExcessDays()}.
   */
  final val HOUR_OF_DAY: = null
  /**
   * The clock-hour-of-day.
   * <p>
   * This counts the hour within the AM/PM, from 1 to 24.
   * This is the hour that would be observed on a 24-hour analog wall clock.
   * This field has the same meaning for all calendar systems.
   * <p>
   * When parsing this field it behaves equivalent to the following:
   * The value is validated from 1 to 24 in strict mode and from
   * 0 to 24 in smart mode. In lenient mode the value is not validated.
   * The field is converted to an {@code HOUR_OF_DAY} with the same value,
   * unless the value is 24, in which case it is converted to 0.
   */
  final val CLOCK_HOUR_OF_DAY: = null
  /**
   * The am-pm-of-day.
   * <p>
   * This counts the AM/PM within the day, from 0 (AM) to 1 (PM).
   * This field has the same meaning for all calendar systems.
   * <p>
   * When parsing this field it behaves equivalent to the following:
   * The value is validated from 0 to 1 in strict and smart mode.
   * In lenient mode the value is not validated. It is combined with
   * {@code HOUR_OF_AMPM} to form {@code HOUR_OF_DAY} by multiplying
   * the {AMPM_OF_DAY} value by 12.
   */
  final val AMPM_OF_DAY: = null
  /**
   * The day-of-week, such as Tuesday.
   * <p>
   * This represents the standard concept of the day of the week.
   * In the default ISO calendar system, this has values from Monday (1) to Sunday (7).
   * The {@link DayOfWeek} class can be used to interpret the result.
   * <p>
   * Most non-ISO calendar systems also define a seven day week that aligns with ISO.
   * Those calendar systems must also use the same numbering system, from Monday (1) to
   * Sunday (7), which allows {@code DayOfWeek} to be used.
   * <p>
   * Calendar systems that do not have a standard seven day week should implement this field
   * if they have a similar concept of named or numbered days within a period similar
   * to a week. It is recommended that the numbering starts from 1.
   */
  final val DAY_OF_WEEK: = null
  /**
   * The aligned day-of-week within a month.
   * <p>
   * This represents concept of the count of days within the period of a week
   * where the weeks are aligned to the start of the month.
   * This field is typically used with {@link #ALIGNED_WEEK_OF_MONTH}.
   * <p>
   * For example, in a calendar systems with a seven day week, the first aligned-week-of-month
   * starts on day-of-month 1, the second aligned-week starts on day-of-month 8, and so on.
   * Within each of these aligned-weeks, the days are numbered from 1 to 7 and returned
   * as the value of this field.
   * As such, day-of-month 1 to 7 will have aligned-day-of-week values from 1 to 7.
   * And day-of-month 8 to 14 will repeat this with aligned-day-of-week values from 1 to 7.
   * <p>
   * Calendar systems that do not have a seven day week should typically implement this
   * field in the same way, but using the alternate week length.
   */
  final val ALIGNED_DAY_OF_WEEK_IN_MONTH: = null
  /**
   * The aligned day-of-week within a year.
   * <p>
   * This represents concept of the count of days within the period of a week
   * where the weeks are aligned to the start of the year.
   * This field is typically used with {@link #ALIGNED_WEEK_OF_YEAR}.
   * <p>
   * For example, in a calendar systems with a seven day week, the first aligned-week-of-year
   * starts on day-of-year 1, the second aligned-week starts on day-of-year 8, and so on.
   * Within each of these aligned-weeks, the days are numbered from 1 to 7 and returned
   * as the value of this field.
   * As such, day-of-year 1 to 7 will have aligned-day-of-week values from 1 to 7.
   * And day-of-year 8 to 14 will repeat this with aligned-day-of-week values from 1 to 7.
   * <p>
   * Calendar systems that do not have a seven day week should typically implement this
   * field in the same way, but using the alternate week length.
   */
  final val ALIGNED_DAY_OF_WEEK_IN_YEAR: = null
  /**
   * The day-of-month.
   * <p>
   * This represents the concept of the day within the month.
   * In the default ISO calendar system, this has values from 1 to 31 in most months.
   * April, June, September, November have days from 1 to 30, while February has days
   * from 1 to 28, or 29 in a leap year.
   * <p>
   * Non-ISO calendar systems should implement this field using the most recognized
   * day-of-month values for users of the calendar system.
   * Normally, this is a count of days from 1 to the length of the month.
   */
  final val DAY_OF_MONTH: = null
  /**
   * The day-of-year.
   * <p>
   * This represents the concept of the day within the year.
   * In the default ISO calendar system, this has values from 1 to 365 in standard
   * years and 1 to 366 in leap years.
   * <p>
   * Non-ISO calendar systems should implement this field using the most recognized
   * day-of-year values for users of the calendar system.
   * Normally, this is a count of days from 1 to the length of the year.
   * <p>
   * Note that a non-ISO calendar system may have year numbering system that changes
   * at a different point to the natural reset in the month numbering. An example
   * of this is the Japanese calendar system where a change of era, which resets
   * the year number to 1, can happen on any date. The era and year reset also cause
   * the day-of-year to be reset to 1, but not the month-of-year or day-of-month.
   */
  final val DAY_OF_YEAR: = null
  /**
   * The epoch-day, based on the Java epoch of 1970-01-01 (ISO).
   * <p>
   * This field is the sequential count of days where 1970-01-01 (ISO) is zero.
   * Note that this uses the <i>local</i> time-line, ignoring offset and time-zone.
   * <p>
   * This field is strictly defined to have the same meaning in all calendar systems.
   * This is necessary to ensure interoperation between calendars.
   */
  final val EPOCH_DAY: = null
  /**
   * The aligned week within a month.
   * <p>
   * This represents concept of the count of weeks within the period of a month
   * where the weeks are aligned to the start of the month.
   * This field is typically used with {@link #ALIGNED_DAY_OF_WEEK_IN_MONTH}.
   * <p>
   * For example, in a calendar systems with a seven day week, the first aligned-week-of-month
   * starts on day-of-month 1, the second aligned-week starts on day-of-month 8, and so on.
   * Thus, day-of-month values 1 to 7 are in aligned-week 1, while day-of-month values
   * 8 to 14 are in aligned-week 2, and so on.
   * <p>
   * Calendar systems that do not have a seven day week should typically implement this
   * field in the same way, but using the alternate week length.
   */
  final val ALIGNED_WEEK_OF_MONTH: = null
  /**
   * The aligned week within a year.
   * <p>
   * This represents concept of the count of weeks within the period of a year
   * where the weeks are aligned to the start of the year.
   * This field is typically used with {@link #ALIGNED_DAY_OF_WEEK_IN_YEAR}.
   * <p>
   * For example, in a calendar systems with a seven day week, the first aligned-week-of-year
   * starts on day-of-year 1, the second aligned-week starts on day-of-year 8, and so on.
   * Thus, day-of-year values 1 to 7 are in aligned-week 1, while day-of-year values
   * 8 to 14 are in aligned-week 2, and so on.
   * <p>
   * Calendar systems that do not have a seven day week should typically implement this
   * field in the same way, but using the alternate week length.
   */
  final val ALIGNED_WEEK_OF_YEAR: = null
  /**
   * The month-of-year, such as March.
   * <p>
   * This represents the concept of the month within the year.
   * In the default ISO calendar system, this has values from January (1) to December (12).
   * <p>
   * Non-ISO calendar systems should implement this field using the most recognized
   * month-of-year values for users of the calendar system.
   * Normally, this is a count of months starting from 1.
   */
  final val MONTH_OF_YEAR: = null
  /**
   * The proleptic-month based, counting months sequentially from year 0.
   * <p>
   * This field is the sequential count of months where the first month
   * in proleptic-year zero has the value zero.
   * Later months have increasingly larger values.
   * Earlier months have increasingly small values.
   * There are no gaps or breaks in the sequence of months.
   * Note that this uses the <i>local</i> time-line, ignoring offset and time-zone.
   * <p>
   * In the default ISO calendar system, June 2012 would have the value
   * {@code (2012 * 12 + 6 - 1)}. This field is primarily for internal use.
   * <p>
   * Non-ISO calendar systems must implement this field as per the definition above.
   * It is just a simple zero-based count of elapsed months from the start of proleptic-year 0.
   * All calendar systems with a full proleptic-year definition will have a year zero.
   * If the calendar system has a minimum year that excludes year zero, then one must
   * be extrapolated in order for this method to be defined.
   */
  final val PROLEPTIC_MONTH: = null
  /**
   * The year within the era.
   * <p>
   * This represents the concept of the year within the era.
   * This field is typically used with {@link #ERA}.
   * <p>
   * The standard mental model for a date is based on three concepts - year, month and day.
   * These map onto the {@code YEAR}, {@code MONTH_OF_YEAR} and {@code DAY_OF_MONTH} fields.
   * Note that there is no reference to eras.
   * The full model for a date requires four concepts - era, year, month and day. These map onto
   * the {@code ERA}, {@code YEAR_OF_ERA}, {@code MONTH_OF_YEAR} and {@code DAY_OF_MONTH} fields.
   * Whether this field or {@code YEAR} is used depends on which mental model is being used.
   * See {@link ChronoLocalDate} for more discussion on this topic.
   * <p>
   * In the default ISO calendar system, there are two eras defined, 'BCE' and 'CE'.
   * The era 'CE' is the one currently in use and year-of-era runs from 1 to the maximum value.
   * The era 'BCE' is the previous era, and the year-of-era runs backwards.
   * <p>
   * For example, subtracting a year each time yield the following:<br>
   * - year-proleptic 2  = 'CE' year-of-era 2<br>
   * - year-proleptic 1  = 'CE' year-of-era 1<br>
   * - year-proleptic 0  = 'BCE' year-of-era 1<br>
   * - year-proleptic -1 = 'BCE' year-of-era 2<br>
   * <p>
   * Note that the ISO-8601 standard does not actually define eras.
   * Note also that the ISO eras do not align with the well-known AD/BC eras due to the
   * change between the Julian and Gregorian calendar systems.
   * <p>
   * Non-ISO calendar systems should implement this field using the most recognized
   * year-of-era value for users of the calendar system.
   * Since most calendar systems have only two eras, the year-of-era numbering approach
   * will typically be the same as that used by the ISO calendar system.
   * The year-of-era value should typically always be positive, however this is not required.
   */
  final val YEAR_OF_ERA: = null
  /**
   * The proleptic year, such as 2012.
   * <p>
   * This represents the concept of the year, counting sequentially and using negative numbers.
   * The proleptic year is not interpreted in terms of the era.
   * See {@link #YEAR_OF_ERA} for an example showing the mapping from proleptic year to year-of-era.
   * <p>
   * The standard mental model for a date is based on three concepts - year, month and day.
   * These map onto the {@code YEAR}, {@code MONTH_OF_YEAR} and {@code DAY_OF_MONTH} fields.
   * Note that there is no reference to eras.
   * The full model for a date requires four concepts - era, year, month and day. These map onto
   * the {@code ERA}, {@code YEAR_OF_ERA}, {@code MONTH_OF_YEAR} and {@code DAY_OF_MONTH} fields.
   * Whether this field or {@code YEAR_OF_ERA} is used depends on which mental model is being used.
   * See {@link ChronoLocalDate} for more discussion on this topic.
   * <p>
   * Non-ISO calendar systems should implement this field as follows.
   * If the calendar system has only two eras, before and after a fixed date, then the
   * proleptic-year value must be the same as the year-of-era value for the later era,
   * and increasingly negative for the earlier era.
   * If the calendar system has more than two eras, then the proleptic-year value may be
   * defined with any appropriate value, although defining it to be the same as ISO may be
   * the best option.
   */
  final val YEAR: = null
  /**
   * The era.
   * <p>
   * This represents the concept of the era, which is the largest division of the time-line.
   * This field is typically used with {@link #YEAR_OF_ERA}.
   * <p>
   * In the default ISO calendar system, there are two eras defined, 'BCE' and 'CE'.
   * The era 'CE' is the one currently in use and year-of-era runs from 1 to the maximum value.
   * The era 'BCE' is the previous era, and the year-of-era runs backwards.
   * See {@link #YEAR_OF_ERA} for a full example.
   * <p>
   * Non-ISO calendar systems should implement this field to define eras.
   * The value of the era that was active on 1970-01-01 (ISO) must be assigned the value 1.
   * Earlier eras must have sequentially smaller values.
   * Later eras must have sequentially larger values,
   */
  final val ERA: = null
  /**
   * The instant epoch-seconds.
   * <p>
   * This represents the concept of the sequential count of seconds where
   * 1970-01-01T00:00Z (ISO) is zero.
   * This field may be used with {@link #NANO_OF_SECOND} to represent the fraction of the second.
   * <p>
   * An {@link Instant} represents an instantaneous point on the time-line.
   * On their own, an instant has insufficient information to allow a local date-time to be obtained.
   * Only when paired with an offset or time-zone can the local date or time be calculated.
   * <p>
   * This field is strictly defined to have the same meaning in all calendar systems.
   * This is necessary to ensure interoperation between calendars.
   */
  final val INSTANT_SECONDS: = null
  /**
   * The offset from UTC/Greenwich.
   * <p>
   * This represents the concept of the offset in seconds of local time from UTC/Greenwich.
   * <p>
   * A {@link ZoneOffset} represents the period of time that local time differs from UTC/Greenwich.
   * This is usually a fixed number of hours and minutes.
   * It is equivalent to the {@link ZoneOffset#getTotalSeconds() total amount} of the offset in seconds.
   * For example, during the winter Paris has an offset of {@code +01:00}, which is 3600 seconds.
   * <p>
   * This field is strictly defined to have the same meaning in all calendar systems.
   * This is necessary to ensure interoperation between calendars.
   */
  final val OFFSET_SECONDS: = null
}

final class ChronoField extends TemporalField {
  private def this(name: String, baseUnit: TemporalUnit, rangeUnit: TemporalUnit, range: ValueRange) {
    this()
    this.name = name
    this.baseUnit = baseUnit
    this.rangeUnit = rangeUnit
    this.range = range
    this.displayNameKey = null
  }

  private def this(name: String, baseUnit: TemporalUnit, rangeUnit: TemporalUnit, range: ValueRange, displayNameKey: String) {
    this()
    this.name = name
    this.baseUnit = baseUnit
    this.rangeUnit = rangeUnit
    this.range = range
    this.displayNameKey = displayNameKey
  }

  override def getDisplayName(locale: Locale): String = {
    Objects.requireNonNull(locale, "locale")
    if (displayNameKey == null) {
      return name
    }
    val lr: LocaleResources = LocaleProviderAdapter.getResourceBundleBased.getLocaleResources(locale)
    val rb: ResourceBundle = lr.getJavaTimeFormatData
    val key: String = "field." + displayNameKey
    return if (rb.containsKey(key)) rb.getString(key) else name
  }

  def getBaseUnit: TemporalUnit = {
    return baseUnit
  }

  def getRangeUnit: TemporalUnit = {
    return rangeUnit
  }

  /**
   * Gets the range of valid values for the field.
   * <p>
   * All fields can be expressed as a {@code long} integer.
   * This method returns an object that describes the valid range for that value.
   * <p>
   * This method returns the range of the field in the ISO-8601 calendar system.
   * This range may be incorrect for other calendar systems.
   * Use {@link Chronology#range(ChronoField)} to access the correct range
   * for a different calendar system.
   * <p>
   * Note that the result only describes the minimum and maximum valid values
   * and it is important not to read too much into them. For example, there
   * could be values within the range that are invalid for the field.
   *
   * @return the range of valid values for the field, not null
   */
  def range: ValueRange = {
    return range
  }

  /**
   * Checks if this field represents a component of a date.
   * <p>
   * Fields from day-of-week to era are date-based.
   *
   * @return true if it is a component of a date
   */
  def isDateBased: Boolean = {
    return ordinal >= DAY_OF_WEEK.ordinal && ordinal <= ERA.ordinal
  }

  /**
   * Checks if this field represents a component of a time.
   * <p>
   * Fields from nano-of-second to am-pm-of-day are time-based.
   *
   * @return true if it is a component of a time
   */
  def isTimeBased: Boolean = {
    return ordinal < DAY_OF_WEEK.ordinal
  }

  /**
   * Checks that the specified value is valid for this field.
   * <p>
   * This validates that the value is within the outer range of valid values
   * returned by {@link #range()}.
   * <p>
   * This method checks against the range of the field in the ISO-8601 calendar system.
   * This range may be incorrect for other calendar systems.
   * Use {@link Chronology#range(ChronoField)} to access the correct range
   * for a different calendar system.
   *
   * @param value  the value to check
   * @return the value that was passed in
   */
  def checkValidValue(value: Long): Long = {
    return range.checkValidValue(value, this)
  }

  /**
   * Checks that the specified value is valid and fits in an {@code int}.
   * <p>
   * This validates that the value is within the outer range of valid values
   * returned by {@link #range()}.
   * It also checks that all valid values are within the bounds of an {@code int}.
   * <p>
   * This method checks against the range of the field in the ISO-8601 calendar system.
   * This range may be incorrect for other calendar systems.
   * Use {@link Chronology#range(ChronoField)} to access the correct range
   * for a different calendar system.
   *
   * @param value  the value to check
   * @return the value that was passed in
   */
  def checkValidIntValue(value: Long): Int = {
    return range.checkValidIntValue(value, this)
  }

  def isSupportedBy(temporal: TemporalAccessor): Boolean = {
    return temporal.isSupported(this)
  }

  def rangeRefinedBy(temporal: TemporalAccessor): ValueRange = {
    return temporal.range(this)
  }

  def getFrom(temporal: TemporalAccessor): Long = {
    return temporal.getLong(this)
  }

  @SuppressWarnings(Array("unchecked")) def adjustInto(temporal: R, newValue: Long): R = {
    return temporal.`with`(this, newValue).asInstanceOf[R]
  }

  override def toString: String = {
    return name
  }

  private final val name: String = null
  private final val baseUnit: TemporalUnit = null
  private final val rangeUnit: TemporalUnit = null
  private final val range: ValueRange = null
  private final val displayNameKey: String = null
}

/*
 * Copyright (c) 2012, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
/*
 * Copyright (c) 2012, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/**
 * A standard set of date periods units.
 * <p>
 * This set of units provide unit-based access to manipulate a date, time or date-time.
 * The standard set of units can be extended by implementing {@link TemporalUnit}.
 * <p>
 * These units are intended to be applicable in multiple calendar systems.
 * For example, most non-ISO calendar systems define units of years, months and days,
 * just with slightly different rules.
 * The documentation of each unit explains how it operates.
 *
 * @implSpec
 * This is a final, immutable and thread-safe enum.
 *
 * @since 1.8
 */
final object ChronoUnit {
  /**
   * Unit that represents the concept of a nanosecond, the smallest supported unit of time.
   * For the ISO calendar system, it is equal to the 1,000,000,000th part of the second unit.
   */
  final val NANOS: = null
  /**
   * Unit that represents the concept of a microsecond.
   * For the ISO calendar system, it is equal to the 1,000,000th part of the second unit.
   */
  final val MICROS: = null
  /**
   * Unit that represents the concept of a millisecond.
   * For the ISO calendar system, it is equal to the 1000th part of the second unit.
   */
  final val MILLIS: = null
  /**
   * Unit that represents the concept of a second.
   * For the ISO calendar system, it is equal to the second in the SI system
   * of units, except around a leap-second.
   */
  final val SECONDS: = null
  /**
   * Unit that represents the concept of a minute.
   * For the ISO calendar system, it is equal to 60 seconds.
   */
  final val MINUTES: = null
  /**
   * Unit that represents the concept of an hour.
   * For the ISO calendar system, it is equal to 60 minutes.
   */
  final val HOURS: = null
  /**
   * Unit that represents the concept of half a day, as used in AM/PM.
   * For the ISO calendar system, it is equal to 12 hours.
   */
  final val HALF_DAYS: = null
  /**
   * Unit that represents the concept of a day.
   * For the ISO calendar system, it is the standard day from midnight to midnight.
   * The estimated duration of a day is {@code 24 Hours}.
   * <p>
   * When used with other calendar systems it must correspond to the day defined by
   * the rising and setting of the Sun on Earth. It is not required that days begin
   * at midnight - when converting between calendar systems, the date should be
   * equivalent at midday.
   */
  final val DAYS: = null
  /**
   * Unit that represents the concept of a week.
   * For the ISO calendar system, it is equal to 7 days.
   * <p>
   * When used with other calendar systems it must correspond to an integral number of days.
   */
  final val WEEKS: = null
  /**
   * Unit that represents the concept of a month.
   * For the ISO calendar system, the length of the month varies by month-of-year.
   * The estimated duration of a month is one twelfth of {@code 365.2425 Days}.
   * <p>
   * When used with other calendar systems it must correspond to an integral number of days.
   */
  final val MONTHS: = null
  /**
   * Unit that represents the concept of a year.
   * For the ISO calendar system, it is equal to 12 months.
   * The estimated duration of a year is {@code 365.2425 Days}.
   * <p>
   * When used with other calendar systems it must correspond to an integral number of days
   * or months roughly equal to a year defined by the passage of the Earth around the Sun.
   */
  final val YEARS: = null
  /**
   * Unit that represents the concept of a decade.
   * For the ISO calendar system, it is equal to 10 years.
   * <p>
   * When used with other calendar systems it must correspond to an integral number of days
   * and is normally an integral number of years.
   */
  final val DECADES: = null
  /**
   * Unit that represents the concept of a century.
   * For the ISO calendar system, it is equal to 100 years.
   * <p>
   * When used with other calendar systems it must correspond to an integral number of days
   * and is normally an integral number of years.
   */
  final val CENTURIES: = null
  /**
   * Unit that represents the concept of a millennium.
   * For the ISO calendar system, it is equal to 1000 years.
   * <p>
   * When used with other calendar systems it must correspond to an integral number of days
   * and is normally an integral number of years.
   */
  final val MILLENNIA: = null
  /**
   * Unit that represents the concept of an era.
   * The ISO calendar system doesn't have eras thus it is impossible to add
   * an era to a date or date-time.
   * The estimated duration of the era is artificially defined as {@code 1,000,000,000 Years}.
   * <p>
   * When used with other calendar systems there are no restrictions on the unit.
   */
  final val ERAS: = null
  /**
   * Artificial unit that represents the concept of forever.
   * This is primarily used with {@link TemporalField} to represent unbounded fields
   * such as the year or era.
   * The estimated duration of the era is artificially defined as the largest duration
   * supported by {@code Duration}.
   */
  final val FOREVER: = null
}

final class ChronoUnit extends TemporalUnit {
  private def this(name: String, estimatedDuration: Nothing) {
    this()
    this.name = name
    this.duration = estimatedDuration
  }

  /**
   * Gets the estimated duration of this unit in the ISO calendar system.
   * <p>
   * All of the units in this class have an estimated duration.
   * Days vary due to daylight saving time, while months have different lengths.
   *
   * @return the estimated duration of this unit, not null
   */
  def getDuration: Nothing = {
    return duration
  }

  /**
   * Checks if the duration of the unit is an estimate.
   * <p>
   * All time units in this class are considered to be accurate, while all date
   * units in this class are considered to be estimated.
   * <p>
   * This definition ignores leap seconds, but considers that Days vary due to
   * daylight saving time and months have different lengths.
   *
   * @return true if the duration is estimated, false if accurate
   */
  def isDurationEstimated: Boolean = {
    return this.compareTo(DAYS) >= 0
  }

  /**
   * Checks if this unit is a date unit.
   * <p>
   * All units from days to eras inclusive are date-based.
   * Time-based units and {@code FOREVER} return false.
   *
   * @return true if a date unit, false if a time unit
   */
  def isDateBased: Boolean = {
    return this.compareTo(DAYS) >= 0 && this ne FOREVER
  }

  /**
   * Checks if this unit is a time unit.
   * <p>
   * All units from nanos to half-days inclusive are time-based.
   * Date-based units and {@code FOREVER} return false.
   *
   * @return true if a time unit, false if a date unit
   */
  def isTimeBased: Boolean = {
    return this.compareTo(DAYS) < 0
  }

  override def isSupportedBy(temporal: Temporal): Boolean = {
    return temporal.isSupported(this)
  }

  @SuppressWarnings(Array("unchecked")) def addTo(temporal: R, amount: Long): R = {
    return temporal.plus(amount, this).asInstanceOf[R]
  }

  def between(temporal1Inclusive: Temporal, temporal2Exclusive: Temporal): Long = {
    return temporal1Inclusive.until(temporal2Exclusive, this)
  }

  override def toString: String = {
    return name
  }

  private final val name: String = null
  private final val duration: Nothing = null
}

/*
 * Copyright (c) 2012, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
/*
 * Copyright (c) 2011-2012, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/**
 * Fields and units specific to the ISO-8601 calendar system,
 * including quarter-of-year and week-based-year.
 * <p>
 * This class defines fields and units that are specific to the ISO calendar system.
 *
 * <h3>Quarter of year</h3>
 * The ISO-8601 standard is based on the standard civic 12 month year.
 * This is commonly divided into four quarters, often abbreviated as Q1, Q2, Q3 and Q4.
 * <p>
 * January, February and March are in Q1.
 * April, May and June are in Q2.
 * July, August and September are in Q3.
 * October, November and December are in Q4.
 * <p>
 * The complete date is expressed using three fields:
 * <p><ul>
 * <li>{@link #DAY_OF_QUARTER DAY_OF_QUARTER} - the day within the quarter, from 1 to 90, 91 or 92
 * <li>{@link #QUARTER_OF_YEAR QUARTER_OF_YEAR} - the week within the week-based-year
 * <li>{@link ChronoField#YEAR YEAR} - the standard ISO year
 * </ul><p>
 *
 * <h3>Week based years</h3>
 * The ISO-8601 standard was originally intended as a data interchange format,
 * defining a string format for dates and times. However, it also defines an
 * alternate way of expressing the date, based on the concept of week-based-year.
 * <p>
 * The date is expressed using three fields:
 * <p><ul>
 * <li>{@link ChronoField#DAY_OF_WEEK DAY_OF_WEEK} - the standard field defining the
 * day-of-week from Monday (1) to Sunday (7)
 * <li>{@link #WEEK_OF_WEEK_BASED_YEAR} - the week within the week-based-year
 * <li>{@link #WEEK_BASED_YEAR WEEK_BASED_YEAR} - the week-based-year
 * </ul><p>
 * The week-based-year itself is defined relative to the standard ISO proleptic year.
 * It differs from the standard year in that it always starts on a Monday.
 * <p>
 * The first week of a week-based-year is the first Monday-based week of the standard
 * ISO year that has at least 4 days in the new year.
 * <p><ul>
 * <li>If January 1st is Monday then week 1 starts on January 1st
 * <li>If January 1st is Tuesday then week 1 starts on December 31st of the previous standard year
 * <li>If January 1st is Wednesday then week 1 starts on December 30th of the previous standard year
 * <li>If January 1st is Thursday then week 1 starts on December 29th of the previous standard year
 * <li>If January 1st is Friday then week 1 starts on January 4th
 * <li>If January 1st is Saturday then week 1 starts on January 3rd
 * <li>If January 1st is Sunday then week 1 starts on January 2nd
 * </ul><p>
 * There are 52 weeks in most week-based years, however on occasion there are 53 weeks.
 * <p>
 * For example:
 * <p>
 * <table cellpadding="0" cellspacing="3" border="0" style="text-align: left; width: 50%;">
 * <caption>Examples of Week based Years</caption>
 * <tr><th>Date</th><th>Day-of-week</th><th>Field values</th></tr>
 * <tr><th>2008-12-28</th><td>Sunday</td><td>Week 52 of week-based-year 2008</td></tr>
 * <tr><th>2008-12-29</th><td>Monday</td><td>Week 1 of week-based-year 2009</td></tr>
 * <tr><th>2008-12-31</th><td>Wednesday</td><td>Week 1 of week-based-year 2009</td></tr>
 * <tr><th>2009-01-01</th><td>Thursday</td><td>Week 1 of week-based-year 2009</td></tr>
 * <tr><th>2009-01-04</th><td>Sunday</td><td>Week 1 of week-based-year 2009</td></tr>
 * <tr><th>2009-01-05</th><td>Monday</td><td>Week 2 of week-based-year 2009</td></tr>
 * </table>
 *
 * @implSpec
 * <p>
 * This class is immutable and thread-safe.
 *
 * @since 1.8
 */
final object IsoFields {
  /**
   * The field that represents the day-of-quarter.
   * <p>
   * This field allows the day-of-quarter value to be queried and set.
   * The day-of-quarter has values from 1 to 90 in Q1 of a standard year, from 1 to 91
   * in Q1 of a leap year, from 1 to 91 in Q2 and from 1 to 92 in Q3 and Q4.
   * <p>
   * The day-of-quarter can only be calculated if the day-of-year, month-of-year and year
   * are available.
   * <p>
   * When setting this field, the value is allowed to be partially lenient, taking any
   * value from 1 to 92. If the quarter has less than 92 days, then day 92, and
   * potentially day 91, is in the following quarter.
   * <p>
   * In the resolving phase of parsing, a date can be created from a year,
   * quarter-of-year and day-of-quarter.
   * <p>
   * In {@linkplain ResolverStyle#STRICT strict mode}, all three fields are
   * validated against their range of valid values. The day-of-quarter field
   * is validated from 1 to 90, 91 or 92 depending on the year and quarter.
   * <p>
   * In {@linkplain ResolverStyle#SMART smart mode}, all three fields are
   * validated against their range of valid values. The day-of-quarter field is
   * validated between 1 and 92, ignoring the actual range based on the year and quarter.
   * If the day-of-quarter exceeds the actual range by one day, then the resulting date
   * is one day later. If the day-of-quarter exceeds the actual range by two days,
   * then the resulting date is two days later.
   * <p>
   * In {@linkplain ResolverStyle#LENIENT lenient mode}, only the year is validated
   * against the range of valid values. The resulting date is calculated equivalent to
   * the following three stage approach. First, create a date on the first of January
   * in the requested year. Then take the quarter-of-year, subtract one, and add the
   * amount in quarters to the date. Finally, take the day-of-quarter, subtract one,
   * and add the amount in days to the date.
   * <p>
   * This unit is an immutable and thread-safe singleton.
   */
  final val DAY_OF_QUARTER: TemporalField = Field.DAY_OF_QUARTER
  /**
   * The field that represents the quarter-of-year.
   * <p>
   * This field allows the quarter-of-year value to be queried and set.
   * The quarter-of-year has values from 1 to 4.
   * <p>
   * The quarter-of-year can only be calculated if the month-of-year is available.
   * <p>
   * In the resolving phase of parsing, a date can be created from a year,
   * quarter-of-year and day-of-quarter.
   * See {@link #DAY_OF_QUARTER} for details.
   * <p>
   * This unit is an immutable and thread-safe singleton.
   */
  final val QUARTER_OF_YEAR: TemporalField = Field.QUARTER_OF_YEAR
  /**
   * The field that represents the week-of-week-based-year.
   * <p>
   * This field allows the week of the week-based-year value to be queried and set.
   * The week-of-week-based-year has values from 1 to 52, or 53 if the
   * week-based-year has 53 weeks.
   * <p>
   * In the resolving phase of parsing, a date can be created from a
   * week-based-year, week-of-week-based-year and day-of-week.
   * <p>
   * In {@linkplain ResolverStyle#STRICT strict mode}, all three fields are
   * validated against their range of valid values. The week-of-week-based-year
   * field is validated from 1 to 52 or 53 depending on the week-based-year.
   * <p>
   * In {@linkplain ResolverStyle#SMART smart mode}, all three fields are
   * validated against their range of valid values. The week-of-week-based-year
   * field is validated between 1 and 53, ignoring the week-based-year.
   * If the week-of-week-based-year is 53, but the week-based-year only has
   * 52 weeks, then the resulting date is in week 1 of the following week-based-year.
   * <p>
   * In {@linkplain ResolverStyle#LENIENT lenient mode}, only the week-based-year
   * is validated against the range of valid values. If the day-of-week is outside
   * the range 1 to 7, then the resulting date is adjusted by a suitable number of
   * weeks to reduce the day-of-week to the range 1 to 7. If the week-of-week-based-year
   * value is outside the range 1 to 52, then any excess weeks are added or subtracted
   * from the resulting date.
   * <p>
   * This unit is an immutable and thread-safe singleton.
   */
  final val WEEK_OF_WEEK_BASED_YEAR: TemporalField = Field.WEEK_OF_WEEK_BASED_YEAR
  /**
   * The field that represents the week-based-year.
   * <p>
   * This field allows the week-based-year value to be queried and set.
   * <p>
   * The field has a range that matches {@link Date#MAX} and {@link Date#MIN}.
   * <p>
   * In the resolving phase of parsing, a date can be created from a
   * week-based-year, week-of-week-based-year and day-of-week.
   * See {@link #WEEK_OF_WEEK_BASED_YEAR} for details.
   * <p>
   * This unit is an immutable and thread-safe singleton.
   */
  final val WEEK_BASED_YEAR: TemporalField = Field.WEEK_BASED_YEAR
  /**
   * The unit that represents week-based-years for the purpose of addition and subtraction.
   * <p>
   * This allows a number of week-based-years to be added to, or subtracted from, a date.
   * The unit is equal to either 52 or 53 weeks.
   * The estimated duration of a week-based-year is the same as that of a standard ISO
   * year at {@code 365.2425 Days}.
   * <p>
   * The rules for addition add the number of week-based-years to the existing value
   * for the week-based-year field. If the resulting week-based-year only has 52 weeks,
   * then the date will be in week 1 of the following week-based-year.
   * <p>
   * This unit is an immutable and thread-safe singleton.
   */
  final val WEEK_BASED_YEARS: TemporalUnit = Unit.WEEK_BASED_YEARS
  /**
   * Unit that represents the concept of a quarter-year.
   * For the ISO calendar system, it is equal to 3 months.
   * The estimated duration of a quarter-year is one quarter of {@code 365.2425 Days}.
   * <p>
   * This unit is an immutable and thread-safe singleton.
   */
  final val QUARTER_YEARS: TemporalUnit = Unit.QUARTER_YEARS

  /**
   * Implementation of the field.
   */
  private object Field {
    private def isIso(temporal: TemporalAccessor): Boolean = {
      return Chronology.from(temporal) == IsoChronology.INSTANCE
    }

    private def getWeekRange(date: Nothing): ValueRange = {
      val wby: Int = getWeekBasedYear(date)
      date = date.withDayOfYear(1).withYear(wby)
      if (date.getDayOfWeek eq THURSDAY || (date.getDayOfWeek eq WEDNESDAY && date.isLeapYear)) {
        return ValueRange.of(1, 53)
      }
      return ValueRange.of(1, 52)
    }

    private def getWeek(date: Nothing): Int = {
      val dow0: Int = date.getDayOfWeek.ordinal
      val doy0: Int = date.getDayOfYear - 1
      val doyThu0: Int = doy0 + (3 - dow0)
      val alignedWeek: Int = doyThu0 / 7
      val firstThuDoy0: Int = doyThu0 - (alignedWeek * 7)
      var firstMonDoy0: Int = firstThuDoy0 - 3
      if (firstMonDoy0 < -3) {
        firstMonDoy0 += 7
      }
      if (doy0 < firstMonDoy0) {
        return getWeekRange(date.withDayOfYear(180).minusYears(1)).getMaximum.asInstanceOf[Int]
      }
      var week: Int = ((doy0 - firstMonDoy0) / 7) + 1
      if (week == 53) {
        if ((firstMonDoy0 == -3 || (firstMonDoy0 == -2 && date.isLeapYear)) == false) {
          week = 1
        }
      }
      return week
    }

    private def getWeekBasedYear(date: Nothing): Int = {
      var year: Int = date.getYear
      var doy: Int = date.getDayOfYear
      if (doy <= 3) {
        val dow: Int = date.getDayOfWeek.ordinal
        if (doy - dow < -2) {
          year -= 1
        }
      }
      else if (doy >= 363) {
        val dow: Int = date.getDayOfWeek.ordinal
        doy = doy - 363 - (if (date.isLeapYear) 1 else 0)
        if (doy - dow >= 0) {
          year += 1
        }
      }
      return year
    }

    final val DAY_OF_QUARTER: = null
    final val QUARTER_OF_YEAR: = null
    final val WEEK_OF_WEEK_BASED_YEAR: = null
    final val WEEK_BASED_YEAR: = null
    private final val QUARTER_DAYS: Array[Int] = Array(0, 90, 181, 273, 0, 91, 182, 274)
  }

  private class Field extends TemporalField {
    def isDateBased: Boolean = {
      return true
    }

    def isTimeBased: Boolean = {
      return false
    }

    def rangeRefinedBy(temporal: TemporalAccessor): ValueRange = {
      return range
    }
  }

  /**
   * Implementation of the period unit.
   */
  private final object Unit {
    /**
     * Unit that represents the concept of a week-based-year.
     */
    final val WEEK_BASED_YEARS: = null
    /**
     * Unit that represents the concept of a quarter-year.
     */
    final val QUARTER_YEARS: = null
  }

  private final class Unit extends TemporalUnit {
    private def this(name: String, estimatedDuration: Nothing) {
      this()
      this.name = name
      this.duration = estimatedDuration
    }

    def getDuration: Nothing = {
      return duration
    }

    def isDurationEstimated: Boolean = {
      return true
    }

    def isDateBased: Boolean = {
      return true
    }

    def isTimeBased: Boolean = {
      return false
    }

    override def isSupportedBy(temporal: Temporal): Boolean = {
      return temporal.isSupported(EPOCH_DAY)
    }

    @SuppressWarnings(Array("unchecked")) def addTo(temporal: R, amount: Long): R = {
      this match {
        case WEEK_BASED_YEARS =>
          return temporal.`with`(WEEK_BASED_YEAR, Math.addExact(temporal.get(WEEK_BASED_YEAR), amount)).asInstanceOf[R]
        case QUARTER_YEARS =>
          return temporal.plus(amount / 256, YEARS).plus((amount % 256) * 3, MONTHS).asInstanceOf[R]
        case _ =>
          throw new IllegalStateException("Unreachable")
      }
    }

    def between(temporal1Inclusive: Temporal, temporal2Exclusive: Temporal): Long = {
      if (temporal1Inclusive.getClass ne temporal2Exclusive.getClass) {
        return temporal1Inclusive.until(temporal2Exclusive, this)
      }
      this match {
        case WEEK_BASED_YEARS =>
          return Math.subtractExact(temporal2Exclusive.getLong(WEEK_BASED_YEAR), temporal1Inclusive.getLong(WEEK_BASED_YEAR))
        case QUARTER_YEARS =>
          return temporal1Inclusive.until(temporal2Exclusive, MONTHS) / 3
        case _ =>
          throw new IllegalStateException("Unreachable")
      }
    }

    override def toString: String = {
      return name
    }

    private final val name: String = null
    private final val duration: Nothing = null
  }

}

final class IsoFields {
  /**
   * Restricted constructor.
   */
  private def this() {
    this()
    throw new AssertionError("Not instantiable")
  }
}

/*
 * Copyright (c) 2012, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
/*
 * This file is available under and governed by the GNU General Public
 * License version 2 only, as published by the Free Software Foundation.
 * However, the following notice accompanied the original version of this
 * file:
 *
 * Copyright (c) 2012, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/**
 * A set of date fields that provide access to Julian Days.
 * <p>
 * The Julian Day is a standard way of expressing date and time commonly used in the scientific community.
 * It is expressed as a decimal number of whole days where days start at midday.
 * This class represents variations on Julian Days that count whole days from midnight.
 * <p>
 * The fields are implemented relative to {@link ChronoField#EPOCH_DAY EPOCH_DAY}.
 * The fields are supported, and can be queried and set if {@code EPOCH_DAY} is available.
 * The fields work with all chronologies.
 *
 * @implSpec
 * This is an immutable and thread-safe class.
 *
 * @since 1.8
 */
final object JulianFields {
  /**
   * The offset from Julian to EPOCH DAY.
   */
  private final val JULIAN_DAY_OFFSET: Long = 2440588L
  /**
   * Julian Day field.
   * <p>
   * This is an integer-based version of the Julian Day Number.
   * Julian Day is a well-known system that represents the count of whole days since day 0,
   * which is defined to be January 1, 4713 BCE in the Julian calendar, and -4713-11-24 Gregorian.
   * The field  has "JulianDay" as 'name', and 'DAYS' as 'baseUnit'.
   * The field always refers to the local date-time, ignoring the offset or zone.
   * <p>
   * For date-times, 'JULIAN_DAY.getFrom()' assumes the same value from
   * midnight until just before the next midnight.
   * When 'JULIAN_DAY.adjustInto()' is applied to a date-time, the time of day portion remains unaltered.
   * 'JULIAN_DAY.adjustInto()' and 'JULIAN_DAY.getFrom()' only apply to {@code Temporal} objects that
   * can be converted into {@link ChronoField#EPOCH_DAY}.
   * An {@link UnsupportedTemporalTypeException} is thrown for any other type of object.
   * <p>
   * In the resolving phase of parsing, a date can be created from a Julian Day field.
   * In {@linkplain ResolverStyle#STRICT strict mode} and {@linkplain ResolverStyle#SMART smart mode}
   * the Julian Day value is validated against the range of valid values.
   * In {@linkplain ResolverStyle#LENIENT lenient mode} no validation occurs.
   * <p>
   * <h3>Astronomical and Scientific Notes</h3>
   * The standard astronomical definition uses a fraction to indicate the time-of-day,
   * thus 3.25 would represent the time 18:00, since days start at midday.
   * This implementation uses an integer and days starting at midnight.
   * The integer value for the Julian Day Number is the astronomical Julian Day value at midday
   * of the date in question.
   * This amounts to the astronomical Julian Day, rounded to an integer {@code JDN = floor(JD + 0.5)}.
   * <p>
   * <pre>
   * | ISO date          |  Julian Day Number | Astronomical Julian Day |
   * | 1970-01-01T00:00  |         2,440,588  |         2,440,587.5     |
   * | 1970-01-01T06:00  |         2,440,588  |         2,440,587.75    |
   * | 1970-01-01T12:00  |         2,440,588  |         2,440,588.0     |
   * | 1970-01-01T18:00  |         2,440,588  |         2,440,588.25    |
   * | 1970-01-02T00:00  |         2,440,589  |         2,440,588.5     |
   * | 1970-01-02T06:00  |         2,440,589  |         2,440,588.75    |
   * | 1970-01-02T12:00  |         2,440,589  |         2,440,589.0     |
   * </pre>
   * <p>
   * Julian Days are sometimes taken to imply Universal Time or UTC, but this
   * implementation always uses the Julian Day number for the local date,
   * regardless of the offset or time-zone.
   */
  final val JULIAN_DAY: TemporalField = Field.JULIAN_DAY
  /**
   * Modified Julian Day field.
   * <p>
   * This is an integer-based version of the Modified Julian Day Number.
   * Modified Julian Day (MJD) is a well-known system that counts days continuously.
   * It is defined relative to astronomical Julian Day as  {@code MJD = JD - 2400000.5}.
   * Each Modified Julian Day runs from midnight to midnight.
   * The field always refers to the local date-time, ignoring the offset or zone.
   * <p>
   * For date-times, 'MODIFIED_JULIAN_DAY.getFrom()' assumes the same value from
   * midnight until just before the next midnight.
   * When 'MODIFIED_JULIAN_DAY.adjustInto()' is applied to a date-time, the time of day portion remains unaltered.
   * 'MODIFIED_JULIAN_DAY.adjustInto()' and 'MODIFIED_JULIAN_DAY.getFrom()' only apply to {@code Temporal} objects
   * that can be converted into {@link ChronoField#EPOCH_DAY}.
   * An {@link UnsupportedTemporalTypeException} is thrown for any other type of object.
   * <p>
   * This implementation is an integer version of MJD with the decimal part rounded to floor.
   * <p>
   * In the resolving phase of parsing, a date can be created from a Modified Julian Day field.
   * In {@linkplain ResolverStyle#STRICT strict mode} and {@linkplain ResolverStyle#SMART smart mode}
   * the Modified Julian Day value is validated against the range of valid values.
   * In {@linkplain ResolverStyle#LENIENT lenient mode} no validation occurs.
   * <p>
   * <h3>Astronomical and Scientific Notes</h3>
   * <pre>
   * | ISO date          | Modified Julian Day |      Decimal MJD |
   * | 1970-01-01T00:00  |             40,587  |       40,587.0   |
   * | 1970-01-01T06:00  |             40,587  |       40,587.25  |
   * | 1970-01-01T12:00  |             40,587  |       40,587.5   |
   * | 1970-01-01T18:00  |             40,587  |       40,587.75  |
   * | 1970-01-02T00:00  |             40,588  |       40,588.0   |
   * | 1970-01-02T06:00  |             40,588  |       40,588.25  |
   * | 1970-01-02T12:00  |             40,588  |       40,588.5   |
   * </pre>
   * <p>
   * Modified Julian Days are sometimes taken to imply Universal Time or UTC, but this
   * implementation always uses the Modified Julian Day for the local date,
   * regardless of the offset or time-zone.
   */
  final val MODIFIED_JULIAN_DAY: TemporalField = Field.MODIFIED_JULIAN_DAY
  /**
   * Rata Die field.
   * <p>
   * Rata Die counts whole days continuously starting day 1 at midnight at the beginning of 0001-01-01 (ISO).
   * The field always refers to the local date-time, ignoring the offset or zone.
   * <p>
   * For date-times, 'RATA_DIE.getFrom()' assumes the same value from
   * midnight until just before the next midnight.
   * When 'RATA_DIE.adjustInto()' is applied to a date-time, the time of day portion remains unaltered.
   * 'RATA_DIE.adjustInto()' and 'RATA_DIE.getFrom()' only apply to {@code Temporal} objects
   * that can be converted into {@link ChronoField#EPOCH_DAY}.
   * An {@link UnsupportedTemporalTypeException} is thrown for any other type of object.
   * <p>
   * In the resolving phase of parsing, a date can be created from a Rata Die field.
   * In {@linkplain ResolverStyle#STRICT strict mode} and {@linkplain ResolverStyle#SMART smart mode}
   * the Rata Die value is validated against the range of valid values.
   * In {@linkplain ResolverStyle#LENIENT lenient mode} no validation occurs.
   */
  final val RATA_DIE: TemporalField = Field.RATA_DIE

  /**
   * Implementation of JulianFields.  Each instance is a singleton.
   */
  private final object Field {
    final val JULIAN_DAY: = null
    final val MODIFIED_JULIAN_DAY: = null
    final val RATA_DIE: = null
    private final val serialVersionUID: Long = -7501623920830201812L
  }

  private final class Field extends TemporalField {
    private def this(name: String, baseUnit: TemporalUnit, rangeUnit: TemporalUnit, offset: Long) {
      this()
      this.name = name
      this.baseUnit = baseUnit
      this.rangeUnit = rangeUnit
      this.range = ValueRange.of(-365243219162L + offset, 365241780471L + offset)
      this.offset = offset
    }

    def getBaseUnit: TemporalUnit = {
      return baseUnit
    }

    def getRangeUnit: TemporalUnit = {
      return rangeUnit
    }

    def isDateBased: Boolean = {
      return true
    }

    def isTimeBased: Boolean = {
      return false
    }

    def range: ValueRange = {
      return range
    }

    def isSupportedBy(temporal: TemporalAccessor): Boolean = {
      return temporal.isSupported(EPOCH_DAY)
    }

    def rangeRefinedBy(temporal: TemporalAccessor): ValueRange = {
      if (isSupportedBy(temporal) == false) {
        throw new Nothing("Unsupported field: " + this)
      }
      return range
    }

    def getFrom(temporal: TemporalAccessor): Long = {
      return temporal.getLong(EPOCH_DAY) + offset
    }

    @SuppressWarnings(Array("unchecked")) def adjustInto(temporal: R, newValue: Long): R = {
      if (range.isValidValue(newValue) == false) {
        throw new Nothing("Invalid value: " + name + " " + newValue)
      }
      return temporal.`with`(EPOCH_DAY, Math.subtractExact(newValue, offset)).asInstanceOf[R]
    }

    override def resolve(fieldValues: Nothing, chronology: Chronology, zone: Nothing, resolverStyle: Nothing): ChronoLocalDate = {
      val value: Long = fieldValues.remove(this)
      if (resolverStyle eq ResolverStyle.LENIENT) {
        return chronology.dateEpochDay(Math.subtractExact(value, offset))
      }
      range.checkValidValue(value, this)
      return chronology.dateEpochDay(value - offset)
    }

    override def toString: String = {
      return name
    }

    @transient
    private final val name: String = null
    @transient
    private final val baseUnit: TemporalUnit = null
    @transient
    private final val rangeUnit: TemporalUnit = null
    @transient
    private final val range: ValueRange = null
    @transient
    private final val offset: Long = 0L
  }

}

final class JulianFields {
  /**
   * Restricted constructor.
   */
  private def this() {
    this()
    throw new AssertionError("Not instantiable")
  }
}

/*
 * Copyright (c) 2012, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
/*
 * This file is available under and governed by the GNU General Public
 * License version 2 only, as published by the Free Software Foundation.
 * However, the following notice accompanied the original version of this
 * file:
 *
 * Copyright (c) 2012, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/**
 * Framework-level interface defining read-write access to a temporal object,
 * such as a date, time, offset or some combination of these.
 * <p>
 * This is the base interface type for date, time and offset objects that
 * are complete enough to be manipulated using plus and minus.
 * It is implemented by those classes that can provide and manipulate information
 * as {@linkplain TemporalField fields} or {@linkplain TemporalQuery queries}.
 * See {@link TemporalAccessor} for the read-only version of this interface.
 * <p>
 * Most date and time information can be represented as a number.
 * These are modeled using {@code TemporalField} with the number held using
 * a {@code long} to handle large values. Year, month and day-of-month are
 * simple examples of fields, but they also include instant and offsets.
 * See {@link ChronoField} for the standard set of fields.
 * <p>
 * Two pieces of date/time information cannot be represented by numbers,
 * the {@linkplain java.time.chrono.Chronology chronology} and the
 * {@linkplain java.time.ZoneId time-zone}.
 * These can be accessed via {@link #query(TemporalQuery) queries} using
 * the static methods defined on {@link TemporalQuery}.
 * <p>
 * This interface is a framework-level interface that should not be widely
 * used in application code. Instead, applications should create and pass
 * around instances of concrete types, such as {@code Date}.
 * There are many reasons for this, part of which is that implementations
 * of this interface may be in calendar systems other than ISO.
 * See {@link java.time.chrono.ChronoLocalDate} for a fuller discussion of the issues.
 *
 * <h3>When to implement</h3>
 * <p>
 * A class should implement this interface if it meets three criteria:
 * <p><ul>
 * <li>it provides access to date/time/offset information, as per {@code TemporalAccessor}
 * <li>the set of fields are contiguous from the largest to the smallest
 * <li>the set of fields are complete, such that no other field is needed to define the
 * valid range of values for the fields that are represented
 * </ul><p>
 * <p>
 * Four examples make this clear:
 * <p><ul>
 * <li>{@code Date} implements this interface as it represents a set of fields
 * that are contiguous from days to forever and require no external information to determine
 * the validity of each date. It is therefore able to implement plus/minus correctly.
 * <li>{@code Time} implements this interface as it represents a set of fields
 * that are contiguous from nanos to within days and require no external information to determine
 * validity. It is able to implement plus/minus correctly, by wrapping around the day.
 * <li>{@code MonthDay}, the combination of month-of-year and day-of-month, does not implement
 * this interface.  While the combination is contiguous, from days to months within years,
 * the combination does not have sufficient information to define the valid range of values
 * for day-of-month.  As such, it is unable to implement plus/minus correctly.
 * <li>The combination day-of-week and day-of-month ("Friday the 13th") should not implement
 * this interface. It does not represent a contiguous set of fields, as days to weeks overlaps
 * days to months.
 * </ul><p>
 *
 * @implSpec
 * This interface places no restrictions on the mutability of implementations,
 * however immutability is strongly recommended.
 * All implementations must be { @link Comparable}.
 *
 * @since 1.8
 */
abstract trait Temporal extends TemporalAccessor {
  /**
   * Checks if the specified unit is supported.
   * <p>
   * This checks if the specified unit can be added to, or subtracted from, this date-time.
   * If false, then calling the {@link #plus(long, TemporalUnit)} and
   * {@link #minus(long, TemporalUnit) minus} methods will throw an exception.
   *
   * @implSpec
     * Implementations must check and handle all units defined in { @link ChronoUnit}.
   *                                                                      If the unit is supported, then true must be returned, otherwise false must be returned.
   *                                                                      <p>
   *                                                                      If the field is not a { @code ChronoUnit}, then the result of this method
   *                                                                                                    is obtained by invoking { @code TemporalUnit.isSupportedBy(Temporal)}
   *                                                                                                                                    passing { @code this} as the argument.
   *                                                                                                                                                    <p>
   *                                                                                                                                                    Implementations must ensure that no observable state is altered when this
   *                                                                                                                                                    read-only method is invoked.
   *
   * @param unit  the unit to check, null returns false
   * @return true if the unit can be added/subtracted, false if not
   */
  def isSupported(unit: TemporalUnit): Boolean

  /**
   * Returns an adjusted object of the same type as this object with the adjustment made.
   * <p>
   * This adjusts this date-time according to the rules of the specified adjuster.
   * A simple adjuster might simply set the one of the fields, such as the year field.
   * A more complex adjuster might set the date to the last day of the month.
   * A selection of common adjustments is provided in {@link TemporalAdjuster}.
   * These include finding the "last day of the month" and "next Wednesday".
   * The adjuster is responsible for handling special cases, such as the varying
   * lengths of month and leap years.
   * <p>
   * Some example code indicating how and why this method is used:
   * <pre>
   * date = date.with(Month.JULY);        // most key classes implement TemporalAdjuster
   * date = date.with(lastDayOfMonth());  // static import from Adjusters
   * date = date.with(next(WEDNESDAY));   // static import from Adjusters and DayOfWeek
   * </pre>
   *
   * @implSpec
     * <p>
   *   Implementations must not alter either this object or the specified temporal object.
   *   Instead, an adjusted copy of the original must be returned.
   *   This provides equivalent, safe behavior for immutable and mutable implementations.
   *   <p>
   *   The default implementation must behave equivalent to this code:
   *   <pre>
   *   return adjuster.adjustInto(this);
   *   </pre>
   *
   * @param adjuster  the adjuster to use, not null
   * @return an object of the same type with the specified adjustment made, not null
   * @throws DateTimeException if unable to make the adjustment
   * @throws ArithmeticException if numeric overflow occurs
   */
  def `with`(adjuster: TemporalAdjuster): Temporal = {
    return adjuster.adjustInto(this)
  }

  /**
   * Returns an object of the same type as this object with the specified field altered.
   * <p>
   * This returns a new object based on this one with the value for the specified field changed.
   * For example, on a {@code Date}, this could be used to set the year, month or day-of-month.
   * The returned object will have the same observable type as this object.
   * <p>
   * In some cases, changing a field is not fully defined. For example, if the target object is
   * a date representing the 31st January, then changing the month to February would be unclear.
   * In cases like this, the field is responsible for resolving the result. Typically it will choose
   * the previous valid date, which would be the last valid day of February in this example.
   *
   * @implSpec
     * Implementations must check and handle all fields defined in { @link ChronoField}.
   *                                                                       If the field is supported, then the adjustment must be performed.
   *                                                                       If unsupported, then an { @code UnsupportedTemporalTypeException} must be thrown.
   *                                                                                                       <p>
   *                                                                                                       If the field is not a { @code ChronoField}, then the result of this method
   *                                                                                                                                     is obtained by invoking { @code TemporalField.adjustInto(Temporal, long)}
   *                                                                                                                                                                     passing { @code this} as the first argument.
   *                                                                                                                                                                                     <p>
   *                                                                                                                                                                                     Implementations must not alter this object.
   *                                                                                                                                                                                     Instead, an adjusted copy of the original must be returned.
   *                                                                                                                                                                                     This provides equivalent, safe behavior for immutable and mutable implementations.
   *
   * @param field  the field to set in the result, not null
   * @param newValue  the new value of the field in the result
   * @return an object of the same type with the specified field set, not null
   * @throws DateTimeException if the field cannot be set
   * @throws UnsupportedTemporalTypeException if the field is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  def `with`(field: TemporalField, newValue: Long): Temporal

  /**
   * Returns an object of the same type as this object with an amount added.
   * <p>
   * This adjusts this temporal, adding according to the rules of the specified amount.
   * The amount is typically a {@link java.time.Period} but may be any other type implementing
   * the {@link TemporalAmount} interface, such as {@link java.time.Duration}.
   * <p>
   * Some example code indicating how and why this method is used:
   * <pre>
   * date = date.plus(period);                // add a Period instance
   * date = date.plus(duration);              // add a Duration instance
   * date = date.plus(workingDays(6));        // example user-written workingDays method
   * </pre>
   * <p>
   * Note that calling {@code plus} followed by {@code minus} is not guaranteed to
   * return the same date-time.
   *
   * @implSpec
     * <p>
   *   Implementations must not alter either this object or the specified temporal object.
   *   Instead, an adjusted copy of the original must be returned.
   *   This provides equivalent, safe behavior for immutable and mutable implementations.
   *   <p>
   *   The default implementation must behave equivalent to this code:
   *   <pre>
   *   return amount.addTo(this);
   *   </pre>
   *
   * @param amount  the amount to add, not null
   * @return an object of the same type with the specified adjustment made, not null
   * @throws DateTimeException if the addition cannot be made
   * @throws ArithmeticException if numeric overflow occurs
   */
  def plus(amount: TemporalAmount): Temporal = {
    return amount.addTo(this)
  }

  /**
   * Returns an object of the same type as this object with the specified period added.
   * <p>
   * This method returns a new object based on this one with the specified period added.
   * For example, on a {@code Date}, this could be used to add a number of years, months or days.
   * The returned object will have the same observable type as this object.
   * <p>
   * In some cases, changing a field is not fully defined. For example, if the target object is
   * a date representing the 31st January, then adding one month would be unclear.
   * In cases like this, the field is responsible for resolving the result. Typically it will choose
   * the previous valid date, which would be the last valid day of February in this example.
   *
   * @implSpec
     * Implementations must check and handle all units defined in { @link ChronoUnit}.
   *                                                                      If the unit is supported, then the addition must be performed.
   *                                                                      If unsupported, then an { @code UnsupportedTemporalTypeException} must be thrown.
   *                                                                                                      <p>
   *                                                                                                      If the unit is not a { @code ChronoUnit}, then the result of this method
   *                                                                                                                                   is obtained by invoking { @code TemporalUnit.addTo(Temporal, long)}
   *                                                                                                                                                                   passing { @code this} as the first argument.
   *                                                                                                                                                                                   <p>
   *                                                                                                                                                                                   Implementations must not alter this object.
   *                                                                                                                                                                                   Instead, an adjusted copy of the original must be returned.
   *                                                                                                                                                                                   This provides equivalent, safe behavior for immutable and mutable implementations.
   *
   * @param amountToAdd  the amount of the specified unit to add, may be negative
   * @param unit  the unit of the period to add, not null
   * @return an object of the same type with the specified period added, not null
   * @throws DateTimeException if the unit cannot be added
   * @throws UnsupportedTemporalTypeException if the unit is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  def plus(amountToAdd: Long, unit: TemporalUnit): Temporal

  /**
   * Returns an object of the same type as this object with an amount subtracted.
   * <p>
   * This adjusts this temporal, subtracting according to the rules of the specified amount.
   * The amount is typically a {@link java.time.Period} but may be any other type implementing
   * the {@link TemporalAmount} interface, such as {@link java.time.Duration}.
   * <p>
   * Some example code indicating how and why this method is used:
   * <pre>
   * date = date.minus(period);               // subtract a Period instance
   * date = date.minus(duration);             // subtract a Duration instance
   * date = date.minus(workingDays(6));       // example user-written workingDays method
   * </pre>
   * <p>
   * Note that calling {@code plus} followed by {@code minus} is not guaranteed to
   * return the same date-time.
   *
   * @implSpec
     * <p>
   *   Implementations must not alter either this object or the specified temporal object.
   *   Instead, an adjusted copy of the original must be returned.
   *   This provides equivalent, safe behavior for immutable and mutable implementations.
   *   <p>
   *   The default implementation must behave equivalent to this code:
   *   <pre>
   *   return amount.subtractFrom(this);
   *   </pre>
   *
   * @param amount  the amount to subtract, not null
   * @return an object of the same type with the specified adjustment made, not null
   * @throws DateTimeException if the subtraction cannot be made
   * @throws ArithmeticException if numeric overflow occurs
   */
  def minus(amount: TemporalAmount): Temporal = {
    return amount.subtractFrom(this)
  }

  /**
   * Returns an object of the same type as this object with the specified period subtracted.
   * <p>
   * This method returns a new object based on this one with the specified period subtracted.
   * For example, on a {@code Date}, this could be used to subtract a number of years, months or days.
   * The returned object will have the same observable type as this object.
   * <p>
   * In some cases, changing a field is not fully defined. For example, if the target object is
   * a date representing the 31st March, then subtracting one month would be unclear.
   * In cases like this, the field is responsible for resolving the result. Typically it will choose
   * the previous valid date, which would be the last valid day of February in this example.
   *
   * @implSpec
     * Implementations must behave in a manor equivalent to the default method behavior.
   *   <p>
   *   Implementations must not alter this object.
   *   Instead, an adjusted copy of the original must be returned.
   *   This provides equivalent, safe behavior for immutable and mutable implementations.
   *   <p>
   *   The default implementation must behave equivalent to this code:
   *   <pre>
   *   return (amountToSubtract == Long.MIN_VALUE ?
   *   plus(Long.MAX_VALUE, unit).plus(1, unit) : plus(-amountToSubtract, unit));
   *   </pre>
   *
   * @param amountToSubtract  the amount of the specified unit to subtract, may be negative
   * @param unit  the unit of the period to subtract, not null
   * @return an object of the same type with the specified period subtracted, not null
   * @throws DateTimeException if the unit cannot be subtracted
   * @throws UnsupportedTemporalTypeException if the unit is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  def minus(amountToSubtract: Long, unit: TemporalUnit): Temporal = {
    return (if (amountToSubtract == Long.MIN_VALUE) plus(Long.MAX_VALUE, unit).plus(1, unit) else plus(-amountToSubtract, unit))
  }

  /**
   * Calculates the amount of time until another temporal in terms of the specified unit.
   * <p>
   * This calculates the amount of time between two temporal objects
   * in terms of a single {@code TemporalUnit}.
   * The start and end points are {@code this} and the specified temporal.
   * The end point is converted to be of the same type as the start point if different.
   * The result will be negative if the end is before the start.
   * For example, the period in hours between two temporal objects can be
   * calculated using {@code startTime.until(endTime, HOURS)}.
   * <p>
   * The calculation returns a whole number, representing the number of
   * complete units between the two temporals.
   * For example, the period in hours between the times 11:30 and 13:29
   * will only be one hour as it is one minute short of two hours.
   * <p>
   * There are two equivalent ways of using this method.
   * The first is to invoke this method directly.
   * The second is to use {@link TemporalUnit#between(Temporal, Temporal)}:
   * <pre>
   * // these two lines are equivalent
   * temporal = start.until(end, unit);
   * temporal = unit.between(start, end);
   * </pre>
   * The choice should be made based on which makes the code more readable.
   * <p>
   * For example, this method allows the number of days between two dates to
   * be calculated:
   * <pre>
   * long daysBetween = start.until(end, DAYS);
   * // or alternatively
   * long daysBetween = DAYS.between(start, end);
   * </pre>
   *
   * @implSpec
     * Implementations must begin by checking to ensure that the input temporal
   *   object is of the same observable type as the implementation.
   *   They must then perform the calculation for all instances of { @link ChronoUnit}.
   *                                                                       An { @code UnsupportedTemporalTypeException} must be thrown for { @code ChronoUnit}
   *                                                                                                                                               instances that are unsupported.
   *                                                                                                                                               <p>
   *                                                                                                                                               If the unit is not a { @code ChronoUnit}, then the result of this method
   *                                                                                                                                                                            is obtained by invoking { @code TemporalUnit.between(Temporal, Temporal)}
   *                                                                                                                                                                                                            passing { @code this} as the first argument and the input temporal as
   *                                                                                                                                                                                                                            the second argument.
   *                                                                                                                                                                                                                            <p>
   *                                                                                                                                                                                                                            In summary, implementations must behave in a manner equivalent to this pseudo-code:
   *                                                                                                                                                                                                                            <pre>
   *                                                                                                                                                                                                                            // convert the end temporal to the same type as this class
   *                                                                                                                                                                                                                            if (unit instanceof ChronoUnit) {
   *                                                                                                                                                                                                                            // if unit is supported, then calculate and return result
   *                                                                                                                                                                                                                            // else throw UnsupportedTemporalTypeException for unsupported units
   *                                                                                                                                                                                                                            }
   *                                                                                                                                                                                                                            return unit.between(this, convertedEndTemporal);
   *                                                                                                                                                                                                                            </pre>
   *                                                                                                                                                                                                                            <p>
   *                                                                                                                                                                                                                            Note that the unit's { @code between} method must only be invoked if the
   *                                                                                                                                                                                                                                                         two temporal objects have exactly the same type evaluated by { @code getClass()}.
   *                                                                                                                                                                                                                                                                                                                              <p>
   *                                                                                                                                                                                                                                                                                                                              Implementations must ensure that no observable state is altered when this
   *                                                                                                                                                                                                                                                                                                                              read-only method is invoked.
   *
   * @param endExclusive  the end temporal, converted to be of the
   *                      same type as this object, not null
   * @param unit  the unit to measure the amount in, not null
   * @return the amount of time between this temporal object and the specified one
   *         in terms of the unit; positive if the specified object is later than this one,
   *         negative if it is earlier than this one
   * @throws DateTimeException if the amount cannot be calculated, or the end
   *                           temporal cannot be converted to the same type as this temporal
   * @throws UnsupportedTemporalTypeException if the unit is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  def until(endExclusive: Temporal, unit: TemporalUnit): Long
}

/*
 * Copyright (c) 2012, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
/*
 * This file is available under and governed by the GNU General Public
 * License version 2 only, as published by the Free Software Foundation.
 * However, the following notice accompanied the original version of this
 * file:
 *
 * Copyright (c) 2012, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/**
 * Framework-level interface defining read-only access to a temporal object,
 * such as a date, time, offset or some combination of these.
 * <p>
 * This is the base interface type for date, time and offset objects.
 * It is implemented by those classes that can provide information
 * as {@linkplain TemporalField fields} or {@linkplain TemporalQuery queries}.
 * <p>
 * Most date and time information can be represented as a number.
 * These are modeled using {@code TemporalField} with the number held using
 * a {@code long} to handle large values. Year, month and day-of-month are
 * simple examples of fields, but they also include instant and offsets.
 * See {@link ChronoField} for the standard set of fields.
 * <p>
 * Two pieces of date/time information cannot be represented by numbers,
 * the {@linkplain java.time.chrono.Chronology chronology} and the
 * {@linkplain java.time.ZoneId time-zone}.
 * These can be accessed via {@linkplain #query(TemporalQuery) queries} using
 * the static methods defined on {@link TemporalQuery}.
 * <p>
 * A sub-interface, {@link Temporal}, extends this definition to one that also
 * supports adjustment and manipulation on more complete temporal objects.
 * <p>
 * This interface is a framework-level interface that should not be widely
 * used in application code. Instead, applications should create and pass
 * around instances of concrete types, such as {@code Date}.
 * There are many reasons for this, part of which is that implementations
 * of this interface may be in calendar systems other than ISO.
 * See {@link java.time.chrono.ChronoLocalDate} for a fuller discussion of the issues.
 *
 * @implSpec
 * This interface places no restrictions on the mutability of implementations,
 * however immutability is strongly recommended.
 *
 * @since 1.8
 */
abstract trait TemporalAccessor {
  /**
   * Checks if the specified field is supported.
   * <p>
   * This checks if the date-time can be queried for the specified field.
   * If false, then calling the {@link #range(TemporalField) range} and {@link #get(TemporalField) get}
   * methods will throw an exception.
   *
   * @implSpec
     * Implementations must check and handle all fields defined in { @link ChronoField}.
   *                                                                       If the field is supported, then true must be returned, otherwise false must be returned.
   *                                                                       <p>
   *                                                                       If the field is not a { @code ChronoField}, then the result of this method
   *                                                                                                     is obtained by invoking { @code TemporalField.isSupportedBy(TemporalAccessor)}
   *                                                                                                                                     passing { @code this} as the argument.
   *                                                                                                                                                     <p>
   *                                                                                                                                                     Implementations must ensure that no observable state is altered when this
   *                                                                                                                                                     read-only method is invoked.
   *
   * @param field  the field to check, null returns false
   * @return true if this date-time can be queried for the field, false if not
   */
  def isSupported(field: TemporalField): Boolean

  /**
   * Gets the range of valid values for the specified field.
   * <p>
   * All fields can be expressed as a {@code long} integer.
   * This method returns an object that describes the valid range for that value.
   * The value of this temporal object is used to enhance the accuracy of the returned range.
   * If the date-time cannot return the range, because the field is unsupported or for
   * some other reason, an exception will be thrown.
   * <p>
   * Note that the result only describes the minimum and maximum valid values
   * and it is important not to read too much into them. For example, there
   * could be values within the range that are invalid for the field.
   *
   * @implSpec
     * Implementations must check and handle all fields defined in { @link ChronoField}.
   *                                                                       If the field is supported, then the range of the field must be returned.
   *                                                                       If unsupported, then an { @code UnsupportedTemporalTypeException} must be thrown.
   *                                                                                                       <p>
   *                                                                                                       If the field is not a { @code ChronoField}, then the result of this method
   *                                                                                                                                     is obtained by invoking { @code TemporalField.rangeRefinedBy(TemporalAccessorl)}
   *                                                                                                                                                                     passing { @code this} as the argument.
   *                                                                                                                                                                                     <p>
   *                                                                                                                                                                                     Implementations must ensure that no observable state is altered when this
   *                                                                                                                                                                                     read-only method is invoked.
   *                                                                                                                                                                                     <p>
   *                                                                                                                                                                                     The default implementation must behave equivalent to this code:
   *                                                                                                                                                                                     <pre>
   *                                                                                                                                                                                     if (field instanceof ChronoField) {
   *                                                                                                                                                                                     if (isSupported(field)) {
   *                                                                                                                                                                                     return field.range();
   *                                                                                                                                                                                     }
   *                                                                                                                                                                                     throw new UnsupportedTemporalTypeException("Unsupported field: " + field);
   *                                                                                                                                                                                     }
   *                                                                                                                                                                                     return field.rangeRefinedBy(this);
   *                                                                                                                                                                                     </pre>
   *
   * @param field  the field to query the range for, not null
   * @return the range of valid values for the field, not null
   * @throws DateTimeException if the range for the field cannot be obtained
   * @throws UnsupportedTemporalTypeException if the field is not supported
   */
  def range(field: TemporalField): ValueRange = {
    if (field.isInstanceOf[ChronoField]) {
      if (isSupported(field)) {
        return field.range
      }
      throw new UnsupportedTemporalTypeException("Unsupported field: " + field)
    }
    Objects.requireNonNull(field, "field")
    return field.rangeRefinedBy(this)
  }

  /**
   * Gets the value of the specified field as an {@code int}.
   * <p>
   * This queries the date-time for the value for the specified field.
   * The returned value will always be within the valid range of values for the field.
   * If the date-time cannot return the value, because the field is unsupported or for
   * some other reason, an exception will be thrown.
   *
   * @implSpec
     * Implementations must check and handle all fields defined in { @link ChronoField}.
   *                                                                       If the field is supported and has an { @code int} range, then the value of
   *                                                                                                                    the field must be returned.
   *                                                                                                                    If unsupported, then an { @code UnsupportedTemporalTypeException} must be thrown.
   *                                                                                                                                                    <p>
   *                                                                                                                                                    If the field is not a { @code ChronoField}, then the result of this method
   *                                                                                                                                                                                  is obtained by invoking { @code TemporalField.getFrom(TemporalAccessor)}
   *                                                                                                                                                                                                                  passing { @code this} as the argument.
   *                                                                                                                                                                                                                                  <p>
   *                                                                                                                                                                                                                                  Implementations must ensure that no observable state is altered when this
   *                                                                                                                                                                                                                                  read-only method is invoked.
   *                                                                                                                                                                                                                                  <p>
   *                                                                                                                                                                                                                                  The default implementation must behave equivalent to this code:
   *                                                                                                                                                                                                                                  <pre>
   *                                                                                                                                                                                                                                  if (range(field).isIntValue()) {
   *                                                                                                                                                                                                                                  return range(field).checkValidIntValue(getLong(field), field);
   *                                                                                                                                                                                                                                  }
   *                                                                                                                                                                                                                                  throw new UnsupportedTemporalTypeException("Invalid field " + field + " + for get() method, use getLong() instead");
   *                                                                                                                                                                                                                                  </pre>
   *
   * @param field  the field to get, not null
   * @return the value for the field, within the valid range of values
   * @throws DateTimeException if a value for the field cannot be obtained or
   *                           the value is outside the range of valid values for the field
   * @throws UnsupportedTemporalTypeException if the field is not supported or
   *                                          the range of values exceeds an { @code int}
   * @throws ArithmeticException if numeric overflow occurs
   */
  def get(field: TemporalField): Int = {
    val range: ValueRange = range(field)
    if (range.isIntValue == false) {
      throw new UnsupportedTemporalTypeException("Invalid field " + field + " + for get() method, use getLong() instead")
    }
    val value: Long = getLong(field)
    if (range.isValidValue(value) == false) {
      throw new Nothing("Invalid value for " + field + " (valid values " + range + "): " + value)
    }
    return value.asInstanceOf[Int]
  }

  /**
   * Gets the value of the specified field as a {@code long}.
   * <p>
   * This queries the date-time for the value for the specified field.
   * The returned value may be outside the valid range of values for the field.
   * If the date-time cannot return the value, because the field is unsupported or for
   * some other reason, an exception will be thrown.
   *
   * @implSpec
     * Implementations must check and handle all fields defined in { @link ChronoField}.
   *                                                                       If the field is supported, then the value of the field must be returned.
   *                                                                       If unsupported, then an { @code UnsupportedTemporalTypeException} must be thrown.
   *                                                                                                       <p>
   *                                                                                                       If the field is not a { @code ChronoField}, then the result of this method
   *                                                                                                                                     is obtained by invoking { @code TemporalField.getFrom(TemporalAccessor)}
   *                                                                                                                                                                     passing { @code this} as the argument.
   *                                                                                                                                                                                     <p>
   *                                                                                                                                                                                     Implementations must ensure that no observable state is altered when this
   *                                                                                                                                                                                     read-only method is invoked.
   *
   * @param field  the field to get, not null
   * @return the value for the field
   * @throws DateTimeException if a value for the field cannot be obtained
   * @throws UnsupportedTemporalTypeException if the field is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  def getLong(field: TemporalField): Long

  /**
   * Queries this date-time.
   * <p>
   * This queries this date-time using the specified query strategy object.
   * <p>
   * Queries are a key tool for extracting information from date-times.
   * They exists to externalize the process of querying, permitting different
   * approaches, as per the strategy design pattern.
   * Examples might be a query that checks if the date is the day before February 29th
   * in a leap year, or calculates the number of days to your next birthday.
   * <p>
   * The most common query implementations are method references, such as
   * {@code Date::from} and {@code ZoneId::from}.
   * Additional implementations are provided as static methods on {@link TemporalQuery}.
   *
   * @implSpec
     * The default implementation must behave equivalent to this code:
   *   <pre>
   *   if (query == TemporalQuery.zoneId() ||
   *   query == TemporalQuery.chronology() || query == TemporalQuery.precision()) {
   *   return null;
   *   }
   *   return query.queryFrom(this);
   *   </pre>
   *   Future versions are permitted to add further queries to the if statement.
   *   <p>
   *   All classes implementing this interface and overriding this method must call
   *   { @code TemporalAccessor.super.query(query)}. JDK classes may avoid calling
   *           super if they provide behavior equivalent to the default behaviour, however
   *           non-JDK classes may not utilize this optimization and must call { @code super}.
   *                                                                                   <p>
   *                                                                                   If the implementation can supply a value for one of the queries listed in the
   *                                                                                   if statement of the default implementation, then it must do so.
   *                                                                                   For example, an application-defined { @code HourMin} class storing the hour
   *                                                                                                                               and minute must override this method as follows:
   *                                                                                                                               <pre>
   *                                                                                                                               if (query == TemporalQuery.precision()) {
   *                                                                                                                               return MINUTES;
   *                                                                                                                               }
   *                                                                                                                               return TemporalAccessor.super.query(query);
   *                                                                                                                               </pre>
   *                                                                                                                               <p>
   *                                                                                                                               Implementations must ensure that no observable state is altered when this
   *                                                                                                                               read-only method is invoked.
   *
   * @param <R> the type of the result
   * @param query  the query to invoke, not null
   * @return the query result, null may be returned (defined by the query)
   * @throws DateTimeException if unable to query
   * @throws ArithmeticException if numeric overflow occurs
   */
  def query(query: TemporalQuery[R]): R = {
    if (query eq TemporalQuery.zoneId || query eq TemporalQuery.chronology || query eq TemporalQuery.precision) {
      return null
    }
    return query.queryFrom(this)
  }
}

/*
 * Copyright (c) 2012, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
/*
 * This file is available under and governed by the GNU General Public
 * License version 2 only, as published by the Free Software Foundation.
 * However, the following notice accompanied the original version of this
 * file:
 *
 * Copyright (c) 2012, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/**
 * Strategy for adjusting a temporal object.
 * <p>
 * Adjusters are a key tool for modifying temporal objects.
 * They exist to externalize the process of adjustment, permitting different
 * approaches, as per the strategy design pattern.
 * Examples might be an adjuster that sets the date avoiding weekends, or one that
 * sets the date to the last day of the month.
 * <p>
 * There are two equivalent ways of using a {@code TemporalAdjuster}.
 * The first is to invoke the method on this interface directly.
 * The second is to use {@link Temporal#with(TemporalAdjuster)}:
 * <pre>
 * // these two lines are equivalent, but the second approach is recommended
 * temporal = thisAdjuster.adjustInto(temporal);
 * temporal = temporal.with(thisAdjuster);
 * </pre>
 * It is recommended to use the second approach, {@code with(TemporalAdjuster)},
 * as it is a lot clearer to read in code.
 * <p>
 * This class also contains a standard set of adjusters, available as static methods.
 * These include:
 * <ul>
 * <li>finding the first or last day of the month
 * <li>finding the first day of next month
 * <li>finding the first or last day of the year
 * <li>finding the first day of next year
 * <li>finding the first or last day-of-week within a month, such as "first Wednesday in June"
 * <li>finding the next or previous day-of-week, such as "next Thursday"
 * </ul>
 *
 * @implSpec
 * This interface places no restrictions on the mutability of implementations,
 * however immutability is strongly recommended.
 * <p>
 * All the implementations supplied by the static methods on this interface are immutable.
 *
 * @since 1.8
 */
@FunctionalInterface object TemporalAdjuster {
  /**
   * Obtains a {@code TemporalAdjuster} that wraps a date adjuster.
   * <p>
   * The {@code TemporalAdjuster} is based on the low level {@code Temporal} interface.
   * This method allows an adjustment from {@code Date} to {@code Date}
   * to be wrapped to match the temporal-based interface.
   * This is provided for convenience to make user-written adjusters simpler.
   * <p>
   * In general, user-written adjusters should be static constants:
   * <pre>{@code
   * static TemporalAdjuster TWO_DAYS_LATER = TemporalAdjuster.ofDateAdjuster(
     *    date -> date.plusDays(2));
     * }</pre>
   *
   * @param dateBasedAdjuster  the date-based adjuster, not null
   * @return the temporal adjuster wrapping on the date adjuster, not null
   */
  def ofDateAdjuster(dateBasedAdjuster: Nothing): TemporalAdjuster = {
    return TemporalAdjusters.ofDateAdjuster(dateBasedAdjuster)
  }

  /**
   * Returns the "first day of month" adjuster, which returns a new date set to
   * the first day of the current month.
   * <p>
   * The ISO calendar system behaves as follows:<br>
   * The input 2011-01-15 will return 2011-01-01.<br>
   * The input 2011-02-15 will return 2011-02-01.
   * <p>
   * The behavior is suitable for use with most calendar systems.
   * It is equivalent to:
   * <pre>
   * temporal.with(DAY_OF_MONTH, 1);
   * </pre>
   *
   * @return the first day-of-month adjuster, not null
   */
  def firstDayOfMonth: TemporalAdjuster = {
    return TemporalAdjusters.firstDayOfMonth
  }

  /**
   * Returns the "last day of month" adjuster, which returns a new date set to
   * the last day of the current month.
   * <p>
   * The ISO calendar system behaves as follows:<br>
   * The input 2011-01-15 will return 2011-01-31.<br>
   * The input 2011-02-15 will return 2011-02-28.<br>
   * The input 2012-02-15 will return 2012-02-29 (leap year).<br>
   * The input 2011-04-15 will return 2011-04-30.
   * <p>
   * The behavior is suitable for use with most calendar systems.
   * It is equivalent to:
   * <pre>
   * long lastDay = temporal.range(DAY_OF_MONTH).getMaximum();
   * temporal.with(DAY_OF_MONTH, lastDay);
   * </pre>
   *
   * @return the last day-of-month adjuster, not null
   */
  def lastDayOfMonth: TemporalAdjuster = {
    return TemporalAdjusters.lastDayOfMonth
  }

  /**
   * Returns the "first day of next month" adjuster, which returns a new date set to
   * the first day of the next month.
   * <p>
   * The ISO calendar system behaves as follows:<br>
   * The input 2011-01-15 will return 2011-02-01.<br>
   * The input 2011-02-15 will return 2011-03-01.
   * <p>
   * The behavior is suitable for use with most calendar systems.
   * It is equivalent to:
   * <pre>
   * temporal.with(DAY_OF_MONTH, 1).plus(1, MONTHS);
   * </pre>
   *
   * @return the first day of next month adjuster, not null
   */
  def firstDayOfNextMonth: TemporalAdjuster = {
    return TemporalAdjusters.firstDayOfNextMonth
  }

  /**
   * Returns the "first day of year" adjuster, which returns a new date set to
   * the first day of the current year.
   * <p>
   * The ISO calendar system behaves as follows:<br>
   * The input 2011-01-15 will return 2011-01-01.<br>
   * The input 2011-02-15 will return 2011-01-01.<br>
   * <p>
   * The behavior is suitable for use with most calendar systems.
   * It is equivalent to:
   * <pre>
   * temporal.with(DAY_OF_YEAR, 1);
   * </pre>
   *
   * @return the first day-of-year adjuster, not null
   */
  def firstDayOfYear: TemporalAdjuster = {
    return TemporalAdjusters.firstDayOfYear
  }

  /**
   * Returns the "last day of year" adjuster, which returns a new date set to
   * the last day of the current year.
   * <p>
   * The ISO calendar system behaves as follows:<br>
   * The input 2011-01-15 will return 2011-12-31.<br>
   * The input 2011-02-15 will return 2011-12-31.<br>
   * <p>
   * The behavior is suitable for use with most calendar systems.
   * It is equivalent to:
   * <pre>
   * long lastDay = temporal.range(DAY_OF_YEAR).getMaximum();
   * temporal.with(DAY_OF_YEAR, lastDay);
   * </pre>
   *
   * @return the last day-of-year adjuster, not null
   */
  def lastDayOfYear: TemporalAdjuster = {
    return TemporalAdjusters.lastDayOfYear
  }

  /**
   * Returns the "first day of next year" adjuster, which returns a new date set to
   * the first day of the next year.
   * <p>
   * The ISO calendar system behaves as follows:<br>
   * The input 2011-01-15 will return 2012-01-01.
   * <p>
   * The behavior is suitable for use with most calendar systems.
   * It is equivalent to:
   * <pre>
   * temporal.with(DAY_OF_YEAR, 1).plus(1, YEARS);
   * </pre>
   *
   * @return the first day of next month adjuster, not null
   */
  def firstDayOfNextYear: TemporalAdjuster = {
    return TemporalAdjusters.firstDayOfNextYear
  }

  /**
   * Returns the first in month adjuster, which returns a new date
   * in the same month with the first matching day-of-week.
   * This is used for expressions like 'first Tuesday in March'.
   * <p>
   * The ISO calendar system behaves as follows:<br>
   * The input 2011-12-15 for (MONDAY) will return 2011-12-05.<br>
   * The input 2011-12-15 for (FRIDAY) will return 2011-12-02.<br>
   * <p>
   * The behavior is suitable for use with most calendar systems.
   * It uses the {@code DAY_OF_WEEK} and {@code DAY_OF_MONTH} fields
   * and the {@code DAYS} unit, and assumes a seven day week.
   *
   * @param dayOfWeek  the day-of-week, not null
   * @return the first in month adjuster, not null
   */
  def firstInMonth(dayOfWeek: DayOfWeek): TemporalAdjuster = {
    return TemporalAdjuster.dayOfWeekInMonth(1, dayOfWeek)
  }

  /**
   * Returns the last in month adjuster, which returns a new date
   * in the same month with the last matching day-of-week.
   * This is used for expressions like 'last Tuesday in March'.
   * <p>
   * The ISO calendar system behaves as follows:<br>
   * The input 2011-12-15 for (MONDAY) will return 2011-12-26.<br>
   * The input 2011-12-15 for (FRIDAY) will return 2011-12-30.<br>
   * <p>
   * The behavior is suitable for use with most calendar systems.
   * It uses the {@code DAY_OF_WEEK} and {@code DAY_OF_MONTH} fields
   * and the {@code DAYS} unit, and assumes a seven day week.
   *
   * @param dayOfWeek  the day-of-week, not null
   * @return the first in month adjuster, not null
   */
  def lastInMonth(dayOfWeek: DayOfWeek): TemporalAdjuster = {
    return TemporalAdjuster.dayOfWeekInMonth(-1, dayOfWeek)
  }

  /**
   * Returns the day-of-week in month adjuster, which returns a new date
   * in the same month with the ordinal day-of-week.
   * This is used for expressions like the 'second Tuesday in March'.
   * <p>
   * The ISO calendar system behaves as follows:<br>
   * The input 2011-12-15 for (1,TUESDAY) will return 2011-12-06.<br>
   * The input 2011-12-15 for (2,TUESDAY) will return 2011-12-13.<br>
   * The input 2011-12-15 for (3,TUESDAY) will return 2011-12-20.<br>
   * The input 2011-12-15 for (4,TUESDAY) will return 2011-12-27.<br>
   * The input 2011-12-15 for (5,TUESDAY) will return 2012-01-03.<br>
   * The input 2011-12-15 for (-1,TUESDAY) will return 2011-12-27 (last in month).<br>
   * The input 2011-12-15 for (-4,TUESDAY) will return 2011-12-06 (3 weeks before last in month).<br>
   * The input 2011-12-15 for (-5,TUESDAY) will return 2011-11-29 (4 weeks before last in month).<br>
   * The input 2011-12-15 for (0,TUESDAY) will return 2011-11-29 (last in previous month).<br>
   * <p>
   * For a positive or zero ordinal, the algorithm is equivalent to finding the first
   * day-of-week that matches within the month and then adding a number of weeks to it.
   * For a negative ordinal, the algorithm is equivalent to finding the last
   * day-of-week that matches within the month and then subtracting a number of weeks to it.
   * The ordinal number of weeks is not validated and is interpreted leniently
   * according to this algorithm. This definition means that an ordinal of zero finds
   * the last matching day-of-week in the previous month.
   * <p>
   * The behavior is suitable for use with most calendar systems.
   * It uses the {@code DAY_OF_WEEK} and {@code DAY_OF_MONTH} fields
   * and the {@code DAYS} unit, and assumes a seven day week.
   *
   * @param ordinal  the week within the month, unbounded but typically from -5 to 5
   * @param dayOfWeek  the day-of-week, not null
   * @return the day-of-week in month adjuster, not null
   */
  def dayOfWeekInMonth(ordinal: Int, dayOfWeek: DayOfWeek): TemporalAdjuster = {
    return TemporalAdjusters.dayOfWeekInMonth(ordinal, dayOfWeek)
  }

  /**
   * Returns the next day-of-week adjuster, which adjusts the date to the
   * first occurrence of the specified day-of-week after the date being adjusted.
   * <p>
   * The ISO calendar system behaves as follows:<br>
   * The input 2011-01-15 (a Saturday) for parameter (MONDAY) will return 2011-01-17 (two days later).<br>
   * The input 2011-01-15 (a Saturday) for parameter (WEDNESDAY) will return 2011-01-19 (four days later).<br>
   * The input 2011-01-15 (a Saturday) for parameter (SATURDAY) will return 2011-01-22 (seven days later).
   * <p>
   * The behavior is suitable for use with most calendar systems.
   * It uses the {@code DAY_OF_WEEK} field and the {@code DAYS} unit,
   * and assumes a seven day week.
   *
   * @param dayOfWeek  the day-of-week to move the date to, not null
   * @return the next day-of-week adjuster, not null
   */
  def next(dayOfWeek: DayOfWeek): TemporalAdjuster = {
    return TemporalAdjusters.next(dayOfWeek)
  }

  /**
   * Returns the next-or-same day-of-week adjuster, which adjusts the date to the
   * first occurrence of the specified day-of-week after the date being adjusted
   * unless it is already on that day in which case the same object is returned.
   * <p>
   * The ISO calendar system behaves as follows:<br>
   * The input 2011-01-15 (a Saturday) for parameter (MONDAY) will return 2011-01-17 (two days later).<br>
   * The input 2011-01-15 (a Saturday) for parameter (WEDNESDAY) will return 2011-01-19 (four days later).<br>
   * The input 2011-01-15 (a Saturday) for parameter (SATURDAY) will return 2011-01-15 (same as input).
   * <p>
   * The behavior is suitable for use with most calendar systems.
   * It uses the {@code DAY_OF_WEEK} field and the {@code DAYS} unit,
   * and assumes a seven day week.
   *
   * @param dayOfWeek  the day-of-week to check for or move the date to, not null
   * @return the next-or-same day-of-week adjuster, not null
   */
  def nextOrSame(dayOfWeek: DayOfWeek): TemporalAdjuster = {
    return TemporalAdjusters.nextOrSame(dayOfWeek)
  }

  /**
   * Returns the previous day-of-week adjuster, which adjusts the date to the
   * first occurrence of the specified day-of-week before the date being adjusted.
   * <p>
   * The ISO calendar system behaves as follows:<br>
   * The input 2011-01-15 (a Saturday) for parameter (MONDAY) will return 2011-01-10 (five days earlier).<br>
   * The input 2011-01-15 (a Saturday) for parameter (WEDNESDAY) will return 2011-01-12 (three days earlier).<br>
   * The input 2011-01-15 (a Saturday) for parameter (SATURDAY) will return 2011-01-08 (seven days earlier).
   * <p>
   * The behavior is suitable for use with most calendar systems.
   * It uses the {@code DAY_OF_WEEK} field and the {@code DAYS} unit,
   * and assumes a seven day week.
   *
   * @param dayOfWeek  the day-of-week to move the date to, not null
   * @return the previous day-of-week adjuster, not null
   */
  def previous(dayOfWeek: DayOfWeek): TemporalAdjuster = {
    return TemporalAdjusters.previous(dayOfWeek)
  }

  /**
   * Returns the previous-or-same day-of-week adjuster, which adjusts the date to the
   * first occurrence of the specified day-of-week before the date being adjusted
   * unless it is already on that day in which case the same object is returned.
   * <p>
   * The ISO calendar system behaves as follows:<br>
   * The input 2011-01-15 (a Saturday) for parameter (MONDAY) will return 2011-01-10 (five days earlier).<br>
   * The input 2011-01-15 (a Saturday) for parameter (WEDNESDAY) will return 2011-01-12 (three days earlier).<br>
   * The input 2011-01-15 (a Saturday) for parameter (SATURDAY) will return 2011-01-15 (same as input).
   * <p>
   * The behavior is suitable for use with most calendar systems.
   * It uses the {@code DAY_OF_WEEK} field and the {@code DAYS} unit,
   * and assumes a seven day week.
   *
   * @param dayOfWeek  the day-of-week to check for or move the date to, not null
   * @return the previous-or-same day-of-week adjuster, not null
   */
  def previousOrSame(dayOfWeek: DayOfWeek): TemporalAdjuster = {
    return TemporalAdjusters.previousOrSame(dayOfWeek)
  }
}

@FunctionalInterface abstract trait TemporalAdjuster {
  /**
   * Adjusts the specified temporal object.
   * <p>
   * This adjusts the specified temporal object using the logic
   * encapsulated in the implementing class.
   * Examples might be an adjuster that sets the date avoiding weekends, or one that
   * sets the date to the last day of the month.
   * <p>
   * There are two equivalent ways of using this method.
   * The first is to invoke this method directly.
   * The second is to use {@link Temporal#with(TemporalAdjuster)}:
   * <pre>
   * // these two lines are equivalent, but the second approach is recommended
   * temporal = thisAdjuster.adjustInto(temporal);
   * temporal = temporal.with(thisAdjuster);
   * </pre>
   * It is recommended to use the second approach, {@code with(TemporalAdjuster)},
   * as it is a lot clearer to read in code.
   *
   * @implSpec
     * The implementation must take the input object and adjust it.
   *   The implementation defines the logic of the adjustment and is responsible for
   *   documenting that logic. It may use any method on { @code Temporal} to
   *                                                            query the temporal object and perform the adjustment.
   *                                                            The returned object must have the same observable type as the input object
   *                                                            <p>
   *                                                            The input object must not be altered.
   *                                                            Instead, an adjusted copy of the original must be returned.
   *                                                            This provides equivalent, safe behavior for immutable and mutable temporal objects.
   *                                                            <p>
   *                                                            The input temporal object may be in a calendar system other than ISO.
   *                                                            Implementations may choose to document compatibility with other calendar systems,
   *                                                            or reject non-ISO temporal objects by { @link TemporalQuery#chronology() querying the chronology}.
   *                                                                                                          <p>
   *                                                                                                          This method may be called from multiple threads in parallel.
   *                                                                                                          It must be thread-safe when invoked.
   *
   * @param temporal  the temporal object to adjust, not null
   * @return an object of the same observable type with the adjustment made, not null
   * @throws DateTimeException if unable to make the adjustment
   * @throws ArithmeticException if numeric overflow occurs
   */
  def adjustInto(temporal: Temporal): Temporal
}

/*
 * Copyright (c) 2012, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
/*
 * This file is available under and governed by the GNU General Public
 * License version 2 only, as published by the Free Software Foundation.
 * However, the following notice accompanied the original version of this
 * file:
 *
 * Copyright (c) 2012-2013, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/**
 * Implementations of the static methods in {@code TemporalAdjuster}
 *
 * @since 1.8
 */
final object TemporalAdjusters {
  /**
   * Obtains a {@code TemporalAdjuster} that wraps a date adjuster.
   * <p>
   * The {@code TemporalAdjuster} is based on the low level {@code Temporal} interface.
   * This method allows an adjustment from {@code Date} to {@code Date}
   * to be wrapped to match the temporal-based interface.
   * This is provided for convenience to make user-written adjusters simpler.
   * <p>
   * In general, user-written adjusters should be static constants:
   * <pre>
   * public static TemporalAdjuster TWO_DAYS_LATER = TemporalAdjuster.ofDateAdjuster(
   * date -> date.plusDays(2));
   * </pre>
   *
   * @param dateBasedAdjuster  the date-based adjuster, not null
   * @return the temporal adjuster wrapping on the date adjuster, not null
   */
  private[temporal] def ofDateAdjuster(dateBasedAdjuster: Nothing): TemporalAdjuster = {
    Objects.requireNonNull(dateBasedAdjuster, "dateBasedAdjuster")
    return (temporal) -> {
      LocalDate input = LocalDate.from(temporal);
      LocalDate output = dateBasedAdjuster.apply(input);
      return temporal.with (output);
    }
  }

  /**
   * Returns the "first day of month" adjuster, which returns a new date set to
   * the first day of the current month.
   * <p>
   * The ISO calendar system behaves as follows:<br>
   * The input 2011-01-15 will return 2011-01-01.<br>
   * The input 2011-02-15 will return 2011-02-01.
   * <p>
   * The behavior is suitable for use with most calendar systems.
   * It is equivalent to:
   * <pre>
   * temporal.with(DAY_OF_MONTH, 1);
   * </pre>
   *
   * @return the first day-of-month adjuster, not null
   */
  private[temporal] def firstDayOfMonth: TemporalAdjuster = {
    return (temporal) -> temporal.with (DAY_OF_MONTH, 1)
  }

  /**
   * Returns the "last day of month" adjuster, which returns a new date set to
   * the last day of the current month.
   * <p>
   * The ISO calendar system behaves as follows:<br>
   * The input 2011-01-15 will return 2011-01-31.<br>
   * The input 2011-02-15 will return 2011-02-28.<br>
   * The input 2012-02-15 will return 2012-02-29 (leap year).<br>
   * The input 2011-04-15 will return 2011-04-30.
   * <p>
   * The behavior is suitable for use with most calendar systems.
   * It is equivalent to:
   * <pre>
   * long lastDay = temporal.range(DAY_OF_MONTH).getMaximum();
   * temporal.with(DAY_OF_MONTH, lastDay);
   * </pre>
   *
   * @return the last day-of-month adjuster, not null
   */
  private[temporal] def lastDayOfMonth: TemporalAdjuster = {
    return (temporal) -> temporal.with (DAY_OF_MONTH, temporal.range(DAY_OF_MONTH).getMaximum())
  }

  /**
   * Returns the "first day of next month" adjuster, which returns a new date set to
   * the first day of the next month.
   * <p>
   * The ISO calendar system behaves as follows:<br>
   * The input 2011-01-15 will return 2011-02-01.<br>
   * The input 2011-02-15 will return 2011-03-01.
   * <p>
   * The behavior is suitable for use with most calendar systems.
   * It is equivalent to:
   * <pre>
   * temporal.with(DAY_OF_MONTH, 1).plus(1, MONTHS);
   * </pre>
   *
   * @return the first day of next month adjuster, not null
   */
  private[temporal] def firstDayOfNextMonth: TemporalAdjuster = {
    return (temporal) -> temporal.with (DAY_OF_MONTH, 1).plus(1, MONTHS)
  }

  /**
   * Returns the "first day of year" adjuster, which returns a new date set to
   * the first day of the current year.
   * <p>
   * The ISO calendar system behaves as follows:<br>
   * The input 2011-01-15 will return 2011-01-01.<br>
   * The input 2011-02-15 will return 2011-01-01.<br>
   * <p>
   * The behavior is suitable for use with most calendar systems.
   * It is equivalent to:
   * <pre>
   * temporal.with(DAY_OF_YEAR, 1);
   * </pre>
   *
   * @return the first day-of-year adjuster, not null
   */
  private[temporal] def firstDayOfYear: TemporalAdjuster = {
    return (temporal) -> temporal.with (DAY_OF_YEAR, 1)
  }

  /**
   * Returns the "last day of year" adjuster, which returns a new date set to
   * the last day of the current year.
   * <p>
   * The ISO calendar system behaves as follows:<br>
   * The input 2011-01-15 will return 2011-12-31.<br>
   * The input 2011-02-15 will return 2011-12-31.<br>
   * <p>
   * The behavior is suitable for use with most calendar systems.
   * It is equivalent to:
   * <pre>
   * long lastDay = temporal.range(DAY_OF_YEAR).getMaximum();
   * temporal.with(DAY_OF_YEAR, lastDay);
   * </pre>
   *
   * @return the last day-of-year adjuster, not null
   */
  private[temporal] def lastDayOfYear: TemporalAdjuster = {
    return (temporal) -> temporal.with (DAY_OF_YEAR, temporal.range(DAY_OF_YEAR).getMaximum())
  }

  /**
   * Returns the "first day of next year" adjuster, which returns a new date set to
   * the first day of the next year.
   * <p>
   * The ISO calendar system behaves as follows:<br>
   * The input 2011-01-15 will return 2012-01-01.
   * <p>
   * The behavior is suitable for use with most calendar systems.
   * It is equivalent to:
   * <pre>
   * temporal.with(DAY_OF_YEAR, 1).plus(1, YEARS);
   * </pre>
   *
   * @return the first day of next month adjuster, not null
   */
  private[temporal] def firstDayOfNextYear: TemporalAdjuster = {
    return (temporal) -> temporal.with (DAY_OF_YEAR, 1).plus(1, YEARS)
  }

  /**
   * Returns the first in month adjuster, which returns a new date
   * in the same month with the first matching day-of-week.
   * This is used for expressions like 'first Tuesday in March'.
   * <p>
   * The ISO calendar system behaves as follows:<br>
   * The input 2011-12-15 for (MONDAY) will return 2011-12-05.<br>
   * The input 2011-12-15 for (FRIDAY) will return 2011-12-02.<br>
   * <p>
   * The behavior is suitable for use with most calendar systems.
   * It uses the {@code DAY_OF_WEEK} and {@code DAY_OF_MONTH} fields
   * and the {@code DAYS} unit, and assumes a seven day week.
   *
   * @param dayOfWeek  the day-of-week, not null
   * @return the first in month adjuster, not null
   */
  private[temporal] def firstInMonth(dayOfWeek: DayOfWeek): TemporalAdjuster = {
    return TemporalAdjusters.dayOfWeekInMonth(1, dayOfWeek)
  }

  /**
   * Returns the last in month adjuster, which returns a new date
   * in the same month with the last matching day-of-week.
   * This is used for expressions like 'last Tuesday in March'.
   * <p>
   * The ISO calendar system behaves as follows:<br>
   * The input 2011-12-15 for (MONDAY) will return 2011-12-26.<br>
   * The input 2011-12-15 for (FRIDAY) will return 2011-12-30.<br>
   * <p>
   * The behavior is suitable for use with most calendar systems.
   * It uses the {@code DAY_OF_WEEK} and {@code DAY_OF_MONTH} fields
   * and the {@code DAYS} unit, and assumes a seven day week.
   *
   * @param dayOfWeek  the day-of-week, not null
   * @return the first in month adjuster, not null
   */
  private[temporal] def lastInMonth(dayOfWeek: DayOfWeek): TemporalAdjuster = {
    return TemporalAdjusters.dayOfWeekInMonth(-1, dayOfWeek)
  }

  /**
   * Returns the day-of-week in month adjuster, which returns a new date
   * in the same month with the ordinal day-of-week.
   * This is used for expressions like the 'second Tuesday in March'.
   * <p>
   * The ISO calendar system behaves as follows:<br>
   * The input 2011-12-15 for (1,TUESDAY) will return 2011-12-06.<br>
   * The input 2011-12-15 for (2,TUESDAY) will return 2011-12-13.<br>
   * The input 2011-12-15 for (3,TUESDAY) will return 2011-12-20.<br>
   * The input 2011-12-15 for (4,TUESDAY) will return 2011-12-27.<br>
   * The input 2011-12-15 for (5,TUESDAY) will return 2012-01-03.<br>
   * The input 2011-12-15 for (-1,TUESDAY) will return 2011-12-27 (last in month).<br>
   * The input 2011-12-15 for (-4,TUESDAY) will return 2011-12-06 (3 weeks before last in month).<br>
   * The input 2011-12-15 for (-5,TUESDAY) will return 2011-11-29 (4 weeks before last in month).<br>
   * The input 2011-12-15 for (0,TUESDAY) will return 2011-11-29 (last in previous month).<br>
   * <p>
   * For a positive or zero ordinal, the algorithm is equivalent to finding the first
   * day-of-week that matches within the month and then adding a number of weeks to it.
   * For a negative ordinal, the algorithm is equivalent to finding the last
   * day-of-week that matches within the month and then subtracting a number of weeks to it.
   * The ordinal number of weeks is not validated and is interpreted leniently
   * according to this algorithm. This definition means that an ordinal of zero finds
   * the last matching day-of-week in the previous month.
   * <p>
   * The behavior is suitable for use with most calendar systems.
   * It uses the {@code DAY_OF_WEEK} and {@code DAY_OF_MONTH} fields
   * and the {@code DAYS} unit, and assumes a seven day week.
   *
   * @param ordinal  the week within the month, unbounded but typically from -5 to 5
   * @param dayOfWeek  the day-of-week, not null
   * @return the day-of-week in month adjuster, not null
   */
  private[temporal] def dayOfWeekInMonth(ordinal: Int, dayOfWeek: DayOfWeek): TemporalAdjuster = {
    Objects.requireNonNull(dayOfWeek, "dayOfWeek")
    val dowValue: Int = dayOfWeek.getValue
    if (ordinal >= 0) {
      return (temporal) -> {
        Temporal temp = temporal.with (DAY_OF_MONTH, 1);
        int curDow = temp.get(DAY_OF_WEEK);
        int dowDiff = (dowValue - curDow + 7) % 7;
        dowDiff += (ordinal - 1L) * 7L; // safe from overflow
        return temp.plus(dowDiff, DAYS);
      }
    }
    else {
      return (temporal) -> {
        Temporal temp = temporal.with (DAY_OF_MONTH, temporal.range(DAY_OF_MONTH).getMaximum());
        int curDow = temp.get(DAY_OF_WEEK);
        int daysDiff = dowValue - curDow;
        daysDiff = (daysDiff == 0 ? 0: (daysDiff > 0 ? daysDiff - 7: daysDiff) );
        daysDiff -= (-ordinal - 1L) * 7L; // safe from overflow
        return temp.plus(daysDiff, DAYS);
      }
    }
  }

  /**
   * Returns the next day-of-week adjuster, which adjusts the date to the
   * first occurrence of the specified day-of-week after the date being adjusted.
   * <p>
   * The ISO calendar system behaves as follows:<br>
   * The input 2011-01-15 (a Saturday) for parameter (MONDAY) will return 2011-01-17 (two days later).<br>
   * The input 2011-01-15 (a Saturday) for parameter (WEDNESDAY) will return 2011-01-19 (four days later).<br>
   * The input 2011-01-15 (a Saturday) for parameter (SATURDAY) will return 2011-01-22 (seven days later).
   * <p>
   * The behavior is suitable for use with most calendar systems.
   * It uses the {@code DAY_OF_WEEK} field and the {@code DAYS} unit,
   * and assumes a seven day week.
   *
   * @param dayOfWeek  the day-of-week to move the date to, not null
   * @return the next day-of-week adjuster, not null
   */
  private[temporal] def next(dayOfWeek: DayOfWeek): TemporalAdjuster = {
    val dowValue: Int = dayOfWeek.getValue
    return (temporal) -> {
      int calDow = temporal.get(DAY_OF_WEEK);
      int daysDiff = calDow - dowValue;
      return temporal.plus(daysDiff >= 0 ? 7 - daysDiff: - daysDiff, DAYS);
    }
  }

  /**
   * Returns the next-or-same day-of-week adjuster, which adjusts the date to the
   * first occurrence of the specified day-of-week after the date being adjusted
   * unless it is already on that day in which case the same object is returned.
   * <p>
   * The ISO calendar system behaves as follows:<br>
   * The input 2011-01-15 (a Saturday) for parameter (MONDAY) will return 2011-01-17 (two days later).<br>
   * The input 2011-01-15 (a Saturday) for parameter (WEDNESDAY) will return 2011-01-19 (four days later).<br>
   * The input 2011-01-15 (a Saturday) for parameter (SATURDAY) will return 2011-01-15 (same as input).
   * <p>
   * The behavior is suitable for use with most calendar systems.
   * It uses the {@code DAY_OF_WEEK} field and the {@code DAYS} unit,
   * and assumes a seven day week.
   *
   * @param dayOfWeek  the day-of-week to check for or move the date to, not null
   * @return the next-or-same day-of-week adjuster, not null
   */
  private[temporal] def nextOrSame(dayOfWeek: DayOfWeek): TemporalAdjuster = {
    val dowValue: Int = dayOfWeek.getValue
    return (temporal) -> {
      int calDow = temporal.get(DAY_OF_WEEK);
      if (calDow == dowValue) {
        return temporal;
      }
      int daysDiff = calDow - dowValue;
      return temporal.plus(daysDiff >= 0 ? 7 - daysDiff: - daysDiff, DAYS);
    }
  }

  /**
   * Returns the previous day-of-week adjuster, which adjusts the date to the
   * first occurrence of the specified day-of-week before the date being adjusted.
   * <p>
   * The ISO calendar system behaves as follows:<br>
   * The input 2011-01-15 (a Saturday) for parameter (MONDAY) will return 2011-01-10 (five days earlier).<br>
   * The input 2011-01-15 (a Saturday) for parameter (WEDNESDAY) will return 2011-01-12 (three days earlier).<br>
   * The input 2011-01-15 (a Saturday) for parameter (SATURDAY) will return 2011-01-08 (seven days earlier).
   * <p>
   * The behavior is suitable for use with most calendar systems.
   * It uses the {@code DAY_OF_WEEK} field and the {@code DAYS} unit,
   * and assumes a seven day week.
   *
   * @param dayOfWeek  the day-of-week to move the date to, not null
   * @return the previous day-of-week adjuster, not null
   */
  private[temporal] def previous(dayOfWeek: DayOfWeek): TemporalAdjuster = {
    val dowValue: Int = dayOfWeek.getValue
    return (temporal) -> {
      int calDow = temporal.get(DAY_OF_WEEK);
      int daysDiff = dowValue - calDow;
      return temporal.minus(daysDiff >= 0 ? 7 - daysDiff: - daysDiff, DAYS);
    }
  }

  /**
   * Returns the previous-or-same day-of-week adjuster, which adjusts the date to the
   * first occurrence of the specified day-of-week before the date being adjusted
   * unless it is already on that day in which case the same object is returned.
   * <p>
   * The ISO calendar system behaves as follows:<br>
   * The input 2011-01-15 (a Saturday) for parameter (MONDAY) will return 2011-01-10 (five days earlier).<br>
   * The input 2011-01-15 (a Saturday) for parameter (WEDNESDAY) will return 2011-01-12 (three days earlier).<br>
   * The input 2011-01-15 (a Saturday) for parameter (SATURDAY) will return 2011-01-15 (same as input).
   * <p>
   * The behavior is suitable for use with most calendar systems.
   * It uses the {@code DAY_OF_WEEK} field and the {@code DAYS} unit,
   * and assumes a seven day week.
   *
   * @param dayOfWeek  the day-of-week to check for or move the date to, not null
   * @return the previous-or-same day-of-week adjuster, not null
   */
  private[temporal] def previousOrSame(dayOfWeek: DayOfWeek): TemporalAdjuster = {
    val dowValue: Int = dayOfWeek.getValue
    return (temporal) -> {
      int calDow = temporal.get(DAY_OF_WEEK);
      if (calDow == dowValue) {
        return temporal;
      }
      int daysDiff = dowValue - calDow;
      return temporal.minus(daysDiff >= 0 ? 7 - daysDiff: - daysDiff, DAYS);
    }
  }
}

final class TemporalAdjusters {
  private def this() {
    this()
  }
}

/*
 * Copyright (c) 2012, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
/*
 * This file is available under and governed by the GNU General Public
 * License version 2 only, as published by the Free Software Foundation.
 * However, the following notice accompanied the original version of this
 * file:
 *
 * Copyright (c) 2012, 2013 Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/**
 * Framework-level interface defining an amount of time, such as
 * "6 hours", "8 days" or "2 years and 3 months".
 * <p>
 * This is the base interface type for amounts of time.
 * An amount is distinct from a date or time-of-day in that it is not tied
 * to any specific point on the time-line.
 * <p>
 * The amount can be thought of as a {@code Map} of {@link TemporalUnit} to
 * {@code long}, exposed via {@link #getUnits()} and {@link #get(TemporalUnit)}.
 * A simple case might have a single unit-value pair, such as "6 hours".
 * A more complex case may have multiple unit-value pairs, such as
 * "7 years, 3 months and 5 days".
 * <p>
 * There are two common implementations.
 * {@link Period} is a date-based implementation, storing years, months and days.
 * {@link Duration} is a time-based implementation, storing seconds and nanoseconds,
 * but providing some access using other duration based units such as minutes,
 * hours and fixed 24-hour days.
 * <p>
 * This interface is a framework-level interface that should not be widely
 * used in application code. Instead, applications should create and pass
 * around instances of concrete types, such as {@code Period} and {@code Duration}.
 *
 * @implSpec
 * This interface places no restrictions on the mutability of implementations,
 * however immutability is strongly recommended.
 *
 * @since 1.8
 */
abstract trait TemporalAmount {
  /**
   * Returns the value of the requested unit.
   * The units returned from {@link #getUnits()} uniquely define the
   * value of the {@code TemporalAmount}.  A value must be returned
   * for each unit listed in {@code getUnits}.
   *
   * @implSpec
     * Implementations may declare support for units not listed by { @link #getUnits()}.
   *                                                                       Typically, the implementation would define additional units
   *                                                                       as conversions for the convenience of developers.
   *
   * @param unit the { @code TemporalUnit} for which to return the value
   * @return the long value of the unit
   * @throws DateTimeException if a value for the unit cannot be obtained
   * @throws UnsupportedTemporalTypeException if the { @code unit} is not supported
   */
  def get(unit: TemporalUnit): Long

  /**
   * Returns the list of units uniquely defining the value of this TemporalAmount.
   * The list of {@code TemporalUnits} is defined by the implementation class.
   * The list is a snapshot of the units at the time {@code getUnits}
   * is called and is not mutable.
   * The units are ordered from longest duration to the shortest duration
   * of the unit.
   *
   * @implSpec
     * The list of units completely and uniquely represents the
   *   state of the object without omissions, overlaps or duplication.
   *   The units are in order from longest duration to shortest.
   *
   * @return the List of { @code TemporalUnits}; not null
   */
  def getUnits: Nothing

  /**
   * Adds to the specified temporal object.
   * <p>
   * Adds the amount to the specified temporal object using the logic
   * encapsulated in the implementing class.
   * <p>
   * There are two equivalent ways of using this method.
   * The first is to invoke this method directly.
   * The second is to use {@link Temporal#plus(TemporalAmount)}:
   * <pre>
   * // These two lines are equivalent, but the second approach is recommended
   * dateTime = amount.addTo(dateTime);
   * dateTime = dateTime.plus(adder);
   * </pre>
   * It is recommended to use the second approach, {@code plus(TemporalAmount)},
   * as it is a lot clearer to read in code.
   *
   * @implSpec
     * The implementation must take the input object and add to it.
   *   The implementation defines the logic of the addition and is responsible for
   *   documenting that logic. It may use any method on { @code Temporal} to
   *                                                            query the temporal object and perform the addition.
   *                                                            The returned object must have the same observable type as the input object
   *                                                            <p>
   *                                                            The input object must not be altered.
   *                                                            Instead, an adjusted copy of the original must be returned.
   *                                                            This provides equivalent, safe behavior for immutable and mutable temporal objects.
   *                                                            <p>
   *                                                            The input temporal object may be in a calendar system other than ISO.
   *                                                            Implementations may choose to document compatibility with other calendar systems,
   *                                                            or reject non-ISO temporal objects by { @link TemporalQuery#chronology() querying the chronology}.
   *                                                                                                          <p>
   *                                                                                                          This method may be called from multiple threads in parallel.
   *                                                                                                          It must be thread-safe when invoked.
   *
   * @param temporal  the temporal object to add the amount to, not null
   * @return an object of the same observable type with the addition made, not null
   * @throws DateTimeException if unable to add
   * @throws ArithmeticException if numeric overflow occurs
   */
  def addTo(temporal: Temporal): Temporal

  /**
   * Subtracts this object from the specified temporal object.
   * <p>
   * Subtracts the amount from the specified temporal object using the logic
   * encapsulated in the implementing class.
   * <p>
   * There are two equivalent ways of using this method.
   * The first is to invoke this method directly.
   * The second is to use {@link Temporal#minus(TemporalAmount)}:
   * <pre>
   * // these two lines are equivalent, but the second approach is recommended
   * dateTime = amount.subtractFrom(dateTime);
   * dateTime = dateTime.minus(amount);
   * </pre>
   * It is recommended to use the second approach, {@code minus(TemporalAmount)},
   * as it is a lot clearer to read in code.
   *
   * @implSpec
     * The implementation must take the input object and subtract from it.
   *   The implementation defines the logic of the subtraction and is responsible for
   *   documenting that logic. It may use any method on { @code Temporal} to
   *                                                            query the temporal object and perform the subtraction.
   *                                                            The returned object must have the same observable type as the input object
   *                                                            <p>
   *                                                            The input object must not be altered.
   *                                                            Instead, an adjusted copy of the original must be returned.
   *                                                            This provides equivalent, safe behavior for immutable and mutable temporal objects.
   *                                                            <p>
   *                                                            The input temporal object may be in a calendar system other than ISO.
   *                                                            Implementations may choose to document compatibility with other calendar systems,
   *                                                            or reject non-ISO temporal objects by { @link TemporalQuery#chronology() querying the chronology}.
   *                                                                                                          <p>
   *                                                                                                          This method may be called from multiple threads in parallel.
   *                                                                                                          It must be thread-safe when invoked.
   *
   * @param temporal  the temporal object to subtract the amount from, not null
   * @return an object of the same observable type with the subtraction made, not null
   * @throws DateTimeException if unable to subtract
   * @throws ArithmeticException if numeric overflow occurs
   */
  def subtractFrom(temporal: Temporal): Temporal
}

/*
 * Copyright (c) 2012, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
/*
 * This file is available under and governed by the GNU General Public
 * License version 2 only, as published by the Free Software Foundation.
 * However, the following notice accompanied the original version of this
 * file:
 *
 * Copyright (c) 2012, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/**
 * A field of date-time, such as month-of-year or hour-of-minute.
 * <p>
 * Date and time is expressed using fields which partition the time-line into something
 * meaningful for humans. Implementations of this interface represent those fields.
 * <p>
 * The most commonly used units are defined in {@link ChronoField}.
 * Further fields are supplied in {@link IsoFields}, {@link WeekFields} and {@link JulianFields}.
 * Fields can also be written by application code by implementing this interface.
 * <p>
 * The field works using double dispatch. Client code calls methods on a date-time like
 * {@code DateTime} which check if the field is a {@code ChronoField}.
 * If it is, then the date-time must handle it.
 * Otherwise, the method call is re-dispatched to the matching method in this interface.
 *
 * @implSpec
 * This interface must be implemented with care to ensure other classes operate correctly.
 * All implementations that can be instantiated must be final, immutable and thread-safe.
 * Implementations should be { @code Serializable} where possible.
 *                                   An enum is as effective implementation choice.
 *
 * @since 1.8
 */
abstract trait TemporalField {
  /**
   * Gets the display name for the field in the requested locale.
   * <p>
   * If there is no display name for the locale then a suitable default must be returned.
   * <p>
   * The default implementation must check the locale is not null
   * and return {@code toString()}.
   *
   * @param locale  the locale to use, not null
   * @return the display name for the locale or a suitable default, not null
   */
  def getDisplayName(locale: Locale): String = {
    Objects.requireNonNull(locale, "locale")
    return toString
  }

  /**
   * Gets the unit that the field is measured in.
   * <p>
   * The unit of the field is the period that varies within the range.
   * For example, in the field 'MonthOfYear', the unit is 'Months'.
   * See also {@link #getRangeUnit()}.
   *
   * @return the period unit defining the base unit of the field, not null
   */
  def getBaseUnit: TemporalUnit

  /**
   * Gets the range that the field is bound by.
   * <p>
   * The range of the field is the period that the field varies within.
   * For example, in the field 'MonthOfYear', the range is 'Years'.
   * See also {@link #getBaseUnit()}.
   * <p>
   * The range is never null. For example, the 'Year' field is shorthand for
   * 'YearOfForever'. It therefore has a unit of 'Years' and a range of 'Forever'.
   *
   * @return the period unit defining the range of the field, not null
   */
  def getRangeUnit: TemporalUnit

  /**
   * Gets the range of valid values for the field.
   * <p>
   * All fields can be expressed as a {@code long} integer.
   * This method returns an object that describes the valid range for that value.
   * This method is generally only applicable to the ISO-8601 calendar system.
   * <p>
   * Note that the result only describes the minimum and maximum valid values
   * and it is important not to read too much into them. For example, there
   * could be values within the range that are invalid for the field.
   *
   * @return the range of valid values for the field, not null
   */
  def range: ValueRange

  /**
   * Checks if this field represents a component of a date.
   * <p>
   * A field is date-based if it can be derived from
   * {@link ChronoField#EPOCH_DAY EPOCH_DAY}.
   * Note that it is valid for both {@code isDateBased()} and {@code isTimeBased()}
   * to return false, such as when representing a field like minute-of-week.
   *
   * @return true if this field is a component of a date
   */
  def isDateBased: Boolean

  /**
   * Checks if this field represents a component of a time.
   * <p>
   * A field is time-based if it can be derived from
   * {@link ChronoField#NANO_OF_DAY NANO_OF_DAY}.
   * Note that it is valid for both {@code isDateBased()} and {@code isTimeBased()}
   * to return false, such as when representing a field like minute-of-week.
   *
   * @return true if this field is a component of a time
   */
  def isTimeBased: Boolean

  /**
   * Checks if this field is supported by the temporal object.
   * <p>
   * This determines whether the temporal accessor supports this field.
   * If this returns false, the the temporal cannot be queried for this field.
   * <p>
   * There are two equivalent ways of using this method.
   * The first is to invoke this method directly.
   * The second is to use {@link TemporalAccessor#isSupported(TemporalField)}:
   * <pre>
   * // these two lines are equivalent, but the second approach is recommended
   * temporal = thisField.isSupportedBy(temporal);
   * temporal = temporal.isSupported(thisField);
   * </pre>
   * It is recommended to use the second approach, {@code isSupported(TemporalField)},
   * as it is a lot clearer to read in code.
   * <p>
   * Implementations should determine whether they are supported using the fields
   * available in {@link ChronoField}.
   *
   * @param temporal  the temporal object to query, not null
   * @return true if the date-time can be queried for this field, false if not
   */
  def isSupportedBy(temporal: TemporalAccessor): Boolean

  /**
   * Get the range of valid values for this field using the temporal object to
   * refine the result.
   * <p>
   * This uses the temporal object to find the range of valid values for the field.
   * This is similar to {@link #range()}, however this method refines the result
   * using the temporal. For example, if the field is {@code DAY_OF_MONTH} the
   * {@code range} method is not accurate as there are four possible month lengths,
   * 28, 29, 30 and 31 days. Using this method with a date allows the range to be
   * accurate, returning just one of those four options.
   * <p>
   * There are two equivalent ways of using this method.
   * The first is to invoke this method directly.
   * The second is to use {@link TemporalAccessor#range(TemporalField)}:
   * <pre>
   * // these two lines are equivalent, but the second approach is recommended
   * temporal = thisField.rangeRefinedBy(temporal);
   * temporal = temporal.range(thisField);
   * </pre>
   * It is recommended to use the second approach, {@code range(TemporalField)},
   * as it is a lot clearer to read in code.
   * <p>
   * Implementations should perform any queries or calculations using the fields
   * available in {@link ChronoField}.
   * If the field is not supported an {@code UnsupportedTemporalTypeException} must be thrown.
   *
   * @param temporal  the temporal object used to refine the result, not null
   * @return the range of valid values for this field, not null
   * @throws DateTimeException if the range for the field cannot be obtained
   * @throws UnsupportedTemporalTypeException if the field is not supported by the temporal
   */
  def rangeRefinedBy(temporal: TemporalAccessor): ValueRange

  /**
   * Gets the value of this field from the specified temporal object.
   * <p>
   * This queries the temporal object for the value of this field.
   * <p>
   * There are two equivalent ways of using this method.
   * The first is to invoke this method directly.
   * The second is to use {@link TemporalAccessor#getLong(TemporalField)}
   * (or {@link TemporalAccessor#get(TemporalField)}):
   * <pre>
   * // these two lines are equivalent, but the second approach is recommended
   * temporal = thisField.getFrom(temporal);
   * temporal = temporal.getLong(thisField);
   * </pre>
   * It is recommended to use the second approach, {@code getLong(TemporalField)},
   * as it is a lot clearer to read in code.
   * <p>
   * Implementations should perform any queries or calculations using the fields
   * available in {@link ChronoField}.
   * If the field is not supported an {@code UnsupportedTemporalTypeException} must be thrown.
   *
   * @param temporal  the temporal object to query, not null
   * @return the value of this field, not null
   * @throws DateTimeException if a value for the field cannot be obtained
   * @throws UnsupportedTemporalTypeException if the field is not supported by the temporal
   * @throws ArithmeticException if numeric overflow occurs
   */
  def getFrom(temporal: TemporalAccessor): Long

  /**
   * Returns a copy of the specified temporal object with the value of this field set.
   * <p>
   * This returns a new temporal object based on the specified one with the value for
   * this field changed. For example, on a {@code Date}, this could be used to
   * set the year, month or day-of-month.
   * The returned object has the same observable type as the specified object.
   * <p>
   * In some cases, changing a field is not fully defined. For example, if the target object is
   * a date representing the 31st January, then changing the month to February would be unclear.
   * In cases like this, the implementation is responsible for resolving the result.
   * Typically it will choose the previous valid date, which would be the last valid
   * day of February in this example.
   * <p>
   * There are two equivalent ways of using this method.
   * The first is to invoke this method directly.
   * The second is to use {@link Temporal#with(TemporalField, long)}:
   * <pre>
   * // these two lines are equivalent, but the second approach is recommended
   * temporal = thisField.adjustInto(temporal);
   * temporal = temporal.with(thisField);
   * </pre>
   * It is recommended to use the second approach, {@code with(TemporalField)},
   * as it is a lot clearer to read in code.
   * <p>
   * Implementations should perform any queries or calculations using the fields
   * available in {@link ChronoField}.
   * If the field is not supported an {@code UnsupportedTemporalTypeException} must be thrown.
   * <p>
   * Implementations must not alter the specified temporal object.
   * Instead, an adjusted copy of the original must be returned.
   * This provides equivalent, safe behavior for immutable and mutable implementations.
   *
   * @param <R>  the type of the Temporal object
   * @param temporal the temporal object to adjust, not null
   * @param newValue the new value of the field
   * @return the adjusted temporal object, not null
   * @throws DateTimeException if the field cannot be set
   * @throws UnsupportedTemporalTypeException if the field is not supported by the temporal
   * @throws ArithmeticException if numeric overflow occurs
   */
  def adjustInto(temporal: R, newValue: Long): R

  /**
   * Resolves this field to provide a simpler alternative or a date.
   * <p>
   * This method is invoked during the resolve phase of parsing.
   * It is designed to allow application defined fields to be simplified into
   * more standard fields, such as those on {@code ChronoField}, or into a date.
   * <p>
   * Applications should not normally invoke this method directly.
   *
   * @implSpec
     * If an implementation represents a field that can be simplified, or
   *   combined with others, then this method must be implemented.
   *   <p>
   *   The specified map contains the current state of the parse.
   *   The map is mutable and must be mutated to resolve the field and
   *   any related fields. This method will only be invoked during parsing
   *   if the map contains this field, and implementations should therefore
   *   assume this field is present.
   *   <p>
   *   Resolving a field will consist of looking at the value of this field,
   *   and potentially other fields, and either updating the map with a
   *   simpler value, such as a { @code ChronoField}, or returning a
   *                                    complete { @code ChronoLocalDate}. If a resolve is successful,
   *                                                     the code must remove all the fields that were resolved from the map,
   *                                                     including this field.
   *                                                     <p>
   *                                                     For example, the { @code IsoFields} class contains the quarter-of-year
   *                                                                              and day-of-quarter fields. The implementation of this method in that class
   *                                                                              resolves the two fields plus the { @link ChronoField#YEAR YEAR} into a
   *                                                                                                                       complete { @code Date}. The resolve method will remove all three
   *                                                                                                                                        fields from the map before returning the { @code Date}.
   *                                                                                                                                                                                         <p>
   *                                                                                                                                                                                         If resolution should be possible, but the data is invalid, the resolver
   *                                                                                                                                                                                         style should be used to determine an appropriate level of leniency, which
   *                                                                                                                                                                                         may require throwing a { @code DateTimeException} or { @code ArithmeticException}.
   *                                                                                                                                                                                                                                                      If no resolution is possible, the resolve method must return null.
   *                                                                                                                                                                                                                                                      <p>
   *                                                                                                                                                                                                                                                      When resolving time fields, the map will be altered and null returned.
   *                                                                                                                                                                                                                                                      When resolving date fields, the date is normally returned from the method,
   *                                                                                                                                                                                                                                                      with the map altered to remove the resolved fields. However, it would also
   *                                                                                                                                                                                                                                                      be acceptable for the date fields to be resolved into other { @code ChronoField}
   *                                                                                                                                                                                                                                                                                                                          instances that can produce a date, such as { @code EPOCH_DAY}.
   *                                                                                                                                                                                                                                                                                                                                                                             <p>
   *                                                                                                                                                                                                                                                                                                                                                                             Not all { @code TemporalAccessor} implementations are accepted as return values.
   *                                                                                                                                                                                                                                                                                                                                                                                             Implementations must accept { @code ChronoLocalDate}, { @code ChronoLocalDateTime},
   *                                                                                                                                                                                                                                                                                                                                                                                                                                                           { @code ChronoZonedDateTime} and { @code Time}.
   *                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    <p>
   *                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    The zone is not normally required for resolution, but is provided for completeness.
   *                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    <p>
   *                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    The default implementation must return null.
   *
   * @param fieldValues  the map of fields to values, which can be updated, not null
   * @param chronology  the effective chronology, not null
   * @param zone  the effective zone, not null
   * @param resolverStyle  the requested type of resolve, not null
   * @return the resolved temporal object; null if resolving only
   *         changed the map, or no resolve occurred
   * @throws ArithmeticException if numeric overflow occurs
   * @throws DateTimeException if resolving results in an error. This must not be thrown
   *                           by querying a field on the temporal without first checking if it is supported
   */
  def resolve(fieldValues: Nothing, chronology: Chronology, zone: Nothing, resolverStyle: Nothing): TemporalAccessor = {
    return null
  }

  /**
   * Gets a descriptive name for the field.
   * <p>
   * The should be of the format 'BaseOfRange', such as 'MonthOfYear',
   * unless the field has a range of {@code FOREVER}, when only
   * the base unit is mentioned, such as 'Year' or 'Era'.
   *
   * @return the name of the field, not null
   */
  override def toString: String
}

/*
 * Copyright (c) 2012, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
/*
 * This file is available under and governed by the GNU General Public
 * License version 2 only, as published by the Free Software Foundation.
 * However, the following notice accompanied the original version of this
 * file:
 *
 * Copyright (c) 2007-2012, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/**
 * Common implementations of {@code TemporalQuery}.
 * <p>
 * This class provides common implementations of {@link TemporalQuery}.
 * These are defined here as they must be constants, and the definition
 * of lambdas does not guarantee that. By assigning them once here,
 * they become 'normal' Java constants.
 *
 * @since 1.8
 */
final object TemporalQueries {
  /**
   * A strict query for the {@code ZoneId}.
   */
  private[temporal] final val ZONE_ID: TemporalQuery[Nothing] = (temporal) -> {
    return temporal.query(ZONE_ID);
  }
  /**
   * A query for the {@code Chronology}.
   */
  private[temporal] final val CHRONO: TemporalQuery[Chronology] = (temporal) -> {
    return temporal.query(CHRONO);
  }
  /**
   * A query for the smallest supported unit.
   */
  private[temporal] final val PRECISION: TemporalQuery[TemporalUnit] = (temporal) -> {
    return temporal.query(PRECISION);
  }
  /**
   * A lenient query for the {@code ZoneId}, falling back to the {@code ZoneOffset}.
   */
  private[temporal] final val ZONE: TemporalQuery[Nothing] = (temporal) -> {
    ZoneId zone = temporal.query(ZONE_ID);
    return (zone != null ? zone: temporal.query(OFFSET));
  }
  /**
   * A query for {@code ZoneOffset} returning null if not found.
   */
  private[temporal] final val OFFSET: TemporalQuery[ZoneOffset] = (temporal) -> {
    if (temporal.isSupported(OFFSET_SECONDS)) {
      return ZoneOffset.ofTotalSeconds(temporal.get(OFFSET_SECONDS));
    }
    return null;
  }
  /**
   * A query for {@code Date} returning null if not found.
   */
  private[temporal] final val LOCAL_DATE: TemporalQuery[Nothing] = (temporal) -> {
    if (temporal.isSupported(EPOCH_DAY)) {
      return LocalDate.ofEpochDay(temporal.getLong(EPOCH_DAY));
    }
    return null;
  }
  /**
   * A query for {@code Time} returning null if not found.
   */
  private[temporal] final val LOCAL_TIME: TemporalQuery[Nothing] = (temporal) -> {
    if (temporal.isSupported(NANO_OF_DAY)) {
      return LocalTime.ofNanoOfDay(temporal.getLong(NANO_OF_DAY));
    }
    return null;
  }
}

final class TemporalQueries {
  /**
   * Private constructor since this is a utility class.
   */
  private def this() {
    this()
  }
}

/*
 * Copyright (c) 2012, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
/*
 * This file is available under and governed by the GNU General Public
 * License version 2 only, as published by the Free Software Foundation.
 * However, the following notice accompanied the original version of this
 * file:
 *
 * Copyright (c) 2012, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/**
 * Strategy for querying a temporal object.
 * <p>
 * Queries are a key tool for extracting information from temporal objects.
 * They exist to externalize the process of querying, permitting different
 * approaches, as per the strategy design pattern.
 * Examples might be a query that checks if the date is the day before February 29th
 * in a leap year, or calculates the number of days to your next birthday.
 * <p>
 * The {@link TemporalField} interface provides another mechanism for querying
 * temporal objects. That interface is limited to returning a {@code long}.
 * By contrast, queries can return any type.
 * <p>
 * There are two equivalent ways of using a {@code TemporalQuery}.
 * The first is to invoke the method on this interface directly.
 * The second is to use {@link TemporalAccessor#query(TemporalQuery)}:
 * <pre>
 * // these two lines are equivalent, but the second approach is recommended
 * temporal = thisQuery.queryFrom(temporal);
 * temporal = temporal.query(thisQuery);
 * </pre>
 * It is recommended to use the second approach, {@code query(TemporalQuery)},
 * as it is a lot clearer to read in code.
 * <p>
 * The most common implementations are method references, such as
 * {@code Date::from} and {@code ZoneId::from}.
 * Additional common implementations are provided on this interface as static methods.
 *
 * @implSpec
 * This interface places no restrictions on the mutability of implementations,
 * however immutability is strongly recommended.
 *
 * @since 1.8
 */
@FunctionalInterface object TemporalQuery {
  /**
   * A strict query for the {@code ZoneId}.
   * <p>
   * This queries a {@code TemporalAccessor} for the zone.
   * The zone is only returned if the date-time conceptually contains a {@code ZoneId}.
   * It will not be returned if the date-time only conceptually has an {@code ZoneOffset}.
   * Thus a {@link java.time.ZonedDateTime} will return the result of {@code getZone()},
   * but an {@link java.time.OffsetDateTime} will return null.
   * <p>
   * In most cases, applications should use {@link #zone()} as this query is too strict.
   * <p>
   * The result from JDK classes implementing {@code TemporalAccessor} is as follows:<br>
   * {@code Date} returns null<br>
   * {@code Time} returns null<br>
   * {@code DateTime} returns null<br>
   * {@code ZonedDateTime} returns the associated zone<br>
   * {@code OffsetTime} returns null<br>
   * {@code OffsetDateTime} returns null<br>
   * {@code ChronoLocalDate} returns null<br>
   * {@code ChronoLocalDateTime} returns null<br>
   * {@code ChronoZonedDateTime} returns the associated zone<br>
   * {@code Era} returns null<br>
   * {@code DayOfWeek} returns null<br>
   * {@code Month} returns null<br>
   * {@code Year} returns null<br>
   * {@code YearMonth} returns null<br>
   * {@code MonthDay} returns null<br>
   * {@code ZoneOffset} returns null<br>
   * {@code Instant} returns null<br>
   *
   * @return a query that can obtain the zone ID of a temporal, not null
   */
  def zoneId: TemporalQuery[Nothing] = {
    return TemporalQueries.ZONE_ID
  }

  /**
   * A query for the {@code Chronology}.
   * <p>
   * This queries a {@code TemporalAccessor} for the chronology.
   * If the target {@code TemporalAccessor} represents a date, or part of a date,
   * then it should return the chronology that the date is expressed in.
   * As a result of this definition, objects only representing time, such as
   * {@code Time}, will return null.
   * <p>
   * The result from JDK classes implementing {@code TemporalAccessor} is as follows:<br>
   * {@code Date} returns {@code IsoChronology.INSTANCE}<br>
   * {@code Time} returns null (does not represent a date)<br>
   * {@code DateTime} returns {@code IsoChronology.INSTANCE}<br>
   * {@code ZonedDateTime} returns {@code IsoChronology.INSTANCE}<br>
   * {@code OffsetTime} returns null (does not represent a date)<br>
   * {@code OffsetDateTime} returns {@code IsoChronology.INSTANCE}<br>
   * {@code ChronoLocalDate} returns the associated chronology<br>
   * {@code ChronoLocalDateTime} returns the associated chronology<br>
   * {@code ChronoZonedDateTime} returns the associated chronology<br>
   * {@code Era} returns the associated chronology<br>
   * {@code DayOfWeek} returns null (shared across chronologies)<br>
   * {@code Month} returns {@code IsoChronology.INSTANCE}<br>
   * {@code Year} returns {@code IsoChronology.INSTANCE}<br>
   * {@code YearMonth} returns {@code IsoChronology.INSTANCE}<br>
   * {@code MonthDay} returns null {@code IsoChronology.INSTANCE}<br>
   * {@code ZoneOffset} returns null (does not represent a date)<br>
   * {@code Instant} returns null (does not represent a date)<br>
   * <p>
   * The method {@link java.time.chrono.Chronology#from(TemporalAccessor)} can be used as a
   * {@code TemporalQuery} via a method reference, {@code Chronology::from}.
   * That method is equivalent to this query, except that it throws an
   * exception if a chronology cannot be obtained.
   *
   * @return a query that can obtain the chronology of a temporal, not null
   */
  def chronology: TemporalQuery[Chronology] = {
    return TemporalQueries.CHRONO
  }

  /**
   * A query for the smallest supported unit.
   * <p>
   * This queries a {@code TemporalAccessor} for the time precision.
   * If the target {@code TemporalAccessor} represents a consistent or complete date-time,
   * date or time then this must return the smallest precision actually supported.
   * Note that fields such as {@code NANO_OF_DAY} and {@code NANO_OF_SECOND}
   * are defined to always return ignoring the precision, thus this is the only
   * way to find the actual smallest supported unit.
   * For example, were {@code GregorianCalendar} to implement {@code TemporalAccessor}
   * it would return a precision of {@code MILLIS}.
   * <p>
   * The result from JDK classes implementing {@code TemporalAccessor} is as follows:<br>
   * {@code Date} returns {@code DAYS}<br>
   * {@code Time} returns {@code NANOS}<br>
   * {@code DateTime} returns {@code NANOS}<br>
   * {@code ZonedDateTime} returns {@code NANOS}<br>
   * {@code OffsetTime} returns {@code NANOS}<br>
   * {@code OffsetDateTime} returns {@code NANOS}<br>
   * {@code ChronoLocalDate} returns {@code DAYS}<br>
   * {@code ChronoLocalDateTime} returns {@code NANOS}<br>
   * {@code ChronoZonedDateTime} returns {@code NANOS}<br>
   * {@code Era} returns {@code ERAS}<br>
   * {@code DayOfWeek} returns {@code DAYS}<br>
   * {@code Month} returns {@code MONTHS}<br>
   * {@code Year} returns {@code YEARS}<br>
   * {@code YearMonth} returns {@code MONTHS}<br>
   * {@code MonthDay} returns null (does not represent a complete date or time)<br>
   * {@code ZoneOffset} returns null (does not represent a date or time)<br>
   * {@code Instant} returns {@code NANOS}<br>
   *
   * @return a query that can obtain the precision of a temporal, not null
   */
  def precision: TemporalQuery[TemporalUnit] = {
    return TemporalQueries.PRECISION
  }

  /**
   * A lenient query for the {@code ZoneId}, falling back to the {@code ZoneOffset}.
   * <p>
   * This queries a {@code TemporalAccessor} for the zone.
   * It first tries to obtain the zone, using {@link #zoneId()}.
   * If that is not found it tries to obtain the {@link #offset()}.
   * Thus a {@link java.time.ZonedDateTime} will return the result of {@code getZone()},
   * while an {@link java.time.OffsetDateTime} will return the result of {@code getOffset()}.
   * <p>
   * In most cases, applications should use this query rather than {@code #zoneId()}.
   * <p>
   * The method {@link ZoneId#from(TemporalAccessor)} can be used as a
   * {@code TemporalQuery} via a method reference, {@code ZoneId::from}.
   * That method is equivalent to this query, except that it throws an
   * exception if a zone cannot be obtained.
   *
   * @return a query that can obtain the zone ID or offset of a temporal, not null
   */
  def zone: TemporalQuery[Nothing] = {
    return TemporalQueries.ZONE
  }

  /**
   * A query for {@code ZoneOffset} returning null if not found.
   * <p>
   * This returns a {@code TemporalQuery} that can be used to query a temporal
   * object for the offset. The query will return null if the temporal
   * object cannot supply an offset.
   * <p>
   * The query implementation examines the {@link ChronoField#OFFSET_SECONDS OFFSET_SECONDS}
   * field and uses it to create a {@code ZoneOffset}.
   * <p>
   * The method {@link java.time.ZoneOffset#from(TemporalAccessor)} can be used as a
   * {@code TemporalQuery} via a method reference, {@code ZoneOffset::from}.
   * This query and {@code ZoneOffset::from} will return the same result if the
   * temporal object contains an offset. If the temporal object does not contain
   * an offset, then the method reference will throw an exception, whereas this
   * query will return null.
   *
   * @return a query that can obtain the offset of a temporal, not null
   */
  def offset: TemporalQuery[ZoneOffset] = {
    return TemporalQueries.OFFSET
  }

  /**
   * A query for {@code Date} returning null if not found.
   * <p>
   * This returns a {@code TemporalQuery} that can be used to query a temporal
   * object for the local date. The query will return null if the temporal
   * object cannot supply a local date.
   * <p>
   * The query implementation examines the {@link ChronoField#EPOCH_DAY EPOCH_DAY}
   * field and uses it to create a {@code Date}.
   * <p>
   * The method {@link ZoneOffset#from(TemporalAccessor)} can be used as a
   * {@code TemporalQuery} via a method reference, {@code Date::from}.
   * This query and {@code Date::from} will return the same result if the
   * temporal object contains a date. If the temporal object does not contain
   * a date, then the method reference will throw an exception, whereas this
   * query will return null.
   *
   * @return a query that can obtain the date of a temporal, not null
   */
  def localDate: TemporalQuery[Nothing] = {
    return TemporalQueries.LOCAL_DATE
  }

  /**
   * A query for {@code Time} returning null if not found.
   * <p>
   * This returns a {@code TemporalQuery} that can be used to query a temporal
   * object for the local time. The query will return null if the temporal
   * object cannot supply a local time.
   * <p>
   * The query implementation examines the {@link ChronoField#NANO_OF_DAY NANO_OF_DAY}
   * field and uses it to create a {@code Time}.
   * <p>
   * The method {@link ZoneOffset#from(TemporalAccessor)} can be used as a
   * {@code TemporalQuery} via a method reference, {@code Time::from}.
   * This query and {@code Time::from} will return the same result if the
   * temporal object contains a time. If the temporal object does not contain
   * a time, then the method reference will throw an exception, whereas this
   * query will return null.
   *
   * @return a query that can obtain the time of a temporal, not null
   */
  def localTime: TemporalQuery[Nothing] = {
    return TemporalQueries.LOCAL_TIME
  }
}

@FunctionalInterface abstract trait TemporalQuery {
  /**
   * Queries the specified temporal object.
   * <p>
   * This queries the specified temporal object to return an object using the logic
   * encapsulated in the implementing class.
   * Examples might be a query that checks if the date is the day before February 29th
   * in a leap year, or calculates the number of days to your next birthday.
   * <p>
   * There are two equivalent ways of using this method.
   * The first is to invoke this method directly.
   * The second is to use {@link TemporalAccessor#query(TemporalQuery)}:
   * <pre>
   * // these two lines are equivalent, but the second approach is recommended
   * temporal = thisQuery.queryFrom(temporal);
   * temporal = temporal.query(thisQuery);
   * </pre>
   * It is recommended to use the second approach, {@code query(TemporalQuery)},
   * as it is a lot clearer to read in code.
   *
   * @implSpec
     * The implementation must take the input object and query it.
   *   The implementation defines the logic of the query and is responsible for
   *   documenting that logic.
   *   It may use any method on { @code TemporalAccessor} to determine the result.
   *                                    The input object must not be altered.
   *                                    <p>
   *                                    The input temporal object may be in a calendar system other than ISO.
   *                                    Implementations may choose to document compatibility with other calendar systems,
   *                                    or reject non-ISO temporal objects by { @link TemporalQuery#chronology() querying the chronology}.
   *                                                                                  <p>
   *                                                                                  This method may be called from multiple threads in parallel.
   *                                                                                  It must be thread-safe when invoked.
   *
   * @param temporal  the temporal object to query, not null
   * @return the queried value, may return null to indicate not found
   * @throws DateTimeException if unable to query
   * @throws ArithmeticException if numeric overflow occurs
   */
  def queryFrom(temporal: TemporalAccessor): R
}

/*
 * Copyright (c) 2012, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
/*
 * This file is available under and governed by the GNU General Public
 * License version 2 only, as published by the Free Software Foundation.
 * However, the following notice accompanied the original version of this
 * file:
 *
 * Copyright (c) 2012, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/**
 * A unit of date-time, such as Days or Hours.
 * <p>
 * Measurement of time is built on units, such as years, months, days, hours, minutes and seconds.
 * Implementations of this interface represent those units.
 * <p>
 * An instance of this interface represents the unit itself, rather than an amount of the unit.
 * See {@link Period} for a class that represents an amount in terms of the common units.
 * <p>
 * The most commonly used units are defined in {@link ChronoUnit}.
 * Further units are supplied in {@link IsoFields}.
 * Units can also be written by application code by implementing this interface.
 * <p>
 * The unit works using double dispatch. Client code calls methods on a date-time like
 * {@code DateTime} which check if the unit is a {@code ChronoUnit}.
 * If it is, then the date-time must handle it.
 * Otherwise, the method call is re-dispatched to the matching method in this interface.
 *
 * @implSpec
 * This interface must be implemented with care to ensure other classes operate correctly.
 * All implementations that can be instantiated must be final, immutable and thread-safe.
 * It is recommended to use an enum where possible.
 *
 * @since 1.8
 */
abstract trait TemporalUnit {
  /**
   * Gets the duration of this unit, which may be an estimate.
   * <p>
   * All units return a duration measured in standard nanoseconds from this method.
   * The duration will be positive and non-zero.
   * For example, an hour has a duration of {@code 60 * 60 * 1,000,000,000ns}.
   * <p>
   * Some units may return an accurate duration while others return an estimate.
   * For example, days have an estimated duration due to the possibility of
   * daylight saving time changes.
   * To determine if the duration is an estimate, use {@link #isDurationEstimated()}.
   *
   * @return the duration of this unit, which may be an estimate, not null
   */
  def getDuration: Nothing

  /**
   * Checks if the duration of the unit is an estimate.
   * <p>
   * All units have a duration, however the duration is not always accurate.
   * For example, days have an estimated duration due to the possibility of
   * daylight saving time changes.
   * This method returns true if the duration is an estimate and false if it is
   * accurate. Note that accurate/estimated ignores leap seconds.
   *
   * @return true if the duration is estimated, false if accurate
   */
  def isDurationEstimated: Boolean

  /**
   * Checks if this unit represents a component of a date.
   * <p>
   * A date is time-based if it can be used to imply meaning from a date.
   * It must have a {@linkplain #getDuration() duration} that is an integral
   * multiple of the length of a standard day.
   * Note that it is valid for both {@code isDateBased()} and {@code isTimeBased()}
   * to return false, such as when representing a unit like 36 hours.
   *
   * @return true if this unit is a component of a date
   */
  def isDateBased: Boolean

  /**
   * Checks if this unit represents a component of a time.
   * <p>
   * A unit is time-based if it can be used to imply meaning from a time.
   * It must have a {@linkplain #getDuration() duration} that divides into
   * the length of a standard day without remainder.
   * Note that it is valid for both {@code isDateBased()} and {@code isTimeBased()}
   * to return false, such as when representing a unit like 36 hours.
   *
   * @return true if this unit is a component of a time
   */
  def isTimeBased: Boolean

  /**
   * Checks if this unit is supported by the specified temporal object.
   * <p>
   * This checks that the implementing date-time can add/subtract this unit.
   * This can be used to avoid throwing an exception.
   * <p>
   * This default implementation derives the value using
   * {@link Temporal#plus(long, TemporalUnit)}.
   *
   * @param temporal  the temporal object to check, not null
   * @return true if the unit is supported
   */
  def isSupportedBy(temporal: Temporal): Boolean = {
    if (temporal.isInstanceOf[Nothing]) {
      return isTimeBased
    }
    if (temporal.isInstanceOf[ChronoLocalDate]) {
      return isDateBased
    }
    if (temporal.isInstanceOf[Nothing] || temporal.isInstanceOf[Nothing]) {
      return true
    }
    try {
      temporal.plus(1, this)
      return true
    }
    catch {
      case ex: UnsupportedTemporalTypeException => {
        return false
      }
      case ex: RuntimeException => {
        try {
          temporal.plus(-1, this)
          return true
        }
        catch {
          case ex2: RuntimeException => {
            return false
          }
        }
      }
    }
  }

  /**
   * Returns a copy of the specified temporal object with the specified period added.
   * <p>
   * The period added is a multiple of this unit. For example, this method
   * could be used to add "3 days" to a date by calling this method on the
   * instance representing "days", passing the date and the period "3".
   * The period to be added may be negative, which is equivalent to subtraction.
   * <p>
   * There are two equivalent ways of using this method.
   * The first is to invoke this method directly.
   * The second is to use {@link Temporal#plus(long, TemporalUnit)}:
   * <pre>
   * // these two lines are equivalent, but the second approach is recommended
   * temporal = thisUnit.addTo(temporal);
   * temporal = temporal.plus(thisUnit);
   * </pre>
   * It is recommended to use the second approach, {@code plus(TemporalUnit)},
   * as it is a lot clearer to read in code.
   * <p>
   * Implementations should perform any queries or calculations using the units
   * available in {@link ChronoUnit} or the fields available in {@link ChronoField}.
   * If the unit is not supported an {@code UnsupportedTemporalTypeException} must be thrown.
   * <p>
   * Implementations must not alter the specified temporal object.
   * Instead, an adjusted copy of the original must be returned.
   * This provides equivalent, safe behavior for immutable and mutable implementations.
   *
   * @param <R>  the type of the Temporal object
   * @param temporal  the temporal object to adjust, not null
   * @param amount  the amount of this unit to add, positive or negative
   * @return the adjusted temporal object, not null
   * @throws DateTimeException if the period cannot be added
   * @throws UnsupportedTemporalTypeException if the unit is not supported by the temporal
   */
  def addTo(temporal: R, amount: Long): R

  /**
   * Calculates the amount of time between two temporal objects.
   * <p>
   * This calculates the amount in terms of this unit. The start and end
   * points are supplied as temporal objects and must be of compatible types.
   * The implementation will convert the second type to be an instance of the
   * first type before the calculating the amount.
   * The result will be negative if the end is before the start.
   * For example, the amount in hours between two temporal objects can be
   * calculated using {@code HOURS.between(startTime, endTime)}.
   * <p>
   * The calculation returns a whole number, representing the number of
   * complete units between the two temporals.
   * For example, the amount in hours between the times 11:30 and 13:29
   * will only be one hour as it is one minute short of two hours.
   * <p>
   * There are two equivalent ways of using this method.
   * The first is to invoke this method directly.
   * The second is to use {@link Temporal#until(Temporal, TemporalUnit)}:
   * <pre>
   * // these two lines are equivalent
   * between = thisUnit.between(start, end);
   * between = start.until(end, thisUnit);
   * </pre>
   * The choice should be made based on which makes the code more readable.
   * <p>
   * For example, this method allows the number of days between two dates to
   * be calculated:
   * <pre>
   * long daysBetween = DAYS.between(start, end);
   * // or alternatively
   * long daysBetween = start.until(end, DAYS);
   * </pre>
   * <p>
   * Implementations should perform any queries or calculations using the units
   * available in {@link ChronoUnit} or the fields available in {@link ChronoField}.
   * If the unit is not supported an {@code UnsupportedTemporalTypeException} must be thrown.
   * Implementations must not alter the specified temporal objects.
   *
   * @implSpec
     * Implementations must begin by checking to if the two temporals have the
   *   same type using { @code getClass()}. If they do not, then the result must be
   *                           obtained by calling { @code temporal1Inclusive.until(temporal2Exclusive, this)}.
   *
   * @param temporal1Inclusive  the base temporal object, not null
   * @param temporal2Exclusive  the other temporal object, not null
   * @return the amount of time between temporal1Inclusive and temporal2Exclusive
   *         in terms of this unit; positive if temporal2Exclusive is later than
   *         temporal1Inclusive, negative if earlier
   * @throws DateTimeException if the amount cannot be calculated, or the end
   *                           temporal cannot be converted to the same type as the start temporal
   * @throws UnsupportedTemporalTypeException if the unit is not supported by the temporal
   * @throws ArithmeticException if numeric overflow occurs
   */
  def between(temporal1Inclusive: Temporal, temporal2Exclusive: Temporal): Long

  /**
   * Gets a descriptive name for the unit.
   * <p>
   * This should be in the plural and upper-first camel case, such as 'Days' or 'Minutes'.
   *
   * @return the name of this unit, not null
   */
  override def toString: String
}

/*
 * Copyright (c) 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
/*
 * This file is available under and governed by the GNU General Public
 * License version 2 only, as published by the Free Software Foundation.
 * However, the following notice accompanied the original version of this
 * file:
 *
 * Copyright (c) 2013, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/**
 * UnsupportedTemporalTypeException indicates that a ChronoField or ChronoUnit is
 * not supported for a Temporal class.
 *
 * @implSpec
 * This class is intended for use in a single thread.
 *
 * @since 1.8
 */
object UnsupportedTemporalTypeException {
  /**
   * Serialization version.
   */
  private final val serialVersionUID: Long = -6158898438688206006L
}

class UnsupportedTemporalTypeException extends DateTimeException {
  /**
   * Constructs a new UnsupportedTemporalTypeException with the specified message.
   *
   * @param message  the message to use for this exception, may be null
   */
  def this(message: String) {
    this()
    `super`(message)
  }

  /**
   * Constructs a new UnsupportedTemporalTypeException with the specified message and cause.
   *
   * @param message  the message to use for this exception, may be null
   * @param cause  the cause of the exception, may be null
   */
  def this(message: String, cause: Throwable) {
    this()
    `super`(message, cause)
  }
}

/*
 * Copyright (c) 2012, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
/*
 * This file is available under and governed by the GNU General Public
 * License version 2 only, as published by the Free Software Foundation.
 * However, the following notice accompanied the original version of this
 * file:
 *
 * Copyright (c) 2011-2012, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/**
 * The range of valid values for a date-time field.
 * <p>
 * All {@link TemporalField} instances have a valid range of values.
 * For example, the ISO day-of-month runs from 1 to somewhere between 28 and 31.
 * This class captures that valid range.
 * <p>
 * It is important to be aware of the limitations of this class.
 * Only the minimum and maximum values are provided.
 * It is possible for there to be invalid values within the outer range.
 * For example, a weird field may have valid values of 1, 2, 4, 6, 7, thus
 * have a range of '1 - 7', despite that fact that values 3 and 5 are invalid.
 * <p>
 * Instances of this class are not tied to a specific field.
 *
 * @implSpec
 * This class is immutable and thread-safe.
 *
 * @since 1.8
 */
final object ValueRange {
  /**
   * Obtains a fixed value range.
   * <p>
   * This factory obtains a range where the minimum and maximum values are fixed.
   * For example, the ISO month-of-year always runs from 1 to 12.
   *
   * @param min  the minimum value
   * @param max  the maximum value
   * @return the ValueRange for min, max, not null
   * @throws IllegalArgumentException if the minimum is greater than the maximum
   */
  def of(min: Long, max: Long): ValueRange = {
    if (min > max) {
      throw new IllegalArgumentException("Minimum value must be less than maximum value")
    }
    return new ValueRange(min, min, max, max)
  }

  /**
   * Obtains a variable value range.
   * <p>
   * This factory obtains a range where the minimum value is fixed and the maximum value may vary.
   * For example, the ISO day-of-month always starts at 1, but ends between 28 and 31.
   *
   * @param min  the minimum value
   * @param maxSmallest  the smallest maximum value
   * @param maxLargest  the largest maximum value
   * @return the ValueRange for min, smallest max, largest max, not null
   * @throws IllegalArgumentException if
   *                                  the minimum is greater than the smallest maximum,
   *                                  or the smallest maximum is greater than the largest maximum
   */
  def of(min: Long, maxSmallest: Long, maxLargest: Long): ValueRange = {
    return of(min, min, maxSmallest, maxLargest)
  }

  /**
   * Obtains a fully variable value range.
   * <p>
   * This factory obtains a range where both the minimum and maximum value may vary.
   *
   * @param minSmallest  the smallest minimum value
   * @param minLargest  the largest minimum value
   * @param maxSmallest  the smallest maximum value
   * @param maxLargest  the largest maximum value
   * @return the ValueRange for smallest min, largest min, smallest max, largest max, not null
   * @throws IllegalArgumentException if
   *                                  the smallest minimum is greater than the smallest maximum,
   *                                  or the smallest maximum is greater than the largest maximum
   *                                  or the largest minimum is greater than the largest maximum
   */
  def of(minSmallest: Long, minLargest: Long, maxSmallest: Long, maxLargest: Long): ValueRange = {
    if (minSmallest > minLargest) {
      throw new IllegalArgumentException("Smallest minimum value must be less than largest minimum value")
    }
    if (maxSmallest > maxLargest) {
      throw new IllegalArgumentException("Smallest maximum value must be less than largest maximum value")
    }
    if (minLargest > maxLargest) {
      throw new IllegalArgumentException("Minimum value must be less than maximum value")
    }
    return new ValueRange(minSmallest, minLargest, maxSmallest, maxLargest)
  }

  /**
   * Serialization version.
   */
  private final val serialVersionUID: Long = -7317881728594519368L
}

final class ValueRange extends Serializable {
  /**
   * Restrictive constructor.
   *
   * @param minSmallest  the smallest minimum value
   * @param minLargest  the largest minimum value
   * @param maxSmallest  the smallest minimum value
   * @param maxLargest  the largest minimum value
   */
  private def this(minSmallest: Long, minLargest: Long, maxSmallest: Long, maxLargest: Long) {
    this()
    this.minSmallest = minSmallest
    this.minLargest = minLargest
    this.maxSmallest = maxSmallest
    this.maxLargest = maxLargest
  }

  /**
   * Is the value range fixed and fully known.
   * <p>
   * For example, the ISO day-of-month runs from 1 to between 28 and 31.
   * Since there is uncertainty about the maximum value, the range is not fixed.
   * However, for the month of January, the range is always 1 to 31, thus it is fixed.
   *
   * @return true if the set of values is fixed
   */
  def isFixed: Boolean = {
    return minSmallest == minLargest && maxSmallest == maxLargest
  }

  /**
   * Gets the minimum value that the field can take.
   * <p>
   * For example, the ISO day-of-month always starts at 1.
   * The minimum is therefore 1.
   *
   * @return the minimum value for this field
   */
  def getMinimum: Long = {
    return minSmallest
  }

  /**
   * Gets the largest possible minimum value that the field can take.
   * <p>
   * For example, the ISO day-of-month always starts at 1.
   * The largest minimum is therefore 1.
   *
   * @return the largest possible minimum value for this field
   */
  def getLargestMinimum: Long = {
    return minLargest
  }

  /**
   * Gets the smallest possible maximum value that the field can take.
   * <p>
   * For example, the ISO day-of-month runs to between 28 and 31 days.
   * The smallest maximum is therefore 28.
   *
   * @return the smallest possible maximum value for this field
   */
  def getSmallestMaximum: Long = {
    return maxSmallest
  }

  /**
   * Gets the maximum value that the field can take.
   * <p>
   * For example, the ISO day-of-month runs to between 28 and 31 days.
   * The maximum is therefore 31.
   *
   * @return the maximum value for this field
   */
  def getMaximum: Long = {
    return maxLargest
  }

  /**
   * Checks if all values in the range fit in an {@code int}.
   * <p>
   * This checks that all valid values are within the bounds of an {@code int}.
   * <p>
   * For example, the ISO month-of-year has values from 1 to 12, which fits in an {@code int}.
   * By comparison, ISO nano-of-day runs from 1 to 86,400,000,000,000 which does not fit in an {@code int}.
   * <p>
   * This implementation uses {@link #getMinimum()} and {@link #getMaximum()}.
   *
   * @return true if a valid value always fits in an { @code int}
   */
  def isIntValue: Boolean = {
    return getMinimum >= Integer.MIN_VALUE && getMaximum <= Integer.MAX_VALUE
  }

  /**
   * Checks if the value is within the valid range.
   * <p>
   * This checks that the value is within the stored range of values.
   *
   * @param value  the value to check
   * @return true if the value is valid
   */
  def isValidValue(value: Long): Boolean = {
    return (value >= getMinimum && value <= getMaximum)
  }

  /**
   * Checks if the value is within the valid range and that all values
   * in the range fit in an {@code int}.
   * <p>
   * This method combines {@link #isIntValue()} and {@link #isValidValue(long)}.
   *
   * @param value  the value to check
   * @return true if the value is valid and fits in an { @code int}
   */
  def isValidIntValue(value: Long): Boolean = {
    return isIntValue && isValidValue(value)
  }

  /**
   * Checks that the specified value is valid.
   * <p>
   * This validates that the value is within the valid range of values.
   * The field is only used to improve the error message.
   *
   * @param value  the value to check
   * @param field  the field being checked, may be null
   * @return the value that was passed in
   * @see #isValidValue(long)
   */
  def checkValidValue(value: Long, field: TemporalField): Long = {
    if (isValidValue(value) == false) {
      throw new Nothing(genInvalidFieldMessage(field, value))
    }
    return value
  }

  /**
   * Checks that the specified value is valid and fits in an {@code int}.
   * <p>
   * This validates that the value is within the valid range of values and that
   * all valid values are within the bounds of an {@code int}.
   * The field is only used to improve the error message.
   *
   * @param value  the value to check
   * @param field  the field being checked, may be null
   * @return the value that was passed in
   * @see #isValidIntValue(long)
   */
  def checkValidIntValue(value: Long, field: TemporalField): Int = {
    if (isValidIntValue(value) == false) {
      throw new Nothing(genInvalidFieldMessage(field, value))
    }
    return value.asInstanceOf[Int]
  }

  private def genInvalidFieldMessage(field: TemporalField, value: Long): String = {
    if (field != null) {
      return "Invalid value for " + field + " (valid values " + this + "): " + value
    }
    else {
      return "Invalid value (valid values " + this + "): " + value
    }
  }

  /**
   * Checks if this range is equal to another range.
   * <p>
   * The comparison is based on the four values, minimum, largest minimum,
   * smallest maximum and maximum.
   * Only objects of type {@code ValueRange} are compared, other types return false.
   *
   * @param obj  the object to check, null returns false
   * @return true if this is equal to the other range
   */
  override def equals(obj: AnyRef): Boolean = {
    if (obj eq this) {
      return true
    }
    if (obj.isInstanceOf[ValueRange]) {
      val other: ValueRange = obj.asInstanceOf[ValueRange]
      return minSmallest == other.minSmallest && minLargest == other.minLargest && maxSmallest == other.maxSmallest && maxLargest == other.maxLargest
    }
    return false
  }

  /**
   * A hash code for this range.
   *
   * @return a suitable hash code
   */
  override def hashCode: Int = {
    val hash: Long = minSmallest + minLargest << 16 + minLargest >> 48 + maxSmallest << 32 + maxSmallest >> 32 + maxLargest << 48 + maxLargest >> 16
    return (hash ^ (hash >>> 32)).asInstanceOf[Int]
  }

  /**
   * Outputs this range as a {@code String}.
   * <p>
   * The format will be '{min}/{largestMin} - {smallestMax}/{max}',
   * where the largestMin or smallestMax sections may be omitted, together
   * with associated slash, if they are the same as the min or max.
   *
   * @return a string representation of this range, not null
   */
  override def toString: String = {
    val buf: StringBuilder = new StringBuilder
    buf.append(minSmallest)
    if (minSmallest != minLargest) {
      buf.append('/').append(minLargest)
    }
    buf.append(" - ").append(maxSmallest)
    if (maxSmallest != maxLargest) {
      buf.append('/').append(maxLargest)
    }
    return buf.toString
  }

  /**
   * The smallest minimum value.
   */
  private final val minSmallest: Long = 0L
  /**
   * The largest minimum value.
   */
  private final val minLargest: Long = 0L
  /**
   * The smallest maximum value.
   */
  private final val maxSmallest: Long = 0L
  /**
   * The largest maximum value.
   */
  private final val maxLargest: Long = 0L
}

/*
 * Copyright (c) 2012, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
/*
 * This file is available under and governed by the GNU General Public
 * License version 2 only, as published by the Free Software Foundation.
 * However, the following notice accompanied the original version of this
 * file:
 *
 * Copyright (c) 2011-2012, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/**
 * Localized definitions of the day-of-week, week-of-month and week-of-year fields.
 * <p>
 * A standard week is seven days long, but cultures have different definitions for some
 * other aspects of a week. This class represents the definition of the week, for the
 * purpose of providing {@link TemporalField} instances.
 * <p>
 * WeekFields provides five fields,
 * {@link #dayOfWeek()}, {@link #weekOfMonth()}, {@link #weekOfYear()},
 * {@link #weekOfWeekBasedYear()}, and {@link #weekBasedYear()}
 * that provide access to the values from any {@linkplain Temporal temporal object}.
 * <p>
 * The computations for day-of-week, week-of-month, and week-of-year are based
 * on the  {@linkplain ChronoField#YEAR proleptic-year},
 * {@linkplain ChronoField#MONTH_OF_YEAR month-of-year},
 * {@linkplain ChronoField#DAY_OF_MONTH day-of-month}, and
 * {@linkplain ChronoField#DAY_OF_WEEK ISO day-of-week} which are based on the
 * {@linkplain ChronoField#EPOCH_DAY epoch-day} and the chronology.
 * The values may not be aligned with the {@linkplain ChronoField#YEAR_OF_ERA year-of-Era}
 * depending on the Chronology.
 * <p>A week is defined by:
 * <ul>
 * <li>The first day-of-week.
 * For example, the ISO-8601 standard considers Monday to be the first day-of-week.
 * <li>The minimal number of days in the first week.
 * For example, the ISO-8601 standard counts the first week as needing at least 4 days.
 * </ul><p>
 * Together these two values allow a year or month to be divided into weeks.
 * <p>
 * <h3>Week of Month</h3>
 * One field is used: week-of-month.
 * The calculation ensures that weeks never overlap a month boundary.
 * The month is divided into periods where each period starts on the defined first day-of-week.
 * The earliest period is referred to as week 0 if it has less than the minimal number of days
 * and week 1 if it has at least the minimal number of days.
 * <p>
 * <table cellpadding="0" cellspacing="3" border="0" style="text-align: left; width: 50%;">
 * <caption>Examples of WeekFields</caption>
 * <tr><th>Date</th><td>Day-of-week</td>
 * <td>First day: Monday<br>Minimal days: 4</td><td>First day: Monday<br>Minimal days: 5</td></tr>
 * <tr><th>2008-12-31</th><td>Wednesday</td>
 * <td>Week 5 of December 2008</td><td>Week 5 of December 2008</td></tr>
 * <tr><th>2009-01-01</th><td>Thursday</td>
 * <td>Week 1 of January 2009</td><td>Week 0 of January 2009</td></tr>
 * <tr><th>2009-01-04</th><td>Sunday</td>
 * <td>Week 1 of January 2009</td><td>Week 0 of January 2009</td></tr>
 * <tr><th>2009-01-05</th><td>Monday</td>
 * <td>Week 2 of January 2009</td><td>Week 1 of January 2009</td></tr>
 * </table>
 *
 * <h3>Week of Year</h3>
 * One field is used: week-of-year.
 * The calculation ensures that weeks never overlap a year boundary.
 * The year is divided into periods where each period starts on the defined first day-of-week.
 * The earliest period is referred to as week 0 if it has less than the minimal number of days
 * and week 1 if it has at least the minimal number of days.
 *
 * <h3>Week Based Year</h3>
 * Two fields are used for week-based-year, one for the
 * {@link #weekOfWeekBasedYear() week-of-week-based-year} and one for
 * {@link #weekBasedYear() week-based-year}.  In a week-based-year, each week
 * belongs to only a single year.  Week 1 of a year is the first week that
 * starts on the first day-of-week and has at least the minimum number of days.
 * The first and last weeks of a year may contain days from the
 * previous calendar year or next calendar year respectively.
 *
 * <table cellpadding="0" cellspacing="3" border="0" style="text-align: left; width: 50%;">
 * <caption>Examples of WeekFields for week-based-year</caption>
 * <tr><th>Date</th><td>Day-of-week</td>
 * <td>First day: Monday<br>Minimal days: 4</td><td>First day: Monday<br>Minimal days: 5</td></tr>
 * <tr><th>2008-12-31</th><td>Wednesday</td>
 * <td>Week 1 of 2009</td><td>Week 53 of 2008</td></tr>
 * <tr><th>2009-01-01</th><td>Thursday</td>
 * <td>Week 1 of 2009</td><td>Week 53 of 2008</td></tr>
 * <tr><th>2009-01-04</th><td>Sunday</td>
 * <td>Week 1 of 2009</td><td>Week 53 of 2008</td></tr>
 * <tr><th>2009-01-05</th><td>Monday</td>
 * <td>Week 2 of 2009</td><td>Week 1 of 2009</td></tr>
 * </table>
 *
 * @implSpec
 * This class is immutable and thread-safe.
 *
 * @since 1.8
 */
final object WeekFields {
  /**
   * Obtains an instance of {@code WeekFields} appropriate for a locale.
   * <p>
   * This will look up appropriate values from the provider of localization data.
   *
   * @param locale  the locale to use, not null
   * @return the week-definition, not null
   */
  def of(locale: Locale): WeekFields = {
    Objects.requireNonNull(locale, "locale")
    locale = new Locale(locale.getLanguage, locale.getCountry)
    val calDow: Int = CalendarDataUtility.retrieveFirstDayOfWeek(locale)
    val dow: DayOfWeek = DayOfWeek.SUNDAY.plus(calDow - 1)
    val minDays: Int = CalendarDataUtility.retrieveMinimalDaysInFirstWeek(locale)
    return WeekFields.of(dow, minDays)
  }

  /**
   * Obtains an instance of {@code WeekFields} from the first day-of-week and minimal days.
   * <p>
   * The first day-of-week defines the ISO {@code DayOfWeek} that is day 1 of the week.
   * The minimal number of days in the first week defines how many days must be present
   * in a month or year, starting from the first day-of-week, before the week is counted
   * as the first week. A value of 1 will count the first day of the month or year as part
   * of the first week, whereas a value of 7 will require the whole seven days to be in
   * the new month or year.
   * <p>
   * WeekFields instances are singletons; for each unique combination
   * of {@code firstDayOfWeek} and {@code minimalDaysInFirstWeek} the
   * the same instance will be returned.
   *
   * @param firstDayOfWeek  the first day of the week, not null
   * @param minimalDaysInFirstWeek  the minimal number of days in the first week, from 1 to 7
   * @return the week-definition, not null
   * @throws IllegalArgumentException if the minimal days value is less than one
   *                                  or greater than 7
   */
  def of(firstDayOfWeek: DayOfWeek, minimalDaysInFirstWeek: Int): WeekFields = {
    val key: String = firstDayOfWeek.toString + minimalDaysInFirstWeek
    var rules: WeekFields = CACHE.get(key)
    if (rules == null) {
      rules = new WeekFields(firstDayOfWeek, minimalDaysInFirstWeek)
      CACHE.putIfAbsent(key, rules)
      rules = CACHE.get(key)
    }
    return rules
  }

  /**
   * The cache of rules by firstDayOfWeek plus minimalDays.
   * Initialized first to be available for definition of ISO, etc.
   */
  private final val CACHE: Nothing = new Nothing(4, 0.75f, 2)
  /**
   * The ISO-8601 definition, where a week starts on Monday and the first week
   * has a minimum of 4 days.
   * <p>
   * The ISO-8601 standard defines a calendar system based on weeks.
   * It uses the week-based-year and week-of-week-based-year concepts to split
   * up the passage of days instead of the standard year/month/day.
   * <p>
   * Note that the first week may start in the previous calendar year.
   * Note also that the first few days of a calendar year may be in the
   * week-based-year corresponding to the previous calendar year.
   */
  final val ISO: WeekFields = new WeekFields(DayOfWeek.MONDAY, 4)
  /**
   * The common definition of a week that starts on Sunday and the first week
   * has a minimum of 1 day.
   * <p>
   * Defined as starting on Sunday and with a minimum of 1 day in the month.
   * This week definition is in use in the US and other European countries.
   */
  final val SUNDAY_START: WeekFields = WeekFields.of(DayOfWeek.SUNDAY, 1)
  /**
   * The unit that represents week-based-years for the purpose of addition and subtraction.
   * <p>
   * This allows a number of week-based-years to be added to, or subtracted from, a date.
   * The unit is equal to either 52 or 53 weeks.
   * The estimated duration of a week-based-year is the same as that of a standard ISO
   * year at {@code 365.2425 Days}.
   * <p>
   * The rules for addition add the number of week-based-years to the existing value
   * for the week-based-year field retaining the week-of-week-based-year
   * and day-of-week, unless the week number it too large for the target year.
   * In that case, the week is set to the last week of the year
   * with the same day-of-week.
   * <p>
   * This unit is an immutable and thread-safe singleton.
   */
  final val WEEK_BASED_YEARS: TemporalUnit = IsoFields.WEEK_BASED_YEARS
  /**
   * Serialization version.
   */
  private final val serialVersionUID: Long = -1177360819670808121L

  /**
   * Field type that computes DayOfWeek, WeekOfMonth, and WeekOfYear
   * based on a WeekFields.
   * A separate Field instance is required for each different WeekFields;
   * combination of start of week and minimum number of days.
   * Constructors are provided to create fields for DayOfWeek, WeekOfMonth,
   * and WeekOfYear.
   */
  private[temporal] object ComputedDayOfField {
    /**
     * Returns a field to access the day of week,
     * computed based on a WeekFields.
     * <p>
     * The WeekDefintion of the first day of the week is used with
     * the ISO DAY_OF_WEEK field to compute week boundaries.
     */
    private[temporal] def ofDayOfWeekField(weekDef: WeekFields): WeekFields.ComputedDayOfField = {
      return new WeekFields.ComputedDayOfField("DayOfWeek", weekDef, DAYS, WEEKS, DAY_OF_WEEK_RANGE)
    }

    /**
     * Returns a field to access the week of month,
     * computed based on a WeekFields.
     * @see WeekFields#weekOfMonth()
     */
    private[temporal] def ofWeekOfMonthField(weekDef: WeekFields): WeekFields.ComputedDayOfField = {
      return new WeekFields.ComputedDayOfField("WeekOfMonth", weekDef, WEEKS, MONTHS, WEEK_OF_MONTH_RANGE)
    }

    /**
     * Returns a field to access the week of year,
     * computed based on a WeekFields.
     * @see WeekFields#weekOfYear()
     */
    private[temporal] def ofWeekOfYearField(weekDef: WeekFields): WeekFields.ComputedDayOfField = {
      return new WeekFields.ComputedDayOfField("WeekOfYear", weekDef, WEEKS, YEARS, WEEK_OF_YEAR_RANGE)
    }

    /**
     * Returns a field to access the week of week-based-year,
     * computed based on a WeekFields.
     * @see WeekFields#weekOfWeekBasedYear()
     */
    private[temporal] def ofWeekOfWeekBasedYearField(weekDef: WeekFields): WeekFields.ComputedDayOfField = {
      return new WeekFields.ComputedDayOfField("WeekOfWeekBasedYear", weekDef, WEEKS, IsoFields.WEEK_BASED_YEARS, WEEK_OF_YEAR_RANGE)
    }

    /**
     * Returns a field to access the week of week-based-year,
     * computed based on a WeekFields.
     * @see WeekFields#weekBasedYear()
     */
    private[temporal] def ofWeekBasedYearField(weekDef: WeekFields): WeekFields.ComputedDayOfField = {
      return new WeekFields.ComputedDayOfField("WeekBasedYear", weekDef, IsoFields.WEEK_BASED_YEARS, FOREVER, ChronoField.YEAR.range)
    }

    private final val DAY_OF_WEEK_RANGE: ValueRange = ValueRange.of(1, 7)
    private final val WEEK_OF_MONTH_RANGE: ValueRange = ValueRange.of(0, 1, 4, 6)
    private final val WEEK_OF_YEAR_RANGE: ValueRange = ValueRange.of(0, 1, 52, 54)
  }

  private[temporal] class ComputedDayOfField extends TemporalField {
    /**
     * Return a new week-based-year date of the Chronology, year, week-of-year,
     * and dow of week.
     * @param chrono The chronology of the new date
     * @param yowby the year of the week-based-year
     * @param wowby the week of the week-based-year
     * @param dow the day of the week
     * @return a ChronoLocalDate for the requested year, week of year, and day of week
     */
    private def ofWeekBasedYear(chrono: Chronology, yowby: Int, wowby: Int, dow: Int): ChronoLocalDate = {
      val date: ChronoLocalDate = chrono.date(yowby, 1, 1)
      val ldow: Int = localizedDayOfWeek(date)
      val offset: Int = startOfWeekOffset(1, ldow)
      val yearLen: Int = date.lengthOfYear
      val newYearWeek: Int = computeWeek(offset, yearLen + weekDef.getMinimalDaysInFirstWeek)
      wowby = Math.min(wowby, newYearWeek - 1)
      val days: Int = -offset + (dow - 1) + (wowby - 1) * 7
      return date.plus(days, DAYS)
    }

    private def this(name: String, weekDef: WeekFields, baseUnit: TemporalUnit, rangeUnit: TemporalUnit, range: ValueRange) {
      this()
      this.name = name
      this.weekDef = weekDef
      this.baseUnit = baseUnit
      this.rangeUnit = rangeUnit
      this.range = range
    }

    def getFrom(temporal: TemporalAccessor): Long = {
      if (rangeUnit eq WEEKS) {
        return localizedDayOfWeek(temporal)
      }
      else if (rangeUnit eq MONTHS) {
        return localizedWeekOfMonth(temporal)
      }
      else if (rangeUnit eq YEARS) {
        return localizedWeekOfYear(temporal)
      }
      else if (rangeUnit eq WEEK_BASED_YEARS) {
        return localizedWeekOfWeekBasedYear(temporal)
      }
      else if (rangeUnit eq FOREVER) {
        return localizedWeekBasedYear(temporal)
      }
      else {
        throw new IllegalStateException("unreachable, rangeUnit: " + rangeUnit + ", this: " + this)
      }
    }

    private def localizedDayOfWeek(temporal: TemporalAccessor): Int = {
      val sow: Int = weekDef.getFirstDayOfWeek.getValue
      val isoDow: Int = temporal.get(DAY_OF_WEEK)
      return Math.floorMod(isoDow - sow, 7) + 1
    }

    private def localizedDayOfWeek(isoDow: Int): Int = {
      val sow: Int = weekDef.getFirstDayOfWeek.getValue
      return Math.floorMod(isoDow - sow, 7) + 1
    }

    private def localizedWeekOfMonth(temporal: TemporalAccessor): Long = {
      val dow: Int = localizedDayOfWeek(temporal)
      val dom: Int = temporal.get(DAY_OF_MONTH)
      val offset: Int = startOfWeekOffset(dom, dow)
      return computeWeek(offset, dom)
    }

    private def localizedWeekOfYear(temporal: TemporalAccessor): Long = {
      val dow: Int = localizedDayOfWeek(temporal)
      val doy: Int = temporal.get(DAY_OF_YEAR)
      val offset: Int = startOfWeekOffset(doy, dow)
      return computeWeek(offset, doy)
    }

    /**
     * Returns the year of week-based-year for the temporal.
     * The year can be the previous year, the current year, or the next year.
     * @param temporal a date of any chronology, not null
     * @return the year of week-based-year for the date
     */
    private def localizedWeekBasedYear(temporal: TemporalAccessor): Int = {
      val dow: Int = localizedDayOfWeek(temporal)
      val year: Int = temporal.get(YEAR)
      val doy: Int = temporal.get(DAY_OF_YEAR)
      val offset: Int = startOfWeekOffset(doy, dow)
      val week: Int = computeWeek(offset, doy)
      if (week == 0) {
        return year - 1
      }
      else {
        val dayRange: ValueRange = temporal.range(DAY_OF_YEAR)
        val yearLen: Int = dayRange.getMaximum.asInstanceOf[Int]
        val newYearWeek: Int = computeWeek(offset, yearLen + weekDef.getMinimalDaysInFirstWeek)
        if (week >= newYearWeek) {
          return year + 1
        }
      }
      return year
    }

    /**
     * Returns the week of week-based-year for the temporal.
     * The week can be part of the previous year, the current year,
     * or the next year depending on the week start and minimum number
     * of days.
     * @param temporal  a date of any chronology
     * @return the week of the year
     * @see #localizedWeekBasedYear(java.time.temporal.TemporalAccessor)
     */
    private def localizedWeekOfWeekBasedYear(temporal: TemporalAccessor): Int = {
      val dow: Int = localizedDayOfWeek(temporal)
      val doy: Int = temporal.get(DAY_OF_YEAR)
      val offset: Int = startOfWeekOffset(doy, dow)
      var week: Int = computeWeek(offset, doy)
      if (week == 0) {
        var date: ChronoLocalDate = Chronology.from(temporal).date(temporal)
        date = date.minus(doy, DAYS)
        return localizedWeekOfWeekBasedYear(date)
      }
      else if (week > 50) {
        val dayRange: ValueRange = temporal.range(DAY_OF_YEAR)
        val yearLen: Int = dayRange.getMaximum.asInstanceOf[Int]
        val newYearWeek: Int = computeWeek(offset, yearLen + weekDef.getMinimalDaysInFirstWeek)
        if (week >= newYearWeek) {
          week = week - newYearWeek + 1
        }
      }
      return week
    }

    /**
     * Returns an offset to align week start with a day of month or day of year.
     *
     * @param day  the day; 1 through infinity
     * @param dow  the day of the week of that day; 1 through 7
     * @return  an offset in days to align a day with the start of the first 'full' week
     */
    private def startOfWeekOffset(day: Int, dow: Int): Int = {
      val weekStart: Int = Math.floorMod(day - dow, 7)
      var offset: Int = -weekStart
      if (weekStart + 1 > weekDef.getMinimalDaysInFirstWeek) {
        offset = 7 - weekStart
      }
      return offset
    }

    /**
     * Returns the week number computed from the reference day and reference dayOfWeek.
     *
     * @param offset the offset to align a date with the start of week
     *               from { @link #startOfWeekOffset}.
     * @param day  the day for which to compute the week number
     * @return the week number where zero is used for a partial week and 1 for the first full week
     */
    private def computeWeek(offset: Int, day: Int): Int = {
      return ((7 + offset + (day - 1)) / 7)
    }

    @SuppressWarnings(Array("unchecked")) def adjustInto(temporal: R, newValue: Long): R = {
      val newVal: Int = range.checkValidIntValue(newValue, this)
      val currentVal: Int = temporal.get(this)
      if (newVal == currentVal) {
        return temporal
      }
      if (rangeUnit eq FOREVER) {
        val idow: Int = temporal.get(weekDef.dayOfWeek)
        val wowby: Int = temporal.get(weekDef.weekOfWeekBasedYear)
        return ofWeekBasedYear(Chronology.from(temporal), newValue.asInstanceOf[Int], wowby, idow).asInstanceOf[R]
      }
      else {
        return temporal.plus(newVal - currentVal, baseUnit).asInstanceOf[R]
      }
    }

    override def resolve(fieldValues: Nothing, chronology: Chronology, zone: Nothing, resolverStyle: Nothing): ChronoLocalDate = {
      val value: Long = fieldValues.get(this)
      val newValue: Int = Math.toIntExact(value)
      if (rangeUnit eq WEEKS) {
        val checkedValue: Int = range.checkValidIntValue(value, this)
        val startDow: Int = weekDef.getFirstDayOfWeek.getValue
        val isoDow: Long = Math.floorMod((startDow - 1) + (checkedValue - 1), 7) + 1
        fieldValues.remove(this)
        fieldValues.put(DAY_OF_WEEK, isoDow)
        return null
      }
      if (fieldValues.containsKey(DAY_OF_WEEK) eq false) {
        return null
      }
      val isoDow: Int = DAY_OF_WEEK.checkValidIntValue(fieldValues.get(DAY_OF_WEEK))
      val dow: Int = localizedDayOfWeek(isoDow)
      if (fieldValues.containsKey(YEAR)) {
        val year: Int = YEAR.checkValidIntValue(fieldValues.get(YEAR))
        if (rangeUnit eq MONTHS && fieldValues.containsKey(MONTH_OF_YEAR)) {
          val month: Long = fieldValues.get(MONTH_OF_YEAR)
          return resolveWoM(fieldValues, chronology, year, month, newValue, dow, resolverStyle)
        }
        if (rangeUnit eq YEARS) {
          return resolveWoY(fieldValues, chronology, year, newValue, dow, resolverStyle)
        }
      }
      else if ((rangeUnit eq WEEK_BASED_YEARS || rangeUnit eq FOREVER) && fieldValues.containsKey(weekDef.weekBasedYear) && fieldValues.containsKey(weekDef.weekOfWeekBasedYear)) {
        return resolveWBY(fieldValues, chronology, dow, resolverStyle)
      }
      return null
    }

    private def resolveWoM(fieldValues: Nothing, chrono: Chronology, year: Int, month: Long, wom: Long, localDow: Int, resolverStyle: Nothing): ChronoLocalDate = {
      var date: ChronoLocalDate = null
      if (resolverStyle eq ResolverStyle.LENIENT) {
        date = chrono.date(year, 1, 1).plus(Math.subtractExact(month, 1), MONTHS)
        val weeks: Long = Math.subtractExact(wom, localizedWeekOfMonth(date))
        val days: Int = localDow - localizedDayOfWeek(date)
        date = date.plus(Math.addExact(Math.multiplyExact(weeks, 7), days), DAYS)
      }
      else {
        val monthValid: Int = MONTH_OF_YEAR.checkValidIntValue(month)
        date = chrono.date(year, monthValid, 1)
        val womInt: Int = range.checkValidIntValue(wom, this)
        val weeks: Int = (womInt - localizedWeekOfMonth(date)).asInstanceOf[Int]
        val days: Int = localDow - localizedDayOfWeek(date)
        date = date.plus(weeks * 7 + days, DAYS)
        if (resolverStyle eq ResolverStyle.STRICT && date.getLong(MONTH_OF_YEAR) != month) {
          throw new Nothing("Strict mode rejected resolved date as it is in a different month")
        }
      }
      fieldValues.remove(this)
      fieldValues.remove(YEAR)
      fieldValues.remove(MONTH_OF_YEAR)
      fieldValues.remove(DAY_OF_WEEK)
      return date
    }

    private def resolveWoY(fieldValues: Nothing, chrono: Chronology, year: Int, woy: Long, localDow: Int, resolverStyle: Nothing): ChronoLocalDate = {
      var date: ChronoLocalDate = chrono.date(year, 1, 1)
      if (resolverStyle eq ResolverStyle.LENIENT) {
        val weeks: Long = Math.subtractExact(woy, localizedWeekOfYear(date))
        val days: Int = localDow - localizedDayOfWeek(date)
        date = date.plus(Math.addExact(Math.multiplyExact(weeks, 7), days), DAYS)
      }
      else {
        val womInt: Int = range.checkValidIntValue(woy, this)
        val weeks: Int = (womInt - localizedWeekOfYear(date)).asInstanceOf[Int]
        val days: Int = localDow - localizedDayOfWeek(date)
        date = date.plus(weeks * 7 + days, DAYS)
        if (resolverStyle eq ResolverStyle.STRICT && date.getLong(YEAR) != year) {
          throw new Nothing("Strict mode rejected resolved date as it is in a different year")
        }
      }
      fieldValues.remove(this)
      fieldValues.remove(YEAR)
      fieldValues.remove(DAY_OF_WEEK)
      return date
    }

    private def resolveWBY(fieldValues: Nothing, chrono: Chronology, localDow: Int, resolverStyle: Nothing): ChronoLocalDate = {
      val yowby: Int = weekDef.weekBasedYear.range.checkValidIntValue(fieldValues.get(weekDef.weekBasedYear), weekDef.weekBasedYear)
      var date: ChronoLocalDate = null
      if (resolverStyle eq ResolverStyle.LENIENT) {
        date = ofWeekBasedYear(chrono, yowby, 1, localDow)
        val wowby: Long = fieldValues.get(weekDef.weekOfWeekBasedYear)
        val weeks: Long = Math.subtractExact(wowby, 1)
        date = date.plus(weeks, WEEKS)
      }
      else {
        val wowby: Int = weekDef.weekOfWeekBasedYear.range.checkValidIntValue(fieldValues.get(weekDef.weekOfWeekBasedYear), weekDef.weekOfWeekBasedYear)
        date = ofWeekBasedYear(chrono, yowby, wowby, localDow)
        if (resolverStyle eq ResolverStyle.STRICT && localizedWeekBasedYear(date) != yowby) {
          throw new Nothing("Strict mode rejected resolved date as it is in a different week-based-year")
        }
      }
      fieldValues.remove(this)
      fieldValues.remove(weekDef.weekBasedYear)
      fieldValues.remove(weekDef.weekOfWeekBasedYear)
      fieldValues.remove(DAY_OF_WEEK)
      return date
    }

    override def getDisplayName(locale: Locale): String = {
      Objects.requireNonNull(locale, "locale")
      if (rangeUnit eq YEARS) {
        val lr: LocaleResources = LocaleProviderAdapter.getResourceBundleBased.getLocaleResources(locale)
        val rb: ResourceBundle = lr.getJavaTimeFormatData
        return if (rb.containsKey("field.week")) rb.getString("field.week") else name
      }
      return name
    }

    def getBaseUnit: TemporalUnit = {
      return baseUnit
    }

    def getRangeUnit: TemporalUnit = {
      return rangeUnit
    }

    def isDateBased: Boolean = {
      return true
    }

    def isTimeBased: Boolean = {
      return false
    }

    def range: ValueRange = {
      return range
    }

    def isSupportedBy(temporal: TemporalAccessor): Boolean = {
      if (temporal.isSupported(DAY_OF_WEEK)) {
        if (rangeUnit eq WEEKS) {
          return true
        }
        else if (rangeUnit eq MONTHS) {
          return temporal.isSupported(DAY_OF_MONTH)
        }
        else if (rangeUnit eq YEARS) {
          return temporal.isSupported(DAY_OF_YEAR)
        }
        else if (rangeUnit eq WEEK_BASED_YEARS) {
          return temporal.isSupported(DAY_OF_YEAR)
        }
        else if (rangeUnit eq FOREVER) {
          return temporal.isSupported(YEAR)
        }
      }
      return false
    }

    def rangeRefinedBy(temporal: TemporalAccessor): ValueRange = {
      if (rangeUnit eq ChronoUnit.WEEKS) {
        return range
      }
      else if (rangeUnit eq MONTHS) {
        return rangeByWeek(temporal, DAY_OF_MONTH)
      }
      else if (rangeUnit eq YEARS) {
        return rangeByWeek(temporal, DAY_OF_YEAR)
      }
      else if (rangeUnit eq WEEK_BASED_YEARS) {
        return rangeWeekOfWeekBasedYear(temporal)
      }
      else if (rangeUnit eq FOREVER) {
        return YEAR.range
      }
      else {
        throw new IllegalStateException("unreachable, rangeUnit: " + rangeUnit + ", this: " + this)
      }
    }

    /**
     * Map the field range to a week range
     * @param temporal the temporal
     * @param field the field to get the range of
     * @return the ValueRange with the range adjusted to weeks.
     */
    private def rangeByWeek(temporal: TemporalAccessor, field: TemporalField): ValueRange = {
      val dow: Int = localizedDayOfWeek(temporal)
      val offset: Int = startOfWeekOffset(temporal.get(field), dow)
      val fieldRange: ValueRange = temporal.range(field)
      return ValueRange.of(computeWeek(offset, fieldRange.getMinimum.asInstanceOf[Int]), computeWeek(offset, fieldRange.getMaximum.asInstanceOf[Int]))
    }

    /**
     * Map the field range to a week range of a week year.
     * @param temporal  the temporal
     * @return the ValueRange with the range adjusted to weeks.
     */
    private def rangeWeekOfWeekBasedYear(temporal: TemporalAccessor): ValueRange = {
      if (!temporal.isSupported(DAY_OF_YEAR)) {
        return WEEK_OF_YEAR_RANGE
      }
      val dow: Int = localizedDayOfWeek(temporal)
      val doy: Int = temporal.get(DAY_OF_YEAR)
      val offset: Int = startOfWeekOffset(doy, dow)
      val week: Int = computeWeek(offset, doy)
      if (week == 0) {
        var date: ChronoLocalDate = Chronology.from(temporal).date(temporal)
        date = date.minus(doy + 7, DAYS)
        return rangeWeekOfWeekBasedYear(date)
      }
      val dayRange: ValueRange = temporal.range(DAY_OF_YEAR)
      val yearLen: Int = dayRange.getMaximum.asInstanceOf[Int]
      val newYearWeek: Int = computeWeek(offset, yearLen + weekDef.getMinimalDaysInFirstWeek)
      if (week >= newYearWeek) {
        var date: ChronoLocalDate = Chronology.from(temporal).date(temporal)
        date = date.plus(yearLen - doy + 1 + 7, ChronoUnit.DAYS)
        return rangeWeekOfWeekBasedYear(date)
      }
      return ValueRange.of(1, newYearWeek - 1)
    }

    override def toString: String = {
      return name + "[" + weekDef.toString + "]"
    }

    private final val name: String = null
    private final val weekDef: WeekFields = null
    private final val baseUnit: TemporalUnit = null
    private final val rangeUnit: TemporalUnit = null
    private final val range: ValueRange = null
  }

}

final class WeekFields extends Serializable {
  /**
   * Creates an instance of the definition.
   *
   * @param firstDayOfWeek  the first day of the week, not null
   * @param minimalDaysInFirstWeek  the minimal number of days in the first week, from 1 to 7
   * @throws IllegalArgumentException if the minimal days value is invalid
   */
  private def this(firstDayOfWeek: DayOfWeek, minimalDaysInFirstWeek: Int) {
    this()
    Objects.requireNonNull(firstDayOfWeek, "firstDayOfWeek")
    if (minimalDaysInFirstWeek < 1 || minimalDaysInFirstWeek > 7) {
      throw new IllegalArgumentException("Minimal number of days is invalid")
    }
    this.firstDayOfWeek = firstDayOfWeek
    this.minimalDays = minimalDaysInFirstWeek
  }

  /**
   * Return the singleton WeekFields associated with the
   * {@code firstDayOfWeek} and {@code minimalDays}.
   * @return the singleton WeekFields for the firstDayOfWeek and minimalDays.
   * @throws InvalidObjectException if the serialized object has invalid
   *                                values for firstDayOfWeek or minimalDays.
   */
  private def readResolve: AnyRef = {
    try {
      return WeekFields.of(firstDayOfWeek, minimalDays)
    }
    catch {
      case iae: IllegalArgumentException => {
        throw new Nothing("Invalid serialized WeekFields: " + iae.getMessage)
      }
    }
  }

  /**
   * Gets the first day-of-week.
   * <p>
   * The first day-of-week varies by culture.
   * For example, the US uses Sunday, while France and the ISO-8601 standard use Monday.
   * This method returns the first day using the standard {@code DayOfWeek} enum.
   *
   * @return the first day-of-week, not null
   */
  def getFirstDayOfWeek: DayOfWeek = {
    return firstDayOfWeek
  }

  /**
   * Gets the minimal number of days in the first week.
   * <p>
   * The number of days considered to define the first week of a month or year
   * varies by culture.
   * For example, the ISO-8601 requires 4 days (more than half a week) to
   * be present before counting the first week.
   *
   * @return the minimal number of days in the first week of a month or year, from 1 to 7
   */
  def getMinimalDaysInFirstWeek: Int = {
    return minimalDays
  }

  /**
   * Returns a field to access the day of week based on this {@code WeekFields}.
   * <p>
   * This is similar to {@link ChronoField#DAY_OF_WEEK} but uses values for
   * the day-of-week based on this {@code WeekFields}.
   * The days are numbered from 1 to 7 where the
   * {@link #getFirstDayOfWeek() first day-of-week} is assigned the value 1.
   * <p>
   * For example, if the first day-of-week is Sunday, then that will have the
   * value 1, with other days ranging from Monday as 2 to Saturday as 7.
   * <p>
   * In the resolving phase of parsing, a localized day-of-week will be converted
   * to a standardized {@code ChronoField} day-of-week.
   * The day-of-week must be in the valid range 1 to 7.
   * Other fields in this class build dates using the standardized day-of-week.
   *
   * @return a field providing access to the day-of-week with localized numbering, not null
   */
  def dayOfWeek: TemporalField = {
    return dayOfWeek
  }

  /**
   * Returns a field to access the week of month based on this {@code WeekFields}.
   * <p>
   * This represents the concept of the count of weeks within the month where weeks
   * start on a fixed day-of-week, such as Monday.
   * This field is typically used with {@link WeekFields#dayOfWeek()}.
   * <p>
   * Week one (1) is the week starting on the {@link WeekFields#getFirstDayOfWeek}
   * where there are at least {@link WeekFields#getMinimalDaysInFirstWeek()} days in the month.
   * Thus, week one may start up to {@code minDays} days before the start of the month.
   * If the first week starts after the start of the month then the period before is week zero (0).
   * <p>
   * For example:<br>
   * - if the 1st day of the month is a Monday, week one starts on the 1st and there is no week zero<br>
   * - if the 2nd day of the month is a Monday, week one starts on the 2nd and the 1st is in week zero<br>
   * - if the 4th day of the month is a Monday, week one starts on the 4th and the 1st to 3rd is in week zero<br>
   * - if the 5th day of the month is a Monday, week two starts on the 5th and the 1st to 4th is in week one<br>
   * <p>
   * This field can be used with any calendar system.
   * <p>
   * In the resolving phase of parsing, a date can be created from a year,
   * week-of-month, month-of-year and day-of-week.
   * <p>
   * In {@linkplain ResolverStyle#STRICT strict mode}, all four fields are
   * validated against their range of valid values. The week-of-month field
   * is validated to ensure that the resulting month is the month requested.
   * <p>
   * In {@linkplain ResolverStyle#SMART smart mode}, all four fields are
   * validated against their range of valid values. The week-of-month field
   * is validated from 0 to 6, meaning that the resulting date can be in a
   * different month to that specified.
   * <p>
   * In {@linkplain ResolverStyle#LENIENT lenient mode}, the year and day-of-week
   * are validated against the range of valid values. The resulting date is calculated
   * equivalent to the following four stage approach.
   * First, create a date on the first day of the first week of January in the requested year.
   * Then take the month-of-year, subtract one, and add the amount in months to the date.
   * Then take the week-of-month, subtract one, and add the amount in weeks to the date.
   * Finally, adjust to the correct day-of-week within the localized week.
   *
   * @return a field providing access to the week-of-month, not null
   */
  def weekOfMonth: TemporalField = {
    return weekOfMonth
  }

  /**
   * Returns a field to access the week of year based on this {@code WeekFields}.
   * <p>
   * This represents the concept of the count of weeks within the year where weeks
   * start on a fixed day-of-week, such as Monday.
   * This field is typically used with {@link WeekFields#dayOfWeek()}.
   * <p>
   * Week one(1) is the week starting on the {@link WeekFields#getFirstDayOfWeek}
   * where there are at least {@link WeekFields#getMinimalDaysInFirstWeek()} days in the year.
   * Thus, week one may start up to {@code minDays} days before the start of the year.
   * If the first week starts after the start of the year then the period before is week zero (0).
   * <p>
   * For example:<br>
   * - if the 1st day of the year is a Monday, week one starts on the 1st and there is no week zero<br>
   * - if the 2nd day of the year is a Monday, week one starts on the 2nd and the 1st is in week zero<br>
   * - if the 4th day of the year is a Monday, week one starts on the 4th and the 1st to 3rd is in week zero<br>
   * - if the 5th day of the year is a Monday, week two starts on the 5th and the 1st to 4th is in week one<br>
   * <p>
   * This field can be used with any calendar system.
   * <p>
   * In the resolving phase of parsing, a date can be created from a year,
   * week-of-year and day-of-week.
   * <p>
   * In {@linkplain ResolverStyle#STRICT strict mode}, all three fields are
   * validated against their range of valid values. The week-of-year field
   * is validated to ensure that the resulting year is the year requested.
   * <p>
   * In {@linkplain ResolverStyle#SMART smart mode}, all three fields are
   * validated against their range of valid values. The week-of-year field
   * is validated from 0 to 54, meaning that the resulting date can be in a
   * different year to that specified.
   * <p>
   * In {@linkplain ResolverStyle#LENIENT lenient mode}, the year and day-of-week
   * are validated against the range of valid values. The resulting date is calculated
   * equivalent to the following three stage approach.
   * First, create a date on the first day of the first week in the requested year.
   * Then take the week-of-year, subtract one, and add the amount in weeks to the date.
   * Finally, adjust to the correct day-of-week within the localized week.
   *
   * @return a field providing access to the week-of-year, not null
   */
  def weekOfYear: TemporalField = {
    return weekOfYear
  }

  /**
   * Returns a field to access the week of a week-based-year based on this {@code WeekFields}.
   * <p>
   * This represents the concept of the count of weeks within the year where weeks
   * start on a fixed day-of-week, such as Monday and each week belongs to exactly one year.
   * This field is typically used with {@link WeekFields#dayOfWeek()} and
   * {@link WeekFields#weekBasedYear()}.
   * <p>
   * Week one(1) is the week starting on the {@link WeekFields#getFirstDayOfWeek}
   * where there are at least {@link WeekFields#getMinimalDaysInFirstWeek()} days in the year.
   * If the first week starts after the start of the year then the period before
   * is in the last week of the previous year.
   * <p>
   * For example:<br>
   * - if the 1st day of the year is a Monday, week one starts on the 1st<br>
   * - if the 2nd day of the year is a Monday, week one starts on the 2nd and
   * the 1st is in the last week of the previous year<br>
   * - if the 4th day of the year is a Monday, week one starts on the 4th and
   * the 1st to 3rd is in the last week of the previous year<br>
   * - if the 5th day of the year is a Monday, week two starts on the 5th and
   * the 1st to 4th is in week one<br>
   * <p>
   * This field can be used with any calendar system.
   * <p>
   * In the resolving phase of parsing, a date can be created from a week-based-year,
   * week-of-year and day-of-week.
   * <p>
   * In {@linkplain ResolverStyle#STRICT strict mode}, all three fields are
   * validated against their range of valid values. The week-of-year field
   * is validated to ensure that the resulting week-based-year is the
   * week-based-year requested.
   * <p>
   * In {@linkplain ResolverStyle#SMART smart mode}, all three fields are
   * validated against their range of valid values. The week-of-week-based-year field
   * is validated from 1 to 53, meaning that the resulting date can be in the
   * following week-based-year to that specified.
   * <p>
   * In {@linkplain ResolverStyle#LENIENT lenient mode}, the year and day-of-week
   * are validated against the range of valid values. The resulting date is calculated
   * equivalent to the following three stage approach.
   * First, create a date on the first day of the first week in the requested week-based-year.
   * Then take the week-of-week-based-year, subtract one, and add the amount in weeks to the date.
   * Finally, adjust to the correct day-of-week within the localized week.
   *
   * @return a field providing access to the week-of-week-based-year, not null
   */
  def weekOfWeekBasedYear: TemporalField = {
    return weekOfWeekBasedYear
  }

  /**
   * Returns a field to access the year of a week-based-year based on this {@code WeekFields}.
   * <p>
   * This represents the concept of the year where weeks start on a fixed day-of-week,
   * such as Monday and each week belongs to exactly one year.
   * This field is typically used with {@link WeekFields#dayOfWeek()} and
   * {@link WeekFields#weekOfWeekBasedYear()}.
   * <p>
   * Week one(1) is the week starting on the {@link WeekFields#getFirstDayOfWeek}
   * where there are at least {@link WeekFields#getMinimalDaysInFirstWeek()} days in the year.
   * Thus, week one may start before the start of the year.
   * If the first week starts after the start of the year then the period before
   * is in the last week of the previous year.
   * <p>
   * This field can be used with any calendar system.
   * <p>
   * In the resolving phase of parsing, a date can be created from a week-based-year,
   * week-of-year and day-of-week.
   * <p>
   * In {@linkplain ResolverStyle#STRICT strict mode}, all three fields are
   * validated against their range of valid values. The week-of-year field
   * is validated to ensure that the resulting week-based-year is the
   * week-based-year requested.
   * <p>
   * In {@linkplain ResolverStyle#SMART smart mode}, all three fields are
   * validated against their range of valid values. The week-of-week-based-year field
   * is validated from 1 to 53, meaning that the resulting date can be in the
   * following week-based-year to that specified.
   * <p>
   * In {@linkplain ResolverStyle#LENIENT lenient mode}, the year and day-of-week
   * are validated against the range of valid values. The resulting date is calculated
   * equivalent to the following three stage approach.
   * First, create a date on the first day of the first week in the requested week-based-year.
   * Then take the week-of-week-based-year, subtract one, and add the amount in weeks to the date.
   * Finally, adjust to the correct day-of-week within the localized week.
   *
   * @return a field providing access to the week-based-year, not null
   */
  def weekBasedYear: TemporalField = {
    return weekBasedYear
  }

  /**
   * Checks if this {@code WeekFields} is equal to the specified object.
   * <p>
   * The comparison is based on the entire state of the rules, which is
   * the first day-of-week and minimal days.
   *
   * @param object  the other rules to compare to, null returns false
   * @return true if this is equal to the specified rules
   */
  override def equals(`object`: AnyRef): Boolean = {
    if (this eq `object`) {
      return true
    }
    if (`object`.isInstanceOf[WeekFields]) {
      return hashCode == `object`.hashCode
    }
    return false
  }

  /**
   * A hash code for this {@code WeekFields}.
   *
   * @return a suitable hash code
   */
  override def hashCode: Int = {
    return firstDayOfWeek.ordinal * 7 + minimalDays
  }

  /**
   * A string representation of this {@code WeekFields} instance.
   *
   * @return the string representation, not null
   */
  override def toString: String = {
    return "WeekFields[" + firstDayOfWeek + ',' + minimalDays + ']'
  }

  /**
   * The first day-of-week.
   */
  private final val firstDayOfWeek: DayOfWeek = null
  /**
   * The minimal number of days in the first week.
   */
  private final val minimalDays: Int = 0
  /**
   * The field used to access the computed DayOfWeek.
   */
  @transient
  private final val dayOfWeek: TemporalField = ComputedDayOfField.ofDayOfWeekField(this)
  /**
   * The field used to access the computed WeekOfMonth.
   */
  @transient
  private final val weekOfMonth: TemporalField = ComputedDayOfField.ofWeekOfMonthField(this)
  /**
   * The field used to access the computed WeekOfYear.
   */
  @transient
  private final val weekOfYear: TemporalField = ComputedDayOfField.ofWeekOfYearField(this)
  /**
   * The field that represents the week-of-week-based-year.
   * <p>
   * This field allows the week of the week-based-year value to be queried and set.
   * <p>
   * This unit is an immutable and thread-safe singleton.
   */
  @transient
  private final val weekOfWeekBasedYear: TemporalField = ComputedDayOfField.ofWeekOfWeekBasedYearField(this)
  /**
   * The field that represents the week-based-year.
   * <p>
   * This field allows the week-based-year value to be queried and set.
   * <p>
   * This unit is an immutable and thread-safe singleton.
   */
  @transient
  private final val weekBasedYear: TemporalField = ComputedDayOfField.ofWeekBasedYearField(this)
}

/*
 * Copyright (c) 2012, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
/*
 * This file is available under and governed by the GNU General Public
 * License version 2 only, as published by the Free Software Foundation.
 * However, the following notice accompanied the original version of this
 * file:
 *
 * Copyright (c) 2012, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/**
 * <p>
 * Access to date and time using fields and units, and date time adjusters.
 * </p>
 * <p>
 * This package expands on the base package to provide additional functionality for
 * more powerful use cases. Support is included for:
 * </p>
 * <ul>
 * <li>Units of date-time, such as years, months, days and hours</li>
 * <li>Fields of date-time, such as month-of-year, day-of-week or hour-of-day</li>
 * <li>Date-time adjustment functions</li>
 * <li>Different definitions of weeks</li>
 * </ul>
 *
 * <h3>Fields and Units</h3>
 * <p>
 * Dates and times are expressed in terms of fields and units.
 * A unit is used to measure an amount of time, such as years, days or minutes.
 * All units implement {@link java.time.temporal.TemporalUnit}.
 * The set of well known units is defined in {@link java.time.temporal.ChronoUnit}, such as {@code DAYS}.
 * The unit interface is designed to allow applications defined units.
 * </p>
 * <p>
 * A field is used to express part of a larger date-time, such as year, month-of-year or second-of-minute.
 * All fields implement {@link java.time.temporal.TemporalField}.
 * The set of well known fields are defined in {@link java.time.temporal.ChronoField}, such as {@code HOUR_OF_DAY}.
 * Additional fields are defined by {@link java.time.temporal.JulianFields}, {@link java.time.temporal.WeekFields}
 * and {@link java.time.temporal.IsoFields}.
 * The field interface is designed to allow applications defined fields.
 * </p>
 * <p>
 * This package provides tools that allow the units and fields of date and time to be accessed
 * in a general way most suited for frameworks.
 * {@link java.time.temporal.Temporal} provides the abstraction for date time types that support fields.
 * Its methods support getting the value of a field, creating a new date time with the value of
 * a field modified, and querying for additional information, typically used to extract the offset or time-zone.
 * </p>
 * <p>
 * One use of fields in application code is to retrieve fields for which there is no convenience method.
 * For example, getting the day-of-month is common enough that there is a method on {@code Date}
 * called {@code getDayOfMonth()}. However for more unusual fields it is necessary to use the field.
 * For example, {@code date.get(ChronoField.ALIGNED_WEEK_OF_MONTH)}.
 * The fields also provide access to the range of valid values.
 * </p>
 *
 * <h3>Adjustment and Query</h3>
 * <p>
 * A key part of the date-time problem space is adjusting a date to a new, related value,
 * such as the "last day of the month", or "next Wednesday".
 * These are modeled as functions that adjust a base date-time.
 * The functions implement {@link java.time.temporal.TemporalAdjuster} and operate on {@code Temporal}.
 * A set of common functions are provided in {@code TemporalAdjuster}.
 * For example, to find the first occurrence of a day-of-week after a given date, use
 * {@link java.time.temporal.TemporalAdjuster#next(DayOfWeek)}, such as
 * {@code date.with(next(MONDAY))}.
 * Applications can also define adjusters by implementing {@code TemporalAdjuster}.
 * </p>
 * <p>
 * The {@link java.time.temporal.TemporalAmount} interface models amounts of relative time.
 * </p>
 * <p>
 * In addition to adjusting a date-time, an interface is provided to enable querying -
 * {@link java.time.temporal.TemporalQuery}.
 * The most common implementations of the query interface are method references.
 * The {@code from(TemporalAccessor)} methods on major classes can all be used, such as
 * {@code Date::from} or {@code Month::from}.
 * Further implementations are provided in {@code TemporalQuery} as static methods.
 * Applications can also define queries by implementing {@code TemporalQuery}.
 * </p>
 *
 * <h3>Weeks</h3>
 * <p>
 * Different locales have different definitions of the week.
 * For example, in Europe the week typically starts on a Monday, while in the US it starts on a Sunday.
 * The {@link java.time.temporal.WeekFields} class models this distinction.
 * </p>
 * <p>
 * The ISO calendar system defines an additional week-based division of years.
 * This defines a year based on whole Monday to Monday weeks.
 * This is modeled in {@link java.time.temporal.IsoFields}.
 * </p>
 *
 * <h3>Package specification</h3>
 * <p>
 * Unless otherwise noted, passing a null argument to a constructor or method in any class or interface
 * in this package will cause a {@link java.lang.NullPointerException NullPointerException} to be thrown.
 * The Javadoc "@param" definition is used to summarise the null-behavior.
 * The "@throws {@link java.lang.NullPointerException}" is not explicitly documented in each method.
 * </p>
 * <p>
 * All calculations should check for numeric overflow and throw either an {@link java.lang.ArithmeticException}
 * or a {@link java.time.DateTimeException}.
 * </p>
 * @since JDK1.8
 */




