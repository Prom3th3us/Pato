import Settings._
import sbt.Keys.scalaVersion

run / javaOptions += "-Xmx4G -Xms2G -XX:MaxGCPauseMillis=500"

lazy val commonSettings = Seq(
  ThisBuild / organization := "peperina",
  version := "1.0",
  scalaVersion := Dependencies.scalaVersion
)

lazy val global = project
    .in(file("."))
    .settings(
      name := "Copernico"
    )
    .settings(commonSettings)
    .settings(modulesSettings)
    .settings(mainSettings)
    .settings(testSettings)
    .settings(scalaFmtSettings)
    .settings(testCoverageSettings)
    .settings(CommandAliases.aliases)
    .enablePlugins(ScoverageSbtPlugin)
    .enablePlugins(JavaServerAppPackaging, DockerPlugin)
    .enablePlugins(Cinnamon)
    .aggregate(
      common,
      pcs,
      readside,
      it
    ) configs (FunTest) settings (inConfig(FunTest)(Defaults.testTasks): _*)

lazy val FunTest = config("fun") extend (Test)

def funTestFilter(name: String): Boolean = ((name endsWith "E2E") || (name endsWith "IntegrationTest"))
def unitTestFilter(name: String): Boolean = ((name endsWith "Spec") && !funTestFilter(name))

FunTest / testOptions := Seq(Tests.Filter(funTestFilter))

Test / testOptions := Seq(Tests.Filter(unitTestFilter))

run / cinnamon := true
test / cinnamon := false
cinnamonLogLevel := "INFO"

lazy val globalResources = file("resources")

lazy val common = (project in file("./common"))

lazy val pcs = project
  .settings(
    Seq(
      Test / parallelExecution := true,
       Compile / unmanagedResourceDirectories += globalResources
    )
  )
  .settings(commonSettings)
  .settings(modulesSettings)
  .settings(
    name := "pcs",
    assemblySettings
  )
  .dependsOn(
    common % "compile->compile;test->test"
  )
  .enablePlugins(JavaServerAppPackaging, DockerPlugin)
  .enablePlugins(CinnamonAgentOnly)
  .settings(
    mainClass := Some("Main")
  )
  .settings(
    dockerBaseImage := "openjdk:11",
    dockerUsername := Some("pcs"),
    dockerEntrypoint := Seq("/opt/docker/bin/pcs"),
    dockerExposedPorts := Seq(
        2551, 2552, 2553, 8081, 8083, 8084, 8558, 9095, 5266
      )
  )
  .settings(
    Compile / mainClass := Some("Main"),
    //run / mainClass := Some("Main")
  )

lazy val readside = project
  .settings(
    Seq(
      Test / parallelExecution := true,
      Compile / unmanagedResourceDirectories += globalResources
    )
  )
  .settings(commonSettings)
  .settings(modulesSettings)
  .settings(
    name := "readside",
    assemblySettings
  )
  .dependsOn(
    pcs % "compile->compile;test->test"
  )
  .enablePlugins(JavaServerAppPackaging, DockerPlugin)
  .enablePlugins(CinnamonAgentOnly)
  .settings(
    mainClass := Some("readside.Main")
  )
  .settings(
    dockerBaseImage := "openjdk:11",
    dockerUsername := Some("readside"),
    dockerEntrypoint := Seq("/opt/docker/bin/readside"),
    dockerExposedPorts := Seq(
        2554,
        8559,
        8081,
        9095,
        5266
      )
  )

lazy val it = project
  .settings(
    Seq(
      Test / parallelExecution := true
    )
  )
  .settings(commonSettings)
  .settings(modulesSettings)
  .settings(
    name := "kafka-event-producer"
  )
  .dependsOn(
    common % "compile->compile;test->test",
    readside % "compile->compile;test->test"
  )

  .enablePlugins(JavaServerAppPackaging, DockerPlugin)
  .settings(
    mainClass := Some("it.KafkaEventProducer")
  )
  .settings(
    dockerBaseImage := "openjdk:11",
    dockerUsername := Some("kafka-event-producer"),
    dockerEntrypoint := Seq("/opt/docker/bin/kafka-event-producer")
  )

val AkkaVersion = "2.6.15"
libraryDependencies += "com.typesafe.akka" %% "akka-persistence" % AkkaVersion

