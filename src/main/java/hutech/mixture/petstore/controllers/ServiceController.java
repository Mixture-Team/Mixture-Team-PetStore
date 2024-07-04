package hutech.mixture.petstore.controllers;

import hutech.mixture.petstore.Services.ServiceDetailService;
import hutech.mixture.petstore.models.CustomService;
import hutech.mixture.petstore.services.CustomServiceService;
import hutech.mixture.petstore.models.ServiceDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/dich-vu")
public class ServiceController {

    @Autowired
    private CustomServiceService CustomServiceService;
    @Autowired
    private ServiceDetailService serviceDetailService;


    @GetMapping
    public String showServiceList(Model model) {
        List<CustomService> customServices = CustomServiceService.findAllActiveCustomServices();
        model.addAttribute("customServices", customServices);
        List<ServiceDetail> serviceDetails = serviceDetailService.findAll();
        model.addAttribute("serviceDetails", serviceDetails);
        return "/service/service-list";
    }
    @GetMapping("/details/{serviceLink}")
    public String showOrderDetails(@PathVariable String serviceLink, Model model) {
        CustomService customServices = CustomServiceService.findByLink(serviceLink);
        model.addAttribute("customServices", customServices);
        return "/service/service-detail";
    }


}
