import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.core.DockerClientBuilder
import org.scalatest.{FlatSpec, Matchers}

import scala.sys.process._

class SSHDSpec extends FlatSpec with Matchers {

  implicit var docker = DockerClientBuilder.getInstance("unix:///var/run/docker.sock").build()

  "user" should "be able to login with a fixed public key" in {
    val key = SSH.genKey()
    val containerId = SSHD("valdisxp1/sshd-socat:latest").authorizedKeys(key.publicKey)
    Thread.sleep(1000)
    val info = docker.inspectContainerCmd(containerId).exec()
    val ip = info.getNetworkSettings.getNetworks.get("bridge").getIpAddress
    containerId should startWith(SSH.connect(s"root@$ip",key = key)("echo","$HOSTNAME").trim)
  }
}

case class SSHD(imageName: String)(implicit docker: DockerClient) {
  def authorizedKeys(keys: String*) = {
    container("AUTHORIZED_KEYS" -> keys.mkString(" "))
  }

  private def container(config: (String, String)): String = {
    val id = docker.createContainerCmd(imageName).withEnv(s"${config._1}=${config._2}").exec().getId
    docker.startContainerCmd(id).exec()
    id
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
