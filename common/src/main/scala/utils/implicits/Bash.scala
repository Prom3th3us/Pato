package utils.implicits
import scala.sys.process._

object Bash {

  implicit class BashString(s: String) {
    def bash: LazyList[String] = runScript(s)
    def lazy_bash: String = prepareScript(s)
  }
  def prepareScript(script: String): String = {
    val path = s"/tmp/${Math abs script.hashCode}.sh"
    val file = reflect.io.File(path)
    file.setExecutable(executable = true)
    file.writeAll(script)
    Thread.sleep(1000)
    s"sh $path"
  }
  def runScript(script: String): LazyList[String] = {
    val path = prepareScript(script)
    path.lazyLines_!
  }
}
