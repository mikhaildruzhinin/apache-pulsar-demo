package demo.pulsar

import com.sksamuel.pulsar4s.circe.circeSchema
import com.sksamuel.pulsar4s.{DefaultProducerMessage, EventTime, ProducerConfig}
import demo.sensor.{SensorDomain, SensorEvent}
import io.circe.generic.auto._

import java.util.concurrent.{ExecutorService, Executors}
import scala.concurrent.ExecutionContext

object PulsarProducer extends BasePulsarApp {
  private val producerName: String = config.getString("pulsar.producer_name")

  private val executor: ExecutorService = Executors.newFixedThreadPool(4)
  private implicit val ec: ExecutionContext = ExecutionContext.fromExecutorService(executor)

  private val producerConfig: ProducerConfig = ProducerConfig(
    topic = topic,
    producerName = Some(producerName),
    enableBatching = Some(true),
    blockIfQueueFull = Some(true)
  )
  private val eventProducer = pulsarClient.producer[SensorEvent](producerConfig)

  def main(args: Array[String]): Unit = {
    SensorDomain.generate().take(100).foreach { sensorEvent =>
      val message = DefaultProducerMessage(
        key = Some(sensorEvent.sensorId),
        value = sensorEvent,
        eventTime = Some(EventTime(sensorEvent.eventTime))
      )
      eventProducer.sendAsync(message)
    }
  }
}
