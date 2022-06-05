package com.group7.mezat.controllers;

import com.group7.mezat.documents.Auction;
import com.group7.mezat.documents.FishPackage;
import com.group7.mezat.requests.AddPackageRequest;
import com.group7.mezat.requests.AuctionUpdateRequest;
import com.group7.mezat.responses.AuctionResponse;
import com.group7.mezat.responses.ErrorResponse;
import com.group7.mezat.responses.PackageResponse;
import com.group7.mezat.services.AuctionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Queue;

@RestController
@RequestMapping("/auction")
@AllArgsConstructor
public class AuctionController {
    private AuctionService auctionService;

    @GetMapping
    public List<Auction> getAllAuction(){
        return auctionService.getAllAuction();
    }

    @GetMapping("/getSortedAuctions")
    public List<Auction> getSortedAuctions(){
        return auctionService.getSortedAuctions();
    }

    @GetMapping("/getCurrentAuction")
    public AuctionResponse getCurrentAuction(){
        return auctionService.getCurrentAuction();
    }

    @GetMapping("/getFishPackage/{Id}")
    public Queue<PackageResponse> getFishPackage(@PathVariable String Id){
        return auctionService.getFishPackage(Id);
    }


    @GetMapping("/auctionId/{id}")
    public Auction getOneAuctionById(@PathVariable String id){
        return auctionService.getOneAuctionById(id);
    }

    @PostMapping
    public ResponseEntity<ErrorResponse> addAuction(@RequestBody Auction auction) throws Exception {
        return auctionService.addAuction(auction);
    }

    @DeleteMapping("/delete/{auctionId}")
    public void deleteAuction(@PathVariable String auctionId){
        auctionService.deleteAuction(auctionId);
    }

    @PutMapping("/update/{auctionId}")
    public void updateAuction(@PathVariable String auctionId, @RequestBody AuctionUpdateRequest updateRequest){
        auctionService.updateAuction(auctionId, updateRequest);
    }

    @PutMapping("/addFish")
    public void addFishPackageToAuction(@RequestBody AddPackageRequest addFishRequest) throws Exception {
       auctionService.addFishPackageToAuction(addFishRequest);
    }

    @PutMapping("/start/{auctionId}")
    public void startAuction(@PathVariable String auctionId) throws Exception {
        auctionService.startAuction(auctionId);
    }

    @PutMapping("/cancel/{auctionId}")
    public void cancelAuction(@PathVariable String auctionId) throws Exception {
        auctionService.cancelAuction(auctionId);
    }

    @PutMapping("/end/{auctionId}")
    public void endAuction(@PathVariable String auctionId){
        auctionService.endAuction(auctionId);
    }

    @GetMapping("/getCurrentFish/{id}")
    public PackageResponse getCurrentFish(@PathVariable String id){
        System.out.println("controller");
        return auctionService.getCurrentFish(id);
    }
}
