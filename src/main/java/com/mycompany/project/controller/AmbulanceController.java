package com.mycompany.project.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mycompany.project.model.Patient;
import com.mycompany.project.service.AmbulanceService;

@Controller
@RequestMapping("/ambulance")
public class AmbulanceController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AmbulanceController.class);
	
	@Autowired
	private AmbulanceService ambulanceService;
		
	@RequestMapping("/main.do")
	public String main(Model model) {
		LOGGER.info("Ambulance main 메소드 실행");
		Patient movingPatient = ambulanceService.getPatientByPcarAssign("car");
		model.addAttribute("movingPatient", movingPatient);
		return "ambulance/main";
	}
	
	@PostMapping("movingPatientInfo.do")
	public void movingPatientInfo(HttpServletResponse response) throws IOException {
		Patient movingPatient = ambulanceService.getPatientByPcarAssign("car");
		
		JSONObject movingPatientJson = new JSONObject();
		movingPatientJson.put("pno", movingPatient.getPno());
		movingPatientJson.put("preportTime", movingPatient.getPreportTime());
		movingPatientJson.put("preportTel", movingPatient.getPreportTel());
		movingPatientJson.put("plocation", movingPatient.getPlocation());
		movingPatientJson.put("pname", movingPatient.getPname());
		movingPatientJson.put("psymptom", movingPatient.getPsymptom());
		movingPatientJson.put("psex", movingPatient.getPsex());
		movingPatientJson.put("page", movingPatient.getPage());
		movingPatientJson.put("pbloodType", movingPatient.getPbloodType());
		String json = movingPatientJson.toString();
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("movingPatient", json);
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter pw = response.getWriter();
		pw.write(jsonObject.toString());
		pw.flush();
		pw.close();
	}
	
	@PostMapping("dispatchPossible.do")
	public void dispatchPossible(HttpServletResponse response) throws IOException {
		int carRow = ambulanceService.selectCountByPcarAssign("car");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("carRow", carRow);
		PrintWriter pw = response.getWriter();
		pw.write(jsonObject.toString());
		pw.flush();
		pw.close();
	}
	
	@PostMapping("convoyEnd.do")
	@ResponseBody
	public void convoyEnd(int pno) {
		ambulanceService.drop(pno);
	}
}
