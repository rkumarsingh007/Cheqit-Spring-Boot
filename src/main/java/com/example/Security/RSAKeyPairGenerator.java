/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.Security;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.util.Base64;

/**
 *
 * @author Raj
 */
public class RSAKeyPairGenerator {

    
    private PrivateKey privateKey;
    private PublicKey publicKey;
    /**
     * @param args the command line arguments
     */
     public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
    RSAKeyPairGenerator() throws NoSuchAlgorithmException{
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1820);
        KeyPair pair = keyGen.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }
     void writeToFile(String path, byte[] key) throws IOException {
        File f = new File(path);
        f.getParentFile().mkdirs();

        FileOutputStream fos = new FileOutputStream(f);
        fos.write(key);
        fos.flush();
        fos.close();
    }
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
         RSAKeyPairGenerator keyPairGenerator = new RSAKeyPairGenerator();
        keyPairGenerator.writeToFile("RSA/publicKey", keyPairGenerator.getPublicKey().getEncoded());
        keyPairGenerator.writeToFile("RSA/privateKey", keyPairGenerator.getPrivateKey().getEncoded());
        System.out.println("public key:   "+ Base64.getEncoder().encodeToString(keyPairGenerator.getPublicKey().getEncoded()));
        System.out.println("private key:   "+Base64.getEncoder().encodeToString(keyPairGenerator.getPrivateKey().getEncoded()));
    }
    
}



//EO/+WISIfLeEfSSp2gppQ7IC40AtaWPMgKZvq2DICUsQODXEPAQgFBdNjL/AenyRVIZXlTFWne+dJhZg9L6SGUNNug0drlPrZLbUBRT1U5bfJopSQ7HbriduovRik4MEnJcdqepzjHFtkF9U9LAEdE7wNu112Dk4Tmyuwg6OQLU=
//Dhiraj is the author