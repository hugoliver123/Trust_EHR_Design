package com.audit;

import com.util.AES;

public class AuditLog {
    String combinedmess;
    String AESkey;
    int patientID;
    String patientname;
    public AuditLog(String timestampvar, int userIDvar, String userIPvar,
                    String actiontypevar, String EHRIDvar, int patientIDvar, String patientnamevar)
    {
        this.AESkey = AES.generateRandomKey_aes128();
        String mess = timestampvar + "||" + Integer.toString(userIDvar) + "||" + userIPvar + "||" +
                actiontypevar + "||" + EHRIDvar ;
        this.combinedmess = AES.aes_encrypt(mess,AESkey);
        this.patientID = patientIDvar;
        this.patientname = patientnamevar;
    }
}
