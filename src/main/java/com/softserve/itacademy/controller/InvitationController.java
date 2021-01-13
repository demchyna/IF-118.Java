package com.softserve.itacademy.controller;

import com.softserve.itacademy.request.InvitationRequest;
import com.softserve.itacademy.response.InvitationResponse;
import com.softserve.itacademy.service.InvitationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/invite")
public class InvitationController {
    private final InvitationService invitationService;

    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @PostMapping
    public ResponseEntity<InvitationResponse> sendInvitation(@RequestBody InvitationRequest invitation) {
        return new ResponseEntity<>(invitationService.sendInvitation(invitation), HttpStatus.OK);
    }

    @GetMapping("/approve/{id}")
    public ResponseEntity<InvitationResponse> approveInvitation(@PathVariable Integer id) {
        invitationService.approve(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
