package demo.sensor

case class SensorEvent(sensorId: String,
                       startupTime: Long,
                       eventTime: Long,
                       freePhysicalMemorySize: String,
                       freeSwapSpaceSize: String,
                       systemCpuLoad: String)
