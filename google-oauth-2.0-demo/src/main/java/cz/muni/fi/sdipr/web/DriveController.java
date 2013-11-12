package cz.muni.fi.sdipr.web;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import cz.muni.fi.sdipr.core.GoogleOAuthLoginException;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: matejkobza
 * Date: 7.11.2013
 * Time: 12:01
 */
@ViewScoped
@ManagedBean
public class DriveController extends AbstractController {

    private Credential credential;
    private List<File> files;

    public DriveController() throws GoogleOAuthLoginException {
        super();
    }

    @PostConstruct
    public void init() {
//        googleLoginBean.addScope(DriveScopes.DRIVE);
    }

    public void fetchDriveFiles() {
        Drive drive = new Drive.Builder(new NetHttpTransport(), new JacksonFactory(), credential)
                .setApplicationName(applicationName)
                .build();
        try {
            Drive.Files.List feed = drive.files().list();
            files = feed.execute().getItems();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
