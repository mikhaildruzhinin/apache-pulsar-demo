package demo.pulsar

import com.sksamuel.pulsar4s.circe.circeSchema
import com.sksamuel.pulsar4s.{Consumer, ConsumerConfig, Subscription}
import demo.sensor.SensorEvent
import io.circe.generic.auto._
import org.apache.pulsar.client.api.{SubscriptionInitialPosition, SubscriptionType}

import scala.annotation.tailrec
import scala.util.{Failure, Success}

object PulsarConsumer extends BasePulsarApp {
  private val subscriptionName: String = config.getString("pulsar.subscription_name")
  private val consumerName: String = config.getString("pulsar.consumer_name")

  private val consumerConfig: ConsumerConfig = ConsumerConfig(
    subscriptionName = Subscription(subscriptionName),
    topics = Seq(topic),
    consumerName = Some(consumerName),
    subscriptionInitialPosition = Some(SubscriptionInitialPosition.Earliest),
    subscriptionType = Some(SubscriptionType.Exclusive)
  )

  private val consumer: Consumer[SensorEvent] = pulsarClient.consumer[SensorEvent](consumerConfig)

  @tailrec
  private def receiveAll(totalMessageCount: Int = 0): Unit = {
    consumer.receive match {
      case Success(message) =>
        println(s"Total messages: $totalMessageCount - Acked message ${message.messageId} - ${message.value}")
        consumer.acknowledge(message.messageId)
        receiveAll(totalMessageCount + 1)
      case Failure(exception) =>
        println(s"Failed to receive message ${exception.getMessage}")
    }
  }

  def main(args: Array[String]): Unit = {
    receiveAll()
  }
}
