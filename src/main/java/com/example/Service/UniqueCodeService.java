package com.example.Service;

import com.example.model.UniqueCode;

public interface UniqueCodeService {
	public UniqueCode findOneByUsername(String username);
	public UniqueCode saveNewCode(UniqueCode code);
	public boolean checkCode(UniqueCode code);
	public void removeCode(UniqueCode code);
	public boolean checkCodeAuth(UniqueCode code);
}

