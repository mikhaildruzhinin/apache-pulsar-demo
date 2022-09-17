package demo.pulsar

import com.sksamuel.pulsar4s.{PulsarAsyncClient, PulsarClient, Topic}
import com.typesafe.config.{Config, ConfigFactory}

trait BasePulsarApp {
  protected val config: Config = ConfigFactory.load()
  protected val pulsarUrl: String = config.getString("pulsar.url")
  protected val pulsarTopicName: String = config.getString("pulsar.topic_name")

  protected val pulsarClient: PulsarAsyncClient = PulsarClient(pulsarUrl)
  protected val topic: Topic = Topic(pulsarTopicName)
}
