package com.example.Service;

import java.util.List;

import com.example.Security.EncryptionException;
import com.example.model.AuthProduct;
import com.example.model.UniqueCode;

public interface ListOfBearService {
	public String[] generateList (String id , int n,AuthProduct pro)throws EncryptionException;
	public boolean checkcode(AuthProduct code);
	public void remove(AuthProduct uni);
}
	