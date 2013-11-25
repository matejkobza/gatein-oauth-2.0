package cz.muni.fi.sdipr.web.google;

import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.ParentReference;
import cz.muni.fi.sdipr.core.GoogleOAuthLoginException;
import cz.muni.fi.sdipr.core.interceptor.Login;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: matejkobza
 * Date: 7.11.2013
 * Time: 12:01
 */
@Login
@Named
@SessionScoped
public class DriveController extends AbstractController {

    private static final long serialVersionUID = -7611041534898427988L;
    private Drive drive;
    private List<File> files;
    private Map<String, Boolean> checked = new HashMap<String, Boolean>();

    public DriveController() throws GoogleOAuthLoginException {
        super();
        System.out.println("@DriveController#Construct");
    }

    /**
     * Active and valid Credential from {@link cz.muni.fi.sdipr.core.GoogleLoginService} is required to run this method.
     */
    @Override
    public void process() throws IOException {
        drive = new Drive.Builder(new NetHttpTransport(), new JacksonFactory(), googleLoginService.getCredential())
                .setApplicationName(applicationName)
                .build();
        loadFolder("root");
    }

    public List<File> getFiles() throws IOException {
        Comparator<File> comparator = new Comparator<File>() {

            @Override
            public int compare(File o1, File o2) {
                if (isFolder(o1)) {
                    return -1;
                } else if (isFolder(o2)) {
                    return 1;
                } else {
                    return o1.getMimeType().compareTo(o2.getMimeType());
                }
            }
        };

        Collections.sort(files, comparator);

        return files;
    }

    public void setScopes() {
        googleLoginService.addScope(DriveScopes.DRIVE);
    }

    public boolean isFolder(File file) {
        if (file.getMimeType().equals("application/vnd.google-apps.folder")) {
            return true;
        }
        return false;
    }

    public boolean isDownloadable(File file) {
        return file.getDownloadUrl() != null && file.getDownloadUrl().length() > 0;
    }

    public void downloadFile(File file) throws IOException, GoogleOAuthLoginException {
        if (file.getDownloadUrl() != null && file.getDownloadUrl().length() > 0) {
            FacesContext ctx = FacesContext.getCurrentInstance();
            ExternalContext ec = ctx.getExternalContext();

            ec.responseReset();
            ec.setResponseContentType(file.getMimeType());
            ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + file.getTitle() + "\"");
            OutputStream output = ec.getResponseOutputStream();
            HttpResponse resp = drive.getRequestFactory().buildGetRequest(new GenericUrl(file.getDownloadUrl()))
                    .execute();
            InputStream is = resp.getContent();
            int b;
            while ((b = is.read()) != -1) {
                output.write(b);
            }
            output.flush();
            output.close();
            ctx.responseComplete();
        } else {
            throw new GoogleOAuthLoginException("File doesn't have any content stored on Drive.");
        }
    }

    public void trashFile(String fileId) throws IOException {
        drive.files().trash(fileId).execute();
    }

    public String getParentsString(File file) throws IOException {
        StringBuffer parents = new StringBuffer();

        for (ParentReference pr : file.getParents()) {
            if (!pr.getIsRoot()) {
                File file1 = drive.files().get(pr.getId()).execute();
                parents.append(file1.getTitle() + ", ");
            }
        }

        String sParents = parents.toString();
        if (sParents.length() > 0) {
            return parents.toString().substring(0, parents.toString().length() - 2);
        }
        return "";
    }

    public void loadFolder(String folderId) throws IOException {
        files = drive.files().list().setQ("'" + folderId + "' in parents and trashed = false").execute().getItems();
    }

    public void loadStarred() throws IOException {
        files = drive.files().list().setQ("starred = true").execute().getItems();
    }

    public void loadTrash() throws IOException {
        files = drive.files().list().setQ("trashed = true").execute().getItems();
    }

    public void upload(FileUploadEvent event) throws IOException {
        UploadedFile file = event.getUploadedFile();

        File body = new File();
        body.setTitle(file.getName());

//        body.setDescription(description);
        body.setMimeType(file.getContentType());

        //todo Set the parent folder. - later we will use this
//        if (parentId != null && parentId.length() > 0) {
//            body.setParents(
//                    Arrays.asList(new ParentReference().setId(parentId)));
//        }

        // File's content.
        java.io.File fileContent = new java.io.File(file.getName());
        FileContent mediaContent = new FileContent(file.getContentType(), fileContent);
        drive.files().insert(body, mediaContent).execute();
    }

    public String getOwnersNames(File file) {
        StringBuilder sb = new StringBuilder();
        for (String name : file.getOwnerNames()) {
            sb.append(name);
            sb.append(", ");
        }
        return sb.toString().substring(0, sb.toString().length() - 2);
    }

    public boolean renderControllsPanel() {
        Set<String> indexes = this.checked.keySet();
        for (String index : indexes) {
            if (this.checked.get(index).booleanValue()) {
                return true;
            }
        }
        return false;
    }

    public void selectAllFiles(ValueChangeEvent event) {
        Boolean value = (Boolean) event.getNewValue();
        if (value) {
            for (File file : files) {
                this.checked.put(file.getId(), true);
            }
        } else {
            this.checked = new HashMap<String, Boolean>();
        }
    }

    public Map<String, Boolean> getChecked() {
        return checked;
    }

    public void setChecked(Map<String, Boolean> checked) {
        this.checked = checked;
    }
}
