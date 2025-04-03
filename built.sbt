import Dependencies.Libraries

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
    core.native)

lazy val core = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .crossType(CrossType.Pure)
  .in(file("core"))
  .settings(
    commonSettings,
    name := "scallamud-core",
    startYear := Some(2025),
    libraryDependencies ++= Libraries.fallatol ++ Libraries.terminus 
  )

addCommandAlias(
  "formatAll",
  "+scalafmtAll; +scalafixAll; +headerCreateAll"
)
