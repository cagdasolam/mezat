package com.group7.mezat.repos;

import com.group7.mezat.documents.BidStatus;
import com.group7.mezat.documents.FishPackage;
import com.group7.mezat.documents.FishStatus;
import com.group7.mezat.responses.PackageResponse;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PackageRepository extends MongoRepository<FishPackage, String> {
    List<FishPackage> findAllByBuyerId(String buyerId);
    List<FishPackage> findAllByStatus(FishStatus status);

    FishPackage findByBidStatus(BidStatus status);
}
