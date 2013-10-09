package cz.muni.fi.sdipr.web;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import cz.muni.fi.sdipr.core.GoogleLoginBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

/**
 * @author matejkobza
 */
@Named
@SessionScoped
public class GoogleDriveBean implements Serializable {

    private static final long serialVersionUID = 7781881562444309512L;
    private static Log log = LogFactory.getLog(GooglePlusBean.class);
//    private List<File> files = null;
//    private Drive drive = null;

    protected GoogleCredential credential;

    @Inject
    protected GoogleLoginBean loginBean;

    public GoogleDriveBean() {
        System.out.println("@PostConstruct");
        System.out.println("GoogleDriveBean");
//        loginBean.addScopes(Arrays.asList(DriveScopes.DRIVE));
    }

//    public void loadDrive() {
//        if (credential == null) {
//            credential = loginBean.getCredential();
//        }
//
//        if (credential == null) {
//            loginBean.requestAccessToken();
//        }
//
//        if (credential != null) {
//            HttpTransport httpTransport = new NetHttpTransport();
//            JacksonFactory jsonFactory = new JacksonFactory();
//
//            drive = new Drive.Builder(httpTransport, jsonFactory, credential)
//                    .setApplicationName(APPLICATION_NAME)
//                    .build();
//        }
//
//    }

    public void loadFiles() {
//        if (drive == null) {
//            this.loadDrive();
//        }
//        try {
//            try {
//                files = drive.files().list().execute().getItems();
//            } catch (GoogleJsonResponseException ex) {
//                log.info("User is not authenticated to Google Drive", ex);
//            }
//        } catch (IOException ex) {
//            log.error(this + ".loadFiles", ex);
//        }
    }

//    public List<File> getFiles() {
//        if (drive == null) {
//            this.loadDrive();
//        }
//
//        if (drive == null) {
//            return null;
//        } else {
//
//            if (files == null) {
//                loadFiles();
//            }
//
//            if (files != null) {
//                for (File file : files) {
//                    System.out.println(file.getMimeType());
//                }
//            }
//
//            return files;
//        }
//    }

//    @Override
//    public Boolean getIsAuthenticated() {
//        if (this.drive == null) {
//            this.loadDrive();
//        }
//        if (drive != null) {
//            try {
//                try {
//                    drive.about().get().execute();
//                    return true;
//                } catch (HttpResponseException ex) {
//                    if (ex.getStatusCode() == 401) {
//                        log.error("Credentials have been revoked", ex);
//                        return false;
//                    }
//                }
//            } catch (IOException ex) {
//                log.error(this + ".getIsAuthenticated", ex);
//                return false;
//            }
//        }
//        return false;
//    }
//
//    public void downloadFile(File file) {
//        System.out.println("downloadFile: " + file.getTitle());
//        System.out.println("webContentLink:\n" + file.getWebContentLink());
//
//        System.out.println("Title: " + file.getTitle());
//        System.out.println("Description: " + file.getDescription());
//        System.out.println("MIME type: " + file.getMimeType());
//    }
}
