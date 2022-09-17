package demo

import com.sksamuel.pulsar4s.circe.circeSchema
import com.sksamuel.pulsar4s.{DefaultProducerMessage, EventTime, FutureAsyncHandler, ProducerConfig, PulsarAsyncClient, PulsarClient, Topic}
import io.circe.generic.auto._

import java.util.concurrent.{ExecutorService, Executors}
import scala.concurrent.ExecutionContext

object PulsarProducer {
  private val executor: ExecutorService = Executors.newFixedThreadPool(4)
  private implicit val ec: ExecutionContext = ExecutionContext.fromExecutorService(executor)

  private val pulsarClient: PulsarAsyncClient = PulsarClient("pulsar://localhost:6650")
  private val topic: Topic = Topic("sensor-events")
  private val eventProducer = pulsarClient.producer[SensorEvent](
    ProducerConfig(
      topic = topic,
      producerName = Some("sensor-producer"),
      enableBatching = Some(true),
      blockIfQueueFull = Some(true)
    )
  )

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
