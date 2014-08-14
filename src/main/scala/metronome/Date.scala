package metronome

import metronome.chrono
import metronome.chrono.{Ser, Era, ChronoLocalDate, IsoChronology}
import metronome.format.DateTimeFormatter
import metronome.temporal._


/**
 * A date without a time-zone in the ISO-8601 calendar system,
 * such as {@code 2007-12-03}.
 * <p>
 * {@code Date} is an immutable date-time object that represents a date,
 * often viewed as year-month-day. Other date fields, such as day-of-year,
 * day-of-week and week-of-year, can also be accessed.
 * For example, the value "2nd October 2007" can be stored in a {@code Date}.
 * <p>
 * This class does not store or represent a time or time-zone.
 * Instead, it is a description of the date, as used for birthdays.
 * It cannot represent an instant on the time-line without additional information
 * such as an offset or time-zone.
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
object Date {


  /**
   * Obtains the current date from the system clock in the specified time-zone.
   * <p>
   * This will query the {@link Clock#system(ZoneId) system clock} to obtain the current date.
   * Specifying the time-zone avoids dependence on the default time-zone.
   * <p>
   * Using this method will prevent the ability to use an alternate clock for testing
   * because the clock is hard-coded.
   *
   * @param zone  the zone ID to use, not null
   * @return the current date using the system clock, not null
   */
  def now(zone: ZoneId): Date = {
    now(Clock.system(zone))
  }

  /**
   * Obtains the current date from the specified clock.
   * <p>
   * This will query the specified clock to obtain the current date - today.
   * Using this method allows the use of an alternate clock for testing.
   * The alternate clock may be introduced using {@link Clock dependency injection}.
   *
   * @param clock  the clock to use, not null
   * @return the current date, not null
   */
  def now(clock: Clock = Clock.systemDefaultZone): Date = {
    val now: Instant = clock.instant
    val offset: ZoneOffset = clock.getZone.getRules.getOffset(now)
    val epochSec: Long = now.getEpochSecond + offset.getTotalSeconds
    val epochDay: Long = Math.floorDiv(epochSec, SECONDS_PER_DAY)
    Date.ofEpochDay(epochDay)
  }

  /**
   * Obtains an instance of {@code Date} from a year, month and day.
   * <p>
   * This returns a {@code Date} with the specified year, month and day-of-month.
   * The day must be valid for the year and month, otherwise an exception will be thrown.
   *
   * @param year  the year to represent, from MIN_YEAR to MAX_YEAR
   * @param month  the month-of-year to represent, not null
   * @param dayOfMonth  the day-of-month to represent, from 1 to 31
   * @return the local date, not null
   * @throws DateTimeException if the value of any field is out of range,
   *                           or if the day-of-month is invalid for the month-year
   */
  def of(year: Int, month: Month, dayOfMonth: Int): Date = {
    YEAR.checkValidValue(year)
    object
    DAY_OF_MONTH.checkValidValue(dayOfMonth)
    create(year, month.getValue, dayOfMonth)
  }

  /**
   * Obtains an instance of {@code Date} from a year, month and day.
   * <p>
   * This returns a {@code Date} with the specified year, month and day-of-month.
   * The day must be valid for the year and month, otherwise an exception will be thrown.
   *
   * @param year  the year to represent, from MIN_YEAR to MAX_YEAR
   * @param month  the month-of-year to represent, from 1 (January) to 12 (December)
   * @param dayOfMonth  the day-of-month to represent, from 1 to 31
   * @return the local date, not null
   * @throws DateTimeException if the value of any field is out of range,
   *                           or if the day-of-month is invalid for the month-year
   */
  def of(year: Int, month: Int, dayOfMonth: Int): Date = {
    YEAR.checkValidValue(year)
    MONTH_OF_YEAR.checkValidValue(month)
    DAY_OF_MONTH.checkValidValue(dayOfMonth)
    create(year, month, dayOfMonth)
  }

  /**
   * Obtains an instance of {@code Date} from a year and day-of-year.
   * <p>
   * This returns a {@code Date} with the specified year and day-of-year.
   * The day-of-year must be valid for the year, otherwise an exception will be thrown.
   *
   * @param year  the year to represent, from MIN_YEAR to MAX_YEAR
   * @param dayOfYear  the day-of-year to represent, from 1 to 366
   * @return the local date, not null
   * @throws DateTimeException if the value of any field is out of range,
   *                           or if the day-of-year is invalid for the month-year
   */
  def ofYearDay(year: Int, dayOfYear: Int): Date = {
    YEAR.checkValidValue(year)
    DAY_OF_YEAR.checkValidValue(dayOfYear)
    val leap: Boolean = IsoChronology.INSTANCE.isLeapYear(year)
    if (dayOfYear == 366 && leap == false) {
      throw new DateTimeException("Invalid date 'DayOfYear 366' as '" + year + "' is not a leap year")
    }
    var moy: Month = Month.of((dayOfYear - 1) / 31 + 1)
    val monthEnd: Int = moy.firstDayOfYear(leap) + moy.length(leap) - 1
    if (dayOfYear > monthEnd) {
      moy = moy.plus(1)
    }
    val dom: Int = dayOfYear - moy.firstDayOfYear(leap) + 1
    new Date(year, moy.getValue, dom)
  }

  /**
   * Obtains an instance of {@code Date} from the epoch day count.
   * <p>
   * This returns a {@code Date} with the specified epoch-day.
   * The {@link ChronoField#EPOCH_DAY EPOCH_DAY} is a simple incrementing count
   * of days where day 0 is 1970-01-01. Negative numbers represent earlier days.
   *
   * @param epochDay  the Epoch Day to convert, based on the epoch 1970-01-01
   * @return the local date, not null
   * @throws DateTimeException if the epoch days exceeds the supported date range
   */
  def ofEpochDay(epochDay: Long): Date = {
    var zeroDay: Long = epochDay + DAYS_0000_TO_1970
    zeroDay -= 60
    var adjust: Long = 0
    if (zeroDay < 0) {
      val adjustCycles: Long = (zeroDay + 1) / DAYS_PER_CYCLE - 1
      adjust = adjustCycles * 400
      zeroDay += -adjustCycles * DAYS_PER_CYCLE
    }
    var yearEst: Long = (400 * zeroDay + 591) / DAYS_PER_CYCLE
    var doyEst: Long = zeroDay - (365 * yearEst + yearEst / 4 - yearEst / 100 + yearEst / 400)
    if (doyEst < 0) {
      yearEst -= 1
      doyEst = zeroDay - (365 * yearEst + yearEst / 4 - yearEst / 100 + yearEst / 400)
    }
    yearEst += adjust
    val marchDoy0: Int = doyEst.asInstanceOf[Int]
    val marchMonth0: Int = (marchDoy0 * 5 + 2) / 153
    val month: Int = (marchMonth0 + 2) % 12 + 1
    val dom: Int = marchDoy0 - (marchMonth0 * 306 + 5) / 10 + 1
    yearEst += marchMonth0 / 10
    val year: Int = YEAR.checkValidIntValue(yearEst)
    new Date(year, month, dom)
  }

  /**
   * Obtains an instance of {@code Date} from a temporal object.
   * <p>
   * This obtains a local date based on the specified temporal.
   * A {@code TemporalAccessor} represents an arbitrary set of date and time information,
   * which this factory converts to an instance of {@code Date}.
   * <p>
   * The conversion uses the {@link TemporalQuery#localDate()} query, which relies
   * on extracting the {@link ChronoField#EPOCH_DAY EPOCH_DAY} field.
   * <p>
   * This method matches the signature of the functional interface {@link TemporalQuery}
   * allowing it to be used as a query via method reference, {@code Date::from}.
   *
   * @param temporal  the temporal object to convert, not null
   * @return the local date, not null
   * @throws DateTimeException if unable to convert to a { @code Date}
   */
  def from(temporal: TemporalAccessor): Date = {
    val date: Date = temporal.query(TemporalQuery.localDate)
    if (date == null) {
      throw new DateTimeException("Unable to obtain Date from TemporalAccessor: " + temporal.getClass)
    }
    date
  }

  /**
   * Obtains an instance of {@code Date} from a text string such as {@code 2007-12-03}.
   * <p>
   * The string must represent a valid date and is parsed using
   * {@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE}.
   *
   * @param text  the text to parse such as "2007-12-03", not null
   * @return the parsed local date, not null
   * @throws DateTimeParseException if the text cannot be parsed
   */
  def parse(text: CharSequence): Date = {
    parse(text, DateTimeFormatter.ISO_LOCAL_DATE)
  }

  /**
   * Obtains an instance of {@code Date} from a text string using a specific formatter.
   * <p>
   * The text is parsed using the formatter, returning a date.
   *
   * @param text  the text to parse, not null
   * @param formatter  the formatter to use, not null
   * @return the parsed local date, not null
   * @throws DateTimeParseException if the text cannot be parsed
   */
  def parse(text: CharSequence, formatter: DateTimeFormatter): Date = {
    formatter.parse(text, Date.from)
  }

  /**
   * Creates a local date from the year, month and day fields.
   *
   * @param year  the year to represent, validated from MIN_YEAR to MAX_YEAR
   * @param month  the month-of-year to represent, from 1 to 12, validated
   * @param dayOfMonth  the day-of-month to represent, validated from 1 to 31
   * @return the local date, not null
   * @throws DateTimeException if the day-of-month is invalid for the month-year
   */
  private def create(year: Int, month: Int, dayOfMonth: Int): Date = {
    if (dayOfMonth > 28) {
      var dom: Int = 31
      month match {
        case 2 =>
          dom = (if (IsoChronology.INSTANCE.isLeapYear(year)) 29 else 28)
          break //todo: break is not supported
        case 4 =>
        case 6 =>
        case 9 =>
        case 11 =>
          dom = 30
          break //todo: break is not supported
      }
      if (dayOfMonth > dom) {
        if (dayOfMonth == 29) {
          throw new DateTimeException("Invalid date 'February 29' as '" + year + "' is not a leap year")
        }
        else {
          throw new DateTimeException("Invalid date '" + Month.of(month).name + " " + dayOfMonth + "'")
        }
      }
    }
    new Date(year, month, dayOfMonth)
  }

  /**
   * Resolves the date, resolving days past the end of month.
   *
   * @param year  the year to represent, validated from MIN_YEAR to MAX_YEAR
   * @param month  the month-of-year to represent, validated from 1 to 12
   * @param day  the day-of-month to represent, validated from 1 to 31
   * @return the resolved date, not null
   */
  private def resolvePreviousValid(year: Int, month: Int, day: Int): Date = {
    month match {
      case 2 =>
        day = Math.min(day, if (IsoChronology.INSTANCE.isLeapYear(year)) 29 else 28)
        break //todo: break is not supported
      case 4 =>
      case 6 =>
      case 9 =>
      case 11 =>
        day = Math.min(day, 30)
        break //todo: break is not supported
    }
    new Date(year, month, day)
  }

  private[time] def readExternal(in: DataInput): Date = {
    val year: Int = in.readInt
    val month: Int = in.readByte
    val dayOfMonth: Int = in.readByte
    Date.of(year, month, dayOfMonth)
  }

  /**
   * The minimum supported {@code Date}, '-999999999-01-01'.
   * This could be used by an application as a "far past" date.
   */
  final val MIN: Date = Date.of(Year.MIN_VALUE, 1, 1)
  /**
   * The maximum supported {@code Date}, '+999999999-12-31'.
   * This could be used by an application as a "far future" date.
   */
  final val MAX: Date = Date.of(Year.MAX_VALUE, 12, 31)
  /**
   * Serialization version.
   */
  private final val serialVersionUID: Long = 2942565459149668126L
  /**
   * The number of days in a 400 year cycle.
   */
  private final val DAYS_PER_CYCLE: Int = 146097
  /**
   * The number of days from year zero to year 1970.
   * There are five 400 year cycles from year zero to 2000.
   * There are 7 leap years from 1970 to 2000.
   */
  private[time] final val DAYS_0000_TO_1970: Long = (DAYS_PER_CYCLE * 5L) - (30L * 365L + 7L)
}

case class Date(year: Int, month: Int, dayOfMonth: Int) extends Temporal with TemporalAdjuster with ChronoLocalDate {

  /**
   * Checks if the specified field is supported.
   * <p>
   * This checks if this date can be queried for the specified field.
   * If false, then calling the {@link #range(TemporalField) range},
   * {@link #get(TemporalField) get} and {@link #with(TemporalField, long)}
   * methods will throw an exception.
   * <p>
   * If the field is a {@link ChronoField} then the query is implemented here.
   * The supported fields are:
   * <ul>
   * <li>{@code DAY_OF_WEEK}
   * <li>{@code ALIGNED_DAY_OF_WEEK_IN_MONTH}
   * <li>{@code ALIGNED_DAY_OF_WEEK_IN_YEAR}
   * <li>{@code DAY_OF_MONTH}
   * <li>{@code DAY_OF_YEAR}
   * <li>{@code EPOCH_DAY}
   * <li>{@code ALIGNED_WEEK_OF_MONTH}
   * <li>{@code ALIGNED_WEEK_OF_YEAR}
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
   * @return true if the field is supported on this date, false if not
   */
  override def isSupported(field: TemporalField): Boolean = {
    ChronoLocalDate.super.isSupported(field)
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
   * <li>{@code DAYS}
   * <li>{@code WEEKS}
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
  override def isSupported(unit: TemporalUnit): Boolean = {
    ChronoLocalDate.super.isSupported(unit)
  }

  /**
   * Gets the range of valid values for the specified field.
   * <p>
   * The range object expresses the minimum and maximum valid values for a field.
   * This date is used to enhance the accuracy of the returned range.
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
    if (field.isInstanceOf[ChronoField]) {
      val f: ChronoField = field.asInstanceOf[ChronoField]
      if (f.isDateBased) {
        f match {
          case DAY_OF_MONTH =>
            ValueRange.of(1, lengthOfMonth)
          case DAY_OF_YEAR =>
            ValueRange.of(1, lengthOfYear)
          case ALIGNED_WEEK_OF_MONTH =>
            ValueRange.of(1, if (getMonth eq Month.FEBRUARY && isLeapYear == false) 4 else 5)
          case YEAR_OF_ERA =>
            (if (getYear <= 0) ValueRange.of(1, Year.MAX_VALUE + 1) else ValueRange.of(1, Year.MAX_VALUE))
        }
        field.range
      }
      throw new UnsupportedTemporalTypeException("Unsupported field: " + field)
    }
    field.rangeRefinedBy(this)
  }

  /**
   * Gets the value of the specified field from this date as an {@code int}.
   * <p>
   * This queries this date for the value for the specified field.
   * The returned value will always be within the valid range of values for the field.
   * If it is not possible to  the value, because the field is not supported
   * or for some other reason, an exception is thrown.
   * <p>
   * If the field is a {@link ChronoField} then the query is implemented here.
   * The {@link #isSupported(TemporalField) supported fields} will  valid
   * values based on this date, except {@code EPOCH_DAY} and {@code PROLEPTIC_MONTH}
   * which are too large to fit in an {@code int} and throw a {@code DateTimeException}.
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
    if (field.isInstanceOf[ChronoField]) {
      get0(field)
    }
    ChronoLocalDate.super.get(field)
  }

  /**
   * Gets the value of the specified field from this date as a {@code long}.
   * <p>
   * This queries this date for the value for the specified field.
   * If it is not possible to  the value, because the field is not supported
   * or for some other reason, an exception is thrown.
   * <p>
   * If the field is a {@link ChronoField} then the query is implemented here.
   * The {@link #isSupported(TemporalField) supported fields} will  valid
   * values based on this date.
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
      if (field eq EPOCH_DAY) {
        toEpochDay
      }
      if (field eq PROLEPTIC_MONTH) {
        getProlepticMonth
      }
      get0(field)
    }
    field.getFrom(this)
  }

  private def get0(field: TemporalField): Int = {
    field.asInstanceOf[ChronoField] match {
      case DAY_OF_WEEK =>
        getDayOfWeek.getValue
      case ALIGNED_DAY_OF_WEEK_IN_MONTH =>
        ((day - 1) % 7) + 1
      case ALIGNED_DAY_OF_WEEK_IN_YEAR =>
        ((getDayOfYear - 1) % 7) + 1
      case DAY_OF_MONTH =>
        day
      case DAY_OF_YEAR =>
        getDayOfYear
      case EPOCH_DAY =>
        throw new UnsupportedTemporalTypeException("Invalid field 'EpochDay' for get() method, use getLong() instead")
      case ALIGNED_WEEK_OF_MONTH =>
        ((day - 1) / 7) + 1
      case ALIGNED_WEEK_OF_YEAR =>
        ((getDayOfYear - 1) / 7) + 1
      case MONTH_OF_YEAR =>
        month
      case PROLEPTIC_MONTH =>
        throw new UnsupportedTemporalTypeException("Invalid field 'ProlepticMonth' for get() method, use getLong() instead")
      case YEAR_OF_ERA =>
        (if (year >= 1) year else 1 - year)
      case YEAR =>
        year
      case ERA =>
        (if (year >= 1) 1 else 0)
    }
    throw new UnsupportedTemporalTypeException("Unsupported field: " + field)
  }

  private def getProlepticMonth: Long = {
    (year * 12L + month - 1)
  }

  /**
   * Gets the chronology of this date, which is the ISO calendar system.
   * <p>
   * The {@code Chronology} represents the calendar system in use.
   * The ISO-8601 calendar system is the modern civil calendar system used today
   * in most of the world. It is equivalent to the proleptic Gregorian calendar
   * system, in which today's rules for leap years are applied for all time.
   *
   * @return the ISO chronology, not null
   */
  def getChronology: IsoChronology = {
    IsoChronology.INSTANCE
  }

  /**
   * Gets the era applicable at this date.
   * <p>
   * The official ISO-8601 standard does not define eras, however {@code IsoChronology} does.
   * It defines two eras, 'CE' from year one onwards and 'BCE' from year zero backwards.
   * Since dates before the Julian-Gregorian cutover are not in line with history,
   * the cutover between 'BCE' and 'CE' is also not aligned with the commonly used
   * eras, often referred to using 'BC' and 'AD'.
   * <p>
   * Users of this class should typically ignore this method as it exists primarily
   * to fulfill the {@link ChronoLocalDate} contract where it is necessary to support
   * the Japanese calendar system.
   * <p>
   * The returned era will be a singleton capable of being compared with the constants
   * in {@link IsoChronology} using the {@code ==} operator.
   *
   * @return the { @code IsoChronology} era constant applicable at this date, not null
   */
  override def getEra: Era = {
    ChronoLocalDate.super.getEra
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
   * Gets the day-of-year field.
   * <p>
   * This method returns the primitive {@code int} value for the day-of-year.
   *
   * @return the day-of-year, from 1 to 365, or 366 in a leap year
   */
  def getDayOfYear: Int = {
    getMonth.firstDayOfYear(isLeapYear) + day - 1
  }

  /**
   * Gets the day-of-week field, which is an enum {@code DayOfWeek}.
   * <p>
   * This method returns the enum {@link DayOfWeek} for the day-of-week.
   * This avoids confusion as to what {@code int} values mean.
   * If you need access to the primitive {@code int} value then the enum
   * provides the {@link DayOfWeek#getValue() int value}.
   * <p>
   * Additional information can be obtained from the {@code DayOfWeek}.
   * This includes textual names of the values.
   *
   * @return the day-of-week, not null
   */
  def getDayOfWeek: DayOfWeek = {
    val dow0: Int = Math.floorMod(toEpochDay + 3, 7).asInstanceOf[Int]
    DayOfWeek.of(dow0 + 1)
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
  override def isLeapYear: Boolean = {
    IsoChronology.INSTANCE.isLeapYear(year)
  }

  /**
   * Returns the length of the month represented by this date.
   * <p>
   * This returns the length of the month in days.
   * For example, a date in January would  31.
   *
   * @return the length of the month in days
   */
  def lengthOfMonth: Int = {
    month match {
      case 2 =>
        (if (isLeapYear) 29 else 28)
      case 4 =>
      case 6 =>
      case 9 =>
      case 11 =>
        30
      case _ =>
        31
    }
  }

  /**
   * Returns the length of the year represented by this date.
   * <p>
   * This returns the length of the year in days, either 365 or 366.
   *
   * @return 366 if the year is leap, 365 otherwise
   */
  override def lengthOfYear: Int = {
    (if (isLeapYear) 366 else 365)
  }

  /**
   * Returns an adjusted copy of this date.
   * <p>
   * This returns a {@code Date}, based on this one, with the date adjusted.
   * The adjustment takes place using the specified adjuster strategy object.
   * Read the documentation of the adjuster to understand what adjustment will be made.
   * <p>
   * A simple adjuster might simply set the one of the fields, such as the year field.
   * A more complex adjuster might set the date to the last day of the month.
   * A selection of common adjustments is provided in {@link TemporalAdjuster}.
   * These include finding the "last day of the month" and "next Wednesday".
   * Key date-time classes also implement the {@code TemporalAdjuster} interface,
   * such as {@link Month} and {@link java.time.MonthDay MonthDay}.
   * The adjuster is responsible for handling special cases, such as the varying
   * lengths of month and leap years.
   * <p>
   * For example this code returns a date on the last day of July:
   * {{{
   * import static java.time.Month.*;
   * import static java.time.temporal.Adjusters.*;
   *
   * result = localDate.with(JULY).with(lastDayOfMonth());
   * }}}
   * <p>
   * The result of this method is obtained by invoking the
   * {@link TemporalAdjuster#adjustInto(Temporal)} method on the
   * specified adjuster passing {@code this} as the argument.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param adjuster the adjuster to use, not null
   * @return a { @code Date} based on { @code this} with the adjustment made, not null
   * @throws DateTimeException if the adjustment cannot be made
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def `with`(adjuster: TemporalAdjuster): Date = {
    if (adjuster.isInstanceOf[Date]) {
      adjuster.asInstanceOf[Date]
    }
    adjuster.adjustInto(this).asInstanceOf[Date]
  }

  /**
   * Returns a copy of this date with the specified field set to a new value.
   * <p>
   * This returns a {@code Date}, based on this one, with the value
   * for the specified field changed.
   * This can be used to change any supported field, such as the year, month or day-of-month.
   * If it is not possible to set the value, because the field is not supported or for
   * some other reason, an exception is thrown.
   * <p>
   * In some cases, changing the specified field can cause the resulting date to become invalid,
   * such as changing the month from 31st January to February would make the day-of-month invalid.
   * In cases like this, the field is responsible for resolving the date. Typically it will choose
   * the previous valid date, which would be the last valid day of February in this example.
   * <p>
   * If the field is a {@link ChronoField} then the adjustment is implemented here.
   * The supported fields behave as follows:
   * <ul>
   * <li>{@code DAY_OF_WEEK} -
   * Returns a {@code Date} with the specified day-of-week.
   * The date is adjusted up to 6 days forward or backward within the boundary
   * of a Monday to Sunday week.
   * <li>{@code ALIGNED_DAY_OF_WEEK_IN_MONTH} -
   * Returns a {@code Date} with the specified aligned-day-of-week.
   * The date is adjusted to the specified month-based aligned-day-of-week.
   * Aligned weeks are counted such that the first week of a given month starts
   * on the first day of that month.
   * This may cause the date to be moved up to 6 days into the following month.
   * <li>{@code ALIGNED_DAY_OF_WEEK_IN_YEAR} -
   * Returns a {@code Date} with the specified aligned-day-of-week.
   * The date is adjusted to the specified year-based aligned-day-of-week.
   * Aligned weeks are counted such that the first week of a given year starts
   * on the first day of that year.
   * This may cause the date to be moved up to 6 days into the following year.
   * <li>{@code DAY_OF_MONTH} -
   * Returns a {@code Date} with the specified day-of-month.
   * The month and year will be unchanged. If the day-of-month is invalid for the
   * year and month, then a {@code DateTimeException} is thrown.
   * <li>{@code DAY_OF_YEAR} -
   * Returns a {@code Date} with the specified day-of-year.
   * The year will be unchanged. If the day-of-year is invalid for the
   * year, then a {@code DateTimeException} is thrown.
   * <li>{@code EPOCH_DAY} -
   * Returns a {@code Date} with the specified epoch-day.
   * This completely replaces the date and is equivalent to {@link #ofEpochDay(long)}.
   * <li>{@code ALIGNED_WEEK_OF_MONTH} -
   * Returns a {@code Date} with the specified aligned-week-of-month.
   * Aligned weeks are counted such that the first week of a given month starts
   * on the first day of that month.
   * This adjustment moves the date in whole week chunks to match the specified week.
   * The result will have the same day-of-week as this date.
   * This may cause the date to be moved into the following month.
   * <li>{@code ALIGNED_WEEK_OF_YEAR} -
   * Returns a {@code Date} with the specified aligned-week-of-year.
   * Aligned weeks are counted such that the first week of a given year starts
   * on the first day of that year.
   * This adjustment moves the date in whole week chunks to match the specified week.
   * The result will have the same day-of-week as this date.
   * This may cause the date to be moved into the following year.
   * <li>{@code MONTH_OF_YEAR} -
   * Returns a {@code Date} with the specified month-of-year.
   * The year will be unchanged. The day-of-month will also be unchanged,
   * unless it would be invalid for the new month and year. In that case, the
   * day-of-month is adjusted to the maximum valid value for the new month and year.
   * <li>{@code PROLEPTIC_MONTH} -
   * Returns a {@code Date} with the specified proleptic-month.
   * The day-of-month will be unchanged, unless it would be invalid for the new month
   * and year. In that case, the day-of-month is adjusted to the maximum valid value
   * for the new month and year.
   * <li>{@code YEAR_OF_ERA} -
   * Returns a {@code Date} with the specified year-of-era.
   * The era and month will be unchanged. The day-of-month will also be unchanged,
   * unless it would be invalid for the new month and year. In that case, the
   * day-of-month is adjusted to the maximum valid value for the new month and year.
   * <li>{@code YEAR} -
   * Returns a {@code Date} with the specified year.
   * The month will be unchanged. The day-of-month will also be unchanged,
   * unless it would be invalid for the new month and year. In that case, the
   * day-of-month is adjusted to the maximum valid value for the new month and year.
   * <li>{@code ERA} -
   * Returns a {@code Date} with the specified era.
   * The year-of-era and month will be unchanged. The day-of-month will also be unchanged,
   * unless it would be invalid for the new month and year. In that case, the
   * day-of-month is adjusted to the maximum valid value for the new month and year.
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
   * @return a { @code Date} based on { @code this} with the specified field set, not null
   * @throws DateTimeException if the field cannot be set
   * @throws UnsupportedTemporalTypeException if the field is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def `with`(field: TemporalField, newValue: Long): Date = {
    if (field.isInstanceOf[ChronoField]) {
      val f: ChronoField = field.asInstanceOf[ChronoField]
      f.checkValidValue(newValue)
      f match {
        case DAY_OF_WEEK =>
          plusDays(newValue - getDayOfWeek.getValue)
        case ALIGNED_DAY_OF_WEEK_IN_MONTH =>
          plusDays(newValue - getLong(ALIGNED_DAY_OF_WEEK_IN_MONTH))
        case ALIGNED_DAY_OF_WEEK_IN_YEAR =>
          plusDays(newValue - getLong(ALIGNED_DAY_OF_WEEK_IN_YEAR))
        case DAY_OF_MONTH =>
          withDayOfMonth(newValue.asInstanceOf[Int])
        case DAY_OF_YEAR =>
          withDayOfYear(newValue.asInstanceOf[Int])
        case EPOCH_DAY =>
          Date.ofEpochDay(newValue)
        case ALIGNED_WEEK_OF_MONTH =>
          plusWeeks(newValue - getLong(ALIGNED_WEEK_OF_MONTH))
        case ALIGNED_WEEK_OF_YEAR =>
          plusWeeks(newValue - getLong(ALIGNED_WEEK_OF_YEAR))
        case MONTH_OF_YEAR =>
          withMonth(newValue.asInstanceOf[Int])
        case PROLEPTIC_MONTH =>
          plusMonths(newValue - getProlepticMonth)
        case YEAR_OF_ERA =>
          withYear((if (year >= 1) newValue else 1 - newValue).asInstanceOf[Int])
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
   * Returns a copy of this date with the year altered.
   * If the day-of-month is invalid for the year, it will be changed to the last valid day of the month.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param year  the year to set in the result, from MIN_YEAR to MAX_YEAR
   * @return a { @code Date} based on this date with the requested year, not null
   * @throws DateTimeException if the year value is invalid
   */
  def withYear(year: Int): Date = {
    if (this.year == year) {
      this
    }
    YEAR.checkValidValue(year)
    resolvePreviousValid(year, month, day)
  }

  /**
   * Returns a copy of this date with the month-of-year altered.
   * If the day-of-month is invalid for the year, it will be changed to the last valid day of the month.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param month  the month-of-year to set in the result, from 1 (January) to 12 (December)
   * @return a { @code Date} based on this date with the requested month, not null
   * @throws DateTimeException if the month-of-year value is invalid
   */
  def withMonth(month: Int): Date = {
    if (this.month == month) {
      this
    }
    MONTH_OF_YEAR.checkValidValue(month)
    resolvePreviousValid(year, month, day)
  }

  /**
   * Returns a copy of this date with the day-of-month altered.
   * If the resulting date is invalid, an exception is thrown.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param dayOfMonth  the day-of-month to set in the result, from 1 to 28-31
   * @return a { @code Date} based on this date with the requested day, not null
   * @throws DateTimeException if the day-of-month value is invalid,
   *                           or if the day-of-month is invalid for the month-year
   */
  def withDayOfMonth(dayOfMonth: Int): Date = {
    if (this.day == dayOfMonth) {
      this
    }
    of(year, month, dayOfMonth)
  }

  /**
   * Returns a copy of this date with the day-of-year altered.
   * If the resulting date is invalid, an exception is thrown.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param dayOfYear  the day-of-year to set in the result, from 1 to 365-366
   * @return a { @code Date} based on this date with the requested day, not null
   * @throws DateTimeException if the day-of-year value is invalid,
   *                           or if the day-of-year is invalid for the year
   */
  def withDayOfYear(dayOfYear: Int): Date = {
    if (this.getDayOfYear == dayOfYear) {
      this
    }
    ofYearDay(year, dayOfYear)
  }

  /**
   * Returns a copy of this date with the specified amount added.
   * <p>
   * This returns a {@code Date}, based on this one, with the specified amount added.
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
   * @return a { @code Date} based on this date with the addition made, not null
   * @throws DateTimeException if the addition cannot be made
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def plus(amountToAdd: TemporalAmount): Date = {
    object
    if (amountToAdd.isInstanceOf[Period]) {
      val periodToAdd: Period = amountToAdd.asInstanceOf[Period]
      plusMonths(periodToAdd.toTotalMonths).plusDays(periodToAdd.getDays)
    }
    amountToAdd.addTo(this).asInstanceOf[Date]
  }

  /**
   * Returns a copy of this date with the specified amount added.
   * <p>
   * This returns a {@code Date}, based on this one, with the amount
   * in terms of the unit added. If it is not possible to add the amount, because the
   * unit is not supported or for some other reason, an exception is thrown.
   * <p>
   * In some cases, adding the amount can cause the resulting date to become invalid.
   * For example, adding one month to 31st January would result in 31st February.
   * In cases like this, the unit is responsible for resolving the date.
   * Typically it will choose the previous valid date, which would be the last valid
   * day of February in this example.
   * <p>
   * If the field is a {@link ChronoUnit} then the addition is implemented here.
   * The supported fields behave as follows:
   * <ul>
   * <li>{@code DAYS} -
   * Returns a {@code Date} with the specified number of days added.
   * This is equivalent to {@link #plusDays(long)}.
   * <li>{@code WEEKS} -
   * Returns a {@code Date} with the specified number of weeks added.
   * This is equivalent to {@link #plusWeeks(long)} and uses a 7 day week.
   * <li>{@code MONTHS} -
   * Returns a {@code Date} with the specified number of months added.
   * This is equivalent to {@link #plusMonths(long)}.
   * The day-of-month will be unchanged unless it would be invalid for the new
   * month and year. In that case, the day-of-month is adjusted to the maximum
   * valid value for the new month and year.
   * <li>{@code YEARS} -
   * Returns a {@code Date} with the specified number of years added.
   * This is equivalent to {@link #plusYears(long)}.
   * The day-of-month will be unchanged unless it would be invalid for the new
   * month and year. In that case, the day-of-month is adjusted to the maximum
   * valid value for the new month and year.
   * <li>{@code DECADES} -
   * Returns a {@code Date} with the specified number of decades added.
   * This is equivalent to calling {@link #plusYears(long)} with the amount
   * multiplied by 10.
   * The day-of-month will be unchanged unless it would be invalid for the new
   * month and year. In that case, the day-of-month is adjusted to the maximum
   * valid value for the new month and year.
   * <li>{@code CENTURIES} -
   * Returns a {@code Date} with the specified number of centuries added.
   * This is equivalent to calling {@link #plusYears(long)} with the amount
   * multiplied by 100.
   * The day-of-month will be unchanged unless it would be invalid for the new
   * month and year. In that case, the day-of-month is adjusted to the maximum
   * valid value for the new month and year.
   * <li>{@code MILLENNIA} -
   * Returns a {@code Date} with the specified number of millennia added.
   * This is equivalent to calling {@link #plusYears(long)} with the amount
   * multiplied by 1,000.
   * The day-of-month will be unchanged unless it would be invalid for the new
   * month and year. In that case, the day-of-month is adjusted to the maximum
   * valid value for the new month and year.
   * <li>{@code ERAS} -
   * Returns a {@code Date} with the specified number of eras added.
   * Only two eras are supported so the amount must be one, zero or minus one.
   * If the amount is non-zero then the year is changed such that the year-of-era
   * is unchanged.
   * The day-of-month will be unchanged unless it would be invalid for the new
   * month and year. In that case, the day-of-month is adjusted to the maximum
   * valid value for the new month and year.
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
   * @return a { @code Date} based on this date with the specified amount added, not null
   * @throws DateTimeException if the addition cannot be made
   * @throws UnsupportedTemporalTypeException if the unit is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def plus(amountToAdd: Long, unit: TemporalUnit): Date = {
    if (unit.isInstanceOf[ChronoUnit]) {
      val f: ChronoUnit = unit.asInstanceOf[ChronoUnit]
      f match {
        case DAYS =>
          plusDays(amountToAdd)
        case WEEKS =>
          plusWeeks(amountToAdd)
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
   * Returns a copy of this {@code Date} with the specified period in years added.
   * <p>
   * This method adds the specified amount to the years field in three steps:
   * <ol>
   * <li>Add the input years to the year field</li>
   * <li>Check if the resulting date would be invalid</li>
   * <li>Adjust the day-of-month to the last valid day if necessary</li>
   * </ol>
   * <p>
   * For example, 2008-02-29 (leap year) plus one year would result in the
   * invalid date 2009-02-29 (standard year). Instead of returning an invalid
   * result, the last valid day of the month, 2009-02-28, is selected instead.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param yearsToAdd  the years to add, may be negative
   * @return a { @code Date} based on this date with the years added, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def plusYears(yearsToAdd: Long): Date = {
    if (yearsToAdd == 0) {
      this
    }
    val newYear: Int = YEAR.checkValidIntValue(year + yearsToAdd)
    resolvePreviousValid(newYear, month, day)
  }

  /**
   * Returns a copy of this {@code Date} with the specified period in months added.
   * <p>
   * This method adds the specified amount to the months field in three steps:
   * <ol>
   * <li>Add the input months to the month-of-year field</li>
   * <li>Check if the resulting date would be invalid</li>
   * <li>Adjust the day-of-month to the last valid day if necessary</li>
   * </ol>
   * <p>
   * For example, 2007-03-31 plus one month would result in the invalid date
   * 2007-04-31. Instead of returning an invalid result, the last valid day
   * of the month, 2007-04-30, is selected instead.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param monthsToAdd  the months to add, may be negative
   * @return a { @code Date} based on this date with the months added, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def plusMonths(monthsToAdd: Long): Date = {
    if (monthsToAdd == 0) {
      this
    }
    val monthCount: Long = year * 12L + (month - 1)
    val calcMonths: Long = monthCount + monthsToAdd
    val newYear: Int = YEAR.checkValidIntValue(Math.floorDiv(calcMonths, 12))
    val newMonth: Int = Math.floorMod(calcMonths, 12).asInstanceOf[Int] + 1
    resolvePreviousValid(newYear, newMonth, day)
  }

  /**
   * Returns a copy of this {@code Date} with the specified period in weeks added.
   * <p>
   * This method adds the specified amount in weeks to the days field incrementing
   * the month and year fields as necessary to ensure the result remains valid.
   * The result is only invalid if the maximum/minimum year is exceeded.
   * <p>
   * For example, 2008-12-31 plus one week would result in 2009-01-07.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param weeksToAdd  the weeks to add, may be negative
   * @return a { @code Date} based on this date with the weeks added, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def plusWeeks(weeksToAdd: Long): Date = {
    plusDays(Math.multiplyExact(weeksToAdd, 7))
  }

  /**
   * Returns a copy of this {@code Date} with the specified number of days added.
   * <p>
   * This method adds the specified amount to the days field incrementing the
   * month and year fields as necessary to ensure the result remains valid.
   * The result is only invalid if the maximum/minimum year is exceeded.
   * <p>
   * For example, 2008-12-31 plus one day would result in 2009-01-01.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param daysToAdd  the days to add, may be negative
   * @return a { @code Date} based on this date with the days added, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def plusDays(daysToAdd: Long): Date = {
    if (daysToAdd == 0) {
      this
    }
    val mjDay: Long = Math.addExact(toEpochDay, daysToAdd)
    Date.ofEpochDay(mjDay)
  }

  /**
   * Returns a copy of this date with the specified amount subtracted.
   * <p>
   * This returns a {@code Date}, based on this one, with the specified amount subtracted.
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
   * @return a { @code Date} based on this date with the subtraction made, not null
   * @throws DateTimeException if the subtraction cannot be made
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def minus(amountToSubtract: TemporalAmount): Date = {
    object
    if (amountToSubtract.isInstanceOf[Period]) {
      val periodToSubtract: Period = amountToSubtract.asInstanceOf[Period]
      minusMonths(periodToSubtract.toTotalMonths).minusDays(periodToSubtract.getDays)
    }
    amountToSubtract.subtractFrom(this).asInstanceOf[Date]
  }

  /**
   * Returns a copy of this date with the specified amount subtracted.
   * <p>
   * This returns a {@code Date}, based on this one, with the amount
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
   * @return a { @code Date} based on this date with the specified amount subtracted, not null
   * @throws DateTimeException if the subtraction cannot be made
   * @throws UnsupportedTemporalTypeException if the unit is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def minus(amountToSubtract: Long, unit: TemporalUnit): Date = {
    (if (amountToSubtract == Long.MIN_VALUE) plus(Long.MAX_VALUE, unit).plus(1, unit) else plus(-amountToSubtract, unit))
  }

  /**
   * Returns a copy of this {@code Date} with the specified period in years subtracted.
   * <p>
   * This method subtracts the specified amount from the years field in three steps:
   * <ol>
   * <li>Subtract the input years to the year field</li>
   * <li>Check if the resulting date would be invalid</li>
   * <li>Adjust the day-of-month to the last valid day if necessary</li>
   * </ol>
   * <p>
   * For example, 2008-02-29 (leap year) minus one year would result in the
   * invalid date 2007-02-29 (standard year). Instead of returning an invalid
   * result, the last valid day of the month, 2007-02-28, is selected instead.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param yearsToSubtract  the years to subtract, may be negative
   * @return a { @code Date} based on this date with the years subtracted, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def minusYears(yearsToSubtract: Long): Date = {
    (if (yearsToSubtract == Long.MIN_VALUE) plusYears(Long.MAX_VALUE).plusYears(1) else plusYears(-yearsToSubtract))
  }

  /**
   * Returns a copy of this {@code Date} with the specified period in months subtracted.
   * <p>
   * This method subtracts the specified amount from the months field in three steps:
   * <ol>
   * <li>Subtract the input months to the month-of-year field</li>
   * <li>Check if the resulting date would be invalid</li>
   * <li>Adjust the day-of-month to the last valid day if necessary</li>
   * </ol>
   * <p>
   * For example, 2007-03-31 minus one month would result in the invalid date
   * 2007-02-31. Instead of returning an invalid result, the last valid day
   * of the month, 2007-02-28, is selected instead.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param monthsToSubtract  the months to subtract, may be negative
   * @return a { @code Date} based on this date with the months subtracted, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def minusMonths(monthsToSubtract: Long): Date = {
    (if (monthsToSubtract == Long.MIN_VALUE) plusMonths(Long.MAX_VALUE).plusMonths(1) else plusMonths(-monthsToSubtract))
  }

  /**
   * Returns a copy of this {@code Date} with the specified period in weeks subtracted.
   * <p>
   * This method subtracts the specified amount in weeks from the days field decrementing
   * the month and year fields as necessary to ensure the result remains valid.
   * The result is only invalid if the maximum/minimum year is exceeded.
   * <p>
   * For example, 2009-01-07 minus one week would result in 2008-12-31.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param weeksToSubtract  the weeks to subtract, may be negative
   * @return a { @code Date} based on this date with the weeks subtracted, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def minusWeeks(weeksToSubtract: Long): Date = {
    (if (weeksToSubtract == Long.MIN_VALUE) plusWeeks(Long.MAX_VALUE).plusWeeks(1) else plusWeeks(-weeksToSubtract))
  }

  /**
   * Returns a copy of this {@code Date} with the specified number of days subtracted.
   * <p>
   * This method subtracts the specified amount from the days field decrementing the
   * month and year fields as necessary to ensure the result remains valid.
   * The result is only invalid if the maximum/minimum year is exceeded.
   * <p>
   * For example, 2009-01-01 minus one day would result in 2008-12-31.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param daysToSubtract  the days to subtract, may be negative
   * @return a { @code Date} based on this date with the days subtracted, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def minusDays(daysToSubtract: Long): Date = {
    (if (daysToSubtract == Long.MIN_VALUE) plusDays(Long.MAX_VALUE).plusDays(1) else plusDays(-daysToSubtract))
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
  override def query(query: TemporalQuery[R]): R = {
    if (query eq TemporalQuery.localDate) {
      this.asInstanceOf[R]
    }
    ChronoLocalDate.super.query(query)
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
  override def adjustInto(temporal: Temporal): Temporal = {
    ChronoLocalDate.super.adjustInto(temporal)
  }

  /**
   * Calculates the amount of time until another date in terms of the specified unit.
   * <p>
   * This calculates the amount of time between two {@code Date}
   * objects in terms of a single {@code TemporalUnit}.
   * The start and end points are {@code this} and the specified date.
   * The result will be negative if the end is before the start.
   * The {@code Temporal} passed to this method is converted to a
   * {@code Date} using {@link #from(TemporalAccessor)}.
   * For example, the amount in days between two dates can be calculated
   * using {@code startDate.until(endDate, DAYS)}.
   * <p>
   * The calculation returns a whole number, representing the number of
   * complete units between the two dates.
   * For example, the amount in months between 2012-06-15 and 2012-08-14
   * will only be one month as it is one day short of two months.
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
   * are supported. Other {@code ChronoUnit} values will throw an exception.
   * <p>
   * If the unit is not a {@code ChronoUnit}, then the result of this method
   * is obtained by invoking {@code TemporalUnit.between(Temporal, Temporal)}
   * passing {@code this} as the first argument and the converted input temporal
   * as the second argument.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param endExclusive  the end date, which is converted to a { @code Date}, not null
   * @param unit  the unit to measure the amount in, not null
   * @return the amount of time between this date and the end date
   * @throws DateTimeException if the amount cannot be calculated, or the end
   *                           temporal cannot be converted to a { @code Date}
   * @throws UnsupportedTemporalTypeException if the unit is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  def until(endExclusive: Temporal, unit: TemporalUnit): Long = {
    val end: Date = Date.from(endExclusive)
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

  private[time] def daysUntil(end: Date): Long = {
    end.toEpochDay - toEpochDay
  }

  private def monthsUntil(end: Date): Long = {
    val packed1: Long = getProlepticMonth * 32L + getDayOfMonth
    val packed2: Long = end.getProlepticMonth * 32L + end.getDayOfMonth
    (packed2 - packed1) / 32
  }

  /**
   * Calculates the period between this date and another date as a {@code Period}.
   * <p>
   * This calculates the period between two dates in terms of years, months and days.
   * The start and end points are {@code this} and the specified date.
   * The result will be negative if the end is before the start.
   * The negative sign will be the same in each of year, month and day.
   * <p>
   * The calculation is performed using the ISO calendar system.
   * If necessary, the input date will be converted to ISO.
   * <p>
   * The start date is included, but the end date is not.
   * The period is calculated by removing complete months, then calculating
   * the remaining number of days, adjusting to ensure that both have the same sign.
   * The number of months is then normalized into years and months based on a 12 month year.
   * A month is considered to be complete if the end day-of-month is greater
   * than or equal to the start day-of-month.
   * For example, from {@code 2010-01-15} to {@code 2011-03-18} is "1 year, 2 months and 3 days".
   * <p>
   * There are two equivalent ways of using this method.
   * The first is to invoke this method.
   * The second is to use {@link Period#between(Date, Date)}:
   * {{{
   * // these two lines are equivalent
   * period = start.until(end);
   * period = Period.between(start, end);
   * }}}
   * The choice should be made based on which makes the code more readable.
   *
   * @param endDateExclusive  the end date, exclusive, which may be in any chronology, not null
   * @return the period between this date and the end date, not null
   */
  def until(endDateExclusive: ChronoLocalDate): Period = {
    val end: Date = Date.from(endDateExclusive)
    var totalMonths: Long = end.getProlepticMonth - this.getProlepticMonth
    var days: Int = end.day - this.day
    if (totalMonths > 0 && days < 0) {
      totalMonths -= 1
      val calcDate: Date = this.plusMonths(totalMonths)
      days = (end.toEpochDay - calcDate.toEpochDay).asInstanceOf[Int]
    }
    else if (totalMonths < 0 && days > 0) {
      totalMonths += 1
      days -= end.lengthOfMonth
    }
    val years: Long = totalMonths / 12
    val months: Int = (totalMonths % 12).asInstanceOf[Int]
    Period.of(Math.toIntExact(years), months, days)
  }

  /**
   * Formats this date using the specified formatter.
   * <p>
   * This date will be passed to the formatter to produce a string.
   *
   * @param formatter  the formatter to use, not null
   * @return the formatted date string, not null
   * @throws DateTimeException if an error occurs during printing
   */
  override def format(formatter: DateTimeFormatter): String = {
    object
    formatter.format(this)
  }

  /**
   * Combines this date with a time to create a {@code DateTime}.
   * <p>
   * This returns a {@code DateTime} formed from this date at the specified time.
   * All possible combinations of date and time are valid.
   *
   * @param time  the time to combine with, not null
   * @return the local date-time formed from this date and the specified time, not null
   */
  override def atTime(time: Time): DateTime = {
    DateTime.of(this, time)
  }

  /**
   * Combines this date with a time to create a {@code DateTime}.
   * <p>
   * This returns a {@code DateTime} formed from this date at the
   * specified hour and minute.
   * The seconds and nanosecond fields will be set to zero.
   * The individual time fields must be within their valid range.
   * All possible combinations of date and time are valid.
   *
   * @param hour  the hour-of-day to use, from 0 to 23
   * @param minute  the minute-of-hour to use, from 0 to 59
   * @return the local date-time formed from this date and the specified time, not null
   * @throws DateTimeException if the value of any field is out of range
   */
  def atTime(hour: Int, minute: Int): DateTime = {
    atTime(Time.of(hour, minute))
  }

  /**
   * Combines this date with a time to create a {@code DateTime}.
   * <p>
   * This returns a {@code DateTime} formed from this date at the
   * specified hour, minute and second.
   * The nanosecond field will be set to zero.
   * The individual time fields must be within their valid range.
   * All possible combinations of date and time are valid.
   *
   * @param hour  the hour-of-day to use, from 0 to 23
   * @param minute  the minute-of-hour to use, from 0 to 59
   * @param second  the second-of-minute to represent, from 0 to 59
   * @return the local date-time formed from this date and the specified time, not null
   * @throws DateTimeException if the value of any field is out of range
   */
  def atTime(hour: Int, minute: Int, second: Int): DateTime = {
    atTime(Time.of(hour, minute, second))
  }

  /**
   * Combines this date with a time to create a {@code DateTime}.
   * <p>
   * This returns a {@code DateTime} formed from this date at the
   * specified hour, minute, second and nanosecond.
   * The individual time fields must be within their valid range.
   * All possible combinations of date and time are valid.
   *
   * @param hour  the hour-of-day to use, from 0 to 23
   * @param minute  the minute-of-hour to use, from 0 to 59
   * @param second  the second-of-minute to represent, from 0 to 59
   * @param nanoOfSecond  the nano-of-second to represent, from 0 to 999,999,999
   * @return the local date-time formed from this date and the specified time, not null
   * @throws DateTimeException if the value of any field is out of range
   */
  def atTime(hour: Int, minute: Int, second: Int, nanoOfSecond: Int): DateTime = {
    atTime(Time.of(hour, minute, second, nanoOfSecond))
  }

  /**
   * Combines this date with an offset time to create an {@code OffsetDateTime}.
   * <p>
   * This returns an {@code OffsetDateTime} formed from this date at the specified time.
   * All possible combinations of date and time are valid.
   *
   * @param time  the time to combine with, not null
   * @return the offset date-time formed from this date and the specified time, not null
   */
  def atTime(time: OffsetTime): OffsetDateTime = {
    OffsetDateTime.of(DateTime.of(this, time.toLocalTime), time.getOffset)
  }

  /**
   * Combines this date with the time of midnight to create a {@code DateTime}
   * at the start of this date.
   * <p>
   * This returns a {@code DateTime} formed from this date at the time of
   * midnight, 00:00, at the start of this date.
   *
   * @return the local date-time of midnight at the start of this date, not null
   */
  def atStartOfDay: DateTime = {
    DateTime.of(this, Time.MIDNIGHT)
  }

  /**
   * Returns a zoned date-time from this date at the earliest valid time according
   * to the rules in the time-zone.
   * <p>
   * Time-zone rules, such as daylight savings, mean that not every local date-time
   * is valid for the specified zone, thus the local date-time may not be midnight.
   * <p>
   * In most cases, there is only one valid offset for a local date-time.
   * In the case of an overlap, there are two valid offsets, and the earlier one is used,
   * corresponding to the first occurrence of midnight on the date.
   * In the case of a gap, the zoned date-time will represent the instant just after the gap.
   * <p>
   * If the zone ID is a {@link ZoneOffset}, then the result always has a time of midnight.
   * <p>
   * To convert to a specific time in a given time-zone call {@link #atTime(Time)}
   * followed by {@link DateTime#atZone(ZoneId)}.
   *
   * @param zone  the zone ID to use, not null
   * @return the zoned date-time formed from this date and the earliest valid time for the zone, not null
   */
  def atStartOfDay(zone: ZoneId): ZonedDateTime = {
    object
    var ldt: DateTime = atTime(Time.MIDNIGHT)
    if (zone.isInstanceOf[ZoneOffset] == false) {
      val rules: ZoneRules = zone.getRules
      val trans: ZoneOffsetTransition = rules.getTransition(ldt)
      if (trans != null && trans.isGap) {
        ldt = trans.getDateTimeAfter
      }
    }
    ZonedDateTime.of(ldt, zone)
  }

  override def toEpochDay: Long = {
    val y: Long = year
    val m: Long = month
    var total: Long = 0
    total += 365 * y
    if (y >= 0) {
      total += (y + 3) / 4 - (y + 99) / 100 + (y + 399) / 400
    }
    else {
      total -= y / -4 - y / -100 + y / -400
    }
    total += ((367 * m - 362) / 12)
    total += day - 1
    if (m > 2) {
      total -= 1
      if (isLeapYear == false) {
        total -= 1
      }
    }
    total - DAYS_0000_TO_1970
  }

  /**
   * Compares this date to another date.
   * <p>
   * The comparison is primarily based on the date, from earliest to latest.
   * It is "consistent with equals", as defined by {@link Comparable}.
   * <p>
   * If all the dates being compared are instances of {@code Date},
   * then the comparison will be entirely based on the date.
   * If some dates being compared are in different chronologies, then the
   * chronology is also considered, see {@link java.time.chrono.ChronoLocalDate#compareTo}.
   *
   * @param other  the other date to compare to, not null
   * @return the comparator value, negative if less, positive if greater
   */
  override def compareTo(other: ChronoLocalDate): Int = {
    if (other.isInstanceOf[Date]) {
      compareTo0(other.asInstanceOf[Date])
    }
    ChronoLocalDate.super.compareTo(other)
  }

  private[time] def compareTo0(otherDate: Date): Int = {
    var cmp: Int = (year - otherDate.year)
    if (cmp == 0) {
      cmp = (month - otherDate.month)
      if (cmp == 0) {
        cmp = (day - otherDate.day)
      }
    }
    cmp
  }

  /**
   * Checks if this date is after the specified date.
   * <p>
   * This checks to see if this date represents a point on the
   * local time-line after the other date.
   * {{{
   * Date a = Date.of(2012, 6, 30);
   * Date b = Date.of(2012, 7, 1);
   * a.isAfter(b) == false
   * a.isAfter(a) == false
   * b.isAfter(a) == true
   * }}}
   * <p>
   * This method only considers the position of the two dates on the local time-line.
   * It does not take into account the chronology, or calendar system.
   * This is different from the comparison in {@link #compareTo(ChronoLocalDate)},
   * but is the same approach as {@link ChronoLocalDate#timeLineOrder()}.
   *
   * @param other  the other date to compare to, not null
   * @return true if this date is after the specified date
   */
  override def isAfter(other: ChronoLocalDate): Boolean = {
    if (other.isInstanceOf[Date]) {
      compareTo0(other.asInstanceOf[Date]) > 0
    }
    ChronoLocalDate.super.isAfter(other)
  }

  /**
   * Checks if this date is before the specified date.
   * <p>
   * This checks to see if this date represents a point on the
   * local time-line before the other date.
   * {{{
   * Date a = Date.of(2012, 6, 30);
   * Date b = Date.of(2012, 7, 1);
   * a.isBefore(b) == true
   * a.isBefore(a) == false
   * b.isBefore(a) == false
   * }}}
   * <p>
   * This method only considers the position of the two dates on the local time-line.
   * It does not take into account the chronology, or calendar system.
   * This is different from the comparison in {@link #compareTo(ChronoLocalDate)},
   * but is the same approach as {@link ChronoLocalDate#timeLineOrder()}.
   *
   * @param other  the other date to compare to, not null
   * @return true if this date is before the specified date
   */
  override def isBefore(other: ChronoLocalDate): Boolean = {
    if (other.isInstanceOf[Date]) {
      compareTo0(other.asInstanceOf[Date]) < 0
    }
    ChronoLocalDate.super.isBefore(other)
  }

  /**
   * Checks if this date is equal to the specified date.
   * <p>
   * This checks to see if this date represents the same point on the
   * local time-line as the other date.
   * {{{
   * Date a = Date.of(2012, 6, 30);
   * Date b = Date.of(2012, 7, 1);
   * a.isEqual(b) == false
   * a.isEqual(a) == true
   * b.isEqual(a) == false
   * }}}
   * <p>
   * This method only considers the position of the two dates on the local time-line.
   * It does not take into account the chronology, or calendar system.
   * This is different from the comparison in {@link #compareTo(ChronoLocalDate)}
   * but is the same approach as {@link ChronoLocalDate#timeLineOrder()}.
   *
   * @param other  the other date to compare to, not null
   * @return true if this date is equal to the specified date
   */
  override def isEqual(other: ChronoLocalDate): Boolean = {
    if (other.isInstanceOf[Date]) {
      compareTo0(other.asInstanceOf[Date]) == 0
    }
    ChronoLocalDate.super.isEqual(other)
  }

  /**
   * Outputs this date as a {@code String}, such as {@code 2007-12-03}.
   * <p>
   * The output will be in the ISO-8601 format {@code uuuu-MM-dd}.
   *
   * @return a string representation of this date, not null
   */
  override def toString: String = {
    val yearValue: Int = year
    val monthValue: Int = month
    val dayValue: Int = day
    val absYear: Int = Math.abs(yearValue)
    val buf: StringBuilder = new StringBuilder(10)
    if (absYear < 1000) {
      if (yearValue < 0) {
        buf.append(yearValue - 10000).deleteCharAt(1)
      }
      else {
        buf.append(yearValue + 10000).deleteCharAt(0)
      }
    }
    else {
      if (yearValue > 9999) {
        buf.append('+')
      }
      buf.append(yearValue)
    }
    buf.append(if (monthValue < 10) "-0" else "-").append(monthValue).append(if (dayValue < 10) "-0" else "-").append(dayValue).toString
  }

  /**
   * Writes the object using a
   * <a href="../../serialized-form.html#java.time.Ser">dedicated serialized form</a>.
   * {{{
   * out.writeByte(3);  // identifies this as a Date
   * out.writeInt(year);
   * out.writeByte(month);
   * out.writeByte(day);
   * }}}
   *
   * @return the instance of { @code Ser}, not null
   */
  private def writeReplace: AnyRef = {
    new Ser(Ser.LOCAL_DATE_TYPE, this)
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
    out.writeByte(day)
  }

}


