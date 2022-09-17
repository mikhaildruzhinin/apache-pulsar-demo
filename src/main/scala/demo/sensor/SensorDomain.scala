package demo.sensor

import java.lang.management.{ManagementFactory, OperatingSystemMXBean}
import java.lang.reflect.Method
import java.net.InetAddress

object SensorDomain {
  private val startupTime: Long = System.currentTimeMillis()

  private val sensorId = InetAddress.getLocalHost.getHostName

  private val operatingSystemMXBean: OperatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean

  private def getReading(methodName: String): String = {
    val method: Method = operatingSystemMXBean.getClass.getDeclaredMethod(methodName)
    method.setAccessible(true)
    try {
      method.invoke(operatingSystemMXBean).toString
    } catch {
      case _: Exception => "-1"
    }
  }

  def generate(): Iterator[SensorEvent] = {
    Thread.sleep(1000)

    val sensorEvent = SensorEvent(
      sensorId = sensorId,
      startupTime = startupTime,
      eventTime = System.currentTimeMillis(),
      freePhysicalMemorySize = getReading("getFreePhysicalMemorySize"),
      freeSwapSpaceSize = getReading("getFreeSwapSpaceSize"),
      systemCpuLoad = getReading("getSystemCpuLoad")
    )

    Iterator.single(sensorEvent) ++ generate()
  }
}
