import Utils.fancyPrompt
//import org.scalafmt.sbt.ScalafmtPlugin.autoImport.{scalafmtConfig, scalafmtOnCompile}
import sbt.Keys._
import sbt._
import sbtassembly.AssemblyPlugin.autoImport.MergeStrategy
import sbtassembly.PathList
import scoverage.ScoverageKeys.{coverageEnabled, coverageExcludedPackages, coverageFailOnMinimum, coverageMinimum}

object Settings extends Dependencies with CommonScalac {

  lazy val globalResources = file("./common/src/main/resources")
  Compile / unmanagedResourceDirectories += globalResources

  val modulesSettings = Seq(
    scalacOptions ++= scalacSettings,
    scalaVersion := scalaVersionUsed,
    resolvers ++= commonResolvers,
    libraryDependencies ++= mainDeps,
    libraryDependencies ++= testDeps map (_ % Test)
  )

  lazy val testCoverageSettings = Seq(
    coverageMinimum := 1,
    coverageFailOnMinimum := true,
    //Test / coverageEnabled := true,
    //compile / coverageEnabled := true,
    //Compile / coverageEnabled := false,
    //compile / coverageEnabled := false,
    coverageExcludedPackages := "router.Routes.*;<empty>;Reverse.*;router\\.*"
  )

  lazy val scalaFmtSettings = Seq(
    //scalafmtOnCompile := true,
    //scalafmtConfig := file(".scalafmt.conf")
  )

  lazy val testSettings = Seq(
    Test / parallelExecution := true,
    Test / fork := true,
    Test / javaOptions += "-Xmx2G",
    //triggeredMessage := Watched.clearWhenTriggered,
    //autoStartServer := false,
    shellPrompt := (_ => fancyPrompt(name.value))
  )

  lazy val mainSettings = Seq(
    run / fork := true, // Calling sbt with -D parameters does not have effects because of `true`
    Compile / mainClass := Some("Main"),
    //run / mainClass := Some("Main")

  )

  import sbtassembly.AssemblyKeys._
  lazy val assemblySettings = Seq(
    assembly / assemblyJarName := name.value + ".jar",
    assembly / assemblyMergeStrategy := {
      case PathList("META-INF", xs @ _*) => MergeStrategy.discard
      case "application.conf" => MergeStrategy.concat
      case x =>
        val oldStrategy = (assembly / assemblyMergeStrategy).value
        oldStrategy(x)
    }
  )

  scalacOptions ++= Seq(
      "-feature",
      "-unchecked",
      "-language:higherKinds",
      "-language:postfixOps",
      "-deprecation"
    ) ++ scalacSettings
}
