package hiddenpitch.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SurfaceTest {
 @Test fun valuesRoundTripAndAreUnique() {
  assertEquals(Surface.entries.size, Surface.entries.map { it.dbValue }.toSet().size)
  Surface.entries.forEach { assertEquals(it, Surface.fromDb(it.dbValue)) }
  assertNull(Surface.fromDb("unknown"))
 }
}
