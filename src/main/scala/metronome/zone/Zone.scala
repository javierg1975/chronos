package metronome.zone

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
 * The shared serialization delegate for this package.
 *
 * @implNote
 * This class is mutable and should be created once per serialization.
 *
 * @serial include
 * @since 1.8
 */
object Ser {
  private[zone] def write(`object`: AnyRef, out: DataOutput) {
    writeInternal(ZRULES, `object`, out)
  }

  private def writeInternal(`type`: Byte, `object`: AnyRef, out: DataOutput) {
    out.writeByte(`type`)
    `type` match {
      case ZRULES =>
        (`object`.asInstanceOf[ZoneRules]).writeExternal(out)
        break //todo: break is not supported
      case ZOT =>
        (`object`.asInstanceOf[ZoneOffsetTransition]).writeExternal(out)
        break //todo: break is not supported
      case ZOTRULE =>
        (`object`.asInstanceOf[ZoneOffsetTransitionRule]).writeExternal(out)
        break //todo: break is not supported
      case _ =>
        throw new InvalidClassException("Unknown serialized type")
    }
  }

  private[zone] def read(in: DataInput): AnyRef = {
    val `type`: Byte = in.readByte
     readInternal(`type`, in)
  }

  private def readInternal(`type`: Byte, in: DataInput): AnyRef = {
    `type` match {
      case ZRULES =>
         ZoneRules.readExternal(in)
      case ZOT =>
         ZoneOffsetTransition.readExternal(in)
      case ZOTRULE =>
         ZoneOffsetTransitionRule.readExternal(in)
      case _ =>
        throw new StreamCorruptedException("Unknown serialized type")
    }
  }

  /**
   * Writes the state to the stream.
   *
   * @param offset  the offset, not null
   * @param out  the output stream, not null
   * @throws IOException if an error occurs
   */
  private[zone] def writeOffset(offset: ZoneOffset, out: DataOutput) {
    val offsetSecs: Int = offset.getTotalSeconds
    val offsetByte: Int = if (offsetSecs % 900 == 0) offsetSecs / 900 else 127
    out.writeByte(offsetByte)
    if (offsetByte == 127) {
      out.writeInt(offsetSecs)
    }
  }

  /**
   * Reads the state from the stream.
   *
   * @param in  the input stream, not null
   * @return the created object, not null
   * @throws IOException if an error occurs
   */
  private[zone] def readOffset(in: DataInput): ZoneOffset = {
    val offsetByte: Int = in.readByte
     (if (offsetByte == 127) ZoneOffset.ofTotalSeconds(in.readInt) else ZoneOffset.ofTotalSeconds(offsetByte * 900))
  }

  /**
   * Writes the state to the stream.
   *
   * @param epochSec  the epoch seconds, not null
   * @param out  the output stream, not null
   * @throws IOException if an error occurs
   */
  private[zone] def writeEpochSec(epochSec: Long, out: DataOutput) {
    if (epochSec >= -4575744000L && epochSec < 10413792000L && epochSec % 900 == 0) {
      val store: Int = ((epochSec + 4575744000L) / 900).asInstanceOf[Int]
      out.writeByte((store >>> 16) & 255)
      out.writeByte((store >>> 8) & 255)
      out.writeByte(store & 255)
    }
    else {
      out.writeByte(255)
      out.writeLong(epochSec)
    }
  }

  /**
   * Reads the state from the stream.
   *
   * @param in  the input stream, not null
   * @return the epoch seconds, not null
   * @throws IOException if an error occurs
   */
  private[zone] def readEpochSec(in: DataInput): Long = {
    val hiByte: Int = in.readByte & 255
    if (hiByte == 255) {
       in.readLong
    }
    else {
      val midByte: Int = in.readByte & 255
      val loByte: Int = in.readByte & 255
      val tot: Long = ((hiByte << 16) + (midByte << 8) + loByte)
       (tot * 900) - 4575744000L
    }
  }


  /** Type for ZoneRules. */
  private[zone] final val ZRULES: Byte = 1
  /** Type for ZoneOffsetTransition. */
  private[zone] final val ZOT: Byte = 2
  /** Type for ZoneOffsetTransition. */
  private[zone] final val ZOTRULE: Byte = 3
}

final class Ser  {


  /**
   * Creates an instance for serialization.
   *
   * @param type  the type
   * @param object  the object
   */
  private[zone] def this(`type`: Byte, `object`: AnyRef) {

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
 * Copyright (c) 2009-2012, Stephen Colebourne & Michael Nascimento Santos
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
 * Loads time-zone rules for 'TZDB'.
 *
 * @since 1.8
 */
final class TzdbZoneRulesProvider extends ZoneRulesProvider {
  /**
   * Creates an instance.
   * Created by the {@code ServiceLoader}.
   *
   * @throws ZoneRulesException if unable to load
   */
  def  {

    try {
      val libDir: String = System.getProperty("java.home") + File.separator + "lib"
      try {
        load(dis)
      }
    }
    catch {
      case ex: Exception => {
        throw new ZoneRulesException("Unable to load TZDB time-zone rules", ex)
      }
    }
  }

  protected def provideZoneIds: Nothing = {
     new Nothing(regionIds)
  }

  protected def provideRules(zoneId: String, forCaching: Boolean): ZoneRules = {
    var obj: AnyRef = regionToRules.get(zoneId)
    if (obj == null) {
      throw new ZoneRulesException("Unknown time-zone ID: " + zoneId)
    }
    try {
      if (obj.isInstanceOf[Array[Byte]]) {
        val bytes: Array[Byte] = obj.asInstanceOf[Array[Byte]]
        val dis: Nothing = new Nothing(new Nothing(bytes))
        obj = Ser.read(dis)
        regionToRules.put(zoneId, obj)
      }
       obj.asInstanceOf[ZoneRules]
    }
    catch {
      case ex: Exception => {
        throw new ZoneRulesException("Invalid binary time-zone data: TZDB:" + zoneId + ", version: " + versionId, ex)
      }
    }
  }

  protected def provideVersions(zoneId: String): Nothing = {
    val map: Nothing = new Nothing
    val rules: ZoneRules = getRules(zoneId, false)
    if (rules != null) {
      map.put(versionId, rules)
    }
     map
  }

  /**
   * Loads the rules from a DateInputStream, often in a jar file.
   *
   * @param dis  the DateInputStream to load, not null
   * @throws Exception if an error occurs
   */
  private def load(dis: Nothing) {
    if (dis.readByte ne 1) {
      throw new StreamCorruptedException("File format not recognised")
    }
    val groupId: String = dis.readUTF
    if (("TZDB" == groupId) == false) {
      throw new StreamCorruptedException("File format not recognised")
    }
    val versionCount: Int = dis.readShort
    {
      var i: Int = 0
      while (i < versionCount) {
        {
          versionId = dis.readUTF
        }
        ({
          i += 1; i - 1
        })
      }
    }
    val regionCount: Int = dis.readShort
    val regionArray: Array[String] = new Array[String](regionCount)
    {
      var i: Int = 0
      while (i < regionCount) {
        {
          regionArray(i) = dis.readUTF
        }
        ({
          i += 1; i - 1
        })
      }
    }
    regionIds = Arrays.asList(regionArray)
    val ruleCount: Int = dis.readShort
    val ruleArray: Array[AnyRef] = new Array[AnyRef](ruleCount)
    {
      var i: Int = 0
      while (i < ruleCount) {
        {
          val bytes: Array[Byte] = new Array[Byte](dis.readShort)
          dis.readFully(bytes)
          ruleArray(i) = bytes
        }
        ({
          i += 1; i - 1
        })
      }
    }
    {
      var i: Int = 0
      while (i < versionCount) {
        {
          val versionRegionCount: Int = dis.readShort
          regionToRules.clear
          {
            var j: Int = 0
            while (j < versionRegionCount) {
              {
                val region: String = regionArray(dis.readShort)
                val rule: AnyRef = ruleArray(dis.readShort & 0xffff)
                regionToRules.put(region, rule)
              }
              ({
                j += 1; j - 1
              })
            }
          }
        }
        ({
          i += 1; i - 1
        })
      }
    }
  }

  override def toString: String = {
     "TZDB[" + versionId + "]"
  }

  /**
   * All the regions that are available.
   */
  private var regionIds: Nothing = null
  /**
   * Version Id of this tzdb rules
   */
  private var versionId: String = null
  /**
   * Region to rules mapping
   */
  private final val regionToRules: Nothing = new Nothing
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
 * Copyright (c) 2009-2012, Stephen Colebourne & Michael Nascimento Santos
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
 * A transition between two offsets caused by a discontinuity in the local time-line.
 * <p>
 * A transition between two offsets is normally the result of a daylight savings cutover.
 * The discontinuity is normally a gap in spring and an overlap in autumn.
 * {@code ZoneOffsetTransition} models the transition between the two offsets.
 * <p>
 * Gaps occur where there are local date-times that simply do not not exist.
 * An example would be when the offset changes from {@code +03:00} to {@code +04:00}.
 * This might be described as 'the clocks will move forward one hour tonight at 1am'.
 * <p>
 * Overlaps occur where there are local date-times that exist twice.
 * An example would be when the offset changes from {@code +04:00} to {@code +03:00}.
 * This might be described as 'the clocks will move back one hour tonight at 2am'.
 *
 * @implSpec
 * This class is immutable and thread-safe.
 *
 * @since 1.8
 */
object ZoneOffsetTransition {
  /**
   * Obtains an instance defining a transition between two offsets.
   * <p>
   * Applications should normally obtain an instance from {@link ZoneRules}.
   * This factory is only intended for use when creating {@link ZoneRules}.
   *
   * @param transition  the transition date-time at the transition, which never
   *                    actually occurs, expressed local to the before offset, not null
   * @param offsetBefore  the offset before the transition, not null
   * @param offsetAfter  the offset at and after the transition, not null
   * @return the transition, not null
   * @throws IllegalArgumentException if { @code offsetBefore} and { @code offsetAfter}
   *                                                                       are equal, or { @code transition.getNano()} returns non-zero value
   */
  def of(transition: Nothing, offsetBefore: ZoneOffset, offsetAfter: ZoneOffset): ZoneOffsetTransition = {



    if (offsetBefore == offsetAfter) {
      throw new IllegalArgumentException("Offsets must not be equal")
    }
    if (transition.getNano ne 0) {
      throw new IllegalArgumentException("Nano-of-second must be zero")
    }
     new ZoneOffsetTransition(transition, offsetBefore, offsetAfter)
  }

  /**
   * Reads the state from the stream.
   *
   * @param in  the input stream, not null
   * @return the created object, not null
   * @throws IOException if an error occurs
   */
  private[zone] def readExternal(in: DataInput): ZoneOffsetTransition = {
    val epochSecond: Long = Ser.readEpochSec(in)
    val before: ZoneOffset = Ser.readOffset(in)
    val after: ZoneOffset = Ser.readOffset(in)
    if (before == after) {
      throw new IllegalArgumentException("Offsets must not be equal")
    }
     new ZoneOffsetTransition(epochSecond, before, after)
  }


}

final class ZoneOffsetTransition extends Comparable[ZoneOffsetTransition]  {
  /**
   * Creates an instance defining a transition between two offsets.
   *
   * @param transition  the transition date-time with the offset before the transition, not null
   * @param offsetBefore  the offset before the transition, not null
   * @param offsetAfter  the offset at and after the transition, not null
   */
  private[zone] def this(transition: Nothing, offsetBefore: ZoneOffset, offsetAfter: ZoneOffset) {

    this.transition = transition
    this.offsetBefore = offsetBefore
    this.offsetAfter = offsetAfter
  }

  /**
   * Creates an instance from epoch-second and offsets.
   *
   * @param epochSecond  the transition epoch-second
   * @param offsetBefore  the offset before the transition, not null
   * @param offsetAfter  the offset at and after the transition, not null
   */
  private[zone] def this(epochSecond: Long, offsetBefore: ZoneOffset, offsetAfter: ZoneOffset) {

    this.transition = LocalDateTime.ofEpochSecond(epochSecond, 0, offsetBefore)
    this.offsetBefore = offsetBefore
    this.offsetAfter = offsetAfter
  }

  /**
   * Uses a serialization delegate.
   *
   * @return the replacing object, not null
   */
  private def writeReplace: AnyRef = {
     new Ser(Ser.ZOT, this)
  }

  /**
   * Writes the state to the stream.
   *
   * @param out  the output stream, not null
   * @throws IOException if an error occurs
   */
  private[zone] def writeExternal(out: DataOutput) {
    Ser.writeEpochSec(toEpochSecond, out)
    Ser.writeOffset(offsetBefore, out)
    Ser.writeOffset(offsetAfter, out)
  }

  /**
   * Gets the transition instant.
   * <p>
   * This is the instant of the discontinuity, which is defined as the first
   * instant that the 'after' offset applies.
   * <p>
   * The methods {@link #getInstant()}, {@link #getDateTimeBefore()} and {@link #getDateTimeAfter()}
   * all represent the same instant.
   *
   * @return the transition instant, not null
   */
  def getInstant: Nothing = {
     transition.toInstant(offsetBefore)
  }

  /**
   * Gets the transition instant as an epoch second.
   *
   * @return the transition epoch second
   */
  def toEpochSecond: Long = {
     transition.toEpochSecond(offsetBefore)
  }

  /**
   * Gets the local transition date-time, as would be expressed with the 'before' offset.
   * <p>
   * This is the date-time where the discontinuity begins expressed with the 'before' offset.
   * At this instant, the 'after' offset is actually used, therefore the combination of this
   * date-time and the 'before' offset will never occur.
   * <p>
   * The combination of the 'before' date-time and offset represents the same instant
   * as the 'after' date-time and offset.
   *
   * @return the transition date-time expressed with the before offset, not null
   */
  def getDateTimeBefore: Nothing = {
     transition
  }

  /**
   * Gets the local transition date-time, as would be expressed with the 'after' offset.
   * <p>
   * This is the first date-time after the discontinuity, when the new offset applies.
   * <p>
   * The combination of the 'before' date-time and offset represents the same instant
   * as the 'after' date-time and offset.
   *
   * @return the transition date-time expressed with the after offset, not null
   */
  def getDateTimeAfter: Nothing = {
     transition.plusSeconds(getDurationSeconds)
  }

  /**
   * Gets the offset before the transition.
   * <p>
   * This is the offset in use before the instant of the transition.
   *
   * @return the offset before the transition, not null
   */
  def getOffsetBefore: ZoneOffset = {
     offsetBefore
  }

  /**
   * Gets the offset after the transition.
   * <p>
   * This is the offset in use on and after the instant of the transition.
   *
   * @return the offset after the transition, not null
   */
  def getOffsetAfter: ZoneOffset = {
     offsetAfter
  }

  /**
   * Gets the duration of the transition.
   * <p>
   * In most cases, the transition duration is one hour, however this is not always the case.
   * The duration will be positive for a gap and negative for an overlap.
   * Time-zones are second-based, so the nanosecond part of the duration will be zero.
   *
   * @return the duration of the transition, positive for gaps, negative for overlaps
   */
  def getDuration: Nothing = {
     Duration.ofSeconds(getDurationSeconds)
  }

  /**
   * Gets the duration of the transition in seconds.
   *
   * @return the duration in seconds
   */
  private def getDurationSeconds: Int = {
     getOffsetAfter.getTotalSeconds - getOffsetBefore.getTotalSeconds
  }

  /**
   * Does this transition represent a gap in the local time-line.
   * <p>
   * Gaps occur where there are local date-times that simply do not not exist.
   * An example would be when the offset changes from {@code +01:00} to {@code +02:00}.
   * This might be described as 'the clocks will move forward one hour tonight at 1am'.
   *
   * @return true if this transition is a gap, false if it is an overlap
   */
  def isGap: Boolean = {
     getOffsetAfter.getTotalSeconds > getOffsetBefore.getTotalSeconds
  }

  /**
   * Does this transition represent an overlap in the local time-line.
   * <p>
   * Overlaps occur where there are local date-times that exist twice.
   * An example would be when the offset changes from {@code +02:00} to {@code +01:00}.
   * This might be described as 'the clocks will move back one hour tonight at 2am'.
   *
   * @return true if this transition is an overlap, false if it is a gap
   */
  def isOverlap: Boolean = {
     getOffsetAfter.getTotalSeconds < getOffsetBefore.getTotalSeconds
  }

  /**
   * Checks if the specified offset is valid during this transition.
   * <p>
   * This checks to see if the given offset will be valid at some point in the transition.
   * A gap will always return false.
   * An overlap will return true if the offset is either the before or after offset.
   *
   * @param offset  the offset to check, null returns false
   * @return true if the offset is valid during the transition
   */
  def isValidOffset(offset: ZoneOffset): Boolean = {
     if (isGap) false else ((getOffsetBefore == offset) || (getOffsetAfter == offset))
  }

  /**
   * Gets the valid offsets during this transition.
   * <p>
   * A gap will return an empty list, while an overlap will return both offsets.
   *
   * @return the list of valid offsets
   */
  private[zone] def getValidOffsets: Nothing = {
    if (isGap) {
       Collections.emptyList
    }
     Arrays.asList(getOffsetBefore, getOffsetAfter)
  }

  /**
   * Compares this transition to another based on the transition instant.
   * <p>
   * This compares the instants of each transition.
   * The offsets are ignored, making this order inconsistent with equals.
   *
   * @param transition  the transition to compare to, not null
   * @return the comparator value, negative if less, positive if greater
   */
  def compareTo(transition: ZoneOffsetTransition): Int = {
     this.getInstant.compareTo(transition.getInstant)
  }

  /**
   * Checks if this object equals another.
   * <p>
   * The entire state of the object is compared.
   *
   * @param other  the other object to compare to, null returns false
   * @return true if equal
   */
  override def equals(other: AnyRef): Boolean = {
    if (other eq this) {
       true
    }
    if (other.isInstanceOf[ZoneOffsetTransition]) {
      val d: ZoneOffsetTransition = other.asInstanceOf[ZoneOffsetTransition]
       (transition == d.transition) && (offsetBefore == d.offsetBefore) && (offsetAfter == d.offsetAfter)
    }
     false
  }

  /**
   * Returns a suitable hash code.
   *
   * @return the hash code
   */
  override def hashCode: Int = {
     transition.hashCode ^ offsetBefore.hashCode ^ Integer.rotateLeft(offsetAfter.hashCode, 16)
  }

  /**
   * Returns a string describing this object.
   *
   * @return a string for debugging, not null
   */
  override def toString: String = {
    val buf: StringBuilder = new StringBuilder
    buf.append("Transition[").append(if (isGap) "Gap" else "Overlap").append(" at ").append(transition).append(offsetBefore).append(" to ").append(offsetAfter).append(']')
     buf.toString
  }

  /**
   * The local transition date-time at the transition.
   */
  private final val transition: Nothing = null
  /**
   * The offset before transition.
   */
  private final val offsetBefore: ZoneOffset = null
  /**
   * The offset after transition.
   */
  private final val offsetAfter: ZoneOffset = null
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
 * Copyright (c) 2009-2012, Stephen Colebourne & Michael Nascimento Santos
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
 * A rule expressing how to create a transition.
 * <p>
 * This class allows rules for identifying future transitions to be expressed.
 * A rule might be written in many forms:
 * <p><ul>
 * <li>the 16th March
 * <li>the Sunday on or after the 16th March
 * <li>the Sunday on or before the 16th March
 * <li>the last Sunday in February
 * </ul><p>
 * These different rule types can be expressed and queried.
 *
 * @implSpec
 * This class is immutable and thread-safe.
 *
 * @since 1.8
 */
object ZoneOffsetTransitionRule {
  /**
   * Obtains an instance defining the yearly rule to create transitions between two offsets.
   * <p>
   * Applications should normally obtain an instance from {@link ZoneRules}.
   * This factory is only intended for use when creating {@link ZoneRules}.
   *
   * @param month  the month of the month-day of the first day of the cutover week, not null
   * @param dayOfMonthIndicator  the day of the month-day of the cutover week, positive if the week is that
   *                             day or later, negative if the week is that day or earlier, counting from the last day of the month,
   *                             from -28 to 31 excluding 0
   * @param dayOfWeek  the required day-of-week, null if the month-day should not be changed
   * @param time  the cutover time in the 'before' offset, not null
   * @param timeEndOfDay  whether the time is midnight at the end of day
   * @param timeDefnition  how to interpret the cutover
   * @param standardOffset  the standard offset in force at the cutover, not null
   * @param offsetBefore  the offset before the cutover, not null
   * @param offsetAfter  the offset after the cutover, not null
   * @return the rule, not null
   * @throws IllegalArgumentException if the day of month indicator is invalid
   * @throws IllegalArgumentException if the end of day flag is true when the time is not midnight
   */
  def of(month: Nothing, dayOfMonthIndicator: Int, dayOfWeek: Nothing, time: Nothing, timeEndOfDay: Boolean, timeDefnition: ZoneOffsetTransitionRule.TimeDefinition, standardOffset: ZoneOffset, offsetBefore: ZoneOffset, offsetAfter: ZoneOffset): ZoneOffsetTransitionRule = {






    if (dayOfMonthIndicator < -28 || dayOfMonthIndicator > 31 || dayOfMonthIndicator == 0) {
      throw new IllegalArgumentException("Day of month indicator must be between -28 and 31 inclusive excluding zero")
    }
    if (timeEndOfDay && (time == LocalTime.MIDNIGHT) eq false) {
      throw new IllegalArgumentException("Time must be midnight when end of day flag is true")
    }
     new ZoneOffsetTransitionRule(month, dayOfMonthIndicator, dayOfWeek, time, timeEndOfDay, timeDefnition, standardOffset, offsetBefore, offsetAfter)
  }

  /**
   * Reads the state from the stream.
   *
   * @param in  the input stream, not null
   * @return the created object, not null
   * @throws IOException if an error occurs
   */
  private[zone] def readExternal(in: DataInput): ZoneOffsetTransitionRule = {
    val data: Int = in.readInt
    val month: Nothing = Month.of(data >>> 28)
    val dom: Int = ((data & (63 << 22)) >>> 22) - 32
    val dowByte: Int = (data & (7 << 19)) >>> 19
    val dow: Nothing = if (dowByte == 0) null else DayOfWeek.of(dowByte)
    val timeByte: Int = (data & (31 << 14)) >>> 14
    val defn: ZoneOffsetTransitionRule.TimeDefinition = TimeDefinition.values((data & (3 << 12)) >>> 12)
    val stdByte: Int = (data & (255 << 4)) >>> 4
    val beforeByte: Int = (data & (3 << 2)) >>> 2
    val afterByte: Int = (data & 3)
    val time: Nothing = (if (timeByte == 31) LocalTime.ofSecondOfDay(in.readInt) else LocalTime.of(timeByte % 24, 0))
    val std: ZoneOffset = (if (stdByte == 255) ZoneOffset.ofTotalSeconds(in.readInt) else ZoneOffset.ofTotalSeconds((stdByte - 128) * 900))
    val before: ZoneOffset = (if (beforeByte == 3) ZoneOffset.ofTotalSeconds(in.readInt) else ZoneOffset.ofTotalSeconds(std.getTotalSeconds + beforeByte * 1800))
    val after: ZoneOffset = (if (afterByte == 3) ZoneOffset.ofTotalSeconds(in.readInt) else ZoneOffset.ofTotalSeconds(std.getTotalSeconds + afterByte * 1800))
     ZoneOffsetTransitionRule.of(month, dom, dow, time, timeByte == 24, defn, std, before, after)
  }

  /**
   * Serialization version.
   */
  private final val serialVersionUID: Long = 6889046316657758795L

  /**
   * A definition of the way a local time can be converted to the actual
   * transition date-time.
   * <p>
   * Time zone rules are expressed in one of three ways:
   * <p><ul>
   * <li>Relative to UTC</li>
   * <li>Relative to the standard offset in force</li>
   * <li>Relative to the wall offset (what you would see on a clock on the wall)</li>
   * </ul><p>
   */
  object TimeDefinition {
    /** The local date-time is expressed in terms of the UTC offset. */
    final val UTC: = null
    /** The local date-time is expressed in terms of the wall offset. */
    final val WALL: = null
    /** The local date-time is expressed in terms of the standard offset. */
    final val STANDARD: = null
  }

  final class TimeDefinition {
    /**
     * Converts the specified local date-time to the local date-time actually
     * seen on a wall clock.
     * <p>
     * This method converts using the type of this enum.
     * The output is defined relative to the 'before' offset of the transition.
     * <p>
     * The UTC type uses the UTC offset.
     * The STANDARD type uses the standard offset.
     * The WALL type returns the input date-time.
     * The result is intended for use with the wall-offset.
     *
     * @param dateTime  the local date-time, not null
     * @param standardOffset  the standard offset, not null
     * @param wallOffset  the wall offset, not null
     * @return the date-time relative to the wall/before offset, not null
     */
    def createDateTime(dateTime: Nothing, standardOffset: ZoneOffset, wallOffset: ZoneOffset): Nothing = {
      this match {
        case UTC => {
          val difference: Int = wallOffset.getTotalSeconds - ZoneOffset.UTC.getTotalSeconds
           dateTime.plusSeconds(difference)
        }
        case STANDARD => {
          val difference: Int = wallOffset.getTotalSeconds - standardOffset.getTotalSeconds
           dateTime.plusSeconds(difference)
        }
        case _ =>
           dateTime
      }
    }
  }

}

final class ZoneOffsetTransitionRule extends Serializable {
  /**
   * Creates an instance defining the yearly rule to create transitions between two offsets.
   *
   * @param month  the month of the month-day of the first day of the cutover week, not null
   * @param dayOfMonthIndicator  the day of the month-day of the cutover week, positive if the week is that
   *                             day or later, negative if the week is that day or earlier, counting from the last day of the month,
   *                             from -28 to 31 excluding 0
   * @param dayOfWeek  the required day-of-week, null if the month-day should not be changed
   * @param time  the cutover time in the 'before' offset, not null
   * @param timeEndOfDay  whether the time is midnight at the end of day
   * @param timeDefnition  how to interpret the cutover
   * @param standardOffset  the standard offset in force at the cutover, not null
   * @param offsetBefore  the offset before the cutover, not null
   * @param offsetAfter  the offset after the cutover, not null
   * @throws IllegalArgumentException if the day of month indicator is invalid
   * @throws IllegalArgumentException if the end of day flag is true when the time is not midnight
   */
  private[zone] def this(month: Nothing, dayOfMonthIndicator: Int, dayOfWeek: Nothing, time: Nothing, timeEndOfDay: Boolean, timeDefnition: ZoneOffsetTransitionRule.TimeDefinition, standardOffset: ZoneOffset, offsetBefore: ZoneOffset, offsetAfter: ZoneOffset) {

    this.month = month
    this.dom = dayOfMonthIndicator.asInstanceOf[Byte]
    this.dow = dayOfWeek
    this.time = time
    this.timeEndOfDay = timeEndOfDay
    this.timeDefinition = timeDefnition
    this.standardOffset = standardOffset
    this.offsetBefore = offsetBefore
    this.offsetAfter = offsetAfter
  }

  /**
   * Uses a serialization delegate.
   *
   * @return the replacing object, not null
   */
  private def writeReplace: AnyRef = {
     new Ser(Ser.ZOTRULE, this)
  }

  /**
   * Writes the state to the stream.
   *
   * @param out  the output stream, not null
   * @throws IOException if an error occurs
   */
  private[zone] def writeExternal(out: DataOutput) {
    val timeSecs: Int = (if (timeEndOfDay) 86400 else time.toSecondOfDay)
    val stdOffset: Int = standardOffset.getTotalSeconds
    val beforeDiff: Int = offsetBefore.getTotalSeconds - stdOffset
    val afterDiff: Int = offsetAfter.getTotalSeconds - stdOffset
    val timeByte: Int = (if (timeSecs % 3600 == 0) (if (timeEndOfDay) 24 else time.getHour) else 31)
    val stdOffsetByte: Int = (if (stdOffset % 900 == 0) stdOffset / 900 + 128 else 255)
    val beforeByte: Int = (if (beforeDiff == 0 || beforeDiff == 1800 || beforeDiff == 3600) beforeDiff / 1800 else 3)
    val afterByte: Int = (if (afterDiff == 0 || afterDiff == 1800 || afterDiff == 3600) afterDiff / 1800 else 3)
    val dowByte: Int = (if (dow == null) 0 else dow.getValue)
    val b: Int = (month.getValue << 28) + ((dom + 32) << 22) + (dowByte << 19) + (timeByte << 14) + (timeDefinition.ordinal << 12) + (stdOffsetByte << 4) + (beforeByte << 2) + afterByte
    out.writeInt(b)
    if (timeByte == 31) {
      out.writeInt(timeSecs)
    }
    if (stdOffsetByte == 255) {
      out.writeInt(stdOffset)
    }
    if (beforeByte == 3) {
      out.writeInt(offsetBefore.getTotalSeconds)
    }
    if (afterByte == 3) {
      out.writeInt(offsetAfter.getTotalSeconds)
    }
  }

  /**
   * Gets the month of the transition.
   * <p>
   * If the rule defines an exact date then the month is the month of that date.
   * <p>
   * If the rule defines a week where the transition might occur, then the month
   * if the month of either the earliest or latest possible date of the cutover.
   *
   * @return the month of the transition, not null
   */
  def getMonth: Nothing = {
     month
  }

  /**
   * Gets the indicator of the day-of-month of the transition.
   * <p>
   * If the rule defines an exact date then the day is the month of that date.
   * <p>
   * If the rule defines a week where the transition might occur, then the day
   * defines either the start of the end of the transition week.
   * <p>
   * If the value is positive, then it represents a normal day-of-month, and is the
   * earliest possible date that the transition can be.
   * The date may refer to 29th February which should be treated as 1st March in non-leap years.
   * <p>
   * If the value is negative, then it represents the number of days back from the
   * end of the month where {@code -1} is the last day of the month.
   * In this case, the day identified is the latest possible date that the transition can be.
   *
   * @return the day-of-month indicator, from -28 to 31 excluding 0
   */
  def getDayOfMonthIndicator: Int = {
     dom
  }

  /**
   * Gets the day-of-week of the transition.
   * <p>
   * If the rule defines an exact date then this returns null.
   * <p>
   * If the rule defines a week where the cutover might occur, then this method
   * returns the day-of-week that the month-day will be adjusted to.
   * If the day is positive then the adjustment is later.
   * If the day is negative then the adjustment is earlier.
   *
   * @return the day-of-week that the transition occurs, null if the rule defines an exact date
   */
  def getDayOfWeek: Nothing = {
     dow
  }

  /**
   * Gets the local time of day of the transition which must be checked with
   * {@link #isMidnightEndOfDay()}.
   * <p>
   * The time is converted into an instant using the time definition.
   *
   * @return the local time of day of the transition, not null
   */
  def getLocalTime: Nothing = {
     time
  }

  /**
   * Is the transition local time midnight at the end of day.
   * <p>
   * The transition may be represented as occurring at 24:00.
   *
   * @return whether a local time of midnight is at the start or end of the day
   */
  def isMidnightEndOfDay: Boolean = {
     timeEndOfDay
  }

  /**
   * Gets the time definition, specifying how to convert the time to an instant.
   * <p>
   * The local time can be converted to an instant using the standard offset,
   * the wall offset or UTC.
   *
   * @return the time definition, not null
   */
  def getTimeDefinition: ZoneOffsetTransitionRule.TimeDefinition = {
     timeDefinition
  }

  /**
   * Gets the standard offset in force at the transition.
   *
   * @return the standard offset, not null
   */
  def getStandardOffset: ZoneOffset = {
     standardOffset
  }

  /**
   * Gets the offset before the transition.
   *
   * @return the offset before, not null
   */
  def getOffsetBefore: ZoneOffset = {
     offsetBefore
  }

  /**
   * Gets the offset after the transition.
   *
   * @return the offset after, not null
   */
  def getOffsetAfter: ZoneOffset = {
     offsetAfter
  }

  /**
   * Creates a transition instance for the specified year.
   * <p>
   * Calculations are performed using the ISO-8601 chronology.
   *
   * @param year  the year to create a transition for, not null
   * @return the transition instance, not null
   */
  def createTransition(year: Int): ZoneOffsetTransition = {
    var date: Nothing = null
    if (dom < 0) {
      date = LocalDate.of(year, month, month.length(IsoChronology.INSTANCE.isLeapYear(year)) + 1 + dom)
      if (dow != null) {
        date = date.`with`(previousOrSame(dow))
      }
    }
    else {
      date = LocalDate.of(year, month, dom)
      if (dow != null) {
        date = date.`with`(nextOrSame(dow))
      }
    }
    if (timeEndOfDay) {
      date = date.plusDays(1)
    }
    val localDT: Nothing = LocalDateTime.of(date, time)
    val transition: Nothing = timeDefinition.createDateTime(localDT, standardOffset, offsetBefore)
     new ZoneOffsetTransition(transition, offsetBefore, offsetAfter)
  }

  /**
   * Checks if this object equals another.
   * <p>
   * The entire state of the object is compared.
   *
   * @param otherRule  the other object to compare to, null returns false
   * @return true if equal
   */
  override def equals(otherRule: AnyRef): Boolean = {
    if (otherRule eq this) {
       true
    }
    if (otherRule.isInstanceOf[ZoneOffsetTransitionRule]) {
      val other: ZoneOffsetTransitionRule = otherRule.asInstanceOf[ZoneOffsetTransitionRule]
       month eq other.month && dom == other.dom && dow eq other.dow && timeDefinition eq other.timeDefinition && (time == other.time) && timeEndOfDay == other.timeEndOfDay && (standardOffset == other.standardOffset) && (offsetBefore == other.offsetBefore) && (offsetAfter == other.offsetAfter)
    }
     false
  }

  /**
   * Returns a suitable hash code.
   *
   * @return the hash code
   */
  override def hashCode: Int = {
    val hash: Int = ((time.toSecondOfDay + (if (timeEndOfDay) 1 else 0)) << 15) + (month.ordinal << 11) + ((dom + 32) << 5) + ((if (dow == null) 7 else dow.ordinal) << 2) + (timeDefinition.ordinal)
     hash ^ standardOffset.hashCode ^ offsetBefore.hashCode ^ offsetAfter.hashCode
  }

  /**
   * Returns a string describing this object.
   *
   * @return a string for debugging, not null
   */
  override def toString: String = {
    val buf: StringBuilder = new StringBuilder
    buf.append("TransitionRule[").append(if (offsetBefore.compareTo(offsetAfter) > 0) "Gap " else "Overlap ").append(offsetBefore).append(" to ").append(offsetAfter).append(", ")
    if (dow != null) {
      if (dom == -1) {
        buf.append(dow.name).append(" on or before last day of ").append(month.name)
      }
      else if (dom < 0) {
        buf.append(dow.name).append(" on or before last day minus ").append(-dom - 1).append(" of ").append(month.name)
      }
      else {
        buf.append(dow.name).append(" on or after ").append(month.name).append(' ').append(dom)
      }
    }
    else {
      buf.append(month.name).append(' ').append(dom)
    }
    buf.append(" at ").append(if (timeEndOfDay) "24:00" else time.toString).append(" ").append(timeDefinition).append(", standard offset ").append(standardOffset).append(']')
     buf.toString
  }

  /**
   * The month of the month-day of the first day of the cutover week.
   * The actual date will be adjusted by the dowChange field.
   */
  private final val month: Nothing = null
  /**
   * The day-of-month of the month-day of the cutover week.
   * If positive, it is the start of the week where the cutover can occur.
   * If negative, it represents the end of the week where cutover can occur.
   * The value is the number of days from the end of the month, such that
   * {@code -1} is the last day of the month, {@code -2} is the second
   * to last day, and so on.
   */
  private final val dom: Byte = 0
  /**
   * The cutover day-of-week, null to retain the day-of-month.
   */
  private final val dow: Nothing = null
  /**
   * The cutover time in the 'before' offset.
   */
  private final val time: Nothing = null
  /**
   * Whether the cutover time is midnight at the end of day.
   */
  private final val timeEndOfDay: Boolean = false
  /**
   * The definition of how the local time should be interpreted.
   */
  private final val timeDefinition: ZoneOffsetTransitionRule.TimeDefinition = null
  /**
   * The standard offset at the cutover.
   */
  private final val standardOffset: ZoneOffset = null
  /**
   * The offset before the cutover.
   */
  private final val offsetBefore: ZoneOffset = null
  /**
   * The offset after the cutover.
   */
  private final val offsetAfter: ZoneOffset = null
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
 * Copyright (c) 2009-2012, Stephen Colebourne & Michael Nascimento Santos
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
 * The rules defining how the zone offset varies for a single time-zone.
 * <p>
 * The rules model all the historic and future transitions for a time-zone.
 * {@link ZoneOffsetTransition} is used for known transitions, typically historic.
 * {@link ZoneOffsetTransitionRule} is used for future transitions that are based
 * on the result of an algorithm.
 * <p>
 * The rules are loaded via {@link ZoneRulesProvider} using a {@link ZoneId}.
 * The same rules may be shared internally between multiple zone IDs.
 * <p>
 * Serializing an instance of {@code ZoneRules} will store the entire set of rules.
 * It does not store the zone ID as it is not part of the state of this object.
 * <p>
 * A rule implementation may or may not store full information about historic
 * and future transitions, and the information stored is only as accurate as
 * that supplied to the implementation by the rules provider.
 * Applications should treat the data provided as representing the best information
 * available to the implementation of this rule.
 *
 * @implSpec
 * This class is immutable and thread-safe.
 *
 * @since 1.8
 */
object ZoneRules {
  /**
   * Obtains an instance of a ZoneRules.
   *
   * @param baseStandardOffset  the standard offset to use before legal rules were set, not null
   * @param baseWallOffset  the wall offset to use before legal rules were set, not null
   * @param standardOffsetTransitionList  the list of changes to the standard offset, not null
   * @param transitionList  the list of transitions, not null
   * @param lastRules  the recurring last rules, size 16 or less, not null
   * @return the zone rules, not null
   */
  def of(baseStandardOffset: ZoneOffset, baseWallOffset: ZoneOffset, standardOffsetTransitionList: Nothing, transitionList: Nothing, lastRules: Nothing): ZoneRules = {





     new ZoneRules(baseStandardOffset, baseWallOffset, standardOffsetTransitionList, transitionList, lastRules)
  }

  /**
   * Obtains an instance of ZoneRules that has fixed zone rules.
   *
   * @param offset  the offset this fixed zone rules is based on, not null
   * @return the zone rules, not null
   * @see #isFixedOffset()
   */
  def of(offset: ZoneOffset): ZoneRules = {

     new ZoneRules(offset)
  }

  /**
   * Reads the state from the stream.
   *
   * @param in  the input stream, not null
   * @return the created object, not null
   * @throws IOException if an error occurs
   */
  private[zone] def readExternal(in: DataInput): ZoneRules = {
    val stdSize: Int = in.readInt
    val stdTrans: Array[Long] = if ((stdSize == 0)) EMPTY_LONG_ARRAY else new Array[Long](stdSize)
    {
      var i: Int = 0
      while (i < stdSize) {
        {
          stdTrans(i) = Ser.readEpochSec(in)
        }
        ({
          i += 1; i - 1
        })
      }
    }
    val stdOffsets: Array[ZoneOffset] = new Array[ZoneOffset](stdSize + 1)
    {
      var i: Int = 0
      while (i < stdOffsets.length) {
        {
          stdOffsets(i) = Ser.readOffset(in)
        }
        ({
          i += 1; i - 1
        })
      }
    }
    val savSize: Int = in.readInt
    val savTrans: Array[Long] = if ((savSize == 0)) EMPTY_LONG_ARRAY else new Array[Long](savSize)
    {
      var i: Int = 0
      while (i < savSize) {
        {
          savTrans(i) = Ser.readEpochSec(in)
        }
        ({
          i += 1; i - 1
        })
      }
    }
    val savOffsets: Array[ZoneOffset] = new Array[ZoneOffset](savSize + 1)
    {
      var i: Int = 0
      while (i < savOffsets.length) {
        {
          savOffsets(i) = Ser.readOffset(in)
        }
        ({
          i += 1; i - 1
        })
      }
    }
    val ruleSize: Int = in.readByte
    val rules: Array[ZoneOffsetTransitionRule] = if ((ruleSize == 0)) EMPTY_LASTRULES else new Array[ZoneOffsetTransitionRule](ruleSize)
    {
      var i: Int = 0
      while (i < ruleSize) {
        {
          rules(i) = ZoneOffsetTransitionRule.readExternal(in)
        }
        ({
          i += 1; i - 1
        })
      }
    }
     new ZoneRules(stdTrans, stdOffsets, savTrans, savOffsets, rules)
  }

  /**
   * Serialization version.
   */
  private final val serialVersionUID: Long = 3044319355680032515L
  /**
   * The last year to have its transitions cached.
   */
  private final val LAST_CACHED_YEAR: Int = 2100
  /**
   * The zero-length long array.
   */
  private final val EMPTY_LONG_ARRAY: Array[Long] = new Array[Long](0)
  /**
   * The zero-length lastrules array.
   */
  private final val EMPTY_LASTRULES: Array[ZoneOffsetTransitionRule] = new Array[ZoneOffsetTransitionRule](0)
  /**
   * The zero-length ldt array.
   */
  private final val EMPTY_LDT_ARRAY: Array[Nothing] = new Array[Nothing](0)
}

final class ZoneRules extends Serializable {
  /**
   * Creates an instance.
   *
   * @param baseStandardOffset  the standard offset to use before legal rules were set, not null
   * @param baseWallOffset  the wall offset to use before legal rules were set, not null
   * @param standardOffsetTransitionList  the list of changes to the standard offset, not null
   * @param transitionList  the list of transitions, not null
   * @param lastRules  the recurring last rules, size 16 or less, not null
   */
  private[zone] def this(baseStandardOffset: ZoneOffset, baseWallOffset: ZoneOffset, standardOffsetTransitionList: Nothing, transitionList: Nothing, lastRules: Nothing) {

    `super`
    this.standardTransitions = new Array[Long](standardOffsetTransitionList.size)
    this.standardOffsets = new Array[ZoneOffset](standardOffsetTransitionList.size + 1)
    this.standardOffsets(0) = baseStandardOffset
    {
      var i: Int = 0
      while (i < standardOffsetTransitionList.size) {
        {
          this.standardTransitions(i) = standardOffsetTransitionList.get(i).toEpochSecond
          this.standardOffsets(i + 1) = standardOffsetTransitionList.get(i).getOffsetAfter
        }
        ({
          i += 1; i - 1
        })
      }
    }
    val localTransitionList: Nothing = new Nothing
    val localTransitionOffsetList: Nothing = new Nothing
    localTransitionOffsetList.add(baseWallOffset)
    import scala.collection.JavaConversions._
    for (trans <- transitionList) {
      if (trans.isGap) {
        localTransitionList.add(trans.getDateTimeBefore)
        localTransitionList.add(trans.getDateTimeAfter)
      }
      else {
        localTransitionList.add(trans.getDateTimeAfter)
        localTransitionList.add(trans.getDateTimeBefore)
      }
      localTransitionOffsetList.add(trans.getOffsetAfter)
    }
    this.savingsLocalTransitions = localTransitionList.toArray(new Array[Nothing](localTransitionList.size))
    this.wallOffsets = localTransitionOffsetList.toArray(new Array[ZoneOffset](localTransitionOffsetList.size))
    this.savingsInstantTransitions = new Array[Long](transitionList.size)
    {
      var i: Int = 0
      while (i < transitionList.size) {
        {
          this.savingsInstantTransitions(i) = transitionList.get(i).toEpochSecond
        }
        ({
          i += 1; i - 1
        })
      }
    }
    if (lastRules.size > 16) {
      throw new IllegalArgumentException("Too many transition rules")
    }
    this.lastRules = lastRules.toArray(new Array[ZoneOffsetTransitionRule](lastRules.size))
  }

  /**
   * Constructor.
   *
   * @param standardTransitions  the standard transitions, not null
   * @param standardOffsets  the standard offsets, not null
   * @param savingsInstantTransitions  the standard transitions, not null
   * @param wallOffsets  the wall offsets, not null
   * @param lastRules  the recurring last rules, size 15 or less, not null
   */
  private def this(standardTransitions: Array[Long], standardOffsets: Array[ZoneOffset], savingsInstantTransitions: Array[Long], wallOffsets: Array[ZoneOffset], lastRules: Array[ZoneOffsetTransitionRule]) {

    `super`
    this.standardTransitions = standardTransitions
    this.standardOffsets = standardOffsets
    this.savingsInstantTransitions = savingsInstantTransitions
    this.wallOffsets = wallOffsets
    this.lastRules = lastRules
    if (savingsInstantTransitions.length == 0) {
      this.savingsLocalTransitions = EMPTY_LDT_ARRAY
    }
    else {
      val localTransitionList: Nothing = new Nothing
      {
        var i: Int = 0
        while (i < savingsInstantTransitions.length) {
          {
            val before: ZoneOffset = wallOffsets(i)
            val after: ZoneOffset = wallOffsets(i + 1)
            val trans: ZoneOffsetTransition = new ZoneOffsetTransition(savingsInstantTransitions(i), before, after)
            if (trans.isGap) {
              localTransitionList.add(trans.getDateTimeBefore)
              localTransitionList.add(trans.getDateTimeAfter)
            }
            else {
              localTransitionList.add(trans.getDateTimeAfter)
              localTransitionList.add(trans.getDateTimeBefore)
            }
          }
          ({
            i += 1; i - 1
          })
        }
      }
      this.savingsLocalTransitions = localTransitionList.toArray(new Array[Nothing](localTransitionList.size))
    }
  }

  /**
   * Creates an instance of ZoneRules that has fixed zone rules.
   *
   * @param offset  the offset this fixed zone rules is based on, not null
   * @return the zone rules, not null
   * @see #isFixedOffset()
   */
  private def this(offset: ZoneOffset) {

    this.standardOffsets = new Array[ZoneOffset](1)
    this.standardOffsets(0) = offset
    this.standardTransitions = EMPTY_LONG_ARRAY
    this.savingsInstantTransitions = EMPTY_LONG_ARRAY
    this.savingsLocalTransitions = EMPTY_LDT_ARRAY
    this.wallOffsets = standardOffsets
    this.lastRules = EMPTY_LASTRULES
  }

  /**
   * Uses a serialization delegate.
   *
   * @return the replacing object, not null
   */
  private def writeReplace: AnyRef = {
     new Ser(Ser.ZRULES, this)
  }

  /**
   * Writes the state to the stream.
   *
   * @param out  the output stream, not null
   * @throws IOException if an error occurs
   */
  private[zone] def writeExternal(out: DataOutput) {
    out.writeInt(standardTransitions.length)
    for (trans <- standardTransitions) {
      Ser.writeEpochSec(trans, out)
    }
    for (offset <- standardOffsets) {
      Ser.writeOffset(offset, out)
    }
    out.writeInt(savingsInstantTransitions.length)
    for (trans <- savingsInstantTransitions) {
      Ser.writeEpochSec(trans, out)
    }
    for (offset <- wallOffsets) {
      Ser.writeOffset(offset, out)
    }
    out.writeByte(lastRules.length)
    for (rule <- lastRules) {
      rule.writeExternal(out)
    }
  }

  /**
   * Checks of the zone rules are fixed, such that the offset never varies.
   *
   * @return true if the time-zone is fixed and the offset never changes
   */
  def isFixedOffset: Boolean = {
     savingsInstantTransitions.length == 0
  }

  /**
   * Gets the offset applicable at the specified instant in these rules.
   * <p>
   * The mapping from an instant to an offset is simple, there is only
   * one valid offset for each instant.
   * This method returns that offset.
   *
   * @param instant  the instant to find the offset for, not null, but null
   *                 may be ignored if the rules have a single offset for all instants
   * @return the offset, not null
   */
  def getOffset(instant: Nothing): ZoneOffset = {
    if (savingsInstantTransitions.length == 0) {
       standardOffsets(0)
    }
    val epochSec: Long = instant.getEpochSecond
    if (lastRules.length > 0 && epochSec > savingsInstantTransitions(savingsInstantTransitions.length - 1)) {
      val year: Int = findYear(epochSec, wallOffsets(wallOffsets.length - 1))
      val transArray: Array[ZoneOffsetTransition] = findTransitionArray(year)
      var trans: ZoneOffsetTransition = null
      {
        var i: Int = 0
        while (i < transArray.length) {
          {
            trans = transArray(i)
            if (epochSec < trans.toEpochSecond) {
               trans.getOffsetBefore
            }
          }
          ({
            i += 1; i - 1
          })
        }
      }
       trans.getOffsetAfter
    }
    var index: Int = Arrays.binarySearch(savingsInstantTransitions, epochSec)
    if (index < 0) {
      index = -index - 2
    }
     wallOffsets(index + 1)
  }

  /**
   * Gets a suitable offset for the specified local date-time in these rules.
   * <p>
   * The mapping from a local date-time to an offset is not straightforward.
   * There are three cases:
   * <p><ul>
   * <li>Normal, with one valid offset. For the vast majority of the year, the normal
   * case applies, where there is a single valid offset for the local date-time.</li>
   * <li>Gap, with zero valid offsets. This is when clocks jump forward typically
   * due to the spring daylight savings change from "winter" to "summer".
   * In a gap there are local date-time values with no valid offset.</li>
   * <li>Overlap, with two valid offsets. This is when clocks are set back typically
   * due to the autumn daylight savings change from "summer" to "winter".
   * In an overlap there are local date-time values with two valid offsets.</li>
   * </ul><p>
   * Thus, for any given local date-time there can be zero, one or two valid offsets.
   * This method returns the single offset in the Normal case, and in the Gap or Overlap
   * case it returns the offset before the transition.
   * <p>
   * Since, in the case of Gap and Overlap, the offset returned is a "best" value, rather
   * than the "correct" value, it should be treated with care. Applications that care
   * about the correct offset should use a combination of this method,
   * {@link #getValidOffsets(DateTime)} and {@link #getTransition(DateTime)}.
   *
   * @param localDateTime  the local date-time to query, not null, but null
   *                       may be ignored if the rules have a single offset for all instants
   * @return the best available offset for the local date-time, not null
   */
  def getOffset(localDateTime: Nothing): ZoneOffset = {
    val info: AnyRef = getOffsetInfo(localDateTime)
    if (info.isInstanceOf[ZoneOffsetTransition]) {
       (info.asInstanceOf[ZoneOffsetTransition]).getOffsetBefore
    }
     info.asInstanceOf[ZoneOffset]
  }

  /**
   * Gets the offset applicable at the specified local date-time in these rules.
   * <p>
   * The mapping from a local date-time to an offset is not straightforward.
   * There are three cases:
   * <p><ul>
   * <li>Normal, with one valid offset. For the vast majority of the year, the normal
   * case applies, where there is a single valid offset for the local date-time.</li>
   * <li>Gap, with zero valid offsets. This is when clocks jump forward typically
   * due to the spring daylight savings change from "winter" to "summer".
   * In a gap there are local date-time values with no valid offset.</li>
   * <li>Overlap, with two valid offsets. This is when clocks are set back typically
   * due to the autumn daylight savings change from "summer" to "winter".
   * In an overlap there are local date-time values with two valid offsets.</li>
   * </ul><p>
   * Thus, for any given local date-time there can be zero, one or two valid offsets.
   * This method returns that list of valid offsets, which is a list of size 0, 1 or 2.
   * In the case where there are two offsets, the earlier offset is returned at index 0
   * and the later offset at index 1.
   * <p>
   * There are various ways to handle the conversion from a {@code DateTime}.
   * One technique, using this method, would be:
   * <pre>
   * List&lt;ZoneOffset&gt; validOffsets = rules.getOffset(localDT);
   * if (validOffsets.size() == 1) {
   * // Normal case: only one valid offset
   * zoneOffset = validOffsets.get(0);
   * } else {
   * // Gap or Overlap: determine what to do from transition (which will be non-null)
   * ZoneOffsetTransition trans = rules.getTransition(localDT);
   * }
   * </pre>
   * <p>
   * In theory, it is possible for there to be more than two valid offsets.
   * This would happen if clocks to be put back more than once in quick succession.
   * This has never happened in the history of time-zones and thus has no special handling.
   * However, if it were to happen, then the list would return more than 2 entries.
   *
   * @param localDateTime  the local date-time to query for valid offsets, not null, but null
   *                       may be ignored if the rules have a single offset for all instants
   * @return the list of valid offsets, may be immutable, not null
   */
  def getValidOffsets(localDateTime: Nothing): Nothing = {
    val info: AnyRef = getOffsetInfo(localDateTime)
    if (info.isInstanceOf[ZoneOffsetTransition]) {
       (info.asInstanceOf[ZoneOffsetTransition]).getValidOffsets
    }
     Collections.singletonList(info.asInstanceOf[ZoneOffset])
  }

  /**
   * Gets the offset transition applicable at the specified local date-time in these rules.
   * <p>
   * The mapping from a local date-time to an offset is not straightforward.
   * There are three cases:
   * <p><ul>
   * <li>Normal, with one valid offset. For the vast majority of the year, the normal
   * case applies, where there is a single valid offset for the local date-time.</li>
   * <li>Gap, with zero valid offsets. This is when clocks jump forward typically
   * due to the spring daylight savings change from "winter" to "summer".
   * In a gap there are local date-time values with no valid offset.</li>
   * <li>Overlap, with two valid offsets. This is when clocks are set back typically
   * due to the autumn daylight savings change from "summer" to "winter".
   * In an overlap there are local date-time values with two valid offsets.</li>
   * </ul><p>
   * A transition is used to model the cases of a Gap or Overlap.
   * The Normal case will return null.
   * <p>
   * There are various ways to handle the conversion from a {@code DateTime}.
   * One technique, using this method, would be:
   * <pre>
   * ZoneOffsetTransition trans = rules.getTransition(localDT);
   * if (trans == null) {
   * // Gap or Overlap: determine what to do from transition
   * } else {
   * // Normal case: only one valid offset
   * zoneOffset = rule.getOffset(localDT);
   * }
   * </pre>
   *
   * @param localDateTime  the local date-time to query for offset transition, not null, but null
   *                       may be ignored if the rules have a single offset for all instants
   * @return the offset transition, null if the local date-time is not in transition
   */
  def getTransition(localDateTime: Nothing): ZoneOffsetTransition = {
    val info: AnyRef = getOffsetInfo(localDateTime)
     (if (info.isInstanceOf[ZoneOffsetTransition]) info.asInstanceOf[ZoneOffsetTransition] else null)
  }

  private def getOffsetInfo(dt: Nothing): AnyRef = {
    if (savingsInstantTransitions.length == 0) {
       standardOffsets(0)
    }
    if (lastRules.length > 0 && dt.isAfter(savingsLocalTransitions(savingsLocalTransitions.length - 1))) {
      val transArray: Array[ZoneOffsetTransition] = findTransitionArray(dt.getYear)
      var info: AnyRef = null
      for (trans <- transArray) {
        info = findOffsetInfo(dt, trans)
        if (info.isInstanceOf[ZoneOffsetTransition] || (info == trans.getOffsetBefore)) {
           info
        }
      }
       info
    }
    var index: Int = Arrays.binarySearch(savingsLocalTransitions, dt)
    if (index == -1) {
       wallOffsets(0)
    }
    if (index < 0) {
      index = -index - 2
    }
    else if (index < savingsLocalTransitions.length - 1 && (savingsLocalTransitions(index) == savingsLocalTransitions(index + 1))) {
      index += 1
    }
    if ((index & 1) == 0) {
      val dtBefore: Nothing = savingsLocalTransitions(index)
      val dtAfter: Nothing = savingsLocalTransitions(index + 1)
      val offsetBefore: ZoneOffset = wallOffsets(index / 2)
      val offsetAfter: ZoneOffset = wallOffsets(index / 2 + 1)
      if (offsetAfter.getTotalSeconds > offsetBefore.getTotalSeconds) {
         new ZoneOffsetTransition(dtBefore, offsetBefore, offsetAfter)
      }
      else {
         new ZoneOffsetTransition(dtAfter, offsetBefore, offsetAfter)
      }
    }
    else {
       wallOffsets(index / 2 + 1)
    }
  }

  /**
   * Finds the offset info for a local date-time and transition.
   *
   * @param dt  the date-time, not null
   * @param trans  the transition, not null
   * @return the offset info, not null
   */
  private def findOffsetInfo(dt: Nothing, trans: ZoneOffsetTransition): AnyRef = {
    val localTransition: Nothing = trans.getDateTimeBefore
    if (trans.isGap) {
      if (dt.isBefore(localTransition)) {
         trans.getOffsetBefore
      }
      if (dt.isBefore(trans.getDateTimeAfter)) {
         trans
      }
      else {
         trans.getOffsetAfter
      }
    }
    else {
      if (dt.isBefore(localTransition) eq false) {
         trans.getOffsetAfter
      }
      if (dt.isBefore(trans.getDateTimeAfter)) {
         trans.getOffsetBefore
      }
      else {
         trans
      }
    }
  }

  /**
   * Finds the appropriate transition array for the given year.
   *
   * @param year  the year, not null
   * @return the transition array, not null
   */
  private def findTransitionArray(year: Int): Array[ZoneOffsetTransition] = {
    val yearObj: Integer = year
    var transArray: Array[ZoneOffsetTransition] = lastRulesCache.get(yearObj)
    if (transArray != null) {
       transArray
    }
    val ruleArray: Array[ZoneOffsetTransitionRule] = lastRules
    transArray = new Array[ZoneOffsetTransition](ruleArray.length)
    {
      var i: Int = 0
      while (i < ruleArray.length) {
        {
          transArray(i) = ruleArray(i).createTransition(year)
        }
        ({
          i += 1; i - 1
        })
      }
    }
    if (year < LAST_CACHED_YEAR) {
      lastRulesCache.putIfAbsent(yearObj, transArray)
    }
     transArray
  }

  /**
   * Gets the standard offset for the specified instant in this zone.
   * <p>
   * This provides access to historic information on how the standard offset
   * has changed over time.
   * The standard offset is the offset before any daylight saving time is applied.
   * This is typically the offset applicable during winter.
   *
   * @param instant  the instant to find the offset information for, not null, but null
   *                 may be ignored if the rules have a single offset for all instants
   * @return the standard offset, not null
   */
  def getStandardOffset(instant: Nothing): ZoneOffset = {
    if (savingsInstantTransitions.length == 0) {
       standardOffsets(0)
    }
    val epochSec: Long = instant.getEpochSecond
    var index: Int = Arrays.binarySearch(standardTransitions, epochSec)
    if (index < 0) {
      index = -index - 2
    }
     standardOffsets(index + 1)
  }

  /**
   * Gets the amount of daylight savings in use for the specified instant in this zone.
   * <p>
   * This provides access to historic information on how the amount of daylight
   * savings has changed over time.
   * This is the difference between the standard offset and the actual offset.
   * Typically the amount is zero during winter and one hour during summer.
   * Time-zones are second-based, so the nanosecond part of the duration will be zero.
   * <p>
   * This default implementation calculates the duration from the
   * {@link #getOffset(java.time.Instant) actual} and
   * {@link #getStandardOffset(java.time.Instant) standard} offsets.
   *
   * @param instant  the instant to find the daylight savings for, not null, but null
   *                 may be ignored if the rules have a single offset for all instants
   * @return the difference between the standard and actual offset, not null
   */
  def getDaylightSavings(instant: Nothing): Nothing = {
    if (savingsInstantTransitions.length == 0) {
       Duration.ZERO
    }
    val standardOffset: ZoneOffset = getStandardOffset(instant)
    val actualOffset: ZoneOffset = getOffset(instant)
     Duration.ofSeconds(actualOffset.getTotalSeconds - standardOffset.getTotalSeconds)
  }

  /**
   * Checks if the specified instant is in daylight savings.
   * <p>
   * This checks if the standard offset and the actual offset are the same
   * for the specified instant.
   * If they are not, it is assumed that daylight savings is in operation.
   * <p>
   * This default implementation compares the {@link #getOffset(java.time.Instant) actual}
   * and {@link #getStandardOffset(java.time.Instant) standard} offsets.
   *
   * @param instant  the instant to find the offset information for, not null, but null
   *                 may be ignored if the rules have a single offset for all instants
   * @return the standard offset, not null
   */
  def isDaylightSavings(instant: Nothing): Boolean = {
     ((getStandardOffset(instant) == getOffset(instant)) == false)
  }

  /**
   * Checks if the offset date-time is valid for these rules.
   * <p>
   * To be valid, the local date-time must not be in a gap and the offset
   * must match one of the valid offsets.
   * <p>
   * This default implementation checks if {@link #getValidOffsets(java.time.DateTime)}
   * contains the specified offset.
   *
   * @param localDateTime  the date-time to check, not null, but null
   *                       may be ignored if the rules have a single offset for all instants
   * @param offset  the offset to check, null returns false
   * @return true if the offset date-time is valid for these rules
   */
  def isValidOffset(localDateTime: Nothing, offset: ZoneOffset): Boolean = {
     getValidOffsets(localDateTime).contains(offset)
  }

  /**
   * Gets the next transition after the specified instant.
   * <p>
   * This returns details of the next transition after the specified instant.
   * For example, if the instant represents a point where "Summer" daylight savings time
   * applies, then the method will return the transition to the next "Winter" time.
   *
   * @param instant  the instant to get the next transition after, not null, but null
   *                 may be ignored if the rules have a single offset for all instants
   * @return the next transition after the specified instant, null if this is after the last transition
   */
  def nextTransition(instant: Nothing): ZoneOffsetTransition = {
    if (savingsInstantTransitions.length == 0) {
       null
    }
    val epochSec: Long = instant.getEpochSecond
    if (epochSec >= savingsInstantTransitions(savingsInstantTransitions.length - 1)) {
      if (lastRules.length == 0) {
         null
      }
      val year: Int = findYear(epochSec, wallOffsets(wallOffsets.length - 1))
      var transArray: Array[ZoneOffsetTransition] = findTransitionArray(year)
      for (trans <- transArray) {
        if (epochSec < trans.toEpochSecond) {
           trans
        }
      }
      if (year < Year.MAX_VALUE) {
        transArray = findTransitionArray(year + 1)
         transArray(0)
      }
       null
    }
    var index: Int = Arrays.binarySearch(savingsInstantTransitions, epochSec)
    if (index < 0) {
      index = -index - 1
    }
    else {
      index += 1
    }
     new ZoneOffsetTransition(savingsInstantTransitions(index), wallOffsets(index), wallOffsets(index + 1))
  }

  /**
   * Gets the previous transition before the specified instant.
   * <p>
   * This returns details of the previous transition after the specified instant.
   * For example, if the instant represents a point where "summer" daylight saving time
   * applies, then the method will return the transition from the previous "winter" time.
   *
   * @param instant  the instant to get the previous transition after, not null, but null
   *                 may be ignored if the rules have a single offset for all instants
   * @return the previous transition after the specified instant, null if this is before the first transition
   */
  def previousTransition(instant: Nothing): ZoneOffsetTransition = {
    if (savingsInstantTransitions.length == 0) {
       null
    }
    var epochSec: Long = instant.getEpochSecond
    if (instant.getNano > 0 && epochSec < Long.MAX_VALUE) {
      epochSec += 1
    }
    val lastHistoric: Long = savingsInstantTransitions(savingsInstantTransitions.length - 1)
    if (lastRules.length > 0 && epochSec > lastHistoric) {
      val lastHistoricOffset: ZoneOffset = wallOffsets(wallOffsets.length - 1)
      var year: Int = findYear(epochSec, lastHistoricOffset)
      var transArray: Array[ZoneOffsetTransition] = findTransitionArray(year)
      {
        var i: Int = transArray.length - 1
        while (i >= 0) {
          {
            if (epochSec > transArray(i).toEpochSecond) {
               transArray(i)
            }
          }
          ({
            i -= 1; i + 1
          })
        }
      }
      val lastHistoricYear: Int = findYear(lastHistoric, lastHistoricOffset)
      if (({
        year -= 1; year
      }) > lastHistoricYear) {
        transArray = findTransitionArray(year)
         transArray(transArray.length - 1)
      }
    }
    var index: Int = Arrays.binarySearch(savingsInstantTransitions, epochSec)
    if (index < 0) {
      index = -index - 1
    }
    if (index <= 0) {
       null
    }
     new ZoneOffsetTransition(savingsInstantTransitions(index - 1), wallOffsets(index - 1), wallOffsets(index))
  }

  private def findYear(epochSecond: Long, offset: ZoneOffset): Int = {
    val localSecond: Long = epochSecond + offset.getTotalSeconds
    val localEpochDay: Long = Math.floorDiv(localSecond, 86400)
     LocalDate.ofEpochDay(localEpochDay).getYear
  }

  /**
   * Gets the complete list of fully defined transitions.
   * <p>
   * The complete set of transitions for this rules instance is defined by this method
   * and {@link #getTransitionRules()}. This method returns those transitions that have
   * been fully defined. These are typically historical, but may be in the future.
   * <p>
   * The list will be empty for fixed offset rules and for any time-zone where there has
   * only ever been a single offset. The list will also be empty if the transition rules are unknown.
   *
   * @return an immutable list of fully defined transitions, not null
   */
  def getTransitions: Nothing = {
    val list: Nothing = new Nothing
    {
      var i: Int = 0
      while (i < savingsInstantTransitions.length) {
        {
          list.add(new ZoneOffsetTransition(savingsInstantTransitions(i), wallOffsets(i), wallOffsets(i + 1)))
        }
        ({
          i += 1; i - 1
        })
      }
    }
     Collections.unmodifiableList(list)
  }

  /**
   * Gets the list of transition rules for years beyond those defined in the transition list.
   * <p>
   * The complete set of transitions for this rules instance is defined by this method
   * and {@link #getTransitions()}. This method returns instances of {@link ZoneOffsetTransitionRule}
   * that define an algorithm for when transitions will occur.
   * <p>
   * For any given {@code ZoneRules}, this list contains the transition rules for years
   * beyond those years that have been fully defined. These rules typically refer to future
   * daylight saving time rule changes.
   * <p>
   * If the zone defines daylight savings into the future, then the list will normally
   * be of size two and hold information about entering and exiting daylight savings.
   * If the zone does not have daylight savings, or information about future changes
   * is uncertain, then the list will be empty.
   * <p>
   * The list will be empty for fixed offset rules and for any time-zone where there is no
   * daylight saving time. The list will also be empty if the transition rules are unknown.
   *
   * @return an immutable list of transition rules, not null
   */
  def getTransitionRules: Nothing = {
     Collections.unmodifiableList(Arrays.asList(lastRules))
  }

  /**
   * Checks if this set of rules equals another.
   * <p>
   * Two rule sets are equal if they will always result in the same output
   * for any given input instant or local date-time.
   * Rules from two different groups may return false even if they are in fact the same.
   * <p>
   * This definition should result in implementations comparing their entire state.
   *
   * @param otherRules  the other rules, null returns false
   * @return true if this rules is the same as that specified
   */
  override def equals(otherRules: AnyRef): Boolean = {
    if (this eq otherRules) {
       true
    }
    if (otherRules.isInstanceOf[ZoneRules]) {
      val other: ZoneRules = otherRules.asInstanceOf[ZoneRules]
       Arrays.equals(standardTransitions, other.standardTransitions) && Arrays.equals(standardOffsets, other.standardOffsets) && Arrays.equals(savingsInstantTransitions, other.savingsInstantTransitions) && Arrays.equals(wallOffsets, other.wallOffsets) && Arrays.equals(lastRules, other.lastRules)
    }
     false
  }

  /**
   * Returns a suitable hash code given the definition of {@code #equals}.
   *
   * @return the hash code
   */
  override def hashCode: Int = {
     Arrays.hashCode(standardTransitions) ^ Arrays.hashCode(standardOffsets) ^ Arrays.hashCode(savingsInstantTransitions) ^ Arrays.hashCode(wallOffsets) ^ Arrays.hashCode(lastRules)
  }

  /**
   * Returns a string describing this object.
   *
   * @return a string for debugging, not null
   */
  override def toString: String = {
     "ZoneRules[currentStandardOffset=" + standardOffsets(standardOffsets.length - 1) + "]"
  }

  /**
   * The transitions between standard offsets (epoch seconds), sorted.
   */
  private final val standardTransitions: Array[Long] = null
  /**
   * The standard offsets.
   */
  private final val standardOffsets: Array[ZoneOffset] = null
  /**
   * The transitions between instants (epoch seconds), sorted.
   */
  private final val savingsInstantTransitions: Array[Long] = null
  /**
   * The transitions between local date-times, sorted.
   * This is a paired array, where the first entry is the start of the transition
   * and the second entry is the end of the transition.
   */
  private final val savingsLocalTransitions: Array[Nothing] = null
  /**
   * The wall offsets.
   */
  private final val wallOffsets: Array[ZoneOffset] = null
  /**
   * The last rule.
   */
  private final val lastRules: Array[ZoneOffsetTransitionRule] = null
  /**
   * The map of recent transitions.
   */
  private final val lastRulesCache: Nothing = new Nothing
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
 * Copyright (c) 2008-2012, Stephen Colebourne & Michael Nascimento Santos
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
 * Thrown to indicate a problem with time-zone configuration.
 * <p>
 * This exception is used to indicate a problems with the configured
 * time-zone rules.
 *
 * @implSpec
 * This class is intended for use in a single thread.
 *
 * @since 1.8
 */
object ZoneRulesException {

}

class ZoneRulesException extends DateTimeException {
  /**
   * Constructs a new date-time exception with the specified message.
   *
   * @param message  the message to use for this exception, may be null
   */
  def this(message: String) {

    `super`(message)
  }

  /**
   * Constructs a new date-time exception with the specified message and cause.
   *
   * @param message  the message to use for this exception, may be null
   * @param cause  the cause of the exception, may be null
   */
  def this(message: String, cause: Throwable) {

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
 * Copyright (c) 2009-2012, Stephen Colebourne & Michael Nascimento Santos
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
 * Provider of time-zone rules to the system.
 * <p>
 * This class manages the configuration of time-zone rules.
 * The static methods provide the public API that can be used to manage the providers.
 * The abstract methods provide the SPI that allows rules to be provided.
 * <p>
 * ZoneRulesProvider may be installed in an instance of the Java Platform as
 * extension classes, that is, jar files placed into any of the usual extension
 * directories. Installed providers are loaded using the service-provider loading
 * facility defined by the {@link ServiceLoader} class. A ZoneRulesProvider
 * identifies itself with a provider configuration file named
 * {@code java.time.zone.ZoneRulesProvider} in the resource directory
 * {@code META-INF/services}. The file should contain a line that specifies the
 * fully qualified concrete zonerules-provider class name.
 * Providers may also be made available by adding them to the class path or by
 * registering themselves via {@link #registerProvider} method.
 * <p>
 * The Java virtual machine has a default provider that provides zone rules
 * for the time-zones defined by IANA Time Zone Database (TZDB). If the system
 * property {@code java.time.zone.DefaultZoneRulesProvider} is defined then
 * it is taken to be the fully-qualified name of a concrete ZoneRulesProvider
 * class to be loaded as the default provider, using the system class loader.
 * If this system property is not defined, a system-default provider will be
 * loaded to serve as the default provider.
 * <p>
 * Rules are looked up primarily by zone ID, as used by {@link ZoneId}.
 * Only zone region IDs may be used, zone offset IDs are not used here.
 * <p>
 * Time-zone rules are political, thus the data can change at any time.
 * Each provider will provide the latest rules for each zone ID, but they
 * may also provide the history of how the rules changed.
 *
 * @implSpec
 * This interface is a service provider that can be called by multiple threads.
 * Implementations must be immutable and thread-safe.
 * <p>
 * Providers must ensure that once a rule has been seen by the application, the
 * rule must continue to be available.
 * <p>
 * Providers are encouraged to implement a meaningful { @code toString} method.
 *                                                            <p>
 *                                                            Many systems would like to update time-zone rules dynamically without stopping the JVM.
 *                                                            When examined in detail, this is a complex problem.
 *                                                            Providers may choose to handle dynamic updates, however the default provider does not.
 *
 * @since 1.8
 */
object ZoneRulesProvider {
  /**
   * Gets the set of available zone IDs.
   * <p>
   * These IDs are the string form of a {@link ZoneId}.
   *
   * @return a modifiable copy of the set of zone IDs, not null
   */
  def getAvailableZoneIds: Nothing = {
     new Nothing(ZONES.keySet)
  }

  /**
   * Gets the rules for the zone ID.
   * <p>
   * This returns the latest available rules for the zone ID.
   * <p>
   * This method relies on time-zone data provider files that are configured.
   * These are loaded using a {@code ServiceLoader}.
   * <p>
   * The caching flag is designed to allow provider implementations to
   * prevent the rules being cached in {@code ZoneId}.
   * Under normal circumstances, the caching of zone rules is highly desirable
   * as it will provide greater performance. However, there is a use case where
   * the caching would not be desirable, see {@link #provideRules}.
   *
   * @param zoneId the zone ID as defined by { @code ZoneId}, not null
   * @param forCaching whether the rules are being queried for caching,
   *                   true if the returned rules will be cached by { @code ZoneId},
   *                                                                        false if they will be returned to the user without being cached in { @code ZoneId}
   * @return the rules, null if { @code forCaching} is true and this
   *                                    is a dynamic provider that wants to prevent caching in { @code ZoneId},
   *                                                                                                   otherwise not null
   * @throws ZoneRulesException if rules cannot be obtained for the zone ID
   */
  def getRules(zoneId: String, forCaching: Boolean): ZoneRules = {

     getProvider(zoneId).provideRules(zoneId, forCaching)
  }

  /**
   * Gets the history of rules for the zone ID.
   * <p>
   * Time-zones are defined by governments and change frequently.
   * This method allows applications to find the history of changes to the
   * rules for a single zone ID. The map is keyed by a string, which is the
   * version string associated with the rules.
   * <p>
   * The exact meaning and format of the version is provider specific.
   * The version must follow lexicographical order, thus the returned map will
   * be order from the oldest known rules to the newest available rules.
   * The default 'TZDB' group uses version numbering consisting of the year
   * followed by a letter, such as '2009e' or '2012f'.
   * <p>
   * Implementations must provide a result for each valid zone ID, however
   * they do not have to provide a history of rules.
   * Thus the map will always contain one element, and will only contain more
   * than one element if historical rule information is available.
   *
   * @param zoneId  the zone ID as defined by { @code ZoneId}, not null
   * @return a modifiable copy of the history of the rules for the ID, sorted
   *         from oldest to newest, not null
   * @throws ZoneRulesException if history cannot be obtained for the zone ID
   */
  def getVersions(zoneId: String): Nothing = {

     getProvider(zoneId).provideVersions(zoneId)
  }

  /**
   * Gets the provider for the zone ID.
   *
   * @param zoneId  the zone ID as defined by { @code ZoneId}, not null
   * @return the provider, not null
   * @throws ZoneRulesException if the zone ID is unknown
   */
  private def getProvider(zoneId: String): ZoneRulesProvider = {
    val provider: ZoneRulesProvider = ZONES.get(zoneId)
    if (provider == null) {
      if (ZONES.isEmpty) {
        throw new ZoneRulesException("No time-zone data files registered")
      }
      throw new ZoneRulesException("Unknown time-zone ID: " + zoneId)
    }
     provider
  }

  /**
   * Registers a zone rules provider.
   * <p>
   * This adds a new provider to those currently available.
   * A provider supplies rules for one or more zone IDs.
   * A provider cannot be registered if it supplies a zone ID that has already been
   * registered. See the notes on time-zone IDs in {@link ZoneId}, especially
   * the section on using the concept of a "group" to make IDs unique.
   * <p>
   * To ensure the integrity of time-zones already created, there is no way
   * to deregister providers.
   *
   * @param provider  the provider to register, not null
   * @throws ZoneRulesException if a zone ID is already registered
   */
  def registerProvider(provider: ZoneRulesProvider) {

    registerProvider0(provider)
    PROVIDERS.add(provider)
  }

  /**
   * Registers the provider.
   *
   * @param provider  the provider to register, not null
   * @throws ZoneRulesException if unable to complete the registration
   */
  private def registerProvider0(provider: ZoneRulesProvider) {
    import scala.collection.JavaConversions._
    for (zoneId <- provider.provideZoneIds) {

      val old: ZoneRulesProvider = ZONES.putIfAbsent(zoneId, provider)
      if (old != null) {
        throw new ZoneRulesException("Unable to register zone as one already registered with that ID: " + zoneId + ", currently loading from provider: " + provider)
      }
    }
  }

  /**
   * Refreshes the rules from the underlying data provider.
   * <p>
   * This method allows an application to request that the providers check
   * for any updates to the provided rules.
   * After calling this method, the offset stored in any {@link ZonedDateTime}
   * may be invalid for the zone ID.
   * <p>
   * Dynamic update of rules is a complex problem and most applications
   * should not use this method or dynamic rules.
   * To achieve dynamic rules, a provider implementation will have to be written
   * as per the specification of this class.
   * In addition, instances of {@code ZoneRules} must not be cached in the
   * application as they will become stale. However, the boolean flag on
   * {@link #provideRules(String, boolean)} allows provider implementations
   * to control the caching of {@code ZoneId}, potentially ensuring that
   * all objects in the system see the new rules.
   * Note that there is likely to be a cost in performance of a dynamic rules
   * provider. Note also that no dynamic rules provider is in this specification.
   *
   * @return true if the rules were updated
   * @throws ZoneRulesException if an error occurs during the refresh
   */
  def refresh: Boolean = {
    var changed: Boolean = false
    import scala.collection.JavaConversions._
    for (provider <- PROVIDERS) {
      changed |= provider.provideRefresh
    }
     changed
  }

  /**
   * The set of loaded providers.
   */
  private final val PROVIDERS: Nothing = new Nothing
  /**
   * The lookup from zone ID to provider.
   */
  private final val ZONES: Nothing = new Nothing(512, 0.75f, 2)
}

abstract class ZoneRulesProvider {
  /**
   * Constructor.
   */
  protected def  {

  }

  /**
   * SPI method to get the available zone IDs.
   * <p>
   * This obtains the IDs that this {@code ZoneRulesProvider} provides.
   * A provider should provide data for at least one zone ID.
   * <p>
   * The returned zone IDs remain available and valid for the lifetime of the application.
   * A dynamic provider may increase the set of IDs as more data becomes available.
   *
   * @return the set of zone IDs being provided, not null
   * @throws ZoneRulesException if a problem occurs while providing the IDs
   */
  protected def provideZoneIds: Nothing

  /**
   * SPI method to get the rules for the zone ID.
   * <p>
   * This loads the rules for the specified zone ID.
   * The provider implementation must validate that the zone ID is valid and
   * available, throwing a {@code ZoneRulesException} if it is not.
   * The result of the method in the valid case depends on the caching flag.
   * <p>
   * If the provider implementation is not dynamic, then the result of the
   * method must be the non-null set of rules selected by the ID.
   * <p>
   * If the provider implementation is dynamic, then the flag gives the option
   * of preventing the returned rules from being cached in {@link ZoneId}.
   * When the flag is true, the provider is permitted to return null, where
   * null will prevent the rules from being cached in {@code ZoneId}.
   * When the flag is false, the provider must return non-null rules.
   *
   * @param zoneId the zone ID as defined by { @code ZoneId}, not null
   * @param forCaching whether the rules are being queried for caching,
   *                   true if the returned rules will be cached by { @code ZoneId},
   *                                                                        false if they will be returned to the user without being cached in { @code ZoneId}
   * @return the rules, null if { @code forCaching} is true and this
   *                                    is a dynamic provider that wants to prevent caching in { @code ZoneId},
   *                                                                                                   otherwise not null
   * @throws ZoneRulesException if rules cannot be obtained for the zone ID
   */
  protected def provideRules(zoneId: String, forCaching: Boolean): ZoneRules

  /**
   * SPI method to get the history of rules for the zone ID.
   * <p>
   * This returns a map of historical rules keyed by a version string.
   * The exact meaning and format of the version is provider specific.
   * The version must follow lexicographical order, thus the returned map will
   * be order from the oldest known rules to the newest available rules.
   * The default 'TZDB' group uses version numbering consisting of the year
   * followed by a letter, such as '2009e' or '2012f'.
   * <p>
   * Implementations must provide a result for each valid zone ID, however
   * they do not have to provide a history of rules.
   * Thus the map will contain at least one element, and will only contain
   * more than one element if historical rule information is available.
   * <p>
   * The returned versions remain available and valid for the lifetime of the application.
   * A dynamic provider may increase the set of versions as more data becomes available.
   *
   * @param zoneId  the zone ID as defined by { @code ZoneId}, not null
   * @return a modifiable copy of the history of the rules for the ID, sorted
   *         from oldest to newest, not null
   * @throws ZoneRulesException if history cannot be obtained for the zone ID
   */
  protected def provideVersions(zoneId: String): Nothing

  /**
   * SPI method to refresh the rules from the underlying data provider.
   * <p>
   * This method provides the opportunity for a provider to dynamically
   * recheck the underlying data provider to find the latest rules.
   * This could be used to load new rules without stopping the JVM.
   * Dynamic behavior is entirely optional and most providers do not support it.
   * <p>
   * This implementation returns false.
   *
   * @return true if the rules were updated
   * @throws ZoneRulesException if an error occurs during the refresh
   */
  protected def provideRefresh: Boolean = {
     false
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
 * <p>
 * Support for time-zones and their rules.
 * </p>
 * <p>
 * Daylight Saving Time and Time-Zones are concepts used by Governments to alter local time.
 * This package provides support for time-zones, their rules and the resulting
 * gaps and overlaps in the local time-line typically caused by Daylight Saving Time.
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




