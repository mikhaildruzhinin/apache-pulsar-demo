package demo

import com.sksamuel.pulsar4s.circe.circeSchema
import com.sksamuel.pulsar4s.{DefaultProducerMessage, EventTime, ProducerConfig, PulsarAsyncClient, PulsarClient, Topic}
import com.typesafe.config.{Config, ConfigFactory}
import io.circe.generic.auto._

import java.util.concurrent.{ExecutorService, Executors}
import scala.concurrent.ExecutionContext

object PulsarProducer {
  private val config: Config = ConfigFactory.load()
  private val pulsarUrl: String = config.getString("pulsar.url")
  private val pulsarTopicName: String = config.getString("pulsar.topic_name")
  private val producerName: String = config.getString("pulsar.producer_name")

  private val executor: ExecutorService = Executors.newFixedThreadPool(4)
  private implicit val ec: ExecutionContext = ExecutionContext.fromExecutorService(executor)

  private val pulsarClient: PulsarAsyncClient = PulsarClient(pulsarUrl)
  private val topic: Topic = Topic(pulsarTopicName)
  private val eventProducer = pulsarClient.producer[SensorEvent](
    ProducerConfig(
      topic = topic,
      producerName = Some(producerName),
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
