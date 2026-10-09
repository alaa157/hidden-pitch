package hiddenpitch.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PlaceStatusTest {
 @Test fun valuesRoundTripAndAreUnique() {
  assertEquals(PlaceStatus.entries.size, PlaceStatus.entries.map { it.dbValue }.toSet().size)
  PlaceStatus.entries.forEach { assertEquals(it, PlaceStatus.fromDb(it.dbValue)) }
  assertNull(PlaceStatus.fromDb("unknown"))
 }
}
