package demo.sensor

import java.util.UUID
import scala.util.Random

object SensorDomain {
  private val startupTime: Long = System.currentTimeMillis()

  private val sensorIds: List[String] = (1 to 10).map(_ => UUID.randomUUID().toString).toList

  private val offSensors: Set[String] = sensorIds.toSet
  private val onSensors: Set[String] = Set[String]()

  private def generateRandomReading(): Double = {
    BigDecimal(40 + Random.nextGaussian()).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
  }

  // Stopped -> Starting -> Running
  def generate(ids: List[String] = sensorIds,
               offSensors: Set[String] = offSensors,
               onSensors: Set[String] = onSensors): Iterator[SensorEvent] = {

    Thread.sleep(Random.nextInt(500) + 200)

    val index = Random.nextInt(sensorIds.size)
    val sensorId = sensorIds(index)
    val reading = if (offSensors.contains(sensorId)) {
      println(s"Starting sensor ${index + 1}")
      SensorEvent(sensorId, "Starting", startupTime, System.currentTimeMillis(), 0.0)
    } else {
      val temperature = generateRandomReading()
      SensorEvent(sensorId, "Running", startupTime, System.currentTimeMillis(), temperature)
    }

    Iterator.single(reading) ++ generate(ids, offSensors - sensorId, onSensors + sensorId)
  }
}
