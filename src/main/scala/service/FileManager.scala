package service

import akka.actor.{Actor, ActorLogging}
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.{CreateBucketRequest, GetBucketLocationRequest}

object FileManager {
  case object StartFilesCopy

  // TODO: create more commands

  // TODO: create props
//  def props() = ???
}

class FileManager(s3Client: AmazonS3, bucketName: String) extends Actor with ActorLogging {
  import FileManager._

  // create a bucket if it does not exist


  if (!s3Client.doesBucketExistV2(bucketName)) {
    // Because the CreateBucketRequest object doesn't specify a region, the
    // bucket is created in the region specified in the client.
    s3Client.createBucket(new CreateBucketRequest(bucketName))

    // Verify that the bucket was created by retrieving it and checking its location.
    val bucketLocation = s3Client.getBucketLocation(new GetBucketLocationRequest(bucketName))
    log.info(s"Bucket location: $bucketLocation")
  } else {
    log.info("Suck bucket already exists.")
  }

  val fileUploader = context.actorOf(FileUploader.props(s3Client, bucketName))

  // TODO: create a file reader
  val fileReader = ???

  // TODO: implement
  override def receive: Receive = ???
}
