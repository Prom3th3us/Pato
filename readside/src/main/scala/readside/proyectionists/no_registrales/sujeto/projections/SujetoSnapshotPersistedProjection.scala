package readside.proyectionists.no_registrales.sujeto.projections
import consumers.no_registral.sujeto.application.entity.SujetoExternalDto
import consumers.no_registral.sujeto.domain.SujetoEvents

final case class SujetoSnapshotPersistedProjection(
    event: SujetoEvents.SujetoSnapshotPersisted
) extends SujetoProjection {

  val registro: Option[SujetoExternalDto] = event.registro
  val fromRegistro: Option[List[(String, Option[Object])]] = registro map { registro =>
    List(
      "suj_canal_origen" -> registro.SUJ_CANAL_ORIGEN,
      "suj_cat_suj_id" -> registro.SUJ_CAT_SUJ_ID,
      "suj_denominacion" -> registro.SUJ_DENOMINACION,
      "suj_dfe" -> registro.SUJ_DFE,
      "suj_direccion" -> registro.SUJ_DIRECCION,
      "suj_email" -> registro.SUJ_EMAIL,
      "suj_id_externo" -> registro.SUJ_ID_EXTERNO,
      "suj_otros_atributos" -> registro.SUJ_OTROS_ATRIBUTOS,
      "suj_riesgo_fiscal" -> registro.SUJ_RIESGO_FISCAL,
      "suj_situacion_fiscal" -> registro.SUJ_SITUACION_FISCAL,
      "suj_telefono" -> registro.SUJ_TELEFONO,
      "suj_tipo" -> registro.SUJ_TIPO
    )
  }

  val others: List[(String, BigDecimal)] =
    List(
      "suj_saldo" -> event.saldo
    )

  val bindings: List[(String, Serializable)] = fromRegistro match {
    case Some(fromRegistro) => fromRegistro ++ others
    case None => others
  }
}
