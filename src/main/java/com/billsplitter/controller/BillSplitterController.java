package com.billsplitter.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.billsplitter.model.Group;
import com.billsplitter.model.Settlement;
import com.billsplitter.service.BillSplitterService;

@Controller
public class BillSplitterController {

    @Autowired
    private BillSplitterService billSplitterService;

    // Serve the main HTML page
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // API: Calculate settlements
    @PostMapping("/api/calculate")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> calculate(@RequestBody Group group) {
        Map<String, Object> response = new HashMap<>();

        if (group.getMembers() == null || group.getMembers().isEmpty()) {
            response.put("error", "Members list cannot be empty");
            return ResponseEntity.badRequest().body(response);
        }

        List<Settlement> settlements = billSplitterService.calculateSettlements(group);
        double totalAmount = billSplitterService.getTotalAmount(group);
        Map<String, Double> personalTotals = billSplitterService.getPersonalTotals(group);

        response.put("settlements", settlements);
        response.put("totalAmount", totalAmount);
        response.put("personalTotals", personalTotals);
        response.put("perPerson", group.getMembers().size() > 0 ? totalAmount / group.getMembers().size() : 0);

        return ResponseEntity.ok(response);
    }
}
