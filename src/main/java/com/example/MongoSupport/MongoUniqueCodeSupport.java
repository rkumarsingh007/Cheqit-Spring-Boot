package com.example.MongoSupport;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Service.UniqueCodeService;
import com.example.model.UniqueCode;
import com.example.repository.UniqueCodeRepository;

@Service
public class MongoUniqueCodeSupport implements UniqueCodeService {
	private UniqueCodeRepository uniqueCode;
	
	@Autowired
	public MongoUniqueCodeSupport(UniqueCodeRepository uniqueCode) {
		this.uniqueCode =uniqueCode;
	}

	@Override
	public UniqueCode findOneByUsername(String username) {
		return uniqueCode.findOneByUsername(username);
	}

	@Override
	public UniqueCode saveNewCode(UniqueCode code) {
		UniqueCode temp =null;
		temp=uniqueCode.findOneByUsername(code.getUsername());
		temp.setCode(code.getCode());
		Date date = new Date(System.currentTimeMillis());
		temp.setDate(date);
		uniqueCode.save(temp);
		return temp;
	}
	
	@Override
	public boolean checkCode(UniqueCode code) {
		UniqueCode u= new UniqueCode();
		u = uniqueCode.findOneByUsername(code.getUsername());
		Date date1 = new Date(System.currentTimeMillis());
		Date date2 = u.getDate();
		long delta = date1.getTime() - date2.getTime();
		if(u.getCode()!=null && u.getCode().equals(code.getCode())&& delta<=10000)
		{
			
			return true;
		}
		else
			return false;
	}
	
	

	@Override
	public void removeCode(UniqueCode code) {
		code.setCode(null);
		uniqueCode.save(code);
	}

	@Override
	public boolean checkCodeAuth(UniqueCode code) {
		UniqueCode u= new UniqueCode();
		u = uniqueCode.findOneByUsername(code.getUsername());
		Date date1 = new Date(System.currentTimeMillis());
		Date date2 = u.getDate();
		long delta = date1.getTime() - date2.getTime();
		if(u.getCode()!=null && u.getCode().equals(code.getCode())&& delta<=300000)
		{
			removeCode(code);
			return true;
		}
		else
			return false;
	}
}
