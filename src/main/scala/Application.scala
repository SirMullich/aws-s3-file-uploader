import akka.actor.ActorSystem
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}
import service.{FileManager, FileUploader}

object Application extends App {
  val bucketName = "user-files-backend-system-design"

  import com.amazonaws.auth.BasicAWSCredentials

  val awsCreds = new BasicAWSCredentials("your_access_key", "your_secret_key")

  val s3Client: AmazonS3 = AmazonS3ClientBuilder.standard()
    .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
    .withRegion(Regions.EU_CENTRAL_1)
    .build()

  implicit val system = ActorSystem()

  val fileManager = system.actorOf(FileManager.props(), "file-manager")

  // start copying files from one folder to another
  fileManager ! FileManager.StartFilesCopy

}
