name := "splice-machine-spark-connector"

version := "0.1"

scalaVersion := "2.11.12"

val spliceVersion = "2.8.0.1919-SNAPSHOT"
val envClassifier = "cdh5.14.0"

val hbaseVersion = s"1.2.0-$envClassifier"
val hadoopVersion = s"2.6.0-$envClassifier"

// FIXME hbase_sql should actually be dependency of splice_spark
//  ClassNotFoundException: com.splicemachine.derby.impl.SpliceSpark

// FIXME hbase_storage should actually be dependency of splice_spark
// java.lang.NoClassDefFoundError: com/splicemachine/access/HConfiguration
// https://stackoverflow.com/a/46763742/1305344

libraryDependencies ++= Seq(
  "splice_spark",
  "hbase_sql",
  "hbase_storage",
  "hbase_pipeline"
).map(spliceDep)

val sparkVersion = "2.2.0.cloudera2"
libraryDependencies += "org.apache.spark" %% "spark-sql" % sparkVersion

libraryDependencies += "org.apache.hadoop" % "hadoop-common" % hadoopVersion
libraryDependencies += "org.apache.hbase" % "hbase-server" % hbaseVersion

resolvers +=
  "splicemachine-public" at "http://repository.splicemachine.com/nexus/content/groups/public"
resolvers +=
  "cloudera" at "https://repository.cloudera.com/artifactory/cloudera-repos/"
// For development only / local Splice SNAPSHOTs
resolvers += Resolver.mavenLocal

// com.fasterxml.jackson.databind.JsonMappingException: Incompatible Jackson version: 2.9.2
libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.8" force()

// FIXME The following dependencies fail in sbt due to envClassifier property not being resolved
// The pattern is to exclude them first and add them explicitly right after

updateOptions := updateOptions.value.withLatestSnapshots(false)

lazy val mavenProps = settingKey[Unit]("workaround for Maven properties")
mavenProps := {
  sys.props("envClassifier") = envClassifier
  sys.props("hbase.version") = hbaseVersion
  sys.props("hadoop.version") = hadoopVersion
  ()
}

val scalatestVer = "3.0.7"
libraryDependencies += "org.scalactic" %% "scalactic" % scalatestVer
libraryDependencies += "org.scalatest" %% "scalatest" % scalatestVer % Test

def spliceDep(name: String): ModuleID = {
  "com.splicemachine" % name % spliceVersion classifier envClassifier withSources()
}