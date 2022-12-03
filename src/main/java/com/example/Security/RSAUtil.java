/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAUtil {

	//key added on 1 September 2020  size 1820
//    private static String publicKey = "MIIBAjANBgkqhkiG9w0BAQEFAAOB8AAwgewCgeQKcvKLnd7WpGPkGu83y69ka0WjO7fVF6TRmykoiRBWsOHrHZODDhmTth2fYIJfVjeUso3JMXtIwwntEc5Pe3ZwlqbT9gMukauaw47ujgsPUyafCOyMuF2gyZSwjC5ofzZYC6Kzna3SYD3BcpK6M5YiF0F2snTeZg6l1wNsaycGd1S7ycSrGvWCZ/kTZZs4FjyE4rD12CyNOWGhreuU0PNi9oRq96nE9atErSZ+3IV1bODL2iMiCC74S3RkQ/UebXssxhgjN9HiIn0POlz/YumbxGKens/7KQOQY9RTvlNQuRzPIqkCAwEAAQ==";
//    private static String privateKey = "MIIENAIBADANBgkqhkiG9w0BAQEFAASCBB4wggQaAgEAAoHkCnLyi53e1qRj5BrvN8uvZGtFozu31Rek0ZspKIkQVrDh6x2Tgw4Zk7Ydn2CCX1Y3lLKNyTF7SMMJ7RHOT3t2cJam0/YDLpGrmsOO7o4LD1MmnwjsjLhdoMmUsIwuaH82WAuis52t0mA9wXKSujOWIhdBdrJ03mYOpdcDbGsnBndUu8nEqxr1gmf5E2WbOBY8hOKw9dgsjTlhoa3rlNDzYvaEavepxPWrRK0mftyFdWzgy9ojIggu+Et0ZEP1Hm17LMYYIzfR4iJ9Dzpc/2Lpm8Rinp7P+ykDkGPUU75TULkczyKpAgMBAAECgeQIegPFu8bBPg9DR451JyocPuH5cELL/ihZWpRjK/4zNb+w6wd2NAQPzpftaW+aQ+lHGirn4tpuxrrQtbc2x6Pn5m9kWJlwOACMxsS3Rkg2oHAeaDDuX8qk26NiLfagE0NU3euZ5vrSRf0TaA0cPSk4csD2wkm8+bkwCPhhQ3FO4ePbRmpGVetM77uMPIe0PtzlZYwyyMemHjDgZqCSXi9FjHTys0vzCnj93iXFU4n/lb3i1gm3BayeXMMnuNE6YBoWlkOSdOysc3DfaVEpSjXUdhUMLwQExFTlZLp3CldcdW0sDzkCcjVuWRwfqzkk8SyqqqmvewFX79m6A1HFm9990gco4m3gQgUgeq4i2zHjx6OgpWUiBh8XgIYhJvHBY/p68bil39Bsq9iRYQnvfnFKof5FRF8TIwG6n6vqDuaGETpQSbgQ3PbmQdwvonNrKRp6tR8+WcRsgwJyMhBEP3N57AQSEJ9a585qLQiEeCnX/d5PUO41pJLcZSMmLrPZqzX2h48y5KCTT6PZRu8kgXgdZQBBMgvQS+IzvX79hHYzDsv42uh8jI7ZxbbCvffP7cgKEW1Y8OcjhYmVugmLVsKiGWQG891rkDo40mRjAnIbXZ8qNlYd+HvFosuyCPx5Ha7s1saodQt2lhyiKiB+oiNuOMpcseGOVdmERt0vRRPZjNH44LXvkFtCiQGQzvQo7d++FgebbRxXrh/1+UghYc3GLu1kwYZML1JXiZOYQJrLofcNbSBI+ROeSkbF78tOPT0Cch3MguMFuB+cuKf+QlzSB27wr/Dabz8j8TAp6fIgnGDy0ApicQCw6SH9QXzGe4ie7XiEZcVOmqiAw02nrH9OkB+OFBahSHjZPjIGygPcD5HY3Ae5FqHhPOTsolEaM5GLuHx4q+Cjj1rvqBfFxv2dOi85IwJyJedYmAnr2Diz/whqGM+grVM+jmnc5h3aKeIkk+yBIAAXwqYtTniTj/bV4Tj7exWgBpFtSs9Lb0jLkfsQe8qTPIwdFBFpUD0ypjyA+3jrn1Y6pgf7vZJ7Qd7Aiad1hdQlaUrh4vcIV4hpvKOJlfii8HJr";
//    2 Dec 2020, 1024 byte
    private static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCs0xW7hSz6squITA0JPyrVH5NaIVO9AojtQIO2sh2WCvFNLsHbnmnlXC5faX1kncJSaOtQjQTSlnvPNa/yBKdu20UX4E993Gv8b+ujTDSExFakgJ5K6tEqhhWH6czJDC/+hiDHF+htWrZyH9bKKkuicXc9OE+8ipE4XRFWuQJUoQIDAQAB";
    private static String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKzTFbuFLPqyq4hMDQk/KtUfk1ohU70CiO1Ag7ayHZYK8U0uwdueaeVcLl9pfWSdwlJo61CNBNKWe881r/IEp27bRRfgT33ca/xv66NMNITEVqSAnkrq0SqGFYfpzMkML/6GIMcX6G1atnIf1soqS6Jxdz04T7yKkThdEVa5AlShAgMBAAECgYBYi1liFsxBsVKy5Qv0xdlDEgdjsRl/CBJxQ5F4A++mlPqmKaOGyrXEniLgHWLyLuWFZV3Sij2DSzgppH79hybkiwhiQVW5U39ujZab3+vbfwoG/xqaxbizL7Uwt0TSibCSCUqROA+3NnO7IZHQxAdLr9/ce0EpmcGN/7MnB0ogAQJBAOypn3VkjeFffqIydJ2piK/aQvit/ynsFFbcX6z+AZPKzmpv9ijpm7me7xVgmzv7XUSvwSjUUvx8oVtpDWkQ76ECQQC68iIbABUEhR1Tc6czwZI9oUovO70tXoyvcgAfbWVZhha2eVSZkOPdf9SMsyT3mXUtDqGtf2qKBewytfN9e0UBAkAV3J10VLOejBnAlfSb09tik0D4g+o8TyiZ1YWpD1XJ90QWJP2STfIqtXnUebYdVl0JSUsIoISB+mlVbmDkwcHBAkEAlmY4pApD3ngNaNcRvGTN94EsQqXIA5kejAOmL6J+ODDVUwZ31ngnqkquQGfbpMsz5wTvc7qfht1O8llJxDkJAQJAd1jSK1ncq19HE3AE2HTJqL7hOIUJmGmTGWatiEj3mshM3oATgSLEdVZzqEpqvhWR5QJ8hrn6+E+MSJTTB5whzQ==";
    public static String getPublicKey() {
		return publicKey;
	}

	public static void setPublicKey(String publicKey) {
		RSAUtil.publicKey = publicKey;
	}

	public static String getPrivateKey() {
		return privateKey;
	}

	public static void setPrivateKey(String privateKey) {
		RSAUtil.privateKey = privateKey;
	}

	//    private static String publicKey = "MIIBAjANBgkqhkiG9w0BAQEFAAOB8AAwgewCgeQKcvKLnd7WpGPkGu83y69ka0WjO7fVF6TRmykoiRBWsOHrHZODDhmTth2fYIJfVjeUso3JMXtIwwntEc5Pe3ZwlqbT9gMukauaw47ujgsPUyafCOyMuF2gyZSwjC5ofzZYC6Kzna3SYD3BcpK6M5YiF0F2snTeZg6l1wNsaycGd1S7ycSrGvWCZ/kTZZs4FjyE4rD12CyNOWGhreuU0PNi9oRq96nE9atErSZ+3IV1bODL2iMiCC74S3RkQ/UebXssxhgjN9HiIn0POlz/YumbxGKens/7KQOQY9RTvlNQuRzPIqkCAwEAAQ==";
//    private static String privateKey = "MIIENAIBADANBgkqhkiG9w0BAQEFAASCBB4wggQaAgEAAoHkCnLyi53e1qRj5BrvN8uvZGtFozu31Rek0ZspKIkQVrDh6x2Tgw4Zk7Ydn2CCX1Y3lLKNyTF7SMMJ7RHOT3t2cJam0/YDLpGrmsOO7o4LD1MmnwjsjLhdoMmUsIwuaH82WAuis52t0mA9wXKSujOWIhdBdrJ03mYOpdcDbGsnBndUu8nEqxr1gmf5E2WbOBY8hOKw9dgsjTlhoa3rlNDzYvaEavepxPWrRK0mftyFdWzgy9ojIggu+Et0ZEP1Hm17LMYYIzfR4iJ9Dzpc/2Lpm8Rinp7P+ykDkGPUU75TULkczyKpAgMBAAECgeQIegPFu8bBPg9DR451JyocPuH5cELL/ihZWpRjK/4zNb+w6wd2NAQPzpftaW+aQ+lHGirn4tpuxrrQtbc2x6Pn5m9kWJlwOACMxsS3Rkg2oHAeaDDuX8qk26NiLfagE0NU3euZ5vrSRf0TaA0cPSk4csD2wkm8+bkwCPhhQ3FO4ePbRmpGVetM77uMPIe0PtzlZYwyyMemHjDgZqCSXi9FjHTys0vzCnj93iXFU4n/lb3i1gm3BayeXMMnuNE6YBoWlkOSdOysc3DfaVEpSjXUdhUMLwQExFTlZLp3CldcdW0sDzkCcjVuWRwfqzkk8SyqqqmvewFX79m6A1HFm9990gco4m3gQgUgeq4i2zHjx6OgpWUiBh8XgIYhJvHBY/p68bil39Bsq9iRYQnvfnFKof5FRF8TIwG6n6vqDuaGETpQSbgQ3PbmQdwvonNrKRp6tR8+WcRsgwJyMhBEP3N57AQSEJ9a585qLQiEeCnX/d5PUO41pJLcZSMmLrPZqzX2h48y5KCTT6PZRu8kgXgdZQBBMgvQS+IzvX79hHYzDsv42uh8jI7ZxbbCvffP7cgKEW1Y8OcjhYmVugmLVsKiGWQG891rkDo40mRjAnIbXZ8qNlYd+HvFosuyCPx5Ha7s1saodQt2lhyiKiB+oiNuOMpcseGOVdmERt0vRRPZjNH44LXvkFtCiQGQzvQo7d++FgebbRxXrh/1+UghYc3GLu1kwYZML1JXiZOYQJrLofcNbSBI+ROeSkbF78tOPT0Cch3MguMFuB+cuKf+QlzSB27wr/Dabz8j8TAp6fIgnGDy0ApicQCw6SH9QXzGe4ie7XiEZcVOmqiAw02nrH9OkB+OFBahSHjZPjIGygPcD5HY3Ae5FqHhPOTsolEaM5GLuHx4q+Cjj1rvqBfFxv2dOi85IwJyJedYmAnr2Diz/whqGM+grVM+jmnc5h3aKeIkk+yBIAAXwqYtTniTj/bV4Tj7exWgBpFtSs9Lb0jLkfsQe8qTPIwdFBFpUD0ypjyA+3jrn1Y6pgf7vZJ7Qd7Aiad1hdQlaUrh4vcIV4hpvKOJlfii8HJr";
    static PublicKey getPublicKey(String base64PublicKey) throws EncryptionException{
        PublicKey publicKey = null;
        try{
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(base64PublicKey.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptionException(e.getMessage());
        } catch (InvalidKeySpecException e) {
            throw new EncryptionException(e.getMessage());
        }
    }

    static PrivateKey getPrivateKey(String base64PrivateKey){
        PrivateKey privateKey = null;
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(base64PrivateKey.getBytes()));
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
            privateKey = keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return privateKey;
    }

    public static String encrypt(String data) throws EncryptionException {//throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        Cipher cipher;
        String ans = null;
		try {
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
			ans = Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
			return ans;
		}
		catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			throw new EncryptionException(e.getMessage()); 
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			throw new EncryptionException(e.getMessage()); 
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			throw new EncryptionException(e.getMessage()); 
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			throw new EncryptionException(e.getMessage()); 
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			throw new EncryptionException(e.getMessage()); 
		}
        
        
    }

    public static String decrypt(byte[] data) throws EncryptionException  {// throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        String ans  = null;
    	try {
	    	Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	        cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKey));
	        ans = new String(cipher.doFinal(data));
	        return ans;
	    }
    	catch (NoSuchAlgorithmException e) {
            throw new EncryptionException(e.getMessage());
        } catch (NoSuchPaddingException e) {
            throw new EncryptionException(e.getMessage());
        } catch (InvalidKeyException e) {
            throw new EncryptionException(e.getMessage());
        } catch (IllegalBlockSizeException e) {
            throw new EncryptionException(e.getMessage());
		} catch (BadPaddingException e) {
            throw new EncryptionException(e.getMessage());
		}
    }

    public static String decrypt(String data) throws Exception {//throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        return decrypt(Base64.getDecoder().decode(data.getBytes()));
    }

//    public static void main(String[] args) throws Exception {
//        try {
//            RSAKeyPairGenerator keyPairGenerator = new RSAKeyPairGenerator();
//            publicKey = Base64.getEncoder().encodeToString(keyPairGenerator.getPublicKey().getEncoded());
//            privateKey = Base64.getEncoder().encodeToString(keyPairGenerator.getPrivateKey().getEncoded());
//            String data="{\"dealerId\":\"5d947cb2fa2ea41e909a29fa\",\"code\":\"157168415862944752028799\","
//                    + "\"pid\":\"5d9ac3aecf3a6dd2f0cac0e9\"}";
//            String encryptedString = encrypt(data);
//            System.out.println(encryptedString+"\n"+encryptedString.length());
//            String decryptedString = RSAUtil.decrypt(encryptedString);
//            System.out.println(decryptedString);
//        } catch (NoSuchAlgorithmException e) {
//            System.err.println(e.getMessage());
//        }
//
//    }
}

//j4hKB2q9I5FNfYDX8TxBbOWRsaZLTHZlPK0Tx3+ENO6MTIQRGPVAwyL2R8z41CioaGqjyiJgAA1Y7JFL08x7JLnxr4fCGQSFwWOqYAY8GDdKCUHbaHkwze29PWxQAVv9Pf61HKTxvZxbwTD7N9NqFDxuD+aanHUNkXV2hUMih+0=
//{"dealerId":"5d947cb2fa2ea41e909a29fa","code":"157168415862944752028799","pid":"5d9ac3aecf3a6dd2f0cac0e9"}

//Cpqv75s9rwp1y2hDoyyKm/ck6VTx6ynhrv3I3cGQPfds+j6QEEIo06DQUHnk3kLqjYLO4FjQRQDdqsxL8D5n/e9pc8TvG5Ev75Lv2rKTTdtrSD2MJPpetD107TIyeldf5lqwym4ofd+1ZCYdu/XQeLREwuKVyNLsO5nBhhkNXPW20h88QT9qXzaJlMuUb0TF/BhPYYwpa5arNSWJ5s7QnoMANmf56IGL7fLHXFTpllmY4qYZOPtVU2yNjopg50F6Bh+PGsL89ijqTsNMwjRJWZ51Ii7dMykkdmOFDPzgrDS4zLSz6FkMbsVZdLnJZ1F7TWWmHg0Wg9aGihkpavb7qw==





