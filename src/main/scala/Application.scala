import java.io.{File, FileInputStream}

import akka.actor.ActorSystem
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.model.{CreateBucketRequest, GetBucketLocationRequest}
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}
import service.{FileManager, FileUploader, PhotoUploader}

object Application extends App {
  val bucketName = "user-files-backend-system-design"

  import com.amazonaws.auth.BasicAWSCredentials

  val awsCreds = new BasicAWSCredentials("your_access_key", "your_secret_key")

  val s3Client: AmazonS3 = AmazonS3ClientBuilder.standard()
    .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
    .withRegion(Regions.EU_CENTRAL_1)
    .build()

  implicit val system = ActorSystem()

//  val fileManager = system.actorOf(FileManager.props(), "file-manager")

  // start copying files from one folder to another
//  fileManager ! FileManager.StartFilesCopy



  /*********** Uploading photo **************/
  if (!s3Client.doesBucketExistV2(bucketName)) {
    // Because the CreateBucketRequest object doesn't specify a region, the
    // bucket is created in the region specified in the client.
    s3Client.createBucket(new CreateBucketRequest(bucketName))

    // Verify that the bucket was created by retrieving it and checking its location.
    val bucketLocation = s3Client.getBucketLocation(new GetBucketLocationRequest(bucketName))
    println(bucketLocation)
  } else {
    println("bucket already exists")
  }

  val photoUploader = system.actorOf(PhotoUploader.props(s3Client, bucketName), "photo-uploader")

  val fileInputStream = new FileInputStream("./src/main/resources/MonkaS.png")

  photoUploader ! PhotoUploader.UploadPhoto(fileInputStream, "MonkaS.png")
}
