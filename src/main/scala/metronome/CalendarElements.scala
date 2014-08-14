package metronome

import metronome.temporal.{TemporalAdjuster, TemporalAccessor}

/**
 * A day-of-week, such as 'Tuesday'.
 * <p>
 * {@code DayOfWeek} is an enum representing the 7 days of the week -
 * Monday, Tuesday, Wednesday, Thursday, Friday, Saturday and Sunday.
 * <p>
 * In addition to the textual enum name, each day-of-week has an {@code int} value.
 * The {@code int} value follows the ISO-8601 standard, from 1 (Monday) to 7 (Sunday).
 * It is recommended that applications use the enum rather than the {@code int} value
 * to ensure code clarity.
 * <p>
 * This enum provides access to the localized textual form of the day-of-week.
 * Some locales also assign different numeric values to the days, declaring
 * Sunday to have the value 1, however this class provides no support for this.
 * See {@link WeekFields} for localized week-numbering.
 * <p>
 * <b>Do not use {@code ordinal()} to obtain the numeric representation of {@code DayOfWeek}.
 * Use {@code getValue()} instead.</b>
 * <p>
 * This enum represents a common concept that is found in many calendar systems.
 * As such, this enum may be used by any calendar system that has the day-of-week
 * concept defined exactly equivalent to the ISO calendar system.
 *
 * @implSpec
 * This is an immutable and thread-safe enum.
 *
 * @since 1.8
 */
object DayOfWeek {
  /**
   * Obtains an instance of {@code DayOfWeek} from an {@code int} value.
   * <p>
   * {@code DayOfWeek} is an enum representing the 7 days of the week.
   * This factory allows the enum to be obtained from the {@code int} value.
   * The {@code int} value follows the ISO-8601 standard, from 1 (Monday) to 7 (Sunday).
   *
   * @param dayOfWeek  the day-of-week to represent, from 1 (Monday) to 7 (Sunday)
   * @return the day-of-week singleton, not null
   * @throws DateTimeException if the day-of-week is invalid
   */
  def of(dayOfWeek: Int): DayOfWeek = {
    if (dayOfWeek < 1 || dayOfWeek > 7) {
      throw new DateTimeException("Invalid value for DayOfWeek: " + dayOfWeek)
    }
     ENUMS(dayOfWeek - 1)
  }

  /**
   * Obtains an instance of {@code DayOfWeek} from a temporal object.
   * <p>
   * This obtains a day-of-week based on the specified temporal.
   * A {@code TemporalAccessor} represents an arbitrary set of date and time information,
   * which this factory converts to an instance of {@code DayOfWeek}.
   * <p>
   * The conversion extracts the {@link ChronoField#DAY_OF_WEEK DAY_OF_WEEK} field.
   * <p>
   * This method matches the signature of the functional interface {@link TemporalQuery}
   * allowing it to be used as a query via method reference, {@code DayOfWeek::from}.
   *
   * @param temporal  the temporal object to convert, not null
   * @return the day-of-week, not null
   * @throws DateTimeException if unable to convert to a { @code DayOfWeek}
   */
  def from(temporal: TemporalAccessor): DayOfWeek = {
    if (temporal.isInstanceOf[DayOfWeek]) {
       temporal.asInstanceOf[DayOfWeek]
    }
     of(temporal.get(DAY_OF_WEEK))
  }

  /**
   * The singleton instance for the day-of-week of Monday.
   * This has the numeric value of {@code 1}.
   */
  final val MONDAY: = null
  /**
   * The singleton instance for the day-of-week of Tuesday.
   * This has the numeric value of {@code 2}.
   */
  final val TUESDAY: = null
  /**
   * The singleton instance for the day-of-week of Wednesday.
   * This has the numeric value of {@code 3}.
   */
  final val WEDNESDAY: = null
  /**
   * The singleton instance for the day-of-week of Thursday.
   * This has the numeric value of {@code 4}.
   */
  final val THURSDAY: = null
  /**
   * The singleton instance for the day-of-week of Friday.
   * This has the numeric value of {@code 5}.
   */
  final val FRIDAY: = null
  /**
   * The singleton instance for the day-of-week of Saturday.
   * This has the numeric value of {@code 6}.
   */
  final val SATURDAY: = null
  /**
   * The singleton instance for the day-of-week of Sunday.
   * This has the numeric value of {@code 7}.
   */
  final val SUNDAY: = null
  /**
   * Private cache of all the constants.
   */
  private final val ENUMS: Array[DayOfWeek] = DayOfWeek.values
}

final class DayOfWeek extends TemporalAccessor with TemporalAdjuster {
  /**
   * Gets the day-of-week {@code int} value.
   * <p>
   * The values are numbered following the ISO-8601 standard, from 1 (Monday) to 7 (Sunday).
   * See {@link WeekFields#dayOfWeek} for localized week-numbering.
   *
   * @return the day-of-week, from 1 (Monday) to 7 (Sunday)
   */
  def getValue: Int = {
     ordinal + 1
  }

  /**
   * Gets the textual representation, such as 'Mon' or 'Friday'.
   * <p>
   * This returns the textual name used to identify the day-of-week,
   * suitable for presentation to the user.
   * The parameters control the style of the returned text and the locale.
   * <p>
   * If no textual mapping is found then the {@link #getValue() numeric value} is returned.
   *
   * @param style  the length of the text required, not null
   * @param locale  the locale to use, not null
   * @return the text value of the day-of-week, not null
   */
  def getDisplayName(style: TextStyle, locale: Locale): String = {
     new DateTimeFormatterBuilder().appendText(DAY_OF_WEEK, style).toFormatter(locale).format(this)
  }

  /**
   * Checks if the specified field is supported.
   * <p>
   * This checks if this day-of-week can be queried for the specified field.
   * If false, then calling the {@link #range(TemporalField) range} and
   * {@link #get(TemporalField) get} methods will throw an exception.
   * <p>
   * If the field is {@link ChronoField#DAY_OF_WEEK DAY_OF_WEEK} then
   * this method returns true.
   * All other {@code ChronoField} instances will  false.
   * <p>
   * If the field is not a {@code ChronoField}, then the result of this method
   * is obtained by invoking {@code TemporalField.isSupportedBy(TemporalAccessor)}
   * passing {@code this} as the argument.
   * Whether the field is supported is determined by the field.
   *
   * @param field  the field to check, null returns false
   * @return true if the field is supported on this day-of-week, false if not
   */
  def isSupported(field: TemporalField): Boolean = {
    if (field.isInstanceOf[ChronoField]) {
       field eq DAY_OF_WEEK
    }
     field != null && field.isSupportedBy(this)
  }

  /**
   * Gets the range of valid values for the specified field.
   * <p>
   * The range object expresses the minimum and maximum valid values for a field.
   * This day-of-week is used to enhance the accuracy of the returned range.
   * If it is not possible to  the range, because the field is not supported
   * or for some other reason, an exception is thrown.
   * <p>
   * If the field is {@link ChronoField#DAY_OF_WEEK DAY_OF_WEEK} then the
   * range of the day-of-week, from 1 to 7, will be returned.
   * All other {@code ChronoField} instances will throw an {@code UnsupportedTemporalTypeException}.
   * <p>
   * If the field is not a {@code ChronoField}, then the result of this method
   * is obtained by invoking {@code TemporalField.rangeRefinedBy(TemporalAccessor)}
   * passing {@code this} as the argument.
   * Whether the range can be obtained is determined by the field.
   *
   * @param field  the field to query the range for, not null
   * @return the range of valid values for the field, not null
   * @throws DateTimeException if the range for the field cannot be obtained
   * @throws UnsupportedTemporalTypeException if the field is not supported
   */
  override def range(field: TemporalField): ValueRange = {
    if (field eq DAY_OF_WEEK) {
       field.range
    }
     TemporalAccessor.super.range(field)
  }

  /**
   * Gets the value of the specified field from this day-of-week as an {@code int}.
   * <p>
   * This queries this day-of-week for the value for the specified field.
   * The returned value will always be within the valid range of values for the field.
   * If it is not possible to  the value, because the field is not supported
   * or for some other reason, an exception is thrown.
   * <p>
   * If the field is {@link ChronoField#DAY_OF_WEEK DAY_OF_WEEK} then the
   * value of the day-of-week, from 1 to 7, will be returned.
   * All other {@code ChronoField} instances will throw an {@code UnsupportedTemporalTypeException}.
   * <p>
   * If the field is not a {@code ChronoField}, then the result of this method
   * is obtained by invoking {@code TemporalField.getFrom(TemporalAccessor)}
   * passing {@code this} as the argument. Whether the value can be obtained,
   * and what the value represents, is determined by the field.
   *
   * @param field  the field to get, not null
   * @return the value for the field, within the valid range of values
   * @throws DateTimeException if a value for the field cannot be obtained or
   *                           the value is outside the range of valid values for the field
   * @throws UnsupportedTemporalTypeException if the field is not supported or
   *                                          the range of values exceeds an { @code int}
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def get(field: TemporalField): Int = {
    if (field eq DAY_OF_WEEK) {
       getValue
    }
     TemporalAccessor.super.get(field)
  }

  /**
   * Gets the value of the specified field from this day-of-week as a {@code long}.
   * <p>
   * This queries this day-of-week for the value for the specified field.
   * If it is not possible to  the value, because the field is not supported
   * or for some other reason, an exception is thrown.
   * <p>
   * If the field is {@link ChronoField#DAY_OF_WEEK DAY_OF_WEEK} then the
   * value of the day-of-week, from 1 to 7, will be returned.
   * All other {@code ChronoField} instances will throw an {@code UnsupportedTemporalTypeException}.
   * <p>
   * If the field is not a {@code ChronoField}, then the result of this method
   * is obtained by invoking {@code TemporalField.getFrom(TemporalAccessor)}
   * passing {@code this} as the argument. Whether the value can be obtained,
   * and what the value represents, is determined by the field.
   *
   * @param field  the field to get, not null
   * @return the value for the field
   * @throws DateTimeException if a value for the field cannot be obtained
   * @throws UnsupportedTemporalTypeException if the field is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  def getLong(field: TemporalField): Long = {
    if (field eq DAY_OF_WEEK) {
       getValue
    }
    else if (field.isInstanceOf[ChronoField]) {
      throw new UnsupportedTemporalTypeException("Unsupported field: " + field)
    }
     field.getFrom(this)
  }

  /**
   * Returns the day-of-week that is the specified number of days after this one.
   * <p>
   * The calculation rolls around the end of the week from Sunday to Monday.
   * The specified period may be negative.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param days  the days to add, positive or negative
   * @return the resulting day-of-week, not null
   */
  def plus(days: Long): DayOfWeek = {
    val amount: Int = (days % 7).asInstanceOf[Int]
     ENUMS((ordinal + (amount + 7)) % 7)
  }

  /**
   * Returns the day-of-week that is the specified number of days before this one.
   * <p>
   * The calculation rolls around the start of the year from Monday to Sunday.
   * The specified period may be negative.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param days  the days to subtract, positive or negative
   * @return the resulting day-of-week, not null
   */
  def minus(days: Long): DayOfWeek = {
     plus(-(days % 7))
  }

  /**
   * Queries this day-of-week using the specified query.
   * <p>
   * This queries this day-of-week using the specified query strategy object.
   * The {@code TemporalQuery} object defines the logic to be used to
   * obtain the result. Read the documentation of the query to understand
   * what the result of this method will be.
   * <p>
   * The result of this method is obtained by invoking the
   * {@link TemporalQuery#queryFrom(TemporalAccessor)} method on the
   * specified query passing {@code this} as the argument.
   *
   * @param <R> the type of the result
   * @param query  the query to invoke, not null
   * @return the query result, null may be returned (defined by the query)
   * @throws DateTimeException if unable to query (defined by the query)
   * @throws ArithmeticException if numeric overflow occurs (defined by the query)
   */
  override def query(query: TemporalQuery[R]): R = {
    if (query eq TemporalQuery.precision) {
       DAYS.asInstanceOf[R]
    }
     TemporalAccessor.super.query(query)
  }

  /**
   * Adjusts the specified temporal object to have this day-of-week.
   * <p>
   * This returns a temporal object of the same observable type as the input
   * with the day-of-week changed to be the same as this.
   * <p>
   * The adjustment is equivalent to using {@link Temporal#with(TemporalField, long)}
   * passing {@link ChronoField#DAY_OF_WEEK} as the field.
   * Note that this adjusts forwards or backwards within a Monday to Sunday week.
   * See {@link WeekFields#dayOfWeek} for localized week start days.
   * See {@code TemporalAdjuster} for other adjusters with more control,
   * such as {@code next(MONDAY)}.
   * <p>
   * In most cases, it is clearer to reverse the calling pattern by using
   * {@link Temporal#with(TemporalAdjuster)}:
   * {{{
   * // these two lines are equivalent, but the second approach is recommended
   * temporal = thisDayOfWeek.adjustInto(temporal);
   * temporal = temporal.with(thisDayOfWeek);
   * }}}
   * <p>
   * For example, given a date that is a Wednesday, the following are output:
   * {{{
   * dateOnWed.with(MONDAY);     // two days earlier
   * dateOnWed.with(TUESDAY);    // one day earlier
   * dateOnWed.with(WEDNESDAY);  // same date
   * dateOnWed.with(THURSDAY);   // one day later
   * dateOnWed.with(FRIDAY);     // two days later
   * dateOnWed.with(SATURDAY);   // three days later
   * dateOnWed.with(SUNDAY);     // four days later
   * }}}
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param temporal  the target object to be adjusted, not null
   * @return the adjusted object, not null
   * @throws DateTimeException if unable to make the adjustment
   * @throws ArithmeticException if numeric overflow occurs
   */
  def adjustInto(temporal: Temporal): Temporal = {
     temporal.`with`(DAY_OF_WEEK, getValue)
  }
}

/**
 * A month-of-year, such as 'July'.
 * <p>
 * {@code Month} is an enum representing the 12 months of the year -
 * January, February, March, April, May, June, July, August, September, October,
 * November and December.
 * <p>
 * In addition to the textual enum name, each month-of-year has an {@code int} value.
 * The {@code int} value follows normal usage and the ISO-8601 standard,
 * from 1 (January) to 12 (December). It is recommended that applications use the enum
 * rather than the {@code int} value to ensure code clarity.
 * <p>
 * <b>Do not use {@code ordinal()} to obtain the numeric representation of {@code Month}.
 * Use {@code getValue()} instead.</b>
 * <p>
 * This enum represents a common concept that is found in many calendar systems.
 * As such, this enum may be used by any calendar system that has the month-of-year
 * concept defined exactly equivalent to the ISO-8601 calendar system.
 *
 * @implSpec
 * This is an immutable and thread-safe enum.
 *
 * @since 1.8
 */
object Month {
  /**
   * Obtains an instance of {@code Month} from an {@code int} value.
   * <p>
   * {@code Month} is an enum representing the 12 months of the year.
   * This factory allows the enum to be obtained from the {@code int} value.
   * The {@code int} value follows the ISO-8601 standard, from 1 (January) to 12 (December).
   *
   * @param month  the month-of-year to represent, from 1 (January) to 12 (December)
   * @return the month-of-year, not null
   * @throws DateTimeException if the month-of-year is invalid
   */
  def of(month: Int): Month = {
    if (month < 1 || month > 12) {
      throw new DateTimeException("Invalid value for MonthOfYear: " + month)
    }
     ENUMS(month - 1)
  }

  /**
   * Obtains an instance of {@code Month} from a temporal object.
   * <p>
   * This obtains a month based on the specified temporal.
   * A {@code TemporalAccessor} represents an arbitrary set of date and time information,
   * which this factory converts to an instance of {@code Month}.
   * <p>
   * The conversion extracts the {@link ChronoField#MONTH_OF_YEAR MONTH_OF_YEAR} field.
   * The extraction is only permitted if the temporal object has an ISO
   * chronology, or can be converted to a {@code Date}.
   * <p>
   * This method matches the signature of the functional interface {@link TemporalQuery}
   * allowing it to be used in queries via method reference, {@code Month::from}.
   *
   * @param temporal  the temporal object to convert, not null
   * @return the month-of-year, not null
   * @throws DateTimeException if unable to convert to a { @code Month}
   */
  def from(temporal: TemporalAccessor): Month = {
    if (temporal.isInstanceOf[Month]) {
       temporal.asInstanceOf[Month]
    }
    try {
      if ((IsoChronology.INSTANCE == Chronology.from(temporal)) == false) {
        temporal = Date.from(temporal)
      }
       of(temporal.get(MONTH_OF_YEAR))
    }
    catch {
      case ex: DateTimeException => {
        throw new DateTimeException("Unable to obtain Month from TemporalAccessor: " + temporal.getClass, ex)
      }
    }
  }

  /**
   * The singleton instance for the month of January with 31 days.
   * This has the numeric value of {@code 1}.
   */
  final val JANUARY: = null
  /**
   * The singleton instance for the month of February with 28 days, or 29 in a leap year.
   * This has the numeric value of {@code 2}.
   */
  final val FEBRUARY: = null
  /**
   * The singleton instance for the month of March with 31 days.
   * This has the numeric value of {@code 3}.
   */
  final val MARCH: = null
  /**
   * The singleton instance for the month of April with 30 days.
   * This has the numeric value of {@code 4}.
   */
  final val APRIL: = null
  /**
   * The singleton instance for the month of May with 31 days.
   * This has the numeric value of {@code 5}.
   */
  final val MAY: = null
  /**
   * The singleton instance for the month of June with 30 days.
   * This has the numeric value of {@code 6}.
   */
  final val JUNE: = null
  /**
   * The singleton instance for the month of July with 31 days.
   * This has the numeric value of {@code 7}.
   */
  final val JULY: = null
  /**
   * The singleton instance for the month of August with 31 days.
   * This has the numeric value of {@code 8}.
   */
  final val AUGUST: = null
  /**
   * The singleton instance for the month of September with 30 days.
   * This has the numeric value of {@code 9}.
   */
  final val SEPTEMBER: = null
  /**
   * The singleton instance for the month of October with 31 days.
   * This has the numeric value of {@code 10}.
   */
  final val OCTOBER: = null
  /**
   * The singleton instance for the month of November with 30 days.
   * This has the numeric value of {@code 11}.
   */
  final val NOVEMBER: = null
  /**
   * The singleton instance for the month of December with 31 days.
   * This has the numeric value of {@code 12}.
   */
  final val DECEMBER: = null
  /**
   * Private cache of all the constants.
   */
  private final val ENUMS: Array[Month] = Month.values
}

final class Month extends TemporalAccessor with TemporalAdjuster {
  /**
   * Gets the month-of-year {@code int} value.
   * <p>
   * The values are numbered following the ISO-8601 standard,
   * from 1 (January) to 12 (December).
   *
   * @return the month-of-year, from 1 (January) to 12 (December)
   */
  def getValue: Int = {
     ordinal + 1
  }

  /**
   * Gets the textual representation, such as 'Jan' or 'December'.
   * <p>
   * This returns the textual name used to identify the month-of-year,
   * suitable for presentation to the user.
   * The parameters control the style of the returned text and the locale.
   * <p>
   * If no textual mapping is found then the {@link #getValue() numeric value} is returned.
   *
   * @param style  the length of the text required, not null
   * @param locale  the locale to use, not null
   * @return the text value of the month-of-year, not null
   */
  def getDisplayName(style: TextStyle, locale: Locale): String = {
     new DateTimeFormatterBuilder().appendText(MONTH_OF_YEAR, style).toFormatter(locale).format(this)
  }

  /**
   * Checks if the specified field is supported.
   * <p>
   * This checks if this month-of-year can be queried for the specified field.
   * If false, then calling the {@link #range(TemporalField) range} and
   * {@link #get(TemporalField) get} methods will throw an exception.
   * <p>
   * If the field is {@link ChronoField#MONTH_OF_YEAR MONTH_OF_YEAR} then
   * this method returns true.
   * All other {@code ChronoField} instances will  false.
   * <p>
   * If the field is not a {@code ChronoField}, then the result of this method
   * is obtained by invoking {@code TemporalField.isSupportedBy(TemporalAccessor)}
   * passing {@code this} as the argument.
   * Whether the field is supported is determined by the field.
   *
   * @param field  the field to check, null returns false
   * @return true if the field is supported on this month-of-year, false if not
   */
  def isSupported(field: TemporalField): Boolean = {
    if (field.isInstanceOf[ChronoField]) {
       field eq MONTH_OF_YEAR
    }
     field != null && field.isSupportedBy(this)
  }

  /**
   * Gets the range of valid values for the specified field.
   * <p>
   * The range object expresses the minimum and maximum valid values for a field.
   * This month is used to enhance the accuracy of the returned range.
   * If it is not possible to  the range, because the field is not supported
   * or for some other reason, an exception is thrown.
   * <p>
   * If the field is {@link ChronoField#MONTH_OF_YEAR MONTH_OF_YEAR} then the
   * range of the month-of-year, from 1 to 12, will be returned.
   * All other {@code ChronoField} instances will throw an {@code UnsupportedTemporalTypeException}.
   * <p>
   * If the field is not a {@code ChronoField}, then the result of this method
   * is obtained by invoking {@code TemporalField.rangeRefinedBy(TemporalAccessor)}
   * passing {@code this} as the argument.
   * Whether the range can be obtained is determined by the field.
   *
   * @param field  the field to query the range for, not null
   * @return the range of valid values for the field, not null
   * @throws DateTimeException if the range for the field cannot be obtained
   * @throws UnsupportedTemporalTypeException if the field is not supported
   */
  override def range(field: TemporalField): ValueRange = {
    if (field eq MONTH_OF_YEAR) {
       field.range
    }
     TemporalAccessor.super.range(field)
  }

  /**
   * Gets the value of the specified field from this month-of-year as an {@code int}.
   * <p>
   * This queries this month for the value for the specified field.
   * The returned value will always be within the valid range of values for the field.
   * If it is not possible to  the value, because the field is not supported
   * or for some other reason, an exception is thrown.
   * <p>
   * If the field is {@link ChronoField#MONTH_OF_YEAR MONTH_OF_YEAR} then the
   * value of the month-of-year, from 1 to 12, will be returned.
   * All other {@code ChronoField} instances will throw an {@code UnsupportedTemporalTypeException}.
   * <p>
   * If the field is not a {@code ChronoField}, then the result of this method
   * is obtained by invoking {@code TemporalField.getFrom(TemporalAccessor)}
   * passing {@code this} as the argument. Whether the value can be obtained,
   * and what the value represents, is determined by the field.
   *
   * @param field  the field to get, not null
   * @return the value for the field, within the valid range of values
   * @throws DateTimeException if a value for the field cannot be obtained or
   *                           the value is outside the range of valid values for the field
   * @throws UnsupportedTemporalTypeException if the field is not supported or
   *                                          the range of values exceeds an { @code int}
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def get(field: TemporalField): Int = {
    if (field eq MONTH_OF_YEAR) {
       getValue
    }
     TemporalAccessor.super.get(field)
  }

  /**
   * Gets the value of the specified field from this month-of-year as a {@code long}.
   * <p>
   * This queries this month for the value for the specified field.
   * If it is not possible to  the value, because the field is not supported
   * or for some other reason, an exception is thrown.
   * <p>
   * If the field is {@link ChronoField#MONTH_OF_YEAR MONTH_OF_YEAR} then the
   * value of the month-of-year, from 1 to 12, will be returned.
   * All other {@code ChronoField} instances will throw an {@code UnsupportedTemporalTypeException}.
   * <p>
   * If the field is not a {@code ChronoField}, then the result of this method
   * is obtained by invoking {@code TemporalField.getFrom(TemporalAccessor)}
   * passing {@code this} as the argument. Whether the value can be obtained,
   * and what the value represents, is determined by the field.
   *
   * @param field  the field to get, not null
   * @return the value for the field
   * @throws DateTimeException if a value for the field cannot be obtained
   * @throws UnsupportedTemporalTypeException if the field is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  def getLong(field: TemporalField): Long = {
    if (field eq MONTH_OF_YEAR) {
       getValue
    }
    else if (field.isInstanceOf[ChronoField]) {
      throw new UnsupportedTemporalTypeException("Unsupported field: " + field)
    }
     field.getFrom(this)
  }

  /**
   * Returns the month-of-year that is the specified number of quarters after this one.
   * <p>
   * The calculation rolls around the end of the year from December to January.
   * The specified period may be negative.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param months  the months to add, positive or negative
   * @return the resulting month, not null
   */
  def plus(months: Long): Month = {
    val amount: Int = (months % 12).asInstanceOf[Int]
     ENUMS((ordinal + (amount + 12)) % 12)
  }

  /**
   * Returns the month-of-year that is the specified number of months before this one.
   * <p>
   * The calculation rolls around the start of the year from January to December.
   * The specified period may be negative.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param months  the months to subtract, positive or negative
   * @return the resulting month, not null
   */
  def minus(months: Long): Month = {
     plus(-(months % 12))
  }

  /**
   * Gets the length of this month in days.
   * <p>
   * This takes a flag to determine whether to  the length for a leap year or not.
   * <p>
   * February has 28 days in a standard year and 29 days in a leap year.
   * April, June, September and November have 30 days.
   * All other months have 31 days.
   *
   * @param leapYear  true if the length is required for a leap year
   * @return the length of this month in days, from 28 to 31
   */
  def length(leapYear: Boolean): Int = {
    this match {
      case FEBRUARY =>
         (if (leapYear) 29 else 28)
      case APRIL =>
      case JUNE =>
      case SEPTEMBER =>
      case NOVEMBER =>
         30
      case _ =>
         31
    }
  }

  /**
   * Gets the minimum length of this month in days.
   * <p>
   * February has a minimum length of 28 days.
   * April, June, September and November have 30 days.
   * All other months have 31 days.
   *
   * @return the minimum length of this month in days, from 28 to 31
   */
  def minLength: Int = {
    this match {
      case FEBRUARY =>
         28
      case APRIL =>
      case JUNE =>
      case SEPTEMBER =>
      case NOVEMBER =>
         30
      case _ =>
         31
    }
  }

  /**
   * Gets the maximum length of this month in days.
   * <p>
   * February has a maximum length of 29 days.
   * April, June, September and November have 30 days.
   * All other months have 31 days.
   *
   * @return the maximum length of this month in days, from 29 to 31
   */
  def maxLength: Int = {
    this match {
      case FEBRUARY =>
         29
      case APRIL =>
      case JUNE =>
      case SEPTEMBER =>
      case NOVEMBER =>
         30
      case _ =>
         31
    }
  }

  /**
   * Gets the day-of-year corresponding to the first day of this month.
   * <p>
   * This returns the day-of-year that this month begins on, using the leap
   * year flag to determine the length of February.
   *
   * @param leapYear  true if the length is required for a leap year
   * @return the day of year corresponding to the first day of this month, from 1 to 336
   */
  def firstDayOfYear(leapYear: Boolean): Int = {
    val leap: Int = if (leapYear) 1 else 0
    this match {
      case JANUARY =>
         1
      case FEBRUARY =>
         32
      case MARCH =>
         60 + leap
      case APRIL =>
         91 + leap
      case MAY =>
         121 + leap
      case JUNE =>
         152 + leap
      case JULY =>
         182 + leap
      case AUGUST =>
         213 + leap
      case SEPTEMBER =>
         244 + leap
      case OCTOBER =>
         274 + leap
      case NOVEMBER =>
         305 + leap
      case DECEMBER =>
      case _ =>
         335 + leap
    }
  }

  /**
   * Gets the month corresponding to the first month of this quarter.
   * <p>
   * The year can be divided into four quarters.
   * This method returns the first month of the quarter for the base month.
   * January, February and March  January.
   * April, May and June  April.
   * July, August and September  July.
   * October, November and December  October.
   *
   * @return the first month of the quarter corresponding to this month, not null
   */
  def firstMonthOfQuarter: Month = {
     ENUMS((ordinal / 3) * 3)
  }

  /**
   * Queries this month-of-year using the specified query.
   * <p>
   * This queries this month-of-year using the specified query strategy object.
   * The {@code TemporalQuery} object defines the logic to be used to
   * obtain the result. Read the documentation of the query to understand
   * what the result of this method will be.
   * <p>
   * The result of this method is obtained by invoking the
   * {@link TemporalQuery#queryFrom(TemporalAccessor)} method on the
   * specified query passing {@code this} as the argument.
   *
   * @param <R> the type of the result
   * @param query  the query to invoke, not null
   * @return the query result, null may be returned (defined by the query)
   * @throws DateTimeException if unable to query (defined by the query)
   * @throws ArithmeticException if numeric overflow occurs (defined by the query)
   */
  override def query(query: TemporalQuery[R]): R = {
    if (query eq TemporalQuery.chronology) {
       IsoChronology.INSTANCE.asInstanceOf[R]
    }
    else if (query eq TemporalQuery.precision) {
       MONTHS.asInstanceOf[R]
    }
     TemporalAccessor.super.query(query)
  }

  /**
   * Adjusts the specified temporal object to have this month-of-year.
   * <p>
   * This returns a temporal object of the same observable type as the input
   * with the month-of-year changed to be the same as this.
   * <p>
   * The adjustment is equivalent to using {@link Temporal#with(TemporalField, long)}
   * passing {@link ChronoField#MONTH_OF_YEAR} as the field.
   * If the specified temporal object does not use the ISO calendar system then
   * a {@code DateTimeException} is thrown.
   * <p>
   * In most cases, it is clearer to reverse the calling pattern by using
   * {@link Temporal#with(TemporalAdjuster)}:
   * {{{
   * // these two lines are equivalent, but the second approach is recommended
   * temporal = thisMonth.adjustInto(temporal);
   * temporal = temporal.with(thisMonth);
   * }}}
   * <p>
   * For example, given a date in May, the following are output:
   * {{{
   * dateInMay.with(JANUARY);    // four months earlier
   * dateInMay.with(APRIL);      // one months earlier
   * dateInMay.with(MAY);        // same date
   * dateInMay.with(JUNE);       // one month later
   * dateInMay.with(DECEMBER);   // seven months later
   * }}}
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param temporal  the target object to be adjusted, not null
   * @return the adjusted object, not null
   * @throws DateTimeException if unable to make the adjustment
   * @throws ArithmeticException if numeric overflow occurs
   */
  def adjustInto(temporal: Temporal): Temporal = {
    if ((Chronology.from(temporal) == IsoChronology.INSTANCE) == false) {
      throw new DateTimeException("Adjustment only supported on ISO date-time")
    }
     temporal.`with`(MONTH_OF_YEAR, getValue)
  }
}

/**
 * A month-day in the ISO-8601 calendar system, such as {@code --12-03}.
 * <p>
 * {@code MonthDay} is an immutable date-time object that represents the combination
 * of a year and month. Any field that can be derived from a month and day, such as
 * quarter-of-year, can be obtained.
 * <p>
 * This class does not store or represent a year, time or time-zone.
 * For example, the value "December 3rd" can be stored in a {@code MonthDay}.
 * <p>
 * Since a {@code MonthDay} does not possess a year, the leap day of
 * February 29th is considered valid.
 * <p>
 * This class implements {@link TemporalAccessor} rather than {@link Temporal}.
 * This is because it is not possible to define whether February 29th is valid or not
 * without external information, preventing the implementation of plus/minus.
 * Related to this, {@code MonthDay} only provides access to query and set the fields
 * {@code MONTH_OF_YEAR} and {@code DAY_OF_MONTH}.
 * <p>
 * The ISO-8601 calendar system is the modern civil calendar system used today
 * in most of the world. It is equivalent to the proleptic Gregorian calendar
 * system, in which today's rules for leap years are applied for all time.
 * For most applications written today, the ISO-8601 rules are entirely suitable.
 * However, any application that makes use of historical dates, and requires them
 * to be accurate will find the ISO-8601 approach unsuitable.
 *
 * @implSpec
 * This class is immutable and thread-safe.
 *
 * @since 1.8
 */
object MonthDay {
  /**
   * Obtains the current month-day from the system clock in the default time-zone.
   * <p>
   * This will query the {@link java.time.Clock#systemDefaultZone() system clock} in the default
   * time-zone to obtain the current month-day.
   * <p>
   * Using this method will prevent the ability to use an alternate clock for testing
   * because the clock is hard-coded.
   *
   * @return the current month-day using the system clock and default time-zone, not null
   */
  def now: MonthDay = {
     now(Clock.systemDefaultZone)
  }

  /**
   * Obtains the current month-day from the system clock in the specified time-zone.
   * <p>
   * This will query the {@link Clock#system(java.time.ZoneId) system clock} to obtain the current month-day.
   * Specifying the time-zone avoids dependence on the default time-zone.
   * <p>
   * Using this method will prevent the ability to use an alternate clock for testing
   * because the clock is hard-coded.
   *
   * @param zone  the zone ID to use, not null
   * @return the current month-day using the system clock, not null
   */
  def now(zone: ZoneId): MonthDay = {
     now(Clock.system(zone))
  }

  /**
   * Obtains the current month-day from the specified clock.
   * <p>
   * This will query the specified clock to obtain the current month-day.
   * Using this method allows the use of an alternate clock for testing.
   * The alternate clock may be introduced using {@link Clock dependency injection}.
   *
   * @param clock  the clock to use, not null
   * @return the current month-day, not null
   */
  def now(clock: Clock): MonthDay = {
    val now: Date = Date.now(clock)
     MonthDay.of(now.getMonth, now.getDayOfMonth)
  }

  /**
   * Obtains an instance of {@code MonthDay}.
   * <p>
   * The day-of-month must be valid for the month within a leap year.
   * Hence, for February, day 29 is valid.
   * <p>
   * For example, passing in April and day 31 will throw an exception, as
   * there can never be April 31st in any year. By contrast, passing in
   * February 29th is permitted, as that month-day can sometimes be valid.
   *
   * @param month  the month-of-year to represent, not null
   * @param dayOfMonth  the day-of-month to represent, from 1 to 31
   * @return the month-day, not null
   * @throws DateTimeException if the value of any field is out of range,
   *                           or if the day-of-month is invalid for the month
   */
  def of(month: Month, dayOfMonth: Int): MonthDay = {
    object
    DAY_OF_MONTH.checkValidValue(dayOfMonth)
    if (dayOfMonth > month.maxLength) {
      throw new DateTimeException("Illegal value for DayOfMonth field, value " + dayOfMonth + " is not valid for month " + month.name)
    }
     new MonthDay(month.getValue, dayOfMonth)
  }

  /**
   * Obtains an instance of {@code MonthDay}.
   * <p>
   * The day-of-month must be valid for the month within a leap year.
   * Hence, for month 2 (February), day 29 is valid.
   * <p>
   * For example, passing in month 4 (April) and day 31 will throw an exception, as
   * there can never be April 31st in any year. By contrast, passing in
   * February 29th is permitted, as that month-day can sometimes be valid.
   *
   * @param month  the month-of-year to represent, from 1 (January) to 12 (December)
   * @param dayOfMonth  the day-of-month to represent, from 1 to 31
   * @return the month-day, not null
   * @throws DateTimeException if the value of any field is out of range,
   *                           or if the day-of-month is invalid for the month
   */
  def of(month: Int, dayOfMonth: Int): MonthDay = {
     of(Month.of(month), dayOfMonth)
  }

  /**
   * Obtains an instance of {@code MonthDay} from a temporal object.
   * <p>
   * This obtains a month-day based on the specified temporal.
   * A {@code TemporalAccessor} represents an arbitrary set of date and time information,
   * which this factory converts to an instance of {@code MonthDay}.
   * <p>
   * The conversion extracts the {@link ChronoField#MONTH_OF_YEAR MONTH_OF_YEAR} and
   * {@link ChronoField#DAY_OF_MONTH DAY_OF_MONTH} fields.
   * The extraction is only permitted if the temporal object has an ISO
   * chronology, or can be converted to a {@code Date}.
   * <p>
   * This method matches the signature of the functional interface {@link TemporalQuery}
   * allowing it to be used in queries via method reference, {@code MonthDay::from}.
   *
   * @param temporal  the temporal object to convert, not null
   * @return the month-day, not null
   * @throws DateTimeException if unable to convert to a { @code MonthDay}
   */
  def from(temporal: TemporalAccessor): MonthDay = {
    if (temporal.isInstanceOf[MonthDay]) {
       temporal.asInstanceOf[MonthDay]
    }
    try {
      if ((IsoChronology.INSTANCE == Chronology.from(temporal)) == false) {
        temporal = Date.from(temporal)
      }
       of(temporal.get(MONTH_OF_YEAR), temporal.get(DAY_OF_MONTH))
    }
    catch {
      case ex: DateTimeException => {
        throw new DateTimeException("Unable to obtain MonthDay from TemporalAccessor: " + temporal.getClass, ex)
      }
    }
  }

  /**
   * Obtains an instance of {@code MonthDay} from a text string such as {@code --12-03}.
   * <p>
   * The string must represent a valid month-day.
   * The format is {@code --MM-dd}.
   *
   * @param text  the text to parse such as "--12-03", not null
   * @return the parsed month-day, not null
   * @throws DateTimeParseException if the text cannot be parsed
   */
  def parse(text: CharSequence): MonthDay = {
     parse(text, PARSER)
  }

  /**
   * Obtains an instance of {@code MonthDay} from a text string using a specific formatter.
   * <p>
   * The text is parsed using the formatter, returning a month-day.
   *
   * @param text  the text to parse, not null
   * @param formatter  the formatter to use, not null
   * @return the parsed month-day, not null
   * @throws DateTimeParseException if the text cannot be parsed
   */
  def parse(text: CharSequence, formatter: DateTimeFormatter): MonthDay = {
    object
     formatter.parse(text, MonthDay.from)
  }

  private[time] def readExternal(in: DataInput): MonthDay = {
    val month: Byte = in.readByte
    val day: Byte = in.readByte
     MonthDay.of(month, day)
  }


  /**
   * Parser.
   */
  private final val PARSER: DateTimeFormatter = new DateTimeFormatterBuilder().appendLiteral("--").appendValue(MONTH_OF_YEAR, 2).appendLiteral('-').appendValue(DAY_OF_MONTH, 2).toFormatter
}

final class MonthDay extends TemporalAccessor with TemporalAdjuster with Comparable[MonthDay]  {
  /**
   * Constructor, previously validated.
   *
   * @param month  the month-of-year to represent, validated from 1 to 12
   * @param dayOfMonth  the day-of-month to represent, validated from 1 to 29-31
   */
  private def this(month: Int, dayOfMonth: Int) {

    this.month = month
    this.day = dayOfMonth
  }

  /**
   * Checks if the specified field is supported.
   * <p>
   * This checks if this month-day can be queried for the specified field.
   * If false, then calling the {@link #range(TemporalField) range} and
   * {@link #get(TemporalField) get} methods will throw an exception.
   * <p>
   * If the field is a {@link ChronoField} then the query is implemented here.
   * The supported fields are:
   * <ul>
   * <li>{@code MONTH_OF_YEAR}
   * <li>{@code YEAR}
   * </ul>
   * All other {@code ChronoField} instances will  false.
   * <p>
   * If the field is not a {@code ChronoField}, then the result of this method
   * is obtained by invoking {@code TemporalField.isSupportedBy(TemporalAccessor)}
   * passing {@code this} as the argument.
   * Whether the field is supported is determined by the field.
   *
   * @param field  the field to check, null returns false
   * @return true if the field is supported on this month-day, false if not
   */
  def isSupported(field: TemporalField): Boolean = {
    if (field.isInstanceOf[ChronoField]) {
       field eq MONTH_OF_YEAR || field eq DAY_OF_MONTH
    }
     field != null && field.isSupportedBy(this)
  }

  /**
   * Gets the range of valid values for the specified field.
   * <p>
   * The range object expresses the minimum and maximum valid values for a field.
   * This month-day is used to enhance the accuracy of the returned range.
   * If it is not possible to  the range, because the field is not supported
   * or for some other reason, an exception is thrown.
   * <p>
   * If the field is a {@link ChronoField} then the query is implemented here.
   * The {@link #isSupported(TemporalField) supported fields} will
   * appropriate range instances.
   * All other {@code ChronoField} instances will throw an {@code UnsupportedTemporalTypeException}.
   * <p>
   * If the field is not a {@code ChronoField}, then the result of this method
   * is obtained by invoking {@code TemporalField.rangeRefinedBy(TemporalAccessor)}
   * passing {@code this} as the argument.
   * Whether the range can be obtained is determined by the field.
   *
   * @param field  the field to query the range for, not null
   * @return the range of valid values for the field, not null
   * @throws DateTimeException if the range for the field cannot be obtained
   * @throws UnsupportedTemporalTypeException if the field is not supported
   */
  override def range(field: TemporalField): ValueRange = {
    if (field eq MONTH_OF_YEAR) {
       field.range
    }
    else if (field eq DAY_OF_MONTH) {
       ValueRange.of(1, getMonth.minLength, getMonth.maxLength)
    }
     TemporalAccessor.super.range(field)
  }

  /**
   * Gets the value of the specified field from this month-day as an {@code int}.
   * <p>
   * This queries this month-day for the value for the specified field.
   * The returned value will always be within the valid range of values for the field.
   * If it is not possible to  the value, because the field is not supported
   * or for some other reason, an exception is thrown.
   * <p>
   * If the field is a {@link ChronoField} then the query is implemented here.
   * The {@link #isSupported(TemporalField) supported fields} will  valid
   * values based on this month-day.
   * All other {@code ChronoField} instances will throw an {@code UnsupportedTemporalTypeException}.
   * <p>
   * If the field is not a {@code ChronoField}, then the result of this method
   * is obtained by invoking {@code TemporalField.getFrom(TemporalAccessor)}
   * passing {@code this} as the argument. Whether the value can be obtained,
   * and what the value represents, is determined by the field.
   *
   * @param field  the field to get, not null
   * @return the value for the field
   * @throws DateTimeException if a value for the field cannot be obtained or
   *                           the value is outside the range of valid values for the field
   * @throws UnsupportedTemporalTypeException if the field is not supported or
   *                                          the range of values exceeds an { @code int}
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def get(field: TemporalField): Int = {
     range(field).checkValidIntValue(getLong(field), field)
  }

  /**
   * Gets the value of the specified field from this month-day as a {@code long}.
   * <p>
   * This queries this month-day for the value for the specified field.
   * If it is not possible to  the value, because the field is not supported
   * or for some other reason, an exception is thrown.
   * <p>
   * If the field is a {@link ChronoField} then the query is implemented here.
   * The {@link #isSupported(TemporalField) supported fields} will  valid
   * values based on this month-day.
   * All other {@code ChronoField} instances will throw an {@code UnsupportedTemporalTypeException}.
   * <p>
   * If the field is not a {@code ChronoField}, then the result of this method
   * is obtained by invoking {@code TemporalField.getFrom(TemporalAccessor)}
   * passing {@code this} as the argument. Whether the value can be obtained,
   * and what the value represents, is determined by the field.
   *
   * @param field  the field to get, not null
   * @return the value for the field
   * @throws DateTimeException if a value for the field cannot be obtained
   * @throws UnsupportedTemporalTypeException if the field is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  def getLong(field: TemporalField): Long = {
    if (field.isInstanceOf[ChronoField]) {
      field.asInstanceOf[ChronoField] match {
        case DAY_OF_MONTH =>
           day
        case MONTH_OF_YEAR =>
           month
      }
      throw new UnsupportedTemporalTypeException("Unsupported field: " + field)
    }
     field.getFrom(this)
  }

  /**
   * Gets the month-of-year field from 1 to 12.
   * <p>
   * This method returns the month as an {@code int} from 1 to 12.
   * Application code is frequently clearer if the enum {@link Month}
   * is used by calling {@link #getMonth()}.
   *
   * @return the month-of-year, from 1 to 12
   * @see #getMonth()
   */
  def getMonthValue: Int = {
     month
  }

  /**
   * Gets the month-of-year field using the {@code Month} enum.
   * <p>
   * This method returns the enum {@link Month} for the month.
   * This avoids confusion as to what {@code int} values mean.
   * If you need access to the primitive {@code int} value then the enum
   * provides the {@link Month#getValue() int value}.
   *
   * @return the month-of-year, not null
   * @see #getMonthValue()
   */
  def getMonth: Month = {
     Month.of(month)
  }

  /**
   * Gets the day-of-month field.
   * <p>
   * This method returns the primitive {@code int} value for the day-of-month.
   *
   * @return the day-of-month, from 1 to 31
   */
  def getDayOfMonth: Int = {
     day
  }

  /**
   * Checks if the year is valid for this month-day.
   * <p>
   * This method checks whether this month and day and the input year form
   * a valid date. This can only  false for February 29th.
   *
   * @param year  the year to validate, an out of range value returns false
   * @return true if the year is valid for this month-day
   * @see Year#isValidMonthDay(MonthDay)
   */
  def isValidYear(year: Int): Boolean = {
     (day == 29 && month == 2 && Year.isLeap(year) == false) == false
  }

  /**
   * Returns a copy of this {@code MonthDay} with the month-of-year altered.
   * <p>
   * This returns a month-day with the specified month.
   * If the day-of-month is invalid for the specified month, the day will
   * be adjusted to the last valid day-of-month.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param month  the month-of-year to set in the returned month-day, from 1 (January) to 12 (December)
   * @return a { @code MonthDay} based on this month-day with the requested month, not null
   * @throws DateTimeException if the month-of-year value is invalid
   */
  def withMonth(month: Int): MonthDay = {
     `with`(Month.of(month))
  }

  /**
   * Returns a copy of this {@code MonthDay} with the month-of-year altered.
   * <p>
   * This returns a month-day with the specified month.
   * If the day-of-month is invalid for the specified month, the day will
   * be adjusted to the last valid day-of-month.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param month  the month-of-year to set in the returned month-day, not null
   * @return a { @code MonthDay} based on this month-day with the requested month, not null
   */
  def `with`(month: Month): MonthDay = {
    object
    if (month.getValue == this.month) {
       this
    }
    val day: Int = Math.min(this.day, month.maxLength)
     new MonthDay(month.getValue, day)
  }

  /**
   * Returns a copy of this {@code MonthDay} with the day-of-month altered.
   * <p>
   * This returns a month-day with the specified day-of-month.
   * If the day-of-month is invalid for the month, an exception is thrown.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param dayOfMonth  the day-of-month to set in the  month-day, from 1 to 31
   * @return a { @code MonthDay} based on this month-day with the requested day, not null
   * @throws DateTimeException if the day-of-month value is invalid,
   *                           or if the day-of-month is invalid for the month
   */
  def withDayOfMonth(dayOfMonth: Int): MonthDay = {
    if (dayOfMonth == this.day) {
       this
    }
     of(month, dayOfMonth)
  }

  /**
   * Queries this month-day using the specified query.
   * <p>
   * This queries this month-day using the specified query strategy object.
   * The {@code TemporalQuery} object defines the logic to be used to
   * obtain the result. Read the documentation of the query to understand
   * what the result of this method will be.
   * <p>
   * The result of this method is obtained by invoking the
   * {@link TemporalQuery#queryFrom(TemporalAccessor)} method on the
   * specified query passing {@code this} as the argument.
   *
   * @param <R> the type of the result
   * @param query  the query to invoke, not null
   * @return the query result, null may be returned (defined by the query)
   * @throws DateTimeException if unable to query (defined by the query)
   * @throws ArithmeticException if numeric overflow occurs (defined by the query)
   */
  override def query(query: TemporalQuery[R]): R = {
    if (query eq TemporalQuery.chronology) {
       IsoChronology.INSTANCE.asInstanceOf[R]
    }
     TemporalAccessor.super.query(query)
  }

  /**
   * Adjusts the specified temporal object to have this month-day.
   * <p>
   * This returns a temporal object of the same observable type as the input
   * with the month and day-of-month changed to be the same as this.
   * <p>
   * The adjustment is equivalent to using {@link Temporal#with(TemporalField, long)}
   * twice, passing {@link ChronoField#MONTH_OF_YEAR} and
   * {@link ChronoField#DAY_OF_MONTH} as the fields.
   * If the specified temporal object does not use the ISO calendar system then
   * a {@code DateTimeException} is thrown.
   * <p>
   * In most cases, it is clearer to reverse the calling pattern by using
   * {@link Temporal#with(TemporalAdjuster)}:
   * {{{
   * // these two lines are equivalent, but the second approach is recommended
   * temporal = thisMonthDay.adjustInto(temporal);
   * temporal = temporal.with(thisMonthDay);
   * }}}
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param temporal  the target object to be adjusted, not null
   * @return the adjusted object, not null
   * @throws DateTimeException if unable to make the adjustment
   * @throws ArithmeticException if numeric overflow occurs
   */
  def adjustInto(temporal: Temporal): Temporal = {
    if ((Chronology.from(temporal) == IsoChronology.INSTANCE) == false) {
      throw new DateTimeException("Adjustment only supported on ISO date-time")
    }
    temporal = temporal.`with`(MONTH_OF_YEAR, month)
     temporal.`with`(DAY_OF_MONTH, Math.min(temporal.range(DAY_OF_MONTH).getMaximum, day))
  }

  /**
   * Formats this month-day using the specified formatter.
   * <p>
   * This month-day will be passed to the formatter to produce a string.
   *
   * @param formatter  the formatter to use, not null
   * @return the formatted month-day string, not null
   * @throws DateTimeException if an error occurs during printing
   */
  def format(formatter: DateTimeFormatter): String = {
    object
     formatter.format(this)
  }

  /**
   * Combines this month-day with a year to create a {@code Date}.
   * <p>
   * This returns a {@code Date} formed from this month-day and the specified year.
   * <p>
   * A month-day of February 29th will be adjusted to February 28th in the resulting
   * date if the year is not a leap year.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param year  the year to use, from MIN_YEAR to MAX_YEAR
   * @return the local date formed from this month-day and the specified year, not null
   * @throws DateTimeException if the year is outside the valid range of years
   */
  def atYear(year: Int): Date = {
     Date.of(year, month, if (isValidYear(year)) day else 28)
  }

  /**
   * Compares this month-day to another month-day.
   * <p>
   * The comparison is based first on value of the month, then on the value of the day.
   * It is "consistent with equals", as defined by {@link Comparable}.
   *
   * @param other  the other month-day to compare to, not null
   * @return the comparator value, negative if less, positive if greater
   */
  def compareTo(other: MonthDay): Int = {
    var cmp: Int = (month - other.month)
    if (cmp == 0) {
      cmp = (day - other.day)
    }
     cmp
  }

  /**
   * Is this month-day after the specified month-day.
   *
   * @param other  the other month-day to compare to, not null
   * @return true if this is after the specified month-day
   */
  def isAfter(other: MonthDay): Boolean = {
     compareTo(other) > 0
  }

  /**
   * Is this month-day before the specified month-day.
   *
   * @param other  the other month-day to compare to, not null
   * @return true if this point is before the specified month-day
   */
  def isBefore(other: MonthDay): Boolean = {
     compareTo(other) < 0
  }

  /**
   * Checks if this month-day is equal to another month-day.
   * <p>
   * The comparison is based on the time-line position of the month-day within a year.
   *
   * @param obj  the object to check, null returns false
   * @return true if this is equal to the other month-day
   */
  override def equals(obj: AnyRef): Boolean = {
    if (this eq obj) {
       true
    }
    if (obj.isInstanceOf[MonthDay]) {
      val other: MonthDay = obj.asInstanceOf[MonthDay]
       month == other.month && day == other.day
    }
     false
  }

  /**
   * A hash code for this month-day.
   *
   * @return a suitable hash code
   */
  override def hashCode: Int = {
     (month << 6) + day
  }

  /**
   * Outputs this month-day as a {@code String}, such as {@code --12-03}.
   * <p>
   * The output will be in the format {@code --MM-dd}:
   *
   * @return a string representation of this month-day, not null
   */
  override def toString: String = {
     new StringBuilder(10).append("--").append(if (month < 10) "0" else "").append(month).append(if (day < 10) "-0" else "-").append(day).toString
  }

  /**
   * Writes the object using a
   * <a href="../../../serialized-form.html#java.time.temporal.Ser">dedicated serialized form</a>.
   * {{{
   * out.writeByte(13);  // identifies this as a MonthDay
   * out.writeByte(month);
   * out.writeByte(day);
   * }}}
   *
   * @return the instance of { @code Ser}, not null
   */
  private def writeReplace: AnyRef = {
     new Ser(Ser.MONTH_DAY_TYPE, this)
  }

  /**
   * Defend against malicious streams.
   * @return never
   * @throws InvalidObjectException always
   */
  private def readResolve: AnyRef = {
    throw new InvalidObjectException("Deserialization via serialization delegate")
  }

  private[time] def writeExternal(out: DataOutput) {
    out.writeByte(month)
    out.writeByte(day)
  }

  /**
   * The month-of-year, not null.
   */
  private final val month: Int = 0
  /**
   * The day-of-month.
   */
  private final val day: Int = 0
}

/**
 * A year in the ISO-8601 calendar system, such as {@code 2007}.
 * <p>
 * {@code Year} is an immutable date-time object that represents a year.
 * Any field that can be derived from a year can be obtained.
 * <p>
 * <b>Note that years in the ISO chronology only align with years in the
 * Gregorian-Julian system for modern years. Parts of Russia did not switch to the
 * modern Gregorian/ISO rules until 1920.
 * As such, historical years must be treated with caution.</b>
 * <p>
 * This class does not store or represent a month, day, time or time-zone.
 * For example, the value "2007" can be stored in a {@code Year}.
 * <p>
 * Years represented by this class follow the ISO-8601 standard and use
 * the proleptic numbering system. Year 1 is preceded by year 0, then by year -1.
 * <p>
 * The ISO-8601 calendar system is the modern civil calendar system used today
 * in most of the world. It is equivalent to the proleptic Gregorian calendar
 * system, in which today's rules for leap years are applied for all time.
 * For most applications written today, the ISO-8601 rules are entirely suitable.
 * However, any application that makes use of historical dates, and requires them
 * to be accurate will find the ISO-8601 approach unsuitable.
 *
 * @implSpec
 * This class is immutable and thread-safe.
 *
 * @since 1.8
 */
object Year {
  /**
   * Obtains the current year from the system clock in the default time-zone.
   * <p>
   * This will query the {@link java.time.Clock#systemDefaultZone() system clock} in the default
   * time-zone to obtain the current year.
   * <p>
   * Using this method will prevent the ability to use an alternate clock for testing
   * because the clock is hard-coded.
   *
   * @return the current year using the system clock and default time-zone, not null
   */
  def now: Year = {
     now(Clock.systemDefaultZone)
  }

  /**
   * Obtains the current year from the system clock in the specified time-zone.
   * <p>
   * This will query the {@link Clock#system(java.time.ZoneId) system clock} to obtain the current year.
   * Specifying the time-zone avoids dependence on the default time-zone.
   * <p>
   * Using this method will prevent the ability to use an alternate clock for testing
   * because the clock is hard-coded.
   *
   * @param zone  the zone ID to use, not null
   * @return the current year using the system clock, not null
   */
  def now(zone: ZoneId): Year = {
     now(Clock.system(zone))
  }

  /**
   * Obtains the current year from the specified clock.
   * <p>
   * This will query the specified clock to obtain the current year.
   * Using this method allows the use of an alternate clock for testing.
   * The alternate clock may be introduced using {@link Clock dependency injection}.
   *
   * @param clock  the clock to use, not null
   * @return the current year, not null
   */
  def now(clock: Clock): Year = {
    val now: Date = Date.now(clock)
     Year.of(now.getYear)
  }

  /**
   * Obtains an instance of {@code Year}.
   * <p>
   * This method accepts a year value from the proleptic ISO calendar system.
   * <p>
   * The year 2AD/CE is represented by 2.<br>
   * The year 1AD/CE is represented by 1.<br>
   * The year 1BC/BCE is represented by 0.<br>
   * The year 2BC/BCE is represented by -1.<br>
   *
   * @param isoYear  the ISO proleptic year to represent, from { @code MIN_VALUE} to { @code MAX_VALUE}
   * @return the year, not null
   * @throws DateTimeException if the field is invalid
   */
  def of(isoYear: Int): Year = {
    YEAR.checkValidValue(isoYear)
     new Year(isoYear)
  }

  /**
   * Obtains an instance of {@code Year} from a temporal object.
   * <p>
   * This obtains a year based on the specified temporal.
   * A {@code TemporalAccessor} represents an arbitrary set of date and time information,
   * which this factory converts to an instance of {@code Year}.
   * <p>
   * The conversion extracts the {@link ChronoField#YEAR year} field.
   * The extraction is only permitted if the temporal object has an ISO
   * chronology, or can be converted to a {@code Date}.
   * <p>
   * This method matches the signature of the functional interface {@link TemporalQuery}
   * allowing it to be used in queries via method reference, {@code Year::from}.
   *
   * @param temporal  the temporal object to convert, not null
   * @return the year, not null
   * @throws DateTimeException if unable to convert to a { @code Year}
   */
  def from(temporal: TemporalAccessor): Year = {
    if (temporal.isInstanceOf[Year]) {
       temporal.asInstanceOf[Year]
    }
    object
    try {
      if ((IsoChronology.INSTANCE == Chronology.from(temporal)) == false) {
        temporal = Date.from(temporal)
      }
       of(temporal.get(YEAR))
    }
    catch {
      case ex: DateTimeException => {
        throw new DateTimeException("Unable to obtain Year from TemporalAccessor: " + temporal.getClass, ex)
      }
    }
  }

  /**
   * Obtains an instance of {@code Year} from a text string such as {@code 2007}.
   * <p>
   * The string must represent a valid year.
   * Years outside the range 0000 to 9999 must be prefixed by the plus or minus symbol.
   *
   * @param text  the text to parse such as "2007", not null
   * @return the parsed year, not null
   * @throws DateTimeParseException if the text cannot be parsed
   */
  def parse(text: CharSequence): Year = {
     parse(text, PARSER)
  }

  /**
   * Obtains an instance of {@code Year} from a text string using a specific formatter.
   * <p>
   * The text is parsed using the formatter, returning a year.
   *
   * @param text  the text to parse, not null
   * @param formatter  the formatter to use, not null
   * @return the parsed year, not null
   * @throws DateTimeParseException if the text cannot be parsed
   */
  def parse(text: CharSequence, formatter: DateTimeFormatter): Year = {
    object
     formatter.parse(text, Year.from)
  }

  /**
   * Checks if the year is a leap year, according to the ISO proleptic
   * calendar system rules.
   * <p>
   * This method applies the current rules for leap years across the whole time-line.
   * In general, a year is a leap year if it is divisible by four without
   * remainder. However, years divisible by 100, are not leap years, with
   * the exception of years divisible by 400 which are.
   * <p>
   * For example, 1904 is a leap year it is divisible by 4.
   * 1900 was not a leap year as it is divisible by 100, however 2000 was a
   * leap year as it is divisible by 400.
   * <p>
   * The calculation is proleptic - applying the same rules into the far future and far past.
   * This is historically inaccurate, but is correct for the ISO-8601 standard.
   *
   * @param year  the year to check
   * @return true if the year is leap, false otherwise
   */
  def isLeap(year: Long): Boolean = {
     ((year & 3) == 0) && ((year % 100) != 0 || (year % 400) == 0)
  }

  private[time] def readExternal(in: DataInput): Year = {
     Year.of(in.readInt)
  }

  /**
   * The minimum supported year, '-999,999,999'.
   */
  final val MIN_VALUE: Int = -999 _999_999
  /**
   * The maximum supported year, '+999,999,999'.
   */
  final val MAX_VALUE: Int = 999 _999_999

  /**
   * Parser.
   */
  private final val PARSER: DateTimeFormatter = new DateTimeFormatterBuilder().appendValue(YEAR, 4, 10, SignStyle.EXCEEDS_PAD).toFormatter
}

final class Year extends Temporal with TemporalAdjuster with Comparable[Year]  {
  /**
   * Constructor.
   *
   * @param year  the year to represent
   */
  private def this(year: Int) {

    this.year = year
  }

  /**
   * Gets the year value.
   * <p>
   * The year returned by this method is proleptic as per {@code get(YEAR)}.
   *
   * @return the year, { @code MIN_VALUE} to { @code MAX_VALUE}
   */
  def getValue: Int = {
     year
  }

  /**
   * Checks if the specified field is supported.
   * <p>
   * This checks if this year can be queried for the specified field.
   * If false, then calling the {@link #range(TemporalField) range},
   * {@link #get(TemporalField) get} and {@link #with(TemporalField, long)}
   * methods will throw an exception.
   * <p>
   * If the field is a {@link ChronoField} then the query is implemented here.
   * The supported fields are:
   * <ul>
   * <li>{@code YEAR_OF_ERA}
   * <li>{@code YEAR}
   * <li>{@code ERA}
   * </ul>
   * All other {@code ChronoField} instances will  false.
   * <p>
   * If the field is not a {@code ChronoField}, then the result of this method
   * is obtained by invoking {@code TemporalField.isSupportedBy(TemporalAccessor)}
   * passing {@code this} as the argument.
   * Whether the field is supported is determined by the field.
   *
   * @param field  the field to check, null returns false
   * @return true if the field is supported on this year, false if not
   */
  def isSupported(field: TemporalField): Boolean = {
    if (field.isInstanceOf[ChronoField]) {
       field eq YEAR || field eq YEAR_OF_ERA || field eq ERA
    }
     field != null && field.isSupportedBy(this)
  }

  /**
   * Checks if the specified unit is supported.
   * <p>
   * This checks if the specified unit can be added to, or subtracted from, this date-time.
   * If false, then calling the {@link #plus(long, TemporalUnit)} and
   * {@link #minus(long, TemporalUnit) minus} methods will throw an exception.
   * <p>
   * If the unit is a {@link ChronoUnit} then the query is implemented here.
   * The supported units are:
   * <ul>
   * <li>{@code YEARS}
   * <li>{@code DECADES}
   * <li>{@code CENTURIES}
   * <li>{@code MILLENNIA}
   * <li>{@code ERAS}
   * </ul>
   * All other {@code ChronoUnit} instances will  false.
   * <p>
   * If the unit is not a {@code ChronoUnit}, then the result of this method
   * is obtained by invoking {@code TemporalUnit.isSupportedBy(Temporal)}
   * passing {@code this} as the argument.
   * Whether the unit is supported is determined by the unit.
   *
   * @param unit  the unit to check, null returns false
   * @return true if the unit can be added/subtracted, false if not
   */
  def isSupported(unit: TemporalUnit): Boolean = {
    if (unit.isInstanceOf[ChronoUnit]) {
       unit eq YEARS || unit eq DECADES || unit eq CENTURIES || unit eq MILLENNIA || unit eq ERAS
    }
     unit != null && unit.isSupportedBy(this)
  }

  /**
   * Gets the range of valid values for the specified field.
   * <p>
   * The range object expresses the minimum and maximum valid values for a field.
   * This year is used to enhance the accuracy of the returned range.
   * If it is not possible to  the range, because the field is not supported
   * or for some other reason, an exception is thrown.
   * <p>
   * If the field is a {@link ChronoField} then the query is implemented here.
   * The {@link #isSupported(TemporalField) supported fields} will
   * appropriate range instances.
   * All other {@code ChronoField} instances will throw an {@code UnsupportedTemporalTypeException}.
   * <p>
   * If the field is not a {@code ChronoField}, then the result of this method
   * is obtained by invoking {@code TemporalField.rangeRefinedBy(TemporalAccessor)}
   * passing {@code this} as the argument.
   * Whether the range can be obtained is determined by the field.
   *
   * @param field  the field to query the range for, not null
   * @return the range of valid values for the field, not null
   * @throws DateTimeException if the range for the field cannot be obtained
   * @throws UnsupportedTemporalTypeException if the field is not supported
   */
  override def range(field: TemporalField): ValueRange = {
    if (field eq YEAR_OF_ERA) {
       (if (year <= 0) ValueRange.of(1, MAX_VALUE + 1) else ValueRange.of(1, MAX_VALUE))
    }
     Temporal.super.range(field)
  }

  /**
   * Gets the value of the specified field from this year as an {@code int}.
   * <p>
   * This queries this year for the value for the specified field.
   * The returned value will always be within the valid range of values for the field.
   * If it is not possible to  the value, because the field is not supported
   * or for some other reason, an exception is thrown.
   * <p>
   * If the field is a {@link ChronoField} then the query is implemented here.
   * The {@link #isSupported(TemporalField) supported fields} will  valid
   * values based on this year.
   * All other {@code ChronoField} instances will throw an {@code UnsupportedTemporalTypeException}.
   * <p>
   * If the field is not a {@code ChronoField}, then the result of this method
   * is obtained by invoking {@code TemporalField.getFrom(TemporalAccessor)}
   * passing {@code this} as the argument. Whether the value can be obtained,
   * and what the value represents, is determined by the field.
   *
   * @param field  the field to get, not null
   * @return the value for the field
   * @throws DateTimeException if a value for the field cannot be obtained or
   *                           the value is outside the range of valid values for the field
   * @throws UnsupportedTemporalTypeException if the field is not supported or
   *                                          the range of values exceeds an { @code int}
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def get(field: TemporalField): Int = {
     range(field).checkValidIntValue(getLong(field), field)
  }

  /**
   * Gets the value of the specified field from this year as a {@code long}.
   * <p>
   * This queries this year for the value for the specified field.
   * If it is not possible to  the value, because the field is not supported
   * or for some other reason, an exception is thrown.
   * <p>
   * If the field is a {@link ChronoField} then the query is implemented here.
   * The {@link #isSupported(TemporalField) supported fields} will  valid
   * values based on this year.
   * All other {@code ChronoField} instances will throw an {@code UnsupportedTemporalTypeException}.
   * <p>
   * If the field is not a {@code ChronoField}, then the result of this method
   * is obtained by invoking {@code TemporalField.getFrom(TemporalAccessor)}
   * passing {@code this} as the argument. Whether the value can be obtained,
   * and what the value represents, is determined by the field.
   *
   * @param field  the field to get, not null
   * @return the value for the field
   * @throws DateTimeException if a value for the field cannot be obtained
   * @throws UnsupportedTemporalTypeException if the field is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  def getLong(field: TemporalField): Long = {
    if (field.isInstanceOf[ChronoField]) {
      field.asInstanceOf[ChronoField] match {
        case YEAR_OF_ERA =>
           (if (year < 1) 1 - year else year)
        case YEAR =>
           year
        case ERA =>
           (if (year < 1) 0 else 1)
      }
      throw new UnsupportedTemporalTypeException("Unsupported field: " + field)
    }
     field.getFrom(this)
  }

  /**
   * Checks if the year is a leap year, according to the ISO proleptic
   * calendar system rules.
   * <p>
   * This method applies the current rules for leap years across the whole time-line.
   * In general, a year is a leap year if it is divisible by four without
   * remainder. However, years divisible by 100, are not leap years, with
   * the exception of years divisible by 400 which are.
   * <p>
   * For example, 1904 is a leap year it is divisible by 4.
   * 1900 was not a leap year as it is divisible by 100, however 2000 was a
   * leap year as it is divisible by 400.
   * <p>
   * The calculation is proleptic - applying the same rules into the far future and far past.
   * This is historically inaccurate, but is correct for the ISO-8601 standard.
   *
   * @return true if the year is leap, false otherwise
   */
  def isLeap: Boolean = {
     Year.isLeap(year)
  }

  /**
   * Checks if the month-day is valid for this year.
   * <p>
   * This method checks whether this year and the input month and day form
   * a valid date.
   *
   * @param monthDay  the month-day to validate, null returns false
   * @return true if the month and day are valid for this year
   */
  def isValidMonthDay(monthDay: MonthDay): Boolean = {
     monthDay != null && monthDay.isValidYear(year)
  }

  /**
   * Gets the length of this year in days.
   *
   * @return the length of this year in days, 365 or 366
   */
  def length: Int = {
     if (isLeap) 366 else 365
  }

  /**
   * Returns an adjusted copy of this year.
   * <p>
   * This returns a {@code Year}, based on this one, with the year adjusted.
   * The adjustment takes place using the specified adjuster strategy object.
   * Read the documentation of the adjuster to understand what adjustment will be made.
   * <p>
   * The result of this method is obtained by invoking the
   * {@link TemporalAdjuster#adjustInto(Temporal)} method on the
   * specified adjuster passing {@code this} as the argument.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param adjuster the adjuster to use, not null
   * @return a { @code Year} based on { @code this} with the adjustment made, not null
   * @throws DateTimeException if the adjustment cannot be made
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def `with`(adjuster: TemporalAdjuster): Year = {
     adjuster.adjustInto(this).asInstanceOf[Year]
  }

  /**
   * Returns a copy of this year with the specified field set to a new value.
   * <p>
   * This returns a {@code Year}, based on this one, with the value
   * for the specified field changed.
   * If it is not possible to set the value, because the field is not supported or for
   * some other reason, an exception is thrown.
   * <p>
   * If the field is a {@link ChronoField} then the adjustment is implemented here.
   * The supported fields behave as follows:
   * <ul>
   * <li>{@code YEAR_OF_ERA} -
   * Returns a {@code Year} with the specified year-of-era
   * The era will be unchanged.
   * <li>{@code YEAR} -
   * Returns a {@code Year} with the specified year.
   * This completely replaces the date and is equivalent to {@link #of(int)}.
   * <li>{@code ERA} -
   * Returns a {@code Year} with the specified era.
   * The year-of-era will be unchanged.
   * </ul>
   * <p>
   * In all cases, if the new value is outside the valid range of values for the field
   * then a {@code DateTimeException} will be thrown.
   * <p>
   * All other {@code ChronoField} instances will throw an {@code UnsupportedTemporalTypeException}.
   * <p>
   * If the field is not a {@code ChronoField}, then the result of this method
   * is obtained by invoking {@code TemporalField.adjustInto(Temporal, long)}
   * passing {@code this} as the argument. In this case, the field determines
   * whether and how to adjust the instant.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param field  the field to set in the result, not null
   * @param newValue  the new value of the field in the result
   * @return a { @code Year} based on { @code this} with the specified field set, not null
   * @throws DateTimeException if the field cannot be set
   * @throws UnsupportedTemporalTypeException if the field is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  def `with`(field: TemporalField, newValue: Long): Year = {
    if (field.isInstanceOf[ChronoField]) {
      val f: ChronoField = field.asInstanceOf[ChronoField]
      f.checkValidValue(newValue)
      f match {
        case YEAR_OF_ERA =>
           Year.of((if (year < 1) 1 - newValue else newValue).asInstanceOf[Int])
        case YEAR =>
           Year.of(newValue.asInstanceOf[Int])
        case ERA =>
           (if (getLong(ERA) == newValue) this else Year.of(1 - year))
      }
      throw new UnsupportedTemporalTypeException("Unsupported field: " + field)
    }
     field.adjustInto(this, newValue)
  }

  /**
   * Returns a copy of this year with the specified amount added.
   * <p>
   * This returns a {@code Year}, based on this one, with the specified amount added.
   * The amount is typically {@link Period} but may be any other type implementing
   * the {@link TemporalAmount} interface.
   * <p>
   * The calculation is delegated to the amount object by calling
   * {@link TemporalAmount#addTo(Temporal)}. The amount implementation is free
   * to implement the addition in any way it wishes, however it typically
   * calls back to {@link #plus(long, TemporalUnit)}. Consult the documentation
   * of the amount implementation to determine if it can be successfully added.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param amountToAdd  the amount to add, not null
   * @return a { @code Year} based on this year with the addition made, not null
   * @throws DateTimeException if the addition cannot be made
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def plus(amountToAdd: TemporalAmount): Year = {
     amountToAdd.addTo(this).asInstanceOf[Year]
  }

  /**
   * Returns a copy of this year with the specified amount added.
   * <p>
   * This returns a {@code Year}, based on this one, with the amount
   * in terms of the unit added. If it is not possible to add the amount, because the
   * unit is not supported or for some other reason, an exception is thrown.
   * <p>
   * If the field is a {@link ChronoUnit} then the addition is implemented here.
   * The supported fields behave as follows:
   * <ul>
   * <li>{@code YEARS} -
   * Returns a {@code Year} with the specified number of years added.
   * This is equivalent to {@link #plusYears(long)}.
   * <li>{@code DECADES} -
   * Returns a {@code Year} with the specified number of decades added.
   * This is equivalent to calling {@link #plusYears(long)} with the amount
   * multiplied by 10.
   * <li>{@code CENTURIES} -
   * Returns a {@code Year} with the specified number of centuries added.
   * This is equivalent to calling {@link #plusYears(long)} with the amount
   * multiplied by 100.
   * <li>{@code MILLENNIA} -
   * Returns a {@code Year} with the specified number of millennia added.
   * This is equivalent to calling {@link #plusYears(long)} with the amount
   * multiplied by 1,000.
   * <li>{@code ERAS} -
   * Returns a {@code Year} with the specified number of eras added.
   * Only two eras are supported so the amount must be one, zero or minus one.
   * If the amount is non-zero then the year is changed such that the year-of-era
   * is unchanged.
   * </ul>
   * <p>
   * All other {@code ChronoUnit} instances will throw an {@code UnsupportedTemporalTypeException}.
   * <p>
   * If the field is not a {@code ChronoUnit}, then the result of this method
   * is obtained by invoking {@code TemporalUnit.addTo(Temporal, long)}
   * passing {@code this} as the argument. In this case, the unit determines
   * whether and how to perform the addition.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param amountToAdd  the amount of the unit to add to the result, may be negative
   * @param unit  the unit of the amount to add, not null
   * @return a { @code Year} based on this year with the specified amount added, not null
   * @throws DateTimeException if the addition cannot be made
   * @throws UnsupportedTemporalTypeException if the unit is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  def plus(amountToAdd: Long, unit: TemporalUnit): Year = {
    if (unit.isInstanceOf[ChronoUnit]) {
      unit.asInstanceOf[ChronoUnit] match {
        case YEARS =>
           plusYears(amountToAdd)
        case DECADES =>
           plusYears(Math.multiplyExact(amountToAdd, 10))
        case CENTURIES =>
           plusYears(Math.multiplyExact(amountToAdd, 100))
        case MILLENNIA =>
           plusYears(Math.multiplyExact(amountToAdd, 1000))
        case ERAS =>
           `with`(ERA, Math.addExact(getLong(ERA), amountToAdd))
      }
      throw new UnsupportedTemporalTypeException("Unsupported unit: " + unit)
    }
     unit.addTo(this, amountToAdd)
  }

  /**
   * Returns a copy of this year with the specified number of years added.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param yearsToAdd  the years to add, may be negative
   * @return a { @code Year} based on this year with the period added, not null
   * @throws DateTimeException if the result exceeds the supported year range
   */
  def plusYears(yearsToAdd: Long): Year = {
    if (yearsToAdd == 0) {
       this
    }
     of(YEAR.checkValidIntValue(year + yearsToAdd))
  }

  /**
   * Returns a copy of this year with the specified amount subtracted.
   * <p>
   * This returns a {@code Year}, based on this one, with the specified amount subtracted.
   * The amount is typically {@link Period} but may be any other type implementing
   * the {@link TemporalAmount} interface.
   * <p>
   * The calculation is delegated to the amount object by calling
   * {@link TemporalAmount#subtractFrom(Temporal)}. The amount implementation is free
   * to implement the subtraction in any way it wishes, however it typically
   * calls back to {@link #minus(long, TemporalUnit)}. Consult the documentation
   * of the amount implementation to determine if it can be successfully subtracted.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param amountToSubtract  the amount to subtract, not null
   * @return a { @code Year} based on this year with the subtraction made, not null
   * @throws DateTimeException if the subtraction cannot be made
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def minus(amountToSubtract: TemporalAmount): Year = {
     amountToSubtract.subtractFrom(this).asInstanceOf[Year]
  }

  /**
   * Returns a copy of this year with the specified amount subtracted.
   * <p>
   * This returns a {@code Year}, based on this one, with the amount
   * in terms of the unit subtracted. If it is not possible to subtract the amount,
   * because the unit is not supported or for some other reason, an exception is thrown.
   * <p>
   * This method is equivalent to {@link #plus(long, TemporalUnit)} with the amount negated.
   * See that method for a full description of how addition, and thus subtraction, works.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param amountToSubtract  the amount of the unit to subtract from the result, may be negative
   * @param unit  the unit of the amount to subtract, not null
   * @return a { @code Year} based on this year with the specified amount subtracted, not null
   * @throws DateTimeException if the subtraction cannot be made
   * @throws UnsupportedTemporalTypeException if the unit is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def minus(amountToSubtract: Long, unit: TemporalUnit): Year = {
     (if (amountToSubtract == Long.MIN_VALUE) plus(Long.MAX_VALUE, unit).plus(1, unit) else plus(-amountToSubtract, unit))
  }

  /**
   * Returns a copy of this year with the specified number of years subtracted.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param yearsToSubtract  the years to subtract, may be negative
   * @return a { @code Year} based on this year with the period subtracted, not null
   * @throws DateTimeException if the result exceeds the supported year range
   */
  def minusYears(yearsToSubtract: Long): Year = {
     (if (yearsToSubtract == Long.MIN_VALUE) plusYears(Long.MAX_VALUE).plusYears(1) else plusYears(-yearsToSubtract))
  }

  /**
   * Queries this year using the specified query.
   * <p>
   * This queries this year using the specified query strategy object.
   * The {@code TemporalQuery} object defines the logic to be used to
   * obtain the result. Read the documentation of the query to understand
   * what the result of this method will be.
   * <p>
   * The result of this method is obtained by invoking the
   * {@link TemporalQuery#queryFrom(TemporalAccessor)} method on the
   * specified query passing {@code this} as the argument.
   *
   * @param <R> the type of the result
   * @param query  the query to invoke, not null
   * @return the query result, null may be returned (defined by the query)
   * @throws DateTimeException if unable to query (defined by the query)
   * @throws ArithmeticException if numeric overflow occurs (defined by the query)
   */
  override def query(query: TemporalQuery[R]): R = {
    if (query eq TemporalQuery.chronology) {
       IsoChronology.INSTANCE.asInstanceOf[R]
    }
    else if (query eq TemporalQuery.precision) {
       YEARS.asInstanceOf[R]
    }
     Temporal.super.query(query)
  }

  /**
   * Adjusts the specified temporal object to have this year.
   * <p>
   * This returns a temporal object of the same observable type as the input
   * with the year changed to be the same as this.
   * <p>
   * The adjustment is equivalent to using {@link Temporal#with(TemporalField, long)}
   * passing {@link ChronoField#YEAR} as the field.
   * If the specified temporal object does not use the ISO calendar system then
   * a {@code DateTimeException} is thrown.
   * <p>
   * In most cases, it is clearer to reverse the calling pattern by using
   * {@link Temporal#with(TemporalAdjuster)}:
   * {{{
   * // these two lines are equivalent, but the second approach is recommended
   * temporal = thisYear.adjustInto(temporal);
   * temporal = temporal.with(thisYear);
   * }}}
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param temporal  the target object to be adjusted, not null
   * @return the adjusted object, not null
   * @throws DateTimeException if unable to make the adjustment
   * @throws ArithmeticException if numeric overflow occurs
   */
  def adjustInto(temporal: Temporal): Temporal = {
    if ((Chronology.from(temporal) == IsoChronology.INSTANCE) == false) {
      throw new DateTimeException("Adjustment only supported on ISO date-time")
    }
     temporal.`with`(YEAR, year)
  }

  /**
   * Calculates the amount of time until another year in terms of the specified unit.
   * <p>
   * This calculates the amount of time between two {@code Year}
   * objects in terms of a single {@code TemporalUnit}.
   * The start and end points are {@code this} and the specified year.
   * The result will be negative if the end is before the start.
   * The {@code Temporal} passed to this method is converted to a
   * {@code Year} using {@link #from(TemporalAccessor)}.
   * For example, the period in decades between two year can be calculated
   * using {@code startYear.until(endYear, DECADES)}.
   * <p>
   * The calculation returns a whole number, representing the number of
   * complete units between the two years.
   * For example, the period in decades between 2012 and 2031
   * will only be one decade as it is one year short of two decades.
   * <p>
   * There are two equivalent ways of using this method.
   * The first is to invoke this method.
   * The second is to use {@link TemporalUnit#between(Temporal, Temporal)}:
   * {{{
   * // these two lines are equivalent
   * amount = start.until(end, YEARS);
   * amount = YEARS.between(start, end);
   * }}}
   * The choice should be made based on which makes the code more readable.
   * <p>
   * The calculation is implemented in this method for {@link ChronoUnit}.
   * The units {@code YEARS}, {@code DECADES}, {@code CENTURIES},
   * {@code MILLENNIA} and {@code ERAS} are supported.
   * Other {@code ChronoUnit} values will throw an exception.
   * <p>
   * If the unit is not a {@code ChronoUnit}, then the result of this method
   * is obtained by invoking {@code TemporalUnit.between(Temporal, Temporal)}
   * passing {@code this} as the first argument and the converted input temporal
   * as the second argument.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param endExclusive  the end date, which is converted to a { @code Year}, not null
   * @param unit  the unit to measure the amount in, not null
   * @return the amount of time between this year and the end year
   * @throws DateTimeException if the amount cannot be calculated, or the end
   *                           temporal cannot be converted to a { @code Year}
   * @throws UnsupportedTemporalTypeException if the unit is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  def until(endExclusive: Temporal, unit: TemporalUnit): Long = {
    val end: Year = Year.from(endExclusive)
    if (unit.isInstanceOf[ChronoUnit]) {
      val yearsUntil: Long = (end.year.asInstanceOf[Long]) - year
      unit.asInstanceOf[ChronoUnit] match {
        case YEARS =>
           yearsUntil
        case DECADES =>
           yearsUntil / 10
        case CENTURIES =>
           yearsUntil / 100
        case MILLENNIA =>
           yearsUntil / 1000
        case ERAS =>
           end.getLong(ERA) - getLong(ERA)
      }
      throw new UnsupportedTemporalTypeException("Unsupported unit: " + unit)
    }
     unit.between(this, end)
  }

  /**
   * Formats this year using the specified formatter.
   * <p>
   * This year will be passed to the formatter to produce a string.
   *
   * @param formatter  the formatter to use, not null
   * @return the formatted year string, not null
   * @throws DateTimeException if an error occurs during printing
   */
  def format(formatter: DateTimeFormatter): String = {
    object
     formatter.format(this)
  }

  /**
   * Combines this year with a day-of-year to create a {@code Date}.
   * <p>
   * This returns a {@code Date} formed from this year and the specified day-of-year.
   * <p>
   * The day-of-year value 366 is only valid in a leap year.
   *
   * @param dayOfYear  the day-of-year to use, not null
   * @return the local date formed from this year and the specified date of year, not null
   * @throws DateTimeException if the day of year is zero or less, 366 or greater or equal
   *                           to 366 and this is not a leap year
   */
  def atDay(dayOfYear: Int): Date = {
     Date.ofYearDay(year, dayOfYear)
  }

  /**
   * Combines this year with a month to create a {@code YearMonth}.
   * <p>
   * This returns a {@code YearMonth} formed from this year and the specified month.
   * All possible combinations of year and month are valid.
   * <p>
   * This method can be used as part of a chain to produce a date:
   * {{{
   * Date date = year.atMonth(month).atDay(day);
   * }}}
   *
   * @param month  the month-of-year to use, not null
   * @return the year-month formed from this year and the specified month, not null
   */
  def atMonth(month: Month): YearMonth = {
     YearMonth.of(year, month)
  }

  /**
   * Combines this year with a month to create a {@code YearMonth}.
   * <p>
   * This returns a {@code YearMonth} formed from this year and the specified month.
   * All possible combinations of year and month are valid.
   * <p>
   * This method can be used as part of a chain to produce a date:
   * {{{
   * Date date = year.atMonth(month).atDay(day);
   * }}}
   *
   * @param month  the month-of-year to use, from 1 (January) to 12 (December)
   * @return the year-month formed from this year and the specified month, not null
   * @throws DateTimeException if the month is invalid
   */
  def atMonth(month: Int): YearMonth = {
     YearMonth.of(year, month)
  }

  /**
   * Combines this year with a month-day to create a {@code Date}.
   * <p>
   * This returns a {@code Date} formed from this year and the specified month-day.
   * <p>
   * A month-day of February 29th will be adjusted to February 28th in the resulting
   * date if the year is not a leap year.
   *
   * @param monthDay  the month-day to use, not null
   * @return the local date formed from this year and the specified month-day, not null
   */
  def atMonthDay(monthDay: MonthDay): Date = {
     monthDay.atYear(year)
  }

  /**
   * Compares this year to another year.
   * <p>
   * The comparison is based on the value of the year.
   * It is "consistent with equals", as defined by {@link Comparable}.
   *
   * @param other  the other year to compare to, not null
   * @return the comparator value, negative if less, positive if greater
   */
  def compareTo(other: Year): Int = {
     year - other.year
  }

  /**
   * Is this year after the specified year.
   *
   * @param other  the other year to compare to, not null
   * @return true if this is after the specified year
   */
  def isAfter(other: Year): Boolean = {
     year > other.year
  }

  /**
   * Is this year before the specified year.
   *
   * @param other  the other year to compare to, not null
   * @return true if this point is before the specified year
   */
  def isBefore(other: Year): Boolean = {
     year < other.year
  }

  /**
   * Checks if this year is equal to another year.
   * <p>
   * The comparison is based on the time-line position of the years.
   *
   * @param obj  the object to check, null returns false
   * @return true if this is equal to the other year
   */
  override def equals(obj: AnyRef): Boolean = {
    if (this eq obj) {
       true
    }
    if (obj.isInstanceOf[Year]) {
       year == (obj.asInstanceOf[Year]).year
    }
     false
  }

  /**
   * A hash code for this year.
   *
   * @return a suitable hash code
   */
  override def hashCode: Int = {
     year
  }

  /**
   * Outputs this year as a {@code String}.
   *
   * @return a string representation of this year, not null
   */
  override def toString: String = {
     Integer.toString(year)
  }

  /**
   * Writes the object using a
   * <a href="../../../serialized-form.html#java.time.temporal.Ser">dedicated serialized form</a>.
   * {{{
   * out.writeByte(11);  // identifies this as a Year
   * out.writeInt(year);
   * }}}
   *
   * @return the instance of { @code Ser}, not null
   */
  private def writeReplace: AnyRef = {
     new Ser(Ser.YEAR_TYPE, this)
  }

  /**
   * Defend against malicious streams.
   * @return never
   * @throws InvalidObjectException always
   */
  private def readResolve: AnyRef = {
    throw new InvalidObjectException("Deserialization via serialization delegate")
  }

  private[time] def writeExternal(out: DataOutput) {
    out.writeInt(year)
  }

  /**
   * The year being represented.
   */
  private final val year: Int = 0
}

/**
 * A year-month in the ISO-8601 calendar system, such as {@code 2007-12}.
 * <p>
 * {@code YearMonth} is an immutable date-time object that represents the combination
 * of a year and month. Any field that can be derived from a year and month, such as
 * quarter-of-year, can be obtained.
 * <p>
 * This class does not store or represent a day, time or time-zone.
 * For example, the value "October 2007" can be stored in a {@code YearMonth}.
 * <p>
 * The ISO-8601 calendar system is the modern civil calendar system used today
 * in most of the world. It is equivalent to the proleptic Gregorian calendar
 * system, in which today's rules for leap years are applied for all time.
 * For most applications written today, the ISO-8601 rules are entirely suitable.
 * However, any application that makes use of historical dates, and requires them
 * to be accurate will find the ISO-8601 approach unsuitable.
 *
 * @implSpec
 * This class is immutable and thread-safe.
 *
 * @since 1.8
 */
object YearMonth {
  /**
   * Obtains the current year-month from the system clock in the default time-zone.
   * <p>
   * This will query the {@link java.time.Clock#systemDefaultZone() system clock} in the default
   * time-zone to obtain the current year-month.
   * The zone and offset will be set based on the time-zone in the clock.
   * <p>
   * Using this method will prevent the ability to use an alternate clock for testing
   * because the clock is hard-coded.
   *
   * @return the current year-month using the system clock and default time-zone, not null
   */
  def now: YearMonth = {
     now(Clock.systemDefaultZone)
  }

  /**
   * Obtains the current year-month from the system clock in the specified time-zone.
   * <p>
   * This will query the {@link Clock#system(java.time.ZoneId) system clock} to obtain the current year-month.
   * Specifying the time-zone avoids dependence on the default time-zone.
   * <p>
   * Using this method will prevent the ability to use an alternate clock for testing
   * because the clock is hard-coded.
   *
   * @param zone  the zone ID to use, not null
   * @return the current year-month using the system clock, not null
   */
  def now(zone: ZoneId): YearMonth = {
     now(Clock.system(zone))
  }

  /**
   * Obtains the current year-month from the specified clock.
   * <p>
   * This will query the specified clock to obtain the current year-month.
   * Using this method allows the use of an alternate clock for testing.
   * The alternate clock may be introduced using {@link Clock dependency injection}.
   *
   * @param clock  the clock to use, not null
   * @return the current year-month, not null
   */
  def now(clock: Clock): YearMonth = {
    val now: Date = Date.now(clock)
     YearMonth.of(now.getYear, now.getMonth)
  }

  /**
   * Obtains an instance of {@code YearMonth} from a year and month.
   *
   * @param year  the year to represent, from MIN_YEAR to MAX_YEAR
   * @param month  the month-of-year to represent, not null
   * @return the year-month, not null
   * @throws DateTimeException if the year value is invalid
   */
  def of(year: Int, month: Month): YearMonth = {
    object
     of(year, month.getValue)
  }

  /**
   * Obtains an instance of {@code YearMonth} from a year and month.
   *
   * @param year  the year to represent, from MIN_YEAR to MAX_YEAR
   * @param month  the month-of-year to represent, from 1 (January) to 12 (December)
   * @return the year-month, not null
   * @throws DateTimeException if either field value is invalid
   */
  def of(year: Int, month: Int): YearMonth = {
    YEAR.checkValidValue(year)
    MONTH_OF_YEAR.checkValidValue(month)
     new YearMonth(year, month)
  }

  /**
   * Obtains an instance of {@code YearMonth} from a temporal object.
   * <p>
   * This obtains a year-month based on the specified temporal.
   * A {@code TemporalAccessor} represents an arbitrary set of date and time information,
   * which this factory converts to an instance of {@code YearMonth}.
   * <p>
   * The conversion extracts the {@link ChronoField#YEAR YEAR} and
   * {@link ChronoField#MONTH_OF_YEAR MONTH_OF_YEAR} fields.
   * The extraction is only permitted if the temporal object has an ISO
   * chronology, or can be converted to a {@code Date}.
   * <p>
   * This method matches the signature of the functional interface {@link TemporalQuery}
   * allowing it to be used in queries via method reference, {@code YearMonth::from}.
   *
   * @param temporal  the temporal object to convert, not null
   * @return the year-month, not null
   * @throws DateTimeException if unable to convert to a { @code YearMonth}
   */
  def from(temporal: TemporalAccessor): YearMonth = {
    if (temporal.isInstanceOf[YearMonth]) {
       temporal.asInstanceOf[YearMonth]
    }
    object
    try {
      if ((IsoChronology.INSTANCE == Chronology.from(temporal)) == false) {
        temporal = Date.from(temporal)
      }
       of(temporal.get(YEAR), temporal.get(MONTH_OF_YEAR))
    }
    catch {
      case ex: DateTimeException => {
        throw new DateTimeException("Unable to obtain YearMonth from TemporalAccessor: " + temporal.getClass, ex)
      }
    }
  }

  /**
   * Obtains an instance of {@code YearMonth} from a text string such as {@code 2007-12}.
   * <p>
   * The string must represent a valid year-month.
   * The format must be {@code uuuu-MM}.
   * Years outside the range 0000 to 9999 must be prefixed by the plus or minus symbol.
   *
   * @param text  the text to parse such as "2007-12", not null
   * @return the parsed year-month, not null
   * @throws DateTimeParseException if the text cannot be parsed
   */
  def parse(text: CharSequence): YearMonth = {
     parse(text, PARSER)
  }

  /**
   * Obtains an instance of {@code YearMonth} from a text string using a specific formatter.
   * <p>
   * The text is parsed using the formatter, returning a year-month.
   *
   * @param text  the text to parse, not null
   * @param formatter  the formatter to use, not null
   * @return the parsed year-month, not null
   * @throws DateTimeParseException if the text cannot be parsed
   */
  def parse(text: CharSequence, formatter: DateTimeFormatter): YearMonth = {
    object
     formatter.parse(text, YearMonth.from)
  }

  private[time] def readExternal(in: DataInput): YearMonth = {
    val year: Int = in.readInt
    val month: Byte = in.readByte
     YearMonth.of(year, month)
  }

  /**
   * Serialization version.
   */
  private final val serialVersionUID: Long = 4183400860270640070L
  /**
   * Parser.
   */
  private final val PARSER: DateTimeFormatter = new DateTimeFormatterBuilder().appendValue(YEAR, 4, 10, SignStyle.EXCEEDS_PAD).appendLiteral('-').appendValue(MONTH_OF_YEAR, 2).toFormatter
}

final class YearMonth extends Temporal with TemporalAdjuster with Comparable[YearMonth]  {
  /**
   * Constructor.
   *
   * @param year  the year to represent, validated from MIN_YEAR to MAX_YEAR
   * @param month  the month-of-year to represent, validated from 1 (January) to 12 (December)
   */
  private def this(year: Int, month: Int) {

    this.year = year
    this.month = month
  }

  /**
   * Returns a copy of this year-month with the new year and month, checking
   * to see if a new object is in fact required.
   *
   * @param newYear  the year to represent, validated from MIN_YEAR to MAX_YEAR
   * @param newMonth  the month-of-year to represent, validated not null
   * @return the year-month, not null
   */
  private def `with`(newYear: Int, newMonth: Int): YearMonth = {
    if (year == newYear && month == newMonth) {
       this
    }
     new YearMonth(newYear, newMonth)
  }

  /**
   * Checks if the specified field is supported.
   * <p>
   * This checks if this year-month can be queried for the specified field.
   * If false, then calling the {@link #range(TemporalField) range},
   * {@link #get(TemporalField) get} and {@link #with(TemporalField, long)}
   * methods will throw an exception.
   * <p>
   * If the field is a {@link ChronoField} then the query is implemented here.
   * The supported fields are:
   * <ul>
   * <li>{@code MONTH_OF_YEAR}
   * <li>{@code PROLEPTIC_MONTH}
   * <li>{@code YEAR_OF_ERA}
   * <li>{@code YEAR}
   * <li>{@code ERA}
   * </ul>
   * All other {@code ChronoField} instances will  false.
   * <p>
   * If the field is not a {@code ChronoField}, then the result of this method
   * is obtained by invoking {@code TemporalField.isSupportedBy(TemporalAccessor)}
   * passing {@code this} as the argument.
   * Whether the field is supported is determined by the field.
   *
   * @param field  the field to check, null returns false
   * @return true if the field is supported on this year-month, false if not
   */
  def isSupported(field: TemporalField): Boolean = {
    if (field.isInstanceOf[ChronoField]) {
       field eq YEAR || field eq MONTH_OF_YEAR || field eq PROLEPTIC_MONTH || field eq YEAR_OF_ERA || field eq ERA
    }
     field != null && field.isSupportedBy(this)
  }

  /**
   * Checks if the specified unit is supported.
   * <p>
   * This checks if the specified unit can be added to, or subtracted from, this date-time.
   * If false, then calling the {@link #plus(long, TemporalUnit)} and
   * {@link #minus(long, TemporalUnit) minus} methods will throw an exception.
   * <p>
   * If the unit is a {@link ChronoUnit} then the query is implemented here.
   * The supported units are:
   * <ul>
   * <li>{@code MONTHS}
   * <li>{@code YEARS}
   * <li>{@code DECADES}
   * <li>{@code CENTURIES}
   * <li>{@code MILLENNIA}
   * <li>{@code ERAS}
   * </ul>
   * All other {@code ChronoUnit} instances will  false.
   * <p>
   * If the unit is not a {@code ChronoUnit}, then the result of this method
   * is obtained by invoking {@code TemporalUnit.isSupportedBy(Temporal)}
   * passing {@code this} as the argument.
   * Whether the unit is supported is determined by the unit.
   *
   * @param unit  the unit to check, null returns false
   * @return true if the unit can be added/subtracted, false if not
   */
  def isSupported(unit: TemporalUnit): Boolean = {
    if (unit.isInstanceOf[ChronoUnit]) {
       unit eq MONTHS || unit eq YEARS || unit eq DECADES || unit eq CENTURIES || unit eq MILLENNIA || unit eq ERAS
    }
     unit != null && unit.isSupportedBy(this)
  }

  /**
   * Gets the range of valid values for the specified field.
   * <p>
   * The range object expresses the minimum and maximum valid values for a field.
   * This year-month is used to enhance the accuracy of the returned range.
   * If it is not possible to  the range, because the field is not supported
   * or for some other reason, an exception is thrown.
   * <p>
   * If the field is a {@link ChronoField} then the query is implemented here.
   * The {@link #isSupported(TemporalField) supported fields} will
   * appropriate range instances.
   * All other {@code ChronoField} instances will throw an {@code UnsupportedTemporalTypeException}.
   * <p>
   * If the field is not a {@code ChronoField}, then the result of this method
   * is obtained by invoking {@code TemporalField.rangeRefinedBy(TemporalAccessor)}
   * passing {@code this} as the argument.
   * Whether the range can be obtained is determined by the field.
   *
   * @param field  the field to query the range for, not null
   * @return the range of valid values for the field, not null
   * @throws DateTimeException if the range for the field cannot be obtained
   * @throws UnsupportedTemporalTypeException if the field is not supported
   */
  override def range(field: TemporalField): ValueRange = {
    if (field eq YEAR_OF_ERA) {
       (if (getYear <= 0) ValueRange.of(1, Year.MAX_VALUE + 1) else ValueRange.of(1, Year.MAX_VALUE))
    }
     Temporal.super.range(field)
  }

  /**
   * Gets the value of the specified field from this year-month as an {@code int}.
   * <p>
   * This queries this year-month for the value for the specified field.
   * The returned value will always be within the valid range of values for the field.
   * If it is not possible to  the value, because the field is not supported
   * or for some other reason, an exception is thrown.
   * <p>
   * If the field is a {@link ChronoField} then the query is implemented here.
   * The {@link #isSupported(TemporalField) supported fields} will  valid
   * values based on this year-month, except {@code PROLEPTIC_MONTH} which is too
   * large to fit in an {@code int} and throw a {@code DateTimeException}.
   * All other {@code ChronoField} instances will throw an {@code UnsupportedTemporalTypeException}.
   * <p>
   * If the field is not a {@code ChronoField}, then the result of this method
   * is obtained by invoking {@code TemporalField.getFrom(TemporalAccessor)}
   * passing {@code this} as the argument. Whether the value can be obtained,
   * and what the value represents, is determined by the field.
   *
   * @param field  the field to get, not null
   * @return the value for the field
   * @throws DateTimeException if a value for the field cannot be obtained or
   *                           the value is outside the range of valid values for the field
   * @throws UnsupportedTemporalTypeException if the field is not supported or
   *                                          the range of values exceeds an { @code int}
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def get(field: TemporalField): Int = {
     range(field).checkValidIntValue(getLong(field), field)
  }

  /**
   * Gets the value of the specified field from this year-month as a {@code long}.
   * <p>
   * This queries this year-month for the value for the specified field.
   * If it is not possible to  the value, because the field is not supported
   * or for some other reason, an exception is thrown.
   * <p>
   * If the field is a {@link ChronoField} then the query is implemented here.
   * The {@link #isSupported(TemporalField) supported fields} will  valid
   * values based on this year-month.
   * All other {@code ChronoField} instances will throw an {@code UnsupportedTemporalTypeException}.
   * <p>
   * If the field is not a {@code ChronoField}, then the result of this method
   * is obtained by invoking {@code TemporalField.getFrom(TemporalAccessor)}
   * passing {@code this} as the argument. Whether the value can be obtained,
   * and what the value represents, is determined by the field.
   *
   * @param field  the field to get, not null
   * @return the value for the field
   * @throws DateTimeException if a value for the field cannot be obtained
   * @throws UnsupportedTemporalTypeException if the field is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  def getLong(field: TemporalField): Long = {
    if (field.isInstanceOf[ChronoField]) {
      field.asInstanceOf[ChronoField] match {
        case MONTH_OF_YEAR =>
           month
        case PROLEPTIC_MONTH =>
           getProlepticMonth
        case YEAR_OF_ERA =>
           (if (year < 1) 1 - year else year)
        case YEAR =>
           year
        case ERA =>
           (if (year < 1) 0 else 1)
      }
      throw new UnsupportedTemporalTypeException("Unsupported field: " + field)
    }
     field.getFrom(this)
  }

  private def getProlepticMonth: Long = {
     (year * 12L + month - 1)
  }

  /**
   * Gets the year field.
   * <p>
   * This method returns the primitive {@code int} value for the year.
   * <p>
   * The year returned by this method is proleptic as per {@code get(YEAR)}.
   *
   * @return the year, from MIN_YEAR to MAX_YEAR
   */
  def getYear: Int = {
     year
  }

  /**
   * Gets the month-of-year field from 1 to 12.
   * <p>
   * This method returns the month as an {@code int} from 1 to 12.
   * Application code is frequently clearer if the enum {@link Month}
   * is used by calling {@link #getMonth()}.
   *
   * @return the month-of-year, from 1 to 12
   * @see #getMonth()
   */
  def getMonthValue: Int = {
     month
  }

  /**
   * Gets the month-of-year field using the {@code Month} enum.
   * <p>
   * This method returns the enum {@link Month} for the month.
   * This avoids confusion as to what {@code int} values mean.
   * If you need access to the primitive {@code int} value then the enum
   * provides the {@link Month#getValue() int value}.
   *
   * @return the month-of-year, not null
   * @see #getMonthValue()
   */
  def getMonth: Month = {
     Month.of(month)
  }

  /**
   * Checks if the year is a leap year, according to the ISO proleptic
   * calendar system rules.
   * <p>
   * This method applies the current rules for leap years across the whole time-line.
   * In general, a year is a leap year if it is divisible by four without
   * remainder. However, years divisible by 100, are not leap years, with
   * the exception of years divisible by 400 which are.
   * <p>
   * For example, 1904 is a leap year it is divisible by 4.
   * 1900 was not a leap year as it is divisible by 100, however 2000 was a
   * leap year as it is divisible by 400.
   * <p>
   * The calculation is proleptic - applying the same rules into the far future and far past.
   * This is historically inaccurate, but is correct for the ISO-8601 standard.
   *
   * @return true if the year is leap, false otherwise
   */
  def isLeapYear: Boolean = {
     IsoChronology.INSTANCE.isLeapYear(year)
  }

  /**
   * Checks if the day-of-month is valid for this year-month.
   * <p>
   * This method checks whether this year and month and the input day form
   * a valid date.
   *
   * @param dayOfMonth  the day-of-month to validate, from 1 to 31, invalid value returns false
   * @return true if the day is valid for this year-month
   */
  def isValidDay(dayOfMonth: Int): Boolean = {
     dayOfMonth >= 1 && dayOfMonth <= lengthOfMonth
  }

  /**
   * Returns the length of the month, taking account of the year.
   * <p>
   * This returns the length of the month in days.
   * For example, a date in January would  31.
   *
   * @return the length of the month in days, from 28 to 31
   */
  def lengthOfMonth: Int = {
     getMonth.length(isLeapYear)
  }

  /**
   * Returns the length of the year.
   * <p>
   * This returns the length of the year in days, either 365 or 366.
   *
   * @return 366 if the year is leap, 365 otherwise
   */
  def lengthOfYear: Int = {
     (if (isLeapYear) 366 else 365)
  }

  /**
   * Returns an adjusted copy of this year-month.
   * <p>
   * This returns a {@code YearMonth}, based on this one, with the year-month adjusted.
   * The adjustment takes place using the specified adjuster strategy object.
   * Read the documentation of the adjuster to understand what adjustment will be made.
   * <p>
   * A simple adjuster might simply set the one of the fields, such as the year field.
   * A more complex adjuster might set the year-month to the next month that
   * Halley's comet will pass the Earth.
   * <p>
   * The result of this method is obtained by invoking the
   * {@link TemporalAdjuster#adjustInto(Temporal)} method on the
   * specified adjuster passing {@code this} as the argument.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param adjuster the adjuster to use, not null
   * @return a { @code YearMonth} based on { @code this} with the adjustment made, not null
   * @throws DateTimeException if the adjustment cannot be made
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def `with`(adjuster: TemporalAdjuster): YearMonth = {
     adjuster.adjustInto(this).asInstanceOf[YearMonth]
  }

  /**
   * Returns a copy of this year-month with the specified field set to a new value.
   * <p>
   * This returns a {@code YearMonth}, based on this one, with the value
   * for the specified field changed.
   * This can be used to change any supported field, such as the year or month.
   * If it is not possible to set the value, because the field is not supported or for
   * some other reason, an exception is thrown.
   * <p>
   * If the field is a {@link ChronoField} then the adjustment is implemented here.
   * The supported fields behave as follows:
   * <ul>
   * <li>{@code MONTH_OF_YEAR} -
   * Returns a {@code YearMonth} with the specified month-of-year.
   * The year will be unchanged.
   * <li>{@code PROLEPTIC_MONTH} -
   * Returns a {@code YearMonth} with the specified proleptic-month.
   * This completely replaces the year and month of this object.
   * <li>{@code YEAR_OF_ERA} -
   * Returns a {@code YearMonth} with the specified year-of-era
   * The month and era will be unchanged.
   * <li>{@code YEAR} -
   * Returns a {@code YearMonth} with the specified year.
   * The month will be unchanged.
   * <li>{@code ERA} -
   * Returns a {@code YearMonth} with the specified era.
   * The month and year-of-era will be unchanged.
   * </ul>
   * <p>
   * In all cases, if the new value is outside the valid range of values for the field
   * then a {@code DateTimeException} will be thrown.
   * <p>
   * All other {@code ChronoField} instances will throw an {@code UnsupportedTemporalTypeException}.
   * <p>
   * If the field is not a {@code ChronoField}, then the result of this method
   * is obtained by invoking {@code TemporalField.adjustInto(Temporal, long)}
   * passing {@code this} as the argument. In this case, the field determines
   * whether and how to adjust the instant.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param field  the field to set in the result, not null
   * @param newValue  the new value of the field in the result
   * @return a { @code YearMonth} based on { @code this} with the specified field set, not null
   * @throws DateTimeException if the field cannot be set
   * @throws UnsupportedTemporalTypeException if the field is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  def `with`(field: TemporalField, newValue: Long): YearMonth = {
    if (field.isInstanceOf[ChronoField]) {
      val f: ChronoField = field.asInstanceOf[ChronoField]
      f.checkValidValue(newValue)
      f match {
        case MONTH_OF_YEAR =>
           withMonth(newValue.asInstanceOf[Int])
        case PROLEPTIC_MONTH =>
           plusMonths(newValue - getProlepticMonth)
        case YEAR_OF_ERA =>
           withYear((if (year < 1) 1 - newValue else newValue).asInstanceOf[Int])
        case YEAR =>
           withYear(newValue.asInstanceOf[Int])
        case ERA =>
           (if (getLong(ERA) == newValue) this else withYear(1 - year))
      }
      throw new UnsupportedTemporalTypeException("Unsupported field: " + field)
    }
     field.adjustInto(this, newValue)
  }

  /**
   * Returns a copy of this {@code YearMonth} with the year altered.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param year  the year to set in the returned year-month, from MIN_YEAR to MAX_YEAR
   * @return a { @code YearMonth} based on this year-month with the requested year, not null
   * @throws DateTimeException if the year value is invalid
   */
  def withYear(year: Int): YearMonth = {
    YEAR.checkValidValue(year)
     `with`(year, month)
  }

  /**
   * Returns a copy of this {@code YearMonth} with the month-of-year altered.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param month  the month-of-year to set in the returned year-month, from 1 (January) to 12 (December)
   * @return a { @code YearMonth} based on this year-month with the requested month, not null
   * @throws DateTimeException if the month-of-year value is invalid
   */
  def withMonth(month: Int): YearMonth = {
    MONTH_OF_YEAR.checkValidValue(month)
     `with`(year, month)
  }

  /**
   * Returns a copy of this year-month with the specified amount added.
   * <p>
   * This returns a {@code YearMonth}, based on this one, with the specified amount added.
   * The amount is typically {@link Period} but may be any other type implementing
   * the {@link TemporalAmount} interface.
   * <p>
   * The calculation is delegated to the amount object by calling
   * {@link TemporalAmount#addTo(Temporal)}. The amount implementation is free
   * to implement the addition in any way it wishes, however it typically
   * calls back to {@link #plus(long, TemporalUnit)}. Consult the documentation
   * of the amount implementation to determine if it can be successfully added.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param amountToAdd  the amount to add, not null
   * @return a { @code YearMonth} based on this year-month with the addition made, not null
   * @throws DateTimeException if the addition cannot be made
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def plus(amountToAdd: TemporalAmount): YearMonth = {
     amountToAdd.addTo(this).asInstanceOf[YearMonth]
  }

  /**
   * Returns a copy of this year-month with the specified amount added.
   * <p>
   * This returns a {@code YearMonth}, based on this one, with the amount
   * in terms of the unit added. If it is not possible to add the amount, because the
   * unit is not supported or for some other reason, an exception is thrown.
   * <p>
   * If the field is a {@link ChronoUnit} then the addition is implemented here.
   * The supported fields behave as follows:
   * <ul>
   * <li>{@code MONTHS} -
   * Returns a {@code YearMonth} with the specified number of months added.
   * This is equivalent to {@link #plusMonths(long)}.
   * <li>{@code YEARS} -
   * Returns a {@code YearMonth} with the specified number of years added.
   * This is equivalent to {@link #plusYears(long)}.
   * <li>{@code DECADES} -
   * Returns a {@code YearMonth} with the specified number of decades added.
   * This is equivalent to calling {@link #plusYears(long)} with the amount
   * multiplied by 10.
   * <li>{@code CENTURIES} -
   * Returns a {@code YearMonth} with the specified number of centuries added.
   * This is equivalent to calling {@link #plusYears(long)} with the amount
   * multiplied by 100.
   * <li>{@code MILLENNIA} -
   * Returns a {@code YearMonth} with the specified number of millennia added.
   * This is equivalent to calling {@link #plusYears(long)} with the amount
   * multiplied by 1,000.
   * <li>{@code ERAS} -
   * Returns a {@code YearMonth} with the specified number of eras added.
   * Only two eras are supported so the amount must be one, zero or minus one.
   * If the amount is non-zero then the year is changed such that the year-of-era
   * is unchanged.
   * </ul>
   * <p>
   * All other {@code ChronoUnit} instances will throw an {@code UnsupportedTemporalTypeException}.
   * <p>
   * If the field is not a {@code ChronoUnit}, then the result of this method
   * is obtained by invoking {@code TemporalUnit.addTo(Temporal, long)}
   * passing {@code this} as the argument. In this case, the unit determines
   * whether and how to perform the addition.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param amountToAdd  the amount of the unit to add to the result, may be negative
   * @param unit  the unit of the amount to add, not null
   * @return a { @code YearMonth} based on this year-month with the specified amount added, not null
   * @throws DateTimeException if the addition cannot be made
   * @throws UnsupportedTemporalTypeException if the unit is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  def plus(amountToAdd: Long, unit: TemporalUnit): YearMonth = {
    if (unit.isInstanceOf[ChronoUnit]) {
      unit.asInstanceOf[ChronoUnit] match {
        case MONTHS =>
           plusMonths(amountToAdd)
        case YEARS =>
           plusYears(amountToAdd)
        case DECADES =>
           plusYears(Math.multiplyExact(amountToAdd, 10))
        case CENTURIES =>
           plusYears(Math.multiplyExact(amountToAdd, 100))
        case MILLENNIA =>
           plusYears(Math.multiplyExact(amountToAdd, 1000))
        case ERAS =>
           `with`(ERA, Math.addExact(getLong(ERA), amountToAdd))
      }
      throw new UnsupportedTemporalTypeException("Unsupported unit: " + unit)
    }
     unit.addTo(this, amountToAdd)
  }

  /**
   * Returns a copy of this year-month with the specified period in years added.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param yearsToAdd  the years to add, may be negative
   * @return a { @code YearMonth} based on this year-month with the years added, not null
   * @throws DateTimeException if the result exceeds the supported range
   */
  def plusYears(yearsToAdd: Long): YearMonth = {
    if (yearsToAdd == 0) {
       this
    }
    val newYear: Int = YEAR.checkValidIntValue(year + yearsToAdd)
     `with`(newYear, month)
  }

  /**
   * Returns a copy of this year-month with the specified period in months added.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param monthsToAdd  the months to add, may be negative
   * @return a { @code YearMonth} based on this year-month with the months added, not null
   * @throws DateTimeException if the result exceeds the supported range
   */
  def plusMonths(monthsToAdd: Long): YearMonth = {
    if (monthsToAdd == 0) {
       this
    }
    val monthCount: Long = year * 12L + (month - 1)
    val calcMonths: Long = monthCount + monthsToAdd
    val newYear: Int = YEAR.checkValidIntValue(Math.floorDiv(calcMonths, 12))
    val newMonth: Int = Math.floorMod(calcMonths, 12).asInstanceOf[Int] + 1
     `with`(newYear, newMonth)
  }

  /**
   * Returns a copy of this year-month with the specified amount subtracted.
   * <p>
   * This returns a {@code YearMonth}, based on this one, with the specified amount subtracted.
   * The amount is typically {@link Period} but may be any other type implementing
   * the {@link TemporalAmount} interface.
   * <p>
   * The calculation is delegated to the amount object by calling
   * {@link TemporalAmount#subtractFrom(Temporal)}. The amount implementation is free
   * to implement the subtraction in any way it wishes, however it typically
   * calls back to {@link #minus(long, TemporalUnit)}. Consult the documentation
   * of the amount implementation to determine if it can be successfully subtracted.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param amountToSubtract  the amount to subtract, not null
   * @return a { @code YearMonth} based on this year-month with the subtraction made, not null
   * @throws DateTimeException if the subtraction cannot be made
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def minus(amountToSubtract: TemporalAmount): YearMonth = {
     amountToSubtract.subtractFrom(this).asInstanceOf[YearMonth]
  }

  /**
   * Returns a copy of this year-month with the specified amount subtracted.
   * <p>
   * This returns a {@code YearMonth}, based on this one, with the amount
   * in terms of the unit subtracted. If it is not possible to subtract the amount,
   * because the unit is not supported or for some other reason, an exception is thrown.
   * <p>
   * This method is equivalent to {@link #plus(long, TemporalUnit)} with the amount negated.
   * See that method for a full description of how addition, and thus subtraction, works.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param amountToSubtract  the amount of the unit to subtract from the result, may be negative
   * @param unit  the unit of the amount to subtract, not null
   * @return a { @code YearMonth} based on this year-month with the specified amount subtracted, not null
   * @throws DateTimeException if the subtraction cannot be made
   * @throws UnsupportedTemporalTypeException if the unit is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def minus(amountToSubtract: Long, unit: TemporalUnit): YearMonth = {
     (if (amountToSubtract == Long.MIN_VALUE) plus(Long.MAX_VALUE, unit).plus(1, unit) else plus(-amountToSubtract, unit))
  }

  /**
   * Returns a copy of this year-month with the specified period in years subtracted.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param yearsToSubtract  the years to subtract, may be negative
   * @return a { @code YearMonth} based on this year-month with the years subtracted, not null
   * @throws DateTimeException if the result exceeds the supported range
   */
  def minusYears(yearsToSubtract: Long): YearMonth = {
     (if (yearsToSubtract == Long.MIN_VALUE) plusYears(Long.MAX_VALUE).plusYears(1) else plusYears(-yearsToSubtract))
  }

  /**
   * Returns a copy of this year-month with the specified period in months subtracted.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param monthsToSubtract  the months to subtract, may be negative
   * @return a { @code YearMonth} based on this year-month with the months subtracted, not null
   * @throws DateTimeException if the result exceeds the supported range
   */
  def minusMonths(monthsToSubtract: Long): YearMonth = {
     (if (monthsToSubtract == Long.MIN_VALUE) plusMonths(Long.MAX_VALUE).plusMonths(1) else plusMonths(-monthsToSubtract))
  }

  /**
   * Queries this year-month using the specified query.
   * <p>
   * This queries this year-month using the specified query strategy object.
   * The {@code TemporalQuery} object defines the logic to be used to
   * obtain the result. Read the documentation of the query to understand
   * what the result of this method will be.
   * <p>
   * The result of this method is obtained by invoking the
   * {@link TemporalQuery#queryFrom(TemporalAccessor)} method on the
   * specified query passing {@code this} as the argument.
   *
   * @param <R> the type of the result
   * @param query  the query to invoke, not null
   * @return the query result, null may be returned (defined by the query)
   * @throws DateTimeException if unable to query (defined by the query)
   * @throws ArithmeticException if numeric overflow occurs (defined by the query)
   */
  override def query(query: TemporalQuery[R]): R = {
    if (query eq TemporalQuery.chronology) {
       IsoChronology.INSTANCE.asInstanceOf[R]
    }
    else if (query eq TemporalQuery.precision) {
       MONTHS.asInstanceOf[R]
    }
     Temporal.super.query(query)
  }

  /**
   * Adjusts the specified temporal object to have this year-month.
   * <p>
   * This returns a temporal object of the same observable type as the input
   * with the year and month changed to be the same as this.
   * <p>
   * The adjustment is equivalent to using {@link Temporal#with(TemporalField, long)}
   * passing {@link ChronoField#PROLEPTIC_MONTH} as the field.
   * If the specified temporal object does not use the ISO calendar system then
   * a {@code DateTimeException} is thrown.
   * <p>
   * In most cases, it is clearer to reverse the calling pattern by using
   * {@link Temporal#with(TemporalAdjuster)}:
   * {{{
   * // these two lines are equivalent, but the second approach is recommended
   * temporal = thisYearMonth.adjustInto(temporal);
   * temporal = temporal.with(thisYearMonth);
   * }}}
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param temporal  the target object to be adjusted, not null
   * @return the adjusted object, not null
   * @throws DateTimeException if unable to make the adjustment
   * @throws ArithmeticException if numeric overflow occurs
   */
  def adjustInto(temporal: Temporal): Temporal = {
    if ((Chronology.from(temporal) == IsoChronology.INSTANCE) == false) {
      throw new DateTimeException("Adjustment only supported on ISO date-time")
    }
     temporal.`with`(PROLEPTIC_MONTH, getProlepticMonth)
  }

  /**
   * Calculates the amount of time until another year-month in terms of the specified unit.
   * <p>
   * This calculates the amount of time between two {@code YearMonth}
   * objects in terms of a single {@code TemporalUnit}.
   * The start and end points are {@code this} and the specified year-month.
   * The result will be negative if the end is before the start.
   * The {@code Temporal} passed to this method is converted to a
   * {@code YearMonth} using {@link #from(TemporalAccessor)}.
   * For example, the period in years between two year-months can be calculated
   * using {@code startYearMonth.until(endYearMonth, YEARS)}.
   * <p>
   * The calculation returns a whole number, representing the number of
   * complete units between the two year-months.
   * For example, the period in decades between 2012-06 and 2032-05
   * will only be one decade as it is one month short of two decades.
   * <p>
   * There are two equivalent ways of using this method.
   * The first is to invoke this method.
   * The second is to use {@link TemporalUnit#between(Temporal, Temporal)}:
   * {{{
   * // these two lines are equivalent
   * amount = start.until(end, MONTHS);
   * amount = MONTHS.between(start, end);
   * }}}
   * The choice should be made based on which makes the code more readable.
   * <p>
   * The calculation is implemented in this method for {@link ChronoUnit}.
   * The units {@code MONTHS}, {@code YEARS}, {@code DECADES},
   * {@code CENTURIES}, {@code MILLENNIA} and {@code ERAS} are supported.
   * Other {@code ChronoUnit} values will throw an exception.
   * <p>
   * If the unit is not a {@code ChronoUnit}, then the result of this method
   * is obtained by invoking {@code TemporalUnit.between(Temporal, Temporal)}
   * passing {@code this} as the first argument and the converted input temporal
   * as the second argument.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param endExclusive  the end date, which is converted to a { @code YearMonth}, not null
   * @param unit  the unit to measure the amount in, not null
   * @return the amount of time between this year-month and the end year-month
   * @throws DateTimeException if the amount cannot be calculated, or the end
   *                           temporal cannot be converted to a { @code YearMonth}
   * @throws UnsupportedTemporalTypeException if the unit is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  def until(endExclusive: Temporal, unit: TemporalUnit): Long = {
    val end: YearMonth = YearMonth.from(endExclusive)
    if (unit.isInstanceOf[ChronoUnit]) {
      val monthsUntil: Long = end.getProlepticMonth - getProlepticMonth
      unit.asInstanceOf[ChronoUnit] match {
        case MONTHS =>
           monthsUntil
        case YEARS =>
           monthsUntil / 12
        case DECADES =>
           monthsUntil / 120
        case CENTURIES =>
           monthsUntil / 1200
        case MILLENNIA =>
           monthsUntil / 12000
        case ERAS =>
           end.getLong(ERA) - getLong(ERA)
      }
      throw new UnsupportedTemporalTypeException("Unsupported unit: " + unit)
    }
     unit.between(this, end)
  }

  /**
   * Formats this year-month using the specified formatter.
   * <p>
   * This year-month will be passed to the formatter to produce a string.
   *
   * @param formatter  the formatter to use, not null
   * @return the formatted year-month string, not null
   * @throws DateTimeException if an error occurs during printing
   */
  def format(formatter: DateTimeFormatter): String = {
    object
     formatter.format(this)
  }

  /**
   * Combines this year-month with a day-of-month to create a {@code Date}.
   * <p>
   * This returns a {@code Date} formed from this year-month and the specified day-of-month.
   * <p>
   * The day-of-month value must be valid for the year-month.
   * <p>
   * This method can be used as part of a chain to produce a date:
   * {{{
   * Date date = year.atMonth(month).atDay(day);
   * }}}
   *
   * @param dayOfMonth  the day-of-month to use, from 1 to 31
   * @return the date formed from this year-month and the specified day, not null
   * @throws DateTimeException if the day is invalid for the year-month
   * @see #isValidDay(int)
   */
  def atDay(dayOfMonth: Int): Date = {
     Date.of(year, month, dayOfMonth)
  }

  /**
   * Returns a {@code Date} at the end of the month.
   * <p>
   * This returns a {@code Date} based on this year-month.
   * The day-of-month is set to the last valid day of the month, taking
   * into account leap years.
   * <p>
   * This method can be used as part of a chain to produce a date:
   * {{{
   * Date date = year.atMonth(month).atEndOfMonth();
   * }}}
   *
   * @return the last valid date of this year-month, not null
   */
  def atEndOfMonth: Date = {
     Date.of(year, month, lengthOfMonth)
  }

  /**
   * Compares this year-month to another year-month.
   * <p>
   * The comparison is based first on the value of the year, then on the value of the month.
   * It is "consistent with equals", as defined by {@link Comparable}.
   *
   * @param other  the other year-month to compare to, not null
   * @return the comparator value, negative if less, positive if greater
   */
  def compareTo(other: YearMonth): Int = {
    var cmp: Int = (year - other.year)
    if (cmp == 0) {
      cmp = (month - other.month)
    }
     cmp
  }

  /**
   * Is this year-month after the specified year-month.
   *
   * @param other  the other year-month to compare to, not null
   * @return true if this is after the specified year-month
   */
  def isAfter(other: YearMonth): Boolean = {
     compareTo(other) > 0
  }

  /**
   * Is this year-month before the specified year-month.
   *
   * @param other  the other year-month to compare to, not null
   * @return true if this point is before the specified year-month
   */
  def isBefore(other: YearMonth): Boolean = {
     compareTo(other) < 0
  }

  /**
   * Checks if this year-month is equal to another year-month.
   * <p>
   * The comparison is based on the time-line position of the year-months.
   *
   * @param obj  the object to check, null returns false
   * @return true if this is equal to the other year-month
   */
  override def equals(obj: AnyRef): Boolean = {
    if (this eq obj) {
       true
    }
    if (obj.isInstanceOf[YearMonth]) {
      val other: YearMonth = obj.asInstanceOf[YearMonth]
       year == other.year && month == other.month
    }
     false
  }

  /**
   * A hash code for this year-month.
   *
   * @return a suitable hash code
   */
  override def hashCode: Int = {
     year ^ (month << 27)
  }

  /**
   * Outputs this year-month as a {@code String}, such as {@code 2007-12}.
   * <p>
   * The output will be in the format {@code uuuu-MM}:
   *
   * @return a string representation of this year-month, not null
   */
  override def toString: String = {
    val absYear: Int = Math.abs(year)
    val buf: StringBuilder = new StringBuilder(9)
    if (absYear < 1000) {
      if (year < 0) {
        buf.append(year - 10000).deleteCharAt(1)
      }
      else {
        buf.append(year + 10000).deleteCharAt(0)
      }
    }
    else {
      buf.append(year)
    }
     buf.append(if (month < 10) "-0" else "-").append(month).toString
  }

  /**
   * Writes the object using a
   * <a href="../../../serialized-form.html#java.time.temporal.Ser">dedicated serialized form</a>.
   * {{{
   * out.writeByte(12);  // identifies this as a YearMonth
   * out.writeInt(year);
   * out.writeByte(month);
   * }}}
   *
   * @return the instance of { @code Ser}, not null
   */
  private def writeReplace: AnyRef = {
     new Ser(Ser.YEAR_MONTH_TYPE, this)
  }

  /**
   * Defend against malicious streams.
   * @return never
   * @throws InvalidObjectException always
   */
  private def readResolve: AnyRef = {
    throw new InvalidObjectException("Deserialization via serialization delegate")
  }

  private[time] def writeExternal(out: DataOutput) {
    out.writeInt(year)
    out.writeByte(month)
  }

  /**
   * The year.
   */
  private final val year: Int = 0
  /**
   * The month-of-year, not null.
   */
  private final val month: Int = 0
}

