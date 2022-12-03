package com.example.model;


import java.sql.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "certificate")
public class Certificate {

	@Id
	private String id;
    private String businessID;
    private String businessName;
    private String corNo;
    private String spaNo;
    private String plotNo;
    private String bpa;
    private long issueDate;
    private long dateExp;
    private String county;
    private String countySub;
    private String pinNo;
    private String vatNo;
    private String poBox;
    private String postCode;
    private String postTown;
    private String telNo1;
    private String telNo2;
    private String fax;
    private String email;
    private String busiActivity;
    private String detailActivity;
    
    public Certificate() {}
    //required data
    public Certificate(String businessID, String businessName, String corNo, String spaNo, String bpa, String plotNo, long issueDate, long dateExp) {
        this.businessID = businessID;
        this.businessName = businessName;
        this.corNo = corNo;
        this.spaNo = spaNo;
        this.plotNo = plotNo;
        this.issueDate = issueDate;
        this.dateExp = dateExp;
        this.bpa = bpa;
    }
    
    
	public String getBpa() {
		return bpa;
	}
	public void setBpa(String bpa) {
		this.bpa = bpa;
	}
	public String getBusiActivity() {
		return busiActivity;
	}
	public void setBusiActivity(String busiActivity) {
		this.busiActivity = busiActivity;
	}
	public String getDetailActivity() {
		return detailActivity;
	}
	public void setDetailActivity(String detailActivity) {
		this.detailActivity = detailActivity;
	}
	public String getPostTown() {
		return postTown;
	}
	public void setPostTown(String postTown) {
		this.postTown = postTown;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getCountySub() {
		return countySub;
	}
	public void setCountySub(String countySub) {
		this.countySub = countySub;
	}
	public String getPinNo() {
		return pinNo;
	}
	public void setPinNo(String pinNo) {
		this.pinNo = pinNo;
	}
	public String getVatNo() {
		return vatNo;
	}
	public void setVatNo(String vatNo) {
		this.vatNo = vatNo;
	}
	public String getPoBox() {
		return poBox;
	}
	public void setPoBox(String poBox) {
		this.poBox = poBox;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	
	public String getTelNo1() {
		return telNo1;
	}
	public void setTelNo1(String telNo1) {
		this.telNo1 = telNo1;
	}
	public String getTelNo2() {
		return telNo2;
	}
	public void setTelNo2(String telNo2) {
		this.telNo2 = telNo2;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessID() {
        return businessID;
    }

    public void setBusinessID(String businessID) {
        this.businessID = businessID;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getCorNo() {
        return corNo;
    }

    public void setCorNo(String corNo) {
        this.corNo = corNo;
    }

    public String getSpaNo() {
        return spaNo;
    }

    public void setSpaNo(String spaNo) {
        this.spaNo = spaNo;
    }


    public String getPlotNo() {
        return plotNo;
    }

    public void setPlotNo(String plotNo) {
        this.plotNo = plotNo;
    }

    public long getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(long issueDate) {
        this.issueDate = issueDate;
    }

    public long getDateExp() {
        return dateExp;
    }

    public void setDateExp(long dateExp) {
        this.dateExp = dateExp;
    }
    
	    
}
