package hiddenpitch.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AudienceTest {
 @Test fun valuesRoundTripAndAreUnique() {
  assertEquals(Audience.entries.size, Audience.entries.map { it.dbValue }.toSet().size)
  Audience.entries.forEach { assertEquals(it, Audience.fromDb(it.dbValue)) }
  assertNull(Audience.fromDb("unknown"))
 }
}
