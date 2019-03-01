package projetapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import projetapi.service.ParticipantServiceProxy;

import org.springframework.http.MediaType;

@RestController
@RequestMapping(value = "/taches", produces = MediaType.APPLICATION_JSON_VALUE)
public class TacheController {

	ParticipantServiceProxy participantServiceProxy;
}
