package metronome.chrono

/**
 * The Japanese Imperial calendar system.
 * <p>
 * This chronology defines the rules of the Japanese Imperial calendar system.
 * This calendar system is primarily used in Japan.
 * The Japanese Imperial calendar system is the same as the ISO calendar system
 * apart from the era-based year numbering.
 * <p>
 * Japan introduced the Gregorian calendar starting with Meiji 6.
 * Only Meiji and later eras are supported;
 * dates before Meiji 6, January 1 are not supported.
 * <p>
 * The supported {@code ChronoField} instances are:
 * <ul>
 * <li>{@code DAY_OF_WEEK}
 * <li>{@code DAY_OF_MONTH}
 * <li>{@code DAY_OF_YEAR}
 * <li>{@code EPOCH_DAY}
 * <li>{@code MONTH_OF_YEAR}
 * <li>{@code PROLEPTIC_MONTH}
 * <li>{@code YEAR_OF_ERA}
 * <li>{@code YEAR}
 * <li>{@code ERA}
 * </ul>
 *
 * @implSpec
 * This class is immutable and thread-safe.
 *
 * @since 1.8
 */
final object JapaneseChronology {
  private[chrono] final val JCAL: LocalGregorianCalendar = CalendarSystem.forName("japanese").asInstanceOf[LocalGregorianCalendar]
  private[chrono] final val LOCALE: Locale = Locale.forLanguageTag("ja-JP-u-ca-japanese")
  /**
   * Singleton instance for Japanese chronology.
   */
  final val INSTANCE: JapaneseChronology = new JapaneseChronology
  /**
   * Serialization version.
   */
  private final val serialVersionUID: Long = 459996390165777884L
}

final class JapaneseChronology extends Chronology with Serializable {
  /**
   * Restricted constructor.
   */
  private def this() {
    this()
  }

  /**
   * Gets the ID of the chronology - 'Japanese'.
   * <p>
   * The ID uniquely identifies the {@code Chronology}.
   * It can be used to lookup the {@code Chronology} using {@link #of(String)}.
   *
   * @return the chronology ID - 'Japanese'
   * @see #getCalendarType()
   */
  def getId: String = {
    return "Japanese"
  }

  /**
   * Gets the calendar type of the underlying calendar system - 'japanese'.
   * <p>
   * The calendar type is an identifier defined by the
   * <em>Unicode Locale Data Markup Language (LDML)</em> specification.
   * It can be used to lookup the {@code Chronology} using {@link #of(String)}.
   * It can also be used as part of a locale, accessible via
   * {@link Locale#getUnicodeLocaleType(String)} with the key 'ca'.
   *
   * @return the calendar system type - 'japanese'
   * @see #getId()
   */
  def getCalendarType: String = {
    return "japanese"
  }

  /**
   * Obtains a local date in Japanese calendar system from the
   * era, year-of-era, month-of-year and day-of-month fields.
   * <p>
   * The Japanese month and day-of-month are the same as those in the
   * ISO calendar system. They are not reset when the era changes.
   * For example:
   * <pre>
   * 6th Jan Showa 64 = ISO 1989-01-06
   * 7th Jan Showa 64 = ISO 1989-01-07
   * 8th Jan Heisei 1 = ISO 1989-01-08
   * 9th Jan Heisei 1 = ISO 1989-01-09
   * </pre>
   *
   * @param era  the Japanese era, not null
   * @param yearOfEra  the year-of-era
   * @param month  the month-of-year
   * @param dayOfMonth  the day-of-month
   * @return the Japanese local date, not null
   * @throws DateTimeException if unable to create the date
   * @throws ClassCastException if the { @code era} is not a { @code JapaneseEra}
   */
  override def date(era: Era, yearOfEra: Int, month: Int, dayOfMonth: Int): JapaneseDate = {
    if (era.isInstanceOf[JapaneseEra] == false) {
      throw new ClassCastException("Era must be JapaneseEra")
    }
    return JapaneseDate.of(era.asInstanceOf[JapaneseEra], yearOfEra, month, dayOfMonth)
  }

  /**
   * Obtains a local date in Japanese calendar system from the
   * proleptic-year, month-of-year and day-of-month fields.
   * <p>
   * The Japanese proleptic year, month and day-of-month are the same as those
   * in the ISO calendar system. They are not reset when the era changes.
   *
   * @param prolepticYear  the proleptic-year
   * @param month  the month-of-year
   * @param dayOfMonth  the day-of-month
   * @return the Japanese local date, not null
   * @throws DateTimeException if unable to create the date
   */
  def date(prolepticYear: Int, month: Int, dayOfMonth: Int): JapaneseDate = {
    return new JapaneseDate(LocalDate.of(prolepticYear, month, dayOfMonth))
  }

  /**
   * Obtains a local date in Japanese calendar system from the
   * era, year-of-era and day-of-year fields.
   * <p>
   * The day-of-year in this factory is expressed relative to the start of the year-of-era.
   * This definition changes the normal meaning of day-of-year only in those years
   * where the year-of-era is reset to one due to a change in the era.
   * For example:
   * <pre>
   * 6th Jan Showa 64 = day-of-year 6
   * 7th Jan Showa 64 = day-of-year 7
   * 8th Jan Heisei 1 = day-of-year 1
   * 9th Jan Heisei 1 = day-of-year 2
   * </pre>
   *
   * @param era  the Japanese era, not null
   * @param yearOfEra  the year-of-era
   * @param dayOfYear  the day-of-year
   * @return the Japanese local date, not null
   * @throws DateTimeException if unable to create the date
   * @throws ClassCastException if the { @code era} is not a { @code JapaneseEra}
   */
  override def dateYearDay(era: Era, yearOfEra: Int, dayOfYear: Int): JapaneseDate = {
    return JapaneseDate.ofYearDay(era.asInstanceOf[JapaneseEra], yearOfEra, dayOfYear)
  }

  /**
   * Obtains a local date in Japanese calendar system from the
   * proleptic-year and day-of-year fields.
   * <p>
   * The day-of-year in this factory is expressed relative to the start of the proleptic year.
   * The Japanese proleptic year and day-of-year are the same as those in the ISO calendar system.
   * They are not reset when the era changes.
   *
   * @param prolepticYear  the proleptic-year
   * @param dayOfYear  the day-of-year
   * @return the Japanese local date, not null
   * @throws DateTimeException if unable to create the date
   */
  def dateYearDay(prolepticYear: Int, dayOfYear: Int): JapaneseDate = {
    return new JapaneseDate(LocalDate.ofYearDay(prolepticYear, dayOfYear))
  }

  /**
   * Obtains a local date in the Japanese calendar system from the epoch-day.
   *
   * @param epochDay  the epoch day
   * @return the Japanese local date, not null
   * @throws DateTimeException if unable to create the date
   */
  def dateEpochDay(epochDay: Long): JapaneseDate = {
    return new JapaneseDate(LocalDate.ofEpochDay(epochDay))
  }

  override def dateNow: JapaneseDate = {
    return dateNow(Clock.systemDefaultZone)
  }

  override def dateNow(zone: ZoneId): JapaneseDate = {
    return dateNow(Clock.system(zone))
  }

  override def dateNow(clock: Clock): JapaneseDate = {
    return date(LocalDate.now(clock))
  }

  def date(temporal: TemporalAccessor): JapaneseDate = {
    if (temporal.isInstanceOf[JapaneseDate]) {
      return temporal.asInstanceOf[JapaneseDate]
    }
    return new JapaneseDate(LocalDate.from(temporal))
  }

  @SuppressWarnings(Array("unchecked")) override def localDateTime(temporal: TemporalAccessor): ChronoLocalDateTime[JapaneseDate] = {
    return super.localDateTime(temporal).asInstanceOf[ChronoLocalDateTime[JapaneseDate]]
  }

  @SuppressWarnings(Array("unchecked")) override def zonedDateTime(temporal: TemporalAccessor): ChronoZonedDateTime[JapaneseDate] = {
    return super.zonedDateTime(temporal).asInstanceOf[ChronoZonedDateTime[JapaneseDate]]
  }

  @SuppressWarnings(Array("unchecked")) override def zonedDateTime(instant: Instant, zone: ZoneId): ChronoZonedDateTime[JapaneseDate] = {
    return super.zonedDateTime(instant, zone).asInstanceOf[ChronoZonedDateTime[JapaneseDate]]
  }

  /**
   * Checks if the specified year is a leap year.
   * <p>
   * Japanese calendar leap years occur exactly in line with ISO leap years.
   * This method does not validate the year passed in, and only has a
   * well-defined result for years in the supported range.
   *
   * @param prolepticYear  the proleptic-year to check, not validated for range
   * @return true if the year is a leap year
   */
  def isLeapYear(prolepticYear: Long): Boolean = {
    return IsoChronology.INSTANCE.isLeapYear(prolepticYear)
  }

  def prolepticYear(era: Era, yearOfEra: Int): Int = {
    if (era.isInstanceOf[JapaneseEra] == false) {
      throw new ClassCastException("Era must be JapaneseEra")
    }
    val jera: JapaneseEra = era.asInstanceOf[JapaneseEra]
    val gregorianYear: Int = jera.getPrivateEra.getSinceDate.getYear + yearOfEra - 1
    if (yearOfEra == 1) {
      return gregorianYear
    }
    if (gregorianYear >= Year.MIN_VALUE && gregorianYear <= Year.MAX_VALUE) {
      val jdate: LocalGregorianCalendar.Date = JCAL.newCalendarDate(null)
      jdate.setEra(jera.getPrivateEra).setDate(yearOfEra, 1, 1)
      if (JapaneseChronology.JCAL.validate(jdate)) {
        return gregorianYear
      }
    }
    throw new DateTimeException("Invalid yearOfEra value")
  }

  /**
   * Returns the calendar system era object from the given numeric value.
   *
   * See the description of each Era for the numeric values of:
   * {@link JapaneseEra#HEISEI}, {@link JapaneseEra#SHOWA},{@link JapaneseEra#TAISHO},
   * {@link JapaneseEra#MEIJI}), only Meiji and later eras are supported.
   *
   * @param eraValue  the era value
   * @return the Japanese { @code Era} for the given numeric era value
   * @throws DateTimeException if { @code eraValue} is invalid
   */
  def eraOf(eraValue: Int): JapaneseEra = {
    return JapaneseEra.of(eraValue)
  }

  def eras: List[Era] = {
    return Arrays.asList[Era](JapaneseEra.values)
  }

  private[chrono] def getCurrentEra: JapaneseEra = {
    val eras: Array[JapaneseEra] = JapaneseEra.values
    return eras(eras.length - 1)
  }

  def range(field: ChronoField): ValueRange = {
    field match {
      case ALIGNED_DAY_OF_WEEK_IN_MONTH =>
      case ALIGNED_DAY_OF_WEEK_IN_YEAR =>
      case ALIGNED_WEEK_OF_MONTH =>
      case ALIGNED_WEEK_OF_YEAR =>
        throw new UnsupportedTemporalTypeException("Unsupported field: " + field)
      case YEAR_OF_ERA => {
        val jcal: Calendar = Calendar.getInstance(LOCALE)
        val startYear: Int = getCurrentEra.getPrivateEra.getSinceDate.getYear
        return ValueRange.of(1, jcal.getGreatestMinimum(Calendar.YEAR), jcal.getLeastMaximum(Calendar.YEAR) + 1, Year.MAX_VALUE - startYear)
      }
      case DAY_OF_YEAR => {
        val jcal: Calendar = Calendar.getInstance(LOCALE)
        val fieldIndex: Int = Calendar.DAY_OF_YEAR
        return ValueRange.of(jcal.getMinimum(fieldIndex), jcal.getGreatestMinimum(fieldIndex), jcal.getLeastMaximum(fieldIndex), jcal.getMaximum(fieldIndex))
      }
      case YEAR =>
        return ValueRange.of(JapaneseDate.MEIJI_6_ISODATE.getYear, Year.MAX_VALUE)
      case ERA =>
        return ValueRange.of(JapaneseEra.MEIJI.getValue, getCurrentEra.getValue)
      case _ =>
        return field.range
    }
  }

  def resolveDate(fieldValues: Map[TemporalField, Long], resolverStyle: ResolverStyle): JapaneseDate = {
    return super.resolveDate(fieldValues, resolverStyle).asInstanceOf[JapaneseDate]
  }

  private[chrono] def resolveYearOfEra(fieldValues: Map[TemporalField, Long], resolverStyle: ResolverStyle): ChronoLocalDate = {
    val eraLong: Long = fieldValues.get(ERA)
    var era: JapaneseEra = null
    if (eraLong != null) {
      era = eraOf(range(ERA).checkValidIntValue(eraLong, ERA))
    }
    val yoeLong: Long = fieldValues.get(YEAR_OF_ERA)
    var yoe: Int = 0
    if (yoeLong != null) {
      yoe = range(YEAR_OF_ERA).checkValidIntValue(yoeLong, YEAR_OF_ERA)
    }
    if (era == null && yoeLong != null && fieldValues.containsKey(YEAR) == false && resolverStyle ne ResolverStyle.STRICT) {
      era = JapaneseEra.values(JapaneseEra.values.length - 1)
    }
    if (yoeLong != null && era != null) {
      if (fieldValues.containsKey(MONTH_OF_YEAR)) {
        if (fieldValues.containsKey(DAY_OF_MONTH)) {
          return resolveYMD(era, yoe, fieldValues, resolverStyle)
        }
      }
      if (fieldValues.containsKey(DAY_OF_YEAR)) {
        return resolveYD(era, yoe, fieldValues, resolverStyle)
      }
    }
    return null
  }

  private def prolepticYearLenient(era: JapaneseEra, yearOfEra: Int): Int = {
    return era.getPrivateEra.getSinceDate.getYear + yearOfEra - 1
  }

  private def resolveYMD(era: JapaneseEra, yoe: Int, fieldValues: Map[TemporalField, Long], resolverStyle: ResolverStyle): ChronoLocalDate = {
    fieldValues.remove(ERA)
    fieldValues.remove(YEAR_OF_ERA)
    if (resolverStyle eq ResolverStyle.LENIENT) {
      val y: Int = prolepticYearLenient(era, yoe)
      val months: Long = Math.subtractExact(fieldValues.remove(MONTH_OF_YEAR), 1)
      val days: Long = Math.subtractExact(fieldValues.remove(DAY_OF_MONTH), 1)
      return date(y, 1, 1).plus(months, MONTHS).plus(days, DAYS)
    }
    val moy: Int = range(MONTH_OF_YEAR).checkValidIntValue(fieldValues.remove(MONTH_OF_YEAR), MONTH_OF_YEAR)
    val dom: Int = range(DAY_OF_MONTH).checkValidIntValue(fieldValues.remove(DAY_OF_MONTH), DAY_OF_MONTH)
    if (resolverStyle eq ResolverStyle.SMART) {
      if (yoe < 1) {
        throw new DateTimeException("Invalid YearOfEra: " + yoe)
      }
      val y: Int = prolepticYearLenient(era, yoe)
      var result: JapaneseDate = null
      try {
        result = date(y, moy, dom)
      }
      catch {
        case ex: DateTimeException => {
          result = date(y, moy, 1).`with`(TemporalAdjuster.lastDayOfMonth)
        }
      }
      if (result.getEra ne era && result.get(YEAR_OF_ERA) > 1 && yoe > 1) {
        throw new DateTimeException("Invalid YearOfEra for Era: " + era + " " + yoe)
      }
      return result
    }
    return date(era, yoe, moy, dom)
  }

  private def resolveYD(era: JapaneseEra, yoe: Int, fieldValues: Map[TemporalField, Long], resolverStyle: ResolverStyle): ChronoLocalDate = {
    fieldValues.remove(ERA)
    fieldValues.remove(YEAR_OF_ERA)
    if (resolverStyle eq ResolverStyle.LENIENT) {
      val y: Int = prolepticYearLenient(era, yoe)
      val days: Long = Math.subtractExact(fieldValues.remove(DAY_OF_YEAR), 1)
      return dateYearDay(y, 1).plus(days, DAYS)
    }
    val doy: Int = range(DAY_OF_YEAR).checkValidIntValue(fieldValues.remove(DAY_OF_YEAR), DAY_OF_YEAR)
    return dateYearDay(era, yoe, doy)
  }
}

/**
 * A date in the Japanese Imperial calendar system.
 * <p>
 * This date operates using the {@linkplain JapaneseChronology Japanese Imperial calendar}.
 * This calendar system is primarily used in Japan.
 * <p>
 * The Japanese Imperial calendar system is the same as the ISO calendar system
 * apart from the era-based year numbering. The proleptic-year is defined to be
 * equal to the ISO proleptic-year.
 * <p>
 * Japan introduced the Gregorian calendar starting with Meiji 6.
 * Only Meiji and later eras are supported;
 * dates before Meiji 6, January 1 are not supported.
 * <p>
 * For example, the Japanese year "Heisei 24" corresponds to ISO year "2012".<br>
 * Calling {@code japaneseDate.get(YEAR_OF_ERA)} will return 24.<br>
 * Calling {@code japaneseDate.get(YEAR)} will return 2012.<br>
 * Calling {@code japaneseDate.get(ERA)} will return 2, corresponding to
 * {@code JapaneseChronology.ERA_HEISEI}.<br>
 *
 * @implSpec
 * This class is immutable and thread-safe.
 *
 * @since 1.8
 */
final object JapaneseDate {
  /**
   * Obtains the current {@code JapaneseDate} from the system clock in the default time-zone.
   * <p>
   * This will query the {@link Clock#systemDefaultZone() system clock} in the default
   * time-zone to obtain the current date.
   * <p>
   * Using this method will prevent the ability to use an alternate clock for testing
   * because the clock is hard-coded.
   *
   * @return the current date using the system clock and default time-zone, not null
   */
  def now: JapaneseDate = {
    return now(Clock.systemDefaultZone)
  }

  /**
   * Obtains the current {@code JapaneseDate} from the system clock in the specified time-zone.
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
  def now(zone: ZoneId): JapaneseDate = {
    return now(Clock.system(zone))
  }

  /**
   * Obtains the current {@code JapaneseDate} from the specified clock.
   * <p>
   * This will query the specified clock to obtain the current date - today.
   * Using this method allows the use of an alternate clock for testing.
   * The alternate clock may be introduced using {@linkplain Clock dependency injection}.
   *
   * @param clock  the clock to use, not null
   * @return the current date, not null
   * @throws DateTimeException if the current date cannot be obtained
   */
  def now(clock: Clock): JapaneseDate = {
    return new JapaneseDate(LocalDate.now(clock))
  }

  /**
   * Obtains a {@code JapaneseDate} representing a date in the Japanese calendar
   * system from the era, year-of-era, month-of-year and day-of-month fields.
   * <p>
   * This returns a {@code JapaneseDate} with the specified fields.
   * The day must be valid for the year and month, otherwise an exception will be thrown.
   * <p>
   * The Japanese month and day-of-month are the same as those in the
   * ISO calendar system. They are not reset when the era changes.
   * For example:
   * <pre>
   * 6th Jan Showa 64 = ISO 1989-01-06
   * 7th Jan Showa 64 = ISO 1989-01-07
   * 8th Jan Heisei 1 = ISO 1989-01-08
   * 9th Jan Heisei 1 = ISO 1989-01-09
   * </pre>
   *
   * @param era  the Japanese era, not null
   * @param yearOfEra  the Japanese year-of-era
   * @param month  the Japanese month-of-year, from 1 to 12
   * @param dayOfMonth  the Japanese day-of-month, from 1 to 31
   * @return the date in Japanese calendar system, not null
   * @throws DateTimeException if the value of any field is out of range,
   *                           or if the day-of-month is invalid for the month-year,
   *                           or if the date is not a Japanese era
   */
  def of(era: JapaneseEra, yearOfEra: Int, month: Int, dayOfMonth: Int): JapaneseDate = {
    Objects.requireNonNull(era, "era")
    val jdate: LocalGregorianCalendar.Date = JapaneseChronology.JCAL.newCalendarDate(null)
    jdate.setEra(era.getPrivateEra).setDate(yearOfEra, month, dayOfMonth)
    if (!JapaneseChronology.JCAL.validate(jdate)) {
      throw new DateTimeException("year, month, and day not valid for Era")
    }
    val date: LocalDate = LocalDate.of(jdate.getNormalizedYear, month, dayOfMonth)
    return new JapaneseDate(era, yearOfEra, date)
  }

  /**
   * Obtains a {@code JapaneseDate} representing a date in the Japanese calendar
   * system from the proleptic-year, month-of-year and day-of-month fields.
   * <p>
   * This returns a {@code JapaneseDate} with the specified fields.
   * The day must be valid for the year and month, otherwise an exception will be thrown.
   * <p>
   * The Japanese proleptic year, month and day-of-month are the same as those
   * in the ISO calendar system. They are not reset when the era changes.
   *
   * @param prolepticYear  the Japanese proleptic-year
   * @param month  the Japanese month-of-year, from 1 to 12
   * @param dayOfMonth  the Japanese day-of-month, from 1 to 31
   * @return the date in Japanese calendar system, not null
   * @throws DateTimeException if the value of any field is out of range,
   *                           or if the day-of-month is invalid for the month-year
   */
  def of(prolepticYear: Int, month: Int, dayOfMonth: Int): JapaneseDate = {
    return new JapaneseDate(LocalDate.of(prolepticYear, month, dayOfMonth))
  }

  /**
   * Obtains a {@code JapaneseDate} representing a date in the Japanese calendar
   * system from the era, year-of-era and day-of-year fields.
   * <p>
   * This returns a {@code JapaneseDate} with the specified fields.
   * The day must be valid for the year, otherwise an exception will be thrown.
   * <p>
   * The day-of-year in this factory is expressed relative to the start of the year-of-era.
   * This definition changes the normal meaning of day-of-year only in those years
   * where the year-of-era is reset to one due to a change in the era.
   * For example:
   * <pre>
   * 6th Jan Showa 64 = day-of-year 6
   * 7th Jan Showa 64 = day-of-year 7
   * 8th Jan Heisei 1 = day-of-year 1
   * 9th Jan Heisei 1 = day-of-year 2
   * </pre>
   *
   * @param era  the Japanese era, not null
   * @param yearOfEra  the Japanese year-of-era
   * @param dayOfYear  the chronology day-of-year, from 1 to 366
   * @return the date in Japanese calendar system, not null
   * @throws DateTimeException if the value of any field is out of range,
   *                           or if the day-of-year is invalid for the year
   */
  private[chrono] def ofYearDay(era: JapaneseEra, yearOfEra: Int, dayOfYear: Int): JapaneseDate = {
    Objects.requireNonNull(era, "era")
    val firstDay: CalendarDate = era.getPrivateEra.getSinceDate
    val jdate: LocalGregorianCalendar.Date = JapaneseChronology.JCAL.newCalendarDate(null)
    jdate.setEra(era.getPrivateEra)
    if (yearOfEra == 1) {
      jdate.setDate(yearOfEra, firstDay.getMonth, firstDay.getDayOfMonth + dayOfYear - 1)
    }
    else {
      jdate.setDate(yearOfEra, 1, dayOfYear)
    }
    JapaneseChronology.JCAL.normalize(jdate)
    if (era.getPrivateEra ne jdate.getEra || yearOfEra != jdate.getYear) {
      throw new DateTimeException("Invalid parameters")
    }
    val localdate: LocalDate = LocalDate.of(jdate.getNormalizedYear, jdate.getMonth, jdate.getDayOfMonth)
    return new JapaneseDate(era, yearOfEra, localdate)
  }

  /**
   * Obtains a {@code JapaneseDate} from a temporal object.
   * <p>
   * This obtains a date in the Japanese calendar system based on the specified temporal.
   * A {@code TemporalAccessor} represents an arbitrary set of date and time information,
   * which this factory converts to an instance of {@code JapaneseDate}.
   * <p>
   * The conversion typically uses the {@link ChronoField#EPOCH_DAY EPOCH_DAY}
   * field, which is standardized across calendar systems.
   * <p>
   * This method matches the signature of the functional interface {@link TemporalQuery}
   * allowing it to be used as a query via method reference, {@code JapaneseDate::from}.
   *
   * @param temporal  the temporal object to convert, not null
   * @return the date in Japanese calendar system, not null
   * @throws DateTimeException if unable to convert to a { @code JapaneseDate}
   */
  def from(temporal: TemporalAccessor): JapaneseDate = {
    return JapaneseChronology.INSTANCE.date(temporal)
  }

  /**
   * Returns a {@code LocalGregorianCalendar.Date} converted from the given {@code isoDate}.
   *
   * @param isoDate  the local date, not null
   * @return a { @code LocalGregorianCalendar.Date}, not null
   */
  private def toPrivateJapaneseDate(isoDate: LocalDate): LocalGregorianCalendar.Date = {
    val jdate: LocalGregorianCalendar.Date = JapaneseChronology.JCAL.newCalendarDate(null)
    val sunEra: Era = JapaneseEra.privateEraFrom(isoDate)
    var year: Int = isoDate.getYear
    if (sunEra != null) {
      year -= sunEra.getSinceDate.getYear - 1
    }
    jdate.setEra(sunEra).setYear(year).setMonth(isoDate.getMonthValue).setDayOfMonth(isoDate.getDayOfMonth)
    JapaneseChronology.JCAL.normalize(jdate)
    return jdate
  }

  private[chrono] def readExternal(in: DataInput): JapaneseDate = {
    val year: Int = in.readInt
    val month: Int = in.readByte
    val dayOfMonth: Int = in.readByte
    return JapaneseChronology.INSTANCE.date(year, month, dayOfMonth)
  }

  /**
   * Serialization version.
   */
  private final val serialVersionUID: Long = -305327627230580483L
  /**
   * The first day supported by the JapaneseChronology is Meiji 6, January 1st.
   */
  private[chrono] final val MEIJI_6_ISODATE: LocalDate = LocalDate.of(1873, 1, 1)
}

final class JapaneseDate extends ChronoLocalDateImpl[JapaneseDate] with ChronoLocalDate with Serializable {
  /**
   * Creates an instance from an ISO date.
   *
   * @param isoDate  the standard local date, validated not null
   */
  private[chrono] def this(isoDate: LocalDate) {
    this()
    if (isoDate.isBefore(MEIJI_6_ISODATE)) {
      throw new DateTimeException("JapaneseDate before Meiji 6 is not supported")
    }
    val jdate: LocalGregorianCalendar.Date = toPrivateJapaneseDate(isoDate)
    this.era = JapaneseEra.toJapaneseEra(jdate.getEra)
    this.yearOfEra = jdate.getYear
    this.isoDate = isoDate
  }

  /**
   * Constructs a {@code JapaneseDate}. This constructor does NOT validate the given parameters,
   * and {@code era} and {@code year} must agree with {@code isoDate}.
   *
   * @param era  the era, validated not null
   * @param year  the year-of-era, validated
   * @param isoDate  the standard local date, validated not null
   */
  private[chrono] def this(era: JapaneseEra, year: Int, isoDate: LocalDate) {
    this()
    if (isoDate.isBefore(MEIJI_6_ISODATE)) {
      throw new DateTimeException("JapaneseDate before Meiji 6 is not supported")
    }
    this.era = era
    this.yearOfEra = year
    this.isoDate = isoDate
  }

  /**
   * Gets the chronology of this date, which is the Japanese calendar system.
   * <p>
   * The {@code Chronology} represents the calendar system in use.
   * The era and other fields in {@link ChronoField} are defined by the chronology.
   *
   * @return the Japanese chronology, not null
   */
  def getChronology: JapaneseChronology = {
    return JapaneseChronology.INSTANCE
  }

  /**
   * Gets the era applicable at this date.
   * <p>
   * The Japanese calendar system has multiple eras defined by {@link JapaneseEra}.
   *
   * @return the era applicable at this date, not null
   */
  override def getEra: JapaneseEra = {
    return era
  }

  /**
   * Returns the length of the month represented by this date.
   * <p>
   * This returns the length of the month in days.
   * Month lengths match those of the ISO calendar system.
   *
   * @return the length of the month in days
   */
  def lengthOfMonth: Int = {
    return isoDate.lengthOfMonth
  }

  override def lengthOfYear: Int = {
    val jcal: Calendar = Calendar.getInstance(JapaneseChronology.LOCALE)
    jcal.set(Calendar.ERA, era.getValue + JapaneseEra.ERA_OFFSET)
    jcal.set(yearOfEra, isoDate.getMonthValue - 1, isoDate.getDayOfMonth)
    return jcal.getActualMaximum(Calendar.DAY_OF_YEAR)
  }

  /**
   * Checks if the specified field is supported.
   * <p>
   * This checks if this date can be queried for the specified field.
   * If false, then calling the {@link #range(TemporalField) range} and
   * {@link #get(TemporalField) get} methods will throw an exception.
   * <p>
   * If the field is a {@link ChronoField} then the query is implemented here.
   * The supported fields are:
   * <ul>
   * <li>{@code DAY_OF_WEEK}
   * <li>{@code DAY_OF_MONTH}
   * <li>{@code DAY_OF_YEAR}
   * <li>{@code EPOCH_DAY}
   * <li>{@code MONTH_OF_YEAR}
   * <li>{@code PROLEPTIC_MONTH}
   * <li>{@code YEAR_OF_ERA}
   * <li>{@code YEAR}
   * <li>{@code ERA}
   * </ul>
   * All other {@code ChronoField} instances will return false.
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
    if (field eq ALIGNED_DAY_OF_WEEK_IN_MONTH || field eq ALIGNED_DAY_OF_WEEK_IN_YEAR || field eq ALIGNED_WEEK_OF_MONTH || field eq ALIGNED_WEEK_OF_YEAR) {
      return false
    }
    return ChronoLocalDate.super.isSupported(field)
  }

  override def range(field: TemporalField): ValueRange = {
    if (field.isInstanceOf[ChronoField]) {
      if (isSupported(field)) {
        val f: ChronoField = field.asInstanceOf[ChronoField]
        f match {
          case DAY_OF_MONTH =>
            return ValueRange.of(1, lengthOfMonth)
          case DAY_OF_YEAR =>
            return ValueRange.of(1, lengthOfYear)
          case YEAR_OF_ERA => {
            val jcal: Calendar = Calendar.getInstance(JapaneseChronology.LOCALE)
            jcal.set(Calendar.ERA, era.getValue + JapaneseEra.ERA_OFFSET)
            jcal.set(yearOfEra, isoDate.getMonthValue - 1, isoDate.getDayOfMonth)
            return ValueRange.of(1, jcal.getActualMaximum(Calendar.YEAR))
          }
        }
        return getChronology.range(f)
      }
      throw new UnsupportedTemporalTypeException("Unsupported field: " + field)
    }
    return field.rangeRefinedBy(this)
  }

  def getLong(field: TemporalField): Long = {
    if (field.isInstanceOf[ChronoField]) {
      field.asInstanceOf[ChronoField] match {
        case ALIGNED_DAY_OF_WEEK_IN_MONTH =>
        case ALIGNED_DAY_OF_WEEK_IN_YEAR =>
        case ALIGNED_WEEK_OF_MONTH =>
        case ALIGNED_WEEK_OF_YEAR =>
          throw new UnsupportedTemporalTypeException("Unsupported field: " + field)
        case YEAR_OF_ERA =>
          return yearOfEra
        case ERA =>
          return era.getValue
        case DAY_OF_YEAR =>
          val jcal: Calendar = Calendar.getInstance(JapaneseChronology.LOCALE)
          jcal.set(Calendar.ERA, era.getValue + JapaneseEra.ERA_OFFSET)
          jcal.set(yearOfEra, isoDate.getMonthValue - 1, isoDate.getDayOfMonth)
          return jcal.get(Calendar.DAY_OF_YEAR)
      }
      return isoDate.getLong(field)
    }
    return field.getFrom(this)
  }

  override def `with`(field: TemporalField, newValue: Long): JapaneseDate = {
    if (field.isInstanceOf[ChronoField]) {
      val f: ChronoField = field.asInstanceOf[ChronoField]
      if (getLong(f) == newValue) {
        return this
      }
      f match {
        case YEAR_OF_ERA =>
        case YEAR =>
        case ERA => {
          val nvalue: Int = getChronology.range(f).checkValidIntValue(newValue, f)
          f match {
            case YEAR_OF_ERA =>
              return this.withYear(nvalue)
            case YEAR =>
              return `with`(isoDate.withYear(nvalue))
            case ERA => {
              return this.withYear(JapaneseEra.of(nvalue), yearOfEra)
            }
          }
        }
      }
      return `with`(isoDate.`with`(field, newValue))
    }
    return super.`with`(field, newValue)
  }

  /**
   * {@inheritDoc}
   * @throws DateTimeException { @inheritDoc}
   * @throws ArithmeticException { @inheritDoc}
   */
  override def `with`(adjuster: TemporalAdjuster): JapaneseDate = {
    return super.`with`(adjuster)
  }

  /**
   * {@inheritDoc}
   * @throws DateTimeException { @inheritDoc}
   * @throws ArithmeticException { @inheritDoc}
   */
  override def plus(amount: TemporalAmount): JapaneseDate = {
    return super.plus(amount)
  }

  /**
   * {@inheritDoc}
   * @throws DateTimeException { @inheritDoc}
   * @throws ArithmeticException { @inheritDoc}
   */
  override def minus(amount: TemporalAmount): JapaneseDate = {
    return super.minus(amount)
  }

  /**
   * Returns a copy of this date with the year altered.
   * <p>
   * This method changes the year of the date.
   * If the month-day is invalid for the year, then the previous valid day
   * will be selected instead.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param era  the era to set in the result, not null
   * @param yearOfEra  the year-of-era to set in the returned date
   * @return a { @code JapaneseDate} based on this date with the requested year, never null
   * @throws DateTimeException if { @code year} is invalid
   */
  private def withYear(era: JapaneseEra, yearOfEra: Int): JapaneseDate = {
    val year: Int = JapaneseChronology.INSTANCE.prolepticYear(era, yearOfEra)
    return `with`(isoDate.withYear(year))
  }

  /**
   * Returns a copy of this date with the year-of-era altered.
   * <p>
   * This method changes the year-of-era of the date.
   * If the month-day is invalid for the year, then the previous valid day
   * will be selected instead.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param year  the year to set in the returned date
   * @return a { @code JapaneseDate} based on this date with the requested year-of-era, never null
   * @throws DateTimeException if { @code year} is invalid
   */
  private def withYear(year: Int): JapaneseDate = {
    return withYear(getEra, year)
  }

  private[chrono] def plusYears(years: Long): JapaneseDate = {
    return `with`(isoDate.plusYears(years))
  }

  private[chrono] def plusMonths(months: Long): JapaneseDate = {
    return `with`(isoDate.plusMonths(months))
  }

  private[chrono] override def plusWeeks(weeksToAdd: Long): JapaneseDate = {
    return `with`(isoDate.plusWeeks(weeksToAdd))
  }

  private[chrono] def plusDays(days: Long): JapaneseDate = {
    return `with`(isoDate.plusDays(days))
  }

  override def plus(amountToAdd: Long, unit: TemporalUnit): JapaneseDate = {
    return super.plus(amountToAdd, unit)
  }

  override def minus(amountToAdd: Long, unit: TemporalUnit): JapaneseDate = {
    return super.minus(amountToAdd, unit)
  }

  private[chrono] override def minusYears(yearsToSubtract: Long): JapaneseDate = {
    return super.minusYears(yearsToSubtract)
  }

  private[chrono] override def minusMonths(monthsToSubtract: Long): JapaneseDate = {
    return super.minusMonths(monthsToSubtract)
  }

  private[chrono] override def minusWeeks(weeksToSubtract: Long): JapaneseDate = {
    return super.minusWeeks(weeksToSubtract)
  }

  private[chrono] override def minusDays(daysToSubtract: Long): JapaneseDate = {
    return super.minusDays(daysToSubtract)
  }

  private def `with`(newDate: LocalDate): JapaneseDate = {
    return (if ((newDate == isoDate)) this else new JapaneseDate(newDate))
  }

  @SuppressWarnings(Array("unchecked")) final override def atTime(localTime: LocalTime): ChronoLocalDateTime[JapaneseDate] = {
    return super.atTime(localTime).asInstanceOf[ChronoLocalDateTime[JapaneseDate]]
  }

  def until(endDate: ChronoLocalDate): ChronoPeriod = {
    val period: Period = isoDate.until(endDate)
    return getChronology.period(period.getYears, period.getMonths, period.getDays)
  }

  override def toEpochDay: Long = {
    return isoDate.toEpochDay
  }

  override def equals(obj: AnyRef): Boolean = {
    if (this eq obj) {
      return true
    }
    if (obj.isInstanceOf[JapaneseDate]) {
      val otherDate: JapaneseDate = obj.asInstanceOf[JapaneseDate]
      return this.isoDate == otherDate.isoDate
    }
    return false
  }

  override def hashCode: Int = {
    return getChronology.getId.hashCode ^ isoDate.hashCode
  }

  private def writeReplace: AnyRef = {
    return new Ser(Ser.JAPANESE_DATE_TYPE, this)
  }

  private[chrono] def writeExternal(out: DataOutput) {
    out.writeInt(get(YEAR))
    out.writeByte(get(MONTH_OF_YEAR))
    out.writeByte(get(DAY_OF_MONTH))
  }

  /**
   * The underlying ISO local date.
   */
  @transient
  private final val isoDate: LocalDate = null
  /**
   * The JapaneseEra of this date.
   */
  @transient
  private var era: JapaneseEra = null
  /**
   * The Japanese imperial calendar year of this date.
   */
  @transient
  private var yearOfEra: Int = 0
}

/**
 * An era in the Japanese Imperial calendar system.
 * <p>
 * This class defines the valid eras for the Japanese chronology.
 * Japan introduced the Gregorian calendar starting with Meiji 6.
 * Only Meiji and later eras are supported;
 * dates before Meiji 6, January 1 are not supported.
 *
 * @implSpec
 * This class is immutable and thread-safe.
 *
 * @since 1.8
 */
final object JapaneseEra {
  /**
   * Obtains an instance of {@code JapaneseEra} from an {@code int} value.
   * <p>
   * The {@link #SHOWA} era that contains 1970-01-01 (ISO calendar system) has the value 1
   * Later era is numbered 2 ({@link #HEISEI}). Earlier eras are numbered 0 ({@link #TAISHO}),
   * -1 ({@link #MEIJI}), only Meiji and later eras are supported.
   *
   * @param japaneseEra  the era to represent
   * @return the { @code JapaneseEra} singleton, not null
   * @throws DateTimeException if the value is invalid
   */
  def of(japaneseEra: Int): JapaneseEra = {
    if (japaneseEra < MEIJI.eraValue || japaneseEra > HEISEI.eraValue) {
      throw new DateTimeException("Invalid era: " + japaneseEra)
    }
    return KNOWN_ERAS(ordinal(japaneseEra))
  }

  /**
   * Returns the {@code JapaneseEra} with the name.
   * <p>
   * The string must match exactly the name of the era.
   * (Extraneous whitespace characters are not permitted.)
   *
   * @param japaneseEra  the japaneseEra name; non-null
   * @return the { @code JapaneseEra} singleton, never null
   * @throws IllegalArgumentException if there is not JapaneseEra with the specified name
   */
  def valueOf(japaneseEra: String): JapaneseEra = {
    Objects.requireNonNull(japaneseEra, "japaneseEra")
    for (era <- KNOWN_ERAS) {
      if (era.getName == japaneseEra) {
        return era
      }
    }
    throw new IllegalArgumentException("japaneseEra is invalid")
  }

  /**
   * Returns an array of JapaneseEras.
   * <p>
   * This method may be used to iterate over the JapaneseEras as follows:
   * <pre>
   * for (JapaneseEra c : JapaneseEra.values())
   * System.out.println(c);
   * </pre>
   *
   * @return an array of JapaneseEras
   */
  def values: Array[JapaneseEra] = {
    return Arrays.copyOf(KNOWN_ERAS, KNOWN_ERAS.length)
  }

  /**
   * Obtains an instance of {@code JapaneseEra} from a date.
   *
   * @param date  the date, not null
   * @return the Era singleton, never null
   */
  private[chrono] def from(date: LocalDate): JapaneseEra = {
    if (date.isBefore(MEIJI_6_ISODATE)) {
      throw new DateTimeException("JapaneseDate before Meiji 6 are not supported")
    }
    {
      var i: Int = KNOWN_ERAS.length - 1
      while (i > 0) {
        {
          val era: JapaneseEra = KNOWN_ERAS(i)
          if (date.compareTo(era.since) >= 0) {
            return era
          }
        }
        ({
          i -= 1; i + 1
        })
      }
    }
    return null
  }

  private[chrono] def toJapaneseEra(privateEra: Era): JapaneseEra = {
    {
      var i: Int = ERA_CONFIG.length - 1
      while (i >= 0) {
        {
          if (ERA_CONFIG(i) == privateEra) {
            return KNOWN_ERAS(i)
          }
        }
        ({
          i -= 1; i + 1
        })
      }
    }
    return null
  }

  private[chrono] def privateEraFrom(isoDate: LocalDate): Era = {
    {
      var i: Int = KNOWN_ERAS.length - 1
      while (i > 0) {
        {
          val era: JapaneseEra = KNOWN_ERAS(i)
          if (isoDate.compareTo(era.since) >= 0) {
            return ERA_CONFIG(i)
          }
        }
        ({
          i -= 1; i + 1
        })
      }
    }
    return null
  }

  /**
   * Returns the index into the arrays from the Era value.
   * the eraValue is a valid Era number, -1..2.
   *
   * @param eraValue  the era value to convert to the index
   * @return the index of the current Era
   */
  private def ordinal(eraValue: Int): Int = {
    return eraValue + ERA_OFFSET - 1
  }

  private[chrono] def readExternal(in: DataInput): JapaneseEra = {
    val eraValue: Byte = in.readByte
    return JapaneseEra.of(eraValue)
  }

  private[chrono] final val ERA_OFFSET: Int = 2
  private[chrono] final val ERA_CONFIG: Array[Era] = null
  /**
   * The singleton instance for the 'Meiji' era (1868-09-08 - 1912-07-29)
   * which has the value -1.
   */
  final val MEIJI: JapaneseEra = new JapaneseEra(-1, LocalDate.of(1868, 1, 1))
  /**
   * The singleton instance for the 'Taisho' era (1912-07-30 - 1926-12-24)
   * which has the value 0.
   */
  final val TAISHO: JapaneseEra = new JapaneseEra(0, LocalDate.of(1912, 7, 30))
  /**
   * The singleton instance for the 'Showa' era (1926-12-25 - 1989-01-07)
   * which has the value 1.
   */
  final val SHOWA: JapaneseEra = new JapaneseEra(1, LocalDate.of(1926, 12, 25))
  /**
   * The singleton instance for the 'Heisei' era (1989-01-08 - current)
   * which has the value 2.
   */
  final val HEISEI: JapaneseEra = new JapaneseEra(2, LocalDate.of(1989, 1, 8))
  private final val N_ERA_CONSTANTS: Int = HEISEI.getValue + ERA_OFFSET + 1
  /**
   * Serialization version.
   */
  private final val serialVersionUID: Long = 1466499369062886794L
  private final val KNOWN_ERAS: Array[JapaneseEra] = null
}

final class JapaneseEra extends Era with Serializable {
  /**
   * Creates an instance.
   *
   * @param eraValue  the era value, validated
   * @param since  the date representing the first date of the era, validated not null
   */
  private def this(eraValue: Int, since: LocalDate) {
    this()
    this.eraValue = eraValue
    this.since = since
  }

  /**
   * Returns the singleton {@code JapaneseEra} corresponding to this object.
   * It's possible that this version of {@code JapaneseEra} doesn't support the latest era value.
   * In that case, this method throws an {@code ObjectStreamException}.
   *
   * @return the singleton { @code JapaneseEra} for this object
   * @throws ObjectStreamException if the deserialized object has any unknown numeric era value.
   */
  private def readResolve: AnyRef = {
    try {
      return of(eraValue)
    }
    catch {
      case e: DateTimeException => {
        val ex: InvalidObjectException = new InvalidObjectException("Invalid era")
        ex.initCause(e)
        throw ex
      }
    }
  }

  /**
   * Returns the Sun private Era instance corresponding to this {@code JapaneseEra}.
   *
   * @return the Sun private Era instance for this { @code JapaneseEra}.
   */
  private[chrono] def getPrivateEra: Era = {
    return ERA_CONFIG(ordinal(eraValue))
  }

  /**
   * Gets the numeric era {@code int} value.
   * <p>
   * The {@link #SHOWA} era that contains 1970-01-01 (ISO calendar system) has the value 1.
   * Later eras are numbered from 2 ({@link #HEISEI}).
   * Earlier eras are numbered 0 ({@link #TAISHO}), -1 ({@link #MEIJI})).
   *
   * @return the era value
   */
  def getValue: Int = {
    return eraValue
  }

  /**
   * Gets the range of valid values for the specified field.
   * <p>
   * The range object expresses the minimum and maximum valid values for a field.
   * This era is used to enhance the accuracy of the returned range.
   * If it is not possible to return the range, because the field is not supported
   * or for some other reason, an exception is thrown.
   * <p>
   * If the field is a {@link ChronoField} then the query is implemented here.
   * The {@code ERA} field returns the range.
   * All other {@code ChronoField} instances will throw an {@code UnsupportedTemporalTypeException}.
   * <p>
   * If the field is not a {@code ChronoField}, then the result of this method
   * is obtained by invoking {@code TemporalField.rangeRefinedBy(TemporalAccessor)}
   * passing {@code this} as the argument.
   * Whether the range can be obtained is determined by the field.
   * <p>
   * The range of valid Japanese eras can change over time due to the nature
   * of the Japanese calendar system.
   *
   * @param field  the field to query the range for, not null
   * @return the range of valid values for the field, not null
   * @throws DateTimeException if the range for the field cannot be obtained
   * @throws UnsupportedTemporalTypeException if the unit is not supported
   */
  override def range(field: TemporalField): ValueRange = {
    if (field eq ERA) {
      return JapaneseChronology.INSTANCE.range(ERA)
    }
    return Era.super.range(field)
  }

  private[chrono] def getAbbreviation: String = {
    val index: Int = ordinal(getValue)
    if (index == 0) {
      return ""
    }
    return ERA_CONFIG(index).getAbbreviation
  }

  private[chrono] def getName: String = {
    return ERA_CONFIG(ordinal(getValue)).getName
  }

  override def toString: String = {
    return getName
  }

  private def writeReplace: AnyRef = {
    return new Ser(Ser.JAPANESE_ERA_TYPE, this)
  }

  private[chrono] def writeExternal(out: DataOutput) {
    out.writeByte(this.getValue)
  }

  /**
   * The era value.
   * @serial
   */
  private final val eraValue: Int = 0
  @transient
  private final val since: LocalDate = null
}

