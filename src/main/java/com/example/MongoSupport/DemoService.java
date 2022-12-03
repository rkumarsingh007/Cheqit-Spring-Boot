package com.example.MongoSupport;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cheqit.zxing.BarcodeFormat;
import com.cheqit.zxing.EncodeHintType;
import com.cheqit.zxing.WriterException;
import com.cheqit.zxing.CheqitCode.CheqitCodeGenerator;
import com.cheqit.zxing.common.BitMatrix;
import com.cheqit.zxing.common.MatrixToImageConfig;
import com.cheqit.zxing.common.MatrixToImageWriter;
import com.cheqit.zxing.qrcode.QRCodeWriter;
import com.cheqit.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.example.Security.Config;
import com.example.Security.EncryptionException;
import com.example.Security.RSAUtil;
import com.example.exception.CompanyDoesNotExistsException;
import com.example.model.Certificate;
import com.example.model.Demo;
import com.example.model.DemoBharatrath;
import com.example.model.GenericProduct;
import com.example.repository.BharatRathRepository;
import com.example.repository.CertRepo;
import com.example.repository.DemoRepo;
import com.example.repository.GenericProductRepository;
import com.google.gson.Gson;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

@Service
public class DemoService {
	
	@Autowired
	private DemoRepo demoRepo;
	@Autowired
	private CertRepo certRepo;
	@Autowired
	private BharatRathRepository bhrepo;
	@Autowired
	private GenericProductRepository genRepo;
	
	public String generateEncyData(Demo d){
		String demoStr = null;
		try {
			Demo demo = new Demo();
			demo.setName(d.getName());
			demo.setDescription(d.getDescription());
			demo = demoRepo.save(demo);
			Map<String,Object > map = new HashMap();
			demo.setName(null);
			demo.setDescription(null);
			map.put("class","Demo");
			map.put("object", demo);
//			System.out.println(new Gson().toJson(map));	
			demoStr = RSAUtil.encrypt(new Gson().toJson(map)); //demo containing object id
			
//			demoRepo.save(listDemo);
			return demoStr;
		}catch(EncryptionException  ec) {
			ec.printStackTrace();
		}
		return null;
	}
	
	
	public String generateGenericProductQR(GenericProduct d,String companyName) {
		try {
			d = genRepo.save(d);
			Map<String, Object> map = new HashMap();
			companyName = companyName.toLowerCase();
			map.put("class", companyName);
			GenericProduct temp = new GenericProduct();
			temp.setId(d.getId());
			map.put("object", temp);
			String enc = RSAUtil.encrypt(new Gson().toJson(map));
//			System.out.println("generic : "+new Gson().toJson(map));
			BufferedImage bw = QRWithLogo(enc,400);
			String base64 = encodeToString(bw ,"PNG");
			return base64;
		}catch(EncryptionException | IOException | WriterException e) {
			e.printStackTrace();
		}
		return null;
	};
	
	public String generateBharathRathQR(DemoBharatrath d) {
		try {
			d = bhrepo.save(d);
			Map<String, Object> map = new HashMap();
			map.put("class", "BharatRath");
			DemoBharatrath temp = new DemoBharatrath();
			temp.setId(d.getId());
			map.put("object", temp);
			String enc = RSAUtil.encrypt(new Gson().toJson(map));
			BufferedImage bw = QRWithLogo(enc,400);
			String base64 = encodeToString(bw ,"PNG");
			return base64;
		}catch(EncryptionException | IOException | WriterException e) {
			e.printStackTrace();
		}
		return null;
	};
	
	private static BitMatrix createQRCode(String qrCodeData,
            String charset, Map hintMap, int qrCodeheight, int qrCodewidth)
		throws WriterException, IOException {
		BitMatrix matrix = new QRCodeWriter().encode(
		new String(qrCodeData.getBytes(charset), charset),
		BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap);
		return matrix;
		}
		
	private static BufferedImage QRWithLogo(String data,int dim) throws IOException, WriterException{
		Map<EncodeHintType,ErrorCorrectionLevel> hintMap = new HashMap<>();
		hintMap.put(
		EncodeHintType.ERROR_CORRECTION,
		ErrorCorrectionLevel.Q );
		BitMatrix b = createQRCode(data, "UTF-8", hintMap, dim, dim);
		MatrixToImageConfig config = new MatrixToImageConfig(MatrixToImageConfig.BLACK, MatrixToImageConfig.WHITE);
		// Load QR image
		BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(b, config);
		// Load logo image
		
		
		File file = new File("./datamartix.png");
		BufferedImage logoImage = ImageIO.read(file);
		
		// Calculate the delta height and width between QR code and logo
		int deltaHeight = qrImage.getHeight() - logoImage.getHeight();
		int deltaWidth = qrImage.getWidth() - logoImage.getWidth();
		// Initialize combined image
		BufferedImage combined = new BufferedImage(qrImage.getHeight(), qrImage.getWidth(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) combined.getGraphics();
		// Write QR code to new image at position 0/0
		g.drawImage(qrImage, 0, 0, null);
		
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		// Write logo into combine image at position (deltaWidth / 2) and
		// (deltaHeight / 2). Background: Left/Right and Top/Bottom must be
		// the same space for the logo to be centered
		int heightOfDM = qrImage.getHeight()/5;
        int initCo = (2*qrImage.getHeight())/5;
        g.drawImage(logoImage, initCo,initCo,heightOfDM,heightOfDM, null);
		return combined;
}



	
	public String getQRBase64(String data){
//		return base64 string Image data
//		setup the spring for opencv 
		String base64 = null;
		try {
			BufferedImage bw = CheqitCodeGenerator.createQR(data,300);	
//			BufferedImage bw = CheqitCodeGenerator.createBWQR(data,300);
			base64 = encodeToString(bw ,"PNG");
			return base64;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	public Map<String,String> getComImageData(String comName) throws CompanyDoesNotExistsException{
		comName = comName.toLowerCase();
		String baseUrl = File.separator+"home"+File.separator+"ubuntu"+File.separator+"Cheqit-Server-Spring"+File.separator+"CompanyData"+File.separator+comName+File.separator;
		String comLogoUrl = baseUrl+comName+"logo.png";
		String proImageUrl = baseUrl+comName+".png";
		String proProperties = baseUrl+comName+".properties";
		Map<String,String> map = new HashMap();
		try {
			comLogoUrl = encodeToString( ImageIO.read(new File(comLogoUrl)), "PNG");
			proImageUrl = encodeToString( ImageIO.read(new File(proImageUrl)), "PNG");
			FileReader reader=new FileReader(proProperties); 
		    Properties p=new Properties();  
		    p.load(reader);
		    String perishable = p.getProperty("perishable");
		    String companyName = p.getProperty("companyName");
		    map.put("companyLogo",comLogoUrl);
		    map.put("productImage", proImageUrl);
		    map.put("perishable", perishable);
		    map.put("companyName", companyName);
		    return map;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new CompanyDoesNotExistsException("Add Company Data in Cheqit-Server-Spring Folder", e);
		}
	}
	
	public Map<String,String> getCompanyData(String cmpName) throws CompanyDoesNotExistsException{
		try {
			Map<String,String> map = new HashMap();
			String baseUrl = File.separator+"home"+File.separator+"ubuntu"+File.separator+"Cheqit-Server-Spring"+File.separator+"CompanyData"+File.separator+cmpName+File.separator;
			String proProperties = baseUrl+cmpName+".properties";
			FileReader reader=new FileReader(proProperties); 
		    Properties p=new Properties();  
		    p.load(reader);
		    String companyName = p.getProperty("companyName");
		    String youtubeUrl = p.getProperty("youtubeUrl");
		    String info = p.getProperty("info");
		    map.put("companyName", companyName);
		    map.put("youtubeUrl", youtubeUrl);
		    map.put("info", info);
		    return map;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new CompanyDoesNotExistsException("Add Company Data in Cheqit-Server-Spring Folder", e);
		}
	}
	
	
 	public String encodeToString(BufferedImage image, String type) {
	    String imageString = null;
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();

	    try {
	        ImageIO.write(image, type, bos);
	        byte[] imageBytes = bos.toByteArray();

	        Base64.Encoder encoder = Base64.getEncoder();
	        imageString = encoder.encodeToString(imageBytes);

	        bos.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return imageString;
	}

	public Demo validateDemo(Demo d) {
		Demo demo = demoRepo.findOneById(d.getId());
//		delete the Data
		demoRepo.deleteById(d.getId());
		return demo;
		 
	}
	
	public Certificate validateCertificate(Certificate c) {
		return certRepo.findOneById(c.getId());
	}
	
	public Certificate saveCertData(Certificate cert) {
		return certRepo.save(cert);
	}
	
	public DemoBharatrath validateBharatRath(DemoBharatrath br) {
		DemoBharatrath obj= bhrepo.findOneById(br.getId());
		bhrepo.deleteOneById(br.getId());
		return obj; 
	}
	
	public GenericProduct validateGenericProduct(GenericProduct br) {
		GenericProduct obj= genRepo.findOneById(br.getId());
		genRepo.deleteOneById(br.getId());
		return obj; 
	}
	

	public DemoBharatrath saveBharatRathData(DemoBharatrath br) {
		return bhrepo.save(br);
	}
	
	//give the certificate present in the database thus it must the 'id'   ... 13 digit timestamp needed ..(in milli) 
	public byte[] generatePDF(Certificate cert) throws DocumentException, IOException {
		PdfReader reader = new PdfReader(File.separator+"home"+File.separator+"ubuntu"+File.separator+"Cheqit-Server-Spring"+File.separator+"certificate.pdf"); // input PDF
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfStamper stamper = new PdfStamper(reader,	baos);
        BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED); // set font
       
        PdfContentByte over = stamper.getOverContent(1);
        int fontSize = 10;
      //BusinessID
        over.beginText();
        over.setFontAndSize(bf, fontSize);    // set font and size
        over.setTextMatrix(100, 525);   // set x,y position (0,0 is at the bottom left)
        over.showText(cert.getBusinessID());  // set text
        over.endText();
        //Business Name 
        over.beginText();
        over.setFontAndSize(bf, fontSize);    // set font and size
        over.setTextMatrix(250  , 525 );   // set x,y position (0,0 is at the bottom left)
        over.showText(cert.getBusinessName());  // set text
        over.endText();
        //CoR No.
        over.beginText();
        over.setFontAndSize(bf, fontSize);    // set font and size
        over.setTextMatrix(100, 490);   // set x,y position (0,0 is at the bottom left)
        over.showText(cert.getCorNo());  // set text
        over.endText(); 
        //SPA No.
        over.beginText();
        over.setFontAndSize(bf, fontSize);    // set font and size
        over.setTextMatrix(280, 360);   // set x,y position (0,0 is at the bottom left)
        over.showText(cert.getSpaNo());  // set text
        over.endText();
        //BA No.
        over.beginText();
        over.setFontAndSize(bf, fontSize);    // set font and size
        over.setTextMatrix(360, 280);   // set x,y position (0,0 is at the bottom left)
        over.showText(cert.getBpa());  // set text
        over.endText();
        //Plot no.
        over.beginText();
        over.setFontAndSize(bf, fontSize);    // set font and size
        over.setTextMatrix(490, 290);   // set x,y position (0,0 is at the bottom left)
        over.showText(cert.getPlotNo()+"");  // set text
        over.endText();
        //Date Of Issue
        over.beginText();
        over.setFontAndSize(bf, fontSize);    // set font and size
        over.setTextMatrix(175, 180);   // set x,y position (0,0 is at the bottom left)
        Date d = new Date(cert.getIssueDate());
        Format format = new SimpleDateFormat("dd MMM yyyy");
        over.showText(format.format(d));  // set text
        over.endText();
        //Date Of Expiry 
        over.beginText();
        over.setFontAndSize(bf, fontSize);    // set font and size
        over.setTextMatrix(400, 180);   // set x,y position (0,0 is at the bottom left)
        d = new Date(cert.getDateExp());
        over.showText(format.format(d));  // set text
        over.endText();
        
//        if(cert.getCounty()!=null){
//            over.beginText();
//            over.setFontAndSize(bf, fontSize);    // set font and size
//            over.setTextMatrix(180, 278);   // set x,y position (0,0 is at the bottom left)
//            over.showText(cert.getCounty());  // set text
//            over.endText();
//        }
//        if(cert.getCountySub()!=null){
//            over.beginText();
//            over.setFontAndSize(bf, fontSize);    // set font and size
//            over.setTextMatrix(180, 278);   // set x,y position (0,0 is at the bottom left)
//            over.showText(cert.getCountySub());  // set text
//            over.endText();
//        }
        if(cert.getPinNo()!=null){
            over.beginText();
            over.setFontAndSize(bf, fontSize);    // set font and size
            over.setTextMatrix(250, 490);   // set x,y position (0,0 is at the bottom left)
            over.showText(cert.getPinNo());  // set text
            over.endText();
        }
        if(cert.getVatNo()!=null){
            over.beginText();
            over.setFontAndSize(bf, fontSize);    // set font and size
            over.setTextMatrix(400, 490);   // set x,y position (0,0 is at the bottom left)
            over.showText(cert.getVatNo());  // set text
            over.endText();
        }
        if(cert.getPoBox()!=null){
            over.beginText();
            over.setFontAndSize(bf, fontSize);    // set font and size
            over.setTextMatrix(100, 290);   // set x,y position (0,0 is at the bottom left)
            over.showText(cert.getPoBox());  // set text
            over.endText();
        }
        if(cert.getPostCode()!=null){
            over.beginText();
            over.setFontAndSize(bf, fontSize);    // set font and size
            over.setTextMatrix(200, 290);   // set x,y position (0,0 is at the bottom left)
            over.showText(cert.getPostCode());  // set text
            over.endText();
        }
        if(cert.getPostTown()!=null){
            over.beginText();
            over.setFontAndSize(bf, fontSize);    // set font and size
            over.setTextMatrix(280, 290);   // set x,y position (0,0 is at the bottom left)
            over.showText(cert.getPostTown());  // set text
            over.endText();
        }
        if(cert.getTelNo1()!=null){
            over.beginText();
            over.setFontAndSize(bf, fontSize);    // set font and size
            over.setTextMatrix(80, 245);   // set x,y position (0,0 is at the bottom left)
            over.showText(cert.getTelNo1());  // set text
            over.endText();
        }
        if(cert.getTelNo2()!=null){
            over.beginText();
            over.setFontAndSize(bf, fontSize);    // set font and size
            over.setTextMatrix(190, 245);   // set x,y position (0,0 is at the bottom left)
            over.showText(cert.getTelNo2());  // set text
            over.endText();
        }
        if(cert.getFax()!=null){
            over.beginText();
            over.setFontAndSize(bf, fontSize);    // set font and size
            over.setTextMatrix(290, 245);   // set x,y position (0,0 is at the bottom left)
            over.showText(cert.getFax());  // set text
            over.endText();
        }
        if(cert.getEmail()!=null){
            over.beginText();
            over.setFontAndSize(bf, fontSize);    // set font and size
            over.setTextMatrix(360, 245);   // set x,y position (0,0 is at the bottom left)
            over.showText(cert.getEmail());  // set text
            over.endText();
        }
        if(cert.getBusiActivity()!=null){
            over.beginText();
            over.setFontAndSize(bf, fontSize);    // set font and size
            over.setTextMatrix(100, 420);   // set x,y position (0,0 is at the bottom left)
            over.showText(cert.getBusiActivity());  // set text
            over.endText();
        }
        if(cert.getDetailActivity()!=null){
            over.beginText();
            over.setFontAndSize(bf, fontSize);    // set font and size
            over.setTextMatrix(330, 420);   // set x,y position (0,0 is at the bottom left)
            over.showText(cert.getDetailActivity());  // set text
            over.endText();
        }
        //now past the image of the QR code 
//        get the buffered Image    
        String enc = null;
        
        try {
        	Map<String,Object> map = new HashMap<String ,Object>();
        	map.put("class", "Certificate");
        	Map<String,Object> data= new HashMap();
        	data.put("id", cert.getId());
        	map.put("object", data);
            enc = RSAUtil.encrypt(new Gson().toJson(map));
//        	enc = new Gson().toJson(map);
            BufferedImage bi = CheqitCodeGenerator.createBWQR(enc, 200);
            ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
            ImageIO.write(bi, "png", baos2);	
            Image iTextImage = Image.getInstance(baos2.toByteArray());
            iTextImage.setAbsolutePosition(420, 620);
            iTextImage.scaleToFit(148,148);
            over.addImage(iTextImage);
        } catch ( WriterException | IOException | DocumentException | EncryptionException ex) {
            ex.printStackTrace();
        }
        stamper.close();
        return  baos.toByteArray();
	}

}
