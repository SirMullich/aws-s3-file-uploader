name := "aws-s3-file-uploader"

version := "0.1"

scalaVersion := "2.12.7"

libraryDependencies ++= Seq(
  // https://mvnrepository.com/artifact/com.amazonaws/aws-java-sdk-s3
  "com.amazonaws" % "aws-java-sdk-s3" % "1.11.452",
  "com.typesafe.akka" %% "akka-actor" % "2.5.18",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.18" % Test
)