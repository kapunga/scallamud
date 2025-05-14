import sbt.*

object Dependencies {
  object V {
    val cats = "2.13.0"
    val catsEffect = "3.7-4972921"
    val circe = "0.14.13"
    val fallatol = "0.3.1"
    val fs2 = "3.12.0"
    val http4s = "0.23.30"
    val munit = "1.1.1"
    val terminus = "0.4.0"
  }

  object Libraries {
    val cats: Seq[ModuleID] = Seq(
      "org.typelevel" %% "cats-core" % V.cats,
      "org.typelevel" %% "cats-effect" % V.catsEffect
    )

    val circe: Seq[ModuleID] = Seq(
      "io.circe" %% "circe-core" % V.circe,
      "io.circe" %% "circe-generic" % V.circe,
      "io.circe" %% "circe-parser" % V.circe
    )

    val fallatol: Seq[ModuleID] =
      Seq("org.kapunga" %% "fallatol-config" % V.fallatol)
      
    val fs2: Seq[ModuleID] = Seq(
      "co.fs2" %% "fs2-core" % V.fs2,
      "co.fs2" %% "fs2-io" % V.fs2
    )
    
    val http4s: Seq[ModuleID] = Seq(
      "org.http4s" %% "http4s-dsl" % V.http4s,
      "org.http4s" %% "http4s-ember-server" % V.http4s,
      "org.http4s" %% "http4s-ember-client" % V.http4s
    )
    
    val munit: Seq[ModuleID] = Seq(
      "org.scalameta" %% "munit" % V.munit % Test
    )
    
    val terminus: Seq[ModuleID] =
      Seq("org.creativescala" %% "terminus-core" % V.terminus)
  }
}