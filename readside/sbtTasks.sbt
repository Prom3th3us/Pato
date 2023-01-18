

lazy val readside = taskKey[Unit]("Start readside")

readside := (Compile / runMain).toTask(" readside.Main").value
