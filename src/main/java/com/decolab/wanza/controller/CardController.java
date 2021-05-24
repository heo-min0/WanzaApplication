package com.decolab.wanza.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.decolab.wanza.dto.CardDTO;
import com.decolab.wanza.service.CardService;

import util.NewFileName;

@RestController
public class CardController {
	
	@Autowired
	CardService service;
	
	@Autowired
	ServletContext servletContext;
	
	
	@RequestMapping(value = "/getCardList", method = {RequestMethod.GET,RequestMethod.POST})
	public List<CardDTO> getCardList() {
		System.out.println("CardController getCardList() " + new Date());
		
		return service.getCardList();
	}
	
	
	@RequestMapping(value = "/cardWrite", method = {RequestMethod.GET,RequestMethod.POST})
	public String cardWrite(@RequestParam("cardFile")MultipartFile cardFile, CardDTO dto, HttpServletRequest req) {
		
		System.out.println("CardController cardWrite() " + new Date());
		
		/* 경로	src/main/webapp/upload 폴더를 생성할 것 */
		String uploadPath = req.getServletContext().getRealPath("/upload");
		//String uploadPath = "d:\\tmp";
		
		String filename = cardFile.getOriginalFilename();
		
		String newFilename = NewFileName.getNewFileName(filename);
		String filepath = uploadPath + File.separator + newFilename;
		
		System.out.println("filepath :" + filepath);
		dto.setCardFileName(newFilename);

		
		System.out.println("dto 들어오나 : "+dto.toString());
		
		
		try {
			BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
			os.write(cardFile.getBytes());
			os.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "file upload fail";
		}
		
		return service.cardWrite(dto)>0?"suc":"err";
	}
	

}
