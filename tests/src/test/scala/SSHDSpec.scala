import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.Await
import sys.process._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class SSHDSpec extends FlatSpec with Matchers {

  val docker = tugboat.Docker("unix:///var/run/docker2.sock")


  "user" should "be able to login with a fixed public key" in {
    val key = SSH.genKey()
    val containerId = Await.result(SSHD("valdisxp1/sshd-socat:latest").authorizedKeys(key.publicKey),1.second)
    Thread.sleep(1000)
    val ip = docker.containers.get(containerId)().map(_.networkSettings.ipAddr)
    SSH.connect(s"root@$ip",key = key)("echo","$HOSTNAME").trim shouldBe containerId
  }
}

case class SSHD(imageName: String) {
  val docker = tugboat.Docker("unix:///var/run/docker2.sock")
  def authorizedKeys(keys: String*) = {
    container("AUTHORIZED_KEYS" -> keys.mkString(" "))
  }

  private def container(config: (String, String)) = {
    docker.containers.create(imageName)
      .env(config)()
      .flatMap {
        container =>
          docker.containers.get(container.id).start().map(_ => container.id)
      }
  }

  def authorizedKeysUrl(url: String) = {
    container("AUTHORIZED_KEYS_URL" -> url)
  }
}

object SSH {

  case class Key(file: String, publicKey: String)

  def genKey(filename: String = "id_rsa") = {
    Seq("rm","-v","id_rsa","id_rsa.pub").!
    Seq("ssh-keygen", "-N", "", "-b", "2048", "-t", "rsa", "-f", filename).!!(ProcessLogger.apply(println(_)))
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
