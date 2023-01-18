import com.lightbend.cinnamon.sbt.Cinnamon.library._
import com.lightbend.cinnamon.sbt.CinnamonLibrary.{cinnamonOpenTracingJaeger, cinnamonOpenTracingZipkin}
import sbt.{Resolver, _}
import sbt._
object Dependencies {
  // Versions
  lazy val scalaVersion = "2.13.1"
  // TODO - private lazy val akkaVersion = "2.6.6"
  private lazy val akkaVersion = "2.6.15" //TODO Before 2.6.12

  // Resolvers
  lazy val commonResolvers = Seq(
    Resolver sonatypeRepo "public",
    Resolver typesafeRepo "releases",
    // for Embedded Kafka 2.4.0
    Resolver.bintrayRepo("seglo", "maven"),
    // the library is available in Bintray repository
    // "dnvriend" at "http://dl.bintray.com/dnvriend/maven"
    Resolver.bintrayRepo("dnvriend", "maven"),
    "Confluent" at "https://packages.confluent.io/maven"
  )

  lazy val kafkaClientsDeps: List[ModuleID] =
    "org.apache.kafka" % "kafka-clients" % "6.1.0-ccs" :: //TODO before 2.6.0
      "io.confluent" % "monitoring-interceptors" % "6.1.0" ::
      Nil

  // Modules
  trait Module {
    def modules: Seq[ModuleID]
  }

  object Test extends Module {
    private lazy val scalaTestVersion = "3.1.0"
    private lazy val scalaCheckVersion = "1.14.0"

    private lazy val scalaTic = "org.scalactic" %% "scalactic" % scalaTestVersion
    private lazy val scalaTest = "org.scalatest" %% "scalatest" % scalaTestVersion
    private lazy val scalaCheck = "org.scalacheck" %% "scalacheck" % scalaCheckVersion
    private lazy val akkaTestKit = "com.typesafe.akka" %% "akka-testkit" % akkaVersion
    private lazy val akkaTypedTestKit = "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion
    private lazy val akkaStreamTestKit = "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion
    private lazy val kafkaTestKit = "com.typesafe.akka" %% "akka-stream-kafka-testkit" % "2.0.0-RC1"


    private lazy val kafkaVersion = "2.7.0"  // TODO - Before kafkaVersion = "2.6.12 "
    private lazy val embeddedKafkaVersion = kafkaVersion
    // private lazy val embeddedKafka = "io.github.seglo" %% "embedded-kafka" % embeddedKafkaVersion // "io.github.embeddedkafka" %% "embedded-kafka" % embeddedKafkaVersion

    override def modules: Seq[ModuleID] =
     // TODO  scalaTest :: scalaTic :: scalaCheck :: akkaTestKit :: akkaTypedTestKit :: akkaStreamTestKit :: kafkaTestKit :: embeddedKafka :: Nil
      scalaTest :: scalaTic :: scalaCheck :: akkaTestKit :: akkaTypedTestKit :: akkaStreamTestKit :: kafkaTestKit :: Nil
  }

  object TestDB extends Module {
    private lazy val lvlDbVersion = "0.12"
    private lazy val lvlDbJniVersion = "1.8"

    private lazy val lvlDb = "org.iq80.leveldb" % "leveldb" % lvlDbVersion
    private lazy val lvlDbJni = "org.fusesource.leveldbjni" % "leveldbjni-all" % lvlDbJniVersion

    override def modules: Seq[ModuleID] =
      lvlDb :: lvlDbJni :: Nil
  }

  object Akka extends Module {
    val akkaHttpVersion = "10.1.11"
    val akkaManagementVersion = "1.1.2" //TODO Before 1.0.9
    val alpakkaKafkaVersion = "2.1.0"

    private def akkaModule(name: String) = "com.typesafe.akka" %% name % akkaVersion
    private def akkaHttpModule(name: String) = "com.typesafe.akka" %% name % akkaHttpVersion
    private def akkaManagmentModule(name: String) = "com.lightbend.akka.management" %% name % akkaManagementVersion


    // TODO - added jackson serializer dependency
    // libraryDependencies += "com.typesafe.akka" %% "akka-serialization-jackson" % akkaVersion


    override def modules: Seq[ModuleID] =
      akkaModule("akka-cluster-tools") ::
      akkaModule("akka-remote") ::
      akkaModule("akka-discovery") ::
      akkaModule("akka-persistence-query") ::
      akkaModule("akka-actor") ::
      "com.typesafe.akka" %% "akka-persistence" % akkaVersion ::
      "com.oracle.database.jdbc" % "ojdbc8" % "21.3.0.0" ::
      "com.typesafe.akka" %% "akka-slf4j" % akkaVersion ::
      "com.typesafe.akka" %% "akka-http-core" % akkaHttpVersion ::
      "com.typesafe.akka" %% "akka-http" % akkaHttpVersion ::
      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion ::
      "com.typesafe.akka" %% "akka-cluster" % akkaVersion ::
      "com.typesafe.akka" %% "akka-cluster-sharding" % akkaVersion ::
      //  "com.typesafe.akka" %% "akka-typed" % akkaVersion ::
      "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion ::
      "com.typesafe.akka" %% "akka-persistence-typed" % akkaVersion ::
      "com.typesafe.akka" %% "akka-cluster-sharding-typed" % akkaVersion ::
      "com.lightbend.akka.discovery" %% "akka-discovery-kubernetes-api" % akkaManagementVersion ::
      "com.lightbend.akka.management" %% "akka-management" % akkaManagementVersion ::
      "com.lightbend.akka.management" %% "akka-management-cluster-http" % akkaManagementVersion ::
      "com.lightbend.akka.management" %% "akka-management-cluster-bootstrap" % akkaManagementVersion ::
        "com.typesafe.akka" %% "akka-stream-kafka" % alpakkaKafkaVersion ::
        "com.typesafe.akka" %% "akka-serialization-jackson" % akkaVersion ::
        "com.typesafe.akka" %% "akka-protobuf" % akkaVersion ::
        // Add Lightbend Platform to your build as documented at https://developer.lightbend.com/docs/lightbend-platform/introduction/getting-started/subscription-and-credentials.html
        "com.lightbend.akka" %% "akka-diagnostics" % "1.1.16" ::
        "com.github.dnvriend" %% "akka-persistence-inmemory" % "2.5.15.2" ::
      Nil
  }

  object Cassandra extends Module {
    lazy val AkkaPersistenceCassandraVersion = "1.0.5"
    lazy val AkkaProjectionVersion = "0.2"
    // TODO lazy val AkkaProjectionVersion = "1.1.0"
    private def akkaPersistenceCassandraModule(name: String) =
      "com.typesafe.akka" %% name % AkkaPersistenceCassandraVersion


    override def modules: Seq[sbt.ModuleID] =
      akkaPersistenceCassandraModule("akka-persistence-cassandra") ::
        "com.lightbend.akka" %% "akka-projection-core" % AkkaProjectionVersion ::
      "com.lightbend.akka" %% "akka-projection-eventsourced" % AkkaProjectionVersion ::
      "com.lightbend.akka" %% "akka-projection-cassandra" % AkkaProjectionVersion ::
      Nil
  }


  object Cinnamon extends Module {

    override def modules: Seq[sbt.ModuleID] =
      cinnamonAkka ::
      cinnamonAkkaHttp ::
      cinnamonJvmMetricsProducer ::
      cinnamonPrometheus ::
      cinnamonPrometheusHttpServer ::
      cinnamonAkkaPersistence ::
      cinnamonAkkaStream ::
        cinnamonOpenTracing ::
        cinnamonOpenTracingJaeger ::
        cinnamonOpenTracingZipkin ::
        cinnamonAkkaTyped ::
      Nil
  }

  object ScalaZ extends Module {
    private lazy val scalazVersion = "7.2.28"

    private lazy val scalazCore = "org.scalaz" %% "scalaz-core" % scalazVersion
    private lazy val scalazConcurrent = "org.scalaz" %% "scalaz-concurrent" % scalazVersion

    override def modules: Seq[ModuleID] = scalazCore :: scalazConcurrent :: Nil
  }

  object Kamon extends Module {
    val core = "io.kamon" %% "kamon-core" % "2.2.0"
    val status = "io.kamon" %% "kamon-status-page" % "2.2.0"
    val prometheus = "io.kamon" %% "kamon-prometheus" % "2.2.0"
    // val bundle = "io.kamon" %% "kamon-bundle" % "2.1.13"
    override def modules: Seq[sbt.ModuleID] = core :: status :: prometheus :: Nil
  }


  object Utils extends Module {
    private lazy val logbackVersion = "1.2.3"
    private lazy val kryoVersion = "2.0.1"

    private lazy val logback = "ch.qos.logback" % "logback-classic" % logbackVersion
    private lazy val logbackEncoder = "net.logstash.logback" % "logstash-logback-encoder" % "5.3"
//    private lazy val kryo = "io.altoo" %% "akka-kryo-serialization" % "1.1.0" //"com.twitter" %% "chill-akka" % kryoVersion
    private lazy val kryo = "io.altoo" %% "akka-kryo-serialization" % "2.0.1" //"com.twitter" %% "chill-akka" % kryoVersion
    private lazy val playJson = "com.typesafe.play" %% "play-json" % "2.8.1"
    private lazy val playJsonExtensions = "ai.x" %% "play-json-extensions" % "0.40.2"
    private lazy val playJsonTraits = "io.leonard" %% "play-json-traits" % "1.5.1"
    private lazy val commonsIO = "commons-io" % "commons-io" % "2.6"
    private lazy val reflections = "org.reflections" % "reflections" % "0.9.10"
    private lazy val shapeless = "com.chuusai" %% "shapeless" % "2.3.3"
    private lazy val heikoseeberger = "de.heikoseeberger" %% "akka-http-play-json" % "1.30.0"
    private lazy val kafkaInterceptor = "io.confluent" % "monitoring-interceptors" % "6.1.0"


    override def modules: Seq[ModuleID] =
      logback ::
      logbackEncoder ::
      kryo ::
      playJson ::
      playJsonExtensions ::
      playJsonTraits ::
      commonsIO ::
      reflections ::
      shapeless ::
      heikoseeberger ::
        kafkaInterceptor ::
      Nil
  }

  // Projects
  lazy val mainDeps
      : Seq[sbt.ModuleID] = Akka.modules ++ ScalaZ.modules ++ Cassandra.modules ++ Utils.modules ++ Kamon.modules ++ Cinnamon.modules
  lazy val testDeps: Seq[sbt.ModuleID] = Test.modules ++ TestDB.modules
}

trait Dependencies {
  val scalaVersionUsed: String = Dependencies.scalaVersion
  val commonResolvers: Seq[MavenRepository] = Dependencies.commonResolvers
  val mainDeps: Seq[sbt.ModuleID] = Dependencies.mainDeps
  val testDeps: Seq[sbt.ModuleID] = Dependencies.testDeps
  val kafkaClientsDeps: List[ModuleID] = Dependencies.kafkaClientsDeps
}
