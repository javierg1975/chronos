
name := "metronome"

version := "0.1"

retrieveManaged := true

libraryDependencies += "org.parboiled" %% "parboiled" % "2.0.0"

// reduce the maximum number of errors shown by the Scala compiler
//maxErrors := 20

// increase the time between polling for file changes when using continuous execution
//pollInterval := 1000

scalacOptions ++= Seq("-unchecked", "-deprecation", "-encoding", "utf8")

scalaVersion := "2.11.2"

// set the prompt (for this build) to include the project id.
//shellPrompt in ThisBuild := { state => Project.extract(state).currentRef.project + "> " }

// set the prompt (for the current project) to include the username
///shellPrompt := { state => System.getProperty("user.name") + "> " }

// only show 20 lines of stack traces
//traceLevel := 20

//logLevel := Level.Info

//artifactName := { (sv: ScalaVersion, module: ModuleID, artifact: Artifact) =>
//  artifact.name + "-" + module.revision + "." + artifact.extension
//}
