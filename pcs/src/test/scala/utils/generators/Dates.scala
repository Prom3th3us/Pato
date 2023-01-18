package utils.generators

import java.time.LocalDateTime

object Dates {

  def now = LocalDateTime.now

  def yesterday = now.minusYears(1)

  def today = now

  def tomorrow = now.plusYears(1)
}
