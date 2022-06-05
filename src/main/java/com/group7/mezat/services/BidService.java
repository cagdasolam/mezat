package com.group7.mezat.services;

import com.group7.mezat.documents.*;
import com.group7.mezat.repos.BidRepository;
import com.group7.mezat.repos.PackageRepository;
import com.group7.mezat.requests.BidRequest;
import com.group7.mezat.requests.PackageSoldRequest;
import com.group7.mezat.responses.PackageResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BidService {
    private BidRepository bidRepository;
    private PackageService packageService;
    private AuctionService auctionService;

    public List<Bid> getAllBids() {
        return bidRepository.findAll();
    }

    public Bid getOneBidById(String id) {
        Bid bid = bidRepository.findById(id).orElse(null);
        return bid;
    }

    public List<Bid> getUserBids(String userId) {
        return bidRepository.findOneBidByBidderId(userId);
    }

    public void takeBid(BidRequest bidRequest) {
        Bid bid = new Bid();

        PackageResponse packageResponse = packageService.getCurrentFishByUser();
        String currentPackageId = packageService.getCurrentFishByUser().getId();
        System.out.println("currentPackageId: " + currentPackageId);
        Auction auction = auctionService.getOneAuctionById(packageResponse.getAuctionId());
        if (currentPackageId != null) {
            bid.setBid(bidRequest.getBid());
            bid.setBidderId(bidRequest.getBidderId());
            bid.setAuctionId(auctionService.getCurrentAuction().getId());
            bid.setFishPackageId(currentPackageId);
            FishPackage fishPackage = packageService.getFishPackageById(currentPackageId);
            List<Bid> bids = fishPackage.getBids();
            bids.add(bid);
            fishPackage.setBids(bids);

            List<FishPackage> fishPackages = auction.getFishList();

//            find fishPackage by id in fishPackages
            FishPackage foundPackage = fishPackages.stream()
                    .filter(fishPackage2 -> fishPackage2.getId().equals(currentPackageId))
                    .findFirst()
                    .orElse(null);

            System.out.println("foundPackage in bid: " + foundPackage.getId());

            if (foundPackage != null) {
                foundPackage.setBids(bids);
                auction.setFishList(fishPackages);
                auctionService.updateAuctionById(auction);
            }
            packageService.updateFishPackage(fishPackage);
            bidRepository.insert(bid);
        }
    }

    public List<Bid> getFishPackageBids(String fishPackageId) {
        return bidRepository.findAllByFishPackageId(Sort.by(Sort.Direction.DESC, "bid"),fishPackageId);
    }

    public void sellPackage(String packageId) {

        Auction auction = auctionService.getCurrentAuction().getAuction();
        List<FishPackage> fishPackageList = auction.getFishList();

        FishPackage foundPackage = fishPackageList.stream()
                .filter(fishPackage2 -> fishPackage2.getId().equals(packageId))
                .findFirst()
                .orElse(null);

        if(foundPackage != null){
            List<Bid> bidList = getFishPackageBids(packageId);
            String buyerId = bidList.get(0).getBidderId();
            foundPackage.setSoldPrice(bidList.get(0).getBid());
            foundPackage.setBuyerId(buyerId);
            foundPackage.setStatus(FishStatus.SOLD);
            foundPackage.setBidStatus(BidStatus.CLOSE);
            auction.setFishList(fishPackageList);
            packageService.updatePackageOnSale(foundPackage);
            auctionService.updateAuctionOnSale(auction);
        }
    }


}
