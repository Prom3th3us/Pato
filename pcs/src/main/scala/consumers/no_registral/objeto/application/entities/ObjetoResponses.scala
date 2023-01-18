package consumers.no_registral.objeto.application.entities

import java.time.LocalDateTime

import consumers.no_registral.objeto.application.entities.ObjetoExternalDto.Exencion
import design_principles.actor_model.Response

sealed trait ObjetoResponses extends Response

object ObjetoResponses {
  case class GetObjetoResponse(
      saldo: BigDecimal,
      tags: Set[String] = Set.empty,
      obligaciones: Set[String] = Set.empty, // implement Json extension for tuples here: objetos: Set[(String, String)] = Set.empty,
      sujetos: Set[String] = Set.empty,
      sujetoResponsable: Option[String] = None,
      fechaUltMod: LocalDateTime = LocalDateTime.MIN,
      registro: Option[ObjetoExternalDto] = None,
      exenciones: Set[Exencion]
  ) extends ObjetoResponses

  case class GetExencionResponse(
      exencion: Option[Exencion]
  ) extends ObjetoResponses
}
