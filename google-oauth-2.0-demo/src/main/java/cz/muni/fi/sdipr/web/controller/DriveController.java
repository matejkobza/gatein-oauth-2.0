package cz.muni.fi.sdipr.web.controller;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import cz.muni.fi.sdipr.core.GoogleLoginBean;
import cz.muni.fi.sdipr.core.GoogleOAuthLoginException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
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

    private List<File> files;

    public DriveController() throws GoogleOAuthLoginException {
        super();
    }

    /**
     * Active and valid Credential from {@link GoogleLoginBean} is required to run this method.
     */
    @Override
    public void process() {
        Drive drive = new Drive.Builder(new NetHttpTransport(), new JacksonFactory(), googleLoginBean.getCredential())
                .setApplicationName(applicationName)
                .build();
        try {
            FileList feed = drive.files().list().execute();
            files = feed.getItems();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public void setScopes() {
        googleLoginBean.addScope(DriveScopes.DRIVE);
    }
}
