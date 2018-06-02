package sg.activewealth.roboadvisor.infra.helper;

import java.io.File;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import sg.activewealth.roboadvisor.infra.exception.SystemException;
import sg.activewealth.roboadvisor.infra.model.IAttachmentable;

@Component
public class S3AttachmentHelper extends AbstractHelper implements IAttachmentableHelper {

    private static final String S3_BUCKET_PREFIX = "uploads/";

    protected Logger logger = Logger.getLogger(S3AttachmentHelper.class);

    private AmazonS3 connectToS3() {
        AWSCredentials awsCredentials = new BasicAWSCredentials(propertiesHelper.awsAccesskey,
                propertiesHelper.awsSecretkey);

        AmazonS3 amazonS3Client = new AmazonS3Client(awsCredentials);

        return amazonS3Client;
    }

    @Override
    public String saveFile(final IAttachmentable attachment) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            AccessControlList accessControlList = new AccessControlList();
            metadata.setContentLength(attachment.getAttachment().getSize());
            metadata.setContentType(attachment.getAttachment().getContentType());
            accessControlList.grantPermission(GroupGrantee.AllUsers,
                    com.amazonaws.services.s3.model.Permission.Read);
            String fileName = S3_BUCKET_PREFIX + attachment.getId();
            String bucketName = propertiesHelper.awsBucketName;
            connectToS3().putObject(new PutObjectRequest(bucketName, fileName,
                    attachment.getAttachment().getInputStream(), metadata)
                            .withAccessControlList(accessControlList));

        } catch (Exception e) {
            throw new SystemException(e);
        }
        return attachment.getId();
    }

    @Override
    public String getFilename(String id) {
        String filename = getFileNameHash(id);
        return getS3Filename(id, filename);
    }

    private String getS3Filename(String id, String filename) {
        return S3_BUCKET_PREFIX + id;
    }

    @Override
    public InputStream retrieveFileInputStream(String id) {
        try {
            String awsS3BucketName = propertiesHelper.awsBucketName;
            S3Object s3object =
                    connectToS3().getObject(new GetObjectRequest(awsS3BucketName, getFilename(id)));
            // TODO Need to store content type or file name with extension to know the file type for
            // download
            return s3object.getObjectContent();
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }

    @Override
    public Object[] retrieveFileInputStreamAndContentType(String id) {
        String filename = getFileNameHash(id);
        try {
            S3Object o = connectToS3().getObject(propertiesHelper.awsBucketName,
                    getS3Filename(id, filename));
            if (o != null) {
                // return new Object[] {o.getDataInputStream(), o.getContentType()};
            }
            return null;
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }

    @Override
    public boolean deleteFile(final String id) {
        try {
            final String filename = getFileNameHash(id);
            connectToS3().deleteObject(propertiesHelper.awsBucketName, S3_BUCKET_PREFIX + filename);
        } catch (Exception e) {
            throw new SystemException(e);
        }
        return true;
    }

    @Override
    public String getFileNameHash(String original) {
        return original;
    }

    public String getUploadsPath() {
        String path = propertiesHelper.appDeploy + System.getProperty("file.separator")
                + S3_BUCKET_PREFIX + System.getProperty("file.separator");
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.mkdirs();
            } catch (Exception e) {
                throw new SystemException(e);
            }
        }
        return path;
    }

    public String getS3BucketName() {
        return propertiesHelper.awsBucketName;
    }

    public String getFormatNameFromContentType(String contentType) {
        contentType = contentType.toLowerCase();
        if (contentType.contains("png")) {
            return "png";
        } else if (contentType.contains("jpeg")) {
            return "jpeg";
        } else if (contentType.contains("jpg")) {
            return "jpg";
        } else if (contentType.contains("gif")) {
            return "gif";
        }
        return "jpg"; // default to jpg
    }

}
