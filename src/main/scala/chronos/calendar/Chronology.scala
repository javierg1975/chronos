package chronos.calendar

/**
 * A calendar system, used to organize and identify dates.
 * <p>
 * The main date and time API is built on the ISO calendar system.
 * This class operates behind the scenes to represent the general concept of a calendar system.
 * For example, the Japanese, Minguo, Thai Buddhist and others.
 * <p>
 * Most other calendar systems also operate on the shared concepts of year, month and day,
 * linked to the cycles of the Earth around the Sun, and the Moon around the Earth.
 * These shared concepts are defined by {@link ChronoField} and are available
 * for use by any {@code Chronology} implementation:
 * {{{
 * Date isoDate = ...
 * ThaiBuddhistDate thaiDate = ...
 * int isoYear = isoDate.get(ChronoField.YEAR);
 * int thaiYear = thaiDate.get(ChronoField.YEAR);
 * }}}
 * As shown, although the date objects are in different calendar systems, represented by different
 * {@code Chronology} instances, both can be queried using the same constant on {@code ChronoField}.
 * For a full discussion of the implications of this, see {@link ChronoLocalDate}.
 * In general, the advice is to use the known ISO-based {@code Date}, rather than
 * {@code ChronoLocalDate}.
 * <p>
 * While a {@code Chronology} object typically uses {@code ChronoField} and is based on
 * an era, year-of-era, month-of-year, day-of-month model of a date, this is not required.
 * A {@code Chronology} instance may represent a totally different kind of calendar system,
 * such as the Mayan.
 * <p>
 * In practical terms, the {@code Chronology} instance also acts as a factory.
 * The {@link #of(String)} method allows an instance to be looked up by identifier,
 * while the {@link #ofLocale(Locale)} method allows lookup by locale.
 * <p>
 * The {@code Chronology} instance provides a set of methods to create {@code ChronoLocalDate} instances.
 * The date classes are used to manipulate specific dates.
 * <p><ul>
 * <li> {@link #dateNow() dateNow()}
 * <li> {@link #dateNow(Clock) dateNow(clock)}
 * <li> {@link #dateNow(ZoneId) dateNow(zone)}
 * <li> {@link #date(int, int, int) date(yearProleptic, month, day)}
 * <li> {@link #date(Era, int, int, int) date(era, yearOfEra, month, day)}
 * <li> {@link #dateYearDay(int, int) dateYearDay(yearProleptic, dayOfYear)}
 * <li> {@link #dateYearDay(Era, int, int) dateYearDay(era, yearOfEra, dayOfYear)}
 * <li> {@link #date(TemporalAccessor) date(TemporalAccessor)}
 * </ul><p>
 *
 * <h3 id="addcalendars">Adding New Calendars</h3>
 * The set of available chronologies can be extended by applications.
 * Adding a new calendar system requires the writing of an implementation of
 * {@code Chronology}, {@code ChronoLocalDate} and {@code Era}.
 * The majority of the logic specific to the calendar system will be in
 * {@code ChronoLocalDate}. The {@code Chronology} subclass acts as a factory.
 * <p>
 * To permit the discovery of additional chronologies, the {@link java.util.ServiceLoader ServiceLoader}
 * is used. A file must be added to the {@code META-INF/services} directory with the
 * name 'java.time.chrono.Chronology' listing the implementation classes.
 * See the ServiceLoader for more details on service loading.
 * For lookup by id or calendarType, the system provided calendars are found
 * first followed by application provided calendars.
 * <p>
 * Each chronology must define a chronology ID that is unique within the system.
 * If the chronology represents a calendar system defined by the
 * CLDR specification then the calendar type is the concatenation of the
 * CLDR type and, if applicable, the CLDR variant,
 *
 * @implSpec
 * This class must be implemented with care to ensure other classes operate correctly.
 * All implementations that can be instantiated must be final, immutable and thread-safe.
 * Subclasses should be Serializable wherever possible.
 *
 * @since 1.8
 */
object Chronology {
  /**
   * Register a Chronology by its ID and type for lookup by {@link #of(java.lang.String)}.
   * Chronologies must not be registered until they are completely constructed.
   * Specifically, not in the constructor of Chronology.
   *
   * @param chrono the chronology to register; not null
   * @return the already registered Chronology if any, may be null
   */
  private[calendar] def registerChrono(chrono: Chronology): Chronology = {
     registerChrono(chrono, chrono.getId)
  }

  /**
   * Register a Chronology by ID and type for lookup by {@link #of(java.lang.String)}.
   * Chronos must not be registered until they are completely constructed.
   * Specifically, not in the constructor of Chronology.
   *
   * @param chrono the chronology to register; not null
   * @param id the ID to register the chronology; not null
   * @return the already registered Chronology if any, may be null
   */
  private[calendar] def registerChrono(chrono: Chronology, id: String): Chronology = {
    val prev: Chronology = CHRONOS_BY_ID.putIfAbsent(id, chrono)
    if (prev == null) {
      val `type`: String = chrono.getCalendarType
      if (`type` != null) {
        CHRONOS_BY_TYPE.putIfAbsent(`type`, chrono)
      }
    }
     prev
  }

  /**
   * Initialization of the maps from id and type to Chronology.
   * The ServiceLoader is used to find and register any implementations
   * of {@link java.time.chrono.Chronology} found in the bootclass loader.
   * The built-in chronologies are registered explicitly.
   * Calendars configured via the Thread's context classloader are local
   * to that thread and are ignored.
   * <p>
   * The initialization is done only once using the registration
   * of the IsoChronology as the test and the final step.
   * Multiple threads may perform the initialization concurrently.
   * Only the first registration of each Chronology is retained by the
   * ConcurrentHashMap.
   * @return true if the cache was initialized
   */
  private def initCache: Boolean = {
    if (CHRONOS_BY_ID.get("ISO") == null) {
      registerChrono(HijrahChronology.INSTANCE)
      registerChrono(JapaneseChronology.INSTANCE)
      registerChrono(MinguoChronology.INSTANCE)
      registerChrono(ThaiBuddhistChronology.INSTANCE)
      @SuppressWarnings(Array("rawtypes")) val loader: ServiceLoader[Chronology] = ServiceLoader.load(classOf[Chronology], null)
      import scala.collection.JavaConversions._
      for (chrono <- loader) {
        val id: String = chrono.getId
        if ((id == "ISO") || registerChrono(chrono) != null) {
          val logger: PlatformLogger = PlatformLogger.getLogger("java.time.chrono")
          logger.warning("Ignoring duplicate Chronology, from ServiceLoader configuration " + id)
        }
      }
      registerChrono(IsoChronology.INSTANCE)
       true
    }
     false
  }

  /**
   * Obtains an instance of {@code Chronology} from a temporal object.
   * <p>
   * This obtains a chronology based on the specified temporal.
   * A {@code TemporalAccessor} represents an arbitrary set of date and time information,
   * which this factory converts to an instance of {@code Chronology}.
   * <p>
   * The conversion will obtain the chronology using {@link TemporalQuery#chronology()}.
   * If the specified temporal object does not have a chronology, {@link IsoChronology} is returned.
   * <p>
   * This method matches the signature of the functional interface {@link TemporalQuery}
   * allowing it to be used in queries via method reference, {@code Chronology::from}.
   *
   * @param temporal  the temporal to convert, not null
   * @return the chronology, not null
   * @throws DateTimeException if unable to convert to an { @code Chronology}
   */
  def from(temporal: TemporalAccessor): Chronology = {
    object
    val obj: Chronology = temporal.query(TemporalQuery.chronology)
     (if (obj != null) obj else IsoChronology.INSTANCE)
  }

  /**
   * Obtains an instance of {@code Chronology} from a locale.
   * <p>
   * This returns a {@code Chronology} based on the specified locale,
   * typically returning {@code IsoChronology}. Other calendar systems
   * are only returned if they are explicitly selected within the locale.
   * <p>
   * The {@link Locale} class provide access to a range of information useful
   * for localizing an application. This includes the language and region,
   * such as "en-GB" for English as used in Great Britain.
   * <p>
   * The {@code Locale} class also supports an extension mechanism that
   * can be used to identify a calendar system. The mechanism is a form
   * of key-value pairs, where the calendar system has the key "ca".
   * For example, the locale "en-JP-u-ca-japanese" represents the English
   * language as used in Japan with the Japanese calendar system.
   * <p>
   * This method finds the desired calendar system by in a manner equivalent
   * to passing "ca" to {@link Locale#getUnicodeLocaleType(String)}.
   * If the "ca" key is not present, then {@code IsoChronology} is returned.
   * <p>
   * Note that the behavior of this method differs from the older
   * {@link java.util.Calendar#getInstance(Locale)} method.
   * If that method receives a locale of "th_TH" it will return {@code BuddhistCalendar}.
   * By contrast, this method will return {@code IsoChronology}.
   * Passing the locale "th-TH-u-ca-buddhist" into either method will
   * result in the Thai Buddhist calendar system and is therefore the
   * recommended approach going forward for Thai calendar system localization.
   * <p>
   * A similar, but simpler, situation occurs for the Japanese calendar system.
   * The locale "jp_JP_JP" has previously been used to access the calendar.
   * However, unlike the Thai locale, "ja_JP_JP" is automatically converted by
   * {@code Locale} to the modern and recommended form of "ja-JP-u-ca-japanese".
   * Thus, there is no difference in behavior between this method and
   * {@code Calendar#getInstance(Locale)}.
   *
   * @param locale  the locale to use to obtain the calendar system, not null
   * @return the calendar system associated with the locale, not null
   * @throws DateTimeException if the locale-specified calendar cannot be found
   */
  def ofLocale(locale: Locale): Chronology = {
    object
    val `type`: String = locale.getUnicodeLocaleType("ca")
    if (`type` == null || ("iso" == `type`) || ("iso8601" == `type`)) {
       IsoChronology.INSTANCE
    }
    do {
      val chrono: Chronology = CHRONOS_BY_TYPE.get(`type`)
      if (chrono != null) {
         chrono
      }
    } while (initCache)
    @SuppressWarnings(Array("rawtypes")) val loader: ServiceLoader[Chronology] = ServiceLoader.load(classOf[Chronology])
    import scala.collection.JavaConversions._
    for (chrono <- loader) {
      if (`type` == chrono.getCalendarType) {
         chrono
      }
    }
    throw new DateTimeException("Unknown calendar system: " + `type`)
  }

  /**
   * Obtains an instance of {@code Chronology} from a chronology ID or
   * calendar system type.
   * <p>
   * This returns a chronology based on either the ID or the type.
   * The {@link #getId() chronology ID} uniquely identifies the chronology.
   * The {@link #getCalendarType() calendar system type} is defined by the
   * CLDR specification.
   * <p>
   * The chronology may be a system chronology or a chronology
   * provided by the application via ServiceLoader configuration.
   * <p>
   * Since some calendars can be customized, the ID or type typically refers
   * to the default customization. For example, the Gregorian calendar can have multiple
   * cutover dates from the Julian, but the lookup only provides the default cutover date.
   *
   * @param id  the chronology ID or calendar system type, not null
   * @return the chronology with the identifier requested, not null
   * @throws DateTimeException if the chronology cannot be found
   */
  def of(id: String): Chronology = {
    object
    do {
      val chrono: Chronology = of0(id)
      if (chrono != null) {
         chrono
      }
    } while (initCache)
    @SuppressWarnings(Array("rawtypes")) val loader: ServiceLoader[Chronology] = ServiceLoader.load(classOf[Chronology])
    import scala.collection.JavaConversions._
    for (chrono <- loader) {
      if ((id == chrono.getId) || (id == chrono.getCalendarType)) {
         chrono
      }
    }
    throw new DateTimeException("Unknown chronology: " + id)
  }

  /**
   * Obtains an instance of {@code Chronology} from a chronology ID or
   * calendar system type.
   *
   * @param id  the chronology ID or calendar system type, not null
   * @return the chronology with the identifier requested, or { @code null} if not found
   */
  private def of0(id: String): Chronology = {
    var chrono: Chronology = CHRONOS_BY_ID.get(id)
    if (chrono == null) {
      chrono = CHRONOS_BY_TYPE.get(id)
    }
     chrono
  }

  /**
   * Returns the available chronologies.
   * <p>
   * Each returned {@code Chronology} is available for use in the system.
   * The set of chronologies includes the system chronologies and
   * any chronologies provided by the application via ServiceLoader
   * configuration.
   *
   * @return the independent, modifiable set of the available chronology IDs, not null
   */
  def getAvailableChronologies: Set[Chronology] = {
    initCache
    val chronos: HashSet[Chronology] = new HashSet[Chronology](CHRONOS_BY_ID.values)
    @SuppressWarnings(Array("rawtypes")) val loader: ServiceLoader[Chronology] = ServiceLoader.load(classOf[Chronology])
    import scala.collection.JavaConversions._
    for (chrono <- loader) {
      chronos.add(chrono)
    }
     chronos
  }

  private[calendar] def readExternal(in: DataInput): Chronology = {
    val id: String = in.readUTF
     Chronology.of(id)
  }

  /**
   * ChronoLocalDate order constant.
   */
  private[calendar] final val DATE_ORDER: Comparator[ChronoLocalDate] = (date1, date2) -> {
     Long.compare(date1.toEpochDay(), date2.toEpochDay());
  }.asInstanceOf[Comparator[ChronoLocalDate]]
  /**
   * ChronoLocalDateTime order constant.
   */
  private[calendar] final val DATE_TIME_ORDER: Comparator[ChronoLocalDateTime[_]] = (dateTime1, dateTime2) -> {
    int cmp = Long.compare(dateTime1.toLocalDate().toEpochDay(), dateTime2.toLocalDate().toEpochDay());
    if (cmp == 0) {
      cmp = Long.compare(dateTime1.toLocalTime().toNanoOfDay(), dateTime2.toLocalTime().toNanoOfDay());
    }
     cmp;
  }.asInstanceOf[Comparator[ChronoLocalDateTime[_]]]
  /**
   * ChronoZonedDateTime order constant.
   */
  private[calendar] final val INSTANT_ORDER: Comparator[ChronoZonedDateTime[_]] = (dateTime1, dateTime2) -> {
    int cmp = Long.compare(dateTime1.toEpochSecond(), dateTime2.toEpochSecond());
    if (cmp == 0) {
      cmp = Long.compare(dateTime1.toLocalTime().getNano(), dateTime2.toLocalTime().getNano());
    }
     cmp;
  }.asInstanceOf[Comparator[ChronoZonedDateTime[_]]]
  /**
   * Map of available calendars by ID.
   */
  private final val CHRONOS_BY_ID: ConcurrentHashMap[String, Chronology] = new ConcurrentHashMap[String, Chronology]
  /**
   * Map of available calendars by calendar type.
   */
  private final val CHRONOS_BY_TYPE: ConcurrentHashMap[String, Chronology] = new ConcurrentHashMap[String, Chronology]
}

abstract class Chronology extends Comparable[Chronology] {
  /**
   * Creates an instance.
   */
  protected def this() {

  }

  /**
   * Gets the ID of the chronology.
   * <p>
   * The ID uniquely identifies the {@code Chronology}.
   * It can be used to lookup the {@code Chronology} using {@link #of(String)}.
   *
   * @return the chronology ID, not null
   * @see #getCalendarType()
   */
  def getId: String

  /**
   * Gets the calendar type of the calendar system.
   * <p>
   * The calendar type is an identifier defined by the CLDR and
   * <em>Unicode Locale Data Markup Language (LDML)</em> specifications
   * to uniquely identification a calendar.
   * The {@code getCalendarType} is the concatenation of the CLDR calendar type
   * and the variant, if applicable, is appended separated by "-".
   * The calendar type is used to lookup the {@code Chronology} using {@link #of(String)}.
   *
   * @return the calendar system type, null if the calendar is not defined by CLDR/LDML
   * @see #getId()
   */
  def getCalendarType: String

  /**
   * Obtains a local date in this chronology from the era, year-of-era,
   * month-of-year and day-of-month fields.
   *
   * @param era  the era of the correct type for the chronology, not null
   * @param yearOfEra  the chronology year-of-era
   * @param month  the chronology month-of-year
   * @param dayOfMonth  the chronology day-of-month
   * @return the local date in this chronology, not null
   * @throws DateTimeException if unable to create the date
   * @throws ClassCastException if the { @code era} is not of the correct type for the chronology
   */
  def date(era: Era, yearOfEra: Int, month: Int, dayOfMonth: Int): ChronoLocalDate = {
     date(prolepticYear(era, yearOfEra), month, dayOfMonth)
  }

  /**
   * Obtains a local date in this chronology from the proleptic-year,
   * month-of-year and day-of-month fields.
   *
   * @param prolepticYear  the chronology proleptic-year
   * @param month  the chronology month-of-year
   * @param dayOfMonth  the chronology day-of-month
   * @return the local date in this chronology, not null
   * @throws DateTimeException if unable to create the date
   */
  def date(prolepticYear: Int, month: Int, dayOfMonth: Int): ChronoLocalDate

  /**
   * Obtains a local date in this chronology from the era, year-of-era and
   * day-of-year fields.
   *
   * @param era  the era of the correct type for the chronology, not null
   * @param yearOfEra  the chronology year-of-era
   * @param dayOfYear  the chronology day-of-year
   * @return the local date in this chronology, not null
   * @throws DateTimeException if unable to create the date
   * @throws ClassCastException if the { @code era} is not of the correct type for the chronology
   */
  def dateYearDay(era: Era, yearOfEra: Int, dayOfYear: Int): ChronoLocalDate = {
     dateYearDay(prolepticYear(era, yearOfEra), dayOfYear)
  }

  /**
   * Obtains a local date in this chronology from the proleptic-year and
   * day-of-year fields.
   *
   * @param prolepticYear  the chronology proleptic-year
   * @param dayOfYear  the chronology day-of-year
   * @return the local date in this chronology, not null
   * @throws DateTimeException if unable to create the date
   */
  def dateYearDay(prolepticYear: Int, dayOfYear: Int): ChronoLocalDate

  /**
   * Obtains a local date in this chronology from the epoch-day.
   * <p>
   * The definition of {@link ChronoField#EPOCH_DAY EPOCH_DAY} is the same
   * for all calendar systems, thus it can be used for conversion.
   *
   * @param epochDay  the epoch day
   * @return the local date in this chronology, not null
   * @throws DateTimeException if unable to create the date
   */
  def dateEpochDay(epochDay: Long): ChronoLocalDate

  /**
   * Obtains the current local date in this chronology from the system clock in the default time-zone.
   * <p>
   * This will query the {@link Clock#systemDefaultZone() system clock} in the default
   * time-zone to obtain the current date.
   * <p>
   * Using this method will prevent the ability to use an alternate clock for testing
   * because the clock is hard-coded.
   * <p>
   * This implementation uses {@link #dateNow(Clock)}.
   *
   * @return the current local date using the system clock and default time-zone, not null
   * @throws DateTimeException if unable to create the date
   */
  def dateNow: ChronoLocalDate = {
     dateNow(Clock.systemDefaultZone)
  }

  /**
   * Obtains the current local date in this chronology from the system clock in the specified time-zone.
   * <p>
   * This will query the {@link Clock#system(ZoneId) system clock} to obtain the current date.
   * Specifying the time-zone avoids dependence on the default time-zone.
   * <p>
   * Using this method will prevent the ability to use an alternate clock for testing
   * because the clock is hard-coded.
   *
   * @param zone  the zone ID to use, not null
   * @return the current local date using the system clock, not null
   * @throws DateTimeException if unable to create the date
   */
  def dateNow(zone: ZoneId): ChronoLocalDate = {
     dateNow(Clock.system(zone))
  }

  /**
   * Obtains the current local date in this chronology from the specified clock.
   * <p>
   * This will query the specified clock to obtain the current date - today.
   * Using this method allows the use of an alternate clock for testing.
   * The alternate clock may be introduced using {@link Clock dependency injection}.
   *
   * @param clock  the clock to use, not null
   * @return the current local date, not null
   * @throws DateTimeException if unable to create the date
   */
  def dateNow(clock: Clock): ChronoLocalDate = {
    object
     date(LocalDate.now(clock))
  }

  /**
   * Obtains a local date in this chronology from another temporal object.
   * <p>
   * This obtains a date in this chronology based on the specified temporal.
   * A {@code TemporalAccessor} represents an arbitrary set of date and time information,
   * which this factory converts to an instance of {@code ChronoLocalDate}.
   * <p>
   * The conversion typically uses the {@link ChronoField#EPOCH_DAY EPOCH_DAY}
   * field, which is standardized across calendar systems.
   * <p>
   * This method matches the signature of the functional interface {@link TemporalQuery}
   * allowing it to be used as a query via method reference, {@code aChronology::date}.
   *
   * @param temporal  the temporal object to convert, not null
   * @return the local date in this chronology, not null
   * @throws DateTimeException if unable to create the date
   * @see ChronoLocalDate#from(TemporalAccessor)
   */
  def date(temporal: TemporalAccessor): ChronoLocalDate

  /**
   * Obtains a local date-time in this chronology from another temporal object.
   * <p>
   * This obtains a date-time in this chronology based on the specified temporal.
   * A {@code TemporalAccessor} represents an arbitrary set of date and time information,
   * which this factory converts to an instance of {@code ChronoLocalDateTime}.
   * <p>
   * The conversion extracts and combines the {@code ChronoLocalDate} and the
   * {@code Time} from the temporal object.
   * Implementations are permitted to perform optimizations such as accessing
   * those fields that are equivalent to the relevant objects.
   * The result uses this chronology.
   * <p>
   * This method matches the signature of the functional interface {@link TemporalQuery}
   * allowing it to be used as a query via method reference, {@code aChronology::localDateTime}.
   *
   * @param temporal  the temporal object to convert, not null
   * @return the local date-time in this chronology, not null
   * @throws DateTimeException if unable to create the date-time
   * @see ChronoLocalDateTime#from(TemporalAccessor)
   */
  def localDateTime(temporal: TemporalAccessor): ChronoLocalDateTime[_ <: ChronoLocalDate] = {
    try {
       date(temporal).atTime(LocalTime.from(temporal))
    }
    catch {
      case ex: DateTimeException => {
        throw new DateTimeException("Unable to obtain ChronoLocalDateTime from TemporalAccessor: " + temporal.getClass, ex)
      }
    }
  }

  /**
   * Obtains a {@code ChronoZonedDateTime} in this chronology from another temporal object.
   * <p>
   * This obtains a zoned date-time in this chronology based on the specified temporal.
   * A {@code TemporalAccessor} represents an arbitrary set of date and time information,
   * which this factory converts to an instance of {@code ChronoZonedDateTime}.
   * <p>
   * The conversion will first obtain a {@code ZoneId} from the temporal object,
   * falling back to a {@code ZoneOffset} if necessary. It will then try to obtain
   * an {@code Instant}, falling back to a {@code ChronoLocalDateTime} if necessary.
   * The result will be either the combination of {@code ZoneId} or {@code ZoneOffset}
   * with {@code Instant} or {@code ChronoLocalDateTime}.
   * Implementations are permitted to perform optimizations such as accessing
   * those fields that are equivalent to the relevant objects.
   * The result uses this chronology.
   * <p>
   * This method matches the signature of the functional interface {@link TemporalQuery}
   * allowing it to be used as a query via method reference, {@code aChronology::zonedDateTime}.
   *
   * @param temporal  the temporal object to convert, not null
   * @return the zoned date-time in this chronology, not null
   * @throws DateTimeException if unable to create the date-time
   * @see ChronoZonedDateTime#from(TemporalAccessor)
   */
  def zonedDateTime(temporal: TemporalAccessor): ChronoZonedDateTime[_ <: ChronoLocalDate] = {
    try {
      val zone: ZoneId = ZoneId.from(temporal)
      try {
        val instant: Instant = Instant.from(temporal)
         zonedDateTime(instant, zone)
      }
      catch {
        case ex1: DateTimeException => {
          val cldt: ChronoLocalDateTimeImpl[_] = ChronoLocalDateTimeImpl.ensureValid(this, localDateTime(temporal))
           ChronoZonedDateTimeImpl.ofBest(cldt, zone, null)
        }
      }
    }
    catch {
      case ex: DateTimeException => {
        throw new DateTimeException("Unable to obtain ChronoZonedDateTime from TemporalAccessor: " + temporal.getClass, ex)
      }
    }
  }

  /**
   * Obtains a {@code ChronoZonedDateTime} in this chronology from an {@code Instant}.
   * <p>
   * This obtains a zoned date-time with the same instant as that specified.
   *
   * @param instant  the instant to create the date-time from, not null
   * @param zone  the time-zone, not null
   * @return the zoned date-time, not null
   * @throws DateTimeException if the result exceeds the supported range
   */
  def zonedDateTime(instant: Instant, zone: ZoneId): ChronoZonedDateTime[_ <: ChronoLocalDate] = {
     ChronoZonedDateTimeImpl.ofInstant(this, instant, zone)
  }

  /**
   * Checks if the specified year is a leap year.
   * <p>
   * A leap-year is a year of a longer length than normal.
   * The exact meaning is determined by the chronology according to the following constraints.
   * <p><ul>
   * <li>a leap-year must imply a year-length longer than a non leap-year.
   * <li>a chronology that does not support the concept of a year must return false.
   * </ul><p>
   *
   * @param prolepticYear  the proleptic-year to check, not validated for range
   * @return true if the year is a leap year
   */
  def isLeapYear(prolepticYear: Long): Boolean

  /**
   * Calculates the proleptic-year given the era and year-of-era.
   * <p>
   * This combines the era and year-of-era into the single proleptic-year field.
   * <p>
   * If the chronology makes active use of eras, such as {@code JapaneseChronology}
   * then the year-of-era will be validated against the era.
   * For other chronologies, validation is optional.
   *
   * @param era  the era of the correct type for the chronology, not null
   * @param yearOfEra  the chronology year-of-era
   * @return the proleptic-year
   * @throws DateTimeException if unable to convert to a proleptic-year,
   *                           such as if the year is invalid for the era
   * @throws ClassCastException if the { @code era} is not of the correct type for the chronology
   */
  def prolepticYear(era: Era, yearOfEra: Int): Int

  /**
   * Creates the chronology era object from the numeric value.
   * <p>
   * The era is, conceptually, the largest division of the time-line.
   * Most calendar systems have a single epoch dividing the time-line into two eras.
   * However, some have multiple eras, such as one for the reign of each leader.
   * The exact meaning is determined by the chronology according to the following constraints.
   * <p>
   * The era in use at 1970-01-01 must have the value 1.
   * Later eras must have sequentially higher values.
   * Earlier eras must have sequentially lower values.
   * Each chronology must refer to an enum or similar singleton to provide the era values.
   * <p>
   * This method returns the singleton era of the correct type for the specified era value.
   *
   * @param eraValue  the era value
   * @return the calendar system era, not null
   * @throws DateTimeException if unable to create the era
   */
  def eraOf(eraValue: Int): Era

  /**
   * Gets the list of eras for the chronology.
   * <p>
   * Most calendar systems have an era, within which the year has meaning.
   * If the calendar system does not support the concept of eras, an empty
   * list must be returned.
   *
   * @return the list of eras for the chronology, may be immutable, not null
   */
  def eras: List[Era]

  /**
   * Gets the range of valid values for the specified field.
   * <p>
   * All fields can be expressed as a {@code long} integer.
   * This method returns an object that describes the valid range for that value.
   * <p>
   * Note that the result only describes the minimum and maximum valid values
   * and it is important not to read too much into them. For example, there
   * could be values within the range that are invalid for the field.
   * <p>
   * This method will return a result whether or not the chronology supports the field.
   *
   * @param field  the field to get the range for, not null
   * @return the range of valid values for the field, not null
   * @throws DateTimeException if the range for the field cannot be obtained
   */
  def range(field: ChronoField): ValueRange

  /**
   * Gets the textual representation of this chronology.
   * <p>
   * This returns the textual name used to identify the chronology,
   * suitable for presentation to the user.
   * The parameters control the style of the returned text and the locale.
   *
   * @param style  the style of the text required, not null
   * @param locale  the locale to use, not null
   * @return the text value of the chronology, not null
   */
  def getDisplayName(style: TextStyle, locale: Locale): String = {
     new DateTimeFormatterBuilder().appendChronologyText(style).toFormatter(locale).format(toTemporal)
  }

  /**
   * Converts this chronology to a {@code TemporalAccessor}.
   * <p>
   * A {@code Chronology} can be fully represented as a {@code TemporalAccessor}.
   * However, the interface is not implemented by this class as most of the
   * methods on the interface have no meaning to {@code Chronology}.
   * <p>
   * The returned temporal has no supported fields, with the query method
   * supporting the return of the chronology using {@link TemporalQuery#chronology()}.
   *
   * @return a temporal equivalent to this chronology, not null
   */
  private def toTemporal: TemporalAccessor = {
     new TemporalAccessor {
      def isSupported(field: TemporalField): Boolean = {
         false
      }

      def getLong(field: TemporalField): Long = {
        throw new UnsupportedTemporalTypeException("Unsupported field: " + field)
      }

      override def query(query: TemporalQuery[R]): R = {
        if (query eq TemporalQuery.chronology) {
           Chronology.this.asInstanceOf[R]
        }
         TemporalAccessor.super.query(query)
      }
    }
  }

  /**
   * Resolves parsed {@code ChronoField} values into a date during parsing.
   * <p>
   * Most {@code TemporalField} implementations are resolved using the
   * resolve method on the field. By contrast, the {@code ChronoField} class
   * defines fields that only have meaning relative to the chronology.
   * As such, {@code ChronoField} date fields are resolved here in the
   * context of a specific chronology.
   * <p>
   * {@code ChronoField} instances are resolved by this method, which may
   * be overridden in subclasses.
   * <ul>
   * <li>{@code EPOCH_DAY} - If present, this is converted to a date and
   * all other date fields are then cross-checked against the date.
   * <li>{@code PROLEPTIC_MONTH} - If present, then it is split into the
   * {@code YEAR} and {@code MONTH_OF_YEAR}. If the mode is strict or smart
   * then the field is validated.
   * <li>{@code YEAR_OF_ERA} and {@code ERA} - If both are present, then they
   * are combined to form a {@code YEAR}. In lenient mode, the {@code YEAR_OF_ERA}
   * range is not validated, in smart and strict mode it is. The {@code ERA} is
   * validated for range in all three modes. If only the {@code YEAR_OF_ERA} is
   * present, and the mode is smart or lenient, then the last available era
   * is assumed. In strict mode, no era is assumed and the {@code YEAR_OF_ERA} is
   * left untouched. If only the {@code ERA} is present, then it is left untouched.
   * <li>{@code YEAR}, {@code MONTH_OF_YEAR} and {@code DAY_OF_MONTH} -
   * If all three are present, then they are combined to form a date.
   * In all three modes, the {@code YEAR} is validated.
   * If the mode is smart or strict, then the month and day are validated.
   * If the mode is lenient, then the date is combined in a manner equivalent to
   * creating a date on the first day of the first month in the requested year,
   * then adding the difference in months, then the difference in days.
   * If the mode is smart, and the day-of-month is greater than the maximum for
   * the year-month, then the day-of-month is adjusted to the last day-of-month.
   * If the mode is strict, then the three fields must form a valid date.
   * <li>{@code YEAR} and {@code DAY_OF_YEAR} -
   * If both are present, then they are combined to form a date.
   * In all three modes, the {@code YEAR} is validated.
   * If the mode is lenient, then the date is combined in a manner equivalent to
   * creating a date on the first day of the requested year, then adding
   * the difference in days.
   * If the mode is smart or strict, then the two fields must form a valid date.
   * <li>{@code YEAR}, {@code MONTH_OF_YEAR}, {@code ALIGNED_WEEK_OF_MONTH} and
   * {@code ALIGNED_DAY_OF_WEEK_IN_MONTH} -
   * If all four are present, then they are combined to form a date.
   * In all three modes, the {@code YEAR} is validated.
   * If the mode is lenient, then the date is combined in a manner equivalent to
   * creating a date on the first day of the first month in the requested year, then adding
   * the difference in months, then the difference in weeks, then in days.
   * If the mode is smart or strict, then the all four fields are validated to
   * their outer ranges. The date is then combined in a manner equivalent to
   * creating a date on the first day of the requested year and month, then adding
   * the amount in weeks and days to reach their values. If the mode is strict,
   * the date is additionally validated to check that the day and week adjustment
   * did not change the month.
   * <li>{@code YEAR}, {@code MONTH_OF_YEAR}, {@code ALIGNED_WEEK_OF_MONTH} and
   * {@code DAY_OF_WEEK} - If all four are present, then they are combined to
   * form a date. The approach is the same as described above for
   * years, months and weeks in {@code ALIGNED_DAY_OF_WEEK_IN_MONTH}.
   * The day-of-week is adjusted as the next or same matching day-of-week once
   * the years, months and weeks have been handled.
   * <li>{@code YEAR}, {@code ALIGNED_WEEK_OF_YEAR} and {@code ALIGNED_DAY_OF_WEEK_IN_YEAR} -
   * If all three are present, then they are combined to form a date.
   * In all three modes, the {@code YEAR} is validated.
   * If the mode is lenient, then the date is combined in a manner equivalent to
   * creating a date on the first day of the requested year, then adding
   * the difference in weeks, then in days.
   * If the mode is smart or strict, then the all three fields are validated to
   * their outer ranges. The date is then combined in a manner equivalent to
   * creating a date on the first day of the requested year, then adding
   * the amount in weeks and days to reach their values. If the mode is strict,
   * the date is additionally validated to check that the day and week adjustment
   * did not change the year.
   * <li>{@code YEAR}, {@code ALIGNED_WEEK_OF_YEAR} and {@code DAY_OF_WEEK} -
   * If all three are present, then they are combined to form a date.
   * The approach is the same as described above for years and weeks in
   * {@code ALIGNED_DAY_OF_WEEK_IN_YEAR}. The day-of-week is adjusted as the
   * next or same matching day-of-week once the years and weeks have been handled.
   * </ul>
   * <p>
   * The default implementation is suitable for most calendar systems.
   * If {@link ChronoField#YEAR_OF_ERA} is found without an {@link ChronoField#ERA}
   * then the last era in {@link #eras()} is used.
   * The implementation assumes a 7 day week, that the first day-of-month
   * has the value 1, that first day-of-year has the value 1, and that the
   * first of the month and year always exists.
   *
   * @param fieldValues  the map of fields to values, which can be updated, not null
   * @param resolverStyle  the requested type of resolve, not null
   * @return the resolved date, null if insufficient information to create a date
   * @throws DateTimeException if the date cannot be resolved, typically
   *                           because of a conflict in the input data
   */
  def resolveDate(fieldValues: Map[TemporalField, Long], resolverStyle: ResolverStyle): ChronoLocalDate = {
    if (fieldValues.containsKey(EPOCH_DAY)) {
       dateEpochDay(fieldValues.remove(EPOCH_DAY))
    }
    resolveProlepticMonth(fieldValues, resolverStyle)
    val resolved: ChronoLocalDate = resolveYearOfEra(fieldValues, resolverStyle)
    if (resolved != null) {
       resolved
    }
    if (fieldValues.containsKey(YEAR)) {
      if (fieldValues.containsKey(MONTH_OF_YEAR)) {
        if (fieldValues.containsKey(DAY_OF_MONTH)) {
           resolveYMD(fieldValues, resolverStyle)
        }
        if (fieldValues.containsKey(ALIGNED_WEEK_OF_MONTH)) {
          if (fieldValues.containsKey(ALIGNED_DAY_OF_WEEK_IN_MONTH)) {
             resolveYMAA(fieldValues, resolverStyle)
          }
          if (fieldValues.containsKey(DAY_OF_WEEK)) {
             resolveYMAD(fieldValues, resolverStyle)
          }
        }
      }
      if (fieldValues.containsKey(DAY_OF_YEAR)) {
         resolveYD(fieldValues, resolverStyle)
      }
      if (fieldValues.containsKey(ALIGNED_WEEK_OF_YEAR)) {
        if (fieldValues.containsKey(ALIGNED_DAY_OF_WEEK_IN_YEAR)) {
           resolveYAA(fieldValues, resolverStyle)
        }
        if (fieldValues.containsKey(DAY_OF_WEEK)) {
           resolveYAD(fieldValues, resolverStyle)
        }
      }
    }
     null
  }

  private[calendar] def resolveProlepticMonth(fieldValues: Map[TemporalField, Long], resolverStyle: ResolverStyle) {
    val pMonth: Long = fieldValues.remove(PROLEPTIC_MONTH)
    if (pMonth != null) {
      if (resolverStyle ne ResolverStyle.LENIENT) {
        PROLEPTIC_MONTH.checkValidValue(pMonth)
      }
      val chronoDate: ChronoLocalDate = dateNow.`with`(DAY_OF_MONTH, 1).`with`(PROLEPTIC_MONTH, pMonth)
      addFieldValue(fieldValues, MONTH_OF_YEAR, chronoDate.get(MONTH_OF_YEAR))
      addFieldValue(fieldValues, YEAR, chronoDate.get(YEAR))
    }
  }

  private[calendar] def resolveYearOfEra(fieldValues: Map[TemporalField, Long], resolverStyle: ResolverStyle): ChronoLocalDate = {
    val yoeLong: Long = fieldValues.remove(YEAR_OF_ERA)
    if (yoeLong != null) {
      val eraLong: Long = fieldValues.remove(ERA)
      var yoe: Int = 0
      if (resolverStyle ne ResolverStyle.LENIENT) {
        yoe = range(YEAR_OF_ERA).checkValidIntValue(yoeLong, YEAR_OF_ERA)
      }
      else {
        yoe = Math.toIntExact(yoeLong)
      }
      if (eraLong != null) {
        val eraObj: Era = eraOf(range(ERA).checkValidIntValue(eraLong, ERA))
        addFieldValue(fieldValues, YEAR, prolepticYear(eraObj, yoe))
      }
      else {
        if (fieldValues.containsKey(YEAR)) {
          val year: Int = range(YEAR).checkValidIntValue(fieldValues.get(YEAR), YEAR)
          val chronoDate: ChronoLocalDate = dateYearDay(year, 1)
          addFieldValue(fieldValues, YEAR, prolepticYear(chronoDate.getEra, yoe))
        }
        else if (resolverStyle eq ResolverStyle.STRICT) {
          fieldValues.put(YEAR_OF_ERA, yoeLong)
        }
        else {
          val eras: List[Era] = eras
          if (eras.isEmpty) {
            addFieldValue(fieldValues, YEAR, yoe)
          }
          else {
            val eraObj: Era = eras.get(eras.size - 1)
            addFieldValue(fieldValues, YEAR, prolepticYear(eraObj, yoe))
          }
        }
      }
    }
    else if (fieldValues.containsKey(ERA)) {
      range(ERA).checkValidValue(fieldValues.get(ERA), ERA)
    }
     null
  }

  private[calendar] def resolveYMD(fieldValues: Map[TemporalField, Long], resolverStyle: ResolverStyle): ChronoLocalDate = {
    val y: Int = range(YEAR).checkValidIntValue(fieldValues.remove(YEAR), YEAR)
    if (resolverStyle eq ResolverStyle.LENIENT) {
      val months: Long = Math.subtractExact(fieldValues.remove(MONTH_OF_YEAR), 1)
      val days: Long = Math.subtractExact(fieldValues.remove(DAY_OF_MONTH), 1)
       date(y, 1, 1).plus(months, MONTHS).plus(days, DAYS)
    }
    val moy: Int = range(MONTH_OF_YEAR).checkValidIntValue(fieldValues.remove(MONTH_OF_YEAR), MONTH_OF_YEAR)
    val domRange: ValueRange = range(DAY_OF_MONTH)
    val dom: Int = domRange.checkValidIntValue(fieldValues.remove(DAY_OF_MONTH), DAY_OF_MONTH)
    if (resolverStyle eq ResolverStyle.SMART) {
      try {
         date(y, moy, dom)
      }
      catch {
        case ex: DateTimeException => {
           date(y, moy, 1).`with`(TemporalAdjuster.lastDayOfMonth)
        }
      }
    }
     date(y, moy, dom)
  }

  private[calendar] def resolveYD(fieldValues: Map[TemporalField, Long], resolverStyle: ResolverStyle): ChronoLocalDate = {
    val y: Int = range(YEAR).checkValidIntValue(fieldValues.remove(YEAR), YEAR)
    if (resolverStyle eq ResolverStyle.LENIENT) {
      val days: Long = Math.subtractExact(fieldValues.remove(DAY_OF_YEAR), 1)
       dateYearDay(y, 1).plus(days, DAYS)
    }
    val doy: Int = range(DAY_OF_YEAR).checkValidIntValue(fieldValues.remove(DAY_OF_YEAR), DAY_OF_YEAR)
     dateYearDay(y, doy)
  }

  private[calendar] def resolveYMAA(fieldValues: Map[TemporalField, Long], resolverStyle: ResolverStyle): ChronoLocalDate = {
    val y: Int = range(YEAR).checkValidIntValue(fieldValues.remove(YEAR), YEAR)
    if (resolverStyle eq ResolverStyle.LENIENT) {
      val months: Long = Math.subtractExact(fieldValues.remove(MONTH_OF_YEAR), 1)
      val weeks: Long = Math.subtractExact(fieldValues.remove(ALIGNED_WEEK_OF_MONTH), 1)
      val days: Long = Math.subtractExact(fieldValues.remove(ALIGNED_DAY_OF_WEEK_IN_MONTH), 1)
       date(y, 1, 1).plus(months, MONTHS).plus(weeks, WEEKS).plus(days, DAYS)
    }
    val moy: Int = range(MONTH_OF_YEAR).checkValidIntValue(fieldValues.remove(MONTH_OF_YEAR), MONTH_OF_YEAR)
    val aw: Int = range(ALIGNED_WEEK_OF_MONTH).checkValidIntValue(fieldValues.remove(ALIGNED_WEEK_OF_MONTH), ALIGNED_WEEK_OF_MONTH)
    val ad: Int = range(ALIGNED_DAY_OF_WEEK_IN_MONTH).checkValidIntValue(fieldValues.remove(ALIGNED_DAY_OF_WEEK_IN_MONTH), ALIGNED_DAY_OF_WEEK_IN_MONTH)
    val date: ChronoLocalDate = date(y, moy, 1).plus((aw - 1) * 7 + (ad - 1), DAYS)
    if (resolverStyle eq ResolverStyle.STRICT && date.get(MONTH_OF_YEAR) != moy) {
      throw new DateTimeException("Strict mode rejected resolved date as it is in a different month")
    }
     date
  }

  private[calendar] def resolveYMAD(fieldValues: Map[TemporalField, Long], resolverStyle: ResolverStyle): ChronoLocalDate = {
    val y: Int = range(YEAR).checkValidIntValue(fieldValues.remove(YEAR), YEAR)
    if (resolverStyle eq ResolverStyle.LENIENT) {
      val months: Long = Math.subtractExact(fieldValues.remove(MONTH_OF_YEAR), 1)
      val weeks: Long = Math.subtractExact(fieldValues.remove(ALIGNED_WEEK_OF_MONTH), 1)
      val dow: Long = Math.subtractExact(fieldValues.remove(DAY_OF_WEEK), 1)
       resolveAligned(date(y, 1, 1), months, weeks, dow)
    }
    val moy: Int = range(MONTH_OF_YEAR).checkValidIntValue(fieldValues.remove(MONTH_OF_YEAR), MONTH_OF_YEAR)
    val aw: Int = range(ALIGNED_WEEK_OF_MONTH).checkValidIntValue(fieldValues.remove(ALIGNED_WEEK_OF_MONTH), ALIGNED_WEEK_OF_MONTH)
    val dow: Int = range(DAY_OF_WEEK).checkValidIntValue(fieldValues.remove(DAY_OF_WEEK), DAY_OF_WEEK)
    val date: ChronoLocalDate = date(y, moy, 1).plus((aw - 1) * 7, DAYS).`with`(nextOrSame(DayOfWeek.of(dow)))
    if (resolverStyle eq ResolverStyle.STRICT && date.get(MONTH_OF_YEAR) != moy) {
      throw new DateTimeException("Strict mode rejected resolved date as it is in a different month")
    }
     date
  }

  private[calendar] def resolveYAA(fieldValues: Map[TemporalField, Long], resolverStyle: ResolverStyle): ChronoLocalDate = {
    val y: Int = range(YEAR).checkValidIntValue(fieldValues.remove(YEAR), YEAR)
    if (resolverStyle eq ResolverStyle.LENIENT) {
      val weeks: Long = Math.subtractExact(fieldValues.remove(ALIGNED_WEEK_OF_YEAR), 1)
      val days: Long = Math.subtractExact(fieldValues.remove(ALIGNED_DAY_OF_WEEK_IN_YEAR), 1)
       dateYearDay(y, 1).plus(weeks, WEEKS).plus(days, DAYS)
    }
    val aw: Int = range(ALIGNED_WEEK_OF_YEAR).checkValidIntValue(fieldValues.remove(ALIGNED_WEEK_OF_YEAR), ALIGNED_WEEK_OF_YEAR)
    val ad: Int = range(ALIGNED_DAY_OF_WEEK_IN_YEAR).checkValidIntValue(fieldValues.remove(ALIGNED_DAY_OF_WEEK_IN_YEAR), ALIGNED_DAY_OF_WEEK_IN_YEAR)
    val date: ChronoLocalDate = dateYearDay(y, 1).plus((aw - 1) * 7 + (ad - 1), DAYS)
    if (resolverStyle eq ResolverStyle.STRICT && date.get(YEAR) != y) {
      throw new DateTimeException("Strict mode rejected resolved date as it is in a different year")
    }
     date
  }

  private[calendar] def resolveYAD(fieldValues: Map[TemporalField, Long], resolverStyle: ResolverStyle): ChronoLocalDate = {
    val y: Int = range(YEAR).checkValidIntValue(fieldValues.remove(YEAR), YEAR)
    if (resolverStyle eq ResolverStyle.LENIENT) {
      val weeks: Long = Math.subtractExact(fieldValues.remove(ALIGNED_WEEK_OF_YEAR), 1)
      val dow: Long = Math.subtractExact(fieldValues.remove(DAY_OF_WEEK), 1)
       resolveAligned(dateYearDay(y, 1), 0, weeks, dow)
    }
    val aw: Int = range(ALIGNED_WEEK_OF_YEAR).checkValidIntValue(fieldValues.remove(ALIGNED_WEEK_OF_YEAR), ALIGNED_WEEK_OF_YEAR)
    val dow: Int = range(DAY_OF_WEEK).checkValidIntValue(fieldValues.remove(DAY_OF_WEEK), DAY_OF_WEEK)
    val date: ChronoLocalDate = dateYearDay(y, 1).plus((aw - 1) * 7, DAYS).`with`(nextOrSame(DayOfWeek.of(dow)))
    if (resolverStyle eq ResolverStyle.STRICT && date.get(YEAR) != y) {
      throw new DateTimeException("Strict mode rejected resolved date as it is in a different year")
    }
     date
  }

  private[calendar] def resolveAligned(base: ChronoLocalDate, months: Long, weeks: Long, dow: Long): ChronoLocalDate = {
    var date: ChronoLocalDate = base.plus(months, MONTHS).plus(weeks, WEEKS)
    if (dow > 7) {
      date = date.plus((dow - 1) / 7, WEEKS)
      dow = ((dow - 1) % 7) + 1
    }
    else if (dow < 1) {
      date = date.plus(Math.subtractExact(dow, 7) / 7, WEEKS)
      dow = ((dow + 6) % 7) + 1
    }
     date.`with`(nextOrSame(DayOfWeek.of(dow.asInstanceOf[Int])))
  }

  /**
   * Adds a field-value pair to the map, checking for conflicts.
   * <p>
   * If the field is not already present, then the field-value pair is added to the map.
   * If the field is already present and it has the same value as that specified, no action occurs.
   * If the field is already present and it has a different value to that specified, then
   * an exception is thrown.
   *
   * @param field  the field to add, not null
   * @param value  the value to add, not null
   * @throws DateTimeException if the field is already present with a different value
   */
  private[calendar] def addFieldValue(fieldValues: Map[TemporalField, Long], field: ChronoField, value: Long) {
    val old: Long = fieldValues.get(field)
    if (old != null && old.longValue != value) {
      throw new DateTimeException("Conflict found: " + field + " " + old + " differs from " + field + " " + value)
    }
    fieldValues.put(field, value)
  }

  /**
   * Obtains a period for this chronology based on years, months and days.
   * <p>
   * This returns a period tied to this chronology using the specified
   * years, months and days.  All supplied chronologies use periods
   * based on years, months and days, however the {@code ChronoPeriod} API
   * allows the period to be represented using other units.
   *
   * @implSpec
     * The default implementation returns an implementation class suitable
   *   for most calendar systems. It is based solely on the three units.
   *   Normalization, addition and subtraction derive the number of months
   *   in a year from the { @link #range(ChronoField)}. If the number of
   *                              months within a year is fixed, then the calculation approach for
   *                              addition, subtraction and normalization is slightly different.
   *                              <p>
   *                              If implementing an unusual calendar system that is not based on
   *                              years, months and days, or where you want direct control, then
   *                              the { @code ChronoPeriod} interface must be directly implemented.
   *                                          <p>
   *                                          The returned period is immutable and thread-safe.
   *
   * @param years  the number of years, may be negative
   * @param months  the number of years, may be negative
   * @param days  the number of years, may be negative
   * @return the period in terms of this chronology, not null
   */
  def period(years: Int, months: Int, days: Int): ChronoPeriod = {
     new ChronoPeriodImpl(this, years, months, days)
  }

  /**
   * Compares this chronology to another chronology.
   * <p>
   * The comparison order first by the chronology ID string, then by any
   * additional information specific to the subclass.
   * It is "consistent with equals", as defined by {@link Comparable}.
   * <p>
   * The default implementation compares the chronology ID.
   * Subclasses must compare any additional state that they store.
   *
   * @param other  the other chronology to compare to, not null
   * @return the comparator value, negative if less, positive if greater
   */
  def compareTo(other: Chronology): Int = {
     getId.compareTo(other.getId)
  }

  /**
   * Checks if this chronology is equal to another chronology.
   * <p>
   * The comparison is based on the entire state of the object.
   * <p>
   * The default implementation checks the type and calls {@link #compareTo(Chronology)}.
   *
   * @param obj  the object to check, null returns false
   * @return true if this is equal to the other chronology
   */
  override def equals(obj: AnyRef): Boolean = {
    if (this eq obj) {
       true
    }
    if (obj.isInstanceOf[Chronology]) {
       compareTo(obj.asInstanceOf[Chronology]) == 0
    }
     false
  }

  /**
   * A hash code for this chronology.
   * <p>
   * The default implementation is based on the ID and class.
   * Subclasses should add any additional state that they store.
   *
   * @return a suitable hash code
   */
  override def hashCode: Int = {
     getClass.hashCode ^ getId.hashCode
  }

  /**
   * Outputs this chronology as a {@code String}, using the ID.
   *
   * @return a string representation of this chronology, not null
   */
  override def toString: String = {
     getId
  }

  /**
   * Writes the Chronology using a
   * <a href="../../../serialized-form.html#java.time.chrono.Ser">dedicated serialized form</a>.
   * {{{
   * out.writeByte(1);  // identifies this as a Chronology
   * out.writeUTF(getId());
   * }}}
   *
   * @return the instance of { @code Ser}, not null
   */
  protected def writeReplace: AnyRef = {
     new Ser(Ser.CHRONO_TYPE, this)
  }

  /**
   * Defend against malicious streams.
   * @return never
   * @throws InvalidObjectException always
   */
  private def readResolve: AnyRef = {
    throw new InvalidObjectException("Deserialization via serialization delegate")
  }

  private[calendar] def writeExternal(out: DataOutput) {
    out.writeUTF(getId)
  }
}

/**
 * An era of the time-line.
 * <p>
 * Most calendar systems have a single epoch dividing the time-line into two eras.
 * However, some calendar systems, have multiple eras, such as one for the reign
 * of each leader.
 * In all cases, the era is conceptually the largest division of the time-line.
 * Each chronology defines the Era's that are known Eras and a
 * {@link Chronology#eras Chronology.eras} to get the valid eras.
 * <p>
 * For example, the Thai Buddhist calendar system divides time into two eras,
 * before and after a single date. By contrast, the Japanese calendar system
 * has one era for the reign of each Emperor.
 * <p>
 * Instances of {@code Era} may be compared using the {@code ==} operator.
 *
 * @implSpec
 * This interface must be implemented with care to ensure other classes operate correctly.
 * All implementations must be singletons - final, immutable and thread-safe.
 * It is recommended to use an enum whenever possible.
 *
 * @since 1.8
 */
trait Era extends TemporalAccessor with TemporalAdjuster {
  /**
   * Gets the numeric value associated with the era as defined by the chronology.
   * Each chronology defines the predefined Eras and methods to list the Eras
   * of the chronology.
   * <p>
   * All fields, including eras, have an associated numeric value.
   * The meaning of the numeric value for era is determined by the chronology
   * according to these principles:
   * <p><ul>
   * <li>The era in use at the epoch 1970-01-01 (ISO) has the value 1.
   * <li>Later eras have sequentially higher values.
   * <li>Earlier eras have sequentially lower values, which may be negative.
   * </ul><p>
   *
   * @return the numeric era value
   */
  def getValue: Int

  /**
   * Checks if the specified field is supported.
   * <p>
   * This checks if this era can be queried for the specified field.
   * If false, then calling the {@link #range(TemporalField) range} and
   * {@link #get(TemporalField) get} methods will throw an exception.
   * <p>
   * If the field is a {@link ChronoField} then the query is implemented here.
   * The {@code ERA} field returns true.
   * All other {@code ChronoField} instances will return false.
   * <p>
   * If the field is not a {@code ChronoField}, then the result of this method
   * is obtained by invoking {@code TemporalField.isSupportedBy(TemporalAccessor)}
   * passing {@code this} as the argument.
   * Whether the field is supported is determined by the field.
   *
   * @param field  the field to check, null returns false
   * @return true if the field is supported on this era, false if not
   */
  def isSupported(field: TemporalField): Boolean = {
    if (field.isInstanceOf[ChronoField]) {
       field eq ERA
    }
     field != null && field.isSupportedBy(this)
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
   * The default implementation must return a range for {@code ERA} from
   * zero to one, suitable for two era calendar systems such as ISO.
   *
   * @param field  the field to query the range for, not null
   * @return the range of valid values for the field, not null
   * @throws DateTimeException if the range for the field cannot be obtained
   * @throws UnsupportedTemporalTypeException if the unit is not supported
   */
  override def range(field: TemporalField): ValueRange = {
     TemporalAccessor.super.range(field)
  }

  /**
   * Gets the value of the specified field from this era as an {@code int}.
   * <p>
   * This queries this era for the value for the specified field.
   * The returned value will always be within the valid range of values for the field.
   * If it is not possible to return the value, because the field is not supported
   * or for some other reason, an exception is thrown.
   * <p>
   * If the field is a {@link ChronoField} then the query is implemented here.
   * The {@code ERA} field returns the value of the era.
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
    if (field eq ERA) {
       getValue
    }
     TemporalAccessor.super.get(field)
  }

  /**
   * Gets the value of the specified field from this era as a {@code long}.
   * <p>
   * This queries this era for the value for the specified field.
   * If it is not possible to return the value, because the field is not supported
   * or for some other reason, an exception is thrown.
   * <p>
   * If the field is a {@link ChronoField} then the query is implemented here.
   * The {@code ERA} field returns the value of the era.
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
    if (field eq ERA) {
       getValue
    }
    else if (field.isInstanceOf[ChronoField]) {
      throw new UnsupportedTemporalTypeException("Unsupported field: " + field)
    }
     field.getFrom(this)
  }

  /**
   * Queries this era using the specified query.
   * <p>
   * This queries this era using the specified query strategy object.
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
       ERAS.asInstanceOf[R]
    }
     TemporalAccessor.super.query(query)
  }

  /**
   * Adjusts the specified temporal object to have the same era as this object.
   * <p>
   * This returns a temporal object of the same observable type as the input
   * with the era changed to be the same as this.
   * <p>
   * The adjustment is equivalent to using {@link Temporal#with(TemporalField, long)}
   * passing {@link ChronoField#ERA} as the field.
   * <p>
   * In most cases, it is clearer to reverse the calling pattern by using
   * {@link Temporal#with(TemporalAdjuster)}:
   * {{{
   * // these two lines are equivalent, but the second approach is recommended
   * temporal = thisEra.adjustInto(temporal);
   * temporal = temporal.with(thisEra);
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
     temporal.`with`(ERA, getValue)
  }

  /**
   * Gets the textual representation of this era.
   * <p>
   * This returns the textual name used to identify the era,
   * suitable for presentation to the user.
   * The parameters control the style of the returned text and the locale.
   * <p>
   * If no textual mapping is found then the {@link #getValue() numeric value} is returned.
   * <p>
   * This default implementation is suitable for all implementations.
   *
   * @param style  the style of the text required, not null
   * @param locale  the locale to use, not null
   * @return the text value of the era, not null
   */
  def getDisplayName(style: TextStyle, locale: Locale): String = {
     new DateTimeFormatterBuilder().appendText(ERA, style).toFormatter(locale).format(this)
  }
}

/**
 * The shared serialization delegate for this package.
 *
 * @implNote
 * This class wraps the object being serialized, and takes a byte representing the type of the class to
 * be serialized.  This byte can also be used for versioning the serialization format.  In this case another
 * byte flag would be used in order to specify an alternative version of the type format.
 * For example { @code CHRONO_TYPE_VERSION_2 = 21}
 *                     <p>
 *                     In order to serialise the object it writes its byte and then calls back to the appropriate class where
 *                     the serialisation is performed.  In order to deserialise the object it read in the type byte, switching
 *                     in order to select which class to call back into.
 *                     <p>
 *                     The serialisation format is determined on a per class basis.  In the case of field based classes each
 *                     of the fields is written out with an appropriate size format in descending order of the field's size.  For
 *                     example in the case of { @link Date} year is written before month.  Composite classes, such as
 *                                                    { @link DateTime} are serialised as one object.  Enum classes are serialised using the index of their
 *                                                            element.
 *                                                            <p>
 *                                                            This class is mutable and should be created once per serialization.
 *
 * @serial include
 * @since 1.8
 */
object Ser {
  private def writeInternal(`type`: Byte, `object`: AnyRef, out: ObjectOutput) {
    out.writeByte(`type`)
    `type` match {
      case CHRONO_TYPE =>
        (`object`.asInstanceOf[Chronology]).writeExternal(out)
        break //todo: break is not supported
      case CHRONO_LOCAL_DATE_TIME_TYPE =>
        (`object`.asInstanceOf[ChronoLocalDateTimeImpl[_]]).writeExternal(out)
        break //todo: break is not supported
      case CHRONO_ZONE_DATE_TIME_TYPE =>
        (`object`.asInstanceOf[ChronoZonedDateTimeImpl[_]]).writeExternal(out)
        break //todo: break is not supported
      case JAPANESE_DATE_TYPE =>
        (`object`.asInstanceOf[JapaneseDate]).writeExternal(out)
        break //todo: break is not supported
      case JAPANESE_ERA_TYPE =>
        (`object`.asInstanceOf[JapaneseEra]).writeExternal(out)
        break //todo: break is not supported
      case HIJRAH_DATE_TYPE =>
        (`object`.asInstanceOf[HijrahDate]).writeExternal(out)
        break //todo: break is not supported
      case HIJRAH_ERA_TYPE =>
        (`object`.asInstanceOf[HijrahEra]).writeExternal(out)
        break //todo: break is not supported
      case MINGUO_DATE_TYPE =>
        (`object`.asInstanceOf[MinguoDate]).writeExternal(out)
        break //todo: break is not supported
      case MINGUO_ERA_TYPE =>
        (`object`.asInstanceOf[MinguoEra]).writeExternal(out)
        break //todo: break is not supported
      case THAIBUDDHIST_DATE_TYPE =>
        (`object`.asInstanceOf[ThaiBuddhistDate]).writeExternal(out)
        break //todo: break is not supported
      case THAIBUDDHIST_ERA_TYPE =>
        (`object`.asInstanceOf[ThaiBuddhistEra]).writeExternal(out)
        break //todo: break is not supported
      case CHRONO_PERIOD_TYPE =>
        (`object`.asInstanceOf[ChronoPeriodImpl]).writeExternal(out)
        break //todo: break is not supported
      case _ =>
        throw new InvalidClassException("Unknown serialized type")
    }
  }

  private[calendar] def read(in: ObjectInput): AnyRef = {
    val `type`: Byte = in.readByte
     readInternal(`type`, in)
  }

  private def readInternal(`type`: Byte, in: ObjectInput): AnyRef = {
    `type` match {
      case CHRONO_TYPE =>
         Chronology.readExternal(in)
      case CHRONO_LOCAL_DATE_TIME_TYPE =>
         ChronoLocalDateTimeImpl.readExternal(in)
      case CHRONO_ZONE_DATE_TIME_TYPE =>
         ChronoZonedDateTimeImpl.readExternal(in)
      case JAPANESE_DATE_TYPE =>
         JapaneseDate.readExternal(in)
      case JAPANESE_ERA_TYPE =>
         JapaneseEra.readExternal(in)
      case HIJRAH_DATE_TYPE =>
         HijrahDate.readExternal(in)
      case HIJRAH_ERA_TYPE =>
         HijrahEra.readExternal(in)
      case MINGUO_DATE_TYPE =>
         MinguoDate.readExternal(in)
      case MINGUO_ERA_TYPE =>
         MinguoEra.readExternal(in)
      case THAIBUDDHIST_DATE_TYPE =>
         ThaiBuddhistDate.readExternal(in)
      case THAIBUDDHIST_ERA_TYPE =>
         ThaiBuddhistEra.readExternal(in)
      case CHRONO_PERIOD_TYPE =>
         ChronoPeriodImpl.readExternal(in)
      case _ =>
        throw new StreamCorruptedException("Unknown serialized type")
    }
  }


  private[calendar] final val CHRONO_TYPE: Byte = 1
  private[calendar] final val CHRONO_LOCAL_DATE_TIME_TYPE: Byte = 2
  private[calendar] final val CHRONO_ZONE_DATE_TIME_TYPE: Byte = 3
  private[calendar] final val JAPANESE_DATE_TYPE: Byte = 4
  private[calendar] final val JAPANESE_ERA_TYPE: Byte = 5
  private[calendar] final val HIJRAH_DATE_TYPE: Byte = 6
  private[calendar] final val HIJRAH_ERA_TYPE: Byte = 7
  private[calendar] final val MINGUO_DATE_TYPE: Byte = 8
  private[calendar] final val MINGUO_ERA_TYPE: Byte = 9
  private[calendar] final val THAIBUDDHIST_DATE_TYPE: Byte = 10
  private[calendar] final val THAIBUDDHIST_ERA_TYPE: Byte = 11
  private[calendar] final val CHRONO_PERIOD_TYPE: Byte = 12
}

final class Ser  {
  /**
   * Constructor for deserialization.
   */
  def  {

  }

  /**
   * Creates an instance for serialization.
   *
   * @param type  the type
   * @param object  the object
   */
  private[calendar] def this(`type`: Byte, `object`: AnyRef) {

    this.`type` = `type`
    this.`object` = `object`
  }

  /**
   * Implements the {@code Externalizable} interface to write the object.
   *
   * @param out  the data stream to write to, not null
   */
  def writeExternal(out: ObjectOutput) {
    writeInternal(`type`, `object`, out)
  }

  /**
   * Implements the {@code Externalizable} interface to read the object.
   *
   * @param in  the data to read, not null
   */
  def readExternal(in: ObjectInput) {
    `type` = in.readByte
    `object` = readInternal(`type`, in)
  }

  /**
   * Returns the object that will replace this one.
   *
   * @return the read object, should never be null
   */
  private def readResolve: AnyRef = {
     `object`
  }

  /** The type being serialized. */
  private var `type`: Byte = 0
  /** The object being serialized. */
  private var `object`: AnyRef = null
}

