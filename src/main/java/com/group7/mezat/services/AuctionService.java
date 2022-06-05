package com.group7.mezat.services;

import com.group7.mezat.controllers.PackageController;
import com.group7.mezat.documents.*;
import com.group7.mezat.repos.AuctionRepository;
import com.group7.mezat.repos.UserRepository;
import com.group7.mezat.requests.AddPackageRequest;
import com.group7.mezat.requests.AuctionUpdateRequest;
import com.group7.mezat.requests.PackageSoldRequest;
import com.group7.mezat.responses.AuctionResponse;
import com.group7.mezat.responses.ErrorResponse;
import com.group7.mezat.responses.PackageResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.time.*;
import java.util.*;

@Service
@AllArgsConstructor
public class AuctionService {
    private AuctionRepository auctionRepository;
    private UserRepository userRepository;
    private PackageService packageService;

    public void updateAuction(String auctionId, AuctionUpdateRequest updateRequest) {
        Optional<Auction> auction = auctionRepository.findById(auctionId);
        if (auction.isPresent()){
            Auction foundAuction = auction.get();
            foundAuction.setAuctionStatus(updateRequest.getAuctionStatus());
            foundAuction.setAuctionStart(updateRequest.getAuctionStart());
            auctionRepository.save(foundAuction);
        }
    }

    public List<Auction> getAllAuction(){
        return auctionRepository.findAll();
    }

    public Auction getOneAuctionById(String id) {
        Optional<Auction> auction = auctionRepository.findById(id);
        if (auction.isPresent()){
            return auction.get();
        }
        return null;
    }

    public ResponseEntity<ErrorResponse> addAuction(Auction auction) throws Exception{
        Optional<Auction> existAuction = Optional.ofNullable(auctionRepository.findByAuctionStart(auction.getAuctionStart()));
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Istanbul"));
        Date res = Date.from(Instant.from(now));
        ErrorResponse response = new ErrorResponse();

//       if auction is before today return error response with status code 400
        if(auction.getAuctionStart().before(res)){  //auction start date is before today
            response.setMessage("Geçmiş bir tarihte bir açılış tarihi olamaz");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); //bad request
        }

//        if auction date is now return error response with status code 400
        if(auction.getAuctionStart().equals(res)){  //auction start date is today
            response.setMessage("Şu ana bir mezat tarihi olamaz");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); //bad request
        }

//        if an auction is already exist return error response with status code 400
        if(existAuction.isPresent()){
            response.setMessage("Bu tarihte zaten bir mezat var");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); //bad request
        }

        response.setMessage("Mezat başarıyla eklendi");
        auctionRepository.insert(auction);

        return new ResponseEntity<>(response, HttpStatus.OK); //ok
    }

    public void deleteAuction(String auctionId) {auctionRepository.deleteById(auctionId);}

    public void addFishPackageToAuction(AddPackageRequest packageRequest) throws Exception {
        List<Auction> auctions = auctionRepository.findAllByAuctionStatus(Sort.by(Sort.Direction.ASC, "auctionStart"), AuctionStatus.OPEN);
        if (auctions.size() > 0){
            Auction foundAuction = auctions.get(0);
            Optional<User> optionalUser  = Optional.ofNullable(userRepository.findByUserMail(packageRequest.getEmail()));
            if (optionalUser.isPresent() && packageRequest.getFishType().matches("[a-zA-Z]+")){
                User foundUser = optionalUser.get();
                FishPackage fishPackage = new FishPackage();
                fishPackage.setFishType(packageRequest.getFishType());
                fishPackage.setBasePrice(packageRequest.getBasePrice());
                fishPackage.setFishAmount(packageRequest.getFishAmount());
                fishPackage.setAuctionId(foundAuction.getId());
                fishPackage.setStatus(FishStatus.UNSOLD);
                fishPackage.setSoldPrice(0);
                fishPackage.setBuyerId(null);
                fishPackage.setSellerId(foundUser.getId());
                fishPackage.setSoldDate(null);
                fishPackage.setBidStatus(BidStatus.CLOSE);
                fishPackage.setBids(new ArrayList<>());
                packageService.addPackage(fishPackage);
                foundAuction.getFishList().add(fishPackage);
                auctionRepository.save(foundAuction);
            }else {
                throw new Exception("email bulunamadı");
            }
        }
        else{
            List<Auction> auctions2 = auctionRepository.findAllByAuctionStatus(Sort.by(Sort.Direction.ASC, "auctionStart"), AuctionStatus.STARTING);
            Auction foundAuction = auctions2.get(0);
            Optional<User> optionalUser  = Optional.ofNullable(userRepository.findByUserMail(packageRequest.getEmail()));
            if (optionalUser.isPresent() && packageRequest.getFishType().matches("[a-zA-Z]+")){
                User foundUser = optionalUser.get();
                FishPackage fishPackage = new FishPackage();
                fishPackage.setFishType(packageRequest.getFishType());
                fishPackage.setBasePrice(packageRequest.getBasePrice());
                fishPackage.setFishAmount(packageRequest.getFishAmount());
                fishPackage.setAuctionId(foundAuction.getId());
                fishPackage.setStatus(FishStatus.UNSOLD);
                fishPackage.setSoldPrice(0);
                fishPackage.setBuyerId(null);
                fishPackage.setSellerId(foundUser.getId());
                fishPackage.setSoldDate(null);
                fishPackage.setBidStatus(BidStatus.CLOSE);
                fishPackage.setBids(new ArrayList<>());
                packageService.addPackage(fishPackage);
                foundAuction.getFishList().add(fishPackage);
                auctionRepository.save(foundAuction);
            }else {
                throw new Exception("email bulunamadı");
            }
        }
    }

    public void startAuction(String auctionId) throws Exception{
        Optional<Auction> auction = auctionRepository.findById(auctionId);
        if (auction.isPresent()){
            Auction foundAuction = auction.get();
            ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Istanbul"));
            Date res = Date.from(Instant.from(now));
            if(foundAuction.getAuctionStart().before(res)){
                foundAuction.setAuctionStatus(AuctionStatus.OPEN);
                auctionRepository.save(foundAuction);
            }
            else{
                throw new Exception("error");
            }
        }
    }

    public void endAuction(String auctionId) {
        Optional<Auction> auction = auctionRepository.findById(auctionId);
        if (auction.isPresent()){
            Auction foundAuction = auction.get();
            foundAuction.setAuctionStatus(AuctionStatus.FINISHED);
            auctionRepository.save(foundAuction);
        }
    }

    public AuctionResponse getCurrentAuction() {
        List<Auction> openAuction = auctionRepository.findAllByAuctionStatus(Sort.by(Sort.Direction.ASC, "auctionStart"), AuctionStatus.OPEN);
        if(openAuction.size() > 0){
            Auction foundAuction = openAuction.get(0);
            AuctionResponse auctionResponse = new AuctionResponse(foundAuction);
            return auctionResponse;

        }
        else{
            List<Auction> auctions = auctionRepository.findAllByAuctionStatus(Sort.by(Sort.Direction.ASC, "auctionStart"), AuctionStatus.STARTING);
            Auction foundAuction = auctions.get(0);
            AuctionResponse response = new AuctionResponse(foundAuction);
            return response;
        }
    }

    public List<Auction> getSortedAuctions() {
        return auctionRepository.findAll(Sort.by(Sort.Direction.ASC, "auctionStart"));
    }

    public void cancelAuction(String auctionId) {
        Optional<Auction> auction = auctionRepository.findById(auctionId);
        if (auction.isPresent()){
            Auction foundAuction = auction.get();
            foundAuction.setAuctionStatus(AuctionStatus.CANCELLED);
            auctionRepository.save(foundAuction);
        }
    }

    public Queue<PackageResponse> getFishPackage(String Id) {
        List<FishPackage> fishPackageList;
        Optional<Auction> auction = auctionRepository.findById(Id);
        if (auction.isPresent()){
            fishPackageList = auction.get().getFishList();
//            fishPackageList.sort();
//            every fishPackage in the fishPackageList map and create a {@link PackageResponse} queue structure
            Queue<PackageResponse> packageResponseQueue = new LinkedList<>();
            int turn = 0;
            for (FishPackage fishPackage : fishPackageList) {
                PackageResponse packageResponse = new PackageResponse(fishPackage);
                packageResponse.setSellerName(userRepository.findById(fishPackage.getSellerId()).get().getName());
                packageResponse.setTurn(turn);
                List<Bid> bidList = fishPackage.getBids();
//                sort bidList according to the bid price descending order
                bidList.sort(Comparator.comparing(Bid::getBid).reversed());
//                bidList.sort(Comparator.comparing(Bid::getBid));
                packageResponse.setBids(bidList);
                System.out.println(bidList);
                turn++;
                packageResponseQueue.add(packageResponse);
            }

            return packageResponseQueue;
        }
        return null;
    }

    public void updateAuctionById(Auction auction){
        Optional<Auction> optAuction = auctionRepository.findById(auction.getId());
        if (optAuction.isPresent()){
            Auction foundAuction = optAuction.get();
            foundAuction.setFishList(auction.getFishList());
            auctionRepository.save(foundAuction);
        }
    }

    public void updateAuctionOnSale(Auction auction) {
        Optional<Auction> newAuction = auctionRepository.findById(auction.getId());
        if(newAuction.isPresent()){
            Auction foundAuction = newAuction.get();
            foundAuction.setFishList(auction.getFishList());
            auctionRepository.save(foundAuction);
        }
    }

    public PackageResponse getCurrentFish(String id) {
        Auction auction = getCurrentAuction().getAuction();
        List<FishPackage> list = auction.getFishList();

        for (FishPackage fishPackage: list
             ) {
//            sort the bidList according to the bid price descending order
            fishPackage.getBids().sort(Comparator.comparing(Bid::getBid).reversed());
            if (fishPackage.getBidStatus().equals(BidStatus.OPEN)){
                fishPackage.setBidStatus(BidStatus.CLOSE);
                packageService.updateFishPackage(fishPackage);
            }
            if (fishPackage.getId().equals(id)){
                fishPackage.setBidStatus(BidStatus.OPEN);
            }
        }
        auction.setFishList(list);
        auctionRepository.save(auction);
        return  packageService.getCurrentFish(id);
    }
}
