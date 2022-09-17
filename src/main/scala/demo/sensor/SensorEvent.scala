package demo.sensor

case class SensorEvent(sensorId: String,
                       status: String,
                       startupTime: Long,
                       eventTime: Long,
                       reading: Double)
