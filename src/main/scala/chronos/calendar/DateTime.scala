package chronos.calendar

import chronos.temporal._

/**
 * A date without time-of-day or time-zone in an arbitrary chronology, intended
 * for advanced globalization use cases.
 * <p>
 * <b>Most applications should declare method signatures, fields and variables
 * as {@link Date}, not this interface.</b>
 * <p>
 * A {@code ChronoLocalDate} is the abstract representation of a date where the
 * {@code Chronology chronology}, or calendar system, is pluggable.
 * The date is defined in terms of fields expressed by {@link TemporalField},
 * where most common implementations are defined in {@link ChronoField}.
 * The chronology defines how the calendar system operates and the meaning of
 * the standard fields.
 *
 * <h3>When to use this interface</h3>
 * The design of the API encourages the use of {@code Date} rather than this
 * interface, even in the case where the application needs to deal with multiple
 * calendar systems. The rationale for this is explored in the following documentation.
 * <p>
 * The primary use case where this interface should be used is where the generic
 * type parameter {@code <D>} is fully defined as a specific chronology.
 * In that case, the assumptions of that chronology are known at development
 * time and specified in the code.
 * <p>
 * When the chronology is defined in the generic type parameter as ? or otherwise
 * unknown at development time, the rest of the discussion below applies.
 * <p>
 * To emphasize the point, declaring a method signature, field or variable as this
 * interface type can initially seem like the sensible way to globalize an application,
 * however it is usually the wrong approach.
 * As such, it should be considered an application-wide architectural decision to choose
 * to use this interface as opposed to {@code Date}.
 *
 * <h3>Architectural issues to consider</h3>
 * These are some of the points that must be considered before using this interface
 * throughout an application.
 * <p>
 * 1) Applications using this interface, as opposed to using just {@code Date},
 * face a significantly higher probability of bugs. This is because the calendar system
 * in use is not known at development time. A key cause of bugs is where the developer
 * applies assumptions from their day-to-day knowledge of the ISO calendar system
 * to code that is intended to deal with any arbitrary calendar system.
 * The section below outlines how those assumptions can cause problems
 * The primary mechanism for reducing this increased risk of bugs is a strong code review process.
 * This should also be considered a extra cost in maintenance for the lifetime of the code.
 * <p>
 * 2) This interface does not enforce immutability of implementations.
 * While the implementation notes indicate that all implementations must be immutable
 * there is nothing in the code or type system to enforce this. Any method declared
 * to accept a {@code ChronoLocalDate} could therefore be passed a poorly or
 * maliciously written mutable implementation.
 * <p>
 * 3) Applications using this interface  must consider the impact of eras.
 * {@code Date} shields users from the concept of eras, by ensuring that {@code getYear()}
 * returns the proleptic year. That decision ensures that developers can think of
 * {@code Date} instances as consisting of three fields - year, month-of-year and day-of-month.
 * By contrast, users of this interface must think of dates as consisting of four fields -
 * era, year-of-era, month-of-year and day-of-month. The extra era field is frequently
 * forgotten, yet it is of vital importance to dates in an arbitrary calendar system.
 * For example, in the Japanese calendar system, the era represents the reign of an Emperor.
 * Whenever one reign ends and another starts, the year-of-era is reset to one.
 * <p>
 * 4) The only agreed international standard for passing a date between two systems
 * is the ISO-8601 standard which requires the ISO calendar system. Using this interface
 * throughout the application will inevitably lead to the requirement to pass the date
 * across a network or component boundary, requiring an application specific protocol or format.
 * <p>
 * 5) Long term persistence, such as a database, will almost always only accept dates in the
 * ISO-8601 calendar system (or the related Julian-Gregorian). Passing around dates in other
 * calendar systems increases the complications of interacting with persistence.
 * <p>
 * 6) Most of the time, passing a {@code ChronoLocalDate} throughout an application
 * is unnecessary, as discussed in the last section below.
 *
 * <h3>False assumptions causing bugs in multi-calendar system code</h3>
 * As indicated above, there are many issues to consider when try to use and manipulate a
 * date in an arbitrary calendar system. These are some of the key issues.
 * <p>
 * Code that queries the day-of-month and assumes that the value will never be more than
 * 31 is invalid. Some calendar systems have more than 31 days in some months.
 * <p>
 * Code that adds 12 months to a date and assumes that a year has been added is invalid.
 * Some calendar systems have a different number of months, such as 13 in the Coptic or Ethiopic.
 * <p>
 * Code that adds one month to a date and assumes that the month-of-year value will increase
 * by one or wrap to the next year is invalid. Some calendar systems have a variable number
 * of months in a year, such as the Hebrew.
 * <p>
 * Code that adds one month, then adds a second one month and assumes that the day-of-month
 * will remain close to its original value is invalid. Some calendar systems have a large difference
 * between the length of the longest month and the length of the shortest month.
 * For example, the Coptic or Ethiopic have 12 months of 30 days and 1 month of 5 days.
 * <p>
 * Code that adds seven days and assumes that a week has been added is invalid.
 * Some calendar systems have weeks of other than seven days, such as the French Revolutionary.
 * <p>
 * Code that assumes that because the year of {@code date1} is greater than the year of {@code date2}
 * then {@code date1} is after {@code date2} is invalid. This is invalid for all calendar systems
 * when referring to the year-of-era, and especially untrue of the Japanese calendar system
 * where the year-of-era restarts with the reign of every new Emperor.
 * <p>
 * Code that treats month-of-year one and day-of-month one as the start of the year is invalid.
 * Not all calendar systems start the year when the month value is one.
 * <p>
 * In general, manipulating a date, and even querying a date, is wide open to bugs when the
 * calendar system is unknown at development time. This is why it is essential that code using
 * this interface is subjected to additional code reviews. It is also why an architectural
 * decision to avoid this interface type is usually the correct one.
 *
 * <h3>Using Date instead</h3>
 * The primary alternative to using this interface throughout your application is as follows.
 * <p><ul>
 * <li>Declare all method signatures referring to dates in terms of {@code Date}.
 * <li>Either store the chronology (calendar system) in the user profile or lookup
 * the chronology from the user locale
 * <li>Convert the ISO {@code Date} to and from the user's preferred calendar system during
 * printing and parsing
 * </ul><p>
 * This approach treats the problem of globalized calendar systems as a localization issue
 * and confines it to the UI layer. This approach is in keeping with other localization
 * issues in the java platform.
 * <p>
 * As discussed above, performing calculations on a date where the rules of the calendar system
 * are pluggable requires skill and is not recommended.
 * Fortunately, the need to perform calculations on a date in an arbitrary calendar system
 * is extremely rare. For example, it is highly unlikely that the business rules of a library
 * book rental scheme will allow rentals to be for one month, where meaning of the month
 * is dependent on the user's preferred calendar system.
 * <p>
 * A key use case for calculations on a date in an arbitrary calendar system is producing
 * a month-by-month calendar for display and user interaction. Again, this is a UI issue,
 * and use of this interface solely within a few methods of the UI layer may be justified.
 * <p>
 * In any other part of the system, where a date must be manipulated in a calendar system
 * other than ISO, the use case will generally specify the calendar system to use.
 * For example, an application may need to calculate the next Islamic or Hebrew holiday
 * which may require manipulating the date.
 * This kind of use case can be handled as follows:
 * <p><ul>
 * <li>start from the ISO {@code Date} being passed to the method
 * <li>convert the date to the alternate calendar system, which for this use case is known
 * rather than arbitrary
 * <li>perform the calculation
 * <li>convert back to {@code Date}
 * </ul><p>
 * Developers writing low-level frameworks or libraries should also avoid this interface.
 * Instead, one of the two general purpose access interfaces should be used.
 * Use {@link TemporalAccessor} if read-only access is required, or use {@link Temporal}
 * if read-write access is required.
 *
 * @implSpec
 * This interface must be implemented with care to ensure other classes operate correctly.
 * All implementations that can be instantiated must be final, immutable and thread-safe.
 * Subclasses should be Serializable wherever possible.
 * <p>
 * Additional calendar systems may be added to the system.
 * See { @link Chronology} for more details.
 *
 * @since 1.8
 */
object ChronoLocalDate {
  /**
   * Gets a comparator that compares {@code ChronoLocalDate} in
   * time-line order ignoring the chronology.
   * <p>
   * This comparator differs from the comparison in {@link #compareTo} in that it
   * only compares the underlying date and not the chronology.
   * This allows dates in different calendar systems to be compared based
   * on the position of the date on the local time-line.
   * The underlying comparison is equivalent to comparing the epoch-day.
   * @return a comparator that compares in time-line order ignoring the chronology
   *
   * @see #isAfter
   * @see #isBefore
   * @see #isEqual
   */
  def timeLineOrder: Comparator[ChronoLocalDate] = {
     Chronology.DATE_ORDER
  }

  /**
   * Obtains an instance of {@code ChronoLocalDate} from a temporal object.
   * <p>
   * This obtains a local date based on the specified temporal.
   * A {@code TemporalAccessor} represents an arbitrary set of date and time information,
   * which this factory converts to an instance of {@code ChronoLocalDate}.
   * <p>
   * The conversion extracts and combines the chronology and the date
   * from the temporal object. The behavior is equivalent to using
   * {@link Chronology#date(TemporalAccessor)} with the extracted chronology.
   * Implementations are permitted to perform optimizations such as accessing
   * those fields that are equivalent to the relevant objects.
   * <p>
   * This method matches the signature of the functional interface {@link TemporalQuery}
   * allowing it to be used as a query via method reference, {@code ChronoLocalDate::from}.
   *
   * @param temporal  the temporal object to convert, not null
   * @return the date, not null
   * @throws DateTimeException if unable to convert to a { @code ChronoLocalDate}
   * @see Chronology#date(TemporalAccessor)
   */
  def from(temporal: TemporalAccessor): ChronoLocalDate = {
    if (temporal.isInstanceOf[ChronoLocalDate]) {
       temporal.asInstanceOf[ChronoLocalDate]
    }

    val chrono: Chronology = temporal.query(TemporalQuery.chronology)
    if (chrono == null) {
      throw new DateTimeException("Unable to obtain ChronoLocalDate from TemporalAccessor: " + temporal.getClass)
    }
     chrono.date(temporal)
  }
}

trait ChronoLocalDate extends Temporal with TemporalAdjuster with Comparable[ChronoLocalDate] {
  /**
   * Gets the chronology of this date.
   * <p>
   * The {@code Chronology} represents the calendar system in use.
   * The era and other fields in {@link ChronoField} are defined by the chronology.
   *
   * @return the chronology, not null
   */
  def getChronology: Chronology

  /**
   * Gets the era, as defined by the chronology.
   * <p>
   * The era is, conceptually, the largest division of the time-line.
   * Most calendar systems have a single epoch dividing the time-line into two eras.
   * However, some have multiple eras, such as one for the reign of each leader.
   * The exact meaning is determined by the {@code Chronology}.
   * <p>
   * All correctly implemented {@code Era} classes are singletons, thus it
   * is valid code to write {@code date.getEra() == SomeChrono.ERA_NAME)}.
   * <p>
   * This default implementation uses {@link Chronology#eraOf(int)}.
   *
   * @return the chronology specific era constant applicable at this date, not null
   */
  def getEra: Era = {
     getChronology.eraOf(get(ERA))
  }

  /**
   * Checks if the year is a leap year, as defined by the calendar system.
   * <p>
   * A leap-year is a year of a longer length than normal.
   * The exact meaning is determined by the chronology with the constraint that
   * a leap-year must imply a year-length longer than a non leap-year.
   * <p>
   * This default implementation uses {@link Chronology#isLeapYear(long)}.
   *
   * @return true if this date is in a leap year, false otherwise
   */
  def isLeapYear: Boolean = {
     getChronology.isLeapYear(getLong(YEAR))
  }

  /**
   * Returns the length of the month represented by this date, as defined by the calendar system.
   * <p>
   * This returns the length of the month in days.
   *
   * @return the length of the month in days
   */
  def lengthOfMonth: Int

  /**
   * Returns the length of the year represented by this date, as defined by the calendar system.
   * <p>
   * This returns the length of the year in days.
   * <p>
   * The default implementation uses {@link #isLeapYear()} and returns 365 or 366.
   *
   * @return the length of the year in days
   */
  def lengthOfYear: Int = {
     (if (isLeapYear) 366 else 365)
  }

  /**
   * Checks if the specified field is supported.
   * <p>
   * This checks if the specified field can be queried on this date.
   * If false, then calling the {@link #range(TemporalField) range},
   * {@link #get(TemporalField) get} and {@link #with(TemporalField, long)}
   * methods will throw an exception.
   * <p>
   * The set of supported fields is defined by the chronology and normally includes
   * all {@code ChronoField} date fields.
   * <p>
   * If the field is not a {@code ChronoField}, then the result of this method
   * is obtained by invoking {@code TemporalField.isSupportedBy(TemporalAccessor)}
   * passing {@code this} as the argument.
   * Whether the field is supported is determined by the field.
   *
   * @param field  the field to check, null returns false
   * @return true if the field can be queried, false if not
   */
  def isSupported(field: TemporalField): Boolean = {
    if (field.isInstanceOf[ChronoField]) {
       field.isDateBased
    }
     field != null && field.isSupportedBy(this)
  }

  /**
   * Checks if the specified unit is supported.
   * <p>
   * This checks if the specified unit can be added to or subtracted from this date.
   * If false, then calling the {@link #plus(long, TemporalUnit)} and
   * {@link #minus(long, TemporalUnit) minus} methods will throw an exception.
   * <p>
   * The set of supported units is defined by the chronology and normally includes
   * all {@code ChronoUnit} date units except {@code FOREVER}.
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
       unit.isDateBased
    }
     unit != null && unit.isSupportedBy(this)
  }

  /**
   * {@inheritDoc}
   * @throws DateTimeException { @inheritDoc}
   * @throws ArithmeticException { @inheritDoc}
   */
  override def `with`(adjuster: TemporalAdjuster): ChronoLocalDate = {
     ChronoLocalDateImpl.ensureValid(getChronology, Temporal.super.`with`(adjuster))
  }

  /**
   * {@inheritDoc}
   * @throws DateTimeException { @inheritDoc}
   * @throws UnsupportedTemporalTypeException { @inheritDoc}
   * @throws ArithmeticException { @inheritDoc}
   */
  def `with`(field: TemporalField, newValue: Long): ChronoLocalDate = {
    if (field.isInstanceOf[ChronoField]) {
      throw new UnsupportedTemporalTypeException("Unsupported field: " + field)
    }
     ChronoLocalDateImpl.ensureValid(getChronology, field.adjustInto(this, newValue))
  }

  /**
   * {@inheritDoc}
   * @throws DateTimeException { @inheritDoc}
   * @throws ArithmeticException { @inheritDoc}
   */
  override def plus(amount: TemporalAmount): ChronoLocalDate = {
     ChronoLocalDateImpl.ensureValid(getChronology, Temporal.super.plus(amount))
  }

  /**
   * {@inheritDoc}
   * @throws DateTimeException { @inheritDoc}
   * @throws ArithmeticException { @inheritDoc}
   */
  def plus(amountToAdd: Long, unit: TemporalUnit): ChronoLocalDate = {
    if (unit.isInstanceOf[ChronoUnit]) {
      throw new UnsupportedTemporalTypeException("Unsupported unit: " + unit)
    }
     ChronoLocalDateImpl.ensureValid(getChronology, unit.addTo(this, amountToAdd))
  }

  /**
   * {@inheritDoc}
   * @throws DateTimeException { @inheritDoc}
   * @throws ArithmeticException { @inheritDoc}
   */
  override def minus(amount: TemporalAmount): ChronoLocalDate = {
     ChronoLocalDateImpl.ensureValid(getChronology, Temporal.super.minus(amount))
  }

  /**
   * {@inheritDoc}
   * @throws DateTimeException { @inheritDoc}
   * @throws UnsupportedTemporalTypeException { @inheritDoc}
   * @throws ArithmeticException { @inheritDoc}
   */
  override def minus(amountToSubtract: Long, unit: TemporalUnit): ChronoLocalDate = {
     ChronoLocalDateImpl.ensureValid(getChronology, Temporal.super.minus(amountToSubtract, unit))
  }

  /**
   * Queries this date using the specified query.
   * <p>
   * This queries this date using the specified query strategy object.
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

  override def query[R](query: TemporalQuery[R]): R = {
    if (query eq TemporalQuery.zoneId || query eq TemporalQuery.zone || query eq TemporalQuery.offset) {
       null
    }
    else if (query eq TemporalQuery.localTime) {
       null
    }
    else if (query eq TemporalQuery.chronology) {
       getChronology.asInstanceOf[R]
    }
    else if (query eq TemporalQuery.precision) {
       DAYS.asInstanceOf[R]
    }
     query.queryFrom(this)
  }

  /**
   * Adjusts the specified temporal object to have the same date as this object.
   * <p>
   * This returns a temporal object of the same observable type as the input
   * with the date changed to be the same as this.
   * <p>
   * The adjustment is equivalent to using {@link Temporal#with(TemporalField, long)}
   * passing {@link ChronoField#EPOCH_DAY} as the field.
   * <p>
   * In most cases, it is clearer to reverse the calling pattern by using
   * {@link Temporal#with(TemporalAdjuster)}:
   * {{{
   * // these two lines are equivalent, but the second approach is recommended
   * temporal = thisLocalDate.adjustInto(temporal);
   * temporal = temporal.with(thisLocalDate);
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
     temporal.`with`(EPOCH_DAY, toEpochDay)
  }

  /**
   * Calculates the amount of time until another date in terms of the specified unit.
   * <p>
   * This calculates the amount of time between two {@code ChronoLocalDate}
   * objects in terms of a single {@code TemporalUnit}.
   * The start and end points are {@code this} and the specified date.
   * The result will be negative if the end is before the start.
   * The {@code Temporal} passed to this method is converted to a
   * {@code ChronoLocalDate} using {@link Chronology#date(TemporalAccessor)}.
   * The calculation returns a whole number, representing the number of
   * complete units between the two dates.
   * For example, the amount in days between two dates can be calculated
   * using {@code startDate.until(endDate, DAYS)}.
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
   * The units {@code DAYS}, {@code WEEKS}, {@code MONTHS}, {@code YEARS},
   * {@code DECADES}, {@code CENTURIES}, {@code MILLENNIA} and {@code ERAS}
   * should be supported by all implementations.
   * Other {@code ChronoUnit} values will throw an exception.
   * <p>
   * If the unit is not a {@code ChronoUnit}, then the result of this method
   * is obtained by invoking {@code TemporalUnit.between(Temporal, Temporal)}
   * passing {@code this} as the first argument and the input temporal as
   * the second argument.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param endExclusive  the end date, which is converted to a
   *                      { @code ChronoLocalDate} in the same chronology, not null
   * @param unit  the unit to measure the amount in, not null
   * @return the amount of time between this date and the end date
   * @throws DateTimeException if the amount cannot be calculated, or the end
   *                           temporal cannot be converted to a { @code ChronoLocalDate}
   * @throws UnsupportedTemporalTypeException if the unit is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  def until(endExclusive: Temporal, unit: TemporalUnit): Long

  /**
   * Calculates the period between this date and another date as a {@code ChronoPeriod}.
   * <p>
   * This calculates the period between two dates. All supplied chronologies
   * calculate the period using years, months and days, however the
   * {@code ChronoPeriod} API allows the period to be represented using other units.
   * <p>
   * The start and end points are {@code this} and the specified date.
   * The result will be negative if the end is before the start.
   * The negative sign will be the same in each of year, month and day.
   * <p>
   * The calculation is performed using the chronology of this date.
   * If necessary, the input date will be converted to match.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param endDateExclusive  the end date, exclusive, which may be in any chronology, not null
   * @return the period between this date and the end date, not null
   * @throws DateTimeException if the period cannot be calculated
   * @throws ArithmeticException if numeric overflow occurs
   */
  def until(endDateExclusive: ChronoLocalDate): ChronoPeriod

  /**
   * Formats this date using the specified formatter.
   * <p>
   * This date will be passed to the formatter to produce a string.
   * <p>
   * The default implementation must behave as follows:
   * {{{
   * return formatter.format(this);
   * }}}
   *
   * @param formatter  the formatter to use, not null
   * @return the formatted date string, not null
   * @throws DateTimeException if an error occurs during printing
   */
  def format(formatter: DateTimeFormatter): String = {

     formatter.format(this)
  }

  /**
   * Combines this date with a time to create a {@code ChronoLocalDateTime}.
   * <p>
   * This returns a {@code ChronoLocalDateTime} formed from this date at the specified time.
   * All possible combinations of date and time are valid.
   *
   * @param localTime  the local time to use, not null
   * @return the local date-time formed from this date and the specified time, not null
   */
  def atTime(localTime: LocalTime): ChronoLocalDateTime[_] = {
     ChronoLocalDateTimeImpl.of(this, localTime)
  }

  /**
   * Converts this date to the Epoch Day.
   * <p>
   * The {@link ChronoField#EPOCH_DAY Epoch Day count} is a simple
   * incrementing count of days where day 0 is 1970-01-01 (ISO).
   * This definition is the same for all chronologies, enabling conversion.
   * <p>
   * This default implementation queries the {@code EPOCH_DAY} field.
   *
   * @return the Epoch Day equivalent to this date
   */
  def toEpochDay: Long = {
     getLong(EPOCH_DAY)
  }

  /**
   * Compares this date to another date, including the chronology.
   * <p>
   * The comparison is based first on the underlying time-line date, then
   * on the chronology.
   * It is "consistent with equals", as defined by {@link Comparable}.
   * <p>
   * For example, the following is the comparator order:
   * <ol>
   * <li>{@code 2012-12-03 (ISO)}</li>
   * <li>{@code 2012-12-04 (ISO)}</li>
   * <li>{@code 2555-12-04 (ThaiBuddhist)}</li>
   * <li>{@code 2012-12-05 (ISO)}</li>
   * </ol>
   * Values #2 and #3 represent the same date on the time-line.
   * When two values represent the same date, the chronology ID is compared to distinguish them.
   * This step is needed to make the ordering "consistent with equals".
   * <p>
   * If all the date objects being compared are in the same chronology, then the
   * additional chronology stage is not required and only the local date is used.
   * To compare the dates of two {@code TemporalAccessor} instances, including dates
   * in two different chronologies, use {@link ChronoField#EPOCH_DAY} as a comparator.
   * <p>
   * This default implementation performs the comparison defined above.
   *
   * @param other  the other date to compare to, not null
   * @return the comparator value, negative if less, positive if greater
   */
  def compareTo(other: ChronoLocalDate): Int = {
    var cmp: Int = Long.compare(toEpochDay, other.toEpochDay)
    if (cmp == 0) {
      cmp = getChronology.compareTo(other.getChronology)
    }
     cmp
  }

  /**
   * Checks if this date is after the specified date ignoring the chronology.
   * <p>
   * This method differs from the comparison in {@link #compareTo} in that it
   * only compares the underlying date and not the chronology.
   * This allows dates in different calendar systems to be compared based
   * on the time-line position.
   * This is equivalent to using {@code date1.toEpochDay() &gt; date2.toEpochDay()}.
   * <p>
   * This default implementation performs the comparison based on the epoch-day.
   *
   * @param other  the other date to compare to, not null
   * @return true if this is after the specified date
   */
  def isAfter(other: ChronoLocalDate): Boolean = {
     this.toEpochDay > other.toEpochDay
  }

  /**
   * Checks if this date is before the specified date ignoring the chronology.
   * <p>
   * This method differs from the comparison in {@link #compareTo} in that it
   * only compares the underlying date and not the chronology.
   * This allows dates in different calendar systems to be compared based
   * on the time-line position.
   * This is equivalent to using {@code date1.toEpochDay() &lt; date2.toEpochDay()}.
   * <p>
   * This default implementation performs the comparison based on the epoch-day.
   *
   * @param other  the other date to compare to, not null
   * @return true if this is before the specified date
   */
  def isBefore(other: ChronoLocalDate): Boolean = {
     this.toEpochDay < other.toEpochDay
  }

  /**
   * Checks if this date is equal to the specified date ignoring the chronology.
   * <p>
   * This method differs from the comparison in {@link #compareTo} in that it
   * only compares the underlying date and not the chronology.
   * This allows dates in different calendar systems to be compared based
   * on the time-line position.
   * This is equivalent to using {@code date1.toEpochDay() == date2.toEpochDay()}.
   * <p>
   * This default implementation performs the comparison based on the epoch-day.
   *
   * @param other  the other date to compare to, not null
   * @return true if the underlying date is equal to the specified date
   */
  def isEqual(other: ChronoLocalDate): Boolean = {
     this.toEpochDay == other.toEpochDay
  }

  /**
   * Checks if this date is equal to another date, including the chronology.
   * <p>
   * Compares this date with another ensuring that the date and chronology are the same.
   * <p>
   * To compare the dates of two {@code TemporalAccessor} instances, including dates
   * in two different chronologies, use {@link ChronoField#EPOCH_DAY} as a comparator.
   *
   * @param obj  the object to check, null returns false
   * @return true if this is equal to the other date
   */
  override def equals(obj: AnyRef): Boolean

  /**
   * A hash code for this date.
   *
   * @return a suitable hash code
   */
  override def hashCode: Int

  /**
   * Outputs this date as a {@code String}.
   * <p>
   * The output will include the full local date.
   *
   * @return the formatted date, not null
   */
  override def toString: String
}

/**
 * A date expressed in terms of a standard year-month-day calendar system.
 * <p>
 * This class is used by applications seeking to handle dates in non-ISO calendar systems.
 * For example, the Japanese, Minguo, Thai Buddhist and others.
 * <p>
 * {@code ChronoLocalDate} is built on the generic concepts of year, month and day.
 * The calendar system, represented by a {@link java.time.chrono.Chronology}, expresses the relationship between
 * the fields and this class allows the resulting date to be manipulated.
 * <p>
 * Note that not all calendar systems are suitable for use with this class.
 * For example, the Mayan calendar uses a system that bears no relation to years, months and days.
 * <p>
 * The API design encourages the use of {@code Date} for the majority of the application.
 * This includes code to read and write from a persistent data store, such as a database,
 * and to send dates and times across a network. The {@code ChronoLocalDate} instance is then used
 * at the user interface level to deal with localized input/output.
 *
 * <P>Example: </p>
 * {{{
 * System.out.printf("Example()%n");
 * // Enumerate the list of available calendars and print today for each
 * Set&lt;Chronology&gt; chronos = Chronology.getAvailableChronologies();
 * for (Chronology chrono : chronos) {
 * ChronoLocalDate date = chrono.dateNow();
 * System.out.printf("   %20s: %s%n", chrono.getID(), date.toString());
 * }
 *
 * // Print the Hijrah date and calendar
 * ChronoLocalDate date = Chronology.of("Hijrah").dateNow();
 * int day = date.get(ChronoField.DAY_OF_MONTH);
 * int dow = date.get(ChronoField.DAY_OF_WEEK);
 * int month = date.get(ChronoField.MONTH_OF_YEAR);
 * int year = date.get(ChronoField.YEAR);
 * System.out.printf("  Today is %s %s %d-%s-%d%n", date.getChronology().getID(),
 * dow, day, month, year);

 * // Print today's date and the last day of the year
 * ChronoLocalDate now1 = Chronology.of("Hijrah").dateNow();
 * ChronoLocalDate first = now1.with(ChronoField.DAY_OF_MONTH, 1)
 * .with(ChronoField.MONTH_OF_YEAR, 1);
 * ChronoLocalDate last = first.plus(1, ChronoUnit.YEARS)
 * .minus(1, ChronoUnit.DAYS);
 * System.out.printf("  Today is %s: start: %s; end: %s%n", last.getChronology().getID(),
 * first, last);
 * }}}
 *
 * <h3>Adding Calendars</h3>
 * <p> The set of calendars is extensible by defining a subclass of {@link ChronoLocalDate}
 * to represent a date instance and an implementation of {@code Chronology}
 * to be the factory for the ChronoLocalDate subclass.
 * </p>
 * <p> To permit the discovery of the additional calendar types the implementation of
 * {@code Chronology} must be registered as a Service implementing the {@code Chronology} interface
 * in the {@code META-INF/Services} file as per the specification of {@link java.util.ServiceLoader}.
 * The subclass must function according to the {@code Chronology} class description and must provide its
 * {@link java.time.chrono.Chronology#getId() chronlogy ID} and {@link Chronology#getCalendarType() calendar type}. </p>
 *
 * @implSpec
 * This abstract class must be implemented with care to ensure other classes operate correctly.
 * All implementations that can be instantiated must be final, immutable and thread-safe.
 * Subclasses should be Serializable wherever possible.
 *
 * @param <D> the ChronoLocalDate of this date-time
 * @since 1.8
 */
object ChronoLocalDateImpl {
  /**
   * Casts the {@code Temporal} to {@code ChronoLocalDate} ensuring it bas the specified chronology.
   *
   * @param chrono  the chronology to check for, not null
   * @param temporal  a date-time to cast, not null
   * @return the date-time checked and cast to { @code ChronoLocalDate}, not null
   * @throws ClassCastException if the date-time cannot be cast to ChronoLocalDate
   *                            or the chronology is not equal this Chronology
   */
  private[calendar] def ensureValid(chrono: Chronology, temporal: Temporal): D = {
    val other: D = temporal.asInstanceOf[D]
    if ((chrono == other.getChronology) == false) {
      throw new ClassCastException("Chronology mismatch, expected: " + chrono.getId + ", actual: " + other.getChronology.getId)
    }
     other
  }


}

abstract class ChronoLocalDateImpl extends ChronoLocalDate with Temporal with TemporalAdjuster  {
  /**
   * Creates an instance.
   */
  private[calendar] def  {

  }

  override def `with`(adjuster: TemporalAdjuster): D = {
     `with`(adjuster).asInstanceOf[D]
  }

  override def `with`(field: TemporalField, value: Long): D = {
     `with`(field, value).asInstanceOf[D]
  }

  override def plus(amount: TemporalAmount): D = {
     plus(amount).asInstanceOf[D]
  }

  override def plus(amountToAdd: Long, unit: TemporalUnit): D = {
    if (unit.isInstanceOf[ChronoUnit]) {
      val f: ChronoUnit = unit.asInstanceOf[ChronoUnit]
      f match {
        case DAYS =>
           plusDays(amountToAdd)
        case WEEKS =>
           plusDays(Math.multiplyExact(amountToAdd, 7))
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
     plus(amountToAdd, unit).asInstanceOf[D]
  }

  override def minus(amount: TemporalAmount): D = {
     minus(amount).asInstanceOf[D]
  }

  override def minus(amountToSubtract: Long, unit: TemporalUnit): D = {
     minus(amountToSubtract, unit).asInstanceOf[D]
  }

  /**
   * Returns a copy of this date with the specified period in years added.
   * <p>
   * This adds the specified period in years to the date.
   * In some cases, adding years can cause the resulting date to become invalid.
   * If this occurs, then other fields, typically the day-of-month, will be adjusted to ensure
   * that the result is valid. Typically this will select the last valid day of the month.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param yearsToAdd  the years to add, may be negative
   * @return a date based on this one with the years added, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  private[calendar] def plusYears(yearsToAdd: Long): D

  /**
   * Returns a copy of this date with the specified period in months added.
   * <p>
   * This adds the specified period in months to the date.
   * In some cases, adding months can cause the resulting date to become invalid.
   * If this occurs, then other fields, typically the day-of-month, will be adjusted to ensure
   * that the result is valid. Typically this will select the last valid day of the month.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param monthsToAdd  the months to add, may be negative
   * @return a date based on this one with the months added, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  private[calendar] def plusMonths(monthsToAdd: Long): D

  /**
   * Returns a copy of this date with the specified period in weeks added.
   * <p>
   * This adds the specified period in weeks to the date.
   * In some cases, adding weeks can cause the resulting date to become invalid.
   * If this occurs, then other fields will be adjusted to ensure that the result is valid.
   * <p>
   * The default implementation uses {@link #plusDays(long)} using a 7 day week.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param weeksToAdd  the weeks to add, may be negative
   * @return a date based on this one with the weeks added, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  private[calendar] def plusWeeks(weeksToAdd: Long): D = {
     plusDays(Math.multiplyExact(weeksToAdd, 7))
  }

  /**
   * Returns a copy of this date with the specified number of days added.
   * <p>
   * This adds the specified period in days to the date.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param daysToAdd  the days to add, may be negative
   * @return a date based on this one with the days added, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  private[calendar] def plusDays(daysToAdd: Long): D

  /**
   * Returns a copy of this date with the specified period in years subtracted.
   * <p>
   * This subtracts the specified period in years to the date.
   * In some cases, subtracting years can cause the resulting date to become invalid.
   * If this occurs, then other fields, typically the day-of-month, will be adjusted to ensure
   * that the result is valid. Typically this will select the last valid day of the month.
   * <p>
   * The default implementation uses {@link #plusYears(long)}.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param yearsToSubtract  the years to subtract, may be negative
   * @return a date based on this one with the years subtracted, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  private[calendar] def minusYears(yearsToSubtract: Long): D = {
     (if (yearsToSubtract == Long.MIN_VALUE) (plusYears(Long.MAX_VALUE).asInstanceOf[ChronoLocalDateImpl[D]]).plusYears(1) else plusYears(-yearsToSubtract))
  }

  /**
   * Returns a copy of this date with the specified period in months subtracted.
   * <p>
   * This subtracts the specified period in months to the date.
   * In some cases, subtracting months can cause the resulting date to become invalid.
   * If this occurs, then other fields, typically the day-of-month, will be adjusted to ensure
   * that the result is valid. Typically this will select the last valid day of the month.
   * <p>
   * The default implementation uses {@link #plusMonths(long)}.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param monthsToSubtract  the months to subtract, may be negative
   * @return a date based on this one with the months subtracted, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  private[calendar] def minusMonths(monthsToSubtract: Long): D = {
     (if (monthsToSubtract == Long.MIN_VALUE) (plusMonths(Long.MAX_VALUE).asInstanceOf[ChronoLocalDateImpl[D]]).plusMonths(1) else plusMonths(-monthsToSubtract))
  }

  /**
   * Returns a copy of this date with the specified period in weeks subtracted.
   * <p>
   * This subtracts the specified period in weeks to the date.
   * In some cases, subtracting weeks can cause the resulting date to become invalid.
   * If this occurs, then other fields will be adjusted to ensure that the result is valid.
   * <p>
   * The default implementation uses {@link #plusWeeks(long)}.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param weeksToSubtract  the weeks to subtract, may be negative
   * @return a date based on this one with the weeks subtracted, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  private[calendar] def minusWeeks(weeksToSubtract: Long): D = {
     (if (weeksToSubtract == Long.MIN_VALUE) (plusWeeks(Long.MAX_VALUE).asInstanceOf[ChronoLocalDateImpl[D]]).plusWeeks(1) else plusWeeks(-weeksToSubtract))
  }

  /**
   * Returns a copy of this date with the specified number of days subtracted.
   * <p>
   * This subtracts the specified period in days to the date.
   * <p>
   * The default implementation uses {@link #plusDays(long)}.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param daysToSubtract  the days to subtract, may be negative
   * @return a date based on this one with the days subtracted, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  private[calendar] def minusDays(daysToSubtract: Long): D = {
     (if (daysToSubtract == Long.MIN_VALUE) (plusDays(Long.MAX_VALUE).asInstanceOf[ChronoLocalDateImpl[D]]).plusDays(1) else plusDays(-daysToSubtract))
  }

  def until(endExclusive: Temporal, unit: TemporalUnit): Long = {
    val end: ChronoLocalDate = getChronology.date(endExclusive)
    if (unit.isInstanceOf[ChronoUnit]) {
      unit.asInstanceOf[ChronoUnit] match {
        case DAYS =>
           daysUntil(end)
        case WEEKS =>
           daysUntil(end) / 7
        case MONTHS =>
           monthsUntil(end)
        case YEARS =>
           monthsUntil(end) / 12
        case DECADES =>
           monthsUntil(end) / 120
        case CENTURIES =>
           monthsUntil(end) / 1200
        case MILLENNIA =>
           monthsUntil(end) / 12000
        case ERAS =>
           end.getLong(ERA) - getLong(ERA)
      }
      throw new UnsupportedTemporalTypeException("Unsupported unit: " + unit)
    }
     unit.between(this, end)
  }

  private def daysUntil(end: ChronoLocalDate): Long = {
     end.toEpochDay - toEpochDay
  }

  private def monthsUntil(end: ChronoLocalDate): Long = {
    val range: ValueRange = getChronology.range(MONTH_OF_YEAR)
    if (range.getMaximum != 12) {
      throw new IllegalStateException("ChronoLocalDateImpl only supports Chronologies with 12 months per year")
    }
    val packed1: Long = getLong(PROLEPTIC_MONTH) * 32L + get(DAY_OF_MONTH)
    val packed2: Long = end.getLong(PROLEPTIC_MONTH) * 32L + end.get(DAY_OF_MONTH)
     (packed2 - packed1) / 32
  }

  override def equals(obj: AnyRef): Boolean = {
    if (this eq obj) {
       true
    }
    if (obj.isInstanceOf[ChronoLocalDate]) {
       compareTo(obj.asInstanceOf[ChronoLocalDate]) == 0
    }
     false
  }

  override def hashCode: Int = {
    val epDay: Long = toEpochDay
     getChronology.hashCode ^ ((epDay ^ (epDay >>> 32)).asInstanceOf[Int])
  }

  override def toString: String = {
    val yoe: Long = getLong(YEAR_OF_ERA)
    val moy: Long = getLong(MONTH_OF_YEAR)
    val dom: Long = getLong(DAY_OF_MONTH)
    val buf: StringBuilder = new StringBuilder(30)
    buf.append(getChronology.toString).append(" ").append(getEra).append(" ").append(yoe).append(if (moy < 10) "-0" else "-").append(moy).append(if (dom < 10) "-0" else "-").append(dom)
     buf.toString
  }
}

/**
 * A date-time without a time-zone in an arbitrary chronology, intended
 * for advanced globalization use cases.
 * <p>
 * <b>Most applications should declare method signatures, fields and variables
 * as {@link DateTime}, not this interface.</b>
 * <p>
 * A {@code ChronoLocalDateTime} is the abstract representation of a local date-time
 * where the {@code Chronology chronology}, or calendar system, is pluggable.
 * The date-time is defined in terms of fields expressed by {@link TemporalField},
 * where most common implementations are defined in {@link ChronoField}.
 * The chronology defines how the calendar system operates and the meaning of
 * the standard fields.
 *
 * <h3>When to use this interface</h3>
 * The design of the API encourages the use of {@code DateTime} rather than this
 * interface, even in the case where the application needs to deal with multiple
 * calendar systems. The rationale for this is explored in detail in {@link ChronoLocalDate}.
 * <p>
 * Ensure that the discussion in {@code ChronoLocalDate} has been read and understood
 * before using this interface.
 *
 * @implSpec
 * This interface must be implemented with care to ensure other classes operate correctly.
 * All implementations that can be instantiated must be final, immutable and thread-safe.
 * Subclasses should be Serializable wherever possible.
 *
 * @param <D> the concrete type for the date of this date-time
 * @since 1.8
 */
object ChronoLocalDateTime {
  /**
   * Gets a comparator that compares {@code ChronoLocalDateTime} in
   * time-line order ignoring the chronology.
   * <p>
   * This comparator differs from the comparison in {@link #compareTo} in that it
   * only compares the underlying date-time and not the chronology.
   * This allows dates in different calendar systems to be compared based
   * on the position of the date-time on the local time-line.
   * The underlying comparison is equivalent to comparing the epoch-day and nano-of-day.
   *
   * @return a comparator that compares in time-line order ignoring the chronology
   *
   * @see #isAfter
   * @see #isBefore
   * @see #isEqual
   */
  def timeLineOrder: Comparator[ChronoLocalDateTime[_]] = {
     Chronology.DATE_TIME_ORDER
  }

  /**
   * Obtains an instance of {@code ChronoLocalDateTime} from a temporal object.
   * <p>
   * This obtains a local date-time based on the specified temporal.
   * A {@code TemporalAccessor} represents an arbitrary set of date and time information,
   * which this factory converts to an instance of {@code ChronoLocalDateTime}.
   * <p>
   * The conversion extracts and combines the chronology and the date-time
   * from the temporal object. The behavior is equivalent to using
   * {@link Chronology#localDateTime(TemporalAccessor)} with the extracted chronology.
   * Implementations are permitted to perform optimizations such as accessing
   * those fields that are equivalent to the relevant objects.
   * <p>
   * This method matches the signature of the functional interface {@link TemporalQuery}
   * allowing it to be used as a query via method reference, {@code ChronoLocalDateTime::from}.
   *
   * @param temporal  the temporal object to convert, not null
   * @return the date-time, not null
   * @throws DateTimeException if unable to convert to a { @code ChronoLocalDateTime}
   * @see Chronology#localDateTime(TemporalAccessor)
   */
  def from(temporal: TemporalAccessor): ChronoLocalDateTime[_] = {
    if (temporal.isInstanceOf[ChronoLocalDateTime[_ <: ChronoLocalDate]]) {
       temporal.asInstanceOf[ChronoLocalDateTime[_]]
    }

    val chrono: Chronology = temporal.query(TemporalQuery.chronology)
    if (chrono == null) {
      throw new DateTimeException("Unable to obtain ChronoLocalDateTime from TemporalAccessor: " + temporal.getClass)
    }
     chrono.localDateTime(temporal)
  }
}

trait ChronoLocalDateTime extends Temporal with TemporalAdjuster with Comparable[ChronoLocalDateTime[_]] {
  /**
   * Gets the chronology of this date-time.
   * <p>
   * The {@code Chronology} represents the calendar system in use.
   * The era and other fields in {@link ChronoField} are defined by the chronology.
   *
   * @return the chronology, not null
   */
  def getChronology: Chronology = {
     toLocalDate.getChronology
  }

  /**
   * Gets the local date part of this date-time.
   * <p>
   * This returns a local date with the same year, month and day
   * as this date-time.
   *
   * @return the date part of this date-time, not null
   */
  def toLocalDate: D

  /**
   * Gets the local time part of this date-time.
   * <p>
   * This returns a local time with the same hour, minute, second and
   * nanosecond as this date-time.
   *
   * @return the time part of this date-time, not null
   */
  def toLocalTime: LocalTime

  /**
   * Checks if the specified field is supported.
   * <p>
   * This checks if the specified field can be queried on this date-time.
   * If false, then calling the {@link #range(TemporalField) range},
   * {@link #get(TemporalField) get} and {@link #with(TemporalField, long)}
   * methods will throw an exception.
   * <p>
   * The set of supported fields is defined by the chronology and normally includes
   * all {@code ChronoField} date and time fields.
   * <p>
   * If the field is not a {@code ChronoField}, then the result of this method
   * is obtained by invoking {@code TemporalField.isSupportedBy(TemporalAccessor)}
   * passing {@code this} as the argument.
   * Whether the field is supported is determined by the field.
   *
   * @param field  the field to check, null returns false
   * @return true if the field can be queried, false if not
   */
  def isSupported(field: TemporalField): Boolean

  /**
   * Checks if the specified unit is supported.
   * <p>
   * This checks if the specified unit can be added to or subtracted from this date-time.
   * If false, then calling the {@link #plus(long, TemporalUnit)} and
   * {@link #minus(long, TemporalUnit) minus} methods will throw an exception.
   * <p>
   * The set of supported units is defined by the chronology and normally includes
   * all {@code ChronoUnit} units except {@code FOREVER}.
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
       unit ne FOREVER
    }
     unit != null && unit.isSupportedBy(this)
  }

  /**
   * {@inheritDoc}
   * @throws DateTimeException { @inheritDoc}
   * @throws ArithmeticException { @inheritDoc}
   */
  override def `with`(adjuster: TemporalAdjuster): ChronoLocalDateTime[D] = {
     ChronoLocalDateTimeImpl.ensureValid(getChronology, Temporal.super.`with`(adjuster))
  }

  /**
   * {@inheritDoc}
   * @throws DateTimeException { @inheritDoc}
   * @throws ArithmeticException { @inheritDoc}
   */
  def `with`(field: TemporalField, newValue: Long): ChronoLocalDateTime[D]

  /**
   * {@inheritDoc}
   * @throws DateTimeException { @inheritDoc}
   * @throws ArithmeticException { @inheritDoc}
   */
  override def plus(amount: TemporalAmount): ChronoLocalDateTime[D] = {
     ChronoLocalDateTimeImpl.ensureValid(getChronology, Temporal.super.plus(amount))
  }

  /**
   * {@inheritDoc}
   * @throws DateTimeException { @inheritDoc}
   * @throws ArithmeticException { @inheritDoc}
   */
  def plus(amountToAdd: Long, unit: TemporalUnit): ChronoLocalDateTime[D]

  /**
   * {@inheritDoc}
   * @throws DateTimeException { @inheritDoc}
   * @throws ArithmeticException { @inheritDoc}
   */
  override def minus(amount: TemporalAmount): ChronoLocalDateTime[D] = {
     ChronoLocalDateTimeImpl.ensureValid(getChronology, Temporal.super.minus(amount))
  }

  /**
   * {@inheritDoc}
   * @throws DateTimeException { @inheritDoc}
   * @throws ArithmeticException { @inheritDoc}
   */
  override def minus(amountToSubtract: Long, unit: TemporalUnit): ChronoLocalDateTime[D] = {
     ChronoLocalDateTimeImpl.ensureValid(getChronology, Temporal.super.minus(amountToSubtract, unit))
  }

  /**
   * Queries this date-time using the specified query.
   * <p>
   * This queries this date-time using the specified query strategy object.
   * The {@code TemporalQuery} object defines the logic to be used to
   * obtain the result. Read the documentation of the query to understand
   * what the result of this method will be.
   * <p>
   * The result of this method is obtained by invoking the
   * {@link java.time.temporal.TemporalQuery#queryFrom(TemporalAccessor)} method on the
   * specified query passing {@code this} as the argument.
   *
   * @param <R> the type of the result
   * @param query  the query to invoke, not null
   * @return the query result, null may be returned (defined by the query)
   * @throws DateTimeException if unable to query (defined by the query)
   * @throws ArithmeticException if numeric overflow occurs (defined by the query)
   */
  override def query(query: TemporalQuery[R]): R = {
    if (query eq TemporalQuery.zoneId || query eq TemporalQuery.zone || query eq TemporalQuery.offset) {
       null
    }
    else if (query eq TemporalQuery.localTime) {
       toLocalTime.asInstanceOf[R]
    }
    else if (query eq TemporalQuery.chronology) {
       getChronology.asInstanceOf[R]
    }
    else if (query eq TemporalQuery.precision) {
       NANOS.asInstanceOf[R]
    }
     query.queryFrom(this)
  }

  /**
   * Adjusts the specified temporal object to have the same date and time as this object.
   * <p>
   * This returns a temporal object of the same observable type as the input
   * with the date and time changed to be the same as this.
   * <p>
   * The adjustment is equivalent to using {@link Temporal#with(TemporalField, long)}
   * twice, passing {@link ChronoField#EPOCH_DAY} and
   * {@link ChronoField#NANO_OF_DAY} as the fields.
   * <p>
   * In most cases, it is clearer to reverse the calling pattern by using
   * {@link Temporal#with(TemporalAdjuster)}:
   * {{{
   * // these two lines are equivalent, but the second approach is recommended
   * temporal = thisLocalDateTime.adjustInto(temporal);
   * temporal = temporal.with(thisLocalDateTime);
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
     temporal.`with`(EPOCH_DAY, toLocalDate.toEpochDay).`with`(NANO_OF_DAY, toLocalTime.toNanoOfDay)
  }

  /**
   * Formats this date-time using the specified formatter.
   * <p>
   * This date-time will be passed to the formatter to produce a string.
   * <p>
   * The default implementation must behave as follows:
   * {{{
   * return formatter.format(this);
   * }}}
   *
   * @param formatter  the formatter to use, not null
   * @return the formatted date-time string, not null
   * @throws DateTimeException if an error occurs during printing
   */
  def format(formatter: DateTimeFormatter): String = {

     formatter.format(this)
  }

  /**
   * Combines this time with a time-zone to create a {@code ChronoZonedDateTime}.
   * <p>
   * This returns a {@code ChronoZonedDateTime} formed from this date-time at the
   * specified time-zone. The result will match this date-time as closely as possible.
   * Time-zone rules, such as daylight savings, mean that not every local date-time
   * is valid for the specified zone, thus the local date-time may be adjusted.
   * <p>
   * The local date-time is resolved to a single instant on the time-line.
   * This is achieved by finding a valid offset from UTC/Greenwich for the local
   * date-time as defined by the {@link ZoneRules rules} of the zone ID.
   * <p>
   * In most cases, there is only one valid offset for a local date-time.
   * In the case of an overlap, where clocks are set back, there are two valid offsets.
   * This method uses the earlier offset typically corresponding to "summer".
   * <p>
   * In the case of a gap, where clocks jump forward, there is no valid offset.
   * Instead, the local date-time is adjusted to be later by the length of the gap.
   * For a typical one hour daylight savings change, the local date-time will be
   * moved one hour later into the offset typically corresponding to "summer".
   * <p>
   * To obtain the later offset during an overlap, call
   * {@link ChronoZonedDateTime#withLaterOffsetAtOverlap()} on the result of this method.
   *
   * @param zone  the time-zone to use, not null
   * @return the zoned date-time formed from this date-time, not null
   */
  def atZone(zone: ZoneId): ChronoZonedDateTime[D]

  /**
   * Converts this date-time to an {@code Instant}.
   * <p>
   * This combines this local date-time and the specified offset to form
   * an {@code Instant}.
   * <p>
   * This default implementation calculates from the epoch-day of the date and the
   * second-of-day of the time.
   *
   * @param offset  the offset to use for the conversion, not null
   * @return an { @code Instant} representing the same instant, not null
   */
  def toInstant(offset: ZoneOffset): Instant = {
     Instant.ofEpochSecond(toEpochSecond(offset), toLocalTime.getNano)
  }

  /**
   * Converts this date-time to the number of seconds from the epoch
   * of 1970-01-01T00:00:00Z.
   * <p>
   * This combines this local date-time and the specified offset to calculate the
   * epoch-second value, which is the number of elapsed seconds from 1970-01-01T00:00:00Z.
   * Instants on the time-line after the epoch are positive, earlier are negative.
   * <p>
   * This default implementation calculates from the epoch-day of the date and the
   * second-of-day of the time.
   *
   * @param offset  the offset to use for the conversion, not null
   * @return the number of seconds from the epoch of 1970-01-01T00:00:00Z
   */
  def toEpochSecond(offset: ZoneOffset): Long = {

    val epochDay: Long = toLocalDate.toEpochDay
    var secs: Long = epochDay * 86400 + toLocalTime.toSecondOfDay
    secs -= offset.getTotalSeconds
     secs
  }

  /**
   * Compares this date-time to another date-time, including the chronology.
   * <p>
   * The comparison is based first on the underlying time-line date-time, then
   * on the chronology.
   * It is "consistent with equals", as defined by {@link Comparable}.
   * <p>
   * For example, the following is the comparator order:
   * <ol>
   * <li>{@code 2012-12-03T12:00 (ISO)}</li>
   * <li>{@code 2012-12-04T12:00 (ISO)}</li>
   * <li>{@code 2555-12-04T12:00 (ThaiBuddhist)}</li>
   * <li>{@code 2012-12-05T12:00 (ISO)}</li>
   * </ol>
   * Values #2 and #3 represent the same date-time on the time-line.
   * When two values represent the same date-time, the chronology ID is compared to distinguish them.
   * This step is needed to make the ordering "consistent with equals".
   * <p>
   * If all the date-time objects being compared are in the same chronology, then the
   * additional chronology stage is not required and only the local date-time is used.
   * <p>
   * This default implementation performs the comparison defined above.
   *
   * @param other  the other date-time to compare to, not null
   * @return the comparator value, negative if less, positive if greater
   */
  def compareTo(other: ChronoLocalDateTime[_]): Int = {
    var cmp: Int = toLocalDate.compareTo(other.toLocalDate)
    if (cmp == 0) {
      cmp = toLocalTime.compareTo(other.toLocalTime)
      if (cmp == 0) {
        cmp = getChronology.compareTo(other.getChronology)
      }
    }
     cmp
  }

  /**
   * Checks if this date-time is after the specified date-time ignoring the chronology.
   * <p>
   * This method differs from the comparison in {@link #compareTo} in that it
   * only compares the underlying date-time and not the chronology.
   * This allows dates in different calendar systems to be compared based
   * on the time-line position.
   * <p>
   * This default implementation performs the comparison based on the epoch-day
   * and nano-of-day.
   *
   * @param other  the other date-time to compare to, not null
   * @return true if this is after the specified date-time
   */
  def isAfter(other: ChronoLocalDateTime[_]): Boolean = {
    val thisEpDay: Long = this.toLocalDate.toEpochDay
    val otherEpDay: Long = other.toLocalDate.toEpochDay
     thisEpDay > otherEpDay || (thisEpDay == otherEpDay && this.toLocalTime.toNanoOfDay > other.toLocalTime.toNanoOfDay)
  }

  /**
   * Checks if this date-time is before the specified date-time ignoring the chronology.
   * <p>
   * This method differs from the comparison in {@link #compareTo} in that it
   * only compares the underlying date-time and not the chronology.
   * This allows dates in different calendar systems to be compared based
   * on the time-line position.
   * <p>
   * This default implementation performs the comparison based on the epoch-day
   * and nano-of-day.
   *
   * @param other  the other date-time to compare to, not null
   * @return true if this is before the specified date-time
   */
  def isBefore(other: ChronoLocalDateTime[_]): Boolean = {
    val thisEpDay: Long = this.toLocalDate.toEpochDay
    val otherEpDay: Long = other.toLocalDate.toEpochDay
     thisEpDay < otherEpDay || (thisEpDay == otherEpDay && this.toLocalTime.toNanoOfDay < other.toLocalTime.toNanoOfDay)
  }

  /**
   * Checks if this date-time is equal to the specified date-time ignoring the chronology.
   * <p>
   * This method differs from the comparison in {@link #compareTo} in that it
   * only compares the underlying date and time and not the chronology.
   * This allows date-times in different calendar systems to be compared based
   * on the time-line position.
   * <p>
   * This default implementation performs the comparison based on the epoch-day
   * and nano-of-day.
   *
   * @param other  the other date-time to compare to, not null
   * @return true if the underlying date-time is equal to the specified date-time on the timeline
   */
  def isEqual(other: ChronoLocalDateTime[_]): Boolean = {
     this.toLocalTime.toNanoOfDay == other.toLocalTime.toNanoOfDay && this.toLocalDate.toEpochDay == other.toLocalDate.toEpochDay
  }

  /**
   * Checks if this date-time is equal to another date-time, including the chronology.
   * <p>
   * Compares this date-time with another ensuring that the date-time and chronology are the same.
   *
   * @param obj  the object to check, null returns false
   * @return true if this is equal to the other date
   */
  override def equals(obj: AnyRef): Boolean

  /**
   * A hash code for this date-time.
   *
   * @return a suitable hash code
   */
  override def hashCode: Int

  /**
   * Outputs this date-time as a {@code String}.
   * <p>
   * The output will include the full local date-time.
   *
   * @return a string representation of this date-time, not null
   */
  override def toString: String
}

/**
 * A date-time without a time-zone for the calendar neutral API.
 * <p>
 * {@code ChronoLocalDateTime} is an immutable date-time object that represents a date-time, often
 * viewed as year-month-day-hour-minute-second. This object can also access other
 * fields such as day-of-year, day-of-week and week-of-year.
 * <p>
 * This class stores all date and time fields, to a precision of nanoseconds.
 * It does not store or represent a time-zone. For example, the value
 * "2nd October 2007 at 13:45.30.123456789" can be stored in an {@code ChronoLocalDateTime}.
 *
 * @implSpec
 * This class is immutable and thread-safe.
 *
 * @param <D> the concrete type for the date of this date-time
 * @since 1.8
 */
object ChronoLocalDateTimeImpl {
  /**
   * Obtains an instance of {@code ChronoLocalDateTime} from a date and time.
   *
   * @param date  the local date, not null
   * @param time  the local time, not null
   * @return the local date-time, not null
   */
  private[calendar] def of(date: R, time: LocalTime): ChronoLocalDateTimeImpl[R] = {
     new ChronoLocalDateTimeImpl[R](date, time)
  }

  /**
   * Casts the {@code Temporal} to {@code ChronoLocalDateTime} ensuring it bas the specified chronology.
   *
   * @param chrono  the chronology to check for, not null
   * @param temporal   a date-time to cast, not null
   * @return the date-time checked and cast to { @code ChronoLocalDateTime}, not null
   * @throws ClassCastException if the date-time cannot be cast to ChronoLocalDateTimeImpl
   *                            or the chronology is not equal this Chronology
   */
  private[calendar] def ensureValid(chrono: Chronology, temporal: Temporal): ChronoLocalDateTimeImpl[R] = {
    val other: ChronoLocalDateTimeImpl[R] = temporal.asInstanceOf[ChronoLocalDateTimeImpl[R]]
    if ((chrono == other.getChronology) == false) {
      throw new ClassCastException("Chronology mismatch, required: " + chrono.getId + ", actual: " + other.getChronology.getId)
    }
     other
  }

  private[calendar] def readExternal(in: ObjectInput): ChronoLocalDateTime[_] = {
    val date: ChronoLocalDate = in.readObject.asInstanceOf[ChronoLocalDate]
    val time: LocalTime = in.readObject.asInstanceOf[LocalTime]
     date.atTime(time)
  }

  /**
   * Serialization version.
   */
  private final val serialVersionUID: Long = 4556003607393004514L
  /**
   * Hours per day.
   */
  private[calendar] final val HOURS_PER_DAY: Int = 24
  /**
   * Minutes per hour.
   */
  private[calendar] final val MINUTES_PER_HOUR: Int = 60
  /**
   * Minutes per day.
   */
  private[calendar] final val MINUTES_PER_DAY: Int = MINUTES_PER_HOUR * HOURS_PER_DAY
  /**
   * Seconds per minute.
   */
  private[calendar] final val SECONDS_PER_MINUTE: Int = 60
  /**
   * Seconds per hour.
   */
  private[calendar] final val SECONDS_PER_HOUR: Int = SECONDS_PER_MINUTE * MINUTES_PER_HOUR
  /**
   * Seconds per day.
   */
  private[calendar] final val SECONDS_PER_DAY: Int = SECONDS_PER_HOUR * HOURS_PER_DAY
  /**
   * Milliseconds per day.
   */
  private[calendar] final val MILLIS_PER_DAY: Long = SECONDS_PER_DAY * 1000L
  /**
   * Microseconds per day.
   */
  private[calendar] final val MICROS_PER_DAY: Long = SECONDS_PER_DAY * 1000 _000L
  /**
   * Nanos per second.
   */
  private[calendar] final val NANOS_PER_SECOND: Long = 1000 _000_000L
  /**
   * Nanos per minute.
   */
  private[calendar] final val NANOS_PER_MINUTE: Long = NANOS_PER_SECOND * SECONDS_PER_MINUTE
  /**
   * Nanos per hour.
   */
  private[calendar] final val NANOS_PER_HOUR: Long = NANOS_PER_MINUTE * MINUTES_PER_HOUR
  /**
   * Nanos per day.
   */
  private[calendar] final val NANOS_PER_DAY: Long = NANOS_PER_HOUR * HOURS_PER_DAY
}

final class ChronoLocalDateTimeImpl extends ChronoLocalDateTime[D] with Temporal with TemporalAdjuster  {
  /**
   * Constructor.
   *
   * @param date  the date part of the date-time, not null
   * @param time  the time part of the date-time, not null
   */
  private def this(date: D, time: LocalTime) {



    this.date = date
    this.time = time
  }

  /**
   * Returns a copy of this date-time with the new date and time, checking
   * to see if a new object is in fact required.
   *
   * @param newDate  the date of the new date-time, not null
   * @param newTime  the time of the new date-time, not null
   * @return the date-time, not null
   */
  private def `with`(newDate: Temporal, newTime: LocalTime): ChronoLocalDateTimeImpl[D] = {
    if (date eq newDate && time eq newTime) {
       this
    }
    val cd: D = ChronoLocalDateImpl.ensureValid(date.getChronology, newDate)
     new ChronoLocalDateTimeImpl[D](cd, newTime)
  }

  def toLocalDate: D = {
     date
  }

  def toLocalTime: LocalTime = {
     time
  }

  def isSupported(field: TemporalField): Boolean = {
    if (field.isInstanceOf[ChronoField]) {
      val f: ChronoField = field.asInstanceOf[ChronoField]
       f.isDateBased || f.isTimeBased
    }
     field != null && field.isSupportedBy(this)
  }

  override def range(field: TemporalField): ValueRange = {
    if (field.isInstanceOf[ChronoField]) {
      val f: ChronoField = field.asInstanceOf[ChronoField]
       (if (f.isTimeBased) time.range(field) else date.range(field))
    }
     field.rangeRefinedBy(this)
  }

  override def get(field: TemporalField): Int = {
    if (field.isInstanceOf[ChronoField]) {
      val f: ChronoField = field.asInstanceOf[ChronoField]
       (if (f.isTimeBased) time.get(field) else date.get(field))
    }
     range(field).checkValidIntValue(getLong(field), field)
  }

  def getLong(field: TemporalField): Long = {
    if (field.isInstanceOf[ChronoField]) {
      val f: ChronoField = field.asInstanceOf[ChronoField]
       (if (f.isTimeBased) time.getLong(field) else date.getLong(field))
    }
     field.getFrom(this)
  }

  override def `with`(adjuster: TemporalAdjuster): ChronoLocalDateTimeImpl[D] = {
    if (adjuster.isInstanceOf[ChronoLocalDate]) {
       `with`(adjuster.asInstanceOf[ChronoLocalDate], time)
    }
    else if (adjuster.isInstanceOf[LocalTime]) {
       `with`(date, adjuster.asInstanceOf[LocalTime])
    }
    else if (adjuster.isInstanceOf[ChronoLocalDateTimeImpl[_ <: ChronoLocalDate]]) {
       ChronoLocalDateTimeImpl.ensureValid(date.getChronology, adjuster.asInstanceOf[ChronoLocalDateTimeImpl[_]])
    }
     ChronoLocalDateTimeImpl.ensureValid(date.getChronology, adjuster.adjustInto(this).asInstanceOf[ChronoLocalDateTimeImpl[_]])
  }

  def `with`(field: TemporalField, newValue: Long): ChronoLocalDateTimeImpl[D] = {
    if (field.isInstanceOf[ChronoField]) {
      val f: ChronoField = field.asInstanceOf[ChronoField]
      if (f.isTimeBased) {
         `with`(date, time.`with`(field, newValue))
      }
      else {
         `with`(date.`with`(field, newValue), time)
      }
    }
     ChronoLocalDateTimeImpl.ensureValid(date.getChronology, field.adjustInto(this, newValue))
  }

  def plus(amountToAdd: Long, unit: TemporalUnit): ChronoLocalDateTimeImpl[D] = {
    if (unit.isInstanceOf[ChronoUnit]) {
      val f: ChronoUnit = unit.asInstanceOf[ChronoUnit]
      f match {
        case NANOS =>
           plusNanos(amountToAdd)
        case MICROS =>
           plusDays(amountToAdd / MICROS_PER_DAY).plusNanos((amountToAdd % MICROS_PER_DAY) * 1000)
        case MILLIS =>
           plusDays(amountToAdd / MILLIS_PER_DAY).plusNanos((amountToAdd % MILLIS_PER_DAY) * 1000000)
        case SECONDS =>
           plusSeconds(amountToAdd)
        case MINUTES =>
           plusMinutes(amountToAdd)
        case HOURS =>
           plusHours(amountToAdd)
        case HALF_DAYS =>
           plusDays(amountToAdd / 256).plusHours((amountToAdd % 256) * 12)
      }
       `with`(date.plus(amountToAdd, unit), time)
    }
     ChronoLocalDateTimeImpl.ensureValid(date.getChronology, unit.addTo(this, amountToAdd))
  }

  private def plusDays(days: Long): ChronoLocalDateTimeImpl[D] = {
     `with`(date.plus(days, ChronoUnit.DAYS), time)
  }

  private def plusHours(hours: Long): ChronoLocalDateTimeImpl[D] = {
     plusWithOverflow(date, hours, 0, 0, 0)
  }

  private def plusMinutes(minutes: Long): ChronoLocalDateTimeImpl[D] = {
     plusWithOverflow(date, 0, minutes, 0, 0)
  }

  private[chrono] def plusSeconds(seconds: Long): ChronoLocalDateTimeImpl[D] = {
     plusWithOverflow(date, 0, 0, seconds, 0)
  }

  private def plusNanos(nanos: Long): ChronoLocalDateTimeImpl[D] = {
     plusWithOverflow(date, 0, 0, 0, nanos)
  }

  private def plusWithOverflow(newDate: D, hours: Long, minutes: Long, seconds: Long, nanos: Long): ChronoLocalDateTimeImpl[D] = {
    if ((hours | minutes | seconds | nanos) == 0) {
       `with`(newDate, time)
    }
    var totDays: Long = nanos / NANOS_PER_DAY + seconds / SECONDS_PER_DAY + minutes / MINUTES_PER_DAY + hours / HOURS_PER_DAY
    var totNanos: Long = nanos % NANOS_PER_DAY + (seconds % SECONDS_PER_DAY) * NANOS_PER_SECOND + (minutes % MINUTES_PER_DAY) * NANOS_PER_MINUTE + (hours % HOURS_PER_DAY) * NANOS_PER_HOUR
    val curNoD: Long = time.toNanoOfDay
    totNanos = totNanos + curNoD
    totDays += Math.floorDiv(totNanos, NANOS_PER_DAY)
    val newNoD: Long = Math.floorMod(totNanos, NANOS_PER_DAY)
    val newTime: LocalTime = (if (newNoD == curNoD) time else LocalTime.ofNanoOfDay(newNoD))
     `with`(newDate.plus(totDays, ChronoUnit.DAYS), newTime)
  }

  def atZone(zone: ZoneId): ChronoZonedDateTime[D] = {
     ChronoZonedDateTimeImpl.ofBest(this, zone, null)
  }

  def until(endExclusive: Temporal, unit: TemporalUnit): Long = {
    val end: ChronoLocalDateTime[D] = getChronology.localDateTime(endExclusive).asInstanceOf[ChronoLocalDateTime[D]]
    if (unit.isInstanceOf[ChronoUnit]) {
      if (unit.isTimeBased) {
        var amount: Long = end.getLong(EPOCH_DAY) - date.getLong(EPOCH_DAY)
        unit.asInstanceOf[ChronoUnit] match {
          case NANOS =>
            amount = Math.multiplyExact(amount, NANOS_PER_DAY)
            break //todo: break is not supported
          case MICROS =>
            amount = Math.multiplyExact(amount, MICROS_PER_DAY)
            break //todo: break is not supported
          case MILLIS =>
            amount = Math.multiplyExact(amount, MILLIS_PER_DAY)
            break //todo: break is not supported
          case SECONDS =>
            amount = Math.multiplyExact(amount, SECONDS_PER_DAY)
            break //todo: break is not supported
          case MINUTES =>
            amount = Math.multiplyExact(amount, MINUTES_PER_DAY)
            break //todo: break is not supported
          case HOURS =>
            amount = Math.multiplyExact(amount, HOURS_PER_DAY)
            break //todo: break is not supported
          case HALF_DAYS =>
            amount = Math.multiplyExact(amount, 2)
            break //todo: break is not supported
        }
         Math.addExact(amount, time.until(end.toLocalTime, unit))
      }
      var endDate: ChronoLocalDate = end.toLocalDate
      if (end.toLocalTime.isBefore(time)) {
        endDate = endDate.minus(1, ChronoUnit.DAYS)
      }
       date.until(endDate, unit)
    }
     unit.between(this, end)
  }

  private def writeReplace: AnyRef = {
     new Ser(Ser.CHRONO_LOCAL_DATE_TIME_TYPE, this)
  }

  /**
   * Defend against malicious streams.
   * @return never
   * @throws InvalidObjectException always
   */
  private def readResolve: AnyRef = {
    throw new InvalidObjectException("Deserialization via serialization delegate")
  }

  private[chrono] def writeExternal(out: ObjectOutput) {
    out.writeObject(date)
    out.writeObject(time)
  }

  override def equals(obj: AnyRef): Boolean = {
    if (this eq obj) {
       true
    }
    if (obj.isInstanceOf[ChronoLocalDateTime[_ <: ChronoLocalDate]]) {
       compareTo(obj.asInstanceOf[ChronoLocalDateTime[_]]) == 0
    }
     false
  }

  override def hashCode: Int = {
     toLocalDate.hashCode ^ toLocalTime.hashCode
  }

  override def toString: String = {
     toLocalDate.toString + 'T' + toLocalTime.toString
  }

  /**
   * The date part.
   */
  private final val date: D = null
  /**
   * The time part.
   */
  private final val time: LocalTime = null
}

/**
 * A date-time with a time-zone in an arbitrary chronology,
 * intended for advanced globalization use cases.
 * <p>
 * <b>Most applications should declare method signatures, fields and variables
 * as {@link ZonedDateTime}, not this interface.</b>
 * <p>
 * A {@code ChronoZonedDateTime} is the abstract representation of an offset date-time
 * where the {@code Chronology chronology}, or calendar system, is pluggable.
 * The date-time is defined in terms of fields expressed by {@link TemporalField},
 * where most common implementations are defined in {@link ChronoField}.
 * The chronology defines how the calendar system operates and the meaning of
 * the standard fields.
 *
 * <h3>When to use this interface</h3>
 * The design of the API encourages the use of {@code ZonedDateTime} rather than this
 * interface, even in the case where the application needs to deal with multiple
 * calendar systems. The rationale for this is explored in detail in {@link ChronoLocalDate}.
 * <p>
 * Ensure that the discussion in {@code ChronoLocalDate} has been read and understood
 * before using this interface.
 *
 * @implSpec
 * This interface must be implemented with care to ensure other classes operate correctly.
 * All implementations that can be instantiated must be final, immutable and thread-safe.
 * Subclasses should be Serializable wherever possible.
 *
 * @param <D> the concrete type for the date of this date-time
 * @since 1.8
 */
object ChronoZonedDateTime {
  /**
   * Gets a comparator that compares {@code ChronoZonedDateTime} in
   * time-line order ignoring the chronology.
   * <p>
   * This comparator differs from the comparison in {@link #compareTo} in that it
   * only compares the underlying instant and not the chronology.
   * This allows dates in different calendar systems to be compared based
   * on the position of the date-time on the instant time-line.
   * The underlying comparison is equivalent to comparing the epoch-second and nano-of-second.
   *
   * @return a comparator that compares in time-line order ignoring the chronology
   *
   * @see #isAfter
   * @see #isBefore
   * @see #isEqual
   */
  def timeLineOrder: Comparator[ChronoZonedDateTime[_]] = {
     Chronology.INSTANT_ORDER
  }

  /**
   * Obtains an instance of {@code ChronoZonedDateTime} from a temporal object.
   * <p>
   * This creates a zoned date-time based on the specified temporal.
   * A {@code TemporalAccessor} represents an arbitrary set of date and time information,
   * which this factory converts to an instance of {@code ChronoZonedDateTime}.
   * <p>
   * The conversion extracts and combines the chronology, date, time and zone
   * from the temporal object. The behavior is equivalent to using
   * {@link Chronology#zonedDateTime(TemporalAccessor)} with the extracted chronology.
   * Implementations are permitted to perform optimizations such as accessing
   * those fields that are equivalent to the relevant objects.
   * <p>
   * This method matches the signature of the functional interface {@link TemporalQuery}
   * allowing it to be used as a query via method reference, {@code ChronoZonedDateTime::from}.
   *
   * @param temporal  the temporal objec t to convert, not null
   * @return the date-time, not null
   * @throws DateTimeException if unable to convert to a { @code ChronoZonedDateTime}
   * @see Chronology#zonedDateTime(TemporalAccessor)
   */
  def from(temporal: TemporalAccessor): ChronoZonedDateTime[_] = {
    if (temporal.isInstanceOf[ChronoZonedDateTime[_ <: ChronoLocalDate]]) {
       temporal.asInstanceOf[ChronoZonedDateTime[_]]
    }

    val chrono: Chronology = temporal.query(TemporalQuery.chronology)
    if (chrono == null) {
      throw new DateTimeException("Unable to obtain ChronoZonedDateTime from TemporalAccessor: " + temporal.getClass)
    }
     chrono.zonedDateTime(temporal)
  }
}

trait ChronoZonedDateTime extends Temporal with Comparable[ChronoZonedDateTime[_]] {
  override def range(field: TemporalField): ValueRange = {
    if (field.isInstanceOf[ChronoField]) {
      if (field eq INSTANT_SECONDS || field eq OFFSET_SECONDS) {
         field.range
      }
       toLocalDateTime.range(field)
    }
     field.rangeRefinedBy(this)
  }

  override def get(field: TemporalField): Int = {
    if (field.isInstanceOf[ChronoField]) {
      field.asInstanceOf[ChronoField] match {
        case INSTANT_SECONDS =>
          throw new UnsupportedTemporalTypeException("Invalid field 'InstantSeconds' for get() method, use getLong() instead")
        case OFFSET_SECONDS =>
           getOffset.getTotalSeconds
      }
       toLocalDateTime.get(field)
    }
     Temporal.super.get(field)
  }

  def getLong(field: TemporalField): Long = {
    if (field.isInstanceOf[ChronoField]) {
      field.asInstanceOf[ChronoField] match {
        case INSTANT_SECONDS =>
           toEpochSecond
        case OFFSET_SECONDS =>
           getOffset.getTotalSeconds
      }
       toLocalDateTime.getLong(field)
    }
     field.getFrom(this)
  }

  /**
   * Gets the local date part of this date-time.
   * <p>
   * This returns a local date with the same year, month and day
   * as this date-time.
   *
   * @return the date part of this date-time, not null
   */
  def toLocalDate: D = {
     toLocalDateTime.toLocalDate
  }

  /**
   * Gets the local time part of this date-time.
   * <p>
   * This returns a local time with the same hour, minute, second and
   * nanosecond as this date-time.
   *
   * @return the time part of this date-time, not null
   */
  def toLocalTime: LocalTime = {
     toLocalDateTime.toLocalTime
  }

  /**
   * Gets the local date-time part of this date-time.
   * <p>
   * This returns a local date with the same year, month and day
   * as this date-time.
   *
   * @return the local date-time part of this date-time, not null
   */
  def toLocalDateTime: ChronoLocalDateTime[D]

  /**
   * Gets the chronology of this date-time.
   * <p>
   * The {@code Chronology} represents the calendar system in use.
   * The era and other fields in {@link ChronoField} are defined by the chronology.
   *
   * @return the chronology, not null
   */
  def getChronology: Chronology = {
     toLocalDate.getChronology
  }

  /**
   * Gets the zone offset, such as '+01:00'.
   * <p>
   * This is the offset of the local date-time from UTC/Greenwich.
   *
   * @return the zone offset, not null
   */
  def getOffset: ZoneOffset

  /**
   * Gets the zone ID, such as 'Europe/Paris'.
   * <p>
   * This returns the stored time-zone id used to determine the time-zone rules.
   *
   * @return the zone ID, not null
   */
  def getZone: ZoneId

  /**
   * Returns a copy of this date-time changing the zone offset to the
   * earlier of the two valid offsets at a local time-line overlap.
   * <p>
   * This method only has any effect when the local time-line overlaps, such as
   * at an autumn daylight savings cutover. In this scenario, there are two
   * valid offsets for the local date-time. Calling this method will return
   * a zoned date-time with the earlier of the two selected.
   * <p>
   * If this method is called when it is not an overlap, {@code this}
   * is returned.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @return a { @code ZoneChronoDateTime} based on this date-time with the earlier offset, not null
   * @throws DateTimeException if no rules can be found for the zone
   * @throws DateTimeException if no rules are valid for this date-time
   */
  def withEarlierOffsetAtOverlap: ChronoZonedDateTime[D]

  /**
   * Returns a copy of this date-time changing the zone offset to the
   * later of the two valid offsets at a local time-line overlap.
   * <p>
   * This method only has any effect when the local time-line overlaps, such as
   * at an autumn daylight savings cutover. In this scenario, there are two
   * valid offsets for the local date-time. Calling this method will return
   * a zoned date-time with the later of the two selected.
   * <p>
   * If this method is called when it is not an overlap, {@code this}
   * is returned.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @return a { @code ChronoZonedDateTime} based on this date-time with the later offset, not null
   * @throws DateTimeException if no rules can be found for the zone
   * @throws DateTimeException if no rules are valid for this date-time
   */
  def withLaterOffsetAtOverlap: ChronoZonedDateTime[D]

  /**
   * Returns a copy of this ZonedDateTime with a different time-zone,
   * retaining the local date-time if possible.
   * <p>
   * This method changes the time-zone and retains the local date-time.
   * The local date-time is only changed if it is invalid for the new zone.
   * <p>
   * To change the zone and adjust the local date-time,
   * use {@link #withZoneSameInstant(ZoneId)}.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param zone  the time-zone to change to, not null
   * @return a { @code ChronoZonedDateTime} based on this date-time with the requested zone, not null
   */
  def withZoneSameLocal(zone: ZoneId): ChronoZonedDateTime[D]

  /**
   * Returns a copy of this date-time with a different time-zone,
   * retaining the instant.
   * <p>
   * This method changes the time-zone and retains the instant.
   * This normally results in a change to the local date-time.
   * <p>
   * This method is based on retaining the same instant, thus gaps and overlaps
   * in the local time-line have no effect on the result.
   * <p>
   * To change the offset while keeping the local time,
   * use {@link #withZoneSameLocal(ZoneId)}.
   *
   * @param zone  the time-zone to change to, not null
   * @return a { @code ChronoZonedDateTime} based on this date-time with the requested zone, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def withZoneSameInstant(zone: ZoneId): ChronoZonedDateTime[D]

  /**
   * Checks if the specified field is supported.
   * <p>
   * This checks if the specified field can be queried on this date-time.
   * If false, then calling the {@link #range(TemporalField) range},
   * {@link #get(TemporalField) get} and {@link #with(TemporalField, long)}
   * methods will throw an exception.
   * <p>
   * The set of supported fields is defined by the chronology and normally includes
   * all {@code ChronoField} fields.
   * <p>
   * If the field is not a {@code ChronoField}, then the result of this method
   * is obtained by invoking {@code TemporalField.isSupportedBy(TemporalAccessor)}
   * passing {@code this} as the argument.
   * Whether the field is supported is determined by the field.
   *
   * @param field  the field to check, null returns false
   * @return true if the field can be queried, false if not
   */
  def isSupported(field: TemporalField): Boolean

  /**
   * Checks if the specified unit is supported.
   * <p>
   * This checks if the specified unit can be added to or subtracted from this date-time.
   * If false, then calling the {@link #plus(long, TemporalUnit)} and
   * {@link #minus(long, TemporalUnit) minus} methods will throw an exception.
   * <p>
   * The set of supported units is defined by the chronology and normally includes
   * all {@code ChronoUnit} units except {@code FOREVER}.
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
       unit ne FOREVER
    }
     unit != null && unit.isSupportedBy(this)
  }

  /**
   * {@inheritDoc}
   * @throws DateTimeException { @inheritDoc}
   * @throws ArithmeticException { @inheritDoc}
   */
  override def `with`(adjuster: TemporalAdjuster): ChronoZonedDateTime[D] = {
     ChronoZonedDateTimeImpl.ensureValid(getChronology, Temporal.super.`with`(adjuster))
  }

  /**
   * {@inheritDoc}
   * @throws DateTimeException { @inheritDoc}
   * @throws ArithmeticException { @inheritDoc}
   */
  def `with`(field: TemporalField, newValue: Long): ChronoZonedDateTime[D]

  /**
   * {@inheritDoc}
   * @throws DateTimeException { @inheritDoc}
   * @throws ArithmeticException { @inheritDoc}
   */
  override def plus(amount: TemporalAmount): ChronoZonedDateTime[D] = {
     ChronoZonedDateTimeImpl.ensureValid(getChronology, Temporal.super.plus(amount))
  }

  /**
   * {@inheritDoc}
   * @throws DateTimeException { @inheritDoc}
   * @throws ArithmeticException { @inheritDoc}
   */
  def plus(amountToAdd: Long, unit: TemporalUnit): ChronoZonedDateTime[D]

  /**
   * {@inheritDoc}
   * @throws DateTimeException { @inheritDoc}
   * @throws ArithmeticException { @inheritDoc}
   */
  override def minus(amount: TemporalAmount): ChronoZonedDateTime[D] = {
     ChronoZonedDateTimeImpl.ensureValid(getChronology, Temporal.super.minus(amount))
  }

  /**
   * {@inheritDoc}
   * @throws DateTimeException { @inheritDoc}
   * @throws ArithmeticException { @inheritDoc}
   */
  override def minus(amountToSubtract: Long, unit: TemporalUnit): ChronoZonedDateTime[D] = {
     ChronoZonedDateTimeImpl.ensureValid(getChronology, Temporal.super.minus(amountToSubtract, unit))
  }

  /**
   * Queries this date-time using the specified query.
   * <p>
   * This queries this date-time using the specified query strategy object.
   * The {@code TemporalQuery} object defines the logic to be used to
   * obtain the result. Read the documentation of the query to understand
   * what the result of this method will be.
   * <p>
   * The result of this method is obtained by invoking the
   * {@link java.time.temporal.TemporalQuery#queryFrom(TemporalAccessor)} method on the
   * specified query passing {@code this} as the argument.
   *
   * @param <R> the type of the result
   * @param query  the query to invoke, not null
   * @return the query result, null may be returned (defined by the query)
   * @throws DateTimeException if unable to query (defined by the query)
   * @throws ArithmeticException if numeric overflow occurs (defined by the query)
   */
  override def query(query: TemporalQuery[R]): R = {
    if (query eq TemporalQuery.zone || query eq TemporalQuery.zoneId) {
       getZone.asInstanceOf[R]
    }
    else if (query eq TemporalQuery.offset) {
       getOffset.asInstanceOf[R]
    }
    else if (query eq TemporalQuery.localTime) {
       toLocalTime.asInstanceOf[R]
    }
    else if (query eq TemporalQuery.chronology) {
       getChronology.asInstanceOf[R]
    }
    else if (query eq TemporalQuery.precision) {
       NANOS.asInstanceOf[R]
    }
     query.queryFrom(this)
  }

  /**
   * Formats this date-time using the specified formatter.
   * <p>
   * This date-time will be passed to the formatter to produce a string.
   * <p>
   * The default implementation must behave as follows:
   * {{{
   * return formatter.format(this);
   * }}}
   *
   * @param formatter  the formatter to use, not null
   * @return the formatted date-time string, not null
   * @throws DateTimeException if an error occurs during printing
   */
  def format(formatter: DateTimeFormatter): String = {

     formatter.format(this)
  }

  /**
   * Converts this date-time to an {@code Instant}.
   * <p>
   * This returns an {@code Instant} representing the same point on the
   * time-line as this date-time. The calculation combines the
   * {@linkplain #toLocalDateTime() local date-time} and
   * {@linkplain #getOffset() offset}.
   *
   * @return an { @code Instant} representing the same instant, not null
   */
  def toInstant: Instant = {
     Instant.ofEpochSecond(toEpochSecond, toLocalTime.getNano)
  }

  /**
   * Converts this date-time to the number of seconds from the epoch
   * of 1970-01-01T00:00:00Z.
   * <p>
   * This uses the {@linkplain #toLocalDateTime() local date-time} and
   * {@linkplain #getOffset() offset} to calculate the epoch-second value,
   * which is the number of elapsed seconds from 1970-01-01T00:00:00Z.
   * Instants on the time-line after the epoch are positive, earlier are negative.
   *
   * @return the number of seconds from the epoch of 1970-01-01T00:00:00Z
   */
  def toEpochSecond: Long = {
    val epochDay: Long = toLocalDate.toEpochDay
    var secs: Long = epochDay * 86400 + toLocalTime.toSecondOfDay
    secs -= getOffset.getTotalSeconds
     secs
  }

  /**
   * Compares this date-time to another date-time, including the chronology.
   * <p>
   * The comparison is based first on the instant, then on the local date-time,
   * then on the zone ID, then on the chronology.
   * It is "consistent with equals", as defined by {@link Comparable}.
   * <p>
   * If all the date-time objects being compared are in the same chronology, then the
   * additional chronology stage is not required.
   * <p>
   * This default implementation performs the comparison defined above.
   *
   * @param other  the other date-time to compare to, not null
   * @return the comparator value, negative if less, positive if greater
   */
  def compareTo(other: ChronoZonedDateTime[_]): Int = {
    var cmp: Int = Long.compare(toEpochSecond, other.toEpochSecond)
    if (cmp == 0) {
      cmp = toLocalTime.getNano - other.toLocalTime.getNano
      if (cmp == 0) {
        cmp = toLocalDateTime.compareTo(other.toLocalDateTime)
        if (cmp == 0) {
          cmp = getZone.getId.compareTo(other.getZone.getId)
          if (cmp == 0) {
            cmp = getChronology.compareTo(other.getChronology)
          }
        }
      }
    }
     cmp
  }

  /**
   * Checks if the instant of this date-time is before that of the specified date-time.
   * <p>
   * This method differs from the comparison in {@link #compareTo} in that it
   * only compares the instant of the date-time. This is equivalent to using
   * {@code dateTime1.toInstant().isBefore(dateTime2.toInstant());}.
   * <p>
   * This default implementation performs the comparison based on the epoch-second
   * and nano-of-second.
   *
   * @param other  the other date-time to compare to, not null
   * @return true if this point is before the specified date-time
   */
  def isBefore(other: ChronoZonedDateTime[_]): Boolean = {
    val thisEpochSec: Long = toEpochSecond
    val otherEpochSec: Long = other.toEpochSecond
     thisEpochSec < otherEpochSec || (thisEpochSec == otherEpochSec && toLocalTime.getNano < other.toLocalTime.getNano)
  }

  /**
   * Checks if the instant of this date-time is after that of the specified date-time.
   * <p>
   * This method differs from the comparison in {@link #compareTo} in that it
   * only compares the instant of the date-time. This is equivalent to using
   * {@code dateTime1.toInstant().isAfter(dateTime2.toInstant());}.
   * <p>
   * This default implementation performs the comparison based on the epoch-second
   * and nano-of-second.
   *
   * @param other  the other date-time to compare to, not null
   * @return true if this is after the specified date-time
   */
  def isAfter(other: ChronoZonedDateTime[_]): Boolean = {
    val thisEpochSec: Long = toEpochSecond
    val otherEpochSec: Long = other.toEpochSecond
     thisEpochSec > otherEpochSec || (thisEpochSec == otherEpochSec && toLocalTime.getNano > other.toLocalTime.getNano)
  }

  /**
   * Checks if the instant of this date-time is equal to that of the specified date-time.
   * <p>
   * This method differs from the comparison in {@link #compareTo} and {@link #equals}
   * in that it only compares the instant of the date-time. This is equivalent to using
   * {@code dateTime1.toInstant().equals(dateTime2.toInstant());}.
   * <p>
   * This default implementation performs the comparison based on the epoch-second
   * and nano-of-second.
   *
   * @param other  the other date-time to compare to, not null
   * @return true if the instant equals the instant of the specified date-time
   */
  def isEqual(other: ChronoZonedDateTime[_]): Boolean = {
     toEpochSecond == other.toEpochSecond && toLocalTime.getNano == other.toLocalTime.getNano
  }

  /**
   * Checks if this date-time is equal to another date-time.
   * <p>
   * The comparison is based on the offset date-time and the zone.
   * To compare for the same instant on the time-line, use {@link #compareTo}.
   * Only objects of type {@code ChronoZonedDateTime} are compared, other types return false.
   *
   * @param obj  the object to check, null returns false
   * @return true if this is equal to the other date-time
   */
  override def equals(obj: AnyRef): Boolean

  /**
   * A hash code for this date-time.
   *
   * @return a suitable hash code
   */
  override def hashCode: Int

  /**
   * Outputs this date-time as a {@code String}.
   * <p>
   * The output will include the full zoned date-time.
   *
   * @return a string representation of this date-time, not null
   */
  override def toString: String
}

/**
 * A date-time with a time-zone in the calendar neutral API.
 * <p>
 * {@code ZoneChronoDateTime} is an immutable representation of a date-time with a time-zone.
 * This class stores all date and time fields, to a precision of nanoseconds,
 * as well as a time-zone and zone offset.
 * <p>
 * The purpose of storing the time-zone is to distinguish the ambiguous case where
 * the local time-line overlaps, typically as a result of the end of daylight time.
 * Information about the local-time can be obtained using methods on the time-zone.
 *
 * @implSpec
 * This class is immutable and thread-safe.
 *
 * @param <D> the concrete type for the date of this date-time
 * @since 1.8
 */
object ChronoZonedDateTimeImpl {
  /**
   * Obtains an instance from a local date-time using the preferred offset if possible.
   *
   * @param localDateTime  the local date-time, not null
   * @param zone  the zone identifier, not null
   * @param preferredOffset  the zone offset, null if no preference
   * @return the zoned date-time, not null
   */
  private[calendar] def ofBest(localDateTime: ChronoLocalDateTimeImpl[R], zone: ZoneId, preferredOffset: ZoneOffset): ChronoZonedDateTime[R] = {


    if (zone.isInstanceOf[ZoneOffset]) {
       new ChronoZonedDateTimeImpl[R](localDateTime, zone.asInstanceOf[ZoneOffset], zone)
    }
    val rules: ZoneRules = zone.getRules
    val isoLDT: LocalDateTime = LocalDateTime.from(localDateTime)
    val validOffsets: List[ZoneOffset] = rules.getValidOffsets(isoLDT)
    var offset: ZoneOffset = null
    if (validOffsets.size == 1) {
      offset = validOffsets.get(0)
    }
    else if (validOffsets.size == 0) {
      val trans: ZoneOffsetTransition = rules.getTransition(isoLDT)
      localDateTime = localDateTime.plusSeconds(trans.getDuration.getSeconds)
      offset = trans.getOffsetAfter
    }
    else {
      if (preferredOffset != null && validOffsets.contains(preferredOffset)) {
        offset = preferredOffset
      }
      else {
        offset = validOffsets.get(0)
      }
    }

     new ChronoZonedDateTimeImpl[R](localDateTime, offset, zone)
  }

  /**
   * Obtains an instance from an instant using the specified time-zone.
   *
   * @param chrono  the chronology, not null
   * @param instant  the instant, not null
   * @param zone  the zone identifier, not null
   * @return the zoned date-time, not null
   */
  private[calendar] def ofInstant(chrono: Chronology, instant: Instant, zone: ZoneId): ChronoZonedDateTimeImpl[_] = {
    val rules: ZoneRules = zone.getRules
    val offset: ZoneOffset = rules.getOffset(instant)

    val ldt: LocalDateTime = LocalDateTime.ofEpochSecond(instant.getEpochSecond, instant.getNano, offset)
    val cldt: ChronoLocalDateTimeImpl[_] = chrono.localDateTime(ldt).asInstanceOf[ChronoLocalDateTimeImpl[_]]
     new ChronoZonedDateTimeImpl[_ <: ChronoLocalDate](cldt, offset, zone)
  }

  /**
   * Casts the {@code Temporal} to {@code ChronoZonedDateTimeImpl} ensuring it bas the specified chronology.
   *
   * @param chrono  the chronology to check for, not null
   * @param temporal  a date-time to cast, not null
   * @return the date-time checked and cast to { @code ChronoZonedDateTimeImpl}, not null
   * @throws ClassCastException if the date-time cannot be cast to ChronoZonedDateTimeImpl
   *                            or the chronology is not equal this Chronology
   */
  private[calendar] def ensureValid(chrono: Chronology, temporal: Temporal): ChronoZonedDateTimeImpl[R] = {
    val other: ChronoZonedDateTimeImpl[R] = temporal.asInstanceOf[ChronoZonedDateTimeImpl[R]]
    if ((chrono == other.getChronology) == false) {
      throw new ClassCastException("Chronology mismatch, required: " + chrono.getId + ", actual: " + other.getChronology.getId)
    }
     other
  }

  private[calendar] def readExternal(in: ObjectInput): ChronoZonedDateTime[_] = {
    val dateTime: ChronoLocalDateTime[_] = in.readObject.asInstanceOf[ChronoLocalDateTime[_]]
    val offset: ZoneOffset = in.readObject.asInstanceOf[ZoneOffset]
    val zone: ZoneId = in.readObject.asInstanceOf[ZoneId]
     dateTime.atZone(offset).withZoneSameLocal(zone)
  }


}

final class ChronoZonedDateTimeImpl extends ChronoZonedDateTime[D]  {
  /**
   * Obtains an instance from an {@code Instant}.
   *
   * @param instant  the instant to create the date-time from, not null
   * @param zone  the time-zone to use, validated not null
   * @return the zoned date-time, validated not null
   */
  private def create(instant: Instant, zone: ZoneId): ChronoZonedDateTimeImpl[D] = {
     ofInstant(getChronology, instant, zone).asInstanceOf[ChronoZonedDateTimeImpl[D]]
  }

  /**
   * Constructor.
   *
   * @param dateTime  the date-time, not null
   * @param offset  the zone offset, not null
   * @param zone  the zone ID, not null
   */
  private def this(dateTime: ChronoLocalDateTimeImpl[D], offset: ZoneOffset, zone: ZoneId) {

    this.dateTime =
    this.offset =
    this.zone =
  }

  def getOffset: ZoneOffset = {
     offset
  }

  def withEarlierOffsetAtOverlap: ChronoZonedDateTime[D] = {
    val trans: ZoneOffsetTransition = getZone.getRules.getTransition(LocalDateTime.from(this))
    if (trans != null && trans.isOverlap) {
      val earlierOffset: ZoneOffset = trans.getOffsetBefore
      if ((earlierOffset == offset) == false) {
         new ChronoZonedDateTimeImpl[D](dateTime, earlierOffset, zone)
      }
    }
     this
  }

  def withLaterOffsetAtOverlap: ChronoZonedDateTime[D] = {
    val trans: ZoneOffsetTransition = getZone.getRules.getTransition(LocalDateTime.from(this))
    if (trans != null) {
      val offset: ZoneOffset = trans.getOffsetAfter
      if ((offset == getOffset) == false) {
         new ChronoZonedDateTimeImpl[D](dateTime, offset, zone)
      }
    }
     this
  }

  def toLocalDateTime: ChronoLocalDateTime[D] = {
     dateTime
  }

  def getZone: ZoneId = {
     zone
  }

  def withZoneSameLocal(zone: ZoneId): ChronoZonedDateTime[D] = {
     ofBest(dateTime, zone, offset)
  }

  def withZoneSameInstant(zone: ZoneId): ChronoZonedDateTime[D] = {

     if ((this.zone == zone)) this else create(dateTime.toInstant(offset), zone)
  }

  def isSupported(field: TemporalField): Boolean = {
     field.isInstanceOf[ChronoField] || (field != null && field.isSupportedBy(this))
  }

  def `with`(field: TemporalField, newValue: Long): ChronoZonedDateTime[D] = {
    if (field.isInstanceOf[ChronoField]) {
      val f: ChronoField = field.asInstanceOf[ChronoField]
      f match {
        case INSTANT_SECONDS =>
           plus(newValue - toEpochSecond, SECONDS)
        case OFFSET_SECONDS => {
          val offset: ZoneOffset = ZoneOffset.ofTotalSeconds(f.checkValidIntValue(newValue))
           create(dateTime.toInstant(offset), zone)
        }
      }
       ofBest(dateTime.`with`(field, newValue), zone, offset)
    }
     ChronoZonedDateTimeImpl.ensureValid(getChronology, field.adjustInto(this, newValue))
  }

  def plus(amountToAdd: Long, unit: TemporalUnit): ChronoZonedDateTime[D] = {
    if (unit.isInstanceOf[ChronoUnit]) {
       `with`(dateTime.plus(amountToAdd, unit))
    }
     ChronoZonedDateTimeImpl.ensureValid(getChronology, unit.addTo(this, amountToAdd))
  }

  def until(endExclusive: Temporal, unit: TemporalUnit): Long = {
    var end: ChronoZonedDateTime[D] = getChronology.zonedDateTime(endExclusive).asInstanceOf[ChronoZonedDateTime[D]]
    if (unit.isInstanceOf[ChronoUnit]) {
      end = end.withZoneSameInstant(offset)
       dateTime.until(end.toLocalDateTime, unit)
    }
     unit.between(this, end)
  }

  private def writeReplace: AnyRef = {
     new Ser(Ser.CHRONO_ZONE_DATE_TIME_TYPE, this)
  }

  /**
   * Defend against malicious streams.
   * @return never
   * @throws InvalidObjectException always
   */
  private def readResolve: AnyRef = {
    throw new InvalidObjectException("Deserialization via serialization delegate")
  }

  private[chrono] def writeExternal(out: ObjectOutput) {
    out.writeObject(dateTime)
    out.writeObject(offset)
    out.writeObject(zone)
  }

  override def equals(obj: AnyRef): Boolean = {
    if (this eq obj) {
       true
    }
    if (obj.isInstanceOf[ChronoZonedDateTime[_ <: ChronoLocalDate]]) {
       compareTo(obj.asInstanceOf[ChronoZonedDateTime[_]]) == 0
    }
     false
  }

  override def hashCode: Int = {
     toLocalDateTime.hashCode ^ getOffset.hashCode ^ Integer.rotateLeft(getZone.hashCode, 3)
  }

  override def toString: String = {
    var str: String = toLocalDateTime.toString + getOffset.toString
    if (getOffset ne getZone) {
      str += '[' + getZone.toString + ']'
    }
     str
  }

  /**
   * The local date-time.
   */
  private final val dateTime: ChronoLocalDateTimeImpl[D] = null
  /**
   * The zone offset.
   */
  private final val offset: ZoneOffset = null
  /**
   * The zone ID.
   */
  private final val zone: ZoneId = null
}

