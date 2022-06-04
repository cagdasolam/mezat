package com.group7.mezat.manager;


import com.group7.mezat.controllers.AuctionController;
import com.group7.mezat.controllers.UserController;
import com.group7.mezat.documents.User;
import com.group7.mezat.requests.BidRequest;
import com.group7.mezat.requests.PackageSoldRequest;
import com.group7.mezat.services.AuctionService;
import com.group7.mezat.services.BidService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auctionManager" )
@AllArgsConstructor
public class AuctionManager {

    private AuctionController auctionController;
    private AuctionService auctionService;
    private List<User> userList;
    //notification
    private BidService bidService;
    private UserController userController;



    @PostMapping("/bid")
    public void takeBid(@RequestBody BidRequest bidRequest) {
        bidService.takeBid(bidRequest);
    }

    public void nextFishPackage() {

    }

    @PutMapping("/sellPackage/{fishPackageId}")
    public void sellPackage(@PathVariable String fishPackageId, @RequestBody PackageSoldRequest packageSoldRequest) {
        auctionService.sellPackage(fishPackageId, packageSoldRequest);
    }



}
