package utils.implicits

object StringT {

  implicit class ImprovedString(s: String) {
    def contains(strings: Set[String]): Boolean =
      s containsAll strings
    def containsAll(strings: Set[String]): Boolean =
      strings.forall(s.contains)
    def containsAny(strings: Set[String]): Boolean =
      strings.exists(s.contains)

    def yay: String = Console.GREEN + s + Console.RESET
    def nay: String = Console.RED + s + Console.RESET

    def prettyBy(separator: String)(in: String): String =
      in.split(separator)
        .map {
          case s"[$a]" if a containsAll Set("[", "]") => s"[${a.pretty}]"
          case s"[$aggregateName|$aggregateRoot]" =>
            s"${Console.YELLOW}$aggregateName${Console.RESET}|${Console.CYAN}$aggregateRoot${Console.RESET}"
          case s"[$a]" => s"[${Console.YELLOW}$a${Console.RESET}]"
          case s"Some($a)" => a
          case s"None" => "_"
          case "Map()" => "_"
          case "-999999999-01-01T00:00" => "_"
          case s"Empty($empty)" => "_"
          case s"$year-$month-${day}T$hour:$min:$sec.$millis" => s"$year-$month-$day"
          case value => value
        }
        .mkString(separator)

    def pretty: String =
      prettyBy(" ")(prettyBy(",")(s))
  }
}
