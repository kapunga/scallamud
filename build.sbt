import Dependencies.Libraries
import sbtassembly.MergeStrategy
import sbtassembly.PathList

val commonSettings = Seq(
  scalaVersion := "3.3.5",
  organization := "org.kapunga",
  organizationName := "Paul (Thor) Thordarson",
  semanticdbEnabled := true,
  semanticdbVersion := scalafixSemanticdb.revision,
  licenses := List("MIT" -> url("https://opensource.org/license/mit")),
  developers := List(
    Developer(
      id = "kapunga",
      name = "Paul (Thor) Thordarson",
      email = "kapunga@gmail.com",
      url = url("https://github.com/kapunga")
    ),
  )
)

lazy val root = (project in file(".")).
  settings(
    commonSettings,
    name := "ScallaMUD"
  ).aggregate(
    core.js,
    core.jvm,
    core.native,
    server,
    client)

lazy val core = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .crossType(CrossType.Pure)
  .in(file("core"))
  .settings(
    commonSettings,
    name := "scallamud-core",
    startYear := Some(2025),
    libraryDependencies ++= Libraries.cats ++ Libraries.circe ++ Libraries.http4s ++ Libraries.fallatol ++ Libraries.terminus ++ Libraries.munit
  )

lazy val server = (project in file("server"))
  .enablePlugins(JavaAppPackaging, DockerPlugin)
  .settings(
    commonSettings,
    name := "scallamud-server",
    startYear := Some(2025),
    libraryDependencies ++= Libraries.cats ++ Libraries.fs2 ++ Libraries.http4s,
    Docker / packageName := "scallamud-server",
    Docker / version := "0.1.0",
    Docker / maintainer := "Paul (Thor) Thordarson <kapunga@gmail.com>",
    Docker / dockerExposedPorts := Seq(8080),
    Docker / dockerBaseImage := "eclipse-temurin:17-jre-alpine"
  ).dependsOn(core.jvm)

lazy val client = (project in file("client"))
  .enablePlugins(AssemblyPlugin)
  .settings(
    commonSettings,
    name := "scallamud-client",
    startYear := Some(2025),
    libraryDependencies ++= Libraries.cats ++ Libraries.fs2 ++ Libraries.http4s,
    assembly / mainClass := Some("slm.client.ScallaMudClient"),
    assembly / assemblyJarName := "scallamud-client.jar",
    // Merge strategy to handle duplicate files during assembly
    assembly / assemblyMergeStrategy := {
      case PathList("META-INF", xs @ _*) => MergeStrategy.discard
      case "module-info.class" => MergeStrategy.discard
      case x if x.endsWith(".properties") => MergeStrategy.first
      case x if x.endsWith(".conf") => MergeStrategy.concat
      case x => 
        val oldStrategy = (assembly / assemblyMergeStrategy).value
        oldStrategy(x)
    }
  ).dependsOn(core.jvm)

addCommandAlias(
  "formatAll",
  "+scalafmtAll; +scalafixAll; +headerCreateAll"
)

addCommandAlias(
  "buildClient",
  "client/assembly"
)

addCommandAlias(
  "buildServer",
  "server/Universal/stage"
)