/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluepot.pkgModel.Config;

import java.io.Serializable;

/**
 *
 * @author andrew
 */
public class Settings implements Serializable {

    private String randomNameFolder = "/dev/null";
    private boolean randomEnabled = false;
    private boolean randomDeviceClass = true;


    private int randomMinutes = 10;

    private String saveFolder = "/tmp/";
    private boolean emailEnabled = false;
    private String emailTo = "";
    private String emailFrom = "";
    private String emailSubject = "";

    private String smtpHost = "";
    private String smtpUser = "";
    private String smtpPassword = "";
    private int smtpPort = 25;

    private String respOBEXGet ="";
    private String respID = "";
    private String respPhone = "";

    public String getRespID() {
        return respID;
    }

    public void setRespID(String respID) {
        this.respID = respID;
    }

    public String getRespOBEXGet() {
        return respOBEXGet;
    }

    public void setRespOBEXGet(String respOBEXGet) {
        this.respOBEXGet = respOBEXGet;
    }

    public String getRespPhone() {
        return respPhone;
    }

    public void setRespPhone(String respPhone) {
        this.respPhone = respPhone;
    }

    public String getRespSMS() {
        return respSMS;
    }

    public void setRespSMS(String respSMS) {
        this.respSMS = respSMS;
    }
    private String respSMS = "";
    
    public int getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(int smtpPort) {
        this.smtpPort = smtpPort;
    }
    
    public boolean isEmailEnabled() {
        return emailEnabled;
    }

    public void setEmailEnabled(boolean emailEnabled) {
        this.emailEnabled = emailEnabled;
    }

    public String getEmailFrom() {
        return emailFrom;
    }

    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public String getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }

    public boolean isRandomEnabled() {
        return randomEnabled;
    }

    public void setRandomEnabled(boolean randomEnabled) {
        this.randomEnabled = randomEnabled;
    }

    public int getRandomMinutes() {
        return randomMinutes;
    }

    public void setRandomMinutes(int randomMinutes) {
        this.randomMinutes = randomMinutes;
    }

    public String getRandomNameFolder() {
        return randomNameFolder;
    }

    public void setRandomNameFolder(String randomNameFolder) {
        this.randomNameFolder = randomNameFolder;
    }

    public String getSaveFolder() {
        return saveFolder;
    }

    public void setSaveFolder(String saveFolder) {
        this.saveFolder = saveFolder;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public String getSmtpPassword() {
        return smtpPassword;
    }

    public void setSmtpPassword(String smtpPassword) {
        this.smtpPassword = smtpPassword;
    }

    public String getSmtpUser() {
    
        return smtpUser;
    }

    public void setSmtpUser(String smtpUser) {
        this.smtpUser = smtpUser;
    }

    public boolean isRandomDeviceClass() {
        return randomDeviceClass;
    }

    public void setRandomDeviceClass(boolean randomDeviceClass) {
        this.randomDeviceClass = randomDeviceClass;
    }
    

}
