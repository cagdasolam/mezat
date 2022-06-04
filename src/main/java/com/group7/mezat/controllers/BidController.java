package com.group7.mezat.controllers;


import com.group7.mezat.documents.Bid;
import com.group7.mezat.services.BidService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bid")
@AllArgsConstructor
@Component
public class BidController {
    private BidService bidService;


    @GetMapping
    public List<Bid> getAllBids(){
        return bidService.getAllBids();
    }

    @GetMapping("/bidId/{id}")
    public Bid getOneBidById(@PathVariable String id){
        return bidService.getOneBidById(id);
    }

    @GetMapping("/userId/{userId}")
    public List<Bid> getUserBids(@PathVariable String userId){return bidService.getUserBids(userId);}

    // get all bids by fishPackageId
    @GetMapping("/fishPackageId/{fishPackageId}")
    public List<Bid> getFishPackageBids(@PathVariable String fishPackageId){return bidService.getFishPackageBids(fishPackageId);}

}
