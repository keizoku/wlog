import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "wlog"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    anorm,
	//"org.squeryl" %% "squeryl" % "0.9.5-2",
	"com.typesafe" % "slick_2.10.0-RC1" % "0.11.2",
	"org.postgresql" % "postgresql" % "9.2-1003-jdbc4"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
