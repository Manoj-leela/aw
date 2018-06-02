package sg.activewealth.roboadvisor.infra.helper;

import java.io.InputStream;

import sg.activewealth.roboadvisor.infra.model.IAttachmentable;

public interface IAttachmentableHelper {
	
	public String saveFile(final IAttachmentable attachment);
	
	public String getFilename(String id);
	
	public InputStream retrieveFileInputStream(String id);
	
	public Object[] retrieveFileInputStreamAndContentType(String id);
	
	public boolean deleteFile(final String id);
	
	public String getFileNameHash(String original);

}
