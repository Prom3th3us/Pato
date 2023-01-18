package serialization.advanced

import play.api.libs.json.JsonNaming

/**
 * For each class property, use the underscore case equivalent
 * to name its column (e.g. EV_ID -> ev_id).
 */
object ExternalDtoCase extends JsonNaming {

  def camelToUnderscores(name: String) =
    "[A-Z]".r.replaceAllIn(name, { m =>
      "_" + m.group(0).toLowerCase()
    })

  /* For each class property, use the camelCase case equivalent
   * to name its column
   * (e.g. exampleFormat --> example_format)
   * (e.g. EXAMPLE_FORMAT --> example_format)
   */
  def lower_underscore(property: String): String = {
    val proposedUnderscore = camelToUnderscores(property).toLowerCase
    // in this case the string was already correct
    // and now looked like this _s_u_j__t_i_p_o
    if (proposedUnderscore contains "__") property.toLowerCase
    else proposedUnderscore
  }

  def apply(property: String): String = lower_underscore(property)

  override val toString = "ExternalDtoCase"
}
