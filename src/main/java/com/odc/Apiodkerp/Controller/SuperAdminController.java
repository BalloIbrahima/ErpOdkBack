package com.odc.Apiodkerp.Controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
@Api(value = "admin", description = "Les fonctionnalités liées à un super administrateur, le grand chef.")
@CrossOrigin
public class SuperAdminController {

}
