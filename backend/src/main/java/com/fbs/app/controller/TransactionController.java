package com.fbs.app.controller;

import com.fbs.app.dto.TransactionRequestDto;
import com.fbs.app.dto.TransactionResponseDto;
import com.fbs.app.model.TransactionModel;
import com.fbs.app.service.TransactionService;
import com.fbs.app.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fbs")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/add-funds")
    public ResponseEntity<String> addIncome(@RequestBody TransactionRequestDto income ){
       transactionService.addIncome(income);

       return ResponseEntity.ok("Fund Added");
    }

    @GetMapping("/add-funds")
    public List<TransactionResponseDto> getAllIncomes( @RequestHeader("Authorization") String authHeader,

                                                       @RequestParam String type ){
        String token = authHeader.startsWith("Bearer ")
                ? authHeader.substring(7)
                : authHeader;

        Claims familyDetail = jwtUtil.extractTokenDetails(token);


        String familyId=familyDetail.get("familyId").toString();

        Integer userId=Integer.parseInt(familyDetail.get("userId").toString());
        System.out.println("gvhvsjcjsdc" +userId);
        String email=familyDetail.get("sub").toString();

        List<String> roles = (List<String>) familyDetail.get("roles");
        System.out.println(roles);
        String userRole = null;
        if (roles != null && !roles.isEmpty()) {
            userRole = roles.get(0);
        }else{
            throw new RuntimeException("You are not Authorised");
        }



        return transactionService.readIncome(type,userId,familyId,userRole);


    }

    @DeleteMapping("/add-funds/{id}")
    public String delIncome(@PathVariable Long id){
        transactionService.delIncome(id);
        return "Transaction Deleted";
    }

}
