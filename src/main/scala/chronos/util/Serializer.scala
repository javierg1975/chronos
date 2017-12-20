package chronos.util

import chronos._
import chronos.time._

/**
 * The shared serialization delegate for this package.
 *
 * @implNote
 * This class wraps the object being serialized, and takes a byte representing the type of the class to
 * be serialized.  This byte can also be used for versioning the serialization format.  In this case another
 * byte flag would be used in order to specify an alternative version of the type format.
 * For example { @code LOCAL_DATE_TYPE_VERSION_2 = 21}.
 *                     <p>
 *                     In order to serialise the object it writes its byte and then calls back to the appropriate class where
 *                     the serialisation is performed.  In order to deserialise the object it read in the type byte, switching
 *                     in order to select which class to call back into.
 *                     <p>
 *                     The serialisation format is determined on a per class basis.  In the case of field based classes each
 *                     of the fields is written out with an appropriate size format in descending order of the field's size.  For
 *                     example in the case of { @link Date} year is written before month.  Composite classes, such as
 *                                                    { @link DateTime} are serialised as one object.
 *                                                            <p>
 *                                                            This class is mutable and should be created once per serialization.
 *
 * @serial include
 * @since 1.8
 */
object Ser {
  private[time] def writeInternal(`type`: Byte, `object`: AnyRef, out: ObjectOutput) {
    out.writeByte(`type`)
    `type` match {
      case DURATION_TYPE =>
        (`object`.asInstanceOf[Duration]).writeExternal(out)
        break //todo: break is not supported
      case INSTANT_TYPE =>
        (`object`.asInstanceOf[Instant]).writeExternal(out)
        break //todo: break is not supported
      case LOCAL_DATE_TYPE =>
        (`object`.asInstanceOf[Date]).writeExternal(out)
        break //todo: break is not supported
      case LOCAL_DATE_TIME_TYPE =>
        (`object`.asInstanceOf[DateTime]).writeExternal(out)
        break //todo: break is not supported
      case LOCAL_TIME_TYPE =>
        (`object`.asInstanceOf[Time]).writeExternal(out)
        break //todo: break is not supported
      case ZONE_REGION_TYPE =>
        (`object`.asInstanceOf[ZoneRegion]).writeExternal(out)
        break //todo: break is not supported
      case ZONE_OFFSET_TYPE =>
        (`object`.asInstanceOf[ZoneOffset]).writeExternal(out)
        break //todo: break is not supported
      case ZONE_DATE_TIME_TYPE =>
        (`object`.asInstanceOf[ZonedDateTime]).writeExternal(out)
        break //todo: break is not supported
      case OFFSET_TIME_TYPE =>
        (`object`.asInstanceOf[OffsetTime]).writeExternal(out)
        break //todo: break is not supported
      case OFFSET_DATE_TIME_TYPE =>
        (`object`.asInstanceOf[OffsetDateTime]).writeExternal(out)
        break //todo: break is not supported
      case YEAR_TYPE =>
        (`object`.asInstanceOf[Year]).writeExternal(out)
        break //todo: break is not supported
      case YEAR_MONTH_TYPE =>
        (`object`.asInstanceOf[YearMonth]).writeExternal(out)
        break //todo: break is not supported
      case MONTH_DAY_TYPE =>
        (`object`.asInstanceOf[MonthDay]).writeExternal(out)
        break //todo: break is not supported
      case PERIOD_TYPE =>
        (`object`.asInstanceOf[Period]).writeExternal(out)
        break //todo: break is not supported
      case _ =>
        throw new InvalidClassException("Unknown serialized type")
    }
  }

  private[time] def read(in: ObjectInput): AnyRef = {
    val `type`: Byte = in.readByte
     readInternal(`type`, in)
  }

  private def readInternal(`type`: Byte, in: ObjectInput): AnyRef = {
    `type` match {
      case DURATION_TYPE =>
         Duration.readExternal(in)
      case INSTANT_TYPE =>
         Instant.readExternal(in)
      case LOCAL_DATE_TYPE =>
         Date.readExternal(in)
      case LOCAL_DATE_TIME_TYPE =>
         DateTime.readExternal(in)
      case LOCAL_TIME_TYPE =>
         Time.readExternal(in)
      case ZONE_DATE_TIME_TYPE =>
         ZonedDateTime.readExternal(in)
      case ZONE_OFFSET_TYPE =>
         ZoneOffset.readExternal(in)
      case ZONE_REGION_TYPE =>
         ZoneRegion.readExternal(in)
      case OFFSET_TIME_TYPE =>
         OffsetTime.readExternal(in)
      case OFFSET_DATE_TIME_TYPE =>
         OffsetDateTime.readExternal(in)
      case YEAR_TYPE =>
         Year.readExternal(in)
      case YEAR_MONTH_TYPE =>
         YearMonth.readExternal(in)
      case MONTH_DAY_TYPE =>
         MonthDay.readExternal(in)
      case PERIOD_TYPE =>
         Period.readExternal(in)
      case _ =>
        throw new StreamCorruptedException("Unknown serialized type")
    }
  }


  private[time] final val DURATION_TYPE: Byte = 1
  private[time] final val INSTANT_TYPE: Byte = 2
  private[time] final val LOCAL_DATE_TYPE: Byte = 3
  private[time] final val LOCAL_TIME_TYPE: Byte = 4
  private[time] final val LOCAL_DATE_TIME_TYPE: Byte = 5
  private[time] final val ZONE_DATE_TIME_TYPE: Byte = 6
  private[time] final val ZONE_REGION_TYPE: Byte = 7
  private[time] final val ZONE_OFFSET_TYPE: Byte = 8
  private[time] final val OFFSET_TIME_TYPE: Byte = 9
  private[time] final val OFFSET_DATE_TIME_TYPE: Byte = 10
  private[time] final val YEAR_TYPE: Byte = 11
  private[time] final val YEAR_MONTH_TYPE: Byte = 12
  private[time] final val MONTH_DAY_TYPE: Byte = 13
  private[time] final val PERIOD_TYPE: Byte = 14
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
  private[time] def this(`type`: Byte, `object`: AnyRef) {

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

