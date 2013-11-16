package cz.muni.fi.sdipr.web.controller;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import cz.muni.fi.sdipr.core.GoogleOAuthLoginException;
import cz.muni.fi.sdipr.core.interceptor.Login;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: matejkobza
 * Date: 7.11.2013
 * Time: 12:01
 */
@Named
@SessionScoped
public class DriveController extends AbstractController {

    private static final long serialVersionUID = -7611041534898427988L;
    private List<File> files;


    public DriveController() throws GoogleOAuthLoginException {
        super();
        System.out.println("@DriveController#Construct");
    }

    /**
     * Active and valid Credential from {@link cz.muni.fi.sdipr.core.GoogleLoginService} is required to run this method.
     */
    @Login
    @Override
    public void process() throws IOException {
        Drive drive = new Drive.Builder(new NetHttpTransport(), new JacksonFactory(), googleLoginService.getCredential())
                .setApplicationName(applicationName)
                .build();
        FileList feed = drive.files().list().execute();
        files = feed.getItems();
        System.out.println(files);
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public void setScopes() {
        googleLoginService.addScope(DriveScopes.DRIVE);
    }
}
