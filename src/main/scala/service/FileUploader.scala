package service

import java.io.File

import akka.actor.{Actor, ActorLogging, Props}
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest

object FileUploader {
  case class UploadText(text: String)
  case class UploadFile(filePath: String)

  def props(s3Client: AmazonS3, bucketName: String) = Props(new FileUploader(s3Client, bucketName))

}

class FileUploader(s3Client: AmazonS3, bucketName: String) extends Actor with ActorLogging {
  import FileUploader._

  override def receive: Receive = {
    case UploadText(text) =>
      // Upload a text string as a new object.// Upload a text string as a new object.

      s3Client.putObject(bucketName, "dir1/dir2/example.txt", text)
      println(s"text put to bucket: $bucketName")


    case UploadFile(filePath) =>

    // Upload a file as a new object with ContentType and title specified.
      val fileName = filePath.split("/").last
      log.info(s"Putting file $fileName to AWS S3.")

      val request = new PutObjectRequest(bucketName, s"daulet/files/$fileName", new File(filePath))
      val metadata = new ObjectMetadata
      metadata.setContentType("plain/text")
      metadata.addUserMetadata("x-amz-meta-title", "bsd-temp-file")
      request.setMetadata(metadata)
      s3Client.putObject(request)

      log.info(s"File: $filePath put to AWS S3.")
  }
}
