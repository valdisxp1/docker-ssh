import com.dimafeng.testcontainers.{ForAllTestContainer, GenericContainer}
import org.junit.runner.Description
import org.scalatest.{FlatSpec, Matchers}
import org.testcontainers.containers.wait.{LogMessageWaitStrategy, Wait}

import scala.collection.JavaConverters._
import sys.process._

class SSHDSpec extends FlatSpec with Matchers {
  implicit private val suiteDescription = Description.createSuiteDescription(this.getClass)

  "user" should "be able to login with a fixed public key" in {
    val key = SSH.genKey()
    val container = SSHD("valdisxp1/sshd-socat:latest").authorizedKeys(key.publicKey)
    container.starting()
    Thread.sleep(1000)
    val ip = container.container.getContainerInfo.getNetworkSettings.getNetworks.asScala("bridge").getIpAddress
    SSH.connect(s"root@$ip",key = key)("echo","$HOSTNAME").trim shouldBe container.containerId
  }
}

case class SSHD(imageName: String) {
  val waitStrategy = new LogMessageWaitStrategy().withRegEx("Authorized keys")
  def authorizedKeys(keys: String*) = GenericContainer(imageName, waitStrategy = waitStrategy,
      env = Map("AUTHORIZED_KEYS" -> keys.mkString(" "))
    )
  def authorizedKeysUrl(url: String) = GenericContainer(imageName, waitStrategy = waitStrategy,
    env = Map("AUTHORIZED_KEYS_URL" -> url)
  )
}

object SSH {

  case class Key(file: String, publicKey: String)

  def genKey(filename: String = "id_rsa") = {
    Seq("ssh-keygen", "-N", "", "-b", "2048", "-t", "rsa", "-f", filename).!!
    Key(filename, Seq("cat", filename + ".pub").!!)
  }

  def connect(host: String, port: Int=22, key: Key)(command: String*): String = {
    (Seq("ssh",
      "-o", "StrictHostKeyChecking=no",
      "-p", port.toString,
      "-i", key.file,
      host) ++ command.toSeq).!!(ProcessLogger.apply(println(_)))
  }
}
