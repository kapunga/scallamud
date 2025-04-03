import sbt.*

object Dependencies {
  object V {
    val fallatol = "0.3.1"
    val terminus = "0.4.0"
  }

  object Libraries {
    val fallatol: Seq[ModuleID] =
      Seq("org.kapunga" %% "fallatol-config" % V.fallatol)

    val terminus: Seq[ModuleID] =
      Seq("org.creativescala" %% "terminus-core" % V.terminus)
  }
}
