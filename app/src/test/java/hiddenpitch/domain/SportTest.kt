package hiddenpitch.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SportTest {
 @Test fun valuesRoundTripAndAreUnique() {
  assertEquals(Sport.entries.size, Sport.entries.map { it.dbValue }.toSet().size)
  Sport.entries.forEach { assertEquals(it, Sport.fromDb(it.dbValue)) }
  assertNull(Sport.fromDb("unknown"))
 }
}
