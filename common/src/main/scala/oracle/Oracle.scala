package oracle
import _root_.oracle.jdbc.pool.OracleDataSource
import com.typesafe.config.{Config, ConfigFactory}
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

object Oracle {
  private val config: Config = ConfigFactory.load()
  val url = Try(System.getenv("STRING_CONEXION_ORACLE")).getOrElse("no")
  val user = Try(System.getenv("USER_ORACLE")).getOrElse("no")
  val password = Try(System.getenv("PASSWORD_ORACLE")).getOrElse("no")

  private val log = LoggerFactory.getLogger(this.getClass)

  val oracleUser = s"${user}"
  val oraclePassword = s"${password}"
  val oracleURL = s"${url}"
  val ods = new OracleDataSource()
  ods.setUser(oracleUser)
  ods.setURL(oracleURL)
  ods.setPassword(oraclePassword)



  def connOracleNifi(input: String, entidad: String, topico: String) = {
    val trimmedList: List[String] = input.split("\"").map(_.trim).toList
    val ev_id = trimmedList(3)
    val a = trimmedList.indexOf("BOB_CANAL_ORIGEN")

    val bob_canal_origenA = trimmedList(a+1) match {
      case s if s.equals(": null,") => "TAX"
      case _ if a.equals(-1) => "TAX"
      case _ => trimmedList(a+2)
    }

    val f = Future(ods.getConnection())

    f.onComplete {
      case Failure(exception) => {
        f.andThen(x => x.get.close())

      }
      case Success(value) => {
        f.andThen(x => x.get.close())
        try {


          val con = ods.getConnection()
          val statement = con.createStatement()
          val queryObligacion =
            s"""
         update tax.EVENTOS_OBN_LOGS
         set PASO = '02', BOB_CANAL_ORIGEN = '${bob_canal_origenA}', topico = '${topico}'
         where EV_ID = '${ev_id}'
  """
          val queryObjeto =
            s"""
         update tax.EVENTOS_OBN_LOGS
         set PASO = '02', BOB_CANAL_ORIGEN = '${bob_canal_origenA}', topico = '${topico}'
         where EV_ID = '${ev_id}'
  """
          val querySujeto =
            s"""
         update tax.EVENTOS_OBN_LOGS
         set PASO = '02', BOB_CANAL_ORIGEN = '${bob_canal_origenA}', topico = '${topico}'
         where EV_ID = '${ev_id}'
  """
          statement.setFetchSize(1000) // important
          entidad match {
            case x if x == "obligacion" => statement.executeUpdate(queryObligacion)
            case x if x == "objeto" => statement.executeUpdate(queryObjeto)
            case x if x == "sujeto" => statement.executeUpdate(querySujeto)
            case _ => log.error("Dont exist this entity")
          }

        } catch {
          case e: Exception => log.error("Error query to Oracle NIFI (Paso 02) - " + e + " - [" + ev_id + "]")
        }

      }
    }


  }

  def connOracleKafkaToWriteside(ev_id: String, entidad: String, bob_canal_origen: String) = {

    val f = Future(ods.getConnection())

    f.onComplete {
      case Failure(exception) => {
        f.andThen(x => x.get.close())

      }
      case Success(value) => {
        f.andThen(x => x.get.close())
        try {
          val con = ods.getConnection()
          val statement = con.createStatement()
          val queryObligacion =
            s"""
           update tax.EVENTOS_OBN_LOGS
           set PASO = '05', BOB_CANAL_ORIGEN = '${bob_canal_origen}'
           where EV_ID = '${ev_id}'
    """
          val queryObjeto =
            s"""
           update tax.EVENTOS_OBN_LOGS
           set PASO = '05', BOB_CANAL_ORIGEN = '${bob_canal_origen}'
           where EV_ID = '${ev_id}
    """
          val querySujeto =
            s"""
           update tax.EVENTOS_OBN_LOGS
           set PASO = '05', BOB_CANAL_ORIGEN = '${bob_canal_origen}'
           where EV_ID = '${ev_id}'
    """

          statement.setFetchSize(1000) // important
          entidad match {
            case x if x == "obligacion" => statement.executeUpdate(queryObligacion)
            case x if x == "objeto" => statement.executeUpdate(queryObjeto)
            case x if x == "sujeto" => statement.executeUpdate(querySujeto)
            case _ => log.error("Dont exist this entity")
          }
        } catch {

          case e: Exception => log.error("Error query to Oracle (Paso 05) - " + e + " - [" + ev_id + "]")
        }

      }
    }

  }




  def connOracleReadsideToCass(ev_id: String,  entidad: String, bob_canal_origen:String) = {
    val bco = bob_canal_origen match {
      case x if (x == null) => "TAX"
      case x => x
    }
    val f = Future(ods.getConnection())

    f.onComplete {
      case Failure(exception) => {
        f.andThen(x => x.get.close())

      }
      case Success(value) => {
        f.andThen(x => x.get.close())
        try {
          val con = ods.getConnection()

          val statement = con.createStatement()
          val queryObligacion =
            s"""
           update tax.EVENTOS_OBN_LOGS
           set PASO = '06', BOB_CANAL_ORIGEN = '${bco}'
           where EV_ID = '${ev_id}'
    """
          val queryObjeto =
            s"""
           update tax.EVENTOS_OBN_LOGS
           set PASO = '06', BOB_CANAL_ORIGEN = '${bco}'
           where EV_ID = '${ev_id}'
    """
          val querySujeto =
            s"""
           update tax.EVENTOS_OBN_LOGS
           set PASO = '06', BOB_CANAL_ORIGEN = '${bco}'
           where EV_ID = '${ev_id}'
    """
          statement.setFetchSize(1000)
          entidad match {
            case x if x == "obligacion" => statement.executeUpdate(queryObligacion)
            case x if x == "objeto" => statement.executeUpdate(queryObjeto)
            case x if x == "sujeto" => statement.executeUpdate(querySujeto)
            case _ => log.error("Dont exist this entity")
          }
        } catch {
          case e: Exception => log.error("Error query to Oracle (Paso 06) - " + e + " - [" + ev_id + "]")
        }
      }
    }

  }

}
