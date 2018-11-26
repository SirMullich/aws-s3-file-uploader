package service

import java.io.{ByteArrayInputStream, InputStream}

import akka.actor.{Actor, ActorLogging, Props}
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.{ObjectMetadata, PutObjectRequest}
import com.amazonaws.util.IOUtils

object PhotoUploader {
  case class UploadPhoto(filePath: InputStream, fileName: String)

  def props(s3Client: AmazonS3, bucketName: String) = Props(new PhotoUploader(s3Client, bucketName))
}

class PhotoUploader(s3Client: AmazonS3, bucketName: String) extends Actor with ActorLogging {
  import PhotoUploader._

  override def receive: Receive = {

    case UploadPhoto(inputStream: InputStream, fileName: String) =>

      val contents = IOUtils.toByteArray(inputStream)
      val byteArrayInputStream = new ByteArrayInputStream(contents)

      log.info("Uploading file")

      val meta = new ObjectMetadata()
      meta.setContentLength(contents.length)
      log.info("photo length: {}", contents.length)
      meta.setContentType("image/png")

      s3Client.putObject(new PutObjectRequest(bucketName, s"my-images/$fileName", byteArrayInputStream, meta))
      inputStream.close()
  }

}
