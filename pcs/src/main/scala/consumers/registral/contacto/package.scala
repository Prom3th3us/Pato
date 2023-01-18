/*package consumers.registral

import java.time.{LocalDateTime, ZoneOffset}
import java.util.Date

import cassandra.CassandraTypesAdapter._
import consumers.registral.contacto.domain.{ContactoEvents, ContactoExternalDto}
import ddd.ReadSideProjection

package object contacto {

  final case class Contacto(
      bctSujIdentificador: String,
      bctCtcId: String,
      bctArchivo: String, // TODO: Type for blob??, because the Object type fails with the serializer
      bctDescripcion: String,
      bctFecha: LocalDateTime,
      bctOtrosAtributos: Map[String, String],
      bctTipo: String
  )

  final case class ContactoReadSideProjection(event: ContactoEvents.ContactoUpdatedFromDto) extends ReadSideProjection[ContactoEvents] {
    def collectionName: String = "read_side.buc_contactos"

    val contacto: ContactoExternalDto = event.registro

    val bindings: List[(String, Object)] = List(
      "bct_suj_identificador" -> contacto.bctSujIdentificador,
      "bct_ctc_id" -> contacto.bctCtcId,
      "bct_archivo" -> contacto.bctArchivo,
      "bct_descripcion" -> contacto.bctDescripcion,
      "bct_fecha" -> localDateTime(contacto.bctFecha),
      "bct_otros_atributos" -> contacto.bctOtrosAtributos,
      "bct_tipo" -> contacto.bctTipo
    ).filterNot {
      _._2 == null
    }

    val keys: List[(String, Object)] = List(
      "bct_suj_identificador" -> contacto.bctSujIdentificador,
      "bct_ctc_id" -> contacto.bctCtcId
    )
  }

}
 */
