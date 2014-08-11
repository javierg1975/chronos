package metronome.chrono

/**
 * The Minguo calendar system.
 * <p>
 * This chronology defines the rules of the Minguo calendar system.
 * This calendar system is primarily used in the Republic of China, often known as Taiwan.
 * Dates are aligned such that {@code 0001-01-01 (Minguo)} is {@code 1912-01-01 (ISO)}.
 * <p>
 * The fields are defined as follows:
 * <p><ul>
 * <li>era - There are two eras, the current 'Republic' (ERA_ROC) and the previous era (ERA_BEFORE_ROC).
 * <li>year-of-era - The year-of-era for the current era increases uniformly from the epoch at year one.
 * For the previous era the year increases from one as time goes backwards.
 * The value for the current era is equal to the ISO proleptic-year minus 1911.
 * <li>proleptic-year - The proleptic year is the same as the year-of-era for the
 * current era. For the previous era, years have zero, then negative values.
 * The value is equal to the ISO proleptic-year minus 1911.
 * <li>month-of-year - The Minguo month-of-year exactly matches ISO.
 * <li>day-of-month - The Minguo day-of-month exactly matches ISO.
 * <li>day-of-year - The Minguo day-of-year exactly matches ISO.
 * <li>leap-year - The Minguo leap-year pattern exactly matches ISO, such that the two calendars
 * are never out of step.
 * </ul><p>
 *
 * @implSpec
 * This class is immutable and thread-safe.
 *
 * @since 1.8
 */
final object MinguoChronology {
  /**
   * Singleton instance for the Minguo chronology.
   */
  final val INSTANCE: MinguoChronology = new MinguoChronology
  /**
   * Serialization version.
   */
  private final val serialVersionUID: Long = 1039765215346859963L
  /**
   * The difference in years between ISO and Minguo.
   */
  private[chrono] final val YEARS_DIFFERENCE: Int = 1911
}

final class MinguoChronology extends Chronology with Serializable {
  /**
   * Restricted constructor.
   */
  private def this() {
    this()
  }

  /**
   * Gets the ID of the chronology - 'Minguo'.
   * <p>
   * The ID uniquely identifies the {@code Chronology}.
   * It can be used to lookup the {@code Chronology} using {@link #of(String)}.
   *
   * @return the chronology ID - 'Minguo'
   * @see #getCalendarType()
   */
  def getId: String = {
    return "Minguo"
  }

  /**
   * Gets the calendar type of the underlying calendar system - 'roc'.
   * <p>
   * The calendar type is an identifier defined by the
   * <em>Unicode Locale Data Markup Language (LDML)</em> specification.
   * It can be used to lookup the {@code Chronology} using {@link #of(String)}.
   * It can also be used as part of a locale, accessible via
   * {@link Locale#getUnicodeLocaleType(String)} with the key 'ca'.
   *
   * @return the calendar system type - 'roc'
   * @see #getId()
   */
  def getCalendarType: String = {
    return "roc"
  }

  /**
   * Obtains a local date in Minguo calendar system from the
   * era, year-of-era, month-of-year and day-of-month fields.
   *
   * @param era  the Minguo era, not null
   * @param yearOfEra  the year-of-era
   * @param month  the month-of-year
   * @param dayOfMonth  the day-of-month
   * @return the Minguo local date, not null
   * @throws DateTimeException if unable to create the date
   * @throws ClassCastException if the { @code era} is not a { @code MinguoEra}
   */
  override def date(era: Era, yearOfEra: Int, month: Int, dayOfMonth: Int): MinguoDate = {
    return date(prolepticYear(era, yearOfEra), month, dayOfMonth)
  }

  /**
   * Obtains a local date in Minguo calendar system from the
   * proleptic-year, month-of-year and day-of-month fields.
   *
   * @param prolepticYear  the proleptic-year
   * @param month  the month-of-year
   * @param dayOfMonth  the day-of-month
   * @return the Minguo local date, not null
   * @throws DateTimeException if unable to create the date
   */
  def date(prolepticYear: Int, month: Int, dayOfMonth: Int): MinguoDate = {
    return new MinguoDate(LocalDate.of(prolepticYear + YEARS_DIFFERENCE, month, dayOfMonth))
  }

  /**
   * Obtains a local date in Minguo calendar system from the
   * era, year-of-era and day-of-year fields.
   *
   * @param era  the Minguo era, not null
   * @param yearOfEra  the year-of-era
   * @param dayOfYear  the day-of-year
   * @return the Minguo local date, not null
   * @throws DateTimeException if unable to create the date
   * @throws ClassCastException if the { @code era} is not a { @code MinguoEra}
   */
  override def dateYearDay(era: Era, yearOfEra: Int, dayOfYear: Int): MinguoDate = {
    return dateYearDay(prolepticYear(era, yearOfEra), dayOfYear)
  }

  /**
   * Obtains a local date in Minguo calendar system from the
   * proleptic-year and day-of-year fields.
   *
   * @param prolepticYear  the proleptic-year
   * @param dayOfYear  the day-of-year
   * @return the Minguo local date, not null
   * @throws DateTimeException if unable to create the date
   */
  def dateYearDay(prolepticYear: Int, dayOfYear: Int): MinguoDate = {
    return new MinguoDate(LocalDate.ofYearDay(prolepticYear + YEARS_DIFFERENCE, dayOfYear))
  }

  /**
   * Obtains a local date in the Minguo calendar system from the epoch-day.
   *
   * @param epochDay  the epoch day
   * @return the Minguo local date, not null
   * @throws DateTimeException if unable to create the date
   */
  def dateEpochDay(epochDay: Long): MinguoDate = {
    return new MinguoDate(LocalDate.ofEpochDay(epochDay))
  }

  override def dateNow: MinguoDate = {
    return dateNow(Clock.systemDefaultZone)
  }

  override def dateNow(zone: ZoneId): MinguoDate = {
    return dateNow(Clock.system(zone))
  }

  override def dateNow(clock: Clock): MinguoDate = {
    return date(LocalDate.now(clock))
  }

  def date(temporal: TemporalAccessor): MinguoDate = {
    if (temporal.isInstanceOf[MinguoDate]) {
      return temporal.asInstanceOf[MinguoDate]
    }
    return new MinguoDate(LocalDate.from(temporal))
  }

  @SuppressWarnings(Array("unchecked")) override def localDateTime(temporal: TemporalAccessor): ChronoLocalDateTime[MinguoDate] = {
    return super.localDateTime(temporal).asInstanceOf[ChronoLocalDateTime[MinguoDate]]
  }

  @SuppressWarnings(Array("unchecked")) override def zonedDateTime(temporal: TemporalAccessor): ChronoZonedDateTime[MinguoDate] = {
    return super.zonedDateTime(temporal).asInstanceOf[ChronoZonedDateTime[MinguoDate]]
  }

  @SuppressWarnings(Array("unchecked")) override def zonedDateTime(instant: Instant, zone: ZoneId): ChronoZonedDateTime[MinguoDate] = {
    return super.zonedDateTime(instant, zone).asInstanceOf[ChronoZonedDateTime[MinguoDate]]
  }

  /**
   * Checks if the specified year is a leap year.
   * <p>
   * Minguo leap years occur exactly in line with ISO leap years.
   * This method does not validate the year passed in, and only has a
   * well-defined result for years in the supported range.
   *
   * @param prolepticYear  the proleptic-year to check, not validated for range
   * @return true if the year is a leap year
   */
  def isLeapYear(prolepticYear: Long): Boolean = {
    return IsoChronology.INSTANCE.isLeapYear(prolepticYear + YEARS_DIFFERENCE)
  }

  def prolepticYear(era: Era, yearOfEra: Int): Int = {
    if (era.isInstanceOf[MinguoEra] == false) {
      throw new ClassCastException("Era must be MinguoEra")
    }
    return (if (era eq MinguoEra.ROC) yearOfEra else 1 - yearOfEra)
  }

  def eraOf(eraValue: Int): MinguoEra = {
    return MinguoEra.of(eraValue)
  }

  def eras: List[Era] = {
    return Arrays.asList[Era](MinguoEra.values)
  }

  def range(field: ChronoField): ValueRange = {
    field match {
      case PROLEPTIC_MONTH => {
        val range: ValueRange = PROLEPTIC_MONTH.range
        return ValueRange.of(range.getMinimum - YEARS_DIFFERENCE * 12L, range.getMaximum - YEARS_DIFFERENCE * 12L)
      }
      case YEAR_OF_ERA => {
        val range: ValueRange = YEAR.range
        return ValueRange.of(1, range.getMaximum - YEARS_DIFFERENCE, -range.getMinimum + 1 + YEARS_DIFFERENCE)
      }
      case YEAR => {
        val range: ValueRange = YEAR.range
        return ValueRange.of(range.getMinimum - YEARS_DIFFERENCE, range.getMaximum - YEARS_DIFFERENCE)
      }
    }
    return field.range
  }

  def resolveDate(fieldValues: Map[TemporalField, Long], resolverStyle: ResolverStyle): MinguoDate = {
    return super.resolveDate(fieldValues, resolverStyle).asInstanceOf[MinguoDate]
  }
}

/**
 * A date in the Minguo calendar system.
 * <p>
 * This date operates using the {@linkplain MinguoChronology Minguo calendar}.
 * This calendar system is primarily used in the Republic of China, often known as Taiwan.
 * Dates are aligned such that {@code 0001-01-01 (Minguo)} is {@code 1912-01-01 (ISO)}.
 *
 * @implSpec
 * This class is immutable and thread-safe.
 *
 * @since 1.8
 */
final object MinguoDate {
  /**
   * Obtains the current {@code MinguoDate} from the system clock in the default time-zone.
   * <p>
   * This will query the {@link Clock#systemDefaultZone() system clock} in the default
   * time-zone to obtain the current date.
   * <p>
   * Using this method will prevent the ability to use an alternate clock for testing
   * because the clock is hard-coded.
   *
   * @return the current date using the system clock and default time-zone, not null
   */
  def now: MinguoDate = {
    return now(Clock.systemDefaultZone)
  }

  /**
   * Obtains the current {@code MinguoDate} from the system clock in the specified time-zone.
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
  def now(zone: ZoneId): MinguoDate = {
    return now(Clock.system(zone))
  }

  /**
   * Obtains the current {@code MinguoDate} from the specified clock.
   * <p>
   * This will query the specified clock to obtain the current date - today.
   * Using this method allows the use of an alternate clock for testing.
   * The alternate clock may be introduced using {@linkplain Clock dependency injection}.
   *
   * @param clock  the clock to use, not null
   * @return the current date, not null
   * @throws DateTimeException if the current date cannot be obtained
   */
  def now(clock: Clock): MinguoDate = {
    return new MinguoDate(LocalDate.now(clock))
  }

  /**
   * Obtains a {@code MinguoDate} representing a date in the Minguo calendar
   * system from the proleptic-year, month-of-year and day-of-month fields.
   * <p>
   * This returns a {@code MinguoDate} with the specified fields.
   * The day must be valid for the year and month, otherwise an exception will be thrown.
   *
   * @param prolepticYear  the Minguo proleptic-year
   * @param month  the Minguo month-of-year, from 1 to 12
   * @param dayOfMonth  the Minguo day-of-month, from 1 to 31
   * @return the date in Minguo calendar system, not null
   * @throws DateTimeException if the value of any field is out of range,
   *                           or if the day-of-month is invalid for the month-year
   */
  def of(prolepticYear: Int, month: Int, dayOfMonth: Int): MinguoDate = {
    return new MinguoDate(LocalDate.of(prolepticYear + YEARS_DIFFERENCE, month, dayOfMonth))
  }

  /**
   * Obtains a {@code MinguoDate} from a temporal object.
   * <p>
   * This obtains a date in the Minguo calendar system based on the specified temporal.
   * A {@code TemporalAccessor} represents an arbitrary set of date and time information,
   * which this factory converts to an instance of {@code MinguoDate}.
   * <p>
   * The conversion typically uses the {@link ChronoField#EPOCH_DAY EPOCH_DAY}
   * field, which is standardized across calendar systems.
   * <p>
   * This method matches the signature of the functional interface {@link TemporalQuery}
   * allowing it to be used as a query via method reference, {@code MinguoDate::from}.
   *
   * @param temporal  the temporal object to convert, not null
   * @return the date in Minguo calendar system, not null
   * @throws DateTimeException if unable to convert to a { @code MinguoDate}
   */
  def from(temporal: TemporalAccessor): MinguoDate = {
    return MinguoChronology.INSTANCE.date(temporal)
  }

  private[chrono] def readExternal(in: DataInput): MinguoDate = {
    val year: Int = in.readInt
    val month: Int = in.readByte
    val dayOfMonth: Int = in.readByte
    return MinguoChronology.INSTANCE.date(year, month, dayOfMonth)
  }

  /**
   * Serialization version.
   */
  private final val serialVersionUID: Long = 1300372329181994526L
}

final class MinguoDate extends ChronoLocalDateImpl[MinguoDate] with ChronoLocalDate with Serializable {
  /**
   * Creates an instance from an ISO date.
   *
   * @param isoDate  the standard local date, validated not null
   */
  private[chrono] def this(isoDate: LocalDate) {
    this()
    Objects.requireNonNull(isoDate, "isoDate")
    this.isoDate = isoDate
  }

  /**
   * Gets the chronology of this date, which is the Minguo calendar system.
   * <p>
   * The {@code Chronology} represents the calendar system in use.
   * The era and other fields in {@link ChronoField} are defined by the chronology.
   *
   * @return the Minguo chronology, not null
   */
  def getChronology: MinguoChronology = {
    return MinguoChronology.INSTANCE
  }

  /**
   * Gets the era applicable at this date.
   * <p>
   * The Minguo calendar system has two eras, 'ROC' and 'BEFORE_ROC',
   * defined by {@link MinguoEra}.
   *
   * @return the era applicable at this date, not null
   */
  override def getEra: MinguoEra = {
    return (if (getProlepticYear >= 1) MinguoEra.ROC else MinguoEra.BEFORE_ROC)
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

  override def range(field: TemporalField): ValueRange = {
    if (field.isInstanceOf[ChronoField]) {
      if (isSupported(field)) {
        val f: ChronoField = field.asInstanceOf[ChronoField]
        f match {
          case DAY_OF_MONTH =>
          case DAY_OF_YEAR =>
          case ALIGNED_WEEK_OF_MONTH =>
            return isoDate.range(field)
          case YEAR_OF_ERA => {
            val range: ValueRange = YEAR.range
            val max: Long = (if (getProlepticYear <= 0) -range.getMinimum + 1 + YEARS_DIFFERENCE else range.getMaximum - YEARS_DIFFERENCE)
            return ValueRange.of(1, max)
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
        case PROLEPTIC_MONTH =>
          return getProlepticMonth
        case YEAR_OF_ERA => {
          val prolepticYear: Int = getProlepticYear
          return (if (prolepticYear >= 1) prolepticYear else 1 - prolepticYear)
        }
        case YEAR =>
          return getProlepticYear
        case ERA =>
          return (if (getProlepticYear >= 1) 1 else 0)
      }
      return isoDate.getLong(field)
    }
    return field.getFrom(this)
  }

  private def getProlepticMonth: Long = {
    return getProlepticYear * 12L + isoDate.getMonthValue - 1
  }

  private def getProlepticYear: Int = {
    return isoDate.getYear - YEARS_DIFFERENCE
  }

  override def `with`(field: TemporalField, newValue: Long): MinguoDate = {
    if (field.isInstanceOf[ChronoField]) {
      val f: ChronoField = field.asInstanceOf[ChronoField]
      if (getLong(f) == newValue) {
        return this
      }
      f match {
        case PROLEPTIC_MONTH =>
          getChronology.range(f).checkValidValue(newValue, f)
          return plusMonths(newValue - getProlepticMonth)
        case YEAR_OF_ERA =>
        case YEAR =>
        case ERA => {
          val nvalue: Int = getChronology.range(f).checkValidIntValue(newValue, f)
          f match {
            case YEAR_OF_ERA =>
              return `with`(isoDate.withYear(if (getProlepticYear >= 1) nvalue + YEARS_DIFFERENCE else (1 - nvalue) + YEARS_DIFFERENCE))
            case YEAR =>
              return `with`(isoDate.withYear(nvalue + YEARS_DIFFERENCE))
            case ERA =>
              return `with`(isoDate.withYear((1 - getProlepticYear) + YEARS_DIFFERENCE))
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
  override def `with`(adjuster: TemporalAdjuster): MinguoDate = {
    return super.`with`(adjuster)
  }

  /**
   * {@inheritDoc}
   * @throws DateTimeException { @inheritDoc}
   * @throws ArithmeticException { @inheritDoc}
   */
  override def plus(amount: TemporalAmount): MinguoDate = {
    return super.plus(amount)
  }

  /**
   * {@inheritDoc}
   * @throws DateTimeException { @inheritDoc}
   * @throws ArithmeticException { @inheritDoc}
   */
  override def minus(amount: TemporalAmount): MinguoDate = {
    return super.minus(amount)
  }

  private[chrono] def plusYears(years: Long): MinguoDate = {
    return `with`(isoDate.plusYears(years))
  }

  private[chrono] def plusMonths(months: Long): MinguoDate = {
    return `with`(isoDate.plusMonths(months))
  }

  private[chrono] override def plusWeeks(weeksToAdd: Long): MinguoDate = {
    return super.plusWeeks(weeksToAdd)
  }

  private[chrono] def plusDays(days: Long): MinguoDate = {
    return `with`(isoDate.plusDays(days))
  }

  override def plus(amountToAdd: Long, unit: TemporalUnit): MinguoDate = {
    return super.plus(amountToAdd, unit)
  }

  override def minus(amountToAdd: Long, unit: TemporalUnit): MinguoDate = {
    return super.minus(amountToAdd, unit)
  }

  private[chrono] override def minusYears(yearsToSubtract: Long): MinguoDate = {
    return super.minusYears(yearsToSubtract)
  }

  private[chrono] override def minusMonths(monthsToSubtract: Long): MinguoDate = {
    return super.minusMonths(monthsToSubtract)
  }

  private[chrono] override def minusWeeks(weeksToSubtract: Long): MinguoDate = {
    return super.minusWeeks(weeksToSubtract)
  }

  private[chrono] override def minusDays(daysToSubtract: Long): MinguoDate = {
    return super.minusDays(daysToSubtract)
  }

  private def `with`(newDate: LocalDate): MinguoDate = {
    return (if ((newDate == isoDate)) this else new MinguoDate(newDate))
  }

  @SuppressWarnings(Array("unchecked")) final override def atTime(localTime: LocalTime): ChronoLocalDateTime[MinguoDate] = {
    return super.atTime(localTime).asInstanceOf[ChronoLocalDateTime[MinguoDate]]
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
    if (obj.isInstanceOf[MinguoDate]) {
      val otherDate: MinguoDate = obj.asInstanceOf[MinguoDate]
      return this.isoDate == otherDate.isoDate
    }
    return false
  }

  override def hashCode: Int = {
    return getChronology.getId.hashCode ^ isoDate.hashCode
  }

  private def writeReplace: AnyRef = {
    return new Ser(Ser.MINGUO_DATE_TYPE, this)
  }

  private[chrono] def writeExternal(out: DataOutput) {
    out.writeInt(get(YEAR))
    out.writeByte(get(MONTH_OF_YEAR))
    out.writeByte(get(DAY_OF_MONTH))
  }

  /**
   * The underlying date.
   */
  private final val isoDate: LocalDate = null
}

/**
 * An era in the Minguo calendar system.
 * <p>
 * The Minguo calendar system has two eras.
 * The current era, for years from 1 onwards, is known as the 'Republic of China' era.
 * All previous years, zero or earlier in the proleptic count or one and greater
 * in the year-of-era count, are part of the 'Before Republic of China' era.
 * <p>
 * <table summary="Minguo years and eras" cellpadding="2" cellspacing="3" border="0" >
 * <thead>
 * <tr class="tableSubHeadingColor">
 * <th class="colFirst" align="left">year-of-era</th>
 * <th class="colFirst" align="left">era</th>
 * <th class="colFirst" align="left">proleptic-year</th>
 * <th class="colLast" align="left">ISO proleptic-year</th>
 * </tr>
 * </thead>
 * <tbody>
 * <tr class="rowColor">
 * <td>2</td><td>ROC</td><td>2</td><td>1913</td>
 * </tr>
 * <tr class="altColor">
 * <td>1</td><td>ROC</td><td>1</td><td>1912</td>
 * </tr>
 * <tr class="rowColor">
 * <td>1</td><td>BEFORE_ROC</td><td>0</td><td>1911</td>
 * </tr>
 * <tr class="altColor">
 * <td>2</td><td>BEFORE_ROC</td><td>-1</td><td>1910</td>
 * </tr>
 * </tbody>
 * </table>
 * <p>
 * <b>Do not use {@code ordinal()} to obtain the numeric representation of {@code MinguoEra}.
 * Use {@code getValue()} instead.</b>
 *
 * @implSpec
 * This is an immutable and thread-safe enum.
 *
 * @since 1.8
 */
final object MinguoEra {
  /**
   * Obtains an instance of {@code MinguoEra} from an {@code int} value.
   * <p>
   * {@code MinguoEra} is an enum representing the Minguo eras of BEFORE_ROC/ROC.
   * This factory allows the enum to be obtained from the {@code int} value.
   *
   * @param minguoEra  the BEFORE_ROC/ROC value to represent, from 0 (BEFORE_ROC) to 1 (ROC)
   * @return the era singleton, not null
   * @throws DateTimeException if the value is invalid
   */
  def of(minguoEra: Int): MinguoEra = {
    minguoEra match {
      case 0 =>
        return BEFORE_ROC
      case 1 =>
        return ROC
      case _ =>
        throw new DateTimeException("Invalid era: " + minguoEra)
    }
  }

  private[chrono] def readExternal(in: DataInput): MinguoEra = {
    val eraValue: Byte = in.readByte
    return MinguoEra.of(eraValue)
  }

  /**
   * The singleton instance for the era before the current one, 'Before Republic of China Era',
   * which has the numeric value 0.
   */
  final val BEFORE_ROC: = null
  /**
   * The singleton instance for the current era, 'Republic of China Era',
   * which has the numeric value 1.
   */
  final val ROC: = null
}

final class MinguoEra extends Era {
  /**
   * Gets the numeric era {@code int} value.
   * <p>
   * The era BEFORE_ROC has the value 0, while the era ROC has the value 1.
   *
   * @return the era value, from 0 (BEFORE_ROC) to 1 (ROC)
   */
  def getValue: Int = {
    return ordinal
  }

  private def writeReplace: AnyRef = {
    return new Ser(Ser.MINGUO_ERA_TYPE, this)
  }

  private[chrono] def writeExternal(out: DataOutput) {
    out.writeByte(this.getValue)
  }
}

